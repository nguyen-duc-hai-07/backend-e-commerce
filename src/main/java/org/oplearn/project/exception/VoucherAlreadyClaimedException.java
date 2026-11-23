package org.oplearn.project.exception;

import org.oplearn.project.exception.base.ConflictException;

public class VoucherAlreadyClaimedException extends ConflictException {
  public VoucherAlreadyClaimedException() {
    super("org.oplearn.project.exception.VoucherAlreadyClaimedException");
  }
}
