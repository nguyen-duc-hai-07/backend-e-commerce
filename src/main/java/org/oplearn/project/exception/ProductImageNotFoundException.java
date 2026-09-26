package org.oplearn.project.exception;

import org.oplearn.project.exception.base.NotFoundException;

public class ProductImageNotFoundException extends NotFoundException {
  public ProductImageNotFoundException() {
    super("org.oplearn.project.exception.ProductImageNotFoundException");
  }
}
