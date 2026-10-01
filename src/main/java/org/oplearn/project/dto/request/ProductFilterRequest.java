package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductFilterRequest {
  private String keyword;
  private Long categoryId;

  @Builder.Default
  private Integer page = 0;

  @Builder.Default
  private Integer size = 10;

  @Builder.Default
  private String sortBy = "id";

  @Builder.Default
  private String direction = "DESC";
}
