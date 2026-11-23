package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.MessageRequest;
import org.oplearn.project.dto.response.CursorPageResponse;
import org.oplearn.project.dto.response.MessageResponse;
import org.oplearn.project.entity.Message;
import org.oplearn.project.exception.MessageNotFoundException;
import org.oplearn.project.repository.MessageRepository;
import org.oplearn.project.service.MessService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessServiceImpl implements MessService {

  private final MessageRepository repository;

  @Override
  @Transactional
  public MessageResponse create(MessageRequest request) {
    log.info("(service) create message: {}", request);

    Message message = Message.builder()
      .conversationId(request.getConversationId())
      .senderId(request.getSenderId())
      .content(request.getContent())
      .imageUrl(request.getImageUrl())
      .build();

    Message savedMessage = repository.save(message);

    return MessageResponse.from(savedMessage);
  }

  @Override
  public MessageResponse detail(Long id) {
    log.info("(service) detail message: {}", id);

    return MessageResponse.from(this.findById(id));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(service) delete message: {}", id);

    this.findById(id);

    repository.softDeleteById(id);
  }

  @Override
  public CursorPageResponse<MessageResponse> findByConversationId(Long conversationId, Long cursor, int size) {
    log.info("(service) get messages: conversationId: {}, cursor: {}, size: {}", conversationId, cursor, size);

    int pageSize = Math.min(Math.max(size, 1), 50);
    Pageable pageable = PageRequest.of(0 , pageSize + 1);

    List<MessageResponse> messageResponses = repository.findByConversationId(conversationId , cursor , pageable);

    boolean hasNext = messageResponses.size() > pageSize;
    Long nextCursor = null;

    if (hasNext) {
      messageResponses = messageResponses.subList(0, pageSize);
      nextCursor = messageResponses.get(messageResponses.size() - 1).getId();
    }

    Long total = (cursor == null ) ? repository.countByConversationIdAndIsDeletedFalse(conversationId) : null;

    return CursorPageResponse.of(messageResponses , nextCursor, hasNext, total);
  }

  @Override
  @Transactional
  public void markAsRead(Long conversationId, Long currentUserId) {
    repository.markAsRead(conversationId , currentUserId);
  }

  private Message findById(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(MessageNotFoundException::new);
  }
}
