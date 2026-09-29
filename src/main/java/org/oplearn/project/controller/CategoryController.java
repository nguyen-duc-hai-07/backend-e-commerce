package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.CategoryFilterRequest;
import org.oplearn.project.dto.request.CategoryRequest;
import org.oplearn.project.dto.response.CategoryResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

  private final CategoryService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, service.create(request));
  }

  @PutMapping("/{id}")
  public ResponseGeneral<CategoryResponse> update(
      @PathVariable("id") Long id,
      @Valid @RequestBody CategoryRequest request
  ) {
    log.info("(update) id: {}, request: {}", id, request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.update(request, id));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<CategoryResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.detail(id));
  }

  @DeleteMapping("/{id}")
  public ResponseGeneral<Void> delete(@PathVariable("id") Long id) {
    log.info("(delete) id: {}", id);
    service.delete(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }

  @GetMapping("/slug/{slug}")
  public ResponseGeneral<CategoryResponse> findBySlug(@PathVariable("slug") String slug) {
    log.info("(findBySlug) slug: {}", slug);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.findBySlug(slug));
  }

  @PostMapping("/filter")
  public ResponseGeneral<PageResponse<CategoryResponse>> filter(
      @Valid @RequestBody(required = false) CategoryFilterRequest request
  ) {
    log.info("(filter) request: {}", request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.filter(request));
  }
}
