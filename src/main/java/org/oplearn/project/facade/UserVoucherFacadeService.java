package org.oplearn.project.facade;

import org.oplearn.project.dto.request.UserVoucherRequest;
import org.oplearn.project.dto.response.OrderVoucherResponse;
import org.oplearn.project.dto.response.UserVoucherResponse;
import org.oplearn.project.enums.VoucherTarget;

import java.util.List;

public interface UserVoucherFacadeService {
  UserVoucherResponse claimVoucher(UserVoucherRequest request);

  List<OrderVoucherResponse> findAvailableVouchersForOrder(VoucherTarget target);
}
