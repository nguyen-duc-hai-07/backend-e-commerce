package org.oplearn.project.repository;

import org.oplearn.project.entity.ReturnRequest;
import org.oplearn.project.enums.ReturnRequestStatus;
import org.oplearn.project.enums.ReturnResolutionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReturnRqRepository extends JpaRepository<ReturnRequest, Long> {
  Optional<ReturnRequest> findByIdAndIsDeletedFalse(Long id);

  boolean existsByOrderIdAndStatusAndIsDeletedFalse(Long orderId, ReturnRequestStatus status);

  int countByOrderIdAndIsDeletedFalse(Long orderId);

  @Modifying
  @Query("UPDATE ReturnRequest r SET r.isDeleted = true WHERE r.id = :id AND r.isDeleted = false AND r.status = org.oplearn.project.enums.ReturnRequestStatus.PENDING")
  void softDeleteById(@Param("id") Long id);

  @Query("""
        SELECT rq
        FROM ReturnRequest rq
        WHERE rq.isDeleted = false
          AND (:status IS NULL OR rq.status = :status)
          AND (:type IS NULL OR rq.resolutionType = :type)
        ORDER BY rq.id DESC
        """)
  Page<ReturnRequest> filter(
    @Param("status") ReturnRequestStatus status,
    @Param("type") ReturnResolutionType type,
    Pageable pageable
  );

  @Query("""
        SELECT rq
        FROM ReturnRequest rq
        JOIN Order o ON rq.orderId = o.id
        WHERE o.userId = :userId
          AND rq.isDeleted = false
          AND o.isDeleted = false
        ORDER BY rq.id DESC
        """)
  Page<ReturnRequest> filterByUserId(@Param("userId") Long userId, Pageable pageable);
}
