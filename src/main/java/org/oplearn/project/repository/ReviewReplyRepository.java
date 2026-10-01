package org.oplearn.project.repository;

import org.oplearn.project.entity.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {
  Optional<ReviewReply> findByIdAndIsDeletedFalse(Long id);

  @Modifying
  @Query("update ReviewReply rr set rr.isDeleted = true where rr.id = :id and rr.isDeleted = false")
  void softDeleteById(Long id);

  boolean existsByReviewIdAndIsDeletedFalse(Long reviewId);

  ReviewReply findByReviewIdAndIsDeletedFalse(Long reviewId);
}
