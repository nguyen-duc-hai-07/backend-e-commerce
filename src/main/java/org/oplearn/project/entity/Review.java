package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {
  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "comment", columnDefinition = "TEXT")
  private String comment;

  @Column(name = "rating", nullable = false)
  private Integer rating;
}
