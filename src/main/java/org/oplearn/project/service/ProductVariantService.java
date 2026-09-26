package org.oplearn.project.service;

import org.oplearn.project.dto.response.ProductVariantResponse;
import org.oplearn.project.entity.ProductVariant;

import java.math.BigDecimal;
import java.util.List;

public interface ProductVariantService {

  ProductVariant create(ProductVariant productVariant);

  ProductVariant update(ProductVariant productVariant, Long id);

  ProductVariantResponse detail(Long id);

  void delete(Long id);

  ProductVariantResponse findBySku(String sku);

  List<ProductVariantResponse> findAllByProductId(Long productId);

  ProductVariant findByIdOrThrow(Long id);

  BigDecimal findMinPriceByProductId(Long productId);
}
