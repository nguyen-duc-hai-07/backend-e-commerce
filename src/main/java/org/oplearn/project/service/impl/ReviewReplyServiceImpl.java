package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ReviewReplyRequest;
import org.oplearn.project.dto.response.ReviewReplyResponse;
import org.oplearn.project.entity.ReviewReply;
import org.oplearn.project.exception.ReviewReplyAlreadyExistedException;
import org.oplearn.project.exception.ReviewReplyNotFoundException;
import org.oplearn.project.repository.ReviewReplyRepository;
import org.oplearn.project.service.ReviewReplyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewReplyServiceImpl implements ReviewReplyService {
  private final ReviewReplyRepository repository;

  @Override
  @Transactional
  public ReviewReplyResponse create(ReviewReplyRequest request) {
    log.info("(create) reviewReply");

    if (repository.existsByReviewIdAndIsDeletedFalse(request.getReviewId())) {
      throw new ReviewReplyAlreadyExistedException();
    }

    ReviewReply reviewReply = ReviewReply.builder()
      .reviewId(request.getReviewId())
      .userId(request.getUserId())
      .content(request.getContent() != null ? request.getContent().trim() : null)
      .build();

    ReviewReply saved = repository.save(reviewReply);
    return ReviewReplyResponse.from(saved);
  }

  @Override
  @Transactional
  public ReviewReplyResponse update(String content, Long id) {
    log.info("(update) reviewReply");

    ReviewReply existing = findByIdOrThrow(id);

    if (content != null) {
      existing.setContent(content.trim());
    }

    ReviewReply updated = repository.save(existing);
    return ReviewReplyResponse.from(updated);
  }

  @Override
  public ReviewReplyResponse detail(Long id) {
    log.info("(detail) id: {}", id);
    return ReviewReplyResponse.from(findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(delete) reviewReply");

    findByIdOrThrow(id);

    repository.softDeleteById(id);
  }

  @Override
  public ReviewReplyResponse findByReviewId(Long reviewId) {
    return ReviewReplyResponse.from(repository.findByReviewIdAndIsDeletedFalse(reviewId));
  }

  private ReviewReply findByIdOrThrow(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(ReviewReplyNotFoundException::new);
  }
}
