package org.oplearn.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.BaseEntity;

import java.text.Normalizer;

@Entity
@Table(name = "categories")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {
  @Column(name = "name")
  private String name;

  @Column(name = "slug")
  private String slug;

  public Category(String name) {
    this.name = name;
    this.slug = toSlug(name);
  }

  public static String toSlug(String name) {
    if (name == null || name.isBlank()) {
      return "";
    }
    String noSign = name.replace('đ', 'd').replace('Đ', 'd');
    noSign = Normalizer.normalize(noSign, Normalizer.Form.NFD);
    noSign = noSign.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

    String slug = noSign.trim().toLowerCase();
    slug = slug.replaceAll("[^a-z0-9\\s-]", "");
    slug = slug.replaceAll("\\s+", "-");
    slug = slug.replaceAll("-+", "-");

    return slug.replaceAll("^-+|-+$", "");
  }
}
