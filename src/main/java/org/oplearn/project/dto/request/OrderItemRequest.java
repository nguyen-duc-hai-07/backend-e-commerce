package org.oplearn.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemRequest {

  @NotNull(message = "order_item.variant_id.not_null")
  private Long variantId;

  @NotNull(message = "order_item.quantity.not_null")
  @Min(value = 1, message = "order_item.quantity.min")
  private Integer quantity;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private BigDecimal unitPrice;
}
