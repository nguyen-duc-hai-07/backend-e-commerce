package org.oplearn.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.ghtk.GhtkCancelResponse;
import org.oplearn.project.dto.ghtk.GhtkOrderResponse;
import org.oplearn.project.dto.ghtk.GhtkTrackingResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.service.ghtk.GhtkOrderLabelService;
import org.oplearn.project.service.ghtk.GhtkOrderService;
import org.oplearn.project.service.ghtk.GhtkOrderTrackingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/shipping/ghtk")
public class GhtkController {

  private final GhtkOrderService ghtkOrderService;
  private final GhtkOrderLabelService ghtkOrderLabelService;
  private final GhtkOrderTrackingService ghtkOrderTrackingService;

  @PostMapping("/orders/{id}")
  public ResponseGeneral<GhtkOrderResponse> createOrder(@PathVariable("id") Long id) {
    log.info("(createOrder) Bắt đầu tạo đơn GHTK cho orderId: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, ghtkOrderService.createShippingOrder(id));
  }

  @GetMapping("/orders/{id}/label")
  public ResponseEntity<byte[]> printLabel(
      @PathVariable("id") Long id,
      @RequestParam(value = "pageSize", defaultValue = "A6") String pageSize
  ) {
    log.info("(printLabel) In tem vận đơn cho orderId: {}, pageSize: {}", id, pageSize);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_PDF)
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"label-order-" + id + ".pdf\"")
        .body(ghtkOrderLabelService.printOrderLabel(id, pageSize));
  }

  @GetMapping("/orders/{id}/tracking")
  public ResponseGeneral<GhtkTrackingResponse> getTracking(@PathVariable("id") Long id) {
    log.info("(getTracking) Tra cứu hành trình cho orderId: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, ghtkOrderTrackingService.getTrackingByOrderId(id));
  }

  /**
   * API Yêu cầu hủy đơn vận chuyển bên GHTK
   *
   * @param id ID đơn hàng trong hệ thống
   * @return ResponseGeneral chứa kết quả phản hồi từ GHTK
   */
  @PostMapping("/orders/{id}/cancel")
  public ResponseGeneral<GhtkCancelResponse> cancelOrder(@PathVariable("id") Long id) {
    log.info("(cancelOrder) Bắt đầu hủy vận đơn GHTK cho orderId: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, ghtkOrderService.cancelShippingOrder(id));
  }
}
