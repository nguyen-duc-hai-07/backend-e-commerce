package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ProductVariantRequest;
import org.oplearn.project.dto.response.ProductVariantResponse;
import org.oplearn.project.entity.ProductVariant;
import org.oplearn.project.exception.ProductVariantNotFoundException;
import org.oplearn.project.exception.SkuAlreadyExistedException;
import org.oplearn.project.repository.ProductVariantRepository;
import org.oplearn.project.service.ProductVariantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

  private final ProductVariantRepository repository;

  @Override
  @Transactional
  public ProductVariantResponse create(ProductVariantRequest request) {
    log.info("(create) productVariant request: {}", request);

    String sku = request.getSku().trim();
    if (repository.existsBySkuAndIsDeletedFalse(sku)) {
      log.error("(create) sku already exists: {}", sku);
      throw new SkuAlreadyExistedException();
    }

    ProductVariant productVariant = ProductVariant.builder()
        .productId(request.getProductId())
        .sku(sku)
        .attributes(request.getAttributes())
        .price(request.getPrice())
        .quantity(request.getQuantity())
        .build();

    ProductVariant saved = repository.save(productVariant);
    return ProductVariantResponse.from(saved);
  }

  @Override
  @Transactional
  public ProductVariantResponse update(ProductVariantRequest request, Long id) {
    log.info("(update) id: {}, request: {}", id, request);
    ProductVariant existing = findByIdOrThrow(id);

    if (request.getSku() != null) {
      String sku = request.getSku().trim();
      if (repository.existsBySkuAndIdNotAndIsDeletedFalse(sku, id)) {
        log.error("(update) sku already exists: {}", sku);
        throw new SkuAlreadyExistedException();
      }
    }

    setProductVariantValues(existing, request);

    ProductVariant updated = repository.save(existing);
    return ProductVariantResponse.from(updated);
  }

  private void setProductVariantValues(ProductVariant target, ProductVariantRequest source) {
    if (source.getProductId() != null) {
      target.setProductId(source.getProductId());
    }
    if (source.getSku() != null) {
      target.setSku(source.getSku().trim());
    }
    if (source.getAttributes() != null) {
      target.setAttributes(source.getAttributes());
    }
    if (source.getPrice() != null) {
      target.setPrice(source.getPrice());
    }
    if (source.getQuantity() != null) {
      target.setQuantity(source.getQuantity());
    }
  }

  @Override
  public ProductVariantResponse detail(Long id) {
    log.info("(detail) id: {}", id);
    return ProductVariantResponse.from(findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(delete) id: {}", id);
    findByIdOrThrow(id);
    repository.softDeleteById(id);
  }

  @Override
  public ProductVariantResponse findBySku(String sku) {
    log.info("(findBySku) sku: {}", sku);
    return repository.findBySkuAndIsDeletedFalse(sku)
        .map(ProductVariantResponse::from)
        .orElseThrow(ProductVariantNotFoundException::new);
  }

  @Override
  public List<ProductVariantResponse> findAllByProductId(Long productId) {
    log.info("(findAllByProductId) productId: {}", productId);
    return repository.findAllByProductIdAndIsDeletedFalse(productId).stream()
        .map(ProductVariantResponse::from)
        .toList();
  }

  private ProductVariant findByIdOrThrow(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(ProductVariantNotFoundException::new);
  }

  @Override
  public BigDecimal findMinPriceByProductId(Long productId) {
    return repository.findMinPriceByProductId(productId)
      .orElse(BigDecimal.ZERO);
  }
}
