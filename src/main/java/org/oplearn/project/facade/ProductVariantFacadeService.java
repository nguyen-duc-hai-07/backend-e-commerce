package org.oplearn.project.facade;

import org.oplearn.project.dto.request.ProductVariantRequest;
import org.oplearn.project.dto.response.ProductVariantResponse;

public interface ProductVariantFacadeService {

  ProductVariantResponse create(ProductVariantRequest request);

  ProductVariantResponse update(ProductVariantRequest request, Long id);

  void delete(Long id);
}
