package org.oplearn.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.DistrictResponse;
import org.oplearn.project.dto.response.ProvinceResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.WardResponse;
import org.oplearn.project.service.AdministrativeUnitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/locations")
public class LocationController {

  private final AdministrativeUnitService administrativeUnitService;

  @GetMapping("/provinces")
  public ResponseGeneral<List<ProvinceResponse>> listProvinces() {
    log.info("(listProvinces)");
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, administrativeUnitService.listProvinces());
  }

  @GetMapping("/districts")
  public ResponseGeneral<List<DistrictResponse>> listDistricts(@RequestParam("provinceCode") String provinceCode) {
    log.info("(listDistricts) provinceCode: {}", provinceCode);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, administrativeUnitService.listDistricts(provinceCode));
  }

  @GetMapping("/wards")
  public ResponseGeneral<List<WardResponse>> listWards(@RequestParam("districtCode") String districtCode) {
    log.info("(listWards) districtCode: {}", districtCode);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, administrativeUnitService.listWards(districtCode));
  }
}
