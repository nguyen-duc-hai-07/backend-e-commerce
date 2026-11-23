package org.oplearn.project.exception;

import org.oplearn.project.exception.base.ConflictException;

public class ReturnRequestAlreadyExistedException extends ConflictException {
  public ReturnRequestAlreadyExistedException() {
    super("org.oplearn.project.exception.ReturnRequestAlreadyExistedException");
  }
}
