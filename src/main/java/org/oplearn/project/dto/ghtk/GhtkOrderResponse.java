package org.oplearn.project.dto.ghtk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GhtkOrderResponse {

  private Boolean success;
  private String message;
  private OrderData order;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class OrderData {
    /**
     * Mã đơn hàng của Shop đối tác gửi sang (order.id / orderCode)
     */
    private String partnerId;

    /**
     * Mã vận đơn GHTK cấp (Ví dụ: S21.HN1.10098412).
     */
    private String label;

    /**
     * Phí dịch vụ giao hàng thực tế (VND)
     */
    private BigDecimal fee;

    /**
     * ID trạng thái của đơn hàng bên GHTK
     */
    private Integer statusId;

    /**
     * Thời gian dự kiến giao hàng thành công đến khách
     */
    private String estimatedDeliverTime;
  }
}
