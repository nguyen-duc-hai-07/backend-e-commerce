package org.oplearn.project.repository;

import org.oplearn.project.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
  Optional<Voucher> findByIdAndIsDeletedFalseAndIsActiveTrue(Long id);

  Optional<Voucher> findByCodeAndIsDeletedFalseAndIsActiveTrue(String code);

  boolean existsByCode(String code);

  @Modifying
  @Query("UPDATE Voucher v SET v.isActive = false WHERE v.id = :id AND v.isActive = true AND v.isDeleted = false")
  void setInactiveById(Long id);

  @Query("""
    SELECT v
    FROM Voucher v
    WHERE v.isDeleted = false
        AND v.isActive = true
        AND :now BETWEEN v.claimStartDate AND v.claimEndDate
        ORDER BY v.claimEndDate ASC
    """)
  Page<Voucher> findClaimableVouchers(@Param("now") Instant now , Pageable pageable);
}
