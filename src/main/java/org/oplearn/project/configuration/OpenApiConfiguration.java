package org.oplearn.project.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

  private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("Spring Boot Base Project API")
        .description("REST API Starter with PostgreSQL, Redis, S3/MinIO, JWT Security")
        .version("v1.0.0")
        .contact(new Contact().name("Backend Team"))
        .license(new License().name("Apache 2.0")))
      .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
      .components(new Components()
        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
          .name(SECURITY_SCHEME_NAME)
          .type(SecurityScheme.Type.HTTP)
          .scheme("bearer")
          .bearerFormat("JWT")
          .description("Nhập JWT token theo cú pháp: Bearer <access_token>")));
  }
}
