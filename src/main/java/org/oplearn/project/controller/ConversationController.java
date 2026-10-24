package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ConversationRequest;
import org.oplearn.project.dto.request.ConversationUpdateRequest;
import org.oplearn.project.dto.response.ConversationResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.facade.ConversationFacadeService;
import org.oplearn.project.service.ConversationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.*;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.PAGE_DEFAULT;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.SIZE_DEFAULT;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversations")
public class ConversationController {

  private final ConversationFacadeService facade;
  private final ConversationService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<ConversationResponse> create(@Valid @RequestBody ConversationRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<ConversationResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.detail(id));
  }

  @PutMapping("/{id}")
  public ResponseGeneral<ConversationResponse> update(
      @PathVariable("id") Long id,
      @Valid @RequestBody ConversationUpdateRequest request
  ) {
    log.info("(update) id: {}, request: {}", id, request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.update(request, id));
  }

  @DeleteMapping("/{id}")
  public ResponseGeneral<Void> delete(@PathVariable("id") Long id) {
    log.info("(delete) id: {}", id);
    service.delete(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }

  @GetMapping("/search")
  public ResponseGeneral<PageResponse<ConversationResponse>> search(
      @RequestParam(name = PARAM_KEYWORD, required = false) String keyword,
      @RequestParam(name = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size,
      @RequestParam(name = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page
  ) {
    log.info("(search) keyword: {}, size: {}, page: {}", keyword, size, page);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.search(keyword, size, page));
  }

  @GetMapping("/filter")
  public ResponseGeneral<PageResponse<ConversationResponse>> filter(
      @RequestParam(name = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size,
      @RequestParam(name = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page
  ) {
    log.info("(filter) size: {}, page: {}", size, page);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.filter(size, page));
  }

  @GetMapping("/broadcasts")
  public ResponseGeneral<List<ConversationResponse>> findAllTypeBroadcast() {
    log.info("(findAllTypeBroadcast)");
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.findAllTypeBroadcast());
  }
}
