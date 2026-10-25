package org.oplearn.project.facade;

import org.oplearn.project.dto.request.ConversationRequest;
import org.oplearn.project.dto.response.ConversationResponse;

public interface ConversationFacadeService {

  ConversationResponse create(ConversationRequest request);

  ConversationResponse detail(Long id);
}
