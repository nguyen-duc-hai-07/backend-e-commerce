package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.client.GhtkClient;
import org.oplearn.project.dto.request.CalculateShippingFeeRequest;
import org.oplearn.project.dto.response.GhtkFeeResponse;
import org.oplearn.project.dto.response.ShippingFeeResponse;
import org.oplearn.project.entity.Address;
import org.oplearn.project.entity.ProductVariant;
import org.oplearn.project.exception.AddressNotFoundException;
import org.oplearn.project.exception.ProductVariantNotFoundException;
import org.oplearn.project.exception.ShippingItemsRequiredException;
import org.oplearn.project.repository.AddressRepository;
import org.oplearn.project.repository.DistrictRepository;
import org.oplearn.project.repository.ProductVariantRepository;
import org.oplearn.project.repository.ProvinceRepository;
import org.oplearn.project.repository.WardRepository;
import org.oplearn.project.service.ShippingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

  private final GhtkClient ghtkClient;
  private final AddressRepository addressRepository;
  private final ProductVariantRepository productVariantRepository;
  private final ProvinceRepository provinceRepository;
  private final DistrictRepository districtRepository;
  private final WardRepository wardRepository;

  @Override
  public ShippingFeeResponse calculateFee(CalculateShippingFeeRequest request) {
    log.info("(calculateFee) request: {}", request);

    Address address = addressRepository.findByIdAndIsDeletedFalse(request.getAddressId())
      .orElseThrow(AddressNotFoundException::new);

    String provinceName = provinceRepository.findNameByCode(address.getProvinceCode())
      .orElse(address.getProvinceCode());

    String districtName = districtRepository.findNameByCode(address.getDistrictCode())
      .orElse(address.getDistrictCode());

    String wardName = wardRepository.findNameByCode(address.getWardCode())
      .orElse(address.getWardCode());

    int totalWeight = 0;
    BigDecimal orderValue = BigDecimal.ZERO;

    if (request.getTotalWeight() != null && request.getTotalWeight() > 0) {
      totalWeight = request.getTotalWeight();
    } else if (request.getItems() != null && !request.getItems().isEmpty()) {
      for (CalculateShippingFeeRequest.ShippingItemRequest item : request.getItems()) {
        ProductVariant variant = productVariantRepository.findByIdAndIsDeletedFalse(item.getVariantId())
          .orElseThrow(ProductVariantNotFoundException::new);

        int itemWeight = variant.getWeight() != null ? variant.getWeight() : 200;
        totalWeight += itemWeight * item.getQuantity();

        BigDecimal itemPrice = variant.getPrice() != null ? variant.getPrice() : BigDecimal.ZERO;
        orderValue = orderValue.add(itemPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
      }
    } else {
      log.error("(calculateFee) neither totalWeight nor items provided");
      throw new ShippingItemsRequiredException();
    }

    GhtkFeeResponse ghtkResponse = ghtkClient.calculateFee(
      provinceName,
      districtName,
      wardName,
      address.getStreetAddress(),
      totalWeight,
      orderValue
    );

    boolean isFallback = !Boolean.TRUE.equals(ghtkResponse.getSuccess()) || ghtkResponse.getFee() == null;
    BigDecimal finalFee = (ghtkResponse.getFee() != null && ghtkResponse.getFee().getFee() != null)
      ? ghtkResponse.getFee().getFee()
      : BigDecimal.valueOf(30000);

    return ShippingFeeResponse.builder()
      .shippingFee(finalFee)
      .totalWeight(totalWeight)
      .isFallback(isFallback)
      .build();
  }
}
