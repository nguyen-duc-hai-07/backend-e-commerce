package org.oplearn.project.facade;

import org.oplearn.project.dto.request.ProductImageRequest;
import org.oplearn.project.dto.response.ProductImageResponse;

public interface ProductImageFacadeService {

  ProductImageResponse create(ProductImageRequest request);

  ProductImageResponse update(ProductImageRequest request, Long id);
}
