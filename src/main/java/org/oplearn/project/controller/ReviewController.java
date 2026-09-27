package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ReviewRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.ReviewResponse;
import org.oplearn.project.facade.ReviewFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.CREATED_MESSAGE;
import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.PARAM_PAGE;
import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.PARAM_SIZE;
import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.MAX_PAGE_SIZE;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.PAGE_DEFAULT;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.SIZE_DEFAULT;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

  private final ReviewFacadeService facade;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<ReviewResponse> create(@Valid @RequestBody ReviewRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @PutMapping("/{id}")
  public ResponseGeneral<ReviewResponse> update(
      @PathVariable("id") Long id,
      @Valid @RequestBody ReviewRequest request
  ) {
    log.info("(update) id: {}, request: {}", id, request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.update(request, id));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<ReviewResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.detail(id));
  }

  @DeleteMapping("/{id}")
  public ResponseGeneral<Void> delete(@PathVariable("id") Long id) {
    log.info("(delete) id: {}", id);
    facade.delete(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }

  @GetMapping("/product/{productId}")
  public ResponseGeneral<PageResponse<ReviewResponse>> findByRatingAndProductId(
      @PathVariable("productId") Long productId,
      @RequestParam(value = "rating", required = false) Integer rating,
      @RequestParam(value = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page,
      @RequestParam(value = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size
  ) {
    log.info("(findByRatingAndProductId) productId: {}, rating: {}, page: {}, size: {}", productId, rating, page, size);
    size = Math.min(size, MAX_PAGE_SIZE);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.findByRatingAndProductId(rating, productId, page, size));
  }
}
