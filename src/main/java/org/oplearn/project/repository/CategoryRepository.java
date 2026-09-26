package org.oplearn.project.repository;

import org.oplearn.project.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByIdAndIsDeletedFalse(Long id);

  Optional<Category> findBySlugAndIsDeletedFalse(String slug);

  boolean existsByNameAndIsDeletedFalse(String name);

  boolean existsByNameAndIdNotAndIsDeletedFalse(String name, Long id);

  boolean existsBySlugAndIsDeletedFalse(String slug);

  boolean existsBySlugAndIdNotAndIsDeletedFalse(String slug, Long id);

  Page<Category> findAllByIsDeletedFalse(Pageable pageable);

  @Modifying
  @Query("UPDATE Category c SET c.isDeleted = true WHERE c.id = :id AND c.isDeleted = false")
  void softDeleteById(@Param("id") Long id);

  @Query("""
    select c from Category c
    where c.isDeleted = false
    and (lower(c.name) like lower(concat('%', :keyword, '%'))
         or lower(c.slug) like lower(concat('%', :keyword, '%')))
    """)
  Page<Category> search(@Param("keyword") String keyword, Pageable pageable);
}
