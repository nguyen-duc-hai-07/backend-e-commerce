package org.oplearn.project.repository;

import org.oplearn.project.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {
  List<Ward> findByDistrictCodeOrderByNameAsc(String districtCode);

  @Query("SELECT w.name FROM Ward w WHERE w.code = :code")
  Optional<String> findNameByCode(@Param("code") String code);
}
