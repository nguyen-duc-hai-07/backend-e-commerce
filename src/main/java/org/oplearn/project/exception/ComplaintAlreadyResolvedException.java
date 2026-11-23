package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class ComplaintAlreadyResolvedException extends BadRequestException {

  public ComplaintAlreadyResolvedException() {
    super("org.oplearn.project.exception.ComplaintAlreadyResolvedException");
  }
}
