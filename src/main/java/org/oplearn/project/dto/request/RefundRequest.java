package org.oplearn.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class RefundRequest {

  @NotNull(message = "refund.order_id.not_null")
  private Long orderId;

  @NotBlank(message = "refund.account_number.not_blank")
  @Size(max = 50, message = "refund.account_number.max_length")
  private String accountNumber;

  @NotBlank(message = "refund.account_holder_name.not_blank")
  @Size(max = 150, message = "refund.account_holder_name.max_length")
  private String accountHolderName;

  @NotBlank(message = "refund.bank_name.not_blank")
  @Size(max = 150, message = "refund.bank_name.max_length")
  private String bankName;

  @Size(max = 20, message = "refund.bank_code.max_length")
  private String bankCode;

  @Size(max = 255, message = "refund.reason.max_length")
  private String reason;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long userId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private BigDecimal amount;
}
