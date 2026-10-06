package org.oplearn.project.exception;

import org.oplearn.project.exception.base.NotFoundException;

public class PaymentMethodNotFoundException extends NotFoundException {
  public PaymentMethodNotFoundException() {
    super("org.oplearn.project.exception.PaymentMethodNotFoundException");
  }
}
