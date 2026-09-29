package org.oplearn.project.service;

import org.oplearn.project.dto.request.ProductFilterRequest;
import org.oplearn.project.dto.request.ProductRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ProductResponse;
import java.math.BigDecimal;

public interface ProductService {

  ProductResponse create(ProductRequest request);

  ProductResponse update(ProductRequest request, Long id);

  ProductResponse detail(Long id);

  void delete(Long id);

  PageResponse<ProductResponse> listByCategoryId(ProductFilterRequest request);

  PageResponse<ProductResponse> search(ProductFilterRequest request);

  void checkProductExist(Long id);

  PageResponse<ProductResponse> random();

  void updateMinPrice(Long id, BigDecimal minPrice);

  void increaseSoldCount(Long id, int quantity);

  void updateAverageRating(Long id, BigDecimal averageRating);

  void updateReviewCount(Long id, int quantity);
}
