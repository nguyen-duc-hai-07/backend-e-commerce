package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.*;
import org.oplearn.project.dto.response.*;
import org.oplearn.project.entity.User;
import org.oplearn.project.enums.OrderStatus;
import org.oplearn.project.enums.PaymentStatus;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
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
  private final PaymentMethodService paymentMethodService;
  private final PaymentQrService paymentQrService;
  private final PaymentService paymentService;
  private final EmailService emailService;

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
  public OrderResponse create(OrderCreateRequest request) {
    log.info("(facade) create order request: {}", request);
    User currentUser = currentUser();
    OrderPreviewResponse preview = this.preView(request);

    PaymentMethodResponse paymentMethod = paymentMethodService.detail(request.getPaymentMethodId());

    OrderStatus initialStatus = "COD".equalsIgnoreCase(paymentMethod.getCode())
      ? OrderStatus.CONFIRMED
      : OrderStatus.PENDING_PAYMENT;

    request.setStatus(initialStatus);

    OrderResponse orderResponse = orderService.create(request);

    for(OrderItemRequest item : request.getItems()) {
      productVariantService.decreaseQuantity(item.getVariantId(), item.getQuantity());
    }

    if (request.getCartItemIds() != null && !request.getCartItemIds().isEmpty()) {
      cartItemService.deleteByIdsAndUserId(request.getCartItemIds(), preview.getUserId());
    }

    setOrderResponse(orderResponse, preview);
    orderResponse.setPaymentMethod(paymentMethod);

    PaymentQrResponse paymentQrResponse = paymentQrService.generateQrCode(orderResponse);
    orderResponse.setPaymentQr(paymentQrResponse);

    PaymentRequest paymentRequest = PaymentRequest.builder()
      .orderId(orderResponse.getId())
      .paymentMethodId(paymentMethod.getId())
      .amount(orderResponse.getTotalAmount())
      .transactionCode(null) // cứ để tạm null trước (nếu dạng COD giữ nguyên, online cập nhập ở SepayService)
      .build();
    paymentService.create(paymentRequest);

    sendOrderCreatedEmail(orderResponse, currentUser);

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
  public void complete(Long id) {
    log.info("(facade) complete order id: {}", id);

    User currentUser = currentUser();
    OrderResponse order = this.detail(id);

    if(!currentUser.getId().equals(order.getUserId()) && !isAdmin()) {
      log.warn("(complete) user not authorized");
      throw new UserUnauthorizedException();
    }

    if(order.getStatus() != OrderStatus.DELIVERED) {
      log.warn("(complete) order status not delivered");
      throw new InvalidOrderStatusException();
    }

    if (order.getItems() != null && !order.getItems().isEmpty()) {
      Map<Long, Integer> productSold = order.getItems().stream()
        .filter(item -> item.getProductId() != null && item.getQuantity() != null)
        .collect(Collectors.groupingBy(
          OrderItemResponse::getProductId,
          Collectors.summingInt(OrderItemResponse::getQuantity)
        ));
      productSold.forEach(productService::increaseSoldCount);
    }

    orderService.updateStatus(id, OrderStatus.COMPLETED);
  }

  @Override
  @Transactional
  public void cancel(Long id) {
    log.info("(facade) cancel order id: {}", id);

    User currentUser = currentUser();
    OrderResponse currentOrder = this.detail(id);

    if (!isAdmin() && !currentUser.getId().equals(currentOrder.getUserId())) {
      throw new UserUnauthorizedException();
    }

    executeCancel(currentOrder);
  }

  private void executeCancel(OrderResponse currentOrder) {
    if (currentOrder.getStatus() != OrderStatus.PENDING_PAYMENT
      && currentOrder.getStatus() != OrderStatus.CONFIRMED) {
      log.warn("(cancel) cannot cancel order with status: {}", currentOrder.getStatus());
      throw new InvalidOrderStatusException();
    }

    if (currentOrder.getItems() != null && !currentOrder.getItems().isEmpty()) {
      Map<Long, Integer> variantStock = currentOrder.getItems().stream()
        .filter(item -> item.getVariantId() != null && item.getQuantity() != null)
        .collect(Collectors.groupingBy(
          OrderItemResponse::getVariantId,
          Collectors.summingInt(OrderItemResponse::getQuantity)
        ));
      variantStock.forEach(productVariantService::increaseQuantity);
    }

    paymentService.updateStatusByOrderId(currentOrder.getId(), PaymentStatus.FAILED);

    orderService.updateStatus(currentOrder.getId(), OrderStatus.CANCELLED);
  }

  @Override
  public void autoCompleteDeliveredOrders() {
    log.info("(facade) autoCompleteDeliveredOrders");

    Instant sevenDaysAgo = Instant.now().minus(7, ChronoUnit.DAYS);

    List<OrderResponse> orders = orderService.findByStatusAndUpdatedAtBefore(OrderStatus.DELIVERED, sevenDaysAgo);

    if(orders.isEmpty()) {
      log.info("(facade) no orders to auto complete");
      return;
    }

    List<Long> orderIds = orders.stream()
      .map(OrderResponse::getId)
      .toList();
    List<OrderItemResponse> allItems = orderService.findItemsByOrderIds(orderIds);

    Map<Long, Integer> productSold = allItems.stream()
      .filter(item -> item.getProductId() != null && item.getQuantity() != null)
      .collect(Collectors.groupingBy(
        OrderItemResponse::getProductId,
        Collectors.summingInt(OrderItemResponse::getQuantity)
      ));

    productSold.forEach(productService::increaseSoldCount);

    for(OrderResponse order : orders) {
      orderService.updateStatus(order.getId(), OrderStatus.COMPLETED);
    }
  }

  @Override
  @Transactional
  public void autoCancelExpiredPaymentOrders() {
    log.info("(facade) autoCancelExpiredPaymentOrders");

    Instant fifteenMinutesAgo = Instant.now().minus(15, ChronoUnit.MINUTES);

    List<OrderResponse> expiredOrders = orderService.findExpiredPendingOrders(OrderStatus.PENDING_PAYMENT, fifteenMinutesAgo);

    if(expiredOrders.isEmpty()) {
      log.info("(facade) no orders to auto cancel");
      return;
    }

    for (OrderResponse order : expiredOrders) {
      try {
        executeCancel(order);
        log.info("(autoCancel) successfully cancelled expired order: {}", order.getOrderCode());
      } catch (Exception e) {
        log.error("(autoCancel) failed to cancel order: {}", order.getId(), e);
      }
    }
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

  private void sendOrderCreatedEmail(OrderResponse orderResponse, User currentUser) {
    if (currentUser.getEmail() == null || currentUser.getEmail().isBlank()) {
      return;
    }

    try {
      Map<String, Object> variables = buildOrderCreatedEmailVariables(orderResponse);

      emailService.sendHtmlEmail(
        currentUser.getEmail(),
        "Xác nhận đơn hàng " + orderResponse.getOrderCode() + " đặt thành công",
        "mail/order-created-email",
        variables
      );
    } catch (Exception e) {
      log.error("(sendOrderCreatedEmail) failed to send email for order: {}", orderResponse.getOrderCode(), e);
    }
  }

  private Map<String, Object> buildOrderCreatedEmailVariables(OrderResponse orderResponse) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("recipientName", orderResponse.getRecipientName());
    variables.put("recipientPhone", orderResponse.getRecipientPhone());
    variables.put("orderCode", orderResponse.getOrderCode());
    variables.put("orderDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    variables.put("orderNote", orderResponse.getNote());

    PaymentMethodResponse paymentMethod = orderResponse.getPaymentMethod();
    if (paymentMethod != null) {
      variables.put("paymentMethodName", paymentMethod.getName());
      variables.put("orderStatusName", "COD".equalsIgnoreCase(paymentMethod.getCode()) ? "Đã xác nhận" : "Chờ thanh toán");
    }

    AddressResponse addressResponse = orderResponse.getAddressId() != null
        ? addressService.detail(orderResponse.getAddressId())
        : null;
    String shippingAddress = addressResponse != null ? addressResponse.getStreetAddress() : null;
    variables.put("shippingAddress", shippingAddress);

    PaymentQrResponse paymentQrResponse = orderResponse.getPaymentQr();
    if (paymentQrResponse != null) {
      variables.put("paymentQrUrl", paymentQrResponse.getQrCodeUrl());
      variables.put("bankName", paymentQrResponse.getBankName());
      variables.put("accountNumber", paymentQrResponse.getAccountNumber());
      variables.put("accountName", paymentQrResponse.getAccountName());
      variables.put("transferContent", paymentQrResponse.getTransferContent());
    }

    BigDecimal subtotal = orderResponse.getItems() != null
        ? orderResponse.getItems().stream()
            .map(OrderItemResponse::getTotalPrice)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
        : BigDecimal.ZERO;

    variables.put("items", orderResponse.getItems());
    variables.put("subtotal", subtotal);
    variables.put("shippingFee", orderResponse.getShippingFee());
    variables.put("discountAmount", orderResponse.getDiscountAmount());
    variables.put("totalAmount", orderResponse.getTotalAmount());
    // variables.put("orderDetailUrl", "https://yourfrontend.com/orders/" + orderResponse.getId());

    return variables;
  }
}
