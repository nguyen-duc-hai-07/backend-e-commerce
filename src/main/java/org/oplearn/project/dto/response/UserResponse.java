package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.oplearn.project.entity.User;
import org.oplearn.project.enums.AuthProvider;
import org.oplearn.project.enums.UserRole;
import org.oplearn.project.enums.UserStatus;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
  private Long id;
  private String fullName;
  private String username;
  private String email;
  private String phoneNumber;
  private String avatarUrl;
  private UserRole role;
  private AuthProvider authProvider;
  private UserStatus status;
  private Instant createdAt;
  private Instant updatedAt;

  public static UserResponse from(User user) {
    if (user == null) {
      return null;
    }
    return UserResponse.builder()
        .id(user.getId())
        .fullName(user.getFullName())
        .avatarUrl(user.getAvatarUrl())
        .username(user.getUsername())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .role(user.getRole())
        .authProvider(user.getProvider())
        .status(user.getStatus())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }
}
