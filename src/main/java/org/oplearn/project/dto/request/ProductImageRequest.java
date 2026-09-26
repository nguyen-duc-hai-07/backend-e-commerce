package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductImageRequest {

  @NotNull(message = "product_image.product_id.not_null")
  private Long productId;

  @NotBlank(message = "product_image.image_url.not_blank")
  private String imageUrl;

  @Min(value = 0, message = "product_image.display_order.min")
  @Builder.Default
  private Integer displayOrder = 0;
}
