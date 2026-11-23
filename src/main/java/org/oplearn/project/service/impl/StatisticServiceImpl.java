package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.OrderOverviewStatisticResponse;
import org.oplearn.project.dto.response.OrderTimelineStatisticResponse;
import org.oplearn.project.repository.OrderRepository;
import org.oplearn.project.repository.OrderRepository.OrderSummaryProjection;
import org.oplearn.project.repository.OrderRepository.OrderTimelineProjection;
import org.oplearn.project.service.StatisticService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

  private final OrderRepository repository;

  private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
  private static final DateTimeFormatter MONTH_LABEL_FORMATTER = DateTimeFormatter.ofPattern("'Tháng' MM/yyyy");
  private static final DateTimeFormatter YEAR_LABEL_FORMATTER = DateTimeFormatter.ofPattern("'Năm' yyyy");

  @Override
  public OrderOverviewStatisticResponse getOverviewStatistics(LocalDate fromDate, LocalDate toDate) {
    LocalDate now = LocalDate.now(VN_ZONE);
    LocalDate start = (fromDate != null) ? fromDate : now.withDayOfMonth(1);
    LocalDate end = (toDate != null) ? toDate : now;

    Instant startInstant = start.atStartOfDay(VN_ZONE).toInstant();
    Instant endInstant = end.plusDays(1).atStartOfDay(VN_ZONE).toInstant();

    log.info("(getOverviewStatistics) startInstant: {}, endInstant: {}", startInstant, endInstant);
    OrderSummaryProjection summary = repository.getOrderSummaryStatistics(
      startInstant,
      endInstant
    );

    if (summary == null) {
      return OrderOverviewStatisticResponse.builder()
        .fromDate(startInstant)
        .toDate(endInstant)
        .totalOrders(0L)
        .completedOrders(0L)
        .cancelledOrders(0L)
        .refundedOrders(0L)
        .inProgressOrders(0L)
        .totalRevenue(BigDecimal.ZERO)
        .build();
    }

    return OrderOverviewStatisticResponse.builder()
      .fromDate(startInstant)
      .toDate(endInstant)
      .totalOrders(summary.getTotalOrders())
      .completedOrders(summary.getCompletedOrders())
      .cancelledOrders(summary.getCancelledOrders())
      .refundedOrders(summary.getRefundedOrders())
      .inProgressOrders(summary.getInProgressOrders())
      .totalRevenue(summary.getTotalRevenue())
      .build();
  }

  @Override
  public List<OrderTimelineStatisticResponse> getMonthlyRevenueStatistics(Integer year) {
    int targetYear = (year != null) ? year : LocalDate.now(VN_ZONE).getYear();
    log.info("(getMonthlyRevenueStatistics) year: {}", targetYear);

    Instant fromDate = LocalDate.of(targetYear, 1, 1).atStartOfDay(VN_ZONE).toInstant();
    Instant toDate = LocalDate.of(targetYear, 12, 1).atStartOfDay(VN_ZONE).toInstant();

    List<OrderTimelineProjection> rawListMonth = repository.getMonthlyRevenueStatistics(fromDate, toDate);

    return rawListMonth.stream()
      .map(item -> OrderTimelineStatisticResponse.builder()
        .period(item.getPeriod())
        .label(item.getPeriod() != null ? item.getPeriod().format(MONTH_LABEL_FORMATTER) : "")
        .revenue(item.getRevenue() != null ? item.getRevenue() : BigDecimal.ZERO)
        .orderCount(item.getOrderCount() != null ? item.getOrderCount() : 0L)
        .build())
      .toList();
  }

  @Override
  public List<OrderTimelineStatisticResponse> getYearlyRevenueStatistics(Integer startYear, Integer endYear) {
    int currentYear = LocalDate.now(VN_ZONE).getYear();
    int end = (endYear != null) ? endYear : currentYear;
    int start = (startYear != null) ? startYear : end - 4;
    log.info("(getYearlyRevenueStatistics) startYear: {}, endYear: {}", start, end);

    Instant fromDate = LocalDate.of(start, 1, 1).atStartOfDay(VN_ZONE).toInstant();
    Instant toDate = LocalDate.of(end, 1, 1).atStartOfDay(VN_ZONE).toInstant();

    List<OrderTimelineProjection> rawListYear = repository.getYearlyRevenueStatistics(fromDate, toDate);

    return rawListYear.stream()
      .map(item -> OrderTimelineStatisticResponse.builder()
        .period(item.getPeriod())
        .label(item.getPeriod() != null ? item.getPeriod().format(YEAR_LABEL_FORMATTER) : "")
        .revenue(item.getRevenue() != null ? item.getRevenue() : BigDecimal.ZERO)
        .orderCount(item.getOrderCount() != null ? item.getOrderCount() : 0L)
        .build())
      .toList();
  }
}
