package org.oplearn.project.facade;

import org.oplearn.project.dto.request.ProductFilterRequest;
import org.oplearn.project.dto.request.ProductRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ProductResponse;

public interface ProductFacadeService {

  ProductResponse create(ProductRequest request);

  ProductResponse update(ProductRequest request, Long id);

  PageResponse<ProductResponse> listByCategoryId(ProductFilterRequest request);

  PageResponse<ProductResponse> search(ProductFilterRequest request);
}
