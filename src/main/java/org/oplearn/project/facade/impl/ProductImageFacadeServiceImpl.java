package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ProductImageRequest;
import org.oplearn.project.dto.response.ProductImageResponse;
import org.oplearn.project.facade.ProductImageFacadeService;
import org.oplearn.project.service.ProductImageService;
import org.oplearn.project.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductImageFacadeServiceImpl implements ProductImageFacadeService {

  private final ProductImageService productImageService;
  private final ProductService productService;

  @Override
  @Transactional
  public ProductImageResponse create(ProductImageRequest request) {
    log.info("(facade) create productImage request: {}", request);

    productService.checkProductExist(request.getProductId());

    return productImageService.create(request);
  }

  @Override
  @Transactional
  public ProductImageResponse update(ProductImageRequest request, Long id) {
    log.info("(facade) update productImage id: {}, request: {}", id, request);

    if (request.getProductId() != null) {
      productService.checkProductExist(request.getProductId());
    }

    return productImageService.update(request, id);
  }
}
