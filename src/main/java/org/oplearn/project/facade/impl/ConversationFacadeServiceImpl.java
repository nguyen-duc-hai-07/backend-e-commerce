package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ConversationRequest;
import org.oplearn.project.dto.response.ConversationResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.enums.ConversationType;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.ConversationFacadeService;
import org.oplearn.project.security.SecurityHelper;
import org.oplearn.project.service.ConversationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationFacadeServiceImpl implements ConversationFacadeService {

  private final ConversationService conversationService;
  private final SecurityHelper securityHelper;

  @Override
  @Transactional
  public ConversationResponse create(ConversationRequest request) {
    log.info("(facade) create conversation");

    User currentUser = securityHelper.currentUser();

    if (request.getType() == ConversationType.BROADCAST) {
      if (!securityHelper.isAdmin()) {
        log.warn("(create) user {} not authorized to create broadcast conversation", currentUser.getId());
        throw new UserUnauthorizedException();
      }
      request.setUserId(null);
    } else {
      request.setUserId(currentUser.getId());
    }

    return conversationService.create(request);
  }

  @Override
  public ConversationResponse detail(Long id) {
    User currentUser = securityHelper.currentUser();

    ConversationResponse response = conversationService.detail(id);

    if(response.getType() != ConversationType.BROADCAST) {
      if (!securityHelper.isAdmin() && !currentUser.getId().equals(response.getUserId())) {
        log.warn("(detail) user not authorized");
        throw new UserUnauthorizedException();
      }
    }

    return response;
  }
}
