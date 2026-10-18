package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class VoucherTargetMismatchException extends BadRequestException {
  public VoucherTargetMismatchException() {
    super("org.oplearn.project.exception.VoucherTargetMismatchException");
  }
}
