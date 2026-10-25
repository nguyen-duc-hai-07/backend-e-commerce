package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Notification;
import org.oplearn.project.enums.NotificationReferenceType;
import org.oplearn.project.enums.NotificationType;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationResponse {

  private Long id;
  private Long recipientId;
  private Long senderId;
  private NotificationType type;
  private String title;
  private String content;
  private NotificationReferenceType referenceType;
  private Long referenceId;
  private Boolean isRead;
  private Instant readAt;
  private Instant createdAt;
  private Instant updatedAt;

  public NotificationResponse(Notification notification, Boolean isRead, Instant readAt) {
    if (notification != null) {
      this.id = notification.getId();
      this.recipientId = notification.getRecipientId();
      this.senderId = notification.getSenderId();
      this.type = notification.getType();
      this.title = notification.getTitle();
      this.content = notification.getContent();
      this.referenceType = notification.getReferenceType();
      this.referenceId = notification.getReferenceId();
      this.isRead = isRead != null && isRead;
      this.readAt = readAt;
      this.createdAt = notification.getCreatedAt();
      this.updatedAt = notification.getUpdatedAt();
    }
  }

  public static NotificationResponse from(Notification notification) {
    return from(notification, false, null);
  }

  public static NotificationResponse from(Notification notification, boolean isRead) {
    return from(notification, isRead, null);
  }

  public static NotificationResponse from(Notification notification, boolean isRead, Instant readAt) {
    if (notification == null) {
      return null;
    }
    return NotificationResponse.builder()
        .id(notification.getId())
        .recipientId(notification.getRecipientId())
        .senderId(notification.getSenderId())
        .type(notification.getType())
        .title(notification.getTitle())
        .content(notification.getContent())
        .referenceType(notification.getReferenceType())
        .referenceId(notification.getReferenceId())
        .isRead(isRead)
        .readAt(readAt)
        .createdAt(notification.getCreatedAt())
        .updatedAt(notification.getUpdatedAt())
        .build();
  }
}
