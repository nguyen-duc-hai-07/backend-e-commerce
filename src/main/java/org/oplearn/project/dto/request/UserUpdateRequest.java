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
import org.oplearn.project.enums.UserRole;
import org.oplearn.project.enums.UserStatus;

/**
 * Payload cập nhật user (admin sửa thông tin / nâng vai trò hoặc user tự sửa profile).
 * Password là tuỳ chọn: chỉ đổi mật khẩu khi có nhập giá trị.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "password")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserUpdateRequest {
  private String fullName;

  private String username;

  private String phoneNumber;

  @Email(message = "user.email.invalid")
  private String email;

  private UserRole role;

  private UserStatus status;

  @Size(min = 8, message = "user.password.min_length")
  private String password;

  private String avatarUrl;
}
