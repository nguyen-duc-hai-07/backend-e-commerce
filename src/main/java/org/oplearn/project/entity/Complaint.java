package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.oplearn.project.entity.base.BaseEntity;
import org.oplearn.project.enums.ComplaintStatus;

@Entity
@Table(name = "complaints")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Complaint extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "order_id")
  private Long orderId;

  @Column(name = "content", columnDefinition = "TEXT", nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 50, nullable = false)
  @Builder.Default
  private ComplaintStatus status = ComplaintStatus.PENDING;

  @Column(name = "admin_reply", columnDefinition = "TEXT")
  private String adminReply;
}
