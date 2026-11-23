package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.client.SepayProperties;
import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PaymentQrResponse;
import org.oplearn.project.enums.OrderStatus;
import org.oplearn.project.service.PaymentQrService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentQrServiceImpl implements PaymentQrService {
  private final SepayProperties sepayProperties;
  private static final String SEPAY_QR_BASE_URL = "https://qr.sepay.vn/img";

  @Override
  public PaymentQrResponse generateQrCode(OrderResponse orderResponse) {
    log.info("(service) generateQrCode");
    if (orderResponse == null || orderResponse.getStatus() != OrderStatus.PENDING_PAYMENT) {
      return null;
    }

    BigDecimal amount = orderResponse.getTotalAmount() != null
      ? orderResponse.getTotalAmount()
      : BigDecimal.ZERO;

    String formattedAmount = amount.stripTrailingZeros().toPlainString();

    String transferContent = orderResponse.getOrderCode() != null
      ? orderResponse.getOrderCode().trim()
      : "";

    String encodedContent = URLEncoder.encode(transferContent, StandardCharsets.UTF_8);

    String template = sepayProperties.getQrTemplate();

    String qrUrl = String.format("%s?acc=%s&bank=%s&amount=%s&des=%s&template=%s",
      SEPAY_QR_BASE_URL,
      sepayProperties.getAccountNumber(),
      sepayProperties.getBank(),
      formattedAmount,
      encodedContent,
      template
    );

    Instant createdAt = orderResponse.getCreatedAt() != null
      ? orderResponse.getCreatedAt()
      : Instant.now();
    Instant expiresAt = createdAt.plus(15, ChronoUnit.MINUTES);

    return PaymentQrResponse.builder()
      .qrCodeUrl(qrUrl)
      .bankName(sepayProperties.getBank())
      .accountNumber(sepayProperties.getAccountNumber())
      .accountName(sepayProperties.getAccountName())
      .amount(amount)
      .transferContent(transferContent)
      .expiresAt(expiresAt)
      .build();
  }
}
