package org.oplearn.project.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SepayWebhookRequest {
  private Long id;                  // ID giao dịch trên SePay (VD: 92704)
  private String gateway;           // Tên ngân hàng (VD: Vietcombank, MBBank)

  @JsonProperty("transactionDate")
  @JsonAlias({"transactionDate", "transaction_date"})
  private String transactionDate;   // Thời gian giao dịch (VD: 2026-10-07 14:02:30)

  @JsonProperty("accountNumber")
  @JsonAlias({"accountNumber", "account_number"})
  private String accountNumber;     // Số tài khoản ngân hàng nhận

  private String code;              // Mã code thanh toán SePay (nếu có)
  private String content;           // Nội dung chuyển khoản thực tế (VD: "ORD123456")

  @JsonProperty("transferType")
  @JsonAlias({"transferType", "transfer_type"})
  private String transferType;      // "in" (tiền vào) hoặc "out" (tiền ra)

  @JsonProperty("transferAmount")
  @JsonAlias({"transferAmount", "transfer_amount"})
  private BigDecimal transferAmount;// Số tiền giao dịch

  private BigDecimal accumulated;   // Số dư lũy kế

  @JsonProperty("subAccount")
  @JsonAlias({"subAccount", "sub_account"})
  private String subAccount;        // Tài khoản phụ

  @JsonProperty("referenceCode")
  @JsonAlias({"referenceCode", "reference_code"})
  private String referenceCode;     // Mã tham chiếu ngân hàng (FT24...)

  private String description;       // Chi tiết biến động
}
