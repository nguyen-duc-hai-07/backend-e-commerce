package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.UserVoucherRequest;
import org.oplearn.project.dto.response.OrderVoucherResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.UserVoucherResponse;
import org.oplearn.project.enums.VoucherTarget;
import org.oplearn.project.facade.UserVoucherFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.CREATED_MESSAGE;
import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-vouchers")
public class UserVoucherController {

  private final UserVoucherFacadeService facade;

  @PostMapping("/claim")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<UserVoucherResponse> claimVoucher(@Valid @RequestBody UserVoucherRequest request) {
    log.info("(claimVoucher) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.claimVoucher(request));
  }

  @GetMapping({"/order"})
  public ResponseGeneral<List<OrderVoucherResponse>> findAvailableVouchersForOrder(
      @RequestParam(name = "target") VoucherTarget target
  ) {
    log.info("(findAvailableVouchersForOrder) target: {}", target);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.findAvailableVouchersForOrder(target));
  }
}
