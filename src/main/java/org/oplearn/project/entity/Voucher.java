package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;
import org.oplearn.project.enums.DiscountType;
import org.oplearn.project.enums.VoucherTarget;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "vouchers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Voucher extends BaseEntity {

  @Column(name = "campaign_code", length = 100, nullable = false)
  private String campaignCode;

  @Column(name = "code", length = 50, nullable = false, unique = true)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(name = "target", length = 30, nullable = false)
  @Builder.Default
  private VoucherTarget target = VoucherTarget.PRODUCT;

  @Enumerated(EnumType.STRING)
  @Column(name = "discount_type", length = 50, nullable = false)
  private DiscountType discountType;

  @Column(name = "discount_value", precision = 15, scale = 2, nullable = false)
  private BigDecimal discountValue;

  @Column(name = "max_discount_amount", precision = 15, scale = 2)
  private BigDecimal maxDiscountAmount;

  @Column(name = "min_order_value", precision = 15, scale = 2, nullable = false)
  @Builder.Default
  private BigDecimal minOrderValue = BigDecimal.ZERO;

  @Column(name = "max_total_claim", nullable = false)
  private Integer maxTotalClaim;

  @Column(name = "claimed_count", nullable = false)
  @Builder.Default
  private Integer claimedCount = 0;

  @Column(name = "claim_start_date", nullable = false)
  private Instant claimStartDate;

  @Column(name = "claim_end_date", nullable = false)
  private Instant claimEndDate;

  @Column(name = "use_start_date", nullable = false)
  private Instant useStartDate;

  @Column(name = "use_end_date", nullable = false)
  private Instant useEndDate;

  @Column(name = "is_active", nullable = false)
  @Builder.Default
  private Boolean isActive = Boolean.TRUE;
}
