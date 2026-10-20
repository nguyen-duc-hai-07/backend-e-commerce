package org.oplearn.project.exception;

import org.oplearn.project.exception.base.ConflictException;

public class VoucherAlreadyExistedException extends ConflictException {
  public VoucherAlreadyExistedException() {
    super("org.oplearn.project.exception.VoucherAlreadyExistedException");
  }
}
