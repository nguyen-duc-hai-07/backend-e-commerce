package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  public ReviewReply create(ReviewReply reviewReply) {
    log.info("(create) reviewReply");

    if(repository.existsByReviewIdAndIsDeletedFalse(reviewReply.getReviewId())) {
      throw new ReviewReplyAlreadyExistedException();
    }

    return repository.save(reviewReply);
  }

  @Override
  @Transactional
  public ReviewReply update(String content, Long id) {
    log.info("(update) reviewReply");

    ReviewReply existing = findByIdOrThrow(id);

    if(content != null) {
      existing.setContent(content);
    }

    return repository.save(existing);
  }

  @Override
  public ReviewReply findByIdOrThrow(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(ReviewReplyNotFoundException::new);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(delete) reviewReply");

    findByIdOrThrow(id);

    repository.softDeleteById(id);
  }

  @Override
  public ReviewReply findByReviewId(Long reviewId) {
    return repository.findByReviewIdAndIsDeletedFalse(reviewId);
  }
}
