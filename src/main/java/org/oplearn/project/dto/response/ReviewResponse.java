package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Review;
import org.oplearn.project.entity.User;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewResponse {
  private Long id;
  private Long productId;
  private Long userId;
  private String userFullName;
  private String userAvatarUrl;
  private Integer rating;
  private String comment;
  private Instant createdAt;

  public static ReviewResponse from(Review review) {
    if (review == null) {
      return null;
    }
    return ReviewResponse.builder()
        .id(review.getId())
        .productId(review.getProductId())
        .userId(review.getUserId())
        .rating(review.getRating())
        .comment(review.getComment())
        .createdAt(review.getCreatedAt())
        .build();
  }

  public static ReviewResponse of(Review review, User user) {
    if (review == null) {
      return null;
    }
    ReviewResponse response = from(review);
    if (user != null) {
      response.setUserFullName(user.getFullName());
      response.setUserAvatarUrl(user.getAvatarUrl());
    }
    return response;
  }

  public static ReviewResponse of(ReviewResponse response, User user) {
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
