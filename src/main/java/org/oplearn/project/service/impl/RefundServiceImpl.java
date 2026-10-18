package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.RefundFilterRequest;
import org.oplearn.project.dto.request.RefundRequest;
import org.oplearn.project.dto.request.RefundUpdateRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.RefundResponse;
import org.oplearn.project.entity.Refund;
import org.oplearn.project.enums.RefundStatus;
import org.oplearn.project.exception.RefundLimitExceededException;
import org.oplearn.project.exception.RefundNotFoundException;
import org.oplearn.project.repository.RefundRepository;
import org.oplearn.project.service.RefundService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {
  private final RefundRepository repository;

  @Override
  @Transactional
  public RefundResponse create(RefundRequest request) {
    log.info("(service) create refund request: {}", request);

    if(repository.countByUserIdAndOrderIdAndIsDeletedFalse(request.getUserId(), request.getOrderId()) > 3) {
      throw new RefundLimitExceededException();
    }

    String qrUrl = buildQrUrl(
      request.getBankCode(),
      request.getAccountNumber(),
      request.getAccountHolderName(),
      request.getAmount(),
      request.getOrderId()
    );

    Refund refund = Refund.builder()
      .orderId(request.getOrderId())
      .userId(request.getUserId())
      .amount(request.getAmount())
      .accountNumber(request.getAccountNumber())
      .accountHolderName(request.getAccountHolderName())
      .bankName(request.getBankName())
      .bankCode(request.getBankCode())
      .qrUrl(qrUrl)
      .status(RefundStatus.PENDING)
      .reason(request.getReason())
      .build();

    return RefundResponse.from(repository.save(refund));
  }

  @Override
  public RefundResponse detail(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
      .map(RefundResponse::from)
      .orElseThrow(RefundNotFoundException::new);
  }

  @Override
  public RefundResponse update(Long id, RefundUpdateRequest request) {
    log.info("(service) update refund request: {}", request);

    Refund refund = repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(RefundNotFoundException::new);

    refund.setStatus(request.getStatus());

    if(refund.getStatus() == RefundStatus.SUCCESS) {
      refund.setRejectReason(null);
    } else {
      refund.setRejectReason(request.getRejectReason());
    }

    Refund updatedRefund = repository.save(refund);

    return RefundResponse.from(updatedRefund);
  }

  @Override
  public PageResponse<RefundResponse> filter(RefundFilterRequest request) {
    log.info("(service) filter refund request: {}", request);

    int page = (request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
    int size = (request.getSize() != null && request.getSize() > 0) ? request.getSize() : 10;
    Pageable pageable = PageRequest.of(page, size);

    Page<Refund> refunds = repository.filter(
        request.getUserId(),
        request.getStatus(),
        pageable
    );

    return PageResponse.of(refunds.map(RefundResponse::from));
  }

  @Override
  public PageResponse<RefundResponse> filterForAdmin(RefundStatus status, Integer page, Integer size) {
    log.info("(service) filterForAdmin status: {}, page: {}, size: {}", status, page, size);

    int pageNum = (page != null && page >= 0) ? page : 0;
    int sizeNum = (size != null && size > 0) ? size : 10;
    Pageable pageable = PageRequest.of(pageNum, sizeNum);

    Page<Refund> refunds = repository.filter(null, status, pageable);
    return PageResponse.of(refunds.map(RefundResponse::from));
  }

  private String buildQrUrl(String bankCode, String accountNumber, String accountHolderName, BigDecimal amount, Long orderId) {
    if (bankCode == null || bankCode.isBlank() || accountNumber == null || accountNumber.isBlank()) {
      return null;
    }

    String encodedName = accountHolderName != null
      ? URLEncoder.encode(accountHolderName, StandardCharsets.UTF_8)
      : "";
    String amountStr = amount != null ? amount.toPlainString() : "0";

    return String.format(
      "https://img.vietqr.io/image/%s-%s-compact2.png?amount=%s&addInfo=HOANTIEN%%20DON%%20%s&accountName=%s",
      bankCode.trim(),
      accountNumber.trim(),
      amountStr,
      orderId,
      encodedName
    );
  }
}
