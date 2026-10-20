package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.RefundFilterRequest;
import org.oplearn.project.dto.request.RefundRequest;
import org.oplearn.project.dto.request.RefundUpdateRequest;
import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.RefundResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.enums.OrderStatus;
import org.oplearn.project.enums.RefundStatus;
import org.oplearn.project.exception.InvalidOrderStatusException;
import org.oplearn.project.exception.RefundNotAllowedForCodException;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.RefundFacadeService;
import org.oplearn.project.service.OrderService;
import org.oplearn.project.service.RefundService;
import org.oplearn.project.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefundFacadeServiceImpl implements RefundFacadeService {
  private final UserService userService;
  private final OrderService orderService;
  private final RefundService refundService;

  private User currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUsernameOrThrow(authentication.getName());
  }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && authentication.getAuthorities().stream()
      .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
  }

  @Override
  @Transactional
  public RefundResponse create(RefundRequest request) {
    log.info("(facade) create refund request: {}", request);
    User currentUser = currentUser();

    OrderResponse order = orderService.detail(request.getOrderId());

    if (!currentUser.getId().equals(order.getUserId())) {
      log.warn("(facade) User {} tried to request refund for Order {} belonging to User {}",
        currentUser.getId(), order.getId(), order.getUserId());
      throw new UserUnauthorizedException();
    }

    if (order.getStatus() != OrderStatus.CANCELLED && order.getStatus() != OrderStatus.REFUNDED) {
      log.warn("(facade) Order {} is in status {}, expected CANCELLED", order.getId(), order.getStatus());
      throw new InvalidOrderStatusException();
    }

    if (order.getPaymentMethod() != null && "COD".equalsIgnoreCase(order.getPaymentMethod().getCode())) {
      log.warn("(facade) COD payment method is not supported for refund on Order {}", order.getId());
      throw new RefundNotAllowedForCodException();
    }

    request.setUserId(currentUser.getId());
    request.setOrderId(order.getId());
    request.setAmount(order.getTotalAmount());

    return refundService.create(request);
  }

  @Override
  public RefundResponse detail(Long id) {
    log.info("(facade) detail refund id: {}", id);
    User currentUser = currentUser();
    RefundResponse refund = refundService.detail(id);

    boolean isOwner = currentUser.getId().equals(refund.getUserId());
    if (!isAdmin() && !isOwner) {
      log.warn("(detail) user {} not authorized to view refund {}", currentUser.getId(), id);
      throw new UserUnauthorizedException();
    }

    return refund;
  }

  @Override
  @Transactional
  public RefundResponse update(Long id, RefundUpdateRequest request) {
    log.info("(facade) update refund id: {}, request: {}", id, request);
    return refundService.update(id, request);
  }

  @Override
  public PageResponse<RefundResponse> filter(RefundFilterRequest request) {
    log.info("(facade) user filter refund request: {}", request);
    User currentUser = currentUser();
    request.setUserId(currentUser.getId());
    return refundService.filter(request);
  }

  @Override
  public PageResponse<RefundResponse> filterForAdmin(RefundStatus status, Integer page, Integer size) {
    log.info("(facade) filterForAdmin status: {}, page: {}, size: {}", status, page, size);
    if (!isAdmin()) {
      log.warn("(filterForAdmin) user not authorized");
      throw new UserUnauthorizedException();
    }
    return refundService.filterForAdmin(status, page, size);
  }
}
