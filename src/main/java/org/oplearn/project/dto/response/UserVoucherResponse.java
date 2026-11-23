package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.UserVoucher;
import org.oplearn.project.entity.Voucher;
import org.oplearn.project.enums.UserVoucherStatus;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserVoucherResponse {

  private Long id;
  private Long voucherId;
  private Long userId;
  private Long orderId;
  private UserVoucherStatus status;
  private Instant claimedAt;
  private Instant usedAt;
  private Instant createdAt;
  private Instant updatedAt;

  private VoucherResponse voucher;

  public static UserVoucherResponse from(UserVoucher userVoucher) {
    if (userVoucher == null) {
      return null;
    }
    return UserVoucherResponse.builder()
        .id(userVoucher.getId())
        .voucherId(userVoucher.getVoucherId())
        .userId(userVoucher.getUserId())
        .orderId(userVoucher.getOrderId())
        .status(userVoucher.getStatus())
        .claimedAt(userVoucher.getClaimedAt())
        .usedAt(userVoucher.getUsedAt())
        .createdAt(userVoucher.getCreatedAt())
        .updatedAt(userVoucher.getUpdatedAt())
        .build();
  }

  public static UserVoucherResponse from(UserVoucher userVoucher, Voucher voucher) {
    if (userVoucher == null) {
      return null;
    }
    return UserVoucherResponse.builder()
        .id(userVoucher.getId())
        .voucherId(userVoucher.getVoucherId())
        .userId(userVoucher.getUserId())
        .orderId(userVoucher.getOrderId())
        .status(userVoucher.getStatus())
        .claimedAt(userVoucher.getClaimedAt())
        .usedAt(userVoucher.getUsedAt())
        .createdAt(userVoucher.getCreatedAt())
        .updatedAt(userVoucher.getUpdatedAt())
        .voucher(VoucherResponse.from(voucher))
        .build();
  }
}
