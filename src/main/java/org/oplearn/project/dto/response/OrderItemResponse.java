package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.dto.request.OrderItemRequest;
import org.oplearn.project.entity.OrderItem;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemResponse {

  private Long id;
  private Long orderId;
  private Long variantId;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal totalPrice;

  private Long productId;
  private String productName;
  private String productThumbnailUrl;
  private String variantImageUrl;
  private String thumbnailUrl;
  private String sku;
  private Map<String, String> attributes;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public static OrderItemResponse from(OrderItem item) {
    if (item == null) {
      return null;
    }
    BigDecimal total = (item.getUnitPrice() != null && item.getQuantity() != null)
        ? item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        : BigDecimal.ZERO;

    return OrderItemResponse.builder()
        .id(item.getId())
        .orderId(item.getOrderId())
        .variantId(item.getVariantId())
        .quantity(item.getQuantity())
        .unitPrice(item.getUnitPrice())
        .totalPrice(total)
        .isDeleted(item.getIsDeleted())
        .createdBy(item.getCreatedBy())
        .createdAt(item.getCreatedAt())
        .updatedAt(item.getUpdatedAt())
        .build();
  }

  public static OrderItemResponse of(OrderItemResponse response, ProductVariantResponse variant) {
    if (response == null) {
      return null;
    }
    if (variant != null) {
      response.setProductId(variant.getProductId());
      response.setSku(variant.getSku());
      response.setAttributes(variant.getAttributes());
      response.setVariantImageUrl(variant.getImageUrl());
      response.setThumbnailUrl(variant.getImageUrl());
    }
    return response;
  }

  public static OrderItemResponse of(
      OrderItemResponse response,
      ProductVariantResponse variant,
      ProductResponse product
  ) {
    if (response == null) {
      return null;
    }
    of(response, variant);
    if (product != null) {
      response.setProductName(product.getName());
      response.setProductThumbnailUrl(product.getThumbnailUrl());
      if (response.getThumbnailUrl() == null || response.getThumbnailUrl().isBlank()) {
        response.setThumbnailUrl(product.getThumbnailUrl());
      }
    }
    return response;
  }

  public static OrderItemResponse of(
      OrderItem item,
      ProductVariantResponse variant,
      ProductResponse product
  ) {
    if (item == null) {
      return null;
    }
    OrderItemResponse response = from(item);
    return of(response, variant, product);
  }

  public static OrderItemResponse of(
      OrderItemRequest request,
      ProductVariantResponse variant,
      ProductResponse product
  ) {
    if (request == null) {
      return null;
    }
    BigDecimal unitPrice = (variant != null && variant.getPrice() != null)
        ? variant.getPrice()
        : BigDecimal.ZERO;
    BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity() != null ? request.getQuantity() : 0));

    OrderItemResponse response = OrderItemResponse.builder()
        .variantId(request.getVariantId())
        .quantity(request.getQuantity())
        .unitPrice(unitPrice)
        .totalPrice(total)
        .build();

    return of(response, variant, product);
  }

  public static OrderItemResponse of(
      OrderItemRequest request,
      ProductVariantResponse variant
  ) {
    return of(request, variant, null);
  }
}
