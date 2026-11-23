package org.oplearn.project.service.ghtk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.client.GhtkClient;
import org.oplearn.project.entity.Order;
import org.oplearn.project.exception.OrderNotFoundException;
import org.oplearn.project.exception.base.BadRequestException;
import org.oplearn.project.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GhtkOrderLabelService {

  private final OrderRepository orderRepository;
  private final GhtkClient ghtkClient;

  public byte[] printOrderLabel(Long orderId, String pageSize) {
    log.info("(printOrderLabel) Lấy tem in cho orderId: {}, pageSize: {}", orderId, pageSize);

    Order order = orderRepository.findByIdAndIsDeletedFalse(orderId)
        .orElseThrow(OrderNotFoundException::new);

    if (order.getTrackingCode() == null || order.getTrackingCode().isBlank()) {
      log.warn("(printOrderLabel) Đơn hàng {} chưa có mã vận đơn", orderId);
      throw new BadRequestException("Đơn hàng chưa được tạo vận đơn bên GHTK, vui lòng bấm Gửi Hàng trước!");
    }

    return ghtkClient.printLabel(order.getTrackingCode(), pageSize);
  }
}
