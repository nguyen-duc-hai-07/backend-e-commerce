package org.oplearn.project.exception;

import org.oplearn.project.exception.base.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
  public ProductNotFoundException() {
    super("org.oplearn.project.exception.ProductNotFoundException");
  }
}
