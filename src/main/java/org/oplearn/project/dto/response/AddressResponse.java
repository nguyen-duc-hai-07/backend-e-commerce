package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Address;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressResponse {
  private Long id;
  private Long userId;
  private String recipientName;
  private String phoneNumber;
  private String provinceCode;
  private String districtCode;
  private String wardCode;
  private String streetAddress;
  private Boolean isDefault;

  public static AddressResponse from(Address address) {
    if (address == null) {
      return null;
    }
    return AddressResponse.builder()
        .id(address.getId())
        .userId(address.getUserId())
        .recipientName(address.getRecipientName())
        .phoneNumber(address.getPhoneNumber())
        .provinceCode(address.getProvinceCode())
        .districtCode(address.getDistrictCode())
        .wardCode(address.getWardCode())
        .streetAddress(address.getStreetAddress())
        .isDefault(address.getIsDefault())
        .build();
  }
}
