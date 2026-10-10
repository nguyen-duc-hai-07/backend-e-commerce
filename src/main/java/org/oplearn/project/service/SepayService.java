package org.oplearn.project.service;

import org.oplearn.project.dto.request.SepayWebhookRequest;
import org.oplearn.project.dto.response.SepayWebhookResponse;

public interface SepayService {
  SepayWebhookResponse handleWebhook(SepayWebhookRequest request , String authHeader);
}
