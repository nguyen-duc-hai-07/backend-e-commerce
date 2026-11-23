package org.oplearn.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.SepayWebhookRequest;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.SepayWebhookResponse;
import org.oplearn.project.service.SepayService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
public class SepayWebhookController {

  private final SepayService sepayService;

  @PostMapping("/sepay")
  public ResponseGeneral<SepayWebhookResponse> handleWebhook(
      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader,
      @RequestBody SepayWebhookRequest request
  ) {
    log.info("(handleWebhook) transactionId: {}, amount: {}, content: {}",
        request.getId(), request.getTransferAmount(), request.getContent()
    );

    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, sepayService.handleWebhook(request, authorizationHeader));
  }
}
