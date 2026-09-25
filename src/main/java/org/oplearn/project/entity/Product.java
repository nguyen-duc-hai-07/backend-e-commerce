package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "category_id", nullable = false)
  private Long categoryId;

  @Column(name = "thumbnail_url", length = 500)
  private String thumbnailUrl;

  @Column(name = "average_rating", precision = 2, scale = 1, nullable = false)
  @Builder.Default
  private BigDecimal averageRating = BigDecimal.ZERO;

  @Column(name = "review_count", nullable = false)
  @Builder.Default
  private Integer reviewCount = 0;
}
