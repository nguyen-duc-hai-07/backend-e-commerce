package org.oplearn.project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class DateTimeConfiguration {

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
