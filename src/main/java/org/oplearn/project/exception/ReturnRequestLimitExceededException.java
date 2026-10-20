package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class ReturnRequestLimitExceededException extends BadRequestException {
  public ReturnRequestLimitExceededException() {
    super("org.oplearn.project.exception.ReturnRequestLimitExceededException");
  }
}
