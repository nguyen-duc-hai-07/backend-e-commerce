package org.oplearn.project.exception;

import org.oplearn.project.exception.base.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {
  public ReviewNotFoundException() {
    super("org.oplearn.project.exception.ReviewNotFoundException");
  }
}
