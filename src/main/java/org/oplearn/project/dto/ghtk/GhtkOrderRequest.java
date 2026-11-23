package org.oplearn.project.dto.ghtk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GhtkOrderRequest {

  private List<ProductItem> products;
  private OrderInfo order;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class ProductItem {
    private String name;
    private Double weight;      // Khối lượng tính theo kg (vd: 0.2 là 200g)
    private Integer quantity;
    private Object productCode; // Mã sản phẩm / SKU
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class OrderInfo {
    /**
     * Mã đơn hàng của Shop trong hệ thống (vd: ORD-102948)
     */
    private String id;

    // --- Thông tin kho lấy hàng của Shop ---
    private String pickName;
    private String pickAddress;
    private String pickProvince;
    private String pickDistrict;
    private String pickWard;
    private String pickTel;

    // --- Thông tin người nhận hàng ---
    private String name;
    private String tel;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String hamlet; // Mặc định để "Khác"

    // --- Cước phí & Thu hộ ---
    private String isFreeship; // "1": Shop chịu ship, "0": Khách trả ship
    private Integer pickMoney; // Tiền thu hộ COD (bằng 0 nếu khách đã thanh toán online)
    private Integer value;     // Giá trị đơn hàng để khai báo bảo hiểm (VND)
    private String note;
  }
}
