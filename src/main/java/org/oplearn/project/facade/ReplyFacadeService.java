package org.oplearn.project.facade;

import org.oplearn.project.dto.request.ReviewReplyRequest;
import org.oplearn.project.dto.response.ReviewReplyResponse;

public interface ReplyFacadeService {
  ReviewReplyResponse create(ReviewReplyRequest request);

  ReviewReplyResponse update(String content, Long id);

  ReviewReplyResponse detail(Long id);

  ReviewReplyResponse findByReviewId(Long reviewId);
}
