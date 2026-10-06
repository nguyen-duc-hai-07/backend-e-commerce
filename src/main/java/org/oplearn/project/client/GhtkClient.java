package org.oplearn.project.client;

import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.configuration.GhtkProperties;
import org.oplearn.project.dto.response.GhtkFeeResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@Component
@Slf4j
public class GhtkClient {

  private final GhtkProperties properties;
  private final RestClient restClient;

  public GhtkClient(GhtkProperties properties) {
    this.properties = properties;

    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    int timeout = properties.getTimeoutMs() != null ? properties.getTimeoutMs() : 5000;
    requestFactory.setConnectTimeout(timeout);
    requestFactory.setReadTimeout(timeout);

    this.restClient = RestClient.builder()
        .requestFactory(requestFactory)
        .build();
  }

  public GhtkFeeResponse calculateFee(
      String deliverProvince,
      String deliverDistrict,
      String deliverWard,
      String deliverAddress,
      Integer weightInGrams,
      BigDecimal orderValue
  ) {
    int weight = (weightInGrams != null && weightInGrams > 0) ? weightInGrams : 200;
    int value = orderValue != null ? orderValue.intValue() : 0;

    GhtkProperties.ShopLocation shop = properties.getShop();

    try {
      UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(properties.getBaseUrl())
          .path("/services/shipment/fee")
          .queryParam("pick_province", shop.getProvince())
          .queryParam("pick_district", shop.getDistrict())
          .queryParam("pick_ward", shop.getWard())
          .queryParam("province", deliverProvince)
          .queryParam("district", deliverDistrict)
          .queryParam("weight", weight)
          .queryParam("value", value);

      if (shop.getAddress() != null && !shop.getAddress().isBlank()) {
        uriBuilder.queryParam("pick_address", shop.getAddress());
      }
      if (deliverWard != null && !deliverWard.isBlank()) {
        uriBuilder.queryParam("ward", deliverWard);
      }
      if (deliverAddress != null && !deliverAddress.isBlank()) {
        uriBuilder.queryParam("address", deliverAddress);
      }

      URI uri = uriBuilder.build().encode().toUri();
      log.info("(GhtkClient) calculateFee URI: {}", uri);

      GhtkFeeResponse response = restClient.get()
          .uri(uri)
          .header("Token", properties.getToken())
          .header("X-Client-Source", properties.getClientSource())
          .retrieve()
          .body(GhtkFeeResponse.class);

      if (response != null && Boolean.TRUE.equals(response.getSuccess()) && response.getFee() != null) {
        log.info("(GhtkClient) calculateFee success: fee={}", response.getFee().getFee());
        return response;
      }

      log.warn("(GhtkClient) GHTK returned non-success response: {}", response);
      return createFallbackResponse();

    } catch (Exception ex) {
      log.warn("(GhtkClient) Exception calling GHTK fee API, falling back to default fee: {}", ex.getMessage());
      return createFallbackResponse();
    }
  }

  private GhtkFeeResponse createFallbackResponse() {
    BigDecimal fallbackFee = properties.getDefaultFee() != null
        ? properties.getDefaultFee()
        : BigDecimal.valueOf(30000);

    GhtkFeeResponse.GhtkFeeData feeData = GhtkFeeResponse.GhtkFeeData.builder()
        .name("fallback")
        .fee(fallbackFee)
        .shipFeeOnly(fallbackFee)
        .deliveryType("standard")
        .delivery(true)
        .build();

    return GhtkFeeResponse.builder()
        .success(false)
        .message("Fallback default shipping fee")
        .fee(feeData)
        .build();
  }
}
