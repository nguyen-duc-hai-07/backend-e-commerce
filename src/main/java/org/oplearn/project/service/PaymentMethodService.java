package org.oplearn.project.service;

import org.oplearn.project.dto.request.PaymentMethodRequest;
import org.oplearn.project.dto.response.PaymentMethodResponse;

import java.util.List;

public interface PaymentMethodService {

  PaymentMethodResponse create(PaymentMethodRequest request);

  PaymentMethodResponse update(PaymentMethodRequest request, Long id);

  PaymentMethodResponse detail(Long id);

  void delete(Long id);

  List<PaymentMethodResponse> findAll();

  void checkPaymentMethodExist(Long id);
}
