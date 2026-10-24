package org.oplearn.project.exception;

import org.oplearn.project.exception.base.ConflictException;

public class ConversationAlreadyExistedException extends ConflictException {

  public ConversationAlreadyExistedException() {
    super("org.oplearn.project.exception.ConversationAlreadyExistedException");
  }
}
