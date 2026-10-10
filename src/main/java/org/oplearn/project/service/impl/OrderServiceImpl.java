package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.OrderCreateRequest;
import org.oplearn.project.dto.request.OrderFilterRequest;
import org.oplearn.project.dto.response.OrderItemResponse;
import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.entity.Order;
import org.oplearn.project.entity.OrderItem;
import org.oplearn.project.enums.OrderStatus;
import org.oplearn.project.exception.OrderNotFoundException;
import org.oplearn.project.repository.OrderItemRepository;
import org.oplearn.project.repository.OrderRepository;
import org.oplearn.project.service.OrderService;
import org.oplearn.project.utils.OrderCodeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository repository;
  private final OrderItemRepository orderItemRepository;

  @Override
  @Transactional
  public OrderResponse create(OrderCreateRequest request) {
    log.info("(create) order request: {}", request);

    Order order = Order.builder()
      .orderCode(OrderCodeUtils.generateOrderCode())
      .userId(request.getUserId())
      .addressId(request.getAddressId())
      .note(request.getNote())
      .shippingFee(request.getShippingFee())
      .discountAmount(request.getDiscountAmount())
      .totalAmount(request.getTotalAmount())
      .status(request.getStatus() != null ? request.getStatus() : OrderStatus.PENDING_PAYMENT)
      .build();

    Order savedOrder = repository.save(order);

    List<OrderItem> orderItems = request.getItems().stream()
      .map(item -> OrderItem.builder()
        .orderId(savedOrder.getId())
        .variantId(item.getVariantId())
        .quantity(item.getQuantity())
        .unitPrice(item.getUnitPrice())
        .build())
      .toList();

    List<OrderItem> savedItems = orderItemRepository.saveAll(orderItems);

    List<OrderItemResponse> itemResponses = savedItems.stream()
      .map(OrderItemResponse::from)
      .toList();

    return OrderResponse.from(savedOrder, itemResponses);
  }

  @Override
  public OrderResponse detail(Long id) {
    log.info("(detail) order id: {}", id);

    Order order = repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(OrderNotFoundException::new);

    List<OrderItemResponse> itemResponses = orderItemRepository.findAllByOrderIdAndIsDeletedFalse(id).stream()
      .map(OrderItemResponse::from)
      .toList();

    return OrderResponse.from(order, itemResponses);
  }

  @Override
  @Transactional
  public void updateStatus(Long id, OrderStatus status) {
    log.info("update status with id = {} , status = {}", id , status);

    Order order = repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(OrderNotFoundException::new);

    order.setStatus(status);
    repository.save(order);
  }

  @Override
  public PageResponse<OrderResponse> findByUserIdAndStatus(OrderFilterRequest request) {
    log.info("(findByUserIdAndStatus) request: {}", request);

    int page = (request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
    int size = (request.getSize() != null && request.getSize() > 0) ? request.getSize() : 10;
    Pageable pageable = PageRequest.of(page, size);

    Page<OrderResponse> responses = repository.findByStatusAndUserId(
        request.getStatus(),
        request.getUserId(),
        pageable
    );

    return PageResponse.of(responses);
  }

  @Override
  public List<OrderItemResponse> findItemsByOrderIds(List<Long> orderIds) {
    log.info("(findItemsByOrderIds) orderIds: {}", orderIds);
    if (orderIds == null || orderIds.isEmpty()) {
      return List.of();
    }
    return orderItemRepository.findAllByOrderIdInAndIsDeletedFalse(orderIds).stream()
        .map(OrderItemResponse::from)
        .toList();
  }

  public boolean hasUserPurchasedProductAndCompleted(Long userId, Long productId) {
    return repository.hasUserPurchasedProductAndCompleted(userId, productId);
  }

  @Override
  public List<OrderResponse> findByStatusAndUpdatedAtBefore(OrderStatus status, Instant cutoffTime) {
    return repository.findByStatusAndUpdatedAtBefore(status, cutoffTime).stream()
      .map(OrderResponse::from)
      .toList();
  }

  @Override
  public List<OrderResponse> findExpiredPendingOrders(OrderStatus status, Instant cutoffTime) {
    return repository.findExpiredPendingOrders(status , cutoffTime).stream()
      .map(OrderResponse::from)
      .toList();
  }
}
