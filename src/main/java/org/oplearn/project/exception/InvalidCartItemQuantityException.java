package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class InvalidCartItemQuantityException extends BadRequestException {
  public InvalidCartItemQuantityException() {
    super("org.oplearn.project.exception.InvalidCartItemQuantityException");
  }
}
