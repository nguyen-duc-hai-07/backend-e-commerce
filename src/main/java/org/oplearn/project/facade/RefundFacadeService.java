package org.oplearn.project.facade;

import org.oplearn.project.dto.request.RefundFilterRequest;
import org.oplearn.project.dto.request.RefundRequest;
import org.oplearn.project.dto.request.RefundUpdateRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.RefundResponse;
import org.oplearn.project.enums.RefundStatus;

public interface RefundFacadeService {
  RefundResponse create(RefundRequest request);

  RefundResponse detail(Long id);

  RefundResponse update(Long id, RefundUpdateRequest request);

  PageResponse<RefundResponse> filter(RefundFilterRequest request);

  PageResponse<RefundResponse> filterForAdmin(RefundStatus status, Integer page, Integer size);
}
