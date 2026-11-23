package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.oplearn.project.entity.ReturnRequestImage;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReturnRequestImageResponse {

  private Long id;
  private Long returnRequestId;
  private String imageUrl;
  private Integer displayOrder;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public static ReturnRequestImageResponse from(ReturnRequestImage image) {
    if (image == null) {
      return null;
    }
    return ReturnRequestImageResponse.builder()
        .id(image.getId())
        .returnRequestId(image.getReturnRequestId())
        .imageUrl(image.getImageUrl())
        .displayOrder(image.getDisplayOrder())
        .isDeleted(image.getIsDeleted())
        .createdBy(image.getCreatedBy())
        .createdAt(image.getCreatedAt())
        .updatedAt(image.getUpdatedAt())
        .build();
  }
}
