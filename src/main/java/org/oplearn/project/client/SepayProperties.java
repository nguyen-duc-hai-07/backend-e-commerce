package org.oplearn.project.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sepay")
@Data
public class SepayProperties {
  private String accountNumber;
  private String bank;
  private String accountName;
  private String apiKey;
  private String webhookToken;
  private String qrTemplate;
  private String orderPrefix;
}
