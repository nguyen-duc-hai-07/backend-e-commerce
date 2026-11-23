package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class VoucherClaimLimitExceededException extends BadRequestException {
  public VoucherClaimLimitExceededException() {
    super("org.oplearn.project.exception.VoucherClaimLimitExceededException");
  }
}
