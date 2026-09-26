package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.oplearn.project.entity.ProductVariant;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductVariantResponse {

  private Long id;
  private Long productId;
  private String sku;
  private Map<String, String> attributes;
  private BigDecimal price;
  private Integer quantity;

  public static ProductVariantResponse from(ProductVariant variant) {
    if (variant == null) {
      return null;
    }
    return ProductVariantResponse.builder()
        .id(variant.getId())
        .productId(variant.getProductId())
        .sku(variant.getSku())
        .attributes(variant.getAttributes())
        .price(variant.getPrice())
        .quantity(variant.getQuantity())
        .build();
  }
}
