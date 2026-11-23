package org.oplearn.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.ghtk.GhtkWebhookRequest;
import org.oplearn.project.dto.ghtk.GhtkWebhookResponse;
import org.oplearn.project.service.ghtk.GhtkWebhookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
public class GhtkWebhookController {

  private final GhtkWebhookService ghtkWebhookService;

  @PostMapping("/ghtk")
  public GhtkWebhookResponse handleGhtkWebhook(@RequestBody GhtkWebhookRequest payload) {
    log.info("(handleGhtkWebhook) Nhận webhook: partnerId={}, statusId={}",
        payload.getPartnerId(), payload.getStatusId());
    return ghtkWebhookService.handleWebhook(payload);
  }
}
