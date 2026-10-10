package org.oplearn.project.repository;

import org.oplearn.project.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {
  List<District> findByProvinceCodeOrderByNameAsc(String provinceCode);

  @Query("SELECT d.name FROM District d WHERE d.code = :code")
  Optional<String> findNameByCode(@Param("code") String code);
}
