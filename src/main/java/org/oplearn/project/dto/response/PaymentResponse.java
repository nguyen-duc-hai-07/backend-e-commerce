package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Payment;
import org.oplearn.project.entity.PaymentMethod;
import org.oplearn.project.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentResponse {
  private Long id;
  private PaymentStatus status;
  private Long orderId;
  private BigDecimal amount;
  private String transactionCode;
  private PaymentMethodResponse paymentMethod;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public PaymentResponse(
    Long id,
    PaymentStatus status,
    Long orderId,
    BigDecimal amount,
    String transactionCode,
    PaymentMethod pm
  ) {
    this.id = id;
    this.status = status;
    this.orderId = orderId;
    this.amount = amount;
    this.transactionCode = transactionCode;
    this.paymentMethod = PaymentMethodResponse.from(pm);
  }

  public PaymentResponse(
    Long id,
    PaymentStatus status,
    Long orderId,
    BigDecimal amount,
    String transactionCode,
    PaymentMethod pm,
    Boolean isDeleted,
    String createdBy,
    Instant createdAt,
    Instant updatedAt
  ) {
    this.id = id;
    this.status = status;
    this.orderId = orderId;
    this.amount = amount;
    this.transactionCode = transactionCode;
    this.paymentMethod = PaymentMethodResponse.from(pm);
    this.isDeleted = isDeleted;
    this.createdBy = createdBy;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static PaymentResponse from(Payment payment) {
    if (payment == null) {
      return null;
    }

    return PaymentResponse.builder()
      .id(payment.getId())
      .status(payment.getStatus())
      .orderId(payment.getOrderId())
      .amount(payment.getAmount())
      .transactionCode(payment.getTransactionCode())
      .isDeleted(payment.getIsDeleted())
      .createdBy(payment.getCreatedBy())
      .createdAt(payment.getCreatedAt())
      .updatedAt(payment.getUpdatedAt())
      .build();
  }

  public static PaymentResponse from(Payment payment, PaymentMethodResponse paymentMethod) {
    PaymentResponse response = from(payment);
    if (response != null) {
      response.setPaymentMethod(paymentMethod);
    }
    return response;
  }
}
