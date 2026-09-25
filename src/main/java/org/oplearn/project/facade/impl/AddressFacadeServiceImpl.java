package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.AddressRequest;
import org.oplearn.project.dto.response.AddressResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.entity.Address;
import org.oplearn.project.entity.User;
import org.oplearn.project.facade.AddressFacadeService;
import org.oplearn.project.service.AddressService;
import org.oplearn.project.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressFacadeServiceImpl implements AddressFacadeService {
  private final AddressService addressService;
  private final UserService userService;
  @Override
  public AddressResponse create(AddressRequest request) {
    log.info("(facade) create address");

    User user = userService.getAvailableUserAndThrow(request.getUserId());

    Address address = Address.builder()
      .userId(user.getId())
      .recipientName(request.getRecipientName())
      .phoneNumber(request.getPhoneNumber())
      .province(request.getProvince())
      .district(request.getDistrict())
      .ward(request.getWard())
      .streetAddress(request.getStreetAddress())
      .build();

    Address savedAddress = addressService.create(address);

    return AddressResponse.from(savedAddress);
  }

  @Override
  public AddressResponse update(AddressRequest request, Long id) {
    log.info("(facade) update address");

    User user = userService.getAvailableUserAndThrow(request.getUserId());

    Address address = Address.builder()
      .userId(user.getId())
      .recipientName(request.getRecipientName())
      .phoneNumber(request.getPhoneNumber())
      .province(request.getProvince())
      .district(request.getDistrict())
      .ward(request.getWard())
      .streetAddress(request.getStreetAddress())
      .build();

    Address updatedAddress = addressService.update(address, id);

    return AddressResponse.from(updatedAddress);
  }

  @Override
  public PageResponse<AddressResponse> list(Long userId) {
    log.info("(facade) list address");

    User user = userService.getAvailableUserAndThrow(userId);

    return addressService.list(user.getId());
  }
}
