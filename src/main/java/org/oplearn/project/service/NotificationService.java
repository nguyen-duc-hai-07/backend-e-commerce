package org.oplearn.project.service;

import org.oplearn.project.dto.request.NotificationRequest;
import org.oplearn.project.dto.response.*;
import org.oplearn.project.entity.Notification;
import org.oplearn.project.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
  NotificationResponse create(NotificationRequest request);

  Notification findById(Long id);

  NotificationResponse getAvailableNotificationAndThrow(Long id);

  long getUnreadCount(Long userId);

  Page<NotificationRepository.NotificationFeedProjection> getFeed(Long userId, Pageable pageable);

  boolean markAsRead(Long notificationId, Long userId);

  int markAllAsRead(Long userId);

  void createAndSendOrderStatus(OrderResponse response);

  void createAndSendReviewReply(ReviewReplyResponse replyResponse, ReviewResponse reviewResponse);

  void createAndSendReturnRequestStatus(ReturnRequestResponse response, OrderResponse order);

  void createAndSendComplaintReply(ComplaintResponse response);

  void createAndSendRefundStatus(RefundResponse response);

  void createAndSendVoucher(VoucherResponse response);

  void createAndSendUserVoucher(UserVoucherResponse response);
}
