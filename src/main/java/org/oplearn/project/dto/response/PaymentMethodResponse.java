package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.PaymentMethod;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentMethodResponse {

  private Long id;
  private String name;
  private String code;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public static PaymentMethodResponse from(PaymentMethod paymentMethod) {
    if (paymentMethod == null) {
      return null;
    }
    return PaymentMethodResponse.builder()
        .id(paymentMethod.getId())
        .name(paymentMethod.getName())
        .code(paymentMethod.getCode())
        .isDeleted(paymentMethod.getIsDeleted())
        .createdBy(paymentMethod.getCreatedBy())
        .createdAt(paymentMethod.getCreatedAt())
        .updatedAt(paymentMethod.getUpdatedAt())
        .build();
  }
}
