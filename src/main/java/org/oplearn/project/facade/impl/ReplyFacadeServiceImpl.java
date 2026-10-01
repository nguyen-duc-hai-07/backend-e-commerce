package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ReviewReplyRequest;
import org.oplearn.project.dto.response.ReviewReplyResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.facade.ReplyFacadeService;
import org.oplearn.project.service.ReviewReplyService;
import org.oplearn.project.service.ReviewService;
import org.oplearn.project.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReplyFacadeServiceImpl implements ReplyFacadeService {
  private final ReviewReplyService reviewReplyService;
  private final ReviewService reviewService;
  private final UserService userService;

  private User currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUsernameOrThrow(authentication.getName());
  }

  @Override
  @Transactional
  public ReviewReplyResponse create(ReviewReplyRequest request) {
    log.info("(facade) create reply");

    User user = currentUser();

    reviewService.detail(request.getReviewId());

    request.setUserId(user.getId());

    ReviewReplyResponse saved = reviewReplyService.create(request);

    return ReviewReplyResponse.of(saved, user);
  }

  @Override
  @Transactional
  public ReviewReplyResponse update(String content, Long id) {
    log.info("(facade) update reply");

    ReviewReplyResponse updated = reviewReplyService.update(content, id);

    return ReviewReplyResponse.of(updated, currentUser());
  }

  @Override
  public ReviewReplyResponse detail(Long id) {
    log.info("(facade) detail reply");

    ReviewReplyResponse reviewReply = reviewReplyService.detail(id);

    return ReviewReplyResponse.of(reviewReply, currentUser());
  }

  @Override
  public ReviewReplyResponse findByReviewId(Long reviewId) {
    log.info("(facade) findByReviewId reply");

    reviewService.detail(reviewId);

    ReviewReplyResponse reviewReply = reviewReplyService.findByReviewId(reviewId);

    return ReviewReplyResponse.of(reviewReply, currentUser());
  }
}
