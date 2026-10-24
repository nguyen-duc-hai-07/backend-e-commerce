package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDetailResponse {

  private Long id;
  private String name;
  private String description;
  private Long categoryId;
  private String thumbnailUrl;
  private BigDecimal averageRating;
  private Integer reviewCount;
  private BigDecimal minPrice;
  private Integer soldCount;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  private List<ProductImageResponse> images;
  private List<ProductVariantResponse> variants;

  public static ProductDetailResponse of(
    ProductResponse product,
    List<ProductImageResponse> images,
    List<ProductVariantResponse> variants
  ) {
    if (product == null) {
      return null;
    }
    return ProductDetailResponse.builder()
      .id(product.getId())
      .name(product.getName())
      .description(product.getDescription())
      .categoryId(product.getCategoryId())
      .thumbnailUrl(product.getThumbnailUrl())
      .averageRating(product.getAverageRating())
      .reviewCount(product.getReviewCount())
      .minPrice(product.getMinPrice())
      .soldCount(product.getSoldCount())
      .isDeleted(product.getIsDeleted())
      .createdBy(product.getCreatedBy())
      .createdAt(product.getCreatedAt())
      .updatedAt(product.getUpdatedAt())
      .images(images != null ? images : List.of())
      .variants(variants != null ? variants : List.of())
      .build();
  }
}
