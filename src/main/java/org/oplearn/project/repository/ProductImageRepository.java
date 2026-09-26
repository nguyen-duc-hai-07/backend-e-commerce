package org.oplearn.project.repository;

import org.oplearn.project.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

  Optional<ProductImage> findByIdAndIsDeletedFalse(Long id);

  @Modifying
  @Query("UPDATE ProductImage pi SET pi.isDeleted = true WHERE pi.id = :id AND pi.isDeleted = false")
  void softDeleteById(@Param("id") Long id);

  List<ProductImage> findAllByProductIdAndIsDeletedFalseOrderByDisplayOrderAsc(Long productId);
}
