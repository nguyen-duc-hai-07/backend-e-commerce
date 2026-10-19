package org.oplearn.project.facade;

import org.oplearn.project.dto.request.ReturnRqRequest;
import org.oplearn.project.dto.request.ReturnRqUpdateRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ReturnRequestResponse;

public interface ReturnRqFacadeService {
  ReturnRequestResponse create(ReturnRqRequest request);

  PageResponse<ReturnRequestResponse> filterByUserId(int size, int page);

  void delete(Long id);

  void updateAdmin(ReturnRqUpdateRequest request, Long id);
}
