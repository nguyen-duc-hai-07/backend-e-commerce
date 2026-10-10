package org.oplearn.project.service;

import org.oplearn.project.dto.request.CalculateShippingFeeRequest;
import org.oplearn.project.dto.response.ShippingFeeResponse;

public interface ShippingService {

  ShippingFeeResponse calculateFee(CalculateShippingFeeRequest request);
}
