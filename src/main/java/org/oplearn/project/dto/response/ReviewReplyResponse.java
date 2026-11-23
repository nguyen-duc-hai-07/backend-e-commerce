package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.ReviewReply;
import org.oplearn.project.entity.User;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewReplyResponse {
  private Long id;
  private Long reviewId;
  private Long userId;
  private String userFullName;
  private String userAvatarUrl;
  private String content;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public static ReviewReplyResponse from(ReviewReply reply) {
    if (reply == null) {
      return null;
    }
    return ReviewReplyResponse.builder()
        .id(reply.getId())
        .reviewId(reply.getReviewId())
        .userId(reply.getUserId())
        .content(reply.getContent())
        .isDeleted(reply.getIsDeleted())
        .createdBy(reply.getCreatedBy())
        .createdAt(reply.getCreatedAt())
        .updatedAt(reply.getUpdatedAt())
        .build();
  }

  public static ReviewReplyResponse of(ReviewReply reply, User user) {
    if (reply == null) {
      return null;
    }
    ReviewReplyResponse response = from(reply);
    if (user != null) {
      response.setUserFullName(user.getFullName());
      response.setUserAvatarUrl(user.getAvatarUrl());
    }
    return response;
  }

  public static ReviewReplyResponse of(ReviewReplyResponse response, User user) {
    if (response == null) {
      return null;
    }
    if (user != null) {
      response.setUserFullName(user.getFullName());
      response.setUserAvatarUrl(user.getAvatarUrl());
    }
    return response;
  }
}
