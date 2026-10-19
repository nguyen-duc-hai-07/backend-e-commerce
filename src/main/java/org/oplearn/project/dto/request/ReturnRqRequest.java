package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.enums.ReturnResolutionType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReturnRqRequest {

  @NotNull(message = "return_request.order_id.not_null")
  private Long orderId;

  @NotBlank(message = "return_request.reason.not_blank")
  @Size(max = 255, message = "return_request.reason.max_length")
  private String reason;

  private String description;

  private ReturnResolutionType resolutionType;

  @Valid
  @Size(max = 5, message = "return_request.images.max_size")
  private List<ReturnRqImageRequest> images;
}
