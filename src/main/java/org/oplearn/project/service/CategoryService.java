package org.oplearn.project.service;

import org.oplearn.project.dto.request.CategoryRequest;
import org.oplearn.project.dto.response.CategoryResponse;
import org.oplearn.project.dto.response.PageResponse;

public interface CategoryService {

  CategoryResponse create(CategoryRequest request);

  CategoryResponse update(CategoryRequest request, Long id);

  CategoryResponse detail(Long id);

  void delete(Long id);

  CategoryResponse findBySlug(String slug);

  PageResponse<CategoryResponse> list(String keyword , int size, int page , boolean isAll);
}
