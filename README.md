# Enterprise Spring Boot Base Starter Template 🚀

Dự án Base chuyên dụng cho các hệ thống và dự án lớn (Enterprise Production-Ready Starter Template) sử dụng **Spring Boot 3 + Java 17/21 + PostgreSQL + Liquibase + Redis + Apache Kafka + WebSocket + RustFS (S3-compatible) + JWT Security**. 

Được thiết kế theo kiến trúc phân lớp chuẩn mực, hướng sự kiện (Event-Driven), hỗ trợ thời gian thực (Real-time WebSockets), bộ nhớ đệm phân tán (Redis Caching), tối ưu hóa hiệu năng, bảo mật cấp độ doanh nghiệp và cực kỳ dễ dàng mở rộng.

---

## 🛠 Tech Stack & Kiến Trúc Hạ Tầng

| Thành phần | Phiên bản / Công nghệ | Vai trò & Mục đích |
|---|---|---|
| **Java** | 17 / 21 | Ngôn ngữ phát triển chính |
| **Spring Boot** | 3.2.2 | Web MVC, Security, Data JPA, Validation, Mail, WebSocket, Actuator |
| **Database** | PostgreSQL 16 | Cơ sở dữ liệu quan hệ chính |
| **Migration** | Liquibase | Quản lý phiên bản Database schema tự động & an toàn |
| **Message Broker** | Apache Kafka 3.7.0 (KRaft) | Kiến trúc hướng sự kiện (Event-driven), xử lý nền bất đồng bộ (OTP, Mail, Events) |
| **Kafka UI** | Provectus Kafka-UI | Giao diện trực quan giám sát Cluster, Topics, Consumer Groups |
| **Realtime** | Spring WebSocket | Giao tiếp 2 chiều thời gian thực (Real-time push, chat, presence, events) |
| **Cache & Session** | Redis 7 + Spring Cache | Cache phân tán (RedisCacheManager), quản lý Blacklist Token, OTP TTL |
| **Object Storage** | RustFS / AWS S3 API | Lưu trữ file, tài liệu, hình ảnh media hiệu năng cao qua giao thức S3 |
| **Security** | JJWT 0.12.5 + Spring Security | Xác thực JWT Access/Refresh Token (HttpOnly Cookie), RBAC, IDOR Protection |
| **Documentation** | SpringDoc OpenAPI 2.3.0 | Swagger UI tích hợp sẵn nút Authorize JWT |
| **Email Template** | Thymeleaf + JavaMailSender | Render mẫu email HTML động gửi OTP xác thực & đặt lại mật khẩu |

---

## 📦 Các Tính Năng Cốt Lõi Có Sẵn Trong Base Project

### 1. Kiến trúc phân lớp chuẩn mực & sạch (Clean Architecture)
- `controller` -> `service` -> `repository` -> `entity` & `dto`.
- Tách biệt rõ ràng giữa Request DTO, Response DTO, Persistence Entities và Domain Exceptions.

### 2. Quản trị Người dùng & Phân quyền (Auth & Users)
- **Đăng ký tài khoản:** Xác thực qua mã OTP gửi qua email bất đồng bộ bằng Kafka event.
- **Đăng nhập:** Cấp phát Access Token (JWT) và Refresh Token (lưu Redis TTL hoặc HttpOnly Cookie chống XSS).
- **Google OAuth2 Login:** Đăng nhập một chạm với tài khoản Google, tự động tạo tài khoản người dùng mới.
- **Quên mật khẩu & Reset:** Luồng gửi mã OTP xác thực qua email và đổi mật khẩu an toàn.
- **Đăng xuất & Thu hồi phiên:** Blacklist Access Token trên Redis và xoá Refresh Token.
- **Quản lý User (User Management):**
  - CRUD User chuẩn: Phân trang `PageResponse`, tìm kiếm từ khoá `keyword`.
  - Phân quyền: Chỉ ADMIN được xem danh sách hoặc xoá tài khoản.
  - Chống IDOR: Người dùng chỉ được xem/sửa thông tin của chính mình.
  - Bảo vệ tài khoản hệ thống: Chặn sửa/xoá tài khoản admin/superadmin (`ProtectedAccountException`).

### 3. Hướng sự kiện với Apache Kafka (Event-Driven Architecture)
- **Producer / Consumer tách biệt:** Đẩy các tác vụ nặng (như gửi email OTP, forgot password) qua Kafka topics thay vì gọi trực tiếp.
- **Retry Mechanism:** Tích hợp `@RetryableTopic` tự động thử lại 3 lần với exponential backoff khi gặp lỗi.
- **Dead Letter Topic (DLT):** Đưa message lỗi vào DLT (`@DltHandler`) sau khi vượt quá số lần retry, không làm nghẽn luồng xử lý chính.
- **Topic Auto-Configuration:** Tự động tạo topics (`auth.registration.otp`, `auth.forgot-password.otp`) với 3 partitions.

### 4. Kết nối Realtime WebSocket (Enterprise WebSocket Engine)
- **Session Registry:** Quản lý toàn bộ phiên kết nối đồng thời qua `EchoWebSocketHandler`.
- **User-to-Session Mapping:** Hỗ trợ 1 user mở nhiều tab/thiết bị cùng lúc.
- **Handshake Authentication:** Tích hợp `WebSocketAuthInterceptor` giải mã JWT Bearer Token ngay lúc bắt tay WebSocket và kiểm tra blacklist Redis.
- **Hỗ trợ API giao tiếp:**
  - `sendToUser(userId, action, data)`: Gửi đích danh tới một người dùng cụ thể.
  - `broadcast(action, data)`: Gửi thông báo đến toàn bộ client đang online.
  - `broadcastOnlineCount()`: Đếm và phát số lượng client đang hoạt động.
  - `isUserOnline(userId)`: Kiểm tra trạng thái trực tuyến của người dùng.

### 5. Bộ nhớ đệm phân tán (Redis Caching & Statistics)
- Cấu hình `RedisCacheManager` với bộ tuần tự hoá JSON (`GenericJackson2JsonRedisSerializer`), cấu hình TTL linh hoạt.
- Hỗ trợ endpoint theo dõi cache: `GET /api/v1/cache/stats` (thống kê hit, miss, hit-ratio của từng cache).

### 6. Quản lý File & Object Storage (RustFS / AWS S3 API)
- Upload đơn lẻ hoặc upload hàng loạt (multiple files) với metadata rõ ràng (`FileResponse`).
- Xoá file qua object key.
- Tích hợp RustFS (`https://rustfs.tuvidausotoanthu.vn`) - hệ thống lưu trữ đối tượng tương thích S3 hiệu năng cao viết bằng Rust.
- Cơ chế tự động kiểm tra và tạo bucket nếu chưa có.

### 7. Chuẩn hoá API Contract & Xử lý lỗi toàn cục
- Mọi phản hồi đều theo cấu trúc chuẩn `ResponseGeneral<T>` và `PageResponse<T>` (SNAKE_CASE).
- Bắt lỗi tập trung qua `ExceptionHandlerAdvice`:
  - Lỗi validate DTO (`MethodArgumentNotValidException`, `ConstraintViolationException`).
  - Lỗi tài nguyên (`NotFoundException`, `ConflictException`, `BadRequestException`, `ForbiddenException`, `UnauthorizedException`).
  - Lỗi hệ thống và ràng buộc database (`DataIntegrityViolationException`).
- Hỗ trợ đa ngôn ngữ quốc tế hoá i18n (`messages.properties` và `messages_vi.properties` UTF-8).

### 8. Cơ sở dữ liệu & JPA Auditing
- `BaseEntity` chuẩn: `id` (IDENTITY), `is_deleted` (xoá mềm), `created_at`, `created_by`, `updated_at`, `updated_by` (tự động điền qua `AuditorAware`).
- `BaseAuditEntity`: Dành cho các bảng log/sự kiện chỉ cần `createdAt` và `updatedAt`.
- Tự động chạy Liquibase migration khi khởi động ứng dụng (`master.xml`).

### 9. Tiện ích & Giám sát (Observability)
- `@TrackTime` AOP đo đạc và ghi log thời gian thực thi của các phương thức quan trọng.
- Thread Pool Task Executor cho các tác vụ bất đồng bộ.
- HTTP GZIP Compression giảm băng thông mạng.
- Spring Boot Actuator (`/actuator/health`, `/actuator/info`, `/actuator/metrics`).
- Swagger OpenAPI với Bearer JWT config sẵn.

---

## 📂 Cấu trúc thư mục

```
src/main/java/org/oplearn/project/
├── annotation/         # @TrackTime (AOP Logging thời gian)
├── configuration/      # Config: S3/MinIO, Async, AOP, JPA Auditing, i18n, Swagger, Cache, Kafka, WebSocket
├── constants/          # Hằng số hệ thống (Auth, Common, Paging, Cache, Kafka)
├── consumer/           # Kafka Listeners (OtpEmailConsumer, ForgotPasswordConsumer với Retry & DLT)
├── controller/         # REST Controllers (AuthController, UserController, FileController)
│   └── advice/         # ExceptionHandlerAdvice (Xử lý ngoại lệ toàn cục)
├── dto/
│   ├── request/        # Request payload DTOs (có validate @Valid, snake_case)
│   └── response/       # Response DTOs (ResponseGeneral, PageResponse, TokenResponse...)
├── entity/             # JPA Entities (User, BaseEntity, BaseAuditEntity)
├── enums/              # UserRole, AuthProvider
├── event/              # Kafka Event Payloads (OtpEmailEvent, ForgotPasswordEvent)
├── exception/          # Custom Exceptions (base/ và business exceptions)
├── repository/         # Spring Data JPA & Redis Repositories
│   └── redis/          # OtpRedisRepository, TokenRedisRepository
├── security/           # JWT Provider, Auth Filter, SecurityConfig, Handlers
├── service/            # Interfaces & Implementations (Auth, User, Email, Message, S3)
├── utils/              # Tiện ích bổ trợ (ClientIpUtils, DateUtils...)
└── websocket/          # EchoWebSocketHandler, WebSocketAuthInterceptor
```

---

## 🚀 Hướng dẫn khởi chạy dự án

### Cách 1: Chạy trọn gói qua Docker Compose (Khuyên dùng)
Hệ thống Docker Compose đã tích hợp đầy đủ PostgreSQL 16, Redis 7, Apache Kafka 3.7.0, Kafka UI và ứng dụng Spring Boot (kết nối trực tiếp RustFS Storage):

```bash
# 1. Sao chép cấu hình môi trường mẫu
cp .env.example .env

# 2. Khởi động toàn bộ cụm dịch vụ
docker compose up -d --build
```

### Cách 2: Chạy phục vụ phát triển (Local Development)

1. **Khởi động các dịch vụ phụ trợ:**
   ```bash
   docker compose up -d postgres redis kafka kafka-ui
   ```

2. **Kiểm tra trạng thái các dịch vụ:**
   - PostgreSQL: `localhost:5432`
   - Redis: `localhost:6379`
   - Kafka: `localhost:9092`
   - Kafka UI: [http://localhost:8085](http://localhost:8085)
   - RustFS Endpoint: [https://rustfs.tuvidausotoanthu.vn](https://rustfs.tuvidausotoanthu.vn)

3. **Chạy ứng dụng Spring Boot:**
   ```bash
   mvn spring-boot:run
   ```

---

## 🔗 Các đường dẫn truy cập quan trọng

- **Swagger UI (OpenAPI):** [http://localhost:8088/swagger-ui.html](http://localhost:8088/swagger-ui.html)
- **Kafka UI:** [http://localhost:8085](http://localhost:8085)
- **Cache Thống Kê:** [http://localhost:8088/api/v1/cache/stats](http://localhost:8088/api/v1/cache/stats)
- **Health Check:** [http://localhost:8088/actuator/health](http://localhost:8088/actuator/health)
- **WebSocket Endpoint:** `ws://localhost:8088/ws?token=<access_token>` (hoặc `/ws/echo`, `/ws/events`)

---

## 💡 Hướng dẫn bắt đầu dự án mới từ Base này

1. **Tạo Repository mới từ template:**
   - Sử dụng repo này làm khuôn mẫu (template) cho dự án mới.
2. **Cập nhật định danh dự án:**
   - `pom.xml`: Đổi `<artifactId>` và `<name>` theo dự án mới.
   - `application.yml`: Đổi `spring.application.name` và tên cơ sở dữ liệu (`app_db` -> `ten_db_moi`).
3. **Thêm module nghiệp vụ mới:**
   - Tạo Entity kế thừa `BaseEntity`.
   - Tạo JpaRepository kế thừa `JpaRepository<Entity, Long>`.
   - Tạo DTO Request/Response theo định dạng chuẩn.
   - Tạo Service Interface và Implementation.
   - Tạo Controller với các API RESTful.
   - Tạo file Liquibase migration mới trong `src/main/resources/db/changelog/003-create-table-xxx.xml` và include vào `src/main/resources/db/master.xml`.
   - Nếu có giao tiếp thời gian thực, inject `EchoWebSocketHandler` vào Service để gọi `sendToUser()` hoặc `broadcast()`.
   - Nếu có xử lý sự kiện nặng, tạo Kafka Event DTO, Producer và Consumer với `@RetryableTopic`.
