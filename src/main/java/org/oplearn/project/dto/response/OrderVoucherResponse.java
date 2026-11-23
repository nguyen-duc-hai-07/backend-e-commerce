package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class OrderVoucherResponse {

  private Long userVoucherId;
  private Long voucherId;
  private String code;
  private String campaignCode;
  private VoucherTarget target;
  private DiscountType discountType;
  private BigDecimal discountValue;
  private BigDecimal maxDiscountAmount;
  private BigDecimal minOrderValue;
  private Instant useStartDate;
  private Instant useEndDate;
}
