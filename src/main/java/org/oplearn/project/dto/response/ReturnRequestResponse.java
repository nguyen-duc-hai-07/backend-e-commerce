package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.ReturnRequest;
import org.oplearn.project.enums.ReturnRequestStatus;
import org.oplearn.project.enums.ReturnResolutionType;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReturnRequestResponse {

  private Long id;
  private Long orderId;
  private String reason;
  private String description;
  private ReturnResolutionType resolutionType;
  private ReturnRequestStatus status;
  private String rejectReason;
  private List<ReturnRequestImageResponse> images;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public static ReturnRequestResponse from(ReturnRequest returnRequest) {
    return from(returnRequest, null);
  }

  public static ReturnRequestResponse from(ReturnRequest returnRequest, List<ReturnRequestImageResponse> images) {
    if (returnRequest == null) {
      return null;
    }
    return ReturnRequestResponse.builder()
        .id(returnRequest.getId())
        .orderId(returnRequest.getOrderId())
        .reason(returnRequest.getReason())
        .description(returnRequest.getDescription())
        .resolutionType(returnRequest.getResolutionType())
        .status(returnRequest.getStatus())
        .rejectReason(returnRequest.getRejectReason())
        .images(images != null ? images : List.of())
        .isDeleted(returnRequest.getIsDeleted())
        .createdBy(returnRequest.getCreatedBy())
        .createdAt(returnRequest.getCreatedAt())
        .updatedAt(returnRequest.getUpdatedAt())
        .build();
  }
}
