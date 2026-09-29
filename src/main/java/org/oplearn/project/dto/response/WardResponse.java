package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Ward;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WardResponse {
  private String code;
  private String name;
  private String nameEn;
  private String fullName;
  private String fullNameEn;
  private String codeName;
  private String districtCode;

  public static WardResponse from(Ward ward) {
    if (ward == null) {
      return null;
    }
    return WardResponse.builder()
        .code(ward.getCode())
        .name(ward.getName())
        .nameEn(ward.getNameEn())
        .fullName(ward.getFullName())
        .fullNameEn(ward.getFullNameEn())
        .codeName(ward.getCodeName())
        .districtCode(ward.getDistrictCode())
        .build();
  }
}
