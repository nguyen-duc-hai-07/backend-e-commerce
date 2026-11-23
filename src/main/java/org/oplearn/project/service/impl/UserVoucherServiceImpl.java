package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.UserVoucherRequest;
import org.oplearn.project.dto.response.OrderVoucherResponse;
import org.oplearn.project.dto.response.UserVoucherResponse;
import org.oplearn.project.entity.UserVoucher;
import org.oplearn.project.enums.UserVoucherStatus;
import org.oplearn.project.enums.VoucherTarget;
import org.oplearn.project.exception.UserVoucherNotFoundException;
import org.oplearn.project.exception.VoucherAlreadyUsedException;
import org.oplearn.project.repository.UserVoucherRepository;
import org.oplearn.project.service.UserVoucherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserVoucherServiceImpl implements UserVoucherService {
  private final UserVoucherRepository repository;
  private final Clock clock;
  @Override
  @Transactional
  public UserVoucherResponse claimVoucher(UserVoucherRequest request) {
    log.info("(service) claim voucher");

    UserVoucher userVoucher = UserVoucher.builder()
      .userId(request.getUserId())
      .voucherId(request.getVoucherId())
      .claimedAt(clock.instant())
      .build();

    return UserVoucherResponse.from(repository.save(userVoucher));
  }

  @Override
  public void useVoucher(Long userId ,Long voucherId , Long orderId) {
    UserVoucher userVoucher = repository.findByUserIdAndVoucherIdAndIsDeletedFalse(userId , voucherId)
        .orElseThrow(UserVoucherNotFoundException::new);

    if (userVoucher.getStatus() == UserVoucherStatus.USED) {
      throw new VoucherAlreadyUsedException();
    }

    userVoucher.setOrderId(orderId);
    userVoucher.setUsedAt(clock.instant());
    userVoucher.setStatus(UserVoucherStatus.USED);

    repository.save(userVoucher);
  }

  @Override
  public boolean exists(Long userId, Long voucherId) {
    return repository.existsByUserIdAndVoucherId(userId, voucherId);
  }

  @Override
  public void refundVoucher(Long orderId) {
    log.info("(service) refund voucher for orderId: {}", orderId);

    List<UserVoucher> userVouchers = repository.findByOrderIdAndIsDeletedFalse(orderId);
    if (userVouchers.isEmpty()) {
      return;
    }

    for (UserVoucher uv : userVouchers) {
      uv.setStatus(UserVoucherStatus.UNUSED);
      uv.setOrderId(null);
      uv.setUsedAt(null);
    }

    repository.saveAll(userVouchers);
  }

  @Override
  public List<OrderVoucherResponse> findAvailableVouchersForOrder(Long userId, VoucherTarget target, Instant now) {
    log.info("(service) findAvailableVouchersForOrder");

    return repository.findAvailableVouchersForOrder(userId, target, now);
  }
}
