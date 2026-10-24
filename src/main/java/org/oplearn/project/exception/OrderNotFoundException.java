package org.oplearn.project.exception;

import org.oplearn.project.exception.base.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
  public OrderNotFoundException() {
    super("org.oplearn.project.exception.OrderNotFoundException");
  }
}
