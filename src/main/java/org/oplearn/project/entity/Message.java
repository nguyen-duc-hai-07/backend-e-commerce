package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.oplearn.project.entity.base.BaseEntity;

@Entity
@Table(name = "messages")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message extends BaseEntity {

  @Column(name = "conversation_id", nullable = false)
  private Long conversationId;

  @Column(name = "sender_id", nullable = false)
  private Long senderId;

  @Column(name = "content", columnDefinition = "TEXT", nullable = false)
  private String content;

  @Column(name = "image_url", length = 500)
  private String imageUrl;

  @Column(name = "is_read", nullable = false)
  @Builder.Default
  private Boolean isRead = Boolean.FALSE;
}
