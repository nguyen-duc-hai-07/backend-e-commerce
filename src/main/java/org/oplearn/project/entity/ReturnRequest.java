package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;
import org.oplearn.project.enums.ReturnRequestStatus;
import org.oplearn.project.enums.ReturnResolutionType;

@Entity
@Table(name = "return_requests")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequest extends BaseEntity {

  @Column(name = "order_id", nullable = false)
  private Long orderId;

  @Column(name = "reason", length = 255, nullable = false)
  private String reason;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "resolution_type", length = 50)
  private ReturnResolutionType resolutionType;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 50, nullable = false)
  @Builder.Default
  private ReturnRequestStatus status = ReturnRequestStatus.PENDING;

  @Column(name = "reject_reason", length = 255)
  private String rejectReason;
}
