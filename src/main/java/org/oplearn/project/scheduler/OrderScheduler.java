package org.oplearn.project.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.facade.OrderFacadeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderScheduler {
  private final OrderFacadeService facade;

  @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Ho_Chi_Minh")
  public void autoCompleteOrdersJob() {
    facade.autoCompleteDeliveredOrders();
  }

  @Scheduled(cron = "0 */1 * * * ?")
  public void autoCancelExpiredOrdersJob() {
    facade.autoCancelExpiredPaymentOrders();
  }
}
