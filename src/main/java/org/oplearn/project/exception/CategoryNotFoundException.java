package org.oplearn.project.exception;

import org.oplearn.project.exception.base.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
  public CategoryNotFoundException() {
    super("org.oplearn.project.exception.CategoryNotFoundException");
  }
}
