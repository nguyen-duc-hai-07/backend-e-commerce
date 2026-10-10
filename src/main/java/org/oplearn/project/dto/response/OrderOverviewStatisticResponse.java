package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderOverviewStatisticResponse {
  private Instant fromDate;
  private Instant toDate;
  private Long totalOrders;
  private Long completedOrders;
  private Long cancelledOrders;
  private Long refundedOrders;
  private Long inProgressOrders;
  private BigDecimal totalRevenue;
}
