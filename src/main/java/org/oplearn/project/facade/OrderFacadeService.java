package org.oplearn.project.facade;

import org.oplearn.project.dto.request.OrderFilterRequest;
import org.oplearn.project.dto.request.OrderRequest;
import org.oplearn.project.dto.response.OrderPreviewResponse;
import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.enums.OrderStatus;

public interface OrderFacadeService {
  OrderResponse create(OrderRequest request);

  OrderPreviewResponse preView(OrderRequest request);

  OrderResponse detail(Long id);

  PageResponse<OrderResponse> findByUserIdAndStatus(OrderFilterRequest request);

  void updateStatus(Long id, OrderStatus status);
}
