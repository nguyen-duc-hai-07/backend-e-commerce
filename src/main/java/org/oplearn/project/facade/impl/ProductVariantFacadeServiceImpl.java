package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ProductVariantRequest;
import org.oplearn.project.dto.response.ProductVariantResponse;
import org.oplearn.project.entity.ProductVariant;
import org.oplearn.project.exception.SkuAlreadyExistedException;
import org.oplearn.project.facade.ProductVariantFacadeService;
import org.oplearn.project.repository.ProductVariantRepository;
import org.oplearn.project.service.ProductService;
import org.oplearn.project.service.ProductVariantService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductVariantFacadeServiceImpl implements ProductVariantFacadeService {

  private final ProductVariantService productVariantService;
  private final ProductService productService;
  private final ProductVariantRepository productVariantRepository;

  @Override
  public ProductVariantResponse create(ProductVariantRequest request) {
    log.info("(facade) create variant request: {}", request);

    productService.findByIdOrThrow(request.getProductId());

    String sku = request.getSku().trim();
    if (productVariantRepository.existsBySkuAndIsDeletedFalse(sku)) {
      throw new SkuAlreadyExistedException();
    }

    ProductVariant variant = ProductVariant.builder()
        .productId(request.getProductId())
        .sku(sku)
        .attributes(request.getAttributes())
        .price(request.getPrice())
        .quantity(request.getQuantity())
        .build();

    ProductVariant saved = productVariantService.create(variant);
    return ProductVariantResponse.from(saved);
  }

  @Override
  public ProductVariantResponse update(ProductVariantRequest request, Long id) {
    log.info("(facade) update variant id: {}, request: {}", id, request);

    productVariantService.findByIdOrThrow(id);

    if (request.getProductId() != null) {
      productService.findByIdOrThrow(request.getProductId());
    }

    String sku = request.getSku() != null ? request.getSku().trim() : null;
    if (sku != null && productVariantRepository.existsBySkuAndIdNotAndIsDeletedFalse(sku, id)) {
      throw new SkuAlreadyExistedException();
    }

    ProductVariant toUpdate = ProductVariant.builder()
        .productId(request.getProductId())
        .sku(sku)
        .attributes(request.getAttributes())
        .price(request.getPrice())
        .quantity(request.getQuantity())
        .build();

    ProductVariant updated = productVariantService.update(toUpdate, id);
    return ProductVariantResponse.from(updated);
  }
}
