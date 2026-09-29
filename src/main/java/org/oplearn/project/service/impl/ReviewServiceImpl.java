package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.constants.OpLearnConstants;
import org.oplearn.project.dto.request.ReviewFilterRequest;
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
  public Review create(Review review) {
    log.info("(service) create review");

    if (repository.existsByProductIdAndUserIdAndIsDeletedFalse(review.getProductId(), review.getUserId())) {
      throw new ReviewAlreadyExistedException();
    }

    return repository.save(review);
  }

  @Override
  @Transactional
  public Review update(Review review, Long id) {
    log.info("(service) update review");

    Review existingReview = findByIdOrThrow(id);

    if (review.getRating() != null) {
      existingReview.setRating(review.getRating());
    }
    if (review.getComment() != null) {
      existingReview.setComment(review.getComment());
    }

    return repository.save(existingReview);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(service) delete review");

    findByIdOrThrow(id);

    repository.softDeleteById(id);
  }

  @Override
  public Review findByIdOrThrow(Long id) {
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
