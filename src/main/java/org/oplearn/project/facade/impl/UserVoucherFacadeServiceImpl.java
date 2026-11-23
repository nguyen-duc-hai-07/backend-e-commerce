package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.UserVoucherRequest;
import org.oplearn.project.dto.response.OrderVoucherResponse;
import org.oplearn.project.dto.response.UserVoucherResponse;
import org.oplearn.project.dto.response.VoucherResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.enums.VoucherTarget;
import org.oplearn.project.exception.VoucherAlreadyClaimedException;
import org.oplearn.project.exception.VoucherClaimLimitExceededException;
import org.oplearn.project.exception.VoucherUsagePeriodInvalidException;
import org.oplearn.project.facade.UserVoucherFacadeService;
import org.oplearn.project.service.UserService;
import org.oplearn.project.service.UserVoucherService;
import org.oplearn.project.service.VoucherService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserVoucherFacadeServiceImpl implements UserVoucherFacadeService {
  private final UserVoucherService userVoucherService;
  private final UserService userService;
  private final VoucherService voucherService;
  private final Clock clock;

  private User currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUsernameOrThrow(authentication.getName());
  }
  @Override
  @Transactional
  public UserVoucherResponse claimVoucher(UserVoucherRequest request) {
    log.info("(facade) claim voucher");

    User currentUser = currentUser();
    request.setUserId(currentUser.getId());

    if(userVoucherService.exists(request.getUserId(), request.getVoucherId())) {
      throw new VoucherAlreadyClaimedException();
    }

    VoucherResponse voucher = voucherService.detail(request.getVoucherId());
    Instant now = clock.instant();

    if (now.isBefore(voucher.getClaimStartDate()) || now.isAfter(voucher.getClaimEndDate())) {
      throw new VoucherUsagePeriodInvalidException();
    }

    if (voucher.getClaimedCount() >= voucher.getMaxTotalClaim()) {
      throw new VoucherClaimLimitExceededException();
    }

    UserVoucherResponse response = userVoucherService.claimVoucher(request);

    voucherService.decreaseQuantity(voucher.getId());

    return response;
  }

  @Override
  public List<OrderVoucherResponse> findAvailableVouchersForOrder(VoucherTarget target) {
    log.info("(facade) findAvailableVouchersForOrder");

    User currentUser = currentUser();

    return userVoucherService.findAvailableVouchersForOrder(currentUser.getId(), target, clock.instant());
  }
}
