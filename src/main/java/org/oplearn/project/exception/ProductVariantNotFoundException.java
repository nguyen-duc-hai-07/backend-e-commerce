package org.oplearn.project.exception;

import org.oplearn.project.exception.base.NotFoundException;

public class ProductVariantNotFoundException extends NotFoundException {
  public ProductVariantNotFoundException() {
    super("org.oplearn.project.exception.ProductVariantNotFoundException");
  }
}
