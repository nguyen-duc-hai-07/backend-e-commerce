package org.oplearn.project.facade;

import org.oplearn.project.dto.request.AddressRequest;
import org.oplearn.project.dto.response.AddressResponse;
import org.oplearn.project.dto.response.PageResponse;

public interface AddressFacadeService {
  AddressResponse create(AddressRequest request);

  AddressResponse update(AddressRequest request, Long id);

  PageResponse<AddressResponse> list(Long userId);
}
