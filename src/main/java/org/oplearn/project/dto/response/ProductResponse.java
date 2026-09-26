package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.oplearn.project.entity.Product;
import org.oplearn.project.repository.ProductRepository;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponse {
  private Long id;
  private String name;
  private String description;
  private Long categoryId;
  private String thumbnailUrl;
  private BigDecimal averageRating;
  private Integer reviewCount;
  private BigDecimal minPrice;
  private Integer soldCount;

  public static ProductResponse from(Product product) {
    if (product == null) {
      return null;
    }
    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .categoryId(product.getCategoryId())
        .thumbnailUrl(product.getThumbnailUrl())
        .averageRating(product.getAverageRating())
        .reviewCount(product.getReviewCount())
        .minPrice(product.getMinPrice())
        .soldCount(product.getSoldCount())
        .build();
  }

  public static ProductResponse from(ProductRepository.ProductSearchRow row) {
    if (row == null) {
      return null;
    }
    return ProductResponse.builder()
        .id(row.getId())
        .name(row.getName())
        .description(row.getDescription())
        .categoryId(row.getCategoryId())
        .thumbnailUrl(row.getThumbnailUrl())
        .averageRating(row.getAverageRating())
        .reviewCount(row.getReviewCount())
        .minPrice(row.getMinPrice())
        .soldCount(row.getSoldCount())
        .build();
  }
}
