package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class RefundNotAllowedForCodException extends BadRequestException {
  public RefundNotAllowedForCodException() {
    super("org.oplearn.project.exception.RefundNotAllowedForCodException");
  }
}
