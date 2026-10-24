package org.oplearn.project.service;

import org.oplearn.project.dto.request.MessageRequest;
import org.oplearn.project.dto.response.CursorPageResponse;
import org.oplearn.project.dto.response.MessageResponse;

public interface MessService {

  MessageResponse create(MessageRequest request);

  MessageResponse detail(Long id);

  void delete(Long id);

  CursorPageResponse<MessageResponse> findByConversationId(Long conversationId, Long cursor, int size);

  void markAsRead(Long conversationId , Long currentUserId);
}
