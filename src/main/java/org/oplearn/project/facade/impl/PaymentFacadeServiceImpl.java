package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PaymentResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.PaymentFacadeService;
import org.oplearn.project.security.SecurityHelper;
import org.oplearn.project.service.OrderService;
import org.oplearn.project.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentFacadeServiceImpl implements PaymentFacadeService {
  private final PaymentService paymentService;
  private final SecurityHelper securityHelper;
  private final OrderService orderService;

  @Override
  public PaymentResponse detail(Long id) {
    log.info("(facade) detail payment id: {}", id);

    User currentUser = securityHelper.currentUser();

    Long userId = paymentService.findUserIdByPaymentId(id);

    boolean isSelf = currentUser.getId().equals(userId);

    if (!securityHelper.isAdmin() && !isSelf) {
      log.warn("(detail) user not authorized");
      throw new UserUnauthorizedException();
    }

    return paymentService.detail(id);
  }

  @Override
  public List<PaymentResponse> findByOrderId(Long orderId) {
    log.info("(facade) findByOrderId: {}", orderId);

    User currentUser = securityHelper.currentUser();

    OrderResponse order = orderService.detail(orderId);

    boolean isSelf = currentUser.getId().equals(order.getUserId());

    if (!securityHelper.isAdmin() && !isSelf) {
      log.warn("(findByOrderId) user not authorized");
      throw new UserUnauthorizedException();
    }

    return paymentService.findByOrderId(orderId);
  }
}
