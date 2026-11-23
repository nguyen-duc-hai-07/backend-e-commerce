package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class ReturnPeriodExpiredException extends BadRequestException {
  public ReturnPeriodExpiredException() {
    super("org.oplearn.project.exception.ReturnPeriodExpiredException");
  }
}
