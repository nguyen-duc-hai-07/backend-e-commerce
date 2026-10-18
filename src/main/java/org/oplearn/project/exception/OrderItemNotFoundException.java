package org.oplearn.project.exception;

import org.oplearn.project.exception.base.NotFoundException;

public class OrderItemNotFoundException extends NotFoundException {
  public OrderItemNotFoundException() {
    super("org.oplearn.project.exception.OrderItemNotFoundException");
  }
}
