package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductVariantRequest {

  @NotNull(message = "product_variant.product_id.not_null")
  private Long productId;

  @NotBlank(message = "product_variant.sku.not_blank")
  private String sku;

  @NotEmpty(message = "product_variant.attributes.not_empty")
  private Map<String, String> attributes;

  @NotNull(message = "product_variant.price.not_null")
  @DecimalMin(value = "0.0", inclusive = false, message = "product_variant.price.min")
  private BigDecimal price;

  @NotNull(message = "product_variant.quantity.not_null")
  @Min(value = 0, message = "product_variant.quantity.min")
  private Integer quantity;
}
