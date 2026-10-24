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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MessageRequest {

  @NotNull(message = "message.conversation_id.not_null")
  private Long conversationId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long senderId;

  @NotBlank(message = "message.content.not_blank")
  @Size(max = 2000, message = "message.content.max_length")
  private String content;

  @Size(max = 500, message = "message.image_url.max_length")
  private String imageUrl;
}
