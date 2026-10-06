package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.CalculateShippingFeeRequest;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.ShippingFeeResponse;
import org.oplearn.project.service.ShippingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/shipping")
public class ShippingController {

  private final ShippingService shippingService;

  @PostMapping("/calculate-fee")
  public ResponseGeneral<ShippingFeeResponse> calculateFee(
      @Valid @RequestBody CalculateShippingFeeRequest request
  ) {
    log.info("(calculateFee) request: {}", request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, shippingService.calculateFee(request));
  }
}
