package org.oplearn.project.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.AddressResponse;
import org.oplearn.project.dto.response.OrderItemResponse;
import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PaymentMethodResponse;
import org.oplearn.project.dto.response.PaymentQrResponse;
import org.oplearn.project.event.order.OrderCompletedEvent;
import org.oplearn.project.event.order.OrderCreatedEvent;
import org.oplearn.project.repository.UserRepository;
import org.oplearn.project.service.AddressService;
import org.oplearn.project.service.EmailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEmailEventListener {

  private final EmailService emailService;
  private final AddressService addressService;
  private final UserRepository userRepository;

  @Async("mailTaskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
  public void handleOrderCreated(OrderCreatedEvent event) {
    if (event.recipientEmail() == null || event.recipientEmail().isBlank()) {
      log.info("(handleOrderCreated) recipientEmail is empty, skip sending email");
      return;
    }

    OrderResponse orderResponse = event.orderResponse();
    log.info("(handleOrderCreated) sending order confirmation email for order: {}", orderResponse.getOrderCode());

    try {
      Map<String, Object> variables = buildOrderCreatedEmailVariables(orderResponse);

      emailService.sendHtmlEmail(
          event.recipientEmail(),
          "Xác nhận đơn hàng " + orderResponse.getOrderCode() + " đặt thành công",
          "mail/order-created-email",
          variables
      );
    } catch (Exception e) {
      log.error("(handleOrderCreated) failed to send email for order: {}", orderResponse.getOrderCode(), e);
    }
  }

  @Async("mailTaskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
  public void handleOrderCompleted(OrderCompletedEvent event) {
    log.info("(handleOrderCompleted) received order completed event for order: {}", event.orderCode());

    userRepository.findByIdAndIsDeletedFalse(event.userId()).ifPresent(user -> {
      if (user.getEmail() == null || user.getEmail().isBlank()) {
        log.info("(handleOrderCompleted) user {} has no email, skip sending completion email", user.getId());
        return;
      }

      try {
        String recipientName = StringUtils.hasText(user.getFullName()) ? user.getFullName() : user.getUsername();
        Map<String, Object> variables = new HashMap<>();
        variables.put("recipientName", recipientName);
        variables.put("orderCode", event.orderCode());
        variables.put("completedTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        variables.put("totalAmount", event.totalAmount());

        emailService.sendHtmlEmail(
            user.getEmail(),
            "Đơn hàng " + event.orderCode() + " đã hoàn tất - Cảm ơn bạn! 🎉",
            "mail/order-completed-email",
            variables
        );
      } catch (Exception e) {
        log.error("(handleOrderCompleted) failed to send order completed email for order: {}", event.orderCode(), e);
      }
    });
  }

  private Map<String, Object> buildOrderCreatedEmailVariables(OrderResponse orderResponse) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("recipientName", orderResponse.getRecipientName());
    variables.put("recipientPhone", orderResponse.getRecipientPhone());
    variables.put("orderCode", orderResponse.getOrderCode());
    variables.put("orderDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    variables.put("orderNote", orderResponse.getNote());

    PaymentMethodResponse paymentMethod = orderResponse.getPaymentMethod();
    if (paymentMethod != null) {
      variables.put("paymentMethodName", paymentMethod.getName());
      variables.put("orderStatusName", "COD".equalsIgnoreCase(paymentMethod.getCode()) ? "Đã xác nhận" : "Chờ thanh toán");
    }

    AddressResponse addressResponse = orderResponse.getAddressId() != null
        ? addressService.detail(orderResponse.getAddressId())
        : null;
    String shippingAddress = addressResponse != null ? addressResponse.getStreetAddress() : null;
    variables.put("shippingAddress", shippingAddress);

    PaymentQrResponse paymentQrResponse = orderResponse.getPaymentQr();
    if (paymentQrResponse != null) {
      variables.put("paymentQrUrl", paymentQrResponse.getQrCodeUrl());
      variables.put("bankName", paymentQrResponse.getBankName());
      variables.put("accountNumber", paymentQrResponse.getAccountNumber());
      variables.put("accountName", paymentQrResponse.getAccountName());
      variables.put("transferContent", paymentQrResponse.getTransferContent());
    }

    BigDecimal subtotal = orderResponse.getItems() != null
        ? orderResponse.getItems().stream()
        .map(OrderItemResponse::getTotalPrice)
        .filter(Objects::nonNull)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        : BigDecimal.ZERO;

    variables.put("items", orderResponse.getItems());
    variables.put("subtotal", subtotal);
    variables.put("shippingFee", orderResponse.getShippingFee());
    variables.put("discountAmount", orderResponse.getDiscountAmount());
    variables.put("totalAmount", orderResponse.getTotalAmount());

    return variables;
  }
}
