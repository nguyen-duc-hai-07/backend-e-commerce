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
import org.oplearn.project.enums.ConversationStatus;
import org.oplearn.project.enums.ConversationType;

import java.time.Instant;

@Entity
@Table(name = "conversations")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conversation extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "type", length = 50, nullable = false)
  @Builder.Default
  private ConversationType type = ConversationType.DIRECT;

  @Column(name = "title")
  private String title;

  @Column(name = "user_id")
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 50, nullable = false)
  @Builder.Default
  private ConversationStatus status = ConversationStatus.OPEN;

  @Column(name = "last_message_at")
  private Instant lastMessageAt;
}
