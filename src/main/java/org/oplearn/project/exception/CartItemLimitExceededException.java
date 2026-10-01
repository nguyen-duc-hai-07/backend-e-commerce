package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class CartItemLimitExceededException extends BadRequestException {
  public CartItemLimitExceededException() {
    super("org.oplearn.project.exception.CartItemLimitExceededException");
  }
}
