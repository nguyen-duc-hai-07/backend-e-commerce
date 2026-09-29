package org.oplearn.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "provinces")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Province {

  @Id
  private String code;
  private String name;
  private String nameEn;
  private String fullName;
  private String fullNameEn;
  private String codeName;
  private Integer administrativeUnitId;
  private Integer administrativeRegionId;
}
