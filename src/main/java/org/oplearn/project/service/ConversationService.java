package org.oplearn.project.service;

import org.oplearn.project.dto.request.ConversationRequest;
import org.oplearn.project.dto.request.ConversationUpdateRequest;
import org.oplearn.project.dto.response.ConversationResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.enums.ConversationStatus;

import java.time.Instant;
import java.util.List;

public interface ConversationService {

  ConversationResponse create(ConversationRequest request);

  ConversationResponse update(ConversationUpdateRequest request, Long id);

  ConversationResponse updateStatus(Long id, ConversationStatus status);

  ConversationResponse detail(Long id);

  void delete(Long id);

  PageResponse<ConversationResponse> search(String keyword, ConversationStatus status, int size, int page);

  default PageResponse<ConversationResponse> search(String keyword, int size, int page) {
    return search(keyword, null, size, page);
  }

  PageResponse<ConversationResponse> filter(ConversationStatus status, int size, int page);

  default PageResponse<ConversationResponse> filter(int size, int page) {
    return filter(null, size, page);
  }

  List<ConversationResponse> findAllTypeBroadcast();

  void updateLastMessageAt(Long id, Instant lastMessageAt);
}
