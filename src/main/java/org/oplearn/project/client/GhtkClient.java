package org.oplearn.project.client;

import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.configuration.GhtkProperties;
import org.oplearn.project.dto.ghtk.GhtkCancelResponse;
import org.oplearn.project.dto.ghtk.GhtkOrderRequest;
import org.oplearn.project.dto.ghtk.GhtkOrderResponse;
import org.oplearn.project.dto.ghtk.GhtkTrackingResponse;
import org.oplearn.project.dto.response.GhtkFeeResponse;
import org.oplearn.project.exception.base.BadRequestException;
import org.springframework.http.MediaType;
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

  /**
   * Đăng ký đơn giao hàng sang GHTK
   * Endpoint: POST /services/shipment/order/?ver=1.5
   */
  public GhtkOrderResponse createOrder(GhtkOrderRequest request) {
    String orderId = (request != null && request.getOrder() != null) ? request.getOrder().getId() : "UNKNOWN";
    log.info("(GhtkClient) createOrder start for partner_order_id: {}", orderId);

    try {
      URI uri = UriComponentsBuilder.fromHttpUrl(properties.getBaseUrl())
          .path("/services/shipment/order/")
          .queryParam("ver", "1.5")
          .build()
          .encode()
          .toUri();

      log.info("(GhtkClient) createOrder URI: {}", uri);

      GhtkOrderResponse response = restClient.post()
          .uri(uri)
          .header("Token", properties.getToken())
          .header("X-Client-Source", properties.getClientSource())
          .contentType(MediaType.APPLICATION_JSON)
          .body(request)
          .retrieve()
          .body(GhtkOrderResponse.class);

      log.info("(GhtkClient) createOrder response for {}: success={}, message={}, label={}",
          orderId,
          response != null ? response.getSuccess() : null,
          response != null ? response.getMessage() : null,
          (response != null && response.getOrder() != null) ? response.getOrder().getLabel() : null
      );

      return response;

    } catch (Exception ex) {
      log.error("(GhtkClient) Exception calling GHTK create order for order {}: {}", orderId, ex.getMessage(), ex);
      return GhtkOrderResponse.builder()
          .success(false)
          .message("Connection error to GHTK: " + ex.getMessage())
          .build();
    }
  }

  public byte[] printLabel(String trackingCode, String pageSize) {
    String size = (pageSize != null && !pageSize.isBlank()) ? pageSize : "A6";
    log.info("(GhtkClient) printLabel for trackingCode: {}, size: {}", trackingCode, size);

    try {
      URI uri = UriComponentsBuilder.fromHttpUrl(properties.getBaseUrl())
          .path("/services/label/" + trackingCode)
          .queryParam("original", size)
          .build()
          .encode()
          .toUri();

      log.info("(GhtkClient) printLabel URI: {}", uri);

      return restClient.get()
          .uri(uri)
          .header("Token", properties.getToken())
          .header("X-Client-Source", properties.getClientSource())
          .accept(MediaType.APPLICATION_PDF)
          .retrieve()
          .body(byte[].class);

    } catch (Exception ex) {
      log.error("(GhtkClient) Exception getting label for {}: {}", trackingCode, ex.getMessage(), ex);
      throw new BadRequestException("Không thể lấy tem in từ GHTK: " + ex.getMessage());
    }
  }

  public GhtkTrackingResponse getTracking(String trackingCode) {
    log.info("(GhtkClient) getTracking for trackingCode: {}", trackingCode);

    try {
      URI uri = UriComponentsBuilder.fromHttpUrl(properties.getBaseUrl())
          .path("/services/shipment/v2/" + trackingCode)
          .build()
          .encode()
          .toUri();

      log.info("(GhtkClient) getTracking URI: {}", uri);

      return restClient.get()
          .uri(uri)
          .header("Token", properties.getToken())
          .header("X-Client-Source", properties.getClientSource())
          .retrieve()
          .body(GhtkTrackingResponse.class);

    } catch (Exception ex) {
      log.error("(GhtkClient) Exception getting tracking for {}: {}", trackingCode, ex.getMessage(), ex);
      throw new BadRequestException("Không thể tra cứu hành trình từ GHTK: " + ex.getMessage());
    }
  }

  /**
   * Hủy đơn hàng vận chuyển bên GHTK
   * Endpoint: POST /services/shipment/cancel/{trackingCode}
   *
   * @param trackingCode Mã vận đơn GHTK (VD: S21.HN1.10098412)
   * @return GhtkCancelResponse Kết quả hủy từ GHTK
   */
  public GhtkCancelResponse cancelOrder(String trackingCode) {
    log.info("(GhtkClient) cancelOrder for trackingCode: {}", trackingCode);

    try {
      URI uri = UriComponentsBuilder.fromHttpUrl(properties.getBaseUrl())
          .path("/services/shipment/cancel/" + trackingCode)
          .build()
          .encode()
          .toUri();

      log.info("(GhtkClient) cancelOrder URI: {}", uri);

      GhtkCancelResponse response = restClient.post()
          .uri(uri)
          .header("Token", properties.getToken())
          .header("X-Client-Source", properties.getClientSource())
          .retrieve()
          .body(GhtkCancelResponse.class);

      log.info("(GhtkClient) cancelOrder result for {}: success={}, message={}",
          trackingCode,
          response != null ? response.getSuccess() : null,
          response != null ? response.getMessage() : null
      );

      return response;

    } catch (Exception ex) {
      log.error("(GhtkClient) Exception canceling order {}: {}", trackingCode, ex.getMessage(), ex);
      return GhtkCancelResponse.builder()
          .success(false)
          .message("Lỗi kết nối tới GHTK khi hủy đơn: " + ex.getMessage())
          .build();
    }
  }
}
