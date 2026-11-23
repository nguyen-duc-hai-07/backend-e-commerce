package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GhtkFeeResponse {
  private Boolean success;
  private String message;
  private GhtkFeeData fee;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class GhtkFeeData {
    private String name;
    private BigDecimal fee;
    private BigDecimal insuranceFee;
    private BigDecimal shipFeeOnly;
    private String deliveryType;
    private Boolean delivery;
  }
}
