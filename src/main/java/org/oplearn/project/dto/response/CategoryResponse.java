package org.oplearn.project.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.Category;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CategoryResponse {
  private Long id;
  private String name;
  private String slug;
  private Boolean isDeleted;
  private String createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public static CategoryResponse from(Category category) {
    if (category == null) {
      return null;
    }
    return CategoryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .slug(category.getSlug())
        .isDeleted(category.getIsDeleted())
        .createdBy(category.getCreatedBy())
        .createdAt(category.getCreatedAt())
        .updatedAt(category.getUpdatedAt())
        .build();
  }
}
