# TÀI LIỆU VÍ DỤ CHI TIẾT TÍCH HỢP API GIAO HÀNG TIẾT KIỆM (GHTK)

Tài liệu này cung cấp toàn bộ ví dụ mẫu chuẩn (Request/Response Examples, cURL, JSON Payload, mã lỗi) cho hệ thống vận chuyển Giao Hàng Tiết Kiệm (GHTK) để phục vụ việc tích hợp vào Backend và Frontend.

---

## MỤC LỤC
1. [Cấu Hình Môi Trường & Xác Thực](#1-cấu-hình-môi-trường--xác-thực)
2. [API 1: Tính Phí Vận Chuyển (Calculate Fee)](#api-1-tính-phí-vận-chuyển-calculate-fee)
3. [API 2: Đăng Ký Đơn Giao Hàng (Create Order)](#api-2-đăng-ký-đơn-giao-hàng-create-order)
4. [API 3: In Nhãn / Tem Vận Đơn (Print Label)](#api-3-in-nhãn--tem-vận-đơn-print-label)
5. [API 4: Webhook Cập Nhật Trạng Thái Tự Động (Webhook Listener)](#api-4-webhook-cập-nhật-trạng-thái-tự-động-webhook-listener)
6. [API 5: Tra Cứu Hành Trình Đơn Hàng (Tracking Order)](#api-5-tra-cứu-hành-trình-đơn-hàng-tracking-order)
7. [API 6: Hủy Đơn Hàng Vận Chuyển (Cancel Order)](#api-6-hủy-đơn-hàng-vận-chuyển-cancel-order)
8. [Phụ Lục: DTO Java Mẫu Cho Backend Spring Boot](#phụ-lục-dto-java-mẫu-cho-backend-spring-boot)

---

## 1. CẤU HÌNH MÔI TRƯỜNG & XÁC THỰC

### Base URLs
* **Môi trường Test (Staging / Sandbox)**: `https://services-staging.ghtk.vn`
* **Môi trường Thật (Production)**: `https://services.giaohangtietkiem.vn`

### Request Headers Bắt Buộc
Mọi request gửi từ máy chủ của bạn sang GHTK đều phải có các Headers sau:
```http
Token: {API_TOKEN_DO_GHTK_CAP}
X-Client-Source: {MA_SHOP_HOAC_PARTNER_CODE}
Content-Type: application/json
```

---

## API 1: TÍNH PHÍ VẬN CHUYỂN (Calculate Fee)

Dùng ở màn hình **Checkout (Thanh toán)** khi khách hàng chọn địa chỉ nhận hàng để tính tiền ship cộng vào đơn.

* **Method**: `GET`
* **Endpoint**: `/services/shipment/fee`

### Request Example (cURL)
```bash
curl --location --request GET 'https://services-staging.ghtk.vn/services/shipment/fee?pick_province=Hà%20Nội&pick_district=Quận%20Hai%20Bà%20Trưng&province=Hải%20Dương&district=Thành%20phố%20Hải%20Dương&weight=500&value=350000&transport=road' \
--header 'Token: 2A839B7C16E7F89A0BC123D45E67F890' \
--header 'Content-Type: application/json'
```

### Query Parameters Giải Thích
| Tham số | Bắt buộc | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
| :--- | :---: | :--- | :--- | :--- |
| `pick_province` | Có | String | Tỉnh/Thành phố của kho Shop | `Hà Nội` |
| `pick_district` | Có | String | Quận/Huyện của kho Shop | `Quận Hai Bà Trưng` |
| `province` | Có | String | Tỉnh/Thành phố người nhận | `Hải Dương` |
| `district` | Có | String | Quận/Huyện người nhận | `Thành phố Hải Dương` |
| `weight` | Có | Integer | Tổng cân nặng kiện hàng tính bằng **Gram** | `500` (500g) |
| `value` | Không | Integer | Giá trị tiền hàng (VNĐ) để tính bảo hiểm | `350000` |
| `transport` | Không | String | Phương thức: `road` (đường bộ) hoặc `fly` (bay) | `road` |

### Response Example 1: Thành Công (HTTP 200 OK)
```json
{
  "success": true,
  "message": "",
  "fee": {
    "name": "Giao hàng chuẩn",
    "fee": 22000,
    "insurance_fee": 0,
    "include_vat": "1",
    "cost_id": 482910,
    "delivery_type": "road",
    "dt": "2026-10-31",
    "extFees": []
  }
}
```

### Response Example 2: Thất Bại / Lỗi Tham Số (HTTP 200/400)
```json
{
  "success": false,
  "message": "Địa chỉ giao hàng không hợp lệ hoặc thiếu Quận/Huyện",
  "error_code": "INVALID_ADDRESS"
}
```

---

## API 2: ĐĂNG KÝ ĐƠN GIAO HÀNG (Create Order)

Dùng khi Shop bấm nút **"Giao Hàng"** trên trang Admin. GHTK tiếp nhận và cấp mã vận đơn.

* **Method**: `POST`
* **Endpoint**: `/services/shipment/order`

### Request Example (cURL)
```bash
curl --location --request POST 'https://services-staging.ghtk.vn/services/shipment/order' \
--header 'Token: 2A839B7C16E7F89A0BC123D45E67F890' \
--header 'Content-Type: application/json' \
--data-raw '{
  "products": [
    {
      "name": "Áo Sơ Mi Nam Oxford Dài Tay",
      "weight": 0.25,
      "quantity": 2,
      "product_code": "SM-OXFORD-TRANG-L"
    }
  ],
  "order": {
    "id": "HAZI_ORDER_100234",
    "pick_name": "Kho Hazi Store Hà Nội",
    "pick_address": "Số 1 Đại Cồ Việt",
    "pick_province": "Hà Nội",
    "pick_district": "Quận Hai Bà Trưng",
    "pick_ward": "Phường Bách Khoa",
    "pick_tel": "0912345678",
    "name": "Nguyễn Văn A",
    "address": "Số 25 đường Nguyễn Lương Bằng",
    "province": "Hải Dương",
    "district": "Thành phố Hải Dương",
    "ward": "Phường Việt Hòa",
    "tel": "0987654321",
    "is_freeship": "1",
    "pick_money": 350000,
    "value": 350000,
    "note": "Cho khách kiểm tra hàng trước khi nhận, không cho thử",
    "transport": "road"
  }
}'
```

### Response Example 1: Tạo Đơn Thành Công (HTTP 200 OK)
```json
{
  "success": true,
  "message": "Đơn hàng đã được tiếp nhận thành công",
  "order": {
    "partner_id": "HAZI_ORDER_100234",
    "label": "S21.HN1.10098412",
    "area": "1",
    "fee": 22000,
    "insurance_fee": 0,
    "estimated_pick_time": "Chiều nay 14:00 - 18:00",
    "estimated_deliver_time": "Ngày mai 31/10/2026",
    "status_id": 2
  }
}
```
> **Backend lưu trữ**: Lưu trường **`order.label`** (`S21.HN1.10098412`) vào database cột `orders.tracking_code`.

### Response Example 2: Thất Bại (Trùng mã đơn hàng)
```json
{
  "success": false,
  "message": "Mã đơn hàng HAZI_ORDER_100234 đã tồn tại trên hệ thống GHTK.",
  "error_code": "ORDER_ID_EXISTED"
}
```

---

## API 3: IN NHÃN / TEM VẬN ĐƠN (Print Label)

Dùng để in tem phiếu gửi (khổ A6 hoặc A7) dán lên gói hàng cho Shipper quét mã.

* **Method**: `GET`
* **Endpoint**: `/services/label/{TRACKING_CODE}`
* **Ví dụ**: `/services/label/S21.HN1.10098412?original=A6`

### Request Example (cURL)
```bash
curl --location --request GET 'https://services-staging.ghtk.vn/services/label/S21.HN1.10098412?original=A6' \
--header 'Token: 2A839B7C16E7F89A0BC123D45E67F890'
```

### Response
* **HTTP Status**: `200 OK`
* **Headers**: `Content-Type: application/pdf`
* **Body**: Dữ liệu file nhị phân (Binary PDF). Trình duyệt có thể mở xem trực tiếp hoặc truyền lệnh in nhiệt.

---

## API 4: WEBHOOK CẬP NHẬT TRẠNG THÁI TỰ ĐỘNG (Webhook Listener)

* **Method**: `POST`
* **URL do Shop cung cấp**: `https://api.yourdomain.com/api/v1/webhooks/ghtk`

### Request Payload GHTK Tự Động Bắn Về Server Của Bạn:

#### Mẫu 1: Shipper Đã Lấy Hàng Thành Công (Chuyển sang SHIPPING)
```json
{
  "partner_id": "HAZI_ORDER_100234",
  "label_id": "S21.HN1.10098412",
  "status_id": 3,
  "action_time": "2026-10-30T14:30:15+07:00",
  "reason_code": "",
  "reason": "",
  "weight": 0.5,
  "fee": 22000,
  "return_part_package": 0
}
```

#### Mẫu 2: Shipper Đang Trên Đường Đi Giao Hàng
```json
{
  "partner_id": "HAZI_ORDER_100234",
  "label_id": "S21.HN1.10098412",
  "status_id": 4,
  "action_time": "2026-10-31T08:15:00+07:00",
  "reason_code": "",
  "reason": "",
  "weight": 0.5,
  "fee": 22000,
  "return_part_package": 0
}
```

#### Mẫu 3: Giao Hàng Thành Công (Chuyển sang DELIVERED)
```json
{
  "partner_id": "HAZI_ORDER_100234",
  "label_id": "S21.HN1.10098412",
  "status_id": 5,
  "action_time": "2026-10-31T10:45:00+07:00",
  "reason_code": "",
  "reason": "",
  "weight": 0.5,
  "fee": 22000,
  "return_part_package": 0
}
```

#### Mẫu 4: Giao Không Thành Công / Delay Hẹn Lại Khách
```json
{
  "partner_id": "HAZI_ORDER_100234",
  "label_id": "S21.HN1.10098412",
  "status_id": 10,
  "action_time": "2026-10-31T11:20:00+07:00",
  "reason_code": "101",
  "reason": "Khách hàng bận, hẹn giao lại vào sáng mai 01/11",
  "weight": 0.5,
  "fee": 22000,
  "return_part_package": 0
}
```

### Response Bắt Buộc Server Bạn Phải Trả Về Cho GHTK:
```json
{
  "success": true,
  "message": "Webhook received successfully"
}
```

---

## API 5: TRA CỨU HÀNH TRÌNH ĐƠN HÀNG (Tracking Order)

Dùng khi người mua hoặc Admin bấm xem **"Lịch sử chi tiết hành trình"**.

* **Method**: `GET`
* **Endpoint**: `/services/shipment/v2/{TRACKING_CODE}`

### Request Example (cURL)
```bash
curl --location --request GET 'https://services-staging.ghtk.vn/services/shipment/v2/S21.HN1.10098412' \
--header 'Token: 2A839B7C16E7F89A0BC123D45E67F890'
```

### Response Example: Thành Công (HTTP 200 OK)
```json
{
  "success": true,
  "message": "",
  "data": {
    "label_id": "S21.HN1.10098412",
    "partner_id": "HAZI_ORDER_100234",
    "status": "4",
    "status_text": "Đang giao hàng",
    "created": "2026-10-30 14:20:11",
    "modified": "2026-10-31 08:15:00",
    "pick_date": "2026-10-30",
    "deliver_date": "2026-10-31",
    "customer_fullname": "Nguyễn Văn A",
    "customer_tel": "0987654321",
    "address": "Số 25 đường Nguyễn Lương Bằng, Phường Việt Hòa, TP Hải Dương",
    "weight": 0.5,
    "fee": 22000,
    "is_freeship": 1
  }
}
```

---

## API 6: HỦY ĐƠN HÀNG VẬN CHUYỂN (Cancel Order)

Dùng khi khách hủy đơn hoặc Shop muốn thu hồi đơn khi Shipper chưa lấy.

* **Method**: `POST`
* **Endpoint**: `/services/shipment/cancel/{TRACKING_CODE}`

### Request Example (cURL)
```bash
curl --location --request POST 'https://services-staging.ghtk.vn/services/shipment/cancel/S21.HN1.10098412' \
--header 'Token: 2A839B7C16E7F89A0BC123D45E67F890'
```

### Response Example 1: Hủy Thành Công (HTTP 200 OK)
```json
{
  "success": true,
  "message": "Yêu cầu hủy đơn hàng được gửi thành công.",
  "log_id": "81696a06745aa3c"
}
```

### Response Example 2: Hủy Thất Bại (Hàng đã trên đường vận chuyển)
```json
{
  "success": false,
  "message": "Đơn đã lấy hàng, không thể hủy đơn.",
  "code": 50102
}
```

---

## PHỤ LỤC: DTO JAVA MẪU CHO BACKEND SPRING BOOT

### 1. DTO Nhận Dữ Liệu Webhook
```java
package org.oplearn.project.dto.ghtk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GhtkWebhookPayload {
    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("label_id")
    private String labelId;

    @JsonProperty("status_id")
    private Integer statusId;

    @JsonProperty("action_time")
    private String actionTime;

    @JsonProperty("reason_code")
    private String reasonCode;

    private String reason;
    private Double weight;
    private Long fee;

    @JsonProperty("return_part_package")
    private Integer returnPartPackage;
}
```

### 2. Spring Boot Controller Xử Lý Webhook
```java
package org.oplearn.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.ghtk.GhtkWebhookPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
public class GhtkWebhookController {

    @PostMapping("/ghtk")
    public ResponseEntity<?> handleGhtkWebhook(@RequestBody GhtkWebhookPayload payload) {
        log.info("🔔 [GHTK Webhook] Nhận cập nhật đơn {}: status_id = {}", 
                payload.getPartnerId(), payload.getStatusId());

        switch (payload.getStatusId()) {
            case 3:
                // Shipper đã lấy hàng -> Chuyển đơn sang SHIPPING
                break;
            case 4:
                // Đang giao hàng -> Bắn WebSocket cho khách hàng
                break;
            case 5:
                // Giao thành công -> Chuyển đơn sang DELIVERED
                break;
            case 10:
                // Delay giao -> Lưu lý do hoãn giao
                break;
            case -1:
                // Đơn bị hủy -> Chuyển sang CANCELLED
                break;
        }

        // BẮT BUỘC TRẢ VỀ HTTP 200 ĐỂ GHTK KHÔNG BẮN LẠI
        return ResponseEntity.ok(Map.of("success", true, "message", "Webhook processed"));
    }
}
```
