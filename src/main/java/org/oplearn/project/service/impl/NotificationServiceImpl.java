package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.NotificationRequest;
import org.oplearn.project.dto.response.*;
import org.oplearn.project.entity.Notification;
import org.oplearn.project.entity.NotificationRead;
import org.oplearn.project.enums.NotificationReferenceType;
import org.oplearn.project.enums.NotificationType;
import org.oplearn.project.exception.NotificationNotFoundException;
import org.oplearn.project.repository.NotificationReadRepository;
import org.oplearn.project.repository.NotificationRepository;
import org.oplearn.project.service.NotificationService;
import org.oplearn.project.websocket.EchoWebSocketHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
  private final NotificationRepository notificationRepository;
  private final NotificationReadRepository readRepository;
  private final EchoWebSocketHandler echoWebSocketHandler;

  @Override
  @Transactional
  public NotificationResponse create(NotificationRequest request) {
    log.info("(service) create notification: {}", request);

    Notification notification = Notification.builder()
      .recipientId(null) // broadcast toàn sàn
      .senderId(request.getSenderId())
      .type(request.getType())
      .title(request.getTitle())
      .content(request.getContent())
      .build();

    Notification saved = notificationRepository.save(notification);
    return NotificationResponse.from(saved);
  }

  @Override
  public Notification findById(Long id) {
    log.info("(service) findById: {}", id);
    return notificationRepository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(NotificationNotFoundException::new);
  }

  @Override
  public NotificationResponse getAvailableNotificationAndThrow(Long id) {
    log.info("(service) getAvailableNotification: {}", id);
    return NotificationResponse.from(this.findById(id));
  }

  @Override
  public long getUnreadCount(Long userId) {
    log.info("(service) getUnreadCount userId: {}", userId);
    return notificationRepository.countUnreadNotifications(userId);
  }

  @Override
  public Page<NotificationRepository.NotificationFeedProjection> getFeed(Long userId, Pageable pageable) {
    log.info("(service) getFeed userId: {}, pageable: {}", userId, pageable);
    return notificationRepository.findFeedByUserId(userId, pageable);
  }

  @Override
  @Transactional
  public boolean markAsRead(Long notificationId, Long userId) {
    log.info("(service) markAsRead notificationId: {}, userId: {}", notificationId, userId);

    boolean alreadyRead = readRepository.existsByUserIdAndNotificationIdAndIsDeletedFalse(userId, notificationId);
    if (!alreadyRead) {
      if (!notificationRepository.existsByIdAndIsDeletedFalse(notificationId)) {
        throw new NotificationNotFoundException();
      }
      NotificationRead record = NotificationRead.builder()
        .notificationId(notificationId)
        .userId(userId)
        .readAt(Instant.now())
        .build();
      readRepository.save(record);
      return true;
    }
    return false;
  }

  @Override
  @Transactional
  public int markAllAsRead(Long userId) {
    log.info("(service) markAllAsRead userId: {}", userId);
    return readRepository.markAllAsRead(userId);
  }

  @Override
  @Transactional
  public void createAndSendOrderStatus(OrderResponse response) {
    if (response == null || response.getUserId() == null) {
      log.warn("(createAndSendOrderStatus) response or userId is null");
      return;
    }

    Long recipientId = response.getUserId();

    String statusText = switch (response.getStatus()) {
      case CONFIRMED -> "đã được xác nhận";
      case PAID -> "đã thanh toán thành công";
      case SHIPPING -> "đang được giao đến bạn";
      case DELIVERED -> "đã giao hàng thành công";
      case COMPLETED -> "đã hoàn tất";
      case CANCELLED -> "đã bị hủy";
      case RETURN_REQUESTED -> "đang có yêu cầu trả hàng";
      case REFUNDED -> "đã được hoàn tiền";
      default -> null;
    };

    if (statusText == null) {
      return;
    }

    String orderCode = response.getOrderCode() != null ? response.getOrderCode() : "";
    String content = "Đơn hàng " + orderCode + " " + statusText + ".";

    Notification notification = Notification.builder()
      .recipientId(recipientId)
      .senderId(null)
      .type(NotificationType.ORDER_STATUS_CHANGED)
      .title("Cập nhật đơn hàng")
      .content(content)
      .referenceType(NotificationReferenceType.ORDER)
      .referenceId(response.getId())
      .build();

    Notification saved = notificationRepository.save(notification);

    NotificationResponse notificationResponse = NotificationResponse.from(saved);
    Map<String, Object> payload = Map.of("notification", notificationResponse);

    echoWebSocketHandler.sendToUser(recipientId, "RECEIVE_NOTIFICATION", payload);
  }

  @Override
  @Transactional
  public void createAndSendReviewReply(ReviewReplyResponse replyResponse, ReviewResponse reviewResponse) {
    if (replyResponse == null || reviewResponse == null || reviewResponse.getUserId() == null) {
      log.warn("(createAndSendReviewReply) replyResponse or reviewResponse is invalid");
      return;
    }

    Long recipientId = reviewResponse.getUserId();
    String content = "Người bán đã phản hồi đánh giá của bạn: \"" + replyResponse.getContent() + "\"";

    Notification notification = Notification.builder()
        .recipientId(recipientId)
        .senderId(replyResponse.getUserId())
        .type(NotificationType.REVIEW_REPLY)
        .title("Phản hồi đánh giá")
        .content(content)
        .referenceType(NotificationReferenceType.REVIEW)
        .referenceId(reviewResponse.getId())
        .build();

    Notification saved = notificationRepository.save(notification);

    NotificationResponse notificationResponse = NotificationResponse.from(saved);
    Map<String, Object> payload = Map.of("notification", notificationResponse);

    echoWebSocketHandler.sendToUser(recipientId, "RECEIVE_NOTIFICATION", payload);
  }

  @Override
  @Transactional
  public void createAndSendReturnRequestStatus(ReturnRequestResponse response, OrderResponse order) {
    if (response == null || order == null || order.getUserId() == null) {
      log.warn("(createAndSendReturnRequestStatus) response or order is invalid");
      return;
    }

    Long recipientId = order.getUserId();

    String statusText = switch (response.getStatus()) {
      case APPROVED -> "đã được người bán chấp thuận.";
      case REJECTED -> "đã bị từ chối." + (response.getRejectReason() != null && !response.getRejectReason().isBlank()
          ? " Lý do: " + response.getRejectReason() : "");
      case PENDING -> "đang chờ được xử lý.";
    };

    String orderCode = order.getOrderCode() != null ? order.getOrderCode() : "";
    String content = "Yêu cầu đổi/trả hàng cho đơn hàng " + orderCode + " " + statusText;

    Notification notification = Notification.builder()
        .recipientId(recipientId)
        .senderId(null)
        .type(NotificationType.RETURN_REQUEST_STATUS)
        .title("Yêu cầu trả hàng / hoàn tiền")
        .content(content)
        .referenceType(NotificationReferenceType.RETURN_REQUEST)
        .referenceId(response.getId())
        .build();

    Notification saved = notificationRepository.save(notification);

    NotificationResponse notificationResponse = NotificationResponse.from(saved);
    Map<String, Object> payload = Map.of("notification", notificationResponse);

    echoWebSocketHandler.sendToUser(recipientId, "RECEIVE_NOTIFICATION", payload);
  }

  @Override
  @Transactional
  public void createAndSendComplaintReply(ComplaintResponse response) {
    if (response == null || response.getUserId() == null) {
      log.warn("(createAndSendComplaintReply) response or userId is invalid");
      return;
    }

    Long recipientId = response.getUserId();
    String replyText = (response.getAdminReply() != null && !response.getAdminReply().isBlank())
        ? ": \"" + response.getAdminReply() + "\""
        : " đã được cập nhật.";
    String content = "Khiếu nại về đơn hàng #" + response.getOrderId() + replyText;

    Notification notification = Notification.builder()
        .recipientId(recipientId)
        .senderId(null)
        .type(NotificationType.COMPLAINT_REPLY)
        .title("Phản hồi khiếu nại")
        .content(content)
        .referenceType(NotificationReferenceType.COMPLAINT)
        .referenceId(response.getId())
        .build();

    Notification saved = notificationRepository.save(notification);

    NotificationResponse notificationResponse = NotificationResponse.from(saved);
    Map<String, Object> payload = Map.of("notification", notificationResponse);

    echoWebSocketHandler.sendToUser(recipientId, "RECEIVE_NOTIFICATION", payload);
  }

  @Override
  @Transactional
  public void createAndSendRefundStatus(RefundResponse response) {
    if (response == null || response.getUserId() == null) {
      log.warn("(createAndSendRefundStatus) response or userId is invalid");
      return;
    }

    Long recipientId = response.getUserId();

    String statusText = switch (response.getStatus()) {
      case SUCCESS -> "đã được xử lý hoàn tiền thành công.";
      case FAILED -> "hoàn tiền thất bại." + (response.getRejectReason() != null && !response.getRejectReason().isBlank()
          ? " Lý do: " + response.getRejectReason() : "");
      case PENDING -> "đang chờ được xử lý.";
    };

    String amountStr = response.getAmount() != null ? response.getAmount().toPlainString() : "0";
    String content = "Yêu cầu hoàn tiền số tiền " + amountStr + "đ cho đơn hàng #" + response.getOrderId() + " " + statusText;

    Notification notification = Notification.builder()
        .recipientId(recipientId)
        .senderId(null)
        .type(NotificationType.REFUND_STATUS)
        .title("Cập nhật hoàn tiền")
        .content(content)
        .referenceType(NotificationReferenceType.REFUND)
        .referenceId(response.getId())
        .build();

    Notification saved = notificationRepository.save(notification);

    NotificationResponse notificationResponse = NotificationResponse.from(saved);
    Map<String, Object> payload = Map.of("notification", notificationResponse);

    echoWebSocketHandler.sendToUser(recipientId, "RECEIVE_NOTIFICATION", payload);
  }

  @Override
  @Transactional
  public void createAndSendVoucher(VoucherResponse response) {
    if (response == null || response.getId() == null) {
      log.warn("(createAndSendVoucher) response is invalid");
      return;
    }

    String voucherCode = response.getCode() != null ? response.getCode() : "";
    String content = "Sàn vừa phát hành voucher mới " + voucherCode + ". Nhanh tay thu thập ưu đãi ngay!";

    Notification notification = Notification.builder()
        .recipientId(null)
        .senderId(null)
        .type(NotificationType.VOUCHER_CREATE)
        .title("Voucher ưu đãi mới")
        .content(content)
        .referenceType(NotificationReferenceType.VOUCHER)
        .referenceId(response.getId())
        .build();

    Notification saved = notificationRepository.save(notification);

    NotificationResponse notificationResponse = NotificationResponse.from(saved);
    Map<String, Object> payload = Map.of("notification", notificationResponse);

    echoWebSocketHandler.broadcast("RECEIVE_NOTIFICATION", payload);
  }

  @Override
  @Transactional
  public void createAndSendUserVoucher(UserVoucherResponse response) {
    if (response == null || response.getUserId() == null) {
      log.warn("(createAndSendUserVoucher) response or userId is invalid");
      return;
    }

    Long recipientId = response.getUserId();
    String voucherCode = (response.getVoucher() != null && response.getVoucher().getCode() != null)
        ? response.getVoucher().getCode()
        : "";
    String content = "Bạn vừa nhận được voucher " + voucherCode + " vào ví ưu đãi. Sử dụng ngay để nhận ưu đãi!";

    Notification notification = Notification.builder()
        .recipientId(recipientId)
        .senderId(null)
        .type(NotificationType.VOUCHER_CREATE)
        .title("Bạn nhận được voucher mới")
        .content(content)
        .referenceType(NotificationReferenceType.VOUCHER)
        .referenceId(response.getVoucherId())
        .build();

    Notification saved = notificationRepository.save(notification);

    NotificationResponse notificationResponse = NotificationResponse.from(saved);
    Map<String, Object> payload = Map.of("notification", notificationResponse);

    echoWebSocketHandler.sendToUser(recipientId, "RECEIVE_NOTIFICATION", payload);
  }
}
