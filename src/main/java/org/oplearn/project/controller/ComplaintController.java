package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ComplaintRequest;
import org.oplearn.project.dto.request.ComplaintUpdateRequest;
import org.oplearn.project.dto.response.ComplaintResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.enums.ComplaintStatus;
import org.oplearn.project.facade.ComplaintFacadeService;
import org.oplearn.project.service.ComplaintService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.*;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.PAGE_DEFAULT;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.SIZE_DEFAULT;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/complaints")
public class ComplaintController {

  private final ComplaintFacadeService facade;
  private final ComplaintService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<ComplaintResponse> create(@Valid @RequestBody ComplaintRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<ComplaintResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.detail(id));
  }

  @GetMapping("/user")
  public ResponseGeneral<PageResponse<ComplaintResponse>> filterByUserId(
      @RequestParam(value = PARAM_STATUS, required = false) ComplaintStatus status,
      @RequestParam(name = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size,
      @RequestParam(name = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page
  ) {
    log.info("(filterByUserId) status: {}, size: {}, page: {}", status, size, page);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.filterByUserId(status, size, page));
  }

  @GetMapping("/admin")
  public ResponseGeneral<PageResponse<ComplaintResponse>> filterForAdmin(
      @RequestParam(value = PARAM_STATUS, required = false) ComplaintStatus status,
      @RequestParam(name = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size,
      @RequestParam(name = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page
  ) {
    log.info("(filterForAdmin) status: {}, size: {}, page: {}", status, size, page);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.filter(status, size, page));
  }

  @PutMapping("/{id}/admin")
  public ResponseGeneral<ComplaintResponse> updateAdmin(
      @PathVariable("id") Long id,
      @Valid @RequestBody ComplaintUpdateRequest request
  ) {
    log.info("(updateAdmin) id: {}, request: {}", id, request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.update(request, id));
  }

  @DeleteMapping("/{id}")
  public ResponseGeneral<Void> delete(@PathVariable("id") Long id) {
    log.info("(delete) id: {}", id);
    facade.delete(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }
}
