package org.oplearn.project.repository;

import org.oplearn.project.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findByUserIdAndIsDeletedFalse(Long userId);

  Optional<Address> findByIdAndIsDeletedFalse(Long id);

  int countByUserIdAndIsDeletedFalse(Long userId);

  @Modifying
  @Query("UPDATE Address a SET a.isDeleted = true WHERE a.id = :id AND a.isDeleted = false")
  void softDeleteById(@Param("id") Long id);

  @Modifying
  @Query("UPDATE Address a SET a.isDefault = true WHERE a.id = :id AND a.isDeleted = false")
  void setDefaultAsTrue(@Param("id") Long id);

  @Modifying
  @Query("UPDATE Address a SET a.isDefault = false WHERE a.id <> :id AND a.isDeleted = false")
  void setOtherDefaultAsFalse(@Param("id") Long id);

  @Query("""
       SELECT CASE WHEN COUNT(a) > :number THEN true ELSE false END
       FROM Address a
       WHERE a.isDeleted = false
       AND a.isDefault = true
       AND a.userId = :userId
       """)
  boolean hasDefaultAddress(@Param("number") int number , @Param("userId") Long userId);
}
