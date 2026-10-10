package org.oplearn.project.service;

import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PaymentQrResponse;

public interface PaymentQrService {
  PaymentQrResponse generateQrCode(OrderResponse orderResponse);
}
