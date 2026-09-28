package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.DistrictResponse;
import org.oplearn.project.dto.response.ProvinceResponse;
import org.oplearn.project.dto.response.WardResponse;
import org.oplearn.project.repository.DistrictRepository;
import org.oplearn.project.repository.ProvinceRepository;
import org.oplearn.project.repository.WardRepository;
import org.oplearn.project.service.AdministrativeUnitService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdministrativeUnitServiceImpl implements AdministrativeUnitService {

  private final ProvinceRepository provinceRepository;
  private final DistrictRepository districtRepository;
  private final WardRepository wardRepository;

  @Override
  public List<ProvinceResponse> listProvinces() {
    log.info("(listProvinces)");
    return provinceRepository.findAllByOrderByNameAsc().stream()
        .map(ProvinceResponse::from)
        .toList();
  }

  @Override
  public List<DistrictResponse> listDistricts(String provinceCode) {
    log.info("(listDistricts) provinceCode: {}", provinceCode);
    return districtRepository.findByProvinceCodeOrderByNameAsc(provinceCode).stream()
        .map(DistrictResponse::from)
        .toList();
  }

  @Override
  public List<WardResponse> listWards(String districtCode) {
    log.info("(listWards) districtCode: {}", districtCode);
    return wardRepository.findByDistrictCodeOrderByNameAsc(districtCode).stream()
        .map(WardResponse::from)
        .toList();
  }
}
