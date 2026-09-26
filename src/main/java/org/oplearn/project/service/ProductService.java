package org.oplearn.project.service;

import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ProductResponse;
import org.oplearn.project.entity.Product;

import java.math.BigDecimal;

public interface ProductService {

  Product create(Product product);

  Product update(Product product, Long id);

  ProductResponse detail(Long id);

  void delete(Long id);

  PageResponse<ProductResponse> listByCategoryId(Long categoryId, int page, int size, String sortBy, String direction);

  PageResponse<ProductResponse> search(String keyword, Long categoryId, int page, int size);

  Product findByIdOrThrow(Long id);

  PageResponse<ProductResponse> random();

  void updateMinPrice(Long id, BigDecimal minPrice);

  void increaseSoldCount(Long id, int quantity);
}
