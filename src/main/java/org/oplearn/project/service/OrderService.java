package org.oplearn.project.service;

import org.oplearn.project.dto.request.OrderFilterRequest;
import org.oplearn.project.dto.request.OrderRequest;
import org.oplearn.project.dto.response.OrderItemResponse;
import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.enums.OrderStatus;

import java.util.List;

public interface OrderService {
  OrderResponse create(OrderRequest request);

  OrderResponse detail(Long id);

  void updateStatus(Long id, OrderStatus status);

  PageResponse<OrderResponse> findByUserIdAndStatus(OrderFilterRequest request);

  List<OrderItemResponse> findItemsByOrderIds(List<Long> orderIds);

  boolean hasUserPurchasedProductAndCompleted(Long userId, Long productId);
}
