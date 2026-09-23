package org.oplearn.project.exception;

import org.oplearn.project.exception.base.NotFoundException;

public class AddressNotFoundException extends NotFoundException {
  public AddressNotFoundException() {
    super("org.oplearn.project.exception.AddressNotFoundException");
  }
}
