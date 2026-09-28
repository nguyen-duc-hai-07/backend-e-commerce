package org.oplearn.project.exception;

import org.oplearn.project.exception.base.ConflictException;

public class ReviewReplyAlreadyExistedException extends ConflictException {
  public ReviewReplyAlreadyExistedException() {
    super("org.oplearn.project.exception.ReviewReplyAlreadyExistedException");
  }
}
