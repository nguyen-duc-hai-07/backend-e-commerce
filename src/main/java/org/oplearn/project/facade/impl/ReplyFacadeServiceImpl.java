package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ReviewReplyRequest;
import org.oplearn.project.dto.response.ReviewReplyResponse;
import org.oplearn.project.dto.response.ReviewResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.facade.ReplyFacadeService;
import org.oplearn.project.security.SecurityHelper;
import org.oplearn.project.service.NotificationService;
import org.oplearn.project.service.ReviewReplyService;
import org.oplearn.project.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReplyFacadeServiceImpl implements ReplyFacadeService {
  private final ReviewReplyService reviewReplyService;
  private final ReviewService reviewService;
  private final SecurityHelper securityHelper;
  private final NotificationService notificationService;

  @Override
  @Transactional
  public ReviewReplyResponse create(ReviewReplyRequest request) {
    log.info("(facade) create reply");

    User user = securityHelper.currentUser();

    ReviewResponse reviewResponse = reviewService.detail(request.getReviewId());

    request.setUserId(user.getId());

    ReviewReplyResponse saved = reviewReplyService.create(request);

    notificationService.createAndSendReviewReply(saved , reviewResponse);

    return ReviewReplyResponse.of(saved, user);
  }

  @Override
  @Transactional
  public ReviewReplyResponse update(String content, Long id) {
    log.info("(facade) update reply");

    ReviewReplyResponse updated = reviewReplyService.update(content, id);

    return ReviewReplyResponse.of(updated, securityHelper.currentUser());
  }

  @Override
  public ReviewReplyResponse detail(Long id) {
    log.info("(facade) detail reply");

    ReviewReplyResponse reviewReply = reviewReplyService.detail(id);

    return ReviewReplyResponse.of(reviewReply, securityHelper.currentUser());
  }

  @Override
  public ReviewReplyResponse findByReviewId(Long reviewId) {
    log.info("(facade) findByReviewId reply");

    reviewService.detail(reviewId);

    ReviewReplyResponse reviewReply = reviewReplyService.findByReviewId(reviewId);

    return ReviewReplyResponse.of(reviewReply, securityHelper.currentUser());
  }
}
