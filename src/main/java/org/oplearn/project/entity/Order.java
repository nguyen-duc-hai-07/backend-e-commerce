package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;
import org.oplearn.project.enums.OrderStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {

  @Column(name = "order_code", length = 100, nullable = false, unique = true)
  private String orderCode;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "address_id", nullable = false)
  private Long addressId;

  @Column(name = "shipping_fee", nullable = false)
  @Builder.Default
  private BigDecimal shippingFee = BigDecimal.ZERO;

  @Column(name = "discount_amount", nullable = false)
  @Builder.Default
  private BigDecimal discountAmount = BigDecimal.ZERO;

  @Column(name = "total_amount", nullable = false)
  private BigDecimal totalAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 50, nullable = false)
  @Builder.Default
  private OrderStatus status = OrderStatus.PENDING_PAYMENT;

  @Column(name = "note", length = 500)
  private String note;

  @Column(name = "original_order_id")
  private Long originalOrderId;
}
