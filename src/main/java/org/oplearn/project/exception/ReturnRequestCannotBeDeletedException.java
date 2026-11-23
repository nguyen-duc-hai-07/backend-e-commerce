package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class ReturnRequestCannotBeDeletedException extends BadRequestException {
  public ReturnRequestCannotBeDeletedException() {
    super("org.oplearn.project.exception.ReturnRequestCannotBeDeletedException");
  }
}
