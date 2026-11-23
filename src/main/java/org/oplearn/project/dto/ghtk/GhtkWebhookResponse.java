package org.oplearn.project.dto.ghtk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GhtkWebhookResponse {

  private boolean success;
  private String message;

  public static GhtkWebhookResponse ok() {
    return new GhtkWebhookResponse(true, "Webhook processed successfully");
  }

  public static GhtkWebhookResponse error(String message) {
    return new GhtkWebhookResponse(false, message);
  }
}
