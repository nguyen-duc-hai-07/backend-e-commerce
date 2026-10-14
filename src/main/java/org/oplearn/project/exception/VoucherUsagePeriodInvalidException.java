package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class VoucherUsagePeriodInvalidException extends BadRequestException {
  public VoucherUsagePeriodInvalidException() {
    super("org.oplearn.project.exception.VoucherUsagePeriodInvalidException");
  }
}
