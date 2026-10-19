package org.oplearn.project.facade;

import org.oplearn.project.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentFacadeService {
  PaymentResponse detail(Long id);

  List<PaymentResponse> findByOrderId(Long orderId);
}
