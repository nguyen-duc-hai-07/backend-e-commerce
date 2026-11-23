package org.oplearn.project.service.ghtk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.client.GhtkClient;
import org.oplearn.project.dto.ghtk.GhtkTrackingResponse;
import org.oplearn.project.entity.Order;
import org.oplearn.project.exception.OrderNotFoundException;
import org.oplearn.project.exception.base.BadRequestException;
import org.oplearn.project.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GhtkOrderTrackingService {

  private final OrderRepository orderRepository;
  private final GhtkClient ghtkClient;

  public GhtkTrackingResponse getTrackingByOrderId(Long orderId) {
    log.info("(getTrackingByOrderId) Tra cứu lộ trình cho orderId: {}", orderId);

    Order order = orderRepository.findByIdAndIsDeletedFalse(orderId)
        .orElseThrow(OrderNotFoundException::new);

    if (order.getTrackingCode() == null || order.getTrackingCode().isBlank()) {
      log.warn("(getTrackingByOrderId) Đơn hàng {} chưa có mã vận đơn", orderId);
      throw new BadRequestException("Đơn hàng chưa được tạo vận đơn bên GHTK, chưa có lộ trình vận chuyển!");
    }

    return ghtkClient.getTracking(order.getTrackingCode());
  }
}
