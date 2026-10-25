package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.MessageRequest;
import org.oplearn.project.dto.response.CursorPageResponse;
import org.oplearn.project.dto.response.MessageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.facade.MessFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.*;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.SIZE_DEFAULT;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {

  private final MessFacadeService facade;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<MessageResponse> create(@Valid @RequestBody MessageRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @DeleteMapping("/{id}")
  public ResponseGeneral<Void> delete(@PathVariable("id") Long id) {
    log.info("(delete) id: {}", id);
    facade.delete(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }

  @GetMapping("/conversation/{conversationId}")
  public ResponseGeneral<CursorPageResponse<MessageResponse>> findByConversationId(
      @PathVariable("conversationId") Long conversationId,
      @RequestParam(name = PARAM_CURSOR, required = false) Long cursor,
      @RequestParam(name = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size
  ) {
    log.info("(findByConversationId) conversationId: {}, cursor: {}, size: {}", conversationId, cursor, size);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.findByConversationId(conversationId, cursor, size));
  }
}
