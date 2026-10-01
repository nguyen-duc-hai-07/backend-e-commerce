package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewReplyRequest {

  private Long userId;

  @NotNull(message = "review_reply.review_id.not_null")
  private Long reviewId;

  @NotBlank(message = "review_reply.content.not_blank")
  @Size(max = 1000, message = "review_reply.content.max_length")
  private String content;
}
