package org.oplearn.project.repository;

import org.oplearn.project.entity.Complaint;
import org.oplearn.project.enums.ComplaintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

  Optional<Complaint> findByIdAndIsDeletedFalse(Long id);

  boolean existsByUserIdAndOrderIdAndStatusAndIsDeletedFalse(Long userId, Long orderId, ComplaintStatus status);

  int countByUserIdAndOrderIdAndIsDeletedFalse(Long userId, Long orderId);

  @Modifying
  @Query("UPDATE Complaint c SET c.isDeleted = true WHERE c.id = :id AND c.isDeleted = false AND c.status = org.oplearn.project.enums.ComplaintStatus.PENDING")
  void softDeleteById(@Param("id") Long id);

  @Query("""
        SELECT c
        FROM Complaint c
        WHERE c.userId = :userId
          AND c.isDeleted = false
          AND (:status IS NULL OR c.status = :status)
        ORDER BY c.id DESC
        """)
  Page<Complaint> filterByUserId(
      @Param("userId") Long userId,
      @Param("status") ComplaintStatus status,
      Pageable pageable
  );

  @Query("""
        SELECT c
        FROM Complaint c
        WHERE c.isDeleted = false
          AND (:status IS NULL OR c.status = :status)
        ORDER BY c.id DESC
        """)
  Page<Complaint> filter(
      @Param("status") ComplaintStatus status,
      Pageable pageable
  );
}
