package org.oplearn.project.service;

import org.oplearn.project.dto.response.ProductImageResponse;
import org.oplearn.project.entity.ProductImage;

import java.util.List;

public interface ProductImageService {

  ProductImage create(ProductImage productImage);

  ProductImage update(ProductImage productImage, Long id);

  ProductImageResponse detail(Long id);

  void delete(Long id);

  List<ProductImageResponse> findAllByProductId(Long productId);

  ProductImage findByIdOrThrow(Long id);
}
