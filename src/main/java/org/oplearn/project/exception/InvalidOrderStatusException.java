package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class InvalidOrderStatusException extends BadRequestException {
  public InvalidOrderStatusException() {
    super("org.oplearn.project.exception.InvalidOrderStatusException");
  }
}
