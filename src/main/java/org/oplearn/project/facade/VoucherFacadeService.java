package org.oplearn.project.facade;

import org.oplearn.project.dto.request.VoucherRequest;
import org.oplearn.project.dto.response.VoucherResponse;

public interface VoucherFacadeService {
  VoucherResponse create(VoucherRequest request);
}
