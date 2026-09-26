package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.constants.OpLearnConstants;
import org.oplearn.project.dto.request.CategoryRequest;
import org.oplearn.project.dto.response.CategoryResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.entity.Category;
import org.oplearn.project.exception.CategoryAlreadyExistedException;
import org.oplearn.project.exception.CategoryNotFoundException;
import org.oplearn.project.repository.CategoryRepository;
import org.oplearn.project.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository repository;

  @Override
  @Transactional
  public CategoryResponse create(CategoryRequest request) {
    log.info("(create) category request: {}", request);

    String name = request.getName().trim();
    if (repository.existsByNameAndIsDeletedFalse(name)) {
      log.error("(create) category name already exists: {}", name);
      throw new CategoryAlreadyExistedException();
    }

    Category category = new Category(name);
    Category saved = repository.save(category);
    return CategoryResponse.from(saved);
  }

  @Override
  @Transactional
  public CategoryResponse update(CategoryRequest request, Long id) {
    log.info("(update) category id: {}, request: {}", id, request);

    Category category = repository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(CategoryNotFoundException::new);

    String name = request.getName().trim();
    if (repository.existsByNameAndIdNotAndIsDeletedFalse(name, id)) {
      log.error("(update) category name already exists: {}", name);
      throw new CategoryAlreadyExistedException();
    }

    category.setName(name);
    category.setSlug(Category.toSlug(name));

    Category updated = repository.save(category);
    return CategoryResponse.from(updated);
  }

  @Override
  public CategoryResponse detail(Long id) {
    log.info("(detail) category id: {}", id);

    Category category = repository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(CategoryNotFoundException::new);

    return CategoryResponse.from(category);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(delete) category id: {}", id);

    repository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(CategoryNotFoundException::new);

    repository.softDeleteById(id);
  }

  @Override
  public CategoryResponse findBySlug(String slug) {
    return CategoryResponse.from(repository.findBySlugAndIsDeletedFalse(slug)
        .orElseThrow(CategoryNotFoundException::new));
  }

  @Override
  public PageResponse<CategoryResponse> list(String keyword , int size, int page , boolean isAll) {
    Pageable pageable = isAll
      ? PageRequest.of(0, OpLearnConstants.VariableConstant.MAX_ALL_SIZE)
      : PageRequest.of(page, size);

    Page<Category> categories = StringUtils.hasText(keyword)
      ? repository.search(keyword , pageable)
      : repository.findAllByIsDeletedFalse(pageable);

    return PageResponse.of(
      categories.map(CategoryResponse::from).getContent(),
      (int) categories.getTotalElements()
    );
  }
}
