package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.enums.DiscountType;
import org.oplearn.project.enums.VoucherTarget;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VoucherRequest {

  @NotBlank(message = "voucher.campaign_code.not_blank")
  private String campaignCode;

  @NotNull(message = "voucher.target.not_null")
  private VoucherTarget target;

  @NotNull(message = "voucher.discount_type.not_null")
  private DiscountType discountType;

  @NotNull(message = "voucher.discount_value.not_null")
  @DecimalMin(value = "0.01", message = "voucher.discount_value.min")
  private BigDecimal discountValue;

  @DecimalMin(value = "0.0", message = "voucher.max_discount_amount.min")
  private BigDecimal maxDiscountAmount;

  @DecimalMin(value = "0.0", message = "voucher.min_order_value.min")
  @Builder.Default
  private BigDecimal minOrderValue = BigDecimal.ZERO;

  @NotNull(message = "voucher.max_total_claim.not_null")
  @Min(value = 1, message = "voucher.max_total_claim.min")
  private Integer maxTotalClaim;

  @NotNull(message = "voucher.claim_start_date.not_null")
  private Instant claimStartDate;

  @NotNull(message = "voucher.claim_end_date.not_null")
  private Instant claimEndDate;

  @NotNull(message = "voucher.use_start_date.not_null")
  private Instant useStartDate;

  @NotNull(message = "voucher.use_end_date.not_null")
  private Instant useEndDate;
}
