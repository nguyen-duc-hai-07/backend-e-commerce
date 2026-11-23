package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ComplaintRequest;
import org.oplearn.project.dto.response.ComplaintResponse;
import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.enums.ComplaintStatus;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.ComplaintFacadeService;
import org.oplearn.project.service.ComplaintService;
import org.oplearn.project.service.OrderService;
import org.oplearn.project.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplaintFacadeServiceImpl implements ComplaintFacadeService {

  private final ComplaintService complaintService;
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
  @Transactional
  public ComplaintResponse create(ComplaintRequest request) {
    log.info("(facade) create complaint");

    User currentUser = currentUser();

    if (request.getOrderId() != null) {
      OrderResponse order = orderService.detail(request.getOrderId());
      if (!order.getUserId().equals(currentUser.getId())) {
        log.warn("(create) user not authorized");
        throw new UserUnauthorizedException();
      }
    }

    request.setUserId(currentUser.getId());

    return complaintService.create(request);
  }

  @Override
  public ComplaintResponse detail(Long id) {
    log.info("(facade) detail complaint: {}", id);

    ComplaintResponse response = complaintService.detail(id);

    User currentUser = currentUser();

    if (!isAdmin() && !response.getUserId().equals(currentUser.getId())) {
      log.warn("(detail) user not authorized");
      throw new UserUnauthorizedException();
    }

    return response;
  }

  @Override
  public PageResponse<ComplaintResponse> filterByUserId(ComplaintStatus status, int size, int page) {
    log.info("(facade) filter complaints by userId");

    User currentUser = currentUser();

    return complaintService.filterByUserId(currentUser.getId(), status, size, page);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(facade) delete complaint: {}", id);

    ComplaintResponse existing = complaintService.detail(id);

    User currentUser = currentUser();

    if (!isAdmin() && !existing.getUserId().equals(currentUser.getId())) {
      log.warn("(delete) user not authorized");
      throw new UserUnauthorizedException();
    }

    complaintService.delete(id);
  }
}
