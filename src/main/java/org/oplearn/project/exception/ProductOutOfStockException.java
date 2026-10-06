package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class ProductOutOfStockException extends BadRequestException {
  public ProductOutOfStockException() {
    super("org.oplearn.project.exception.ProductOutOfStockException");
  }
}
