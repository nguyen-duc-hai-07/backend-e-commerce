package org.oplearn.project.facade;

import org.oplearn.project.dto.request.CartItemRequest;
import org.oplearn.project.dto.response.CartItemResponse;
import org.oplearn.project.dto.response.CursorPageResponse;

public interface CartItemFacadeService {
  CartItemResponse create(CartItemRequest request);

  CartItemResponse update(Integer quantity, Long id);

  CartItemResponse detail(Long id);

  CursorPageResponse<CartItemResponse> findByUserId(Long userId , Long cursor , int size);

  void delete(Long id);
}
