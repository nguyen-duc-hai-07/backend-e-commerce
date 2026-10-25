package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;
import org.oplearn.project.enums.RefundStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "refunds")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Refund extends BaseEntity {

  @Column(name = "order_id", nullable = false)
  private Long orderId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "amount", precision = 15, scale = 2, nullable = false)
  private BigDecimal amount;

  @Column(name = "account_number", length = 50, nullable = false)
  private String accountNumber;

  @Column(name = "account_holder_name", length = 150, nullable = false)
  private String accountHolderName;

  @Column(name = "bank_name", length = 150, nullable = false)
  private String bankName;

  @Column(name = "bank_code", length = 20)
  private String bankCode;

  @Column(name = "qr_url", length = 500)
  private String qrUrl;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 50, nullable = false)
  @Builder.Default
  private RefundStatus status = RefundStatus.PENDING;

  @Column(name = "reason", length = 255)
  private String reason;

  @Column(name = "reject_reason", length = 255)
  private String rejectReason;
}
