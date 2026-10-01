package org.oplearn.project.service;

import org.oplearn.project.dto.request.ReviewReplyRequest;
import org.oplearn.project.dto.response.ReviewReplyResponse;

public interface ReviewReplyService {
  ReviewReplyResponse create(ReviewReplyRequest request);

  ReviewReplyResponse update(String content, Long id);

  ReviewReplyResponse detail(Long id);

  void delete(Long id);

  ReviewReplyResponse findByReviewId(Long reviewId);
}
