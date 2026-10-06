package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.CalculateShippingFeeRequest;
import org.oplearn.project.dto.request.OrderFilterRequest;
import org.oplearn.project.dto.request.OrderItemRequest;
import org.oplearn.project.dto.request.OrderRequest;
import org.oplearn.project.dto.response.*;
import org.oplearn.project.entity.User;
import org.oplearn.project.enums.OrderStatus;
import org.oplearn.project.exception.InvalidOrderStatusException;
import org.oplearn.project.exception.ProductOutOfStockException;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.OrderFacadeService;
import org.oplearn.project.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderFacadeServiceImpl implements OrderFacadeService {
  private final OrderService orderService;
  private final UserService userService;
  private final AddressService addressService;
  private final ShippingService shippingService;
  private final ProductVariantService productVariantService;
  private final ProductService productService;
  private final CartItemService cartItemService;

  private User currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUsernameOrThrow(authentication.getName());
  }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getAuthorities().stream()
      .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
  }


  @Override
  @Transactional
  public OrderResponse create(OrderRequest request) {
    log.info("(facade) create order request: {}", request);
    OrderPreviewResponse preview = this.preView(request);

    OrderResponse orderResponse = orderService.create(request);

    for(OrderItemRequest item : request.getItems()) {
      productVariantService.decreaseQuantity(item.getVariantId(), item.getQuantity());
    }

    if (request.getCartItemIds() != null && !request.getCartItemIds().isEmpty()) {
      cartItemService.deleteByIdsAndUserId(request.getCartItemIds(), preview.getUserId());
    }

    setOrderResponse(orderResponse, preview);

    return orderResponse;
  }

  private void setOrderResponse(OrderResponse orderResponse, OrderPreviewResponse preview) {
    orderResponse.setRecipientName(preview.getRecipientName());
    orderResponse.setRecipientPhone(preview.getRecipientPhone());

    if (orderResponse.getItems() != null && preview.getItems() != null) {
      for (int i = 0; i < orderResponse.getItems().size() && i < preview.getItems().size(); i++) {
        preview.getItems().get(i).setId(orderResponse.getItems().get(i).getId());
        preview.getItems().get(i).setOrderId(orderResponse.getId());
      }
    }
    orderResponse.setItems(preview.getItems());
  }

  @Override
  public OrderPreviewResponse preView(OrderRequest request) {
    log.info("(facade) preView order request: {}", request);

    User currentUser = currentUser();
    request.setUserId(currentUser.getId());

    AddressResponse addressResponse;
    if (request.getAddressId() == null) {
      addressResponse = addressService.getDefaultAddress(currentUser.getId());
      request.setAddressId(addressResponse.getId());
    } else {
      addressResponse = addressService.detail(request.getAddressId());

      if (!currentUser.getId().equals(addressResponse.getUserId())) {
        log.warn("(facade) User {} tried to access address {} belonging to User {}",
          currentUser.getId(), request.getAddressId(), addressResponse.getUserId());
        throw new UserUnauthorizedException();
      }
    }

    int totalWeight = 0;
    BigDecimal subtotal = BigDecimal.ZERO;
    List<OrderItemResponse> orderItems = new ArrayList<>();
    for(OrderItemRequest item : request.getItems()) {
      ProductVariantResponse variant = productVariantService.detail(item.getVariantId());

      if(variant.getQuantity() < item.getQuantity()) {
        log.warn("(createOrder) Variant {} not enough stock: requested {}, available {}",
          variant.getId(), item.getQuantity(), variant.getQuantity());
        throw new ProductOutOfStockException();
      }

      item.setUnitPrice(variant.getPrice());

      Integer itemWeight = (variant.getWeight() != null && variant.getWeight() > 0)
        ? variant.getWeight()
        : 200;

      totalWeight += itemWeight * item.getQuantity();

      BigDecimal itemPrice = variant.getPrice() != null ? variant.getPrice() : BigDecimal.ZERO;
      subtotal = subtotal.add(itemPrice.multiply(BigDecimal.valueOf(item.getQuantity())));

      ProductResponse product = productService.detail(variant.getProductId());
      orderItems.add(OrderItemResponse.of(item, variant, product));
    }

    List<CalculateShippingFeeRequest.ShippingItemRequest> shippingItems = request.getItems() == null
      ? List.of()
      : request.getItems().stream()
      .map(item -> CalculateShippingFeeRequest.ShippingItemRequest.builder()
        .variantId(item.getVariantId())
        .quantity(item.getQuantity())
        .build())
      .toList();

    CalculateShippingFeeRequest shippingFeeRequest = CalculateShippingFeeRequest.builder()
      .addressId(addressResponse.getId())
      .totalWeight(totalWeight)
      .items(shippingItems)
      .build();

    ShippingFeeResponse shippingFeeResponse = shippingService.calculateFee(shippingFeeRequest);
    BigDecimal shippingFee = shippingFeeResponse.getShippingFee();
    request.setShippingFee(shippingFee);


    BigDecimal discountAmount = BigDecimal.ZERO;
    // set tạm discountAmount = 0 trước , mai sau xử lí bảng voucher thì tính sau
    request.setDiscountAmount(discountAmount);

    BigDecimal totalAmount = subtotal.subtract(discountAmount).add(shippingFee);
    request.setTotalAmount(totalAmount);

    return OrderPreviewResponse.of(
      request,
      addressResponse,
      orderItems,
      subtotal,
      totalWeight
    );
  }

  @Override
  public OrderResponse detail(Long id) {
    log.info("(facade) detail order id: {}", id);

    User currentUser = currentUser();

    OrderResponse orderResponse = orderService.detail(id);

    if (!isAdmin() && !currentUser.getId().equals(orderResponse.getUserId())) {
      log.warn("(detail) user not authorized");
      throw new UserUnauthorizedException();
    }

    AddressResponse addressResponse = addressService.detail(orderResponse.getAddressId());
    orderResponse.setRecipientName(addressResponse.getRecipientName());
    orderResponse.setRecipientPhone(addressResponse.getPhoneNumber());
    enrichOrderItems(orderResponse.getItems());

    return orderResponse;
  }

  @Override
  public PageResponse<OrderResponse> findByUserIdAndStatus(OrderFilterRequest request) {
    log.info("(facade) findByUserIdAndStatus request: {}", request);

    User currentUser = currentUser();
    request.setUserId(currentUser.getId());
    if(!isAdmin() && !currentUser.getId().equals(request.getUserId())) {
      log.warn("(findByUserIdAndStatus) user not authorized");
      throw new UserUnauthorizedException();
    }

    PageResponse<OrderResponse> responses = orderService.findByUserIdAndStatus(request);

    List<Long> orderIds = responses.getContent().stream()
        .map(OrderResponse::getId)
        .toList();

    List<OrderItemResponse> allItems = orderService.findItemsByOrderIds(orderIds);
    enrichOrderItems(allItems);

    Map<Long, List<OrderItemResponse>> itemsByOrderId = allItems.stream()
        .collect(Collectors.groupingBy(OrderItemResponse::getOrderId));

    for (OrderResponse order : responses.getContent()) {
      order.setItems(itemsByOrderId.getOrDefault(order.getId(), List.of()));
    }

    return responses;
  }

  @Override
  @Transactional
  public void updateStatus(Long id, OrderStatus status) {
    log.info("(facade) updateStatus id: {}, status: {}", id, status);

    OrderResponse currentOrder = this.detail(id);

    switch (status) {
      case PENDING_PAYMENT -> {
        this.pendingPayment(currentOrder);
      }
      case PAID -> {
        this.paid(currentOrder);
      }
      case SHIPPING -> {
        this.shipping(currentOrder);
      }
      case DELIVERED -> {
        this.delivered(currentOrder);
      }
      case COMPLETED -> {
        this.completed(currentOrder);
      }
      case CANCELLED -> {
        this.cancelled(currentOrder);
      }
      case RETURN_REQUESTED -> {
        this.returnRequested(currentOrder);
      }
      case REFUNDED -> {
        this.refund(currentOrder);
      }
    }

    orderService.updateStatus(id, status);
  }

  private void pendingPayment(OrderResponse currentOrder) {
    if (currentOrder.getStatus() != null && currentOrder.getStatus() != OrderStatus.PENDING_PAYMENT) {
      throw new InvalidOrderStatusException();
    }
  }

  private void paid(OrderResponse currentOrder) {
    if (currentOrder.getStatus() == OrderStatus.PAID) {
      return;
    }

    if (currentOrder.getStatus() != OrderStatus.PENDING_PAYMENT) {
      throw new InvalidOrderStatusException();
    }

    //xây nhánh payment thì sẽ kiểm tra phương thức COD hay ONLINE
  }

  private void shipping(OrderResponse currentOrder) {
    if (currentOrder.getStatus() != OrderStatus.PAID
      && currentOrder.getStatus() != OrderStatus.PENDING_PAYMENT) {
      throw new InvalidOrderStatusException();
    }
  }

  private void delivered(OrderResponse currentOrder) {
    if (currentOrder.getStatus() != OrderStatus.SHIPPING) {
      throw new InvalidOrderStatusException();
    }
  }

  private void completed(OrderResponse currentOrder) {
    // 1 tuần sau khi giao xong (trong 1 tuần đó khách hàng có thể yêu câu hoàn, đổi, trả)

    if (currentOrder.getStatus() == OrderStatus.COMPLETED) {
      return;
    }

    //đơn hàng sẽ cập nhập COMPLETED khi RETURN_REQUESTED bị admin từ chối
    if (currentOrder.getStatus() != OrderStatus.DELIVERED
      && currentOrder.getStatus() != OrderStatus.RETURN_REQUESTED) {
      throw new InvalidOrderStatusException();
    }

    if (currentOrder.getItems() == null || currentOrder.getItems().isEmpty()) {
      return;
    }

    Map<Long , Integer> productSold = currentOrder.getItems().stream()
      .filter(item -> item.getProductId() != null && item.getQuantity() != null)
      .collect(
        Collectors.groupingBy(
          OrderItemResponse::getProductId,
          Collectors.summingInt(OrderItemResponse::getQuantity)
        )
      );

    productSold.forEach(productService::increaseSoldCount);
  }

  private void returnRequested(OrderResponse currentOrder) {
    if (currentOrder.getStatus() != OrderStatus.DELIVERED
      && currentOrder.getStatus() != OrderStatus.COMPLETED) {
      throw new InvalidOrderStatusException();
    }
  }

  private void refund(OrderResponse currentOrder) {
    if (currentOrder.getStatus() != OrderStatus.RETURN_REQUESTED
      && currentOrder.getStatus() != OrderStatus.CANCELLED) {
      throw new InvalidOrderStatusException();
    }
  }

  private void cancelled(OrderResponse currentOrder) {
    if (currentOrder.getStatus() != OrderStatus.PENDING_PAYMENT
      && currentOrder.getStatus() != OrderStatus.PAID) {
      log.warn("(cancelled) can not cancelled order status: {}", currentOrder.getStatus());
      throw new InvalidOrderStatusException();
    }

    if (currentOrder.getItems() == null || currentOrder.getItems().isEmpty()) {
      return;
    }

    Map<Long , Integer> variantStock = currentOrder.getItems().stream()
      .filter(item -> item.getVariantId() != null && item.getQuantity() != null)
      .collect(
        Collectors.groupingBy(
          OrderItemResponse::getVariantId,
          Collectors.summingInt(OrderItemResponse::getQuantity)
        )
      );

    variantStock.forEach(productVariantService::increaseQuantity);
  }

  private void enrichOrderItems(List<OrderItemResponse> items) {
    if (items == null || items.isEmpty()) {
      return;
    }
    for (OrderItemResponse item : items) {
      if (item.getVariantId() != null) {
        ProductVariantResponse variant = productVariantService.detail(item.getVariantId());
        ProductResponse product = productService.detail(variant.getProductId());
        OrderItemResponse.of(item, variant, product);
      }
    }
  }
}
