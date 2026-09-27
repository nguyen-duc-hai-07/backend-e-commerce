package org.oplearn.project.exception;

import org.oplearn.project.exception.base.ConflictException;

public class ReviewAlreadyExistedException extends ConflictException {
  public ReviewAlreadyExistedException() {
    super("org.oplearn.project.exception.ReviewAlreadyExistedException");
  }
}
