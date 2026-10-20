package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ReturnRqRequest;
import org.oplearn.project.dto.request.ReturnRqUpdateRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.ReturnRequestResponse;
import org.oplearn.project.enums.ReturnRequestStatus;
import org.oplearn.project.enums.ReturnResolutionType;
import org.oplearn.project.facade.ReturnRqFacadeService;
import org.oplearn.project.service.ReturnRqService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.*;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.PAGE_DEFAULT;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.SIZE_DEFAULT;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/return-requests")
public class ReturnRqController {

  private final ReturnRqFacadeService facade;
  private final ReturnRqService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<ReturnRequestResponse> create(@Valid @RequestBody ReturnRqRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<ReturnRequestResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.detail(id));
  }

  @GetMapping("/user")
  public ResponseGeneral<PageResponse<ReturnRequestResponse>> filterByUserId(
      @RequestParam(name = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size,
      @RequestParam(name = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page
  ) {
    log.info("(filterByUserId) size: {}, page: {}", size, page);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.filterByUserId(size, page));
  }

  @GetMapping("/admin")
  public ResponseGeneral<PageResponse<ReturnRequestResponse>> filterForAdmin(
      @RequestParam(value = PARAM_STATUS, required = false) ReturnRequestStatus status,
      @RequestParam(value = "type", required = false) ReturnResolutionType type,
      @RequestParam(name = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size,
      @RequestParam(name = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page
  ) {
    log.info("(filterForAdmin) status: {}, type: {}, size: {}, page: {}", status, type, size, page);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.filter(status, type, size, page));
  }

  @PutMapping("/{id}/admin")
  public ResponseGeneral<Void> updateAdmin(
      @PathVariable("id") Long id,
      @Valid @RequestBody ReturnRqUpdateRequest request
  ) {
    log.info("(updateAdmin) id: {}, request: {}", id, request);
    facade.updateAdmin(request, id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }

  @DeleteMapping("/{id}")
  public ResponseGeneral<Void> delete(@PathVariable("id") Long id) {
    log.info("(delete) id: {}", id);
    facade.delete(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }
}
