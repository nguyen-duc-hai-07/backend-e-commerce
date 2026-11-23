package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class VoucherAlreadyUsedException extends BadRequestException {
  public VoucherAlreadyUsedException() {
    super("org.oplearn.project.exception.VoucherAlreadyUsedException");
  }
}
