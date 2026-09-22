package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.oplearn.project.enums.AuthProvider;
import org.oplearn.project.enums.UserRole;
import org.oplearn.project.enums.UserStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "password")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRequest {
  @NotBlank(message = "user.full_name.not_blank")
  private String fullName;

  @NotBlank(message = "user.username.not_blank")
  private String username;

  @NotBlank(message = "user.password.not_blank")
  @Size(min = 8, message = "user.password.min_length")
  private String password;

  private String phoneNumber;

  @Email(message = "user.email.invalid")
  private String email;

  private UserRole role;

  private UserStatus status;

  private AuthProvider authProvider;

  private String avatarUrl;
}
