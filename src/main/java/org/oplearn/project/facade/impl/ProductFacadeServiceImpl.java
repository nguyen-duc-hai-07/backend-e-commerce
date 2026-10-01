package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ProductFilterRequest;
import org.oplearn.project.dto.request.ProductRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ProductDetailResponse;
import org.oplearn.project.dto.response.ProductImageResponse;
import org.oplearn.project.dto.response.ProductResponse;
import org.oplearn.project.dto.response.ProductVariantResponse;
import org.oplearn.project.facade.ProductFacadeService;
import org.oplearn.project.service.CategoryService;
import org.oplearn.project.service.ProductImageService;
import org.oplearn.project.service.ProductService;
import org.oplearn.project.service.ProductVariantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductFacadeServiceImpl implements ProductFacadeService {

  private final ProductService productService;
  private final ProductVariantService productVariantService;
  private final ProductImageService productImageService;
  private final CategoryService categoryService;

  @Override
  @Transactional
  public ProductResponse create(ProductRequest request) {
    log.info("(facade) create product request: {}", request);

    categoryService.detail(request.getCategoryId());

    return productService.create(request);
  }

  @Override
  @Transactional
  public ProductResponse update(ProductRequest request, Long id) {
    log.info("(facade) update product id: {}, request: {}", id, request);

    if (request.getCategoryId() != null) {
      categoryService.detail(request.getCategoryId());
    }

    return productService.update(request, id);
  }

  @Override
  public PageResponse<ProductResponse> listByCategoryId(ProductFilterRequest request) {
    log.info("(facade) list products by categoryId: {}, page: {}, size: {}, sortBy: {}, direction: {}",
      request.getCategoryId(), request.getPage(), request.getSize(), request.getSortBy(), request.getDirection()
    );

    categoryService.detail(request.getCategoryId());

    return productService.listByCategoryId(request);
  }

  @Override
  public PageResponse<ProductResponse> search(ProductFilterRequest request) {
    log.info("(facade) search products keyword: {}, categoryId: {}, page: {}, size: {}",
      request.getKeyword(), request.getCategoryId(), request.getPage(), request.getSize()
    );

    if (request.getCategoryId() != null) {
      categoryService.detail(request.getCategoryId());
    }

    return productService.search(request);
  }

  @Override
  public ProductDetailResponse detail(Long id) {
    log.info("(facade) detail with id : {}", id);

    ProductResponse product = productService.detail(id);

    List<ProductVariantResponse> variants = productVariantService.findAllByProductId(id);

    List<ProductImageResponse> images = productImageService.findAllByProductId(id);

    return ProductDetailResponse.of(product, images, variants);
  }
}
