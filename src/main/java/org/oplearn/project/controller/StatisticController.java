package org.oplearn.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.OrderOverviewStatisticResponse;
import org.oplearn.project.dto.response.OrderTimelineStatisticResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.service.StatisticService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/statistics")
public class StatisticController {

  private final StatisticService statisticService;

  @GetMapping("/overview")
  public ResponseGeneral<OrderOverviewStatisticResponse> getOverview(
      @RequestParam(value = "fromDate", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam(value = "toDate", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
  ) {
    log.info("(getOverview) fromDate: {}, toDate: {}", fromDate, toDate);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, statisticService.getOverviewStatistics(fromDate, toDate));
  }

  @GetMapping("/chart/monthly")
  public ResponseGeneral<List<OrderTimelineStatisticResponse>> getMonthlyChart(
      @RequestParam(value = "year", required = false) Integer year
  ) {
    log.info("(getMonthlyChart) year: {}", year);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, statisticService.getMonthlyRevenueStatistics(year));
  }

  @GetMapping("/chart/yearly")
  public ResponseGeneral<List<OrderTimelineStatisticResponse>> getYearlyChart(
      @RequestParam(value = "startYear", required = false) Integer startYear,
      @RequestParam(value = "endYear", required = false) Integer endYear
  ) {
    log.info("(getYearlyChart) startYear: {}, endYear: {}", startYear, endYear);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, statisticService.getYearlyRevenueStatistics(startYear, endYear));
  }
}
