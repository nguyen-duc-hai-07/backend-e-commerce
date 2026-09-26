package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ProductRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ProductResponse;
import org.oplearn.project.entity.Product;
import org.oplearn.project.facade.ProductFacadeService;
import org.oplearn.project.service.CategoryService;
import org.oplearn.project.service.ProductService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductFacadeServiceImpl implements ProductFacadeService {

  private final ProductService productService;
  private final CategoryService categoryService;

  @Override
  public ProductResponse create(ProductRequest request) {
    log.info("(facade) create product request: {}", request);

    categoryService.detail(request.getCategoryId());

    Product product = Product.builder()
        .name(request.getName().trim())
        .description(request.getDescription())
        .categoryId(request.getCategoryId())
        .thumbnailUrl(request.getThumbnailUrl())
        .build();

    Product savedProduct = productService.create(product);
    return ProductResponse.from(savedProduct);
  }

  @Override
  public ProductResponse update(ProductRequest request, Long id) {
    log.info("(facade) update product id: {}, request: {}", id, request);

    if (request.getCategoryId() != null) {
      categoryService.detail(request.getCategoryId());
    }

    Product product = Product.builder()
        .name(request.getName() != null ? request.getName().trim() : null)
        .description(request.getDescription())
        .categoryId(request.getCategoryId())
        .thumbnailUrl(request.getThumbnailUrl())
        .build();

    Product updatedProduct = productService.update(product, id);
    return ProductResponse.from(updatedProduct);
  }

  @Override
  public PageResponse<ProductResponse> listByCategoryId(Long categoryId, int page, int size, String direction) {
    log.info("(facade) list products by categoryId: {}, page: {}, size: {}, direction: {}", categoryId, page, size, direction);

    categoryService.detail(categoryId);

    return productService.listByCategoryId(categoryId, page, size, direction);
  }

  @Override
  public PageResponse<ProductResponse> search(String keyword, Long categoryId, int page, int size) {
    log.info("(facade) search products keyword: {}, categoryId: {}, page: {}, size: {}", keyword, categoryId, page, size);

    if (categoryId != null) {
      categoryService.detail(categoryId);
    }

    return productService.search(keyword, categoryId, page, size);
  }
}
