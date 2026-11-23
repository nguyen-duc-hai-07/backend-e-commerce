package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Complaint;
import org.oplearn.project.enums.ComplaintStatus;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ComplaintResponse {

  private Long id;
  private Long userId;
  private Long orderId;
  private String content;
  private ComplaintStatus status;
  private String adminReply;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public static ComplaintResponse from(Complaint complaint) {
    if (complaint == null) {
      return null;
    }
    return ComplaintResponse.builder()
        .id(complaint.getId())
        .userId(complaint.getUserId())
        .orderId(complaint.getOrderId())
        .content(complaint.getContent())
        .status(complaint.getStatus())
        .adminReply(complaint.getAdminReply())
        .isDeleted(complaint.getIsDeleted())
        .createdBy(complaint.getCreatedBy())
        .createdAt(complaint.getCreatedAt())
        .updatedAt(complaint.getUpdatedAt())
        .build();
  }
}
