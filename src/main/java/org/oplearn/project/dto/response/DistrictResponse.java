package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.District;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DistrictResponse {
  private String code;
  private String name;
  private String nameEn;
  private String fullName;
  private String fullNameEn;
  private String codeName;
  private String provinceCode;

  public static DistrictResponse from(District district) {
    if (district == null) {
      return null;
    }
    return DistrictResponse.builder()
        .code(district.getCode())
        .name(district.getName())
        .nameEn(district.getNameEn())
        .fullName(district.getFullName())
        .fullNameEn(district.getFullNameEn())
        .codeName(district.getCodeName())
        .provinceCode(district.getProvinceCode())
        .build();
  }
}
