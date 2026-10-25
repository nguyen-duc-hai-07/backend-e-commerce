package org.oplearn.project.repository;

import org.oplearn.project.dto.response.OrderVoucherResponse;
import org.oplearn.project.entity.UserVoucher;
import org.oplearn.project.enums.VoucherTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long> {
  boolean existsByUserIdAndVoucherId(Long userId, Long voucherId);

  Optional<UserVoucher> findByUserIdAndVoucherIdAndIsDeletedFalse(Long userId, Long voucherId);

  Optional<UserVoucher> findByIdAndIsDeletedFalse(Long id);

  List<UserVoucher> findByOrderIdAndIsDeletedFalse(Long orderId);

  @Query("""
      SELECT new org.oplearn.project.dto.response.OrderVoucherResponse(
          uv.id,
          v.id,
          v.code,
          v.campaignCode,
          v.target,
          v.discountType,
          v.discountValue,
          v.maxDiscountAmount,
          v.minOrderValue,
          v.useStartDate,
          v.useEndDate
      )
      FROM UserVoucher uv
      JOIN Voucher v ON uv.voucherId = v.id
      WHERE uv.userId = :userId
        AND uv.isDeleted = false
        AND v.isDeleted = false
        AND uv.status = org.oplearn.project.enums.UserVoucherStatus.UNUSED
        AND v.isActive = true
        AND :now BETWEEN v.useStartDate AND v.useEndDate
        AND v.target = :target
      ORDER BY v.useEndDate ASC
  """)
  List<OrderVoucherResponse> findAvailableVouchersForOrder(
      @Param("userId") Long userId,
      @Param("target") VoucherTarget target,
      @Param("now") Instant now
  );
}
