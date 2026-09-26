package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.ProductImageResponse;
import org.oplearn.project.entity.ProductImage;
import org.oplearn.project.exception.ProductImageNotFoundException;
import org.oplearn.project.repository.ProductImageRepository;
import org.oplearn.project.service.ProductImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

  private final ProductImageRepository repository;

  @Override
  @Transactional
  public ProductImage create(ProductImage productImage) {
    log.info("(create) productImage: {}", productImage);
    return repository.save(productImage);
  }

  @Override
  @Transactional
  public ProductImage update(ProductImage productImage, Long id) {
    log.info("(update) id: {}, productImage: {}", id, productImage);
    ProductImage existing = findByIdOrThrow(id);

    if (productImage.getProductId() != null) {
      existing.setProductId(productImage.getProductId());
    }
    if (productImage.getImageUrl() != null) {
      existing.setImageUrl(productImage.getImageUrl());
    }
    if (productImage.getDisplayOrder() != null) {
      existing.setDisplayOrder(productImage.getDisplayOrder());
    }

    return repository.save(existing);
  }

  @Override
  public ProductImageResponse detail(Long id) {
    log.info("(detail) id: {}", id);
    return ProductImageResponse.from(findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(delete) id: {}", id);
    findByIdOrThrow(id);
    repository.softDeleteById(id);
  }

  @Override
  public List<ProductImageResponse> findAllByProductId(Long productId) {
    log.info("(findAllByProductId) productId: {}", productId);
    return repository.findAllByProductIdAndIsDeletedFalseOrderByDisplayOrderAsc(productId).stream()
        .map(ProductImageResponse::from)
        .toList();
  }

  @Override
  public ProductImage findByIdOrThrow(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(ProductImageNotFoundException::new);
  }
}
