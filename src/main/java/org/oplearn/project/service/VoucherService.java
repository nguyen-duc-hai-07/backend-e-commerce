package org.oplearn.project.service;

import org.oplearn.project.dto.request.VoucherRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.VoucherResponse;

import java.math.BigDecimal;

public interface VoucherService {
  VoucherResponse create(VoucherRequest request);

  VoucherResponse detail(Long id);

  VoucherResponse findByCode(String code);

  void inactive(Long id);

  PageResponse<VoucherResponse> findClaimableVouchers(int page, int size);

  BigDecimal calculateDiscountProduct(Long voucherProductId, BigDecimal subtotal);

  BigDecimal calculateDiscountShipping(Long voucherShippingId, BigDecimal shippingFee , BigDecimal subtotal);

  void decreaseQuantity(Long id);
}
