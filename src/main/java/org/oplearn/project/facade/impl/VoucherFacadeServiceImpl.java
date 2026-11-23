package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.VoucherRequest;
import org.oplearn.project.dto.response.VoucherResponse;
import org.oplearn.project.facade.VoucherFacadeService;
import org.oplearn.project.service.NotificationService;
import org.oplearn.project.service.VoucherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoucherFacadeServiceImpl implements VoucherFacadeService {

  private final VoucherService voucherService;
  private final NotificationService notificationService;

  @Override
  @Transactional
  public VoucherResponse create(VoucherRequest request) {
    log.info("(facade) create voucher: {}", request);
    VoucherResponse response = voucherService.create(request);

    notificationService.createAndSendVoucher(response);

    return response;
  }
}
