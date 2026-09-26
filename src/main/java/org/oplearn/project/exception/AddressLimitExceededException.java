package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class AddressLimitExceededException extends BadRequestException {
  public AddressLimitExceededException() {
    super("org.oplearn.project.exception.AddressLimitExceededException");
  }
}
