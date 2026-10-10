package org.oplearn.project.facade;

import org.oplearn.project.dto.request.OrderCreateRequest;
import org.oplearn.project.dto.request.OrderFilterRequest;
import org.oplearn.project.dto.request.OrderRequest;
import org.oplearn.project.dto.response.OrderPreviewResponse;
import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PageResponse;

public interface OrderFacadeService {
  OrderResponse create(OrderCreateRequest request);

  OrderPreviewResponse preView(OrderRequest request);

  OrderResponse detail(Long id);

  PageResponse<OrderResponse> findByUserIdAndStatus(OrderFilterRequest request);

  void complete(Long id);

  void cancel(Long id);

  void autoCompleteDeliveredOrders();

  void autoCancelExpiredPaymentOrders();
}
