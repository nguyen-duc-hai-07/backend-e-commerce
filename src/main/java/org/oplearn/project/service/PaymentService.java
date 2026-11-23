package org.oplearn.project.service;

import org.oplearn.project.dto.request.PaymentRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.PaymentResponse;
import org.oplearn.project.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {
  PageResponse<PaymentResponse> findByStatus(PaymentStatus status , int page , int size);

  PaymentResponse detail(Long id);

  List<PaymentResponse> findByOrderId(Long orderId);

  Long findUserIdByPaymentId(Long paymentId);

  void create(PaymentRequest request);

  void updateStatusByOrderId(Long orderId, PaymentStatus status);
}
