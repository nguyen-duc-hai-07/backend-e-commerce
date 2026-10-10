package org.oplearn.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.oplearn.project.enums.OrderStatus;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderCreateRequest extends OrderRequest {

  @NotNull(message = "order.payment_method_id.not_null")
  private Long paymentMethodId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private OrderStatus status;
}
