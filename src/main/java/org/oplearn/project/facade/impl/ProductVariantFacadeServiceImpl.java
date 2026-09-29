package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ProductVariantRequest;
import org.oplearn.project.dto.response.ProductVariantResponse;
import org.oplearn.project.facade.ProductVariantFacadeService;
import org.oplearn.project.service.ProductService;
import org.oplearn.project.service.ProductVariantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductVariantFacadeServiceImpl implements ProductVariantFacadeService {

  private final ProductVariantService productVariantService;
  private final ProductService productService;

  @Override
  @Transactional
  public ProductVariantResponse create(ProductVariantRequest request) {
    log.info("(facade) create variant request: {}", request);

    productService.checkProductExist(request.getProductId());

    return productVariantService.create(request);
  }

  @Override
  @Transactional
  public ProductVariantResponse update(ProductVariantRequest request, Long id) {
    log.info("(facade) update variant id: {}, request: {}", id, request);

    if (request.getProductId() != null) {
      productService.checkProductExist(request.getProductId());
    }

    return productVariantService.update(request, id);
  }
}
