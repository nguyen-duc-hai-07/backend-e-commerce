package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.MessageRequest;
import org.oplearn.project.dto.response.ConversationResponse;
import org.oplearn.project.dto.response.CursorPageResponse;
import org.oplearn.project.dto.response.MessageResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.enums.ConversationType;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.MessFacadeService;
import org.oplearn.project.security.SecurityHelper;
import org.oplearn.project.service.ConversationService;
import org.oplearn.project.service.MessService;
import org.oplearn.project.service.UserService;
import org.oplearn.project.websocket.EchoWebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessFacadeServiceImpl implements MessFacadeService {

  private final MessService messService;
  private final UserService userService;
  private final ConversationService conversationService;
  private final EchoWebSocketHandler echoWebSocketHandler;
  private final SecurityHelper securityHelper;

  @Override
  @Transactional
  public MessageResponse create(MessageRequest request) {
    log.info("(facade) create message: {}", request);

    User user = securityHelper.currentUser();

    ConversationResponse conversation = conversationService.detail(request.getConversationId());

    if (conversation.getType() == ConversationType.BROADCAST) {
      if (!securityHelper.isAdmin()) {
        log.warn("(create) only admin can broadcast message");
        throw new UserUnauthorizedException();
      }
    } else {
      if (!securityHelper.isAdmin() && !user.getId().equals(conversation.getUserId())) {
        log.warn("(create) user not authorized");
        throw new UserUnauthorizedException();
      }
    }

    request.setSenderId(user.getId());
    request.setConversationId(conversation.getId());

    MessageResponse response = messService.create(request);

    response.setFullName(user.getFullName());
    conversationService.updateLastMessageAt(conversation.getId(), response.getCreatedAt());

    Map<String, Object> messagePayload = Map.of("message", response);

    if (conversation.getType() == ConversationType.BROADCAST) {
      echoWebSocketHandler.broadcast("RECEIVE_MESSAGE", messagePayload);
    } else {
      if (user.getId().equals(conversation.getUserId())) {
        List<Long> adminIds = userService.findAllAdminIds();
        for (Long adminId : adminIds) {
          echoWebSocketHandler.sendToUser(adminId, "RECEIVE_MESSAGE", messagePayload);
        }
      } else {
        echoWebSocketHandler.sendToUser(conversation.getUserId(), "RECEIVE_MESSAGE", messagePayload);
      }
    }

    return response;
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(facade) delete message: {}", id);

    User currentUser = securityHelper.currentUser();

    MessageResponse response = messService.detail(id);

    if (!response.getSenderId().equals(currentUser.getId())) {
      throw new UserUnauthorizedException();
    }

    messService.delete(id);

    ConversationResponse conversation = conversationService.detail(response.getConversationId());
    Map<String, Object> deletePayload = Map.of(
      "conversationId", response.getConversationId(),
      "messageId", id
    );

    if (conversation.getType() == ConversationType.BROADCAST) {
      echoWebSocketHandler.broadcast("DELETE_MESSAGE", deletePayload);
    } else {
      if (currentUser.getId().equals(conversation.getUserId())) {
        List<Long> adminIds = userService.findAllAdminIds();
        for (Long adminId : adminIds) {
          echoWebSocketHandler.sendToUser(adminId, "DELETE_MESSAGE", deletePayload);
        }
      } else {
        echoWebSocketHandler.sendToUser(conversation.getUserId(), "DELETE_MESSAGE", deletePayload);
      }
    }
  }

  @Override
  @Transactional
  public CursorPageResponse<MessageResponse> findByConversationId(Long conversationId, Long cursor, int size) {
    log.info("(facade) findByConversationId: conversationId: {}, cursor: {}, size: {}", conversationId, cursor, size);

    User currentUser = securityHelper.currentUser();

    ConversationResponse conversation = conversationService.detail(conversationId);

    if (conversation.getType() == ConversationType.DIRECT) {
      if (!securityHelper.isAdmin() && !currentUser.getId().equals(conversation.getUserId())) {
        throw new UserUnauthorizedException();
      }
    }

    if (cursor == null && conversation.getType() == ConversationType.DIRECT) {
      messService.markAsRead(conversationId, currentUser.getId());
      Map<String, Object> readPayload = Map.of(
        "conversationId", conversationId,
        "readerId", currentUser.getId()
      );

      if (currentUser.getId().equals(conversation.getUserId())) {
        List<Long> adminIds = userService.findAllAdminIds();
        for (Long adminId : adminIds) {
          echoWebSocketHandler.sendToUser(adminId, "MESSAGES_READ", readPayload);
        }
      } else {
        echoWebSocketHandler.sendToUser(conversation.getUserId(), "MESSAGES_READ", readPayload);
      }
    }

    return messService.findByConversationId(conversationId , cursor , size);
  }
}
