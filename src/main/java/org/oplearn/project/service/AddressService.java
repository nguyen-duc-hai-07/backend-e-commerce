package org.oplearn.project.service;

import org.oplearn.project.dto.request.AddressRequest;
import org.oplearn.project.dto.response.AddressResponse;
import org.oplearn.project.dto.response.PageResponse;

public interface AddressService {
  AddressResponse create(AddressRequest request);

  AddressResponse update(AddressRequest request , Long id);

  AddressResponse detail(Long id);

  void delete(Long id);

  void setDefault(Long id);

  PageResponse<AddressResponse> list(Long userId);

  AddressResponse getDefaultAddress(Long userId);
}
