package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class ReturnRequestImageLimitExceededException extends BadRequestException {
  public ReturnRequestImageLimitExceededException() {
    super("org.oplearn.project.exception.ReturnRequestImageLimitExceededException");
  }
}
