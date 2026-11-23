package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.oplearn.project.entity.ProductImage;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductImageResponse {

  private Long id;
  private Long productId;
  private String imageUrl;
  private Integer displayOrder;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public static ProductImageResponse from(ProductImage image) {
    if (image == null) {
      return null;
    }
    return ProductImageResponse.builder()
        .id(image.getId())
        .productId(image.getProductId())
        .imageUrl(image.getImageUrl())
        .displayOrder(image.getDisplayOrder())
        .isDeleted(image.getIsDeleted())
        .createdBy(image.getCreatedBy())
        .createdAt(image.getCreatedAt())
        .updatedAt(image.getUpdatedAt())
        .build();
  }
}
