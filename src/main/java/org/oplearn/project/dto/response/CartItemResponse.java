package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.CartItem;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartItemResponse {

  private Long id;
  private Long userId;
  private Long variantId;
  private Integer quantity;

  private Long productId;
  private String sku;
  private Map<String, String> attributes;
  private BigDecimal price;
  private Integer stockQuantity;
  private BigDecimal totalPrice;

  private String productName;
  private String productThumbnailUrl;
  private String variantImageUrl;
  private String thumbnailUrl;

  public CartItemResponse(
    Long id,
    Long userId,
    Long variantId,
    Integer quantity,
    Long productId,
    String sku,
    Map<String, String> attributes,
    BigDecimal price,
    Integer stockQuantity,
    String productName,
    String productThumbnailUrl
  ) {
    this.id = id;
    this.userId = userId;
    this.variantId = variantId;
    this.quantity = quantity;
    this.productId = productId;
    this.sku = sku;
    this.attributes = attributes;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.productName = productName;
    this.productThumbnailUrl = productThumbnailUrl;
    this.thumbnailUrl = productThumbnailUrl;
    if (price != null && quantity != null) {
      this.totalPrice = price.multiply(BigDecimal.valueOf(quantity));
    }
  }

  public static CartItemResponse from(CartItem cartItem) {
    if (cartItem == null) {
      return null;
    }
    return CartItemResponse.builder()
        .id(cartItem.getId())
        .userId(cartItem.getUserId())
        .variantId(cartItem.getVariantId())
        .quantity(cartItem.getQuantity())
        .build();
  }

  public static CartItemResponse of(CartItemResponse response, ProductVariantResponse variant) {
    if (response == null) {
      return null;
    }
    if (variant != null) {
      response.setProductId(variant.getProductId());
      response.setSku(variant.getSku());
      response.setAttributes(variant.getAttributes());
      response.setPrice(variant.getPrice());
      response.setStockQuantity(variant.getQuantity());
      response.setVariantImageUrl(variant.getImageUrl());
      response.setThumbnailUrl(variant.getImageUrl());
      if (variant.getPrice() != null && response.getQuantity() != null) {
        response.setTotalPrice(variant.getPrice().multiply(BigDecimal.valueOf(response.getQuantity())));
      }
    }
    return response;
  }

  public static CartItemResponse of(
      CartItemResponse response,
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

  public static CartItemResponse of(
      CartItem cartItem,
      ProductVariantResponse variant,
      ProductResponse product
  ) {
    if (cartItem == null) {
      return null;
    }
    CartItemResponse response = from(cartItem);
    return of(response, variant, product);
  }
}
