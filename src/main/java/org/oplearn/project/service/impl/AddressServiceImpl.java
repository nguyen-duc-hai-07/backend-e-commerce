package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  public Address create(Address address) {
    log.info("create address");

    int addressCount = repository.countByUserIdAndIsDeletedFalse(address.getUserId());
    if (addressCount >= MAX_ADDRESSES_PER_USER) {
      log.error("user {} has reached max address", address.getUserId());
      throw new AddressLimitExceededException();
    }

    return repository.save(address);
  }

  @Override
  @Transactional
  public Address update(Address address, Long id) {
    log.info("update address with id: {}", id);

    Address existingAddress = repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(AddressNotFoundException::new);
    if (existingAddress != null) {
      existingAddress.setUserId(address.getUserId());
    }
    existingAddress.setProvince(address.getProvince());
    existingAddress.setRecipientName(address.getRecipientName());
    existingAddress.setPhoneNumber(address.getPhoneNumber());
    existingAddress.setDistrict(address.getDistrict());
    existingAddress.setWard(address.getWard());
    existingAddress.setStreetAddress(address.getStreetAddress());

    return repository.save(existingAddress);
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

    repository.deleteById(id);
  }

  @Override
  @Transactional
  public void setDefault(Long id) {
    repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(AddressNotFoundException::new);

    repository.setOtherDefaultAsFalse(id);

    repository.setDefaultAsTrue(id);
  }

  public PageResponse<AddressResponse> list(Long userId) {
    return PageResponse.of(
      repository.findByUserIdAndIsDeletedFalse(userId).stream()
        .map(AddressResponse::from).toList(),
      repository.countByUserIdAndIsDeletedFalse(userId)
    );
  }
}
