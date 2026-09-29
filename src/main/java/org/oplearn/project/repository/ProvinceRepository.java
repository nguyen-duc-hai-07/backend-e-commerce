package org.oplearn.project.repository;

import org.oplearn.project.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {
  List<Province> findAllByOrderByNameAsc();
}
