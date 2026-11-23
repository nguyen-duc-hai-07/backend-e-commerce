package org.oplearn.project.service;

import org.oplearn.project.dto.request.ReturnRqRequest;
import org.oplearn.project.dto.request.ReturnRqUpdateRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ReturnRequestResponse;
import org.oplearn.project.enums.ReturnRequestStatus;
import org.oplearn.project.enums.ReturnResolutionType;

public interface ReturnRqService {
  ReturnRequestResponse create(ReturnRqRequest request);

  ReturnRequestResponse detail(Long id);

  void adminUpdate(ReturnRqUpdateRequest request, Long id);

  void delete(Long id);

  PageResponse<ReturnRequestResponse> filter(ReturnRequestStatus status, ReturnResolutionType type, int size, int page);

  PageResponse<ReturnRequestResponse> filterByUserId(Long userId, int size, int page);

  boolean hasPendingRequest(Long orderId);

  boolean hasApprovedRequest(Long orderId);

  int countByOrderId(Long orderId);
}
