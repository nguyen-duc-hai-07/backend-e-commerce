package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ReviewFilterRequest;
import org.oplearn.project.dto.request.ReviewRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ReviewResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.ReviewFacadeService;
import org.oplearn.project.service.OrderService;
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
  private final OrderService orderService;

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

    productService.checkProductExist(request.getProductId());

    User currentUser = currentUser();
    request.setUserId(currentUser.getId());

    if(!orderService.hasUserPurchasedProductAndCompleted(currentUser.getId(), request.getProductId())) {
      log.warn("(create) user not authorized");
      throw new UserUnauthorizedException();
    }

    ReviewResponse saved = reviewService.create(request);

    syncProductAverageRating(request.getProductId());

    syncProductReviewCount(request.getProductId());

    return ReviewResponse.of(saved, currentUser);
  }

  @Override
  @Transactional
  public ReviewResponse update(ReviewRequest request, Long id) {
    log.info("(facade) update review");

    User currentUser = currentUser();

    ReviewResponse existing = reviewService.detail(id);

    boolean isOwner = currentUser.getId().equals(existing.getUserId());

    if(!isOwner) {
      log.warn("(update) user not authorized");
      throw new UserUnauthorizedException();
    }

    ReviewResponse updated = reviewService.update(request, id);

    if (request.getRating() != null && !request.getRating().equals(existing.getRating())) {
      syncProductAverageRating(existing.getProductId());
    }

    return ReviewResponse.of(updated, currentUser);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(facade) delete review");

    ReviewResponse existing = reviewService.detail(id);

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

    productService.checkProductExist(request.getProductId());

    return reviewService.findByRatingAndProductId(request);
  }

  @Override
  public ReviewResponse detail(Long id) {
    log.info("(facade) detail review id: {}", id);

    ReviewResponse review = reviewService.detail(id);

    User user = userService.getAvailableUserAndThrow(review.getUserId());

    return ReviewResponse.of(review, user);
  }

  private void syncProductAverageRating(Long productId) {
    productService.checkProductExist(productId);

    BigDecimal averageRating = reviewService.findAverageRatingByProductId(productId);

    BigDecimal finalRating = averageRating != null
      ? averageRating.setScale(1, java.math.RoundingMode.HALF_UP)
      : BigDecimal.ZERO;

    productService.updateAverageRating(productId, finalRating);
  }

  private void syncProductReviewCount(Long productId) {
    productService.checkProductExist(productId);

    int quantity = reviewService.countByProductId(productId);

    productService.updateReviewCount(productId, quantity);
  }
}
