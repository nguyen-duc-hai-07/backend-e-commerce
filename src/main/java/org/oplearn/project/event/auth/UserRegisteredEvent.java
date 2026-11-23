package org.oplearn.project.event.auth;

import org.oplearn.project.entity.User;

public record UserRegisteredEvent(
    Long userId,
    String email,
    String fullName,
    String username
) {
  public static UserRegisteredEvent from(User user) {
    return new UserRegisteredEvent(
        user.getId(),
        user.getEmail(),
        user.getFullName(),
        user.getUsername()
    );
  }
}
