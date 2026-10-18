package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.RefundFilterRequest;
import org.oplearn.project.dto.request.RefundRequest;
import org.oplearn.project.dto.request.RefundUpdateRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.RefundResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.enums.RefundStatus;
import org.oplearn.project.facade.RefundFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.*;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.PAGE_DEFAULT;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.SIZE_DEFAULT;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/refunds")
public class RefundController {

  private final RefundFacadeService facade;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<RefundResponse> create(@Valid @RequestBody RefundRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<RefundResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.detail(id));
  }

  @PutMapping("/{id}")
  public ResponseGeneral<RefundResponse> update(
      @PathVariable("id") Long id,
      @Valid @RequestBody RefundUpdateRequest request
  ) {
    log.info("(update) id: {}, request: {}", id, request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.update(id, request));
  }

  @PostMapping("/filter")
  public ResponseGeneral<PageResponse<RefundResponse>> filter(
      @Valid @RequestBody RefundFilterRequest request
  ) {
    log.info("(filter) request: {}", request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.filter(request));
  }

  @GetMapping("/admin")
  public ResponseGeneral<PageResponse<RefundResponse>> listForAdmin(
      @RequestParam(value = "status", required = false) RefundStatus status,
      @RequestParam(name = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page,
      @RequestParam(name = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size
  ) {
    log.info("(listForAdmin) status: {}, page: {}, size: {}", status, page, size);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.filterForAdmin(status, page, size));
  }
}
