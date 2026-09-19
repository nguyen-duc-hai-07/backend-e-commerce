package org.oplearn.project.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.oplearn.project.constants.OpLearnConstants.KafkaConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(name = "spring.kafka.topic-creation.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaTopicConfiguration {

  @Bean
  public NewTopic authRegistrationOtpTopic() {
    return TopicBuilder.name(KafkaConstant.TOPIC_AUTH_REGISTRATION_OTP)
        .partitions(3)
        .replicas(1)
        .build();
  }

  @Bean
  public NewTopic authForgotPasswordOtpTopic() {
    return TopicBuilder.name(KafkaConstant.TOPIC_AUTH_FORGOT_PASSWORD_OTP)
        .partitions(3)
        .replicas(1)
        .build();
  }
}
