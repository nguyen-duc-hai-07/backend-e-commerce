package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class ComplaintCannotBeDeletedException extends BadRequestException {

  public ComplaintCannotBeDeletedException() {
    super("org.oplearn.project.exception.ComplaintCannotBeDeletedException");
  }
}
