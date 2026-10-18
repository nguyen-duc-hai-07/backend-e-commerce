package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Voucher;
import org.oplearn.project.enums.DiscountType;
import org.oplearn.project.enums.VoucherTarget;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VoucherResponse {

  private Long id;
  private String campaignCode;
  private String code;
  private VoucherTarget target;
  private DiscountType discountType;
  private BigDecimal discountValue;
  private BigDecimal maxDiscountAmount;
  private BigDecimal minOrderValue;
  private Integer maxTotalClaim;
  private Integer claimedCount;
  private Instant claimStartDate;
  private Instant claimEndDate;
  private Instant useStartDate;
  private Instant useEndDate;
  private Boolean isActive;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public static VoucherResponse from(Voucher voucher) {
    if (voucher == null) {
      return null;
    }
    return VoucherResponse.builder()
        .id(voucher.getId())
        .campaignCode(voucher.getCampaignCode())
        .code(voucher.getCode())
        .target(voucher.getTarget())
        .discountType(voucher.getDiscountType())
        .discountValue(voucher.getDiscountValue())
        .maxDiscountAmount(voucher.getMaxDiscountAmount())
        .minOrderValue(voucher.getMinOrderValue())
        .maxTotalClaim(voucher.getMaxTotalClaim())
        .claimedCount(voucher.getClaimedCount())
        .claimStartDate(voucher.getClaimStartDate())
        .claimEndDate(voucher.getClaimEndDate())
        .useStartDate(voucher.getUseStartDate())
        .useEndDate(voucher.getUseEndDate())
        .isActive(voucher.getIsActive())
        .isDeleted(voucher.getIsDeleted())
        .createdBy(voucher.getCreatedBy())
        .createdAt(voucher.getCreatedAt())
        .updatedAt(voucher.getUpdatedAt())
        .build();
  }
}
