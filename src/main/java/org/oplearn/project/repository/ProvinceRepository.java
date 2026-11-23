package org.oplearn.project.repository;

import org.oplearn.project.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {
  List<Province> findAllByOrderByNameAsc();

  @Query("SELECT p.name FROM Province p WHERE p.code = :code")
  Optional<String> findNameByCode(@Param("code") String code);
}
