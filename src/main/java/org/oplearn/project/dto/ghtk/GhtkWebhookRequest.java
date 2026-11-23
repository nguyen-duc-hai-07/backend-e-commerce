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
public class GhtkWebhookRequest {

  /**
   * Mã đơn hàng của Shop đối tác gửi sang (order.orderCode)
   */
  private String partnerId;

  /**
   * Mã vận đơn GHTK (order.trackingCode, ví dụ: S21.HN1.10098412)
   */
  private String labelId;

  /**
   * Mã trạng thái từ ứng dụng của Shipper GHTK (3, 4, 5, 8, 10, -1...)
   */
  private Integer statusId;

  /**
   * Thời điểm bưu tá thao tác trên app
   */
  private String actionTime;

  /**
   * Mã lý do (ví dụ: 121, 122, 125, 130...)
   */
  private String reasonCode;

  /**
   * Chuỗi mô tả lý do chi tiết bằng tiếng Việt
   */
  private String reason;

  /**
   * Khối lượng thực tế bưu tá cân lại (kg)
   */
  private Double weight;

  /**
   * Cước phí thực tế của đơn hàng (VND)
   */
  private BigDecimal fee;

  /**
   * Cờ trả một phần kiện hàng
   */
  private Integer returnPartPackage;
}
