package org.oplearn.project.exception;

import org.oplearn.project.exception.base.NotFoundException;

public class UserVoucherNotFoundException extends NotFoundException {
  public UserVoucherNotFoundException() {
    super("org.oplearn.project.exception.UserVoucherNotFoundException");
  }
}
