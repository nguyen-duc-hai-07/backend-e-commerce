package org.oplearn.project.facade;

import org.oplearn.project.dto.request.ReviewRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ReviewResponse;

import java.math.BigDecimal;

public interface ReviewFacadeService {
  ReviewResponse create(ReviewRequest request);

  ReviewResponse update(ReviewRequest request, Long id);

  void delete(Long id);

  PageResponse<ReviewResponse> findByRatingAndProductId(Integer rating , Long productId, int page, int size);

  ReviewResponse detail(Long id);
}
