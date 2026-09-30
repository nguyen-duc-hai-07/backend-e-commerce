package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod extends BaseEntity {

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "code", length = 50, nullable = false, unique = true)
  private String code;
}
