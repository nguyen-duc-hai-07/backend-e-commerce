package org.oplearn.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewRequest {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long userId;

  @NotNull(message = "review.product_id.not_null")
  private Long productId;

  @NotNull(message = "review.rating.not_null")
  @Min(value = 1, message = "review.rating.min")
  @Max(value = 5, message = "review.rating.max")
  private Integer rating;

  private String comment;
}
