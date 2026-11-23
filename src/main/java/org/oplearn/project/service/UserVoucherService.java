package org.oplearn.project.service;

import org.oplearn.project.dto.request.UserVoucherRequest;
import org.oplearn.project.dto.response.OrderVoucherResponse;
import org.oplearn.project.dto.response.UserVoucherResponse;
import org.oplearn.project.enums.VoucherTarget;

import java.time.Instant;
import java.util.List;

public interface UserVoucherService {
  UserVoucherResponse claimVoucher(UserVoucherRequest request);

  void useVoucher(Long userId, Long voucherId, Long orderId);

  boolean exists(Long userId, Long voucherId);

  void refundVoucher(Long orderId);

  List<OrderVoucherResponse> findAvailableVouchersForOrder(Long userId , VoucherTarget target , Instant now);
}
