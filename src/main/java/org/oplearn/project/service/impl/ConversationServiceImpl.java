package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ConversationRequest;
import org.oplearn.project.dto.request.ConversationUpdateRequest;
import org.oplearn.project.dto.response.ConversationResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.entity.Conversation;
import org.oplearn.project.enums.ConversationStatus;
import org.oplearn.project.enums.ConversationType;
import org.oplearn.project.exception.ConversationAlreadyExistedException;
import org.oplearn.project.exception.ConversationNotFoundException;
import org.oplearn.project.repository.ConversationRepository;
import org.oplearn.project.service.ConversationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

  private final ConversationRepository repository;

  @Override
  @Transactional
  public ConversationResponse create(ConversationRequest request) {
    log.info("(service) create conversation");

    if (request.getUserId() != null && repository.existsByUserIdAndIsDeletedFalse(request.getUserId())) {
      log.warn("(create) conversation already exists for user: {}", request.getUserId());
      throw new ConversationAlreadyExistedException();
    }

    Conversation conversation = Conversation.builder()
      .type(request.getType() != null ? request.getType() : ConversationType.DIRECT)
      .userId(request.getUserId())
      .status(ConversationStatus.OPEN)
      .build();

    Conversation saved = repository.save(conversation);
    return ConversationResponse.from(saved);
  }

  @Override
  @Transactional
  public ConversationResponse update(ConversationUpdateRequest request, Long id) {
    log.info("(service) update conversation: {}", id);

    Conversation conversation = this.findById(id);

    if (request.getTitle() != null) {
      conversation.setTitle(request.getTitle());
    }

    if (request.getStatus() != null) {
      conversation.setStatus(request.getStatus());
    }

    Conversation updatedConversation = repository.save(conversation);
    return ConversationResponse.from(updatedConversation);
  }

  @Override
  public ConversationResponse detail(Long id) {
    log.info("(service) detail conversation: {}", id);
    return ConversationResponse.from(this.findById(id));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(service) delete conversation: {}", id);

    this.findById(id);
    repository.softDeleteById(id);
  }

  @Override
  public PageResponse<ConversationResponse> search(String keyword, int size, int page) {
    log.info("(service) search conversations: keyword: {}, size: {}, page: {}", keyword, size, page);

    Pageable pageable = PageRequest.of(page, size);
    Page<Conversation> conversations = repository.search(keyword, pageable);

    return PageResponse.of(conversations.map(ConversationResponse::from));
  }

  @Override
  public PageResponse<ConversationResponse> filter(int size, int page) {
    log.info("(service) filter conversations: size: {}, page: {}", size, page);
    return this.search(null, size, page);
  }

  @Override
  public List<ConversationResponse> findAllTypeBroadcast() {
    log.info("(service) find all broadcast conversations");

    return repository.findAllBroadcast().stream()
      .map(ConversationResponse::from)
      .toList();
  }

  @Override
  @Transactional
  public void updateLastMessageAt(Long id, Instant lastMessageAt) {
    log.info("(service) update lastMessageAt: id: {}, lastMessageAt: {}", id, lastMessageAt);
    repository.updateLastMessageAt(id, lastMessageAt != null ? lastMessageAt : Instant.now());
  }

  private Conversation findById(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(ConversationNotFoundException::new);
  }
}
