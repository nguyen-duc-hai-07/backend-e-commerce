package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.DecimalMin;
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
public class PaymentRequest {

  @NotNull(message = "payment.order_id.not_null")
  private Long orderId;

  @NotNull(message = "payment.payment_method_id.not_null")
  private Long paymentMethodId;

  @NotNull(message = "payment.amount.not_null")
  @DecimalMin(value = "0.0", inclusive = false, message = "payment.amount.min")
  private BigDecimal amount;

  private String transactionCode;
}
