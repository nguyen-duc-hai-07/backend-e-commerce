package org.oplearn.project.service;

import org.oplearn.project.dto.response.OrderOverviewStatisticResponse;
import org.oplearn.project.dto.response.OrderTimelineStatisticResponse;

import java.time.LocalDate;
import java.util.List;

public interface StatisticService {
  OrderOverviewStatisticResponse getOverviewStatistics(LocalDate fromDate, LocalDate toDate);

  List<OrderTimelineStatisticResponse> getMonthlyRevenueStatistics(Integer year);

  List<OrderTimelineStatisticResponse> getYearlyRevenueStatistics(Integer startYear, Integer endYear);
}
