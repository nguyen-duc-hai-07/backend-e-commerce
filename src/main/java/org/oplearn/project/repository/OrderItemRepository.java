package org.oplearn.project.repository;

import org.oplearn.project.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  Optional<OrderItem> findByIdAndIsDeletedFalse(Long id);

  List<OrderItem> findAllByOrderIdAndIsDeletedFalse(Long orderId);

  List<OrderItem> findAllByOrderIdInAndIsDeletedFalse(List<Long> orderIds);

  List<OrderItem> findAllByOrderId(Long orderId);

  @Modifying
  @Query("UPDATE OrderItem oi SET oi.isDeleted = true WHERE oi.id = :id AND oi.isDeleted = false")
  void softDeleteById(Long id);
}
