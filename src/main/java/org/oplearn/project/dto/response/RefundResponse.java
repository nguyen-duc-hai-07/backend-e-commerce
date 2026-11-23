package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Refund;
import org.oplearn.project.enums.RefundStatus;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RefundResponse {

  private Long id;
  private Long orderId;
  private Long userId;
  private BigDecimal amount;
  private String accountNumber;
  private String accountHolderName;
  private String bankName;
  private String bankCode;
  private String qrUrl;
  private RefundStatus status;
  private String reason;
  private String rejectReason;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public static RefundResponse from(Refund refund) {
    if (refund == null) {
      return null;
    }

    return RefundResponse.builder()
        .id(refund.getId())
        .orderId(refund.getOrderId())
        .userId(refund.getUserId())
        .amount(refund.getAmount())
        .accountNumber(refund.getAccountNumber())
        .accountHolderName(refund.getAccountHolderName())
        .bankName(refund.getBankName())
        .bankCode(refund.getBankCode())
        .qrUrl(refund.getQrUrl())
        .status(refund.getStatus())
        .reason(refund.getReason())
        .rejectReason(refund.getRejectReason())
        .isDeleted(refund.getIsDeleted())
        .createdBy(refund.getCreatedBy())
        .createdAt(refund.getCreatedAt())
        .updatedAt(refund.getUpdatedAt())
        .build();
  }
}
