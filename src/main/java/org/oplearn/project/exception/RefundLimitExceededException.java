package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class RefundLimitExceededException extends BadRequestException {
  public RefundLimitExceededException() {
    super("org.oplearn.project.exception.RefundLimitExceededException");
  }
}
