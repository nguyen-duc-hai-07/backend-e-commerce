package org.oplearn.project.repository;

import org.oplearn.project.dto.response.OrderResponse;
import org.oplearn.project.entity.Order;
import org.oplearn.project.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  Optional<Order> findByIdAndIsDeletedFalse(Long id);

  Optional<Order> findByOrderCodeAndIsDeletedFalse(String orderCode);

  @Modifying
  @Query("UPDATE Order o SET o.isDeleted = true WHERE o.id = :id AND o.isDeleted = false")
  void softDeleteById(Long id);

  @Modifying
  @Query("UPDATE Order o SET o.status = :status WHERE o.id = :id AND o.isDeleted = false")
  void updateStatusById(Long id, String status);

  @Query("""
    SELECT new org.oplearn.project.dto.response.OrderResponse(
        o.id,
        o.orderCode,
        o.userId,
        o.addressId,
        a.recipientName,
        a.phoneNumber,
        o.shippingFee,
        o.discountAmount,
        o.totalAmount,
        o.status,
        o.note,
        o.originalOrderId,
        o.isDeleted,
        o.createdBy,
        o.createdAt,
        o.updatedAt
    )
    FROM Order o
    LEFT JOIN Address a ON o.addressId = a.id AND a.isDeleted = false
    WHERE o.id = :id AND o.isDeleted = false
    """)
  Optional<OrderResponse> findByIdAndReturnResponse(@Param("id") Long id);

  @Query("""
    SELECT new org.oplearn.project.dto.response.OrderResponse(
        o.id,
        o.orderCode,
        o.userId,
        o.addressId,
        a.recipientName,
        a.phoneNumber,
        o.shippingFee,
        o.discountAmount,
        o.totalAmount,
        o.status,
        o.note,
        o.originalOrderId,
        o.isDeleted,
        o.createdBy,
        o.createdAt,
        o.updatedAt
    )
    FROM Order o
    LEFT JOIN Address a ON o.addressId = a.id AND a.isDeleted = false
    WHERE o.userId = :userId
      AND (:status IS NULL OR o.status = :status)
      AND o.isDeleted = false
    ORDER BY o.id DESC
    """)
  Page<OrderResponse> findByStatusAndUserId(
      @Param("status") OrderStatus status,
      @Param("userId") Long userId,
      Pageable pageable
  );

  @Query("""
      SELECT COUNT(o) > 0
      FROM Order o
      JOIN OrderItem oi ON o.id = oi.orderId
      JOIN ProductVariant pv ON oi.variantId = pv.id
      WHERE o.userId = :userId
        AND pv.productId = :productId
        AND o.status = org.oplearn.project.enums.OrderStatus.COMPLETED
        AND o.isDeleted = false
    """)
  boolean hasUserPurchasedProductAndCompleted(@Param("userId") Long userId, @Param("productId") Long productId);

  @Query("""
        SELECT o FROM Order o
        WHERE o.status = :status
          AND o.updatedAt <= :cutoffTime
          AND o.isDeleted = false
    """)
  List<Order> findByStatusAndUpdatedAtBefore(
    @Param("status") OrderStatus status,
    @Param("cutoffTime") Instant cutoffTime
  );

  @Query("""
          SELECT o FROM Order o
          WHERE o.status = :status
            AND o.createdAt <= :cutoffTime
            AND o.isDeleted = false
      """)
  List<Order> findExpiredPendingOrders(
    @Param("status") OrderStatus status,
    @Param("cutoffTime") Instant cutoffTime
  );

  @Query(value = """
      SELECT COUNT(*) AS totalOrders,
             COUNT(*) FILTER (WHERE o.status = 'COMPLETED') AS completedOrders,
             COUNT(*) FILTER (WHERE o.status = 'CANCELLED') AS cancelledOrders,
             COUNT(*) FILTER (WHERE o.status = 'REFUNDED')  AS refundedOrders,
             COUNT(*) FILTER (WHERE o.status NOT IN ('COMPLETED', 'CANCELLED', 'REFUNDED')) AS inProgressOrders,
             COALESCE(SUM(o.total_amount - o.shipping_fee) FILTER (WHERE o.status = 'COMPLETED'), 0) AS totalRevenue
      FROM orders o
      WHERE o.original_order_id IS NULL
          AND o.is_deleted = false
          AND o.created_at >= :fromDate AND o.created_at < :toDate
    """, nativeQuery = true)
  OrderSummaryProjection getOrderSummaryStatistics(
    @Param("fromDate") Instant fromDate,
    @Param("toDate") Instant toDate
  );

  interface OrderSummaryProjection {
    Long getTotalOrders();
    Long getCompletedOrders();
    Long getCancelledOrders();
    Long getRefundedOrders();
    Long getInProgressOrders();
    BigDecimal getTotalRevenue();
  }

  interface OrderTimelineProjection {
    LocalDate getPeriod();
    BigDecimal getRevenue();
    Long getOrderCount();
  }

  @Query( value = """
    SELECT CAST(m.period AS date) AS period,
           COALESCE(SUM(o.total_amount - o.shipping_fee) , 0) AS revenue,
           COUNT(o.id) AS orderCount
    FROM generate_series(
           date_trunc('month', timezone('Asia/Ho_Chi_Minh', CAST(:fromDate AS timestamptz))),
           date_trunc('month', timezone('Asia/Ho_Chi_Minh', CAST(:toDate AS timestamptz))),
           interval '1 month') AS m(period)
    LEFT JOIN orders o
        ON o.created_at >= (m.period AT TIME ZONE 'Asia/Ho_Chi_Minh')
        AND o.created_at < ((m.period + interval '1 month') AT TIME ZONE 'Asia/Ho_Chi_Minh')
        AND o.status = 'COMPLETED'
        AND o.original_order_id IS NULL
            AND o.is_deleted = false
      GROUP BY m.period
      ORDER BY m.period
  """, nativeQuery = true)
  List<OrderTimelineProjection> getMonthlyRevenueStatistics(
    @Param("fromDate") Instant fromDate,
    @Param("toDate") Instant toDate
  );

  @Query( value = """
    SELECT CAST(m.period AS date) AS period,
           COALESCE(SUM(o.total_amount - o.shipping_fee) , 0) AS revenue,
           COUNT(o.id) AS orderCount
    FROM generate_series(
           date_trunc('year', timezone('Asia/Ho_Chi_Minh', CAST(:fromDate AS timestamptz))),
           date_trunc('year', timezone('Asia/Ho_Chi_Minh', CAST(:toDate AS timestamptz))),
           interval '1 year') AS m(period)
    LEFT JOIN orders o
        ON o.created_at >= (m.period AT TIME ZONE 'Asia/Ho_Chi_Minh')
        AND o.created_at < ((m.period + interval '1 year') AT TIME ZONE 'Asia/Ho_Chi_Minh')
        AND o.status = 'COMPLETED'
        AND o.original_order_id IS NULL
            AND o.is_deleted = false
      GROUP BY m.period
      ORDER BY m.period
  """, nativeQuery = true)
  List<OrderTimelineProjection> getYearlyRevenueStatistics(
    @Param("fromDate") Instant fromDate,
    @Param("toDate") Instant toDate
  );
}
