package org.oplearn.project.facade;

import org.oplearn.project.dto.request.ComplaintRequest;
import org.oplearn.project.dto.response.ComplaintResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.enums.ComplaintStatus;

public interface ComplaintFacadeService {
  ComplaintResponse create(ComplaintRequest request);

  ComplaintResponse detail(Long id);

  PageResponse<ComplaintResponse> filterByUserId(ComplaintStatus status, int size, int page);

  void delete(Long id);
}
