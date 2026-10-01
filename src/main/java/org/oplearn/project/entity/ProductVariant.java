package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.oplearn.project.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariant extends BaseEntity {

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "sku", length = 100, nullable = false, unique = true)
  private String sku;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "attributes", columnDefinition = "jsonb", nullable = false)
  private Map<String, String> attributes;

  @Column(name = "price", precision = 15, scale = 2, nullable = false)
  private BigDecimal price;

  @Column(name = "quantity", nullable = false)
  @Builder.Default
  private Integer quantity = 0;
}
