package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ProductVariantRequest;
import org.oplearn.project.dto.response.ProductVariantResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.facade.ProductVariantFacadeService;
import org.oplearn.project.service.ProductVariantService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.CREATED_MESSAGE;
import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-variants")
public class ProductVariantController {

  private final ProductVariantFacadeService facade;
  private final ProductVariantService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<ProductVariantResponse> create(@Valid @RequestBody ProductVariantRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @PutMapping("/{id}")
  public ResponseGeneral<ProductVariantResponse> update(
      @PathVariable("id") Long id,
      @Valid @RequestBody ProductVariantRequest request
  ) {
    log.info("(update) id: {}, request: {}", id, request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.update(request, id));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<ProductVariantResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.detail(id));
  }

  @DeleteMapping("/{id}")
  public ResponseGeneral<Void> delete(@PathVariable("id") Long id) {
    log.info("(delete) id: {}", id);
    service.delete(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }

  @GetMapping("/sku/{sku}")
  public ResponseGeneral<ProductVariantResponse> findBySku(@PathVariable("sku") String sku) {
    log.info("(findBySku) sku: {}", sku);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.findBySku(sku));
  }

  @GetMapping("/product/{productId}")
  public ResponseGeneral<List<ProductVariantResponse>> findAllByProductId(@PathVariable("productId") Long productId) {
    log.info("(findAllByProductId) productId: {}", productId);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, service.findAllByProductId(productId));
  }
}
