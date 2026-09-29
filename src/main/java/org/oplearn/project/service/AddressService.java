package org.oplearn.project.service;

import org.oplearn.project.dto.response.AddressResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.entity.Address;
import org.springframework.data.domain.PageRequest;

public interface AddressService {
  Address create(Address address);

  Address update(Address address , Long id);

  AddressResponse detail(Long id);

  void delete(Long id);

  void setDefault(Long id);

  PageResponse<AddressResponse> list(Long userId);
}
