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
public class GhtkTrackingResponse {

  private Boolean success;
  private String message;
  private TrackingData data;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class TrackingData {
    /**
     * Mã vận đơn GHTK (Ví dụ: S21.HN1.10098412)
     */
    private String labelId;

    /**
     * Mã đơn hàng của Shop đối tác (orderCode)
     */
    private String partnerId;

    /**
     * Mã trạng thái số (Ví dụ: 3, 4, 5...)
     */
    private String status;

    /**
     * Mô tả trạng thái dạng chữ (Ví dụ: "Đang giao hàng", "Đã lấy hàng"...)
     */
    private String statusText;

    /**
     * Thời điểm tạo đơn
     */
    private String created;

    /**
     * Thời điểm cập nhật trạng thái gần nhất
     */
    private String modified;

    /**
     * Ngày shipper lấy hàng
     */
    private String pickDate;

    /**
     * Ngày dự kiến giao hàng đến khách
     */
    private String deliverDate;

    /**
     * Tên khách hàng nhận
     */
    private String customerFullname;

    /**
     * Số điện thoại khách nhận
     */
    private String customerTel;

    /**
     * Địa chỉ giao hàng
     */
    private String address;

    /**
     * Khối lượng gói hàng (kg)
     */
    private Double weight;

    /**
     * Cước phí vận chuyển (VND)
     */
    private BigDecimal fee;
  }
}
