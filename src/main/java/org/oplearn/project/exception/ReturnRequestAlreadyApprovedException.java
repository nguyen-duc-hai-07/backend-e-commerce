package org.oplearn.project.exception;

import org.oplearn.project.exception.base.ConflictException;

public class ReturnRequestAlreadyApprovedException extends ConflictException {
  public ReturnRequestAlreadyApprovedException() {
    super("org.oplearn.project.exception.ReturnRequestAlreadyApprovedException");
  }
}
