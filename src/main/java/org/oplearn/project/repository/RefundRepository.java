package org.oplearn.project.repository;

import org.oplearn.project.entity.Refund;
import org.oplearn.project.enums.RefundStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
  Optional<Refund> findByIdAndIsDeletedFalse(Long id);

  int countByUserIdAndOrderIdAndIsDeletedFalse(Long userId, Long orderId);

  @Query("""
      SELECT r FROM Refund r
      WHERE (:userId IS NULL OR r.userId = :userId)
        AND (:status IS NULL OR r.status = :status)
        AND r.isDeleted = false
      ORDER BY r.id DESC
      """)
  Page<Refund> filter(
      @Param("userId") Long userId,
      @Param("status") RefundStatus status,
      Pageable pageable
  );
}
