package org.oplearn.project.service.ghtk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.client.GhtkClient;
import org.oplearn.project.configuration.GhtkProperties;
import org.oplearn.project.dto.ghtk.GhtkCancelResponse;
import org.oplearn.project.dto.ghtk.GhtkOrderRequest;
import org.oplearn.project.dto.ghtk.GhtkOrderResponse;
import org.oplearn.project.entity.Address;
import org.oplearn.project.entity.Order;
import org.oplearn.project.entity.OrderItem;
import org.oplearn.project.entity.Product;
import org.oplearn.project.entity.ProductVariant;
import org.oplearn.project.enums.OrderStatus;
import org.oplearn.project.exception.AddressNotFoundException;
import org.oplearn.project.exception.InvalidOrderStatusException;
import org.oplearn.project.exception.OrderNotFoundException;
import org.oplearn.project.exception.ProductNotFoundException;
import org.oplearn.project.exception.ProductVariantNotFoundException;
import org.oplearn.project.exception.ShippingItemsRequiredException;
import org.oplearn.project.exception.base.BadRequestException;
import org.oplearn.project.repository.AddressRepository;
import org.oplearn.project.repository.DistrictRepository;
import org.oplearn.project.repository.OrderItemRepository;
import org.oplearn.project.repository.OrderRepository;
import org.oplearn.project.repository.ProductRepository;
import org.oplearn.project.repository.ProductVariantRepository;
import org.oplearn.project.repository.ProvinceRepository;
import org.oplearn.project.repository.WardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GhtkOrderService {

  private final GhtkClient ghtkClient;
  private final GhtkProperties ghtkProperties;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final AddressRepository addressRepository;
  private final ProductVariantRepository productVariantRepository;
  private final ProductRepository productRepository;
  private final ProvinceRepository provinceRepository;
  private final DistrictRepository districtRepository;
  private final WardRepository wardRepository;

  @Transactional
  public GhtkOrderResponse createShippingOrder(Long orderId) {
    log.info("(createShippingOrder) Bắt đầu tạo đơn giao hàng GHTK cho orderId: {}", orderId);

    // 1. Kiểm tra đơn hàng trong DB
    Order order = orderRepository.findByIdAndIsDeletedFalse(orderId)
        .orElseThrow(OrderNotFoundException::new);

    // Đơn hàng phải ở trạng thái ĐÃ XÁC NHẬN (CONFIRMED) hoặc ĐÃ THANH TOÁN (PAID)
    if (order.getStatus() != OrderStatus.CONFIRMED && order.getStatus() != OrderStatus.PAID) {
      log.warn("(createShippingOrder) Trạng thái đơn không hợp lệ để giao hàng: {}", order.getStatus());
      throw new InvalidOrderStatusException();
    }

    // 2. Lấy địa chỉ người nhận và giải mã tên Tỉnh/Huyện/Xã
    Address address = addressRepository.findByIdAndIsDeletedFalse(order.getAddressId())
        .orElseThrow(AddressNotFoundException::new);

    String provinceName = provinceRepository.findNameByCode(address.getProvinceCode())
        .orElse(address.getProvinceCode());
    String districtName = districtRepository.findNameByCode(address.getDistrictCode())
        .orElse(address.getDistrictCode());
    String wardName = wardRepository.findNameByCode(address.getWardCode())
        .orElse(address.getWardCode());

    // 3. Lấy danh sách sản phẩm và tính khối lượng (kg)
    List<OrderItem> orderItems = orderItemRepository.findAllByOrderIdAndIsDeletedFalse(orderId);
    if (orderItems.isEmpty()) {
      throw new ShippingItemsRequiredException();
    }

    List<GhtkOrderRequest.ProductItem> products = new ArrayList<>();
    for (OrderItem item : orderItems) {
      ProductVariant variant = productVariantRepository.findByIdAndIsDeletedFalse(item.getVariantId())
          .orElseThrow(ProductVariantNotFoundException::new);

      Product product = productRepository.findByIdAndIsDeletedFalse(variant.getProductId())
          .orElseThrow(ProductNotFoundException::new);

      // Quy đổi gram trong DB sang kg cho GHTK (mặc định 200g = 0.2kg)
      double weightInKg = (variant.getWeight() != null ? variant.getWeight() : 200) / 1000.0;

      products.add(GhtkOrderRequest.ProductItem.builder()
          .name(product.getName()) // Tên sản phẩm chính thức
          .weight(weightInKg)
          .quantity(item.getQuantity())
          .productCode(variant.getSku())
          .build());
    }

    // 4. Tính tiền thu hộ COD (pick_money)
    // Khách đã thanh toán online (PAID) -> COD = 0. Thanh toán khi nhận hàng -> COD = totalAmount.
    int pickMoney = (order.getStatus() == OrderStatus.PAID) ? 0 : order.getTotalAmount().intValue();

    // 5. Lấy cấu hình kho Shop từ application.yml
    GhtkProperties.ShopLocation shop = ghtkProperties.getShop();

    // 6. Xây dựng thông tin chi tiết đơn hàng (OrderInfo)
    GhtkOrderRequest.OrderInfo orderInfo = GhtkOrderRequest.OrderInfo.builder()
        .id(order.getOrderCode()) // Mã đơn hàng trong hệ thống gửi sang GHTK

        // --- Thông tin kho của Shop (Lấy từ cấu hình application.yml) ---
        .pickName(shop.getName())
        .pickTel(shop.getTel())
        .pickAddress(shop.getAddress())
        .pickProvince(shop.getProvince())
        .pickDistrict(shop.getDistrict())
        .pickWard(shop.getWard())

        // --- Thông tin khách nhận hàng ---
        .name(address.getRecipientName())
        .tel(address.getPhoneNumber())
        .address(address.getStreetAddress())
        .province(provinceName)
        .district(districtName)
        .ward(wardName)
        .hamlet("Khác")

        // --- Tiền cước và tiền thu hộ ---
        .isFreeship("1")
        .pickMoney(pickMoney)
        .value(order.getTotalAmount().intValue())
        .note(order.getNote() != null ? order.getNote() : "Cho khách xem hàng trước khi nhận")
        .build();

    // 7. Ráp Request hoàn chỉnh gồm products và orderInfo
    GhtkOrderRequest request = GhtkOrderRequest.builder()
        .products(products)
        .order(orderInfo)
        .build();

    // 8. Gọi GhtkClient bắn sang GHTK qua HTTP
    GhtkOrderResponse response = ghtkClient.createOrder(request);

    // 9. Kiểm tra kết quả phản hồi
    if (response == null || !Boolean.TRUE.equals(response.getSuccess()) || response.getOrder() == null) {
      String errorMsg = (response != null && response.getMessage() != null)
          ? response.getMessage()
          : "GHTK từ chối tạo đơn giao hàng";
      log.error("(createShippingOrder) GHTK tạo đơn thất bại cho orderId {}: {}", orderId, errorMsg);
      throw new BadRequestException(errorMsg);
    }

    // 10. Lấy mã vận đơn thành công (Ví dụ: S21.HN1.10098412)
    String trackingCode = response.getOrder().getLabel();
    log.info("(createShippingOrder) Đăng ký GHTK thành công! Order: {}, Mã vận đơn: {}",
        order.getOrderCode(), trackingCode);

    // Lưu mã vận đơn để in tem phiếu (Trạng thái đơn giữ nguyên, chờ Webhook shipper quét hàng mới đổi sang SHIPPING)
    order.setTrackingCode(trackingCode);
    orderRepository.save(order);

    return response;
  }

  /**
   * Hủy đơn vận chuyển bên GHTK
   *
   * @param orderId ID đơn hàng trong DB
   * @return GhtkCancelResponse Kết quả phản hồi từ GHTK
   */
  public GhtkCancelResponse cancelShippingOrder(Long orderId) {
    log.info("(cancelShippingOrder) Bắt đầu hủy vận đơn GHTK cho orderId: {}", orderId);

    Order order = orderRepository.findByIdAndIsDeletedFalse(orderId)
        .orElseThrow(OrderNotFoundException::new);

    if (order.getTrackingCode() == null || order.getTrackingCode().isBlank()) {
      log.warn("(cancelShippingOrder) Đơn hàng {} chưa có mã vận đơn", orderId);
      throw new BadRequestException("Đơn hàng chưa được tạo vận đơn bên GHTK để hủy!");
    }

    if (order.getStatus() == OrderStatus.SHIPPING) {
      log.warn("(cancelShippingOrder) Đơn hàng {} đã bàn giao cho bưu tá, không thể hủy", orderId);
      throw new BadRequestException("Hàng đã được bàn giao cho bưu tá GHTK, không thể hủy đơn!");
    }

    GhtkCancelResponse response = ghtkClient.cancelOrder(order.getTrackingCode());
    if (Boolean.TRUE.equals(response.getSuccess())) {
      log.info("(cancelShippingOrder) Hủy GHTK thành công cho đơn {}", order.getOrderCode());
    }

    return response;
  }
}
