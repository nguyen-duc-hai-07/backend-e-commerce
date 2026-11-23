package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class InvalidComplaintStatusException extends BadRequestException {

  public InvalidComplaintStatusException() {
    super("org.oplearn.project.exception.InvalidComplaintStatusException");
  }
}
