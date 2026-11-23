package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;

@Entity
@Table(name = "return_request_images")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequestImage extends BaseEntity {

  @Column(name = "return_request_id", nullable = false)
  private Long returnRequestId;

  @Column(name = "image_url", length = 500, nullable = false)
  private String imageUrl;

  @Column(name = "display_order", nullable = false)
  @Builder.Default
  private Integer displayOrder = 0;
}
