package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ProductImageRequest;
import org.oplearn.project.dto.response.ProductImageResponse;
import org.oplearn.project.entity.ProductImage;
import org.oplearn.project.facade.ProductImageFacadeService;
import org.oplearn.project.service.ProductImageService;
import org.oplearn.project.service.ProductService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductImageFacadeServiceImpl implements ProductImageFacadeService {

  private final ProductImageService productImageService;
  private final ProductService productService;

  @Override
  public ProductImageResponse create(ProductImageRequest request) {
    log.info("(facade) create productImage request: {}", request);

    productService.findByIdOrThrow(request.getProductId());

    ProductImage image = ProductImage.builder()
        .productId(request.getProductId())
        .imageUrl(request.getImageUrl().trim())
        .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
        .build();

    ProductImage saved = productImageService.create(image);
    return ProductImageResponse.from(saved);
  }

  @Override
  public ProductImageResponse update(ProductImageRequest request, Long id) {
    log.info("(facade) update productImage id: {}, request: {}", id, request);

    productImageService.findByIdOrThrow(id);

    if (request.getProductId() != null) {
      productService.findByIdOrThrow(request.getProductId());
    }

    ProductImage toUpdate = ProductImage.builder()
        .productId(request.getProductId())
        .imageUrl(request.getImageUrl() != null ? request.getImageUrl().trim() : null)
        .displayOrder(request.getDisplayOrder())
        .build();

    ProductImage updated = productImageService.update(toUpdate, id);
    return ProductImageResponse.from(updated);
  }
}
