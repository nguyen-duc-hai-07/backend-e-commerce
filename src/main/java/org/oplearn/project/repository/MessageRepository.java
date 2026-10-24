package org.oplearn.project.repository;

import org.oplearn.project.dto.response.MessageResponse;
import org.oplearn.project.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  Optional<Message> findByIdAndIsDeletedFalse(Long id);

  @Modifying
  @Query("UPDATE Message m SET m.isDeleted = true WHERE m.id = :id AND m.isDeleted = false")
  void softDeleteById(@Param("id") Long id);

  @Query("""
    SELECT new org.oplearn.project.dto.response.MessageResponse(m, u.fullName)
    FROM Message m
    LEFT JOIN User u ON m.senderId = u.id
    WHERE m.conversationId = :conversationId
      AND m.isDeleted = false
      AND (:cursor IS NULL OR m.id < :cursor)
    ORDER BY m.id DESC
    """)
  List<MessageResponse> findByConversationId(
    @Param("conversationId") Long conversationId,
    @Param("cursor") Long cursor,
    Pageable pageable
  );

  Long countByConversationIdAndIsDeletedFalse(Long conversationId);

  @Modifying
  @Query("""
    UPDATE Message m
    SET m.isRead = true
    WHERE m.conversationId = :conversationId
      AND m.senderId != :currentUserId
      AND m.isRead = false
      AND m.isDeleted = false
    """)
  void markAsRead(
    @Param("conversationId") Long conversationId,
    @Param("currentUserId") Long currentUserId
  );
}
