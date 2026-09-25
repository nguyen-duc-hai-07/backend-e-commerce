package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressRequest {

  private Long userId;

  @NotBlank(message = "address.recipient_name.not_blank")
  private String recipientName;

  @NotBlank(message = "address.phone_number.not_blank")
  private String phoneNumber;

  @NotBlank(message = "address.province.not_blank")
  private String province;

  @NotBlank(message = "address.district.not_blank")
  private String district;

  @NotBlank(message = "address.ward.not_blank")
  private String ward;

  @NotBlank(message = "address.street_address.not_blank")
  private String streetAddress;

  private Boolean isDefault;
}
