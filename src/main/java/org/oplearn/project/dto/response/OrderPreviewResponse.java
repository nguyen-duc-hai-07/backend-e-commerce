package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.dto.request.OrderRequest;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderPreviewResponse {

  private Long userId;
  private Long addressId;
  private String recipientName;
  private String recipientPhone;

  private List<OrderItemResponse> items;

  private BigDecimal subtotal;
  private BigDecimal shippingFee;
  private BigDecimal discountAmount;
  private BigDecimal totalAmount;

  private Integer totalWeight;
  private String note;

  public static OrderPreviewResponse from(OrderRequest request) {
    if (request == null) {
      return null;
    }
    return OrderPreviewResponse.builder()
        .userId(request.getUserId())
        .addressId(request.getAddressId())
        .shippingFee(request.getShippingFee())
        .discountAmount(request.getDiscountAmount())
        .totalAmount(request.getTotalAmount())
        .note(request.getNote())
        .build();
  }

  public static OrderPreviewResponse of(
      OrderRequest request,
      AddressResponse address,
      List<OrderItemResponse> items,
      BigDecimal subtotal,
      Integer totalWeight
  ) {
    if (request == null) {
      return null;
    }
    return OrderPreviewResponse.builder()
        .userId(request.getUserId())
        .addressId(request.getAddressId())
        .recipientName(address != null ? address.getRecipientName() : null)
        .recipientPhone(address != null ? address.getPhoneNumber() : null)
        .items(items)
        .subtotal(subtotal)
        .shippingFee(request.getShippingFee())
        .discountAmount(request.getDiscountAmount())
        .totalAmount(request.getTotalAmount())
        .totalWeight(totalWeight)
        .note(request.getNote())
        .build();
  }
}
