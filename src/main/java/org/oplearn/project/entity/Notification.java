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
import lombok.ToString;
import org.oplearn.project.entity.base.BaseEntity;
import org.oplearn.project.enums.NotificationReferenceType;
import org.oplearn.project.enums.NotificationType;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Notification extends BaseEntity {

  @Column(name = "recipient_id")
  private Long recipientId;

  @Column(name = "sender_id")
  private Long senderId;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", length = 50, nullable = false)
  private NotificationType type;

  @Column(name = "title")
  private String title;

  @Column(name = "content", columnDefinition = "TEXT")
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(name = "reference_type", length = 50)
  private NotificationReferenceType referenceType;

  @Column(name = "reference_id")
  private Long referenceId;
}
