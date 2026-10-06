package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CalculateShippingFeeRequest {

  @NotNull(message = "shipping.address_id.not_null")
  private Long addressId;

  @Min(value = 1, message = "shipping.total_weight.min")
  private Integer totalWeight;

  @Valid
  private List<ShippingItemRequest> items;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class ShippingItemRequest {

    @NotNull(message = "shipping.variant_id.not_null")
    private Long variantId;

    @NotNull(message = "shipping.quantity.not_null")
    @Min(value = 1, message = "shipping.quantity.min")
    private Integer quantity;
  }
}
