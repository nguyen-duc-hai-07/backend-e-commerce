package org.oplearn.project.repository;

import org.oplearn.project.entity.ReturnRequestImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnRqImageRepository extends JpaRepository<ReturnRequestImage, Long> {
  List<ReturnRequestImage> findByReturnRequestIdAndIsDeletedFalseOrderByDisplayOrderAsc(Long returnRequestId);

  List<ReturnRequestImage> findByReturnRequestIdInAndIsDeletedFalse(List<Long> returnRequestIds);
}
