package org.oplearn.project.exception;

import org.oplearn.project.exception.base.NotFoundException;

public class CartItemNotFoundException extends NotFoundException {
  public CartItemNotFoundException() {
    super("org.oplearn.project.exception.CartItemNotFoundException");
  }
}
