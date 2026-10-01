package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.constants.OpLearnConstants;
import org.oplearn.project.dto.request.ProductFilterRequest;
import org.oplearn.project.dto.request.ProductRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ProductResponse;
import org.oplearn.project.entity.Product;
import org.oplearn.project.exception.ProductAlreadyExistedException;
import org.oplearn.project.exception.ProductNotFoundException;
import org.oplearn.project.repository.ProductRepository;
import org.oplearn.project.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.DIRECTION_ASC;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository repository;

  @Override
  @Transactional
  public ProductResponse create(ProductRequest request) {
    log.info("(create) product request: {}", request);

    String name = request.getName().trim();
    if (repository.existsByNameAndIsDeletedFalse(name)) {
      log.error("(create) product name already exists: {}", name);
      throw new ProductAlreadyExistedException();
    }

    Product product = Product.builder()
        .name(name)
        .description(request.getDescription())
        .categoryId(request.getCategoryId())
        .thumbnailUrl(request.getThumbnailUrl())
        .minPrice(request.getMinPrice() != null ? request.getMinPrice() : BigDecimal.ZERO)
        .soldCount(0)
        .build();

    Product savedProduct = repository.save(product);
    return ProductResponse.from(savedProduct);
  }

  @Override
  @Transactional
  public ProductResponse update(ProductRequest request, Long id) {
    log.info("(update) product id: {}, request: {}", id, request);

    Product existingProduct = findByIdOrThrow(id);

    if (request.getName() != null) {
      String name = request.getName().trim();
      if (repository.existsByNameAndIdNotAndIsDeletedFalse(name, id)) {
        log.error("(update) product name already exists: {}", name);
        throw new ProductAlreadyExistedException();
      }
    }

    setProductValues(existingProduct, request);

    Product updatedProduct = repository.save(existingProduct);
    return ProductResponse.from(updatedProduct);
  }

  private void setProductValues(Product target, ProductRequest source) {
    if (source.getName() != null) {
      target.setName(source.getName().trim());
    }
    if (source.getDescription() != null) {
      target.setDescription(source.getDescription());
    }
    if (source.getCategoryId() != null) {
      target.setCategoryId(source.getCategoryId());
    }
    if (source.getThumbnailUrl() != null) {
      target.setThumbnailUrl(source.getThumbnailUrl());
    }
    if (source.getMinPrice() != null) {
      target.setMinPrice(source.getMinPrice());
    }
  }

  @Override
  public ProductResponse detail(Long id) {
    log.info("(detail) product id: {}", id);
    return ProductResponse.from(findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(delete) product id: {}", id);
    findByIdOrThrow(id);
    repository.softDeleteById(id);
  }

  @Override
  public PageResponse<ProductResponse> listByCategoryId(ProductFilterRequest request) {
    log.info("(listByCategoryId) categoryId: {}, page: {}, size: {}, sortBy: {}, direction: {}",
      request.getCategoryId(), request.getPage(), request.getSize(), request.getSortBy(), request.getDirection()
    );

    if (request.getCategoryId() == null) {
      request = new ProductFilterRequest();
    }

    int page = (request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
    int size = (request.getSize() != null && request.getSize() > 0)
      ? Math.min(request.getSize(), OpLearnConstants.VariableConstant.MAX_PAGE_SIZE)
      : Integer.parseInt(OpLearnConstants.VariableConstant.SIZE_DEFAULT);

    String sortProperty = "id";
    boolean isAsc = DIRECTION_ASC.equalsIgnoreCase(request.getDirection());

    if (request.getSortBy() != null) {
      String s = request.getSortBy().trim().toLowerCase();
      if ("min_price".equals(s)) {
        sortProperty = "minPrice";
      } else if ("sold_count".equals(s)) {
        sortProperty = "soldCount";
      }
    }

    Sort sort = isAsc
      ? Sort.by(sortProperty).ascending()
      : Sort.by(sortProperty).descending();

    if (!"id".equals(sortProperty)) {
      sort = sort.and(Sort.by("id").descending());
    }

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Product> productPage = repository.findByCategoryId(request.getCategoryId(), pageable);

    return PageResponse.of(
      productPage.map(ProductResponse::from).getContent(),
      (int) productPage.getTotalElements()
    );
  }

  @Override
  public PageResponse<ProductResponse> search(ProductFilterRequest request) {
    log.info("(search) keyword: {}, categoryId: {}, page: {}, size: {}",
      request.getKeyword(), request.getCategoryId(), request.getPage(), request.getSize());

    if(request.getKeyword() == null) {
      request = new ProductFilterRequest();
    }

    int page = (request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
    int size = (request.getSize() != null && request.getSize() > 0)
      ? Math.min(request.getSize(), OpLearnConstants.VariableConstant.MAX_PAGE_SIZE)
      : Integer.parseInt(OpLearnConstants.VariableConstant.SIZE_DEFAULT);

    String keyword = request.getKeyword();
    Long categoryId = request.getCategoryId();

    Pageable pageable = PageRequest.of(page, size);

    String kw = StringUtils.hasText(keyword) ? keyword.trim() : null;

    Page<ProductResponse> productPage = repository.search(kw, categoryId, pageable)
      .map(ProductResponse::from);

    return PageResponse.of(
      productPage.getContent(),
      (int) productPage.getTotalElements()
    );
  }

  @Override
  public PageResponse<ProductResponse> random() {
    log.info("(random) get random products");

    Instant sevenDaysAgo = Instant.now().minus(7, ChronoUnit.DAYS);
    List<Long> ids = repository.findFastRandomIds(sevenDaysAgo, 10);

    if (ids.size() < 10) {
      ids = repository.findRandomIds(sevenDaysAgo, 10);
    }

    if (ids.isEmpty()) {
      return PageResponse.empty();
    }

    List<Product> products = new ArrayList<>(repository.findByIds(ids));
    Collections.shuffle(products);

    return PageResponse.of(
      products.stream().map(ProductResponse::from).toList(),
      products.size()
    );
  }

  @Override
  public void checkProductExist(Long id) {
    log.debug("(checkProductExist) id: {}", id);
    if (!repository.existsByIdAndIsDeletedFalse(id)) {
      log.error("(checkProductExist) product not found: {}", id);
      throw new ProductNotFoundException();
    }
  }

  private Product findByIdOrThrow(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(ProductNotFoundException::new);
  }

  @Override
  @Transactional
  public void updateMinPrice(Long id, BigDecimal minPrice) {
    log.info("(updateMinPrice) id: {}, minPrice: {}", id, minPrice);
    repository.updateMinPrice(id, minPrice);
  }

  @Override
  @Transactional
  public void increaseSoldCount(Long id, int quantity) {
    log.info("(increaseSoldCount) id: {}, quantity: {}", id, quantity);
    repository.increaseSoldCount(id, quantity);
  }

  @Override
  @Transactional
  public void updateAverageRating(Long id, BigDecimal averageRating) {
    log.info("(updateAverageRating) id: {}, averageRating: {}", id, averageRating);
    repository.updateAverageRating(id, averageRating);
  }

  @Override
  @Transactional
  public void updateReviewCount(Long id, int quantity) {
    log.info("(updateReviewCount) id: {}, quantity: {}", id, quantity);
    repository.updateReviewCount(id, quantity);
  }
}
