package org.oplearn.project.exception;

import org.oplearn.project.exception.base.ConflictException;

public class PaymentMethodAlreadyExistedException extends ConflictException {
  public PaymentMethodAlreadyExistedException() {
    super("org.oplearn.project.exception.PaymentMethodAlreadyExistedException");
  }
}
