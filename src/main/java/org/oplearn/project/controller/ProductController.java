package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ProductRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ProductDetailResponse;
import org.oplearn.project.dto.response.ProductResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.facade.ProductFacadeService;
import org.oplearn.project.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.*;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductFacadeService facade;
  private final ProductService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @PutMapping("/{id}")
  public ResponseGeneral<ProductResponse> update(
    @PathVariable("id") Long id,
    @Valid @RequestBody ProductRequest request
  ) {
    log.info("(update) id: {}, request: {}", id, request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.update(request, id));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<ProductDetailResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.detail(id));
  }

  @DeleteMapping("/{id}")
  public ResponseGeneral<Void> delete(@PathVariable("id") Long id) {
    log.info("(delete) id: {}", id);
    service.delete(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }

  @GetMapping("/category/{categoryId}")
  public ResponseGeneral<PageResponse<ProductResponse>> listByCategoryId(
    @PathVariable("categoryId") Long categoryId,
    @RequestParam(value = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page,
    @RequestParam(value = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size,
    @RequestParam(value = PARAM_SORT_BY, defaultValue = SORT_BY_ID) String sortBy,
    @RequestParam(value = PARAM_DIRECTION, defaultValue = DIRECTION_DESC) String direction
  ) {
    log.info("(listByCategoryId) categoryId: {}, page: {}, size: {}, sortBy: {}, direction: {}", categoryId, page, size, sortBy, direction);
    size = Math.min(size, MAX_PAGE_SIZE);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.listByCategoryId(categoryId, page, size, sortBy, direction));
  }

  @GetMapping("/search")
  public ResponseGeneral<PageResponse<ProductResponse>> search(
    @RequestParam(value = PARAM_KEYWORD, required = false) String keyword,
    @RequestParam(value = PARAM_CATEGORY_ID, required = false) Long categoryId,
    @RequestParam(value = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page,
    @RequestParam(value = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size
  ) {
    log.info("(search) keyword: {}, categoryId: {}, page: {}, size: {}", keyword, categoryId, page, size);
    size = Math.min(size, MAX_PAGE_SIZE);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.search(keyword, categoryId, page, size));
  }

  @GetMapping("/random")
  public ResponseGeneral<PageResponse<ProductResponse>> random() {
    log.info("(random) products");
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE , service.random());
  }
}
