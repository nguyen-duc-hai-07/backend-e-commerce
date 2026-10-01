package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem extends BaseEntity {
  @Column(name = "user_id" , nullable = false)
  private Long userId;

  @Column(name = "variant_id" , nullable = false)
  private Long variantId;

  @Column(name = "quantity" , nullable = false)
  private Integer quantity;
}
