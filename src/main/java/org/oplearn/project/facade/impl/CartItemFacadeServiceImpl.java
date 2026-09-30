package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.CartItemRequest;
import org.oplearn.project.dto.response.CartItemResponse;
import org.oplearn.project.dto.response.CursorPageResponse;
import org.oplearn.project.dto.response.ProductResponse;
import org.oplearn.project.dto.response.ProductVariantResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.CartItemFacadeService;
import org.oplearn.project.service.CartItemService;
import org.oplearn.project.service.ProductService;
import org.oplearn.project.service.ProductVariantService;
import org.oplearn.project.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemFacadeServiceImpl implements CartItemFacadeService {
  private final CartItemService cartItemService;
  private final UserService userService;
  private final ProductVariantService productVariantService;
  private final ProductService productService;

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
  public CartItemResponse create(CartItemRequest request) {
    log.info("(facade) create cartItem request: {}", request);

    User currentUser = currentUser();
    request.setUserId(currentUser.getId());

    ProductVariantResponse productVariant = productVariantService.detail(request.getVariantId());

    ProductResponse product = productService.detail(productVariant.getProductId());

    CartItemResponse saved = cartItemService.create(request);

    return CartItemResponse.of(saved , productVariant , product);
  }

  @Override
  public CursorPageResponse<CartItemResponse> findByUserId(Long userId, Long cursor, int size) {
    log.info("(facade) findByUserId: {}, cursor: {}, size: {}", userId, cursor, size);

    User currentUser = currentUser();
    boolean isSelf = currentUser.getId().equals(userId);

    if (!isAdmin() && !isSelf) {
      log.warn("(findByUserId) user not authorized");
      throw new UserUnauthorizedException();
    }

    return cartItemService.findByUserId(userId, cursor, size);
  }

  @Override
  public CartItemResponse update(Integer quantity, Long id) {
    log.info("(facade) update cartItem id: {}, quantity: {}", id, quantity);

    CartItemResponse cart = cartItemService.detail(id);

    User currentUser = currentUser();
    boolean isSelf = currentUser.getId().equals(cart.getUserId());

    if (!isAdmin() && !isSelf) {
      log.warn("(update) user not authorized");
      throw new UserUnauthorizedException();
    }

    ProductVariantResponse variant = productVariantService.detail(cart.getVariantId());
    ProductResponse product = productService.detail(variant.getProductId());

    CartItemResponse updated = cartItemService.update(quantity, id);

    return CartItemResponse.of(updated, variant, product);
  }

  @Override
  public CartItemResponse detail(Long id) {
    log.info("(facade) detail cartItem id: {}", id);

    CartItemResponse cart = cartItemService.detail(id);

    User currentUser = currentUser();
    boolean isSelf = currentUser.getId().equals(cart.getUserId());

    if (!isAdmin() && !isSelf) {
      log.warn("(detail) user not authorized");
      throw new UserUnauthorizedException();
    }

    ProductVariantResponse variant = productVariantService.detail(cart.getVariantId());
    ProductResponse product = productService.detail(variant.getProductId());

    return CartItemResponse.of(cart, variant, product);
  }

  @Override
  public void delete(Long id) {
    log.info("(facade) delete cartItem id: {}", id);

    CartItemResponse cart = cartItemService.detail(id);

    User currentUser = currentUser();
    boolean isSelf = currentUser.getId().equals(cart.getUserId());

    if (!isAdmin() && !isSelf) {
      log.warn("(delete) user not authorized");
      throw new UserUnauthorizedException();
    }

    cartItemService.delete(id);
  }
}
