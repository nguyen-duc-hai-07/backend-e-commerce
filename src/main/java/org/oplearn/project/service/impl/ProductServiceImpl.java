package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ProductResponse;
import org.oplearn.project.entity.Product;
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

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.DIRECTION_ASC;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository repository;

  @Override
  @Transactional
  public Product create(Product product) {
    log.info("(create) product: {}", product);
    return repository.save(product);
  }

  @Override
  @Transactional
  public Product update(Product product, Long id) {
    log.info("(update) product id: {}, data: {}", id, product);

    Product existingProduct = findByIdOrThrow(id);

    if (product.getName() != null) {
      existingProduct.setName(product.getName());
    }
    if (product.getDescription() != null) {
      existingProduct.setDescription(product.getDescription());
    }
    if (product.getCategoryId() != null) {
      existingProduct.setCategoryId(product.getCategoryId());
    }
    if (product.getThumbnailUrl() != null) {
      existingProduct.setThumbnailUrl(product.getThumbnailUrl());
    }
    if (product.getMinPrice() != null) {
      existingProduct.setMinPrice(product.getMinPrice());
    }
    if (product.getSoldCount() != null) {
      existingProduct.setSoldCount(product.getSoldCount());
    }

    return repository.save(existingProduct);
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
  public PageResponse<ProductResponse> listByCategoryId(Long categoryId, int page, int size, String sortBy, String direction) {
    log.info("(listByCategoryId) categoryId: {}, page: {}, size: {}, sortBy: {}, direction: {}", categoryId, page, size, sortBy, direction);

    String sortProperty = "id";
    boolean isAsc = DIRECTION_ASC.equalsIgnoreCase(direction);

    if (sortBy != null) {
      String s = sortBy.trim().toLowerCase();
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
    Page<Product> productPage = repository.findByCategoryId(categoryId, pageable);

    return PageResponse.of(
      productPage.map(ProductResponse::from).getContent(),
      (int) productPage.getTotalElements()
    );
  }

  @Override
  public PageResponse<ProductResponse> search(String keyword, Long categoryId, int page, int size) {
    log.info("(search) keyword: {}, categoryId: {}, page: {}, size: {}", keyword, categoryId, page, size);
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
  public Product findByIdOrThrow(Long id) {
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
}
