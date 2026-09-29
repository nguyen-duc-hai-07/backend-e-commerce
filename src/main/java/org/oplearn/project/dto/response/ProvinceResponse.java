package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Province;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProvinceResponse {
  private String code;
  private String name;
  private String nameEn;
  private String fullName;
  private String fullNameEn;
  private String codeName;

  public static ProvinceResponse from(Province province) {
    if (province == null) {
      return null;
    }
    return ProvinceResponse.builder()
        .code(province.getCode())
        .name(province.getName())
        .nameEn(province.getNameEn())
        .fullName(province.getFullName())
        .fullNameEn(province.getFullNameEn())
        .codeName(province.getCodeName())
        .build();
  }
}
