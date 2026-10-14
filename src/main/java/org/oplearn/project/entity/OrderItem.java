package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity {

  @Column(name = "order_id", nullable = false)
  private Long orderId;

  @Column(name = "variant_id", nullable = false)
  private Long variantId;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "unit_price", nullable = false)
  private BigDecimal unitPrice;
}
