package org.oplearn.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SepayWebhookResponse {
  private boolean success;
  private String message;

  public static SepayWebhookResponse ok(String message) {
    return new SepayWebhookResponse(true, message);
  }

  public static SepayWebhookResponse error(String message) {
    return new SepayWebhookResponse(false, message);
  }
}
