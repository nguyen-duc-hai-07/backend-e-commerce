package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.CartItemRequest;
import org.oplearn.project.dto.response.CartItemResponse;
import org.oplearn.project.dto.response.CursorPageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.facade.CartItemFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.CREATED_MESSAGE;
import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart-items")
public class CartItemController {

  private final CartItemFacadeService facade;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<CartItemResponse> create(@Valid @RequestBody CartItemRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @PutMapping("/{id}")
  public ResponseGeneral<CartItemResponse> update(
      @PathVariable("id") Long id,
      @RequestParam Integer quantity
  ) {
    log.info("(update) id: {}, quantity: {}", id, quantity);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.update(quantity, id));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<CartItemResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.detail(id));
  }

  @DeleteMapping("/{id}")
  public ResponseGeneral<Void> delete(@PathVariable("id") Long id) {
    log.info("(delete) id: {}", id);
    facade.delete(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }

  @GetMapping("/user/{userId}")
  public ResponseGeneral<CursorPageResponse<CartItemResponse>> findByUserId(
      @PathVariable("userId") Long userId,
      @RequestParam(value = "cursor", required = false) Long cursor,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    log.info("(findByUserId) userId: {}, cursor: {}, size: {}", userId, cursor, size);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.findByUserId(userId, cursor, size));
  }
}
