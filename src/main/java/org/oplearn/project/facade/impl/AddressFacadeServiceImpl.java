package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.AddressRequest;
import org.oplearn.project.dto.response.AddressResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.entity.Address;
import org.oplearn.project.entity.User;
import org.oplearn.project.exception.UserUnauthorizedException;
import org.oplearn.project.facade.AddressFacadeService;
import org.oplearn.project.service.AddressService;
import org.oplearn.project.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressFacadeServiceImpl implements AddressFacadeService {
  private final AddressService addressService;
  private final UserService userService;

  private User currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUsernameOrThrow(authentication.getName());
  }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
  }

  @Override
  public AddressResponse create(AddressRequest request) {
    log.info("(facade) create address");

    User currentUser = currentUser();

    boolean isSelf = request.getUserId() == null || currentUser.getId().equals(request.getUserId());

    if (!isAdmin() && !isSelf) {
      log.warn("(create) user not authorized");
      throw new UserUnauthorizedException();
    }

    Long targetUserId = (isAdmin() && request.getUserId() != null) ? request.getUserId() : currentUser.getId();

    Address address = Address.builder()
      .userId(targetUserId)
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
    log.info("(facade) update address id: {}", id);

    User currentUser = currentUser();
    AddressResponse existing = addressService.detail(id);

    boolean isOwner = currentUser.getId().equals(existing.getUserId());
    if (!isAdmin() && !isOwner) {
      log.warn("(update) user not authorized");
      throw new UserUnauthorizedException();
    }

    Address address = Address.builder()
      .userId(existing.getUserId())
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
  public AddressResponse detail(Long id) {
    log.info("(facade) detail address id: {}", id);

    User currentUser = currentUser();
    AddressResponse address = addressService.detail(id);

    boolean isOwner = currentUser.getId().equals(address.getUserId());
    if (!isAdmin() && !isOwner) {
      log.warn("(detail) user not authorized");
      throw new UserUnauthorizedException();
    }

    return address;
  }

  @Override
  public void delete(Long id) {
    log.info("(facade) delete address id: {}", id);

    User currentUser = currentUser();
    AddressResponse address = addressService.detail(id);

    boolean isOwner = currentUser.getId().equals(address.getUserId());
    if (!isAdmin() && !isOwner) {
      log.warn("(delete) user not authorized");
      throw new UserUnauthorizedException();
    }

    addressService.delete(id);
  }

  @Override
  public void setDefault(Long id) {
    log.info("(facade) setDefault address id: {}", id);

    User currentUser = currentUser();
    AddressResponse address = addressService.detail(id);

    boolean isOwner = currentUser.getId().equals(address.getUserId());
    if (!isAdmin() && !isOwner) {
      log.warn("(setDefault) user not authorized");
      throw new UserUnauthorizedException();
    }

    addressService.setDefault(id);
  }

  @Override
  public PageResponse<AddressResponse> list(Long userId) {
    log.info("(facade) list address for userId: {}", userId);

    User currentUser = currentUser();

    Long targetUserId = (userId != null) ? userId : currentUser.getId();
    boolean isSelf = currentUser.getId().equals(targetUserId);

    if (!isAdmin() && !isSelf) {
      log.warn("(list) user not authorized");
      throw new UserUnauthorizedException();
    }

    return addressService.list(targetUserId);
  }
}
