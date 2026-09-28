package org.oplearn.project.service;

import org.oplearn.project.entity.ReviewReply;

public interface ReviewReplyService {
  ReviewReply create(ReviewReply reviewReply);

  ReviewReply update(String content, Long id);

  ReviewReply findByIdOrThrow(Long id);

  void delete(Long id);

  ReviewReply findByReviewId(Long reviewId);
}
