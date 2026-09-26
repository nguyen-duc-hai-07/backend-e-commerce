package org.oplearn.project.repository;

import org.oplearn.project.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<Product> findByIdAndIsDeletedFalse(Long id);

  @Modifying
  @Query("UPDATE Product p SET p.isDeleted = true WHERE p.id = :id AND p.isDeleted = false")
  void softDeleteById(@Param("id") Long id);

  Page<Product> findAllByIsDeletedFalse(Pageable pageable);

  @Query("""
    SELECT p FROM Product p
    WHERE p.isDeleted = false
    AND p.categoryId = :categoryId
    """)
  Page<Product> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

  @Query(value = """
        SELECT p.id AS "id",
               p.name AS "name",
               p.description AS "description",
               p.category_id AS "categoryId",
               p.thumbnail_url AS "thumbnailUrl",
               p.average_rating AS "averageRating",
               p.review_count AS "reviewCount",
               p.min_price AS "minPrice",
               p.sold_count AS "soldCount"
        FROM products p
        WHERE p.is_deleted = false
          AND (CAST(:categoryId AS bigint) IS NULL OR p.category_id = CAST(:categoryId AS bigint))
          AND (
            CAST(:keyword AS text) IS NULL
            OR p.search_vec @@ websearch_to_tsquery('simple', f_unaccent(CAST(:keyword AS text)))
            OR f_unaccent(lower(p.name)) LIKE '%' || f_unaccent(lower(CAST(:keyword AS text))) || '%'
          )
        ORDER BY
          (CAST(:keyword AS text) IS NOT NULL AND f_unaccent(lower(p.name)) = f_unaccent(lower(CAST(:keyword AS text)))) DESC,
          (CAST(:keyword AS text) IS NOT NULL AND f_unaccent(lower(p.name)) LIKE f_unaccent(lower(CAST(:keyword AS text))) || '%') DESC,
          (CAST(:keyword AS text) IS NOT NULL AND f_unaccent(lower(p.name)) LIKE '%' || f_unaccent(lower(CAST(:keyword AS text))) || '%') DESC,
          (CAST(:keyword AS text) IS NOT NULL AND p.search_vec @@ phraseto_tsquery('simple', f_unaccent(CAST(:keyword AS text)))) DESC,
          ts_rank(p.search_vec, websearch_to_tsquery('simple', f_unaccent(COALESCE(CAST(:keyword AS text), '')))) DESC,
          p.id DESC
    """,
    countQuery = """
        SELECT count(*)
        FROM products p
        WHERE p.is_deleted = false
          AND (CAST(:categoryId AS bigint) IS NULL OR p.category_id = CAST(:categoryId AS bigint))
          AND (
            CAST(:keyword AS text) IS NULL
            OR p.search_vec @@ websearch_to_tsquery('simple', f_unaccent(CAST(:keyword AS text)))
            OR f_unaccent(lower(p.name)) LIKE '%' || f_unaccent(lower(CAST(:keyword AS text))) || '%'
          )
    """,
    nativeQuery = true)
  Page<ProductSearchRow> search(
      @Param("keyword") String keyword,
      @Param("categoryId") Long categoryId,
      Pageable pageable);

  interface ProductSearchRow {
    Long getId();
    String getName();
    String getDescription();
    Long getCategoryId();
    String getThumbnailUrl();
    BigDecimal getAverageRating();
    Integer getReviewCount();
    BigDecimal getMinPrice();
    Integer getSoldCount();
  }

  @Query(value = "SELECT id FROM products TABLESAMPLE SYSTEM (10) WHERE is_deleted = false AND created_at >= :since  LIMIT :n", nativeQuery = true)
  List<Long> findFastRandomIds(@Param("since") Instant since, @Param("n") int n);

  @Query(value = "SELECT id FROM products WHERE is_deleted = false AND created_at >= :since  ORDER BY random() LIMIT :n", nativeQuery = true)
  List<Long> findRandomIds(@Param("since") Instant since, @Param("n") int n);

  @Query("""
    SELECT p FROM Product p
    WHERE p.id IN :ids
    """)
  List<Product> findByIds(@Param("ids") List<Long> ids);

  @Modifying
  @Query("UPDATE Product p SET p.minPrice = :minPrice WHERE p.id = :id")
  void updateMinPrice(@Param("id") Long id, @Param("minPrice") BigDecimal minPrice);

  @Modifying
  @Query("UPDATE Product p SET p.soldCount = p.soldCount + :quantity WHERE p.id = :id")
  void increaseSoldCount(@Param("id") Long id, @Param("quantity") int quantity);
}
