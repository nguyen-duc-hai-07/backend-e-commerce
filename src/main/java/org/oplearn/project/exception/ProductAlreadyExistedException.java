package org.oplearn.project.exception;

import org.oplearn.project.exception.base.ConflictException;

public class ProductAlreadyExistedException extends ConflictException {
  public ProductAlreadyExistedException() {
    super("org.oplearn.project.exception.ProductAlreadyExistedException");
  }
}
