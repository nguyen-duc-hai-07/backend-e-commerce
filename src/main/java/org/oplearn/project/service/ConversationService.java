package org.oplearn.project.service;

import org.oplearn.project.dto.request.ConversationRequest;
import org.oplearn.project.dto.request.ConversationUpdateRequest;
import org.oplearn.project.dto.response.ConversationResponse;
import org.oplearn.project.dto.response.PageResponse;

import java.time.Instant;
import java.util.List;

public interface ConversationService {

  ConversationResponse create(ConversationRequest request);

  ConversationResponse update(ConversationUpdateRequest request, Long id);

  ConversationResponse detail(Long id);

  void delete(Long id);

  PageResponse<ConversationResponse> search(String keyword, int size, int page);

  PageResponse<ConversationResponse> filter(int size, int page);

  List<ConversationResponse> findAllTypeBroadcast();

  void updateLastMessageAt(Long id, Instant lastMessageAt);
}
