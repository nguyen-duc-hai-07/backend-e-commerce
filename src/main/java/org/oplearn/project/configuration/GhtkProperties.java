package org.oplearn.project.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "ghtk.api")
@Getter
@Setter
public class GhtkProperties {
  private String baseUrl;
  private String token;
  private String clientSource;
  private Integer timeoutMs;
  private BigDecimal defaultFee;
  private ShopLocation shop = new ShopLocation();

  @Getter
  @Setter
  public static class ShopLocation {
    private String province;
    private String district;
    private String ward;
    private String address;
  }
}
