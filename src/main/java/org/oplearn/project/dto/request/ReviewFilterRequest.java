package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewFilterRequest {

  private Long productId;

  @Min(value = 1, message = "review.rating.min")
  @Max(value = 5, message = "review.rating.max")
  private Integer rating;

  @Builder.Default
  private Integer page = 0;

  @Builder.Default
  private Integer size = 10;
}
