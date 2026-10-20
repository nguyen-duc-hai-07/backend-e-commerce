package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.VoucherRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.VoucherResponse;
import org.oplearn.project.service.VoucherService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.*;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/vouchers")
public class VoucherController {

  private final VoucherService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<VoucherResponse> create(@Valid @RequestBody VoucherRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, service.create(request));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<VoucherResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.detail(id));
  }

  @GetMapping("/code/{code}")
  public ResponseGeneral<VoucherResponse> findByCode(@PathVariable("code") String code) {
    log.info("(findByCode) code: {}", code);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.findByCode(code));
  }

  @PutMapping("/{id}/inactive")
  public ResponseGeneral<Void> inactive(@PathVariable("id") Long id) {
    log.info("(inactive) id: {}", id);
    service.inactive(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }

  @GetMapping("/claimable")
  public ResponseGeneral<PageResponse<VoucherResponse>> findClaimableVouchers(
      @RequestParam(name = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page,
      @RequestParam(name = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size
  ) {
    log.info("(findClaimableVouchers) page: {}, size: {}", page, size);
    size = Math.min(size, MAX_PAGE_SIZE);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.findClaimableVouchers(page, size));
  }
}
