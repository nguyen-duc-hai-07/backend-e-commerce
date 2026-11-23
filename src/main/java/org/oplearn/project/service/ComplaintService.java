package org.oplearn.project.service;

import org.oplearn.project.dto.request.ComplaintRequest;
import org.oplearn.project.dto.request.ComplaintUpdateRequest;
import org.oplearn.project.dto.response.ComplaintResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.enums.ComplaintStatus;

public interface ComplaintService {

  ComplaintResponse create(ComplaintRequest request);

  ComplaintResponse update(ComplaintUpdateRequest request, Long id);

  ComplaintResponse detail(Long id);

  void delete(Long id);

  PageResponse<ComplaintResponse> filterByUserId(Long userId, ComplaintStatus status, int size, int page);

  PageResponse<ComplaintResponse> filter(ComplaintStatus status, int size, int page);
}
