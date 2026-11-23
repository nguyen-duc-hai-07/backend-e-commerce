package org.oplearn.project.dto.ghtk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GhtkCancelResponse {

  private Boolean success;

  private String message;

  @JsonProperty("log_id")
  private String logId;

  private Integer code;
}
