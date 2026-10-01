package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.constants.OpLearnConstants;
import org.oplearn.project.dto.request.ReviewFilterRequest;
import org.oplearn.project.dto.request.ReviewRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ReviewResponse;
import org.oplearn.project.entity.Review;
import org.oplearn.project.exception.ReviewAlreadyExistedException;
import org.oplearn.project.exception.ReviewNotFoundException;
import org.oplearn.project.repository.ReviewRepository;
import org.oplearn.project.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {
  private final ReviewRepository repository;

  @Override
  @Transactional
  public ReviewResponse create(ReviewRequest request) {
    log.info("(service) create review");

    if (repository.existsByProductIdAndUserIdAndIsDeletedFalse(request.getProductId(), request.getUserId())) {
      throw new ReviewAlreadyExistedException();
    }

    Review review = Review.builder()
      .productId(request.getProductId())
      .userId(request.getUserId())
      .rating(request.getRating())
      .comment(request.getComment() != null ? request.getComment().trim() : null)
      .build();

    Review saved = repository.save(review);
    return ReviewResponse.from(saved);
  }

  @Override
  @Transactional
  public ReviewResponse update(ReviewRequest request, Long id) {
    log.info("(service) update review");

    Review existingReview = findByIdOrThrow(id);

    setReviewValues(existingReview, request);

    Review updated = repository.save(existingReview);
    return ReviewResponse.from(updated);
  }

  private void setReviewValues(Review target, ReviewRequest source) {
    if (source.getRating() != null) {
      target.setRating(source.getRating());
    }
    if (source.getComment() != null) {
      target.setComment(source.getComment().trim());
    }
  }

  @Override
  public ReviewResponse detail(Long id) {
    log.info("(detail) id: {}", id);
    return ReviewResponse.from(findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(service) delete review");

    findByIdOrThrow(id);

    repository.softDeleteById(id);
  }

  private Review findByIdOrThrow(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(ReviewNotFoundException::new);
  }

  @Override
  public PageResponse<ReviewResponse> findByRatingAndProductId(ReviewFilterRequest request) {
    log.info("(service) find reviews request: {}", request);

    int page = (request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
    int size = (request.getSize() != null && request.getSize() > 0)
        ? Math.min(request.getSize(), OpLearnConstants.VariableConstant.MAX_PAGE_SIZE)
        : Integer.parseInt(OpLearnConstants.VariableConstant.SIZE_DEFAULT);

    Pageable pageable = PageRequest.of(page, size);

    Page<ReviewResponse> response = repository.findByRatingAndProductId(
        request.getRating(),
        request.getProductId(),
        pageable
    );

    return PageResponse.of(
      response.getContent(),
      (int) response.getTotalElements()
    );
  }

  @Override
  public BigDecimal findAverageRatingByProductId(Long productId) {
    return repository.findAverageRatingByProductId(productId);
  }

  @Override
  public int countByProductId(Long productId) {
    return repository.countByProductIdAndIsDeletedFalse(productId);
  }
}
