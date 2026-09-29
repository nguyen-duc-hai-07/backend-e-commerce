package org.oplearn.project.exception;

import org.oplearn.project.exception.base.ConflictException;

public class CategoryAlreadyExistedException extends ConflictException {
  public CategoryAlreadyExistedException() {
    super("org.oplearn.project.exception.CategoryAlreadyExistedException");
  }
}
