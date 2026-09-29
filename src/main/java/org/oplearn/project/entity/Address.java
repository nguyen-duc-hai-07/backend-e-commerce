package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "recipient_name", nullable = false)
  private String recipientName;

  @Column(name = "phone_number", length = 50, nullable = false)
  private String phoneNumber;

  @Column(name = "province_code", length = 20, nullable = false)
  private String provinceCode;

  @Column(name = "district_code", length = 20, nullable = false)
  private String districtCode;

  @Column(name = "ward_code", length = 20, nullable = false)
  private String wardCode;

  @Column(name = "street_address", nullable = false)
  private String streetAddress;

  @Column(name = "is_default", nullable = false)
  @Builder.Default
  private Boolean isDefault = false;
}
