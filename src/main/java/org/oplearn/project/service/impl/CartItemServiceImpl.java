package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.CartItemRequest;
import org.oplearn.project.dto.response.CartItemResponse;
import org.oplearn.project.dto.response.CursorPageResponse;
import org.oplearn.project.entity.CartItem;
import org.oplearn.project.exception.CartItemLimitExceededException;
import org.oplearn.project.exception.CartItemNotFoundException;
import org.oplearn.project.exception.InvalidCartItemQuantityException;
import org.oplearn.project.repository.CartItemRepository;
import org.oplearn.project.service.CartItemService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
  private static final int MAX_CART_ITEMS_PER_USER = 100;

  private final CartItemRepository repository;

  @Override
  @Transactional
  public CartItemResponse create(CartItemRequest request) {
    log.info("(create) cartItem request: {}", request);

    Optional<CartItem> existingCartItem = repository.findByUserIdAndVariantIdAndIsDeletedFalse(
      request.getUserId(), request.getVariantId()
    );

    CartItem cartItem;
    if (existingCartItem.isPresent()) {
      cartItem = existingCartItem.get();
      cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
    } else {
      Long cartItemCount = repository.countByUserIdAndIsDeletedFalse(request.getUserId());
      if (cartItemCount != null && cartItemCount >= MAX_CART_ITEMS_PER_USER) {
        log.error("user {} has reached max cart items", request.getUserId());
        throw new CartItemLimitExceededException();
      }
      cartItem = CartItem.builder()
        .userId(request.getUserId())
        .variantId(request.getVariantId())
        .quantity(request.getQuantity())
        .build();
    }

    CartItem saved = repository.save(cartItem);

    return CartItemResponse.from(saved);
  }

  @Override
  @Transactional
  public CartItemResponse update(Integer quantity, Long id) {
    log.info("(update) cartItem id: {}, quantity: {}", id, quantity);

    if (quantity == null || quantity <= 0) {
      throw new InvalidCartItemQuantityException();
    }

    CartItem cartItem = findByIdOrThrow(id);

    cartItem.setQuantity(quantity);
    CartItem saved = repository.save(cartItem);

    return CartItemResponse.from(saved);
  }

  @Override
  public CartItemResponse detail(Long id) {
    log.info("(detail) cartItem id: {}", id);
    return CartItemResponse.from(findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(delete) cartItem id: {}", id);
    findByIdOrThrow(id);
    repository.softDeleteById(id);
  }

  @Override
  public CursorPageResponse<CartItemResponse> findByUserId(Long userId, Long cursor, int size) {
    log.info("findByUserId: {}, cursor: {}, size: {}", userId, cursor, size);
    int pageSize = Math.min(Math.max(size, 1), 50);
    Pageable pageable = PageRequest.of(0, pageSize + 1);

    List<CartItemResponse> cartItems = repository.findByUserId(userId, cursor, pageable);

    boolean hasNext = cartItems.size() > pageSize;
    Long nextCursor = null;

    if (hasNext) {
      cartItems = cartItems.subList(0, pageSize);
      nextCursor = cartItems.get(cartItems.size() - 1).getId();
    }

    Long total = (cursor == null) ? repository.countByUserIdAndIsDeletedFalse(userId) : null;

    return CursorPageResponse.of(cartItems, nextCursor, hasNext, total);
  }

  private CartItem findByIdOrThrow(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(CartItemNotFoundException::new);
  }
}
