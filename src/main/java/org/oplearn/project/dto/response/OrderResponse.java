package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Order;
import org.oplearn.project.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderResponse {

  private Long id;
  private String orderCode;
  private Long userId;
  private Long addressId;
  private String recipientName;
  private String recipientPhone;
  private BigDecimal shippingFee;
  private BigDecimal discountAmount;
  private BigDecimal totalAmount;
  private OrderStatus status;
  private String note;
  private Long originalOrderId;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;
  private List<OrderItemResponse> items;
  private PaymentMethodResponse paymentMethod;
  private PaymentQrResponse paymentQr;

  public OrderResponse(
      Long id,
      String orderCode,
      Long userId,
      Long addressId,
      String recipientName,
      String recipientPhone,
      BigDecimal shippingFee,
      BigDecimal discountAmount,
      BigDecimal totalAmount,
      OrderStatus status,
      String note,
      Long originalOrderId,
      Instant createdAt
  ) {
    this.id = id;
    this.orderCode = orderCode;
    this.userId = userId;
    this.addressId = addressId;
    this.recipientName = recipientName;
    this.recipientPhone = recipientPhone;
    this.shippingFee = shippingFee;
    this.discountAmount = discountAmount;
    this.totalAmount = totalAmount;
    this.status = status;
    this.note = note;
    this.originalOrderId = originalOrderId;
    this.createdAt = createdAt;
  }

  public OrderResponse(
      Long id,
      String orderCode,
      Long userId,
      Long addressId,
      String recipientName,
      String recipientPhone,
      BigDecimal shippingFee,
      BigDecimal discountAmount,
      BigDecimal totalAmount,
      OrderStatus status,
      String note,
      Long originalOrderId,
      Boolean isDeleted,
      String createdBy,
      Instant createdAt,
      Instant updatedAt
  ) {
    this.id = id;
    this.orderCode = orderCode;
    this.userId = userId;
    this.addressId = addressId;
    this.recipientName = recipientName;
    this.recipientPhone = recipientPhone;
    this.shippingFee = shippingFee;
    this.discountAmount = discountAmount;
    this.totalAmount = totalAmount;
    this.status = status;
    this.note = note;
    this.originalOrderId = originalOrderId;
    this.isDeleted = isDeleted;
    this.createdBy = createdBy;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static OrderResponse from(Order order) {
    if (order == null) {
      return null;
    }
    return OrderResponse.builder()
        .id(order.getId())
        .orderCode(order.getOrderCode())
        .userId(order.getUserId())
        .addressId(order.getAddressId())
        .shippingFee(order.getShippingFee())
        .discountAmount(order.getDiscountAmount())
        .totalAmount(order.getTotalAmount())
        .status(order.getStatus())
        .note(order.getNote())
        .originalOrderId(order.getOriginalOrderId())
        .isDeleted(order.getIsDeleted())
        .createdBy(order.getCreatedBy())
        .createdAt(order.getCreatedAt())
        .updatedAt(order.getUpdatedAt())
        .build();
  }

  public static OrderResponse from(Order order, List<OrderItemResponse> items) {
    OrderResponse response = from(order);
    if (response != null) {
      response.setItems(items);
    }
    return response;
  }

  public static OrderResponse from(OrderResponse response, List<OrderItemResponse> items) {
    if (response == null) {
      return null;
    }
    response.setItems(items);
    return response;
  }

  public static OrderResponse of(OrderResponse response, OrderPreviewResponse preview) {
    if (response == null || preview == null) {
      return response;
    }
    response.setRecipientName(preview.getRecipientName());
    response.setRecipientPhone(preview.getRecipientPhone());
    return response;
  }

  public static OrderResponse from(OrderResponse response, OrderPreviewResponse preview) {
    return of(response, preview);
  }
}
