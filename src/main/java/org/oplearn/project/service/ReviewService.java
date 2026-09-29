package org.oplearn.project.service;

import org.oplearn.project.dto.request.ReviewFilterRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ReviewResponse;
import org.oplearn.project.entity.Review;

import java.math.BigDecimal;

public interface ReviewService {
  Review create (Review review);

  Review update (Review review, Long id);

  void delete (Long id);

  Review findByIdOrThrow(Long id);

  PageResponse<ReviewResponse> findByRatingAndProductId(ReviewFilterRequest request);

  BigDecimal findAverageRatingByProductId(Long productId);

  int countByProductId(Long productId);
}
