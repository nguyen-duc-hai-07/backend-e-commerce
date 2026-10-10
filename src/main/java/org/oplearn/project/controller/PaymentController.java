package org.oplearn.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.PaymentResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.enums.PaymentStatus;
import org.oplearn.project.facade.PaymentFacadeService;
import org.oplearn.project.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.PARAM_PAGE;
import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.PARAM_SIZE;
import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.PARAM_STATUS;
import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.MAX_PAGE_SIZE;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.PAGE_DEFAULT;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.SIZE_DEFAULT;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

  private final PaymentFacadeService facade;
  private final PaymentService service;

  @GetMapping("/{id}")
  public ResponseGeneral<PaymentResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.detail(id));
  }

  @GetMapping("/order/{orderId}")
  public ResponseGeneral<List<PaymentResponse>> findByOrderId(@PathVariable("orderId") Long orderId) {
    log.info("(findByOrderId) orderId: {}", orderId);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.findByOrderId(orderId));
  }

  @GetMapping
  public ResponseGeneral<PageResponse<PaymentResponse>> findByStatus(
      @RequestParam(name = PARAM_STATUS) PaymentStatus status,
      @RequestParam(name = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page,
      @RequestParam(name = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size
  ) {
    log.info("(findByStatus) status: {}, page: {}, size: {}", status, page, size);
    size = Math.min(size, MAX_PAGE_SIZE);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.findByStatus(status, page, size));
  }
}
