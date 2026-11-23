package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.VoucherRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.VoucherResponse;
import org.oplearn.project.entity.Voucher;
import org.oplearn.project.enums.DiscountType;
import org.oplearn.project.enums.VoucherTarget;
import org.oplearn.project.exception.VoucherMinOrderValueNotReachedException;
import org.oplearn.project.exception.VoucherNotFoundException;
import org.oplearn.project.exception.VoucherTargetMismatchException;
import org.oplearn.project.exception.VoucherUsagePeriodInvalidException;
import org.oplearn.project.repository.VoucherRepository;
import org.oplearn.project.service.VoucherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
  private final VoucherRepository repository;
  private final Clock clock;

  @Override
  @Transactional
  public VoucherResponse create(VoucherRequest request) {
    log.info("(create) voucher");

    String code = generateUniqueVoucherCode();

    Voucher voucher = Voucher.builder()
      .campaignCode(request.getCampaignCode().trim())
      .code(code)
      .target(request.getTarget() != null ? request.getTarget() : VoucherTarget.PRODUCT)
      .discountType(request.getDiscountType())
      .discountValue(request.getDiscountValue())
      .maxDiscountAmount(request.getMaxDiscountAmount())
      .minOrderValue(request.getMinOrderValue() != null ? request.getMinOrderValue() : BigDecimal.ZERO)
      .maxTotalClaim(request.getMaxTotalClaim())
      .claimedCount(0)
      .claimStartDate(request.getClaimStartDate())
      .claimEndDate(request.getClaimEndDate())
      .useStartDate(request.getUseStartDate())
      .useEndDate(request.getUseEndDate())
      .isActive(Boolean.TRUE)
      .build();

    Voucher saved = repository.save(voucher);
    return VoucherResponse.from(saved);
  }

  @Override
  public VoucherResponse detail(Long id) {
    return repository.findByIdAndIsDeletedFalseAndIsActiveTrue(id)
      .map(VoucherResponse::from)
      .orElseThrow(VoucherNotFoundException::new);
  }

  @Override
  public VoucherResponse findByCode(String code) {
    return repository.findByCodeAndIsDeletedFalseAndIsActiveTrue(code)
      .map(VoucherResponse::from)
      .orElseThrow(VoucherNotFoundException::new);
  }

  @Override
  @Transactional
  public void inactive(Long id) {
    this.detail(id);

    repository.setInactiveById(id);
  }

  @Override
  public PageResponse<VoucherResponse> findClaimableVouchers(int page, int size) {
    log.info("(findClaimableVouchers) page: {}, size: {}", page, size);

    Instant now = clock.instant();

    Pageable pageable = PageRequest.of(page, size);

    Page<Voucher> vouchers = repository.findClaimableVouchers(now, pageable);
    return PageResponse.of(vouchers.map(VoucherResponse::from));
  }

  @Override
  public BigDecimal calculateDiscountProduct(Long voucherProductId, BigDecimal subtotal) {
    log.info("(calculateDiscountProduct) voucherProductId: {}, subtotal: {}",
      voucherProductId, subtotal
    );

    VoucherResponse voucher = detail(voucherProductId);

    if (voucher.getTarget() != VoucherTarget.PRODUCT) {
      throw new VoucherTargetMismatchException();
    }

    Instant now = clock.instant();
    if (now.isBefore(voucher.getUseStartDate()) || now.isAfter(voucher.getUseEndDate())) {
      throw new VoucherUsagePeriodInvalidException();
    }

    if (subtotal == null || subtotal.compareTo(voucher.getMinOrderValue()) < 0) {
      throw new VoucherMinOrderValueNotReachedException();
    }

    return calculate(voucher, subtotal).min(subtotal);
  }

  @Override
  public BigDecimal calculateDiscountShipping(Long voucherShippingId, BigDecimal shippingFee, BigDecimal subtotal) {
    log.info("(calculateDiscountShipping) voucherShippingId: {}, shippingFee: {}, subtotal: {}",
      voucherShippingId, shippingFee, subtotal
    );

    if (shippingFee == null || shippingFee.compareTo(BigDecimal.ZERO) <= 0) {
      return BigDecimal.ZERO;
    }

    VoucherResponse voucher = detail(voucherShippingId);

    if (voucher.getTarget() != VoucherTarget.SHIPPING) {
      throw new VoucherTargetMismatchException();
    }

    Instant now = clock.instant();
    if (now.isBefore(voucher.getUseStartDate()) || now.isAfter(voucher.getUseEndDate())) {
      throw new VoucherUsagePeriodInvalidException();
    }

    if (subtotal == null || subtotal.compareTo(voucher.getMinOrderValue()) < 0) {
      throw new VoucherMinOrderValueNotReachedException();
    }

    return calculate(voucher, shippingFee).min(shippingFee);
  }

  @Override
  public void decreaseQuantity(Long id) {
    log.info("(decreaseQuantity) id: {}", id);

    Voucher voucher = this.findById(id);

    voucher.setClaimedCount(voucher.getClaimedCount() + 1);

    repository.save(voucher);
  }

  private BigDecimal calculate(VoucherResponse voucher , BigDecimal typeDiscount) {
    BigDecimal discount;
    if (voucher.getDiscountType() == DiscountType.PERCENT) {
      discount = typeDiscount.multiply(voucher.getDiscountValue())
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

      if (voucher.getMaxDiscountAmount() != null && discount.compareTo(voucher.getMaxDiscountAmount()) > 0) {
        discount = voucher.getMaxDiscountAmount();
      }
    } else {
      discount = voucher.getDiscountValue();
    }

    return discount;
  }

  private String generateUniqueVoucherCode() {
    String code;
    do {
      code = "VC" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    } while (repository.existsByCode(code));
    return code;
  }

  private Voucher findById(Long id) {
    return repository.findByIdAndIsDeletedFalseAndIsActiveTrue(id)
      .orElseThrow(VoucherNotFoundException::new);
  }
}
