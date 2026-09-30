package org.oplearn.project.repository;

import org.oplearn.project.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

  Optional<PaymentMethod> findByIdAndIsDeletedFalse(Long id);

  boolean existsByIdAndIsDeletedFalse(Long id);

  boolean existsByCodeAndIsDeletedFalse(String code);

  boolean existsByCodeAndIdNotAndIsDeletedFalse(String code, Long id);

  List<PaymentMethod> findAllByIsDeletedFalse();

  @Modifying
  @Query("UPDATE PaymentMethod pm SET pm.isDeleted = true WHERE pm.id = :id AND pm.isDeleted = false")
  void softDeleteById(@Param("id") Long id);
}
