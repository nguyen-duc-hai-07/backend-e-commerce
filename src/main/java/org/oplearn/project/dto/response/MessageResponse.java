package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Message;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MessageResponse {

  private Long id;
  private Long conversationId;
  private Long senderId;
  private String fullName;
  private String content;
  private String imageUrl;
  private Boolean isRead;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public MessageResponse(Message message, String fullName) {
    if (message != null) {
      this.id = message.getId();
      this.conversationId = message.getConversationId();
      this.senderId = message.getSenderId();
      this.fullName = fullName;
      this.content = message.getContent();
      this.imageUrl = message.getImageUrl();
      this.isRead = message.getIsRead();
      this.isDeleted = message.getIsDeleted();
      this.createdBy = message.getCreatedBy();
      this.createdAt = message.getCreatedAt();
      this.updatedAt = message.getUpdatedAt();
    }
  }

  public static MessageResponse from(Message message) {
    return from(message, null);
  }

  public static MessageResponse from(Message message, String fullName) {
    if (message == null) {
      return null;
    }
    return MessageResponse.builder()
        .id(message.getId())
        .conversationId(message.getConversationId())
        .senderId(message.getSenderId())
        .fullName(fullName)
        .content(message.getContent())
        .imageUrl(message.getImageUrl())
        .isRead(message.getIsRead())
        .isDeleted(message.getIsDeleted())
        .createdBy(message.getCreatedBy())
        .createdAt(message.getCreatedAt())
        .updatedAt(message.getUpdatedAt())
        .build();
  }
}
