package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class VoucherMinOrderValueNotReachedException extends BadRequestException {
  public VoucherMinOrderValueNotReachedException() {
    super("org.oplearn.project.exception.VoucherMinOrderValueNotReachedException");
  }
}
