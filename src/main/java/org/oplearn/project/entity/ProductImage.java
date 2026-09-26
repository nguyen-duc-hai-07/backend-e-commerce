package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage extends BaseEntity {
  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @Column(name = "display_order", nullable = false)
  @Builder.Default
  private Integer displayOrder = 0;
}
