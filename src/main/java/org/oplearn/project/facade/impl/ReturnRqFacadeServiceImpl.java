package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ReturnRqRequest;
import org.oplearn.project.dto.request.ReturnRqUpdateRequest;
import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ReturnRequestResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.enums.OrderStatus;
import org.oplearn.project.enums.ReturnRequestStatus;
import org.oplearn.project.exception.InvalidOrderStatusException;
import org.oplearn.project.exception.ReturnPeriodExpiredException;
import org.oplearn.project.exception.ReturnRequestAlreadyApprovedException;
import org.oplearn.project.exception.ReturnRequestAlreadyExistedException;
import org.oplearn.project.exception.ReturnRequestLimitExceededException;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.ReturnRqFacadeService;
import org.oplearn.project.service.OrderService;
import org.oplearn.project.service.ReturnRqService;
import org.oplearn.project.service.UserService;
import org.oplearn.project.service.UserVoucherService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReturnRqFacadeServiceImpl implements ReturnRqFacadeService {
  private static final int MAX_RETURN_REQUESTS = 3;
  private static final int RETURN_WINDOW_DAYS = 7;

  private final ReturnRqService returnRqService;
  private final UserService userService;
  private final OrderService orderService;
  private final UserVoucherService userVoucherService;

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
  @Transactional
  public ReturnRequestResponse create(ReturnRqRequest request) {
    log.info("(facade) create return request: {}", request);

    User currentUser = currentUser();
    OrderResponse order = orderService.detail(request.getOrderId());

    this.validateCreate(currentUser, order);

    ReturnRequestResponse response = returnRqService.create(request);
    orderService.updateStatus(order.getId(), OrderStatus.RETURN_REQUESTED);

    return response;
  }

  @Override
  public PageResponse<ReturnRequestResponse> filterByUserId(int size, int page) {
    log.info("(facade) list return request: userId: {}, size: {}, page: {}", size, page);

    User currentUser = currentUser();

    return returnRqService.filterByUserId(currentUser.getId(), size, page);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(facade) delete return request: id: {}", id);

    User currentUser = currentUser();
    ReturnRequestResponse existing = returnRqService.detail(id);
    OrderResponse order = orderService.detail(existing.getOrderId());

    if (!order.getUserId().equals(currentUser.getId())) {
      log.warn("(delete) user not authorized");
      throw new UserUnauthorizedException();
    }

    returnRqService.delete(id);

    orderService.updateStatus(order.getId(), OrderStatus.COMPLETED);
  }

  private void validateCreate(User currentUser, OrderResponse order) {
    if (!order.getUserId().equals(currentUser.getId())) {
      log.warn("(validateCreate) user not authorized");
      throw new UserUnauthorizedException();
    }

    if (returnRqService.hasApprovedRequest(order.getId())) {
      log.warn("(validateCreate) return request already approved for order {}", order.getId());
      throw new ReturnRequestAlreadyApprovedException();
    }

    if (returnRqService.countByOrderId(order.getId()) >= MAX_RETURN_REQUESTS) {
      log.warn("(validateCreate) return request limit exceeded for order {}", order.getId());
      throw new ReturnRequestLimitExceededException();
    }

    if (returnRqService.hasPendingRequest(order.getId())) {
      log.warn("(validateCreate) return request already pending for order {}", order.getId());
      throw new ReturnRequestAlreadyExistedException();
    }

    if (order.getStatus() != OrderStatus.DELIVERED && order.getStatus() != OrderStatus.COMPLETED) {
      log.warn("(validateCreate) order is in status {}, cannot return/exchange", order.getStatus());
      throw new InvalidOrderStatusException();
    }

    Instant returnDeadline = Instant.now().minus(RETURN_WINDOW_DAYS, ChronoUnit.DAYS);
    if (order.getUpdatedAt() != null && order.getUpdatedAt().isBefore(returnDeadline)) {
      log.warn("(validateCreate) return period expired for order {}", order.getId());
      throw new ReturnPeriodExpiredException();
    }
  }

  @Override
  @Transactional
  public void updateAdmin(ReturnRqUpdateRequest request, Long id) {
    log.info("(facade) update return request: id: {}", id);

    returnRqService.adminUpdate(request, id);

    ReturnRequestResponse updated = returnRqService.detail(id);
    OrderResponse order = orderService.detail(updated.getOrderId());

    if (updated.getStatus() == ReturnRequestStatus.APPROVED) {

      userVoucherService.refundVoucher(order.getId());

      orderService.updateStatus(order.getId(), OrderStatus.REFUNDED);
    } else if (updated.getStatus() == ReturnRequestStatus.REJECTED) {

      orderService.updateStatus(order.getId(), OrderStatus.COMPLETED);
    }
  }
}
