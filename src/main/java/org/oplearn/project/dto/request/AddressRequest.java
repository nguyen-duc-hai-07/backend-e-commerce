package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressRequest {

  @NotBlank(message = "address.recipient_name.not_blank")
  private String recipientName;

  @NotBlank(message = "address.phone_number.not_blank")
  private String phoneNumber;

  @NotBlank(message = "address.province_code.not_blank")
  private String provinceCode;

  @NotBlank(message = "address.district_code.not_blank")
  private String districtCode;

  @NotBlank(message = "address.ward_code.not_blank")
  private String wardCode;

  @NotBlank(message = "address.street_address.not_blank")
  private String streetAddress;

  private Boolean isDefault;
}
