package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.PaymentRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.PaymentResponse;
import org.oplearn.project.entity.Payment;
import org.oplearn.project.enums.PaymentStatus;
import org.oplearn.project.exception.PaymentNotFoundException;
import org.oplearn.project.exception.UserNotFoundException;
import org.oplearn.project.repository.PaymentRepository;
import org.oplearn.project.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
  private final PaymentRepository repository;
  @Override
  public PageResponse<PaymentResponse> findByStatus(PaymentStatus status, int page, int size) {
    log.info("(service) findByStatus");

    Pageable pageable = PageRequest.of(page, size);

    Page<PaymentResponse> responses = repository.findByStatus(status, pageable);

    return PageResponse.of(responses);
  }

  @Override
  public PaymentResponse detail(Long id) {
    return repository.findByIdAndReturnResponse(id)
      .orElseThrow(PaymentNotFoundException::new);
  }

  @Override
  public List<PaymentResponse> findByOrderId(Long orderId) {
    log.info("(service) findByOrderId: {}", orderId);

    return repository.findByOrderId(orderId);
  }

  @Override
  public Long findUserIdByPaymentId(Long paymentId) {
    return repository.findUserIdByPaymentId(paymentId)
      .orElseThrow(UserNotFoundException::new);
  }

  @Override
  @Transactional
  public void create(PaymentRequest request) {
    log.info("(service) create");

    Payment payment = Payment.builder()
      .orderId(request.getOrderId())
      .paymentMethodId(request.getPaymentMethodId())
      .amount(request.getAmount())
      .status(PaymentStatus.PENDING)
      .transactionCode(request.getTransactionCode())
      .build();

    repository.save(payment);
  }

  @Override
  @Transactional
  public void updateStatusByOrderId(Long orderId, PaymentStatus status) {
    log.info("(service) updateStatusByOrderId: {}, status: {}", orderId, status);
    repository.findByOrderIdAndIsDeletedFalse(orderId)
      .ifPresent(payment -> {
        if (payment.getStatus() == PaymentStatus.PENDING) {
          payment.setStatus(status);
          repository.save(payment);
        }
      });
  }
}
