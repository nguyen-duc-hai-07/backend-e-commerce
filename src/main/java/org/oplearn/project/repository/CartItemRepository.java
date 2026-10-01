package org.oplearn.project.repository;

import org.oplearn.project.dto.response.CartItemResponse;
import org.oplearn.project.entity.CartItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  Optional<CartItem> findByIdAndIsDeletedFalse(Long id);

  @Modifying
  @Query("UPDATE CartItem ci SET ci.isDeleted = true WHERE ci.id = :id AND ci.isDeleted = false")
  void softDeleteById(Long id);

  Long countByUserIdAndIsDeletedFalse(Long userId);

  @Query("""
    SELECT new org.oplearn.project.dto.response.CartItemResponse(
          ci.id,
          ci.userId,
          ci.variantId,
          ci.quantity,
          pv.productId,
          pv.sku,
          pv.attributes,
          pv.price,
          pv.quantity,
          p.name,
          p.thumbnailUrl
    )
    FROM CartItem ci
    JOIN ProductVariant pv ON ci.variantId = pv.id
    JOIN Product p ON pv.productId = p.id
    WHERE ci.userId = :userId
        AND ci.isDeleted = false
        AND pv.isDeleted = false
        AND p.isDeleted = false
    AND (:cursor IS NULL OR ci.id < :cursor)
    ORDER BY ci.id DESC
    """)
  List<CartItemResponse> findByUserId(
    @Param("userId") Long userId,
    @Param("cursor") Long cursor,
    Pageable pageable
  );

  boolean existsByUserIdAndVariantIdAndIsDeletedFalse(Long userId, Long variantId);

  Optional<CartItem> findByUserIdAndVariantIdAndIsDeletedFalse(Long userId, Long variantId);
}
