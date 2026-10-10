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
public class PaymentQrResponse {
  private String qrCodeUrl;       // Link ảnh VietQR
  private String bankName;        // Tên ngân hàng
  private String accountNumber;   // Số tài khoản
  private String accountName;     // Chủ tài khoản
  private BigDecimal amount;      // Số tiền cần chuyển
  private String transferContent; // Cú pháp chuyển khoản bắt buộc
  private Instant expiresAt;      // thời gian hết hạn qr
}
