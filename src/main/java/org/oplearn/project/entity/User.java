package org.oplearn.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;
import org.oplearn.project.enums.AuthProvider;
import org.oplearn.project.enums.UserRole;
import org.oplearn.project.enums.UserStatus;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "password")
public class User extends BaseEntity {
  @Column(name = "full_name")
  private String fullName;

  @Column(name = "avatar_url", length = 500)
  private String avatarUrl;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", length = 50, nullable = false)
  @Builder.Default
  private UserRole role = UserRole.USER;

  @Enumerated(EnumType.STRING)
  @Column(name = "auth_provider", length = 50, nullable = false)
  @Builder.Default
  private AuthProvider provider = AuthProvider.LOCAL;

  @Column(name = "provider_id")
  private String providerId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 50, nullable = false)
  @Builder.Default
  private UserStatus status = UserStatus.ACTIVE;
}
