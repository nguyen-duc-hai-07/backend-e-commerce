package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.AddressRequest;
import org.oplearn.project.dto.response.AddressResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.entity.Address;
import org.oplearn.project.exception.AddressLimitExceededException;
import org.oplearn.project.exception.AddressNotFoundException;
import org.oplearn.project.repository.AddressRepository;
import org.oplearn.project.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.MAX_ADDRESSES_PER_USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
  private final AddressRepository repository;

  @Override
  @Transactional
  public AddressResponse create(AddressRequest request) {
    log.info("create address");

    int addressCount = repository.countByUserIdAndIsDeletedFalse(request.getUserId());
    if (addressCount >= MAX_ADDRESSES_PER_USER) {
      log.error("user {} has reached max address", request.getUserId());
      throw new AddressLimitExceededException();
    }

    Address address = Address.builder()
      .userId(request.getUserId())
      .recipientName(request.getRecipientName())
      .phoneNumber(request.getPhoneNumber())
      .provinceCode(request.getProvinceCode())
      .districtCode(request.getDistrictCode())
      .wardCode(request.getWardCode())
      .streetAddress(request.getStreetAddress())
      .isDefault(addressCount == 0)
      .build();

    Address savedAddress = repository.save(address);

    return AddressResponse.from(savedAddress);
  }

  @Override
  @Transactional
  public AddressResponse update(AddressRequest request, Long id) {
    log.info("update address with id: {}", id);

    Address existingAddress = repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(AddressNotFoundException::new);
    setAddressValues(existingAddress, request);
    repository.save(existingAddress);

    return AddressResponse.from(existingAddress);
  }

  private void setAddressValues(Address target, AddressRequest source) {
    target.setProvinceCode(source.getProvinceCode());
    target.setDistrictCode(source.getDistrictCode());
    target.setWardCode(source.getWardCode());
    target.setRecipientName(source.getRecipientName());
    target.setPhoneNumber(source.getPhoneNumber());
    target.setStreetAddress(source.getStreetAddress());
  }

  @Override
  public AddressResponse detail(Long id) {
    return AddressResponse.from(repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(AddressNotFoundException::new));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(AddressNotFoundException::new);

    repository.softDeleteById(id);
  }

  @Override
  @Transactional
  public void setDefault(Long id) {
    repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(AddressNotFoundException::new);

    repository.setOtherDefaultAsFalse(id);

    repository.setDefaultAsTrue(id);
  }

  @Override
  public PageResponse<AddressResponse> list(Long userId) {
    return PageResponse.of(
      repository.findByUserIdAndIsDeletedFalse(userId).stream()
        .map(AddressResponse::from).toList(),
      repository.countByUserIdAndIsDeletedFalse(userId)
    );
  }

  @Override
  public AddressResponse getDefaultAddress(Long userId) {
    log.info("(getDefaultAddress) userId: {}", userId);
    return AddressResponse.from(
      repository.findFirstByUserIdAndIsDefaultTrueAndIsDeletedFalse(userId)
        .orElseThrow(AddressNotFoundException::new)
    );
  }
}
