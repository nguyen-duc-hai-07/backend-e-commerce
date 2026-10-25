package org.oplearn.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.enums.NotificationType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationRequest {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long senderId;

  @NotNull(message = "notification.type.not_null")
  private NotificationType type;

  @NotBlank(message = "notification.title.not_blank")
  @Size(max = 255, message = "notification.title.max_length")
  private String title;

  @NotBlank(message = "notification.content.not_blank")
  private String content;
}
