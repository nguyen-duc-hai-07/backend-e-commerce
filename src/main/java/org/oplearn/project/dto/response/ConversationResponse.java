package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Conversation;
import org.oplearn.project.enums.ConversationStatus;
import org.oplearn.project.enums.ConversationType;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConversationResponse {

  private Long id;
  private ConversationType type;
  private String title;
  private Long userId;
  private ConversationStatus status;
  private Instant lastMessageAt;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public static ConversationResponse from(Conversation conversation) {
    if (conversation == null) {
      return null;
    }
    return ConversationResponse.builder()
        .id(conversation.getId())
        .type(conversation.getType())
        .title(conversation.getTitle())
        .userId(conversation.getUserId())
        .status(conversation.getStatus())
        .lastMessageAt(conversation.getLastMessageAt())
        .isDeleted(conversation.getIsDeleted())
        .createdBy(conversation.getCreatedBy())
        .createdAt(conversation.getCreatedAt())
        .updatedAt(conversation.getUpdatedAt())
        .build();
  }
}
