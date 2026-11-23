package org.oplearn.project.repository;

import org.oplearn.project.entity.Conversation;
import org.oplearn.project.enums.ConversationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

  Optional<Conversation> findByIdAndIsDeletedFalse(Long id);

  boolean existsByUserIdAndIsDeletedFalse(Long userId);

  Optional<Conversation> findByUserIdAndIsDeletedFalse(Long userId);

  @Query("""
      SELECT c
      FROM Conversation c
      WHERE c.type = org.oplearn.project.enums.ConversationType.BROADCAST
        AND c.isDeleted = false
      ORDER BY c.lastMessageAt DESC NULLS LAST, c.id DESC
      """)
  List<Conversation> findAllBroadcast();

  @Modifying
  @Query("UPDATE Conversation c SET c.isDeleted = true WHERE c.id = :id AND c.isDeleted = false")
  void softDeleteById(@Param("id") Long id);

  @Query(
      value = """
          SELECT c
          FROM Conversation c
          WHERE c.isDeleted = false
            AND (:status IS NULL OR c.status = :status)
            AND (:keyword IS NULL OR :keyword = '' OR (c.title IS NOT NULL AND LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))))
          ORDER BY c.lastMessageAt DESC NULLS LAST, c.id DESC
          """,
      countQuery = """
          SELECT COUNT(c)
          FROM Conversation c
          WHERE c.isDeleted = false
            AND (:status IS NULL OR c.status = :status)
            AND (:keyword IS NULL OR :keyword = '' OR (c.title IS NOT NULL AND LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))))
          """
  )
  Page<Conversation> search(
      @Param("keyword") String keyword,
      @Param("status") ConversationStatus status,
      Pageable pageable
  );

  @Modifying
  @Query("UPDATE Conversation c SET c.status = :status WHERE c.id = :id AND c.isDeleted = false")
  void updateStatus(@Param("id") Long id, @Param("status") ConversationStatus status);

  @Modifying
  @Query("UPDATE Conversation c SET c.lastMessageAt = :lastMessageAt WHERE c.id = :id AND c.isDeleted = false")
  void updateLastMessageAt(@Param("id") Long id, @Param("lastMessageAt") Instant lastMessageAt);
}
