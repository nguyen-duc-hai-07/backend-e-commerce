package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.oplearn.project.entity.Address;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressResponse {
  private Long id;
  private Long userId;
  private String recipientName;
  private String phoneNumber;
  private String province;
  private String district;
  private String ward;
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
        .province(address.getProvince())
        .district(address.getDistrict())
        .ward(address.getWard())
        .streetAddress(address.getStreetAddress())
        .isDefault(address.getIsDefault())
        .build();
  }
}
