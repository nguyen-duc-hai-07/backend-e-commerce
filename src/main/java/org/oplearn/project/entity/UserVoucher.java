package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;
import org.oplearn.project.enums.UserVoucherStatus;

import java.time.Instant;

@Entity
@Table(
  name = "user_vouchers",
  uniqueConstraints = {
    @UniqueConstraint(name = "uq_user_vouchers_voucher_user", columnNames = {"voucher_id", "user_id"})
  }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVoucher extends BaseEntity {

  @Column(name = "voucher_id", nullable = false)
  private Long voucherId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "order_id")
  private Long orderId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 50, nullable = false)
  @Builder.Default
  private UserVoucherStatus status = UserVoucherStatus.UNUSED;

  @Column(name = "claimed_at", nullable = false)
  private Instant claimedAt;

  @Column(name = "used_at")
  private Instant usedAt;
}
