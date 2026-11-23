package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.OrderFilterRequest;
import org.oplearn.project.dto.request.OrderRequest;
import org.oplearn.project.dto.response.OrderPreviewResponse;
import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.enums.OrderStatus;
import org.oplearn.project.facade.OrderFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.CREATED_MESSAGE;
import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final OrderFacadeService facade;

  @PostMapping("/preview")
  public ResponseGeneral<OrderPreviewResponse> preview(@Valid @RequestBody OrderRequest request) {
    log.info("(preview) request: {}", request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.preView(request));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<OrderResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.detail(id));
  }

  @PostMapping("/filter")
  public ResponseGeneral<PageResponse<OrderResponse>> findByUserIdAndStatus(
      @Valid @RequestBody OrderFilterRequest request
  ) {
    log.info("(findByUserIdAndStatus) request: {}", request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.findByUserIdAndStatus(request));
  }

  @PatchMapping("/{id}/status")
  public ResponseGeneral<Void> updateStatus(
      @PathVariable("id") Long id,
      @RequestParam("status") OrderStatus status
  ) {
    log.info("(updateStatus) id: {}, status: {}", id, status);
    facade.updateStatus(id, status);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }
}
