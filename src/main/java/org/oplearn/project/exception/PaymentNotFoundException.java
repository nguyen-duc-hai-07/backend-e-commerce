package org.oplearn.project.exception;

import org.oplearn.project.exception.base.NotFoundException;

public class PaymentNotFoundException extends NotFoundException {
  public PaymentNotFoundException() {
    super("org.oplearn.project.exception.PaymentNotFoundException");
  }
}
