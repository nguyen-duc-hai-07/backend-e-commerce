package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.client.SepayProperties;
import org.oplearn.project.dto.request.SepayWebhookRequest;
import org.oplearn.project.dto.response.SepayWebhookResponse;
import org.oplearn.project.entity.Order;
import org.oplearn.project.entity.Payment;
import org.oplearn.project.enums.OrderStatus;
import org.oplearn.project.enums.PaymentStatus;
import org.oplearn.project.repository.OrderRepository;
import org.oplearn.project.repository.PaymentRepository;
import org.oplearn.project.service.SepayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class SepayServiceImpl implements SepayService {
  private final SepayProperties sepayProperties;
  private final PaymentRepository paymentRepository;
  private final OrderRepository orderRepository;

  private static final Pattern ORDER_CODE_PATTERN = Pattern.compile("HAZI[A-Z0-9]{16}", Pattern.CASE_INSENSITIVE);

  @Override
  @Transactional
  public SepayWebhookResponse handleWebhook(SepayWebhookRequest request , String authHeader) {
    if(!isValidToken(authHeader)) {
      log.warn("Invalid token");
      return SepayWebhookResponse.error("Invalid token");
    }

    if(!"in".equalsIgnoreCase(request.getTransferType())) {
      log.info("(processWebhook) Ignored 'out' transfer");
      return SepayWebhookResponse.ok("Ignored out transfer");
    }

    String transactionCode = (request.getReferenceCode() != null && !request.getReferenceCode().isBlank())
      ? request.getReferenceCode()
      : String.valueOf(request.getId());

    //kiểm tra mã giao dịch đã tồn tại hay chưa
    if(paymentRepository.existsByTransactionCodeAndIsDeletedFalse(transactionCode)) {
      log.info("(processWebhook) Transaction {} already processed", transactionCode);
      return SepayWebhookResponse.ok("Already processed");
    }

    String orderCode = extractOrderCode(request.getContent());
    Order order = orderRepository.findByOrderCodeAndIsDeletedFalse(orderCode)
      .orElse(null);


    if (order == null) {
      log.warn("(processWebhook) Order not found for content: {}", request.getContent());
      return SepayWebhookResponse.error("Order not found");
    }

    if(order.getStatus() != OrderStatus.PENDING_PAYMENT) {
      log.warn("(processWebhook) Order status is not PENDING_PAYMENT");
      return SepayWebhookResponse.ok("Order status is not PENDING_PAYMENT");
    }

    if (request.getTransferAmount() == null || order.getTotalAmount() == null
      || request.getTransferAmount().compareTo(order.getTotalAmount()) < 0) {
      log.warn("(processWebhook) Insufficient amount or invalid amount data");
      return SepayWebhookResponse.error("Insufficient amount");
    }

    Payment payment = paymentRepository.findByOrderIdAndIsDeletedFalse(order.getId())
      .orElse(null);

    if (payment == null) {
      log.warn("(handleWebhook) Payment not found for orderId: {}", order.getId());
      return SepayWebhookResponse.error("Payment not found");
    }

    payment.setStatus(PaymentStatus.SUCCESS);
    payment.setTransactionCode(transactionCode);
    paymentRepository.save(payment);

    order.setStatus(OrderStatus.PAID);
    orderRepository.save(order);

    return SepayWebhookResponse.ok("Payment confirmed successfully");
  }

  private boolean isValidToken(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Apikey ")) {
      return false;
    }

    String token = authHeader.substring(7).trim();
    return token.equals(sepayProperties.getWebhookToken());
  }

  private String extractOrderCode(String content) {
    if (content == null || content.isBlank()) {
      return null;
    }

    Matcher matcher = ORDER_CODE_PATTERN.matcher(content);
    if (matcher.find()) {
      return matcher.group();
    }

    return content.trim();
  }
}
