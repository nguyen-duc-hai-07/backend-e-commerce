package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.NotificationRequest;
import org.oplearn.project.dto.response.NotificationResponse;
import org.oplearn.project.entity.Notification;
import org.oplearn.project.entity.User;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.NotificationFacadeService;
import org.oplearn.project.repository.NotificationRepository;
import org.oplearn.project.security.SecurityHelper;
import org.oplearn.project.service.NotificationService;
import org.oplearn.project.websocket.EchoWebSocketHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationFacadeServiceImpl implements NotificationFacadeService {
  private final NotificationService notificationService;
  private final SecurityHelper securityHelper;
  private final EchoWebSocketHandler echoWebSocketHandler;

  @Override
  @Transactional
  public NotificationResponse create(NotificationRequest request) {
    log.info("(facade) create notification: {}", request);
    User currentUser = securityHelper.currentUser();
    request.setSenderId(currentUser.getId());

    NotificationResponse response = notificationService.create(request);

    Map<String, Object> payload = Map.of("notification", response);

    echoWebSocketHandler.broadcast("RECEIVE_NOTIFICATION", payload);

    return response;
  }

  @Override
  public Page<NotificationRepository.NotificationFeedProjection> getFeed(Pageable pageable) {
    log.info("(facade) getFeed: {}", pageable);
    User currentUser = securityHelper.currentUser();

    return notificationService.getFeed(currentUser.getId(), pageable);
  }

  @Override
  @Transactional
  public NotificationResponse detail(Long id) {
    log.info("(facade) detail: {}", id);
    User currentUser = securityHelper.currentUser();

    Notification notification = notificationService.findById(id);

    if (notification.getRecipientId() != null
        && !notification.getRecipientId().equals(currentUser.getId())) {
      log.warn("(detail) user {} not authorized to view notification {}", currentUser.getId(), id);
      throw new UserUnauthorizedException();
    }

    notificationService.markAsRead(id, currentUser.getId());

    return NotificationResponse.from(notification, true, Instant.now());
  }

  @Override
  @Transactional
  public boolean markAsRead(Long notificationId) {
    log.info("(facade) markAsRead: {}", notificationId);
    User currentUser = securityHelper.currentUser();

    Notification notification = notificationService.findById(notificationId);

    if (notification.getRecipientId() != null
      && !notification.getRecipientId().equals(currentUser.getId())) {
      log.warn("(markAsRead) user {} not authorized to mark notification {}", currentUser.getId(), notificationId);
      throw new UserUnauthorizedException();
    }

    return notificationService.markAsRead(notificationId, currentUser.getId());
  }

  @Override
  @Transactional
  public int markAllAsRead() {
    log.info("(facade) markAllAsRead");
    User currentUser = securityHelper.currentUser();

    return notificationService.markAllAsRead(currentUser.getId());
  }

  @Override
  public long getUnreadCount() {
    log.info("(facade) getUnreadCount");
    User currentUser = securityHelper.currentUser();

    return notificationService.getUnreadCount(currentUser.getId());
  }
}
