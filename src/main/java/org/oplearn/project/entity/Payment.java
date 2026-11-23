package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;
import org.oplearn.project.enums.PaymentStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity {

  @Column(name = "order_id", nullable = false)
  private Long orderId;

  @Column(name = "payment_method_id", nullable = false)
  private Long paymentMethodId;

  @Column(name = "amount", precision = 15, scale = 2, nullable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 50, nullable = false)
  @Builder.Default
  private PaymentStatus status = PaymentStatus.PENDING;

  @Column(name = "transaction_code", length = 255)
  private String transactionCode;
}
