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
import org.oplearn.project.security.SecurityHelper;
import org.oplearn.project.service.ComplaintService;
import org.oplearn.project.service.NotificationService;
import org.oplearn.project.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplaintFacadeServiceImpl implements ComplaintFacadeService {

  private final ComplaintService complaintService;
  private final SecurityHelper securityHelper;
  private final OrderService orderService;
  private final NotificationService notificationService;

  @Override
  @Transactional
  public ComplaintResponse create(ComplaintRequest request) {
    log.info("(facade) create complaint");

    User currentUser = securityHelper.currentUser();

    if (request.getOrderId() != null) {
      OrderResponse order = orderService.detail(request.getOrderId());
      if (!order.getUserId().equals(currentUser.getId())) {
        log.warn("(create) user not authorized");
        throw new UserUnauthorizedException();
      }
    }

    request.setUserId(currentUser.getId());

    ComplaintResponse response = complaintService.create(request);

    notificationService.createAndSendComplaintReply(response);

    return response;
  }

  @Override
  public ComplaintResponse detail(Long id) {
    log.info("(facade) detail complaint: {}", id);

    ComplaintResponse response = complaintService.detail(id);

    User currentUser = securityHelper.currentUser();

    if (!securityHelper.isAdmin() && !response.getUserId().equals(currentUser.getId())) {
      log.warn("(detail) user not authorized");
      throw new UserUnauthorizedException();
    }

    return response;
  }

  @Override
  public PageResponse<ComplaintResponse> filterByUserId(ComplaintStatus status, int size, int page) {
    log.info("(facade) filter complaints by userId");

    User currentUser = securityHelper.currentUser();

    return complaintService.filterByUserId(currentUser.getId(), status, size, page);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(facade) delete complaint: {}", id);

    ComplaintResponse existing = complaintService.detail(id);

    User currentUser = securityHelper.currentUser();

    if (!securityHelper.isAdmin() && !existing.getUserId().equals(currentUser.getId())) {
      log.warn("(delete) user not authorized");
      throw new UserUnauthorizedException();
    }

    complaintService.delete(id);
  }
}
