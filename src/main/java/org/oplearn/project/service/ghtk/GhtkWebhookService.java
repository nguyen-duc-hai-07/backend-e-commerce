package org.oplearn.project.service.ghtk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.ghtk.GhtkWebhookRequest;
import org.oplearn.project.dto.ghtk.GhtkWebhookResponse;
import org.oplearn.project.entity.Order;
import org.oplearn.project.enums.OrderStatus;
import org.oplearn.project.facade.OrderFacadeService;
import org.oplearn.project.repository.OrderRepository;
import org.oplearn.project.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GhtkWebhookService {

  private final OrderRepository orderRepository;
  private final OrderFacadeService orderFacadeService;
  private final OrderService orderService;

  @Transactional
  public GhtkWebhookResponse handleWebhook(GhtkWebhookRequest payload) {
    if (payload == null) {
      log.warn("[GHTK Webhook] Payload rỗng");
      return GhtkWebhookResponse.ok();
    }

    log.info("🔔 [GHTK Webhook] Nhận tín hiệu: partnerId={}, labelId={}, statusId={}, reason={}",
        payload.getPartnerId(), payload.getLabelId(), payload.getStatusId(), payload.getReason());

    // 1. Tìm đơn hàng qua mã đơn của Shop (partnerId) hoặc mã vận đơn (labelId)
    Order order = null;
    if (payload.getPartnerId() != null && !payload.getPartnerId().isBlank()) {
      order = orderRepository.findByOrderCodeAndIsDeletedFalse(payload.getPartnerId()).orElse(null);
    }
    if (order == null && payload.getLabelId() != null && !payload.getLabelId().isBlank()) {
      order = orderRepository.findByTrackingCodeAndIsDeletedFalse(payload.getLabelId()).orElse(null);
    }

    if (order == null) {
      log.warn("[GHTK Webhook] Không tìm thấy đơn hàng trong hệ thống: partnerId={}, labelId={}",
          payload.getPartnerId(), payload.getLabelId());
      return GhtkWebhookResponse.ok();
    }

    // 2. Phân loại mã trạng thái bưu tá GHTK
    try {
      if (payload.getStatusId() != null) {
        switch (payload.getStatusId()) {

          // --- NHÓM 1: BƯU TÁ ĐÃ LẤY HÀNG TẠI SHOP -> CHUYỂN SANG SHIPPING ---
          case 3 -> {
            log.info("[GHTK Webhook] Đơn {} bưu tá đã lấy hàng thành công -> Chuyển SHIPPING", order.getOrderCode());
            orderFacadeService.shipping(order.getId());
          }

          // --- NHÓM 2: BƯU TÁ ĐANG TRÊN ĐƯỜNG ĐI GIAO ĐẾN KHÁCH ---
          case 4 -> {
            log.info("[GHTK Webhook] Đơn {} bưu tá đang trên đường đi giao đến khách", order.getOrderCode());
          }

          // --- NHÓM 3: GIAO HÀNG THÀNH CÔNG -> CHUYỂN DELIVERED + THU TIỀN COD ---
          case 5 -> {
            log.info("[GHTK Webhook] Đơn {} đã giao hàng thành công -> Chuyển DELIVERED", order.getOrderCode());
            orderFacadeService.delivered(order.getId());
          }

          // --- NHÓM 4: CHẬM LẤY HÀNG (Mưa bão, bưu cục quá tải...) ---
          case 8 -> {
            log.warn("[GHTK Webhook] Đơn {} chậm lấy hàng. Lý do: {}", order.getOrderCode(), payload.getReason());
          }

          // --- NHÓM 5: HOÃN GIAO HÀNG (Khách không nghe máy, hẹn lại ca sau...) ---
          case 9, 10 -> {
            log.warn("[GHTK Webhook] Đơn {} tạm hoãn giao hàng. Lý do: {}", order.getOrderCode(), payload.getReason());
          }

          // --- NHÓM 6: KHÔNG GIAO ĐƯỢC / BOM HÀNG -> ĐANG CHUYỂN HOÀN VỀ SHOP ---
          case 11, 12, 13 -> {
            log.warn("[GHTK Webhook] Đơn {} chuyển hoàn về Shop. Lý do: {}", order.getOrderCode(), payload.getReason());
            if (order.getStatus() != OrderStatus.RETURN_REQUESTED && order.getStatus() != OrderStatus.CANCELLED) {
              orderService.updateStatus(order.getId(), OrderStatus.RETURN_REQUESTED);
            }
          }

          // --- NHÓM 7: ĐƠN VẬN CHUYỂN ĐÃ BỊ HỦY BÊN GHTK ---
          case -1 -> {
            log.info("[GHTK Webhook] Đơn {} bị hủy bên GHTK -> Đồng bộ sang CANCELLED", order.getOrderCode());
            orderFacadeService.cancelByWebhook(order.getId());
          }

          // --- CÁC MÃ TRUNG CHUYỂN BƯU CỤC (1, 2, 123, 127, 128...) ---
          default -> {
            log.info("[GHTK Webhook] Đơn {} cập nhật lộ trình: statusId={}, reason={}",
                order.getOrderCode(), payload.getStatusId(), payload.getReason());
          }
        }
      }
    } catch (Exception ex) {
      log.error("[GHTK Webhook] Lỗi khi xử lý trạng thái đơn {}: {}", order.getOrderCode(), ex.getMessage(), ex);
    }

    // 3. Luôn phản hồi HTTP 200 { success: true } để GHTK biết máy chủ đã tiếp nhận
    return GhtkWebhookResponse.ok();
  }
}
