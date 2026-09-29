package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ReviewFilterRequest;
import org.oplearn.project.dto.request.ReviewRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ReviewResponse;
import org.oplearn.project.entity.Product;
import org.oplearn.project.entity.Review;
import org.oplearn.project.entity.User;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.ReviewFacadeService;
import org.oplearn.project.service.ProductService;
import org.oplearn.project.service.ReviewService;
import org.oplearn.project.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewFacadeServiceImpl implements ReviewFacadeService {
  private final ReviewService reviewService;
  private final ProductService productService;
  private final UserService userService;

  private User currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUsernameOrThrow(authentication.getName());
  }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getAuthorities().stream()
      .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
  }

  @Override
  @Transactional
  public ReviewResponse create(ReviewRequest request) {
    log.info("(facade) create review");

    Product product = productService.findByIdOrThrow(request.getProductId());

    User currentUser = currentUser();

    Review review = Review.builder()
      .rating(request.getRating())
      .comment(request.getComment() != null ? request.getComment().trim() : null)
      .productId(product.getId())
      .userId(currentUser.getId())
      .build();

    Review saved = reviewService.create(review);

    syncProductAverageRating(product.getId());

    syncProductReviewCount(product.getId());

    return ReviewResponse.of(saved , currentUser);
  }

  @Override
  @Transactional
  public ReviewResponse update(ReviewRequest request, Long id) {
    log.info("(facade) update review");

    User currentUser = currentUser();

    Review existing = reviewService.findByIdOrThrow(id);

    boolean isOwner = currentUser.getId().equals(existing.getUserId());

    if(!isOwner) {
      log.warn("(update) user not authorized");
      throw new UserUnauthorizedException();
    }

    Review review = Review.builder()
      .rating(request.getRating())
      .comment(request.getComment() != null ? request.getComment().trim() : null)
      .build();

    Review updated = reviewService.update(review, id);

    if (request.getRating() != null && !request.getRating().equals(existing.getRating())) {
      syncProductAverageRating(existing.getProductId());
    }

    return ReviewResponse.of(updated, currentUser);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(facade) delete review");

    Review existing = reviewService.findByIdOrThrow(id);

    boolean isOwner = currentUser().getId().equals(existing.getUserId());

    boolean isAdmin = isAdmin();

    if(!isAdmin && !isOwner) {
      log.warn("(delete) user not authorized");
      throw new UserUnauthorizedException();
    }

    reviewService.delete(id);

    syncProductAverageRating(existing.getProductId());

    syncProductReviewCount(existing.getProductId());
  }

  @Override
  public PageResponse<ReviewResponse> findByRatingAndProductId(ReviewFilterRequest request) {
    log.info("(facade) find reviews request: {}", request);

    productService.findByIdOrThrow(request.getProductId());

    return reviewService.findByRatingAndProductId(request);
  }

  @Override
  public ReviewResponse detail(Long id) {
    log.info("(facade) detail review id: {}", id);

    Review review = reviewService.findByIdOrThrow(id);

    User user = userService.getAvailableUserAndThrow(review.getUserId());

    return ReviewResponse.of(review, user);
  }

  private void syncProductAverageRating(Long productId) {
    Product product = productService.findByIdOrThrow(productId);

    BigDecimal averageRating = reviewService.findAverageRatingByProductId(productId);

    BigDecimal finalRating = averageRating != null
      ? averageRating.setScale(1, java.math.RoundingMode.HALF_UP)
      : BigDecimal.ZERO;

    productService.updateAverageRating(product.getId(), finalRating);
  }

  private void syncProductReviewCount(Long productId) {
    Product product = productService.findByIdOrThrow(productId);

    int quantity = reviewService.countByProductId(productId);

    productService.updateReviewCount(product.getId(), quantity);
  }
}
