package org.oplearn.project.facade;

import org.oplearn.project.dto.request.MessageRequest;
import org.oplearn.project.dto.response.CursorPageResponse;
import org.oplearn.project.dto.response.MessageResponse;

public interface MessFacadeService {

  MessageResponse create(MessageRequest request);

  void delete(Long id);

  CursorPageResponse<MessageResponse> findByConversationId(Long conversationId, Long cursor, int size);
}
