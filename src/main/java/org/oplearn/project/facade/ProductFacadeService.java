package org.oplearn.project.facade;

import org.oplearn.project.dto.request.ProductRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ProductDetailResponse;
import org.oplearn.project.dto.response.ProductResponse;

public interface ProductFacadeService {

  ProductResponse create(ProductRequest request);

  ProductResponse update(ProductRequest request, Long id);

  PageResponse<ProductResponse> listByCategoryId(Long categoryId, int page, int size, String sortBy, String direction);

  PageResponse<ProductResponse> search(String keyword, Long categoryId, int page, int size);

  ProductDetailResponse detail(Long id);
}
