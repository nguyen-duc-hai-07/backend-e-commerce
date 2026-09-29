package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ProductImageRequest;
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
  public ProductImageResponse create(ProductImageRequest request) {
    log.info("(create) productImage request: {}", request);

    ProductImage image = ProductImage.builder()
        .productId(request.getProductId())
        .imageUrl(request.getImageUrl().trim())
        .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
        .build();

    ProductImage saved = repository.save(image);
    return ProductImageResponse.from(saved);
  }

  @Override
  @Transactional
  public ProductImageResponse update(ProductImageRequest request, Long id) {
    log.info("(update) id: {}, request: {}", id, request);
    ProductImage existing = findByIdOrThrow(id);

    setProductImageValues(existing, request);

    ProductImage updated = repository.save(existing);
    return ProductImageResponse.from(updated);
  }

  private void setProductImageValues(ProductImage target, ProductImageRequest source) {
    if (source.getProductId() != null) {
      target.setProductId(source.getProductId());
    }
    if (source.getImageUrl() != null) {
      target.setImageUrl(source.getImageUrl().trim());
    }
    if (source.getDisplayOrder() != null) {
      target.setDisplayOrder(source.getDisplayOrder());
    }
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
