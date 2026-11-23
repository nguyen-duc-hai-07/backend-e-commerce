package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PaymentResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.PaymentFacadeService;
import org.oplearn.project.service.OrderService;
import org.oplearn.project.service.PaymentService;
import org.oplearn.project.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentFacadeServiceImpl implements PaymentFacadeService {
  private final PaymentService paymentService;
  private final UserService userService;
  private final OrderService orderService;

  private User currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUsernameOrThrow(authentication.getName());
  }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getAuthorities().stream()
      .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
  }

  @Override
  public PaymentResponse detail(Long id) {
    log.info("(facade) detail payment id: {}", id);

    User currentUser = currentUser();

    Long userId = paymentService.findUserIdByPaymentId(id);

    boolean isSelf = currentUser.getId().equals(userId);

    if (!isAdmin() && !isSelf) {
      log.warn("(detail) user not authorized");
      throw new UserUnauthorizedException();
    }

    return paymentService.detail(id);
  }

  @Override
  public List<PaymentResponse> findByOrderId(Long orderId) {
    log.info("(facade) findByOrderId: {}", orderId);

    User currentUser = currentUser();

    OrderResponse order = orderService.detail(orderId);

    boolean isSelf = currentUser.getId().equals(order.getUserId());

    if (!isAdmin() && !isSelf) {
      log.warn("(findByOrderId) user not authorized");
      throw new UserUnauthorizedException();
    }

    return paymentService.findByOrderId(orderId);
  }
}
