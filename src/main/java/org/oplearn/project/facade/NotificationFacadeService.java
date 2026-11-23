package org.oplearn.project.facade;

import org.oplearn.project.dto.request.NotificationRequest;
import org.oplearn.project.dto.response.NotificationResponse;
import org.oplearn.project.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationFacadeService {
  NotificationResponse create(NotificationRequest request);

  Page<NotificationRepository.NotificationFeedProjection> getFeed(Pageable pageable);

  NotificationResponse detail(Long id);

  boolean markAsRead(Long notificationId);

  int markAllAsRead();

  long getUnreadCount();
}
