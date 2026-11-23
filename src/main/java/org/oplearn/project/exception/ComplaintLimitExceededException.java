package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class ComplaintLimitExceededException extends BadRequestException {

  public ComplaintLimitExceededException() {
    super("org.oplearn.project.exception.ComplaintLimitExceededException");
  }
}
