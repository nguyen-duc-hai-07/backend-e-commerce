package org.oplearn.project.service;

import org.oplearn.project.dto.response.DistrictResponse;
import org.oplearn.project.dto.response.ProvinceResponse;
import org.oplearn.project.dto.response.WardResponse;

import java.util.List;

public interface AdministrativeUnitService {
  List<ProvinceResponse> listProvinces();
  List<DistrictResponse> listDistricts(String provinceCode);
  List<WardResponse> listWards(String districtCode);

  void checkProvinceExist(String provinceCode);
  void checkDistrictExist(String districtCode);
  void checkWardExist(String wardCode);
}
