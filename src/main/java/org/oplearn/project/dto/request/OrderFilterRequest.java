package org.oplearn.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.enums.OrderStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderFilterRequest {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long userId;

  private OrderStatus status;

  @Builder.Default
  private Integer page = 0;

  @Builder.Default
  private Integer size = 10;
}
