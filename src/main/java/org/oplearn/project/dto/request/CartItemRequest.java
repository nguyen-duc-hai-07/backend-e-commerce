package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartItemRequest {

  private Long userId;

  @NotNull(message = "cart_item.variant_id.not_null")
  private Long variantId;

  @NotNull(message = "cart_item.quantity.not_null")
  @Min(value = 1, message = "cart_item.quantity.min")
  private Integer quantity;
}
