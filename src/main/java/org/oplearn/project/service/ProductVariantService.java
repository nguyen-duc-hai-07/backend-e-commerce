package org.oplearn.project.service;

import org.oplearn.project.dto.request.ProductVariantRequest;
import org.oplearn.project.dto.response.ProductVariantResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductVariantService {

  ProductVariantResponse create(ProductVariantRequest request);

  ProductVariantResponse update(ProductVariantRequest request, Long id);

  ProductVariantResponse detail(Long id);

  void delete(Long id);

  ProductVariantResponse findBySku(String sku);

  List<ProductVariantResponse> findAllByProductId(Long productId);

  BigDecimal findMinPriceByProductId(Long productId);
}
