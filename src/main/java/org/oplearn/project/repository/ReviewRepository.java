package org.oplearn.project.repository;

import org.oplearn.project.dto.response.ReviewResponse;
import org.oplearn.project.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
  Optional<Review> findByIdAndIsDeletedFalse(Long id);

  @Modifying
  @Query("update Review r set r.isDeleted = true where r.id = :id AND r.isDeleted = false")
  void softDeleteById(Long id);

  @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productId = :productId AND r.isDeleted = false")
  BigDecimal findAverageRatingByProductId(Long productId);

  @Query("""
      SELECT new org.oplearn.project.dto.response.ReviewResponse(
          r.id,
          r.productId,
          r.userId,
          u.fullName,
          u.avatarUrl,
          r.rating,
          r.comment,
          r.isDeleted,
          r.createdBy,
          r.createdAt,
          r.updatedAt
      )
      FROM Review r
      LEFT JOIN User u ON r.userId = u.id
      WHERE r.isDeleted = false
        AND r.productId = :productId
        AND (:rating IS NULL OR r.rating = :rating)
      ORDER BY r.id DESC
      """)
  Page<ReviewResponse> findByRatingAndProductId(
      @Param("rating") Integer rating,
      @Param("productId") Long productId,
      Pageable pageable
  );

  boolean existsByProductIdAndUserIdAndIsDeletedFalse(Long productId, Long userId);

  int countByProductIdAndIsDeletedFalse(Long productId);
}
