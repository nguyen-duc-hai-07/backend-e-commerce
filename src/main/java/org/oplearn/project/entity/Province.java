package org.oplearn.project.entity;

import jakarta.persistence.Column;
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
  @Column(name = "code", length = 20, nullable = false)
  private String code;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "name_en")
  private String nameEn;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "full_name_en")
  private String fullNameEn;

  @Column(name = "code_name")
  private String codeName;

  @Column(name = "administrative_unit_id")
  private Integer administrativeUnitId;

  @Column(name = "administrative_region_id")
  private Integer administrativeRegionId;
}
