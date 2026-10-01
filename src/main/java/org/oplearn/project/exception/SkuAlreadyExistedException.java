package org.oplearn.project.exception;

import org.oplearn.project.exception.base.ConflictException;

public class SkuAlreadyExistedException extends ConflictException {
  public SkuAlreadyExistedException() {
    super("org.oplearn.project.exception.SkuAlreadyExistedException");
  }
}
