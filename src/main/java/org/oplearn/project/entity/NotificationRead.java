package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.oplearn.project.entity.base.BaseEntity;

import java.time.Instant;

@Entity
@Table(
    name = "notification_reads",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_notification_reads_notification_user",
            columnNames = {"notification_id", "user_id"}
        )
    }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NotificationRead extends BaseEntity {

  @Column(name = "notification_id", nullable = false)
  private Long notificationId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "read_at", nullable = false)
  @Builder.Default
  private Instant readAt = Instant.now();
}
