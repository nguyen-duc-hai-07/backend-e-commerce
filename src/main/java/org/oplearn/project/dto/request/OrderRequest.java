package org.oplearn.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderRequest {

  private Long addressId;

  private String note;

  private List<Long> cartItemIds;

  @NotEmpty(message = "order.items.not_empty")
  @Valid
  private List<OrderItemRequest> items;

  //  private String voucherCode;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long userId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private BigDecimal shippingFee;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private BigDecimal discountAmount;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private BigDecimal totalAmount;
}
