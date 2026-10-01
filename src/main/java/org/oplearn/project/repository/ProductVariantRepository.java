package org.oplearn.project.repository;

import org.oplearn.project.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

  Optional<ProductVariant> findByIdAndIsDeletedFalse(Long id);

  Optional<ProductVariant> findBySkuAndIsDeletedFalse(String sku);

  boolean existsBySkuAndIsDeletedFalse(String sku);

  boolean existsBySkuAndIdNotAndIsDeletedFalse(String sku, Long id);

  @Modifying
  @Query("UPDATE ProductVariant pv SET pv.isDeleted = true WHERE pv.id = :id AND pv.isDeleted = false")
  void softDeleteById(@Param("id") Long id);

  List<ProductVariant> findAllByProductIdAndIsDeletedFalse(Long productId);
}
