package org.oplearn.project.repository;

import org.oplearn.project.dto.response.PaymentResponse;
import org.oplearn.project.entity.Payment;
import org.oplearn.project.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Optional<Payment> findByIdAndIsDeletedFalse(Long id);

  Optional<Payment> findByOrderIdAndIsDeletedFalse(Long orderId);

  boolean existsByTransactionCodeAndIsDeletedFalse(String transactionCode);

  @Query(
    value = """
          SELECT new org.oplearn.project.dto.response.PaymentResponse(
              p.id,
              p.status,
              p.orderId,
              p.amount,
              p.transactionCode,
              pm,
              p.isDeleted,
              p.createdBy,
              p.createdAt,
              p.updatedAt
          )
          FROM Payment p
          JOIN PaymentMethod pm ON pm.id = p.paymentMethodId
          WHERE p.status = :status
          AND p.isDeleted = false
          ORDER BY p.id DESC
          """,
    countQuery = """
          SELECT COUNT(p.id)
          FROM Payment p
          WHERE p.status = :status
            AND p.isDeleted = false
          """
  )
  Page<PaymentResponse> findByStatus(@Param("status") PaymentStatus status, Pageable pageable);

  @Query("""
    SELECT new org.oplearn.project.dto.response.PaymentResponse(
              p.id,
              p.status,
              p.orderId,
              p.amount,
              p.transactionCode,
              pm,
              p.isDeleted,
              p.createdBy,
              p.createdAt,
              p.updatedAt
          )
          FROM Payment p
          JOIN PaymentMethod pm ON pm.id = p.paymentMethodId
          WHERE p.id = :id
            AND p.isDeleted = false
    """)
  Optional<PaymentResponse> findByIdAndReturnResponse(@Param("id") Long id);

  @Query("""
    SELECT new org.oplearn.project.dto.response.PaymentResponse(
              p.id,
              p.status,
              p.orderId,
              p.amount,
              p.transactionCode,
              pm,
              p.isDeleted,
              p.createdBy,
              p.createdAt,
              p.updatedAt
          )
          FROM Payment p
          JOIN PaymentMethod pm ON pm.id = p.paymentMethodId
          WHERE p.orderId = :orderId
          AND p.isDeleted = false
          ORDER BY p.id DESC
    """)
  List<PaymentResponse> findByOrderId(@Param("orderId") Long orderId);

  @Query("""
    SELECT u.id
    FROM Payment p
    JOIN Order o ON o.id = p.orderId
    JOIN User u ON u.id = o.userId
    WHERE p.id = :id
    AND p.isDeleted = false
    """)
  Optional<Long> findUserIdByPaymentId(@Param("id") Long id);
}
