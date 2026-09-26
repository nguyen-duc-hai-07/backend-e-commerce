package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.ProductVariantResponse;
import org.oplearn.project.entity.ProductVariant;
import org.oplearn.project.exception.ProductVariantNotFoundException;
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
  public ProductVariant create(ProductVariant productVariant) {
    log.info("(create) productVariant: {}", productVariant);
    return repository.save(productVariant);
  }

  @Override
  @Transactional
  public ProductVariant update(ProductVariant productVariant, Long id) {
    log.info("(update) id: {}, productVariant: {}", id, productVariant);
    ProductVariant existing = findByIdOrThrow(id);

    if (productVariant.getProductId() != null) {
      existing.setProductId(productVariant.getProductId());
    }
    if (productVariant.getSku() != null) {
      existing.setSku(productVariant.getSku());
    }
    if (productVariant.getAttributes() != null) {
      existing.setAttributes(productVariant.getAttributes());
    }
    if (productVariant.getPrice() != null) {
      existing.setPrice(productVariant.getPrice());
    }
    if (productVariant.getQuantity() != null) {
      existing.setQuantity(productVariant.getQuantity());
    }

    return repository.save(existing);
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

  @Override
  public ProductVariant findByIdOrThrow(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(ProductVariantNotFoundException::new);
  }

  @Override
  public BigDecimal findMinPriceByProductId(Long productId) {
    return repository.findMinPriceByProductId(productId)
      .orElse(BigDecimal.ZERO);
  }
}
