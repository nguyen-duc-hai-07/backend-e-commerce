package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.oplearn.project.entity.ProductImage;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductImageResponse {

  private Long id;
  private Long productId;
  private String imageUrl;
  private Integer displayOrder;

  public static ProductImageResponse from(ProductImage image) {
    if (image == null) {
      return null;
    }
    return ProductImageResponse.builder()
        .id(image.getId())
        .productId(image.getProductId())
        .imageUrl(image.getImageUrl())
        .displayOrder(image.getDisplayOrder())
        .build();
  }
}
