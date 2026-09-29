package org.oplearn.project.service;

import org.oplearn.project.dto.request.ReviewFilterRequest;
import org.oplearn.project.dto.request.ReviewRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ReviewResponse;

import java.math.BigDecimal;

public interface ReviewService {
  ReviewResponse create(ReviewRequest request);

  ReviewResponse update(ReviewRequest request, Long id);

  ReviewResponse detail(Long id);

  void delete(Long id);

  PageResponse<ReviewResponse> findByRatingAndProductId(ReviewFilterRequest request);

  BigDecimal findAverageRatingByProductId(Long productId);

  int countByProductId(Long productId);
}
