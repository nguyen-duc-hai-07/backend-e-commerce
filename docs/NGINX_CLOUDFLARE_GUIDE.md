# CẨM NANG TOÀN TẬP: NGINX REVERSE PROXY & CLOUDFLARE (WEB SERVER, CDN & SECURITY)

> **Tài liệu tham khảo chuyên sâu & Bộ câu hỏi phỏng vấn thực chiến**  
> Dành cho Kỹ sư Backend Spring Boot / DevOps trong các hệ thống Web & E-Commerce Production.

---

## MỤC LỤC
1. [Tổng quan Kiến trúc Phân tầng (The Big Picture)](#1-tổng-quan-kiến-trúc-phân-tầng)
2. [CHUYÊN ĐỀ 1: Nginx Reverse Proxy, Load Balancing & SSL Termination](#chuyên-đề-1-nginx-reverse-proxy-load-balancing--ssl-termination)
   - [1.1. Bản chất Reverse Proxy vs Forward Proxy](#11-bản-chất-reverse-proxy-vs-forward-proxy)
   - [1.2. Mổ xẻ cấu trúc `nginx.conf` (`server`, `location`, `proxy_pass`)](#12-mổ-xẻ-cấu-trúc-nginxconf)
   - [1.3. Cân bằng tải Load Balancing (`upstream`, thuật toán, failover)](#13-cân-bằng-tải-load-balancing)
   - [1.4. SSL/TLS Termination với Certbot Let's Encrypt](#14-ssltls-termination-với-certbot-lets-encrypt)
   - [1.5. Tối ưu hiệu năng: Gzip Compression & Client Max Body Size](#15-tối-ưu-hiệu-năng-gzip--client-max-body-size)
   - [1.6. Phòng thủ tầng Nginx: Rate Limiting (`limit_req`)](#16-phòng-thủ-tầng-nginx-rate-limiting)
   - [1.7. Giải đáp Câu hỏi Phỏng vấn Trọng tâm (Chuyên đề 1)](#17-giải-đáp-câu-hỏi-phỏng-vấn-trọng-tâm-chuyên-đề-1)
3. [CHUYÊN ĐỀ 2: Cloudflare - Quản lý DNS, CDN Edge Caching & Bảo mật WAF / DDoS](#chuyên-đề-2-cloudflare---dns-cdn-edge-caching--waf--ddos)
   - [2.1. Quản lý DNS & Bản chất Chế độ Proxied (Đám mây cam)](#21-quản-lý-dns--bản-chất-chế-độ-proxied-đám-mây-cam)
   - [2.2. Phân tích 4 chế độ SSL/TLS & Cài đặt Origin Certificate](#22-phân-tích-4-chế-độ-ssltls--origin-certificate)
   - [2.3. CDN Edge Caching: Tăng tốc và giảm tải 80% băng thông](#23-cdn-edge-caching)
   - [2.4. Tường lửa WAF, Chống DDoS & Bot Fight Mode](#24-tường-lửa-waf-chống-ddos--bot-fight-mode)
   - [2.5. Giải đáp Câu hỏi Phỏng vấn Trọng tâm (Chuyên đề 2)](#25-giải-đáp-câu-hỏi-phỏng-vấn-trọng-tâm-chuyên-đề-2)
4. [Mẫu cấu hình Production End-to-End](#4-mẫu-cấu-hình-production-end-to-end)

---

# 1. TỔNG QUAN KIẾN TRÚC PHÂN TẦNG

Trong một hệ thống Production chuyên nghiệp, người dùng **không bao giờ** kết nối trực tiếp vào ứng dụng Spring Boot. Lưu lượng truy cập bắt buộc phải đi qua 3 tầng lá chắn:

```text
[ KHÁCH HÀNG INTERNET ]
         │
         ▼  (1) Tầng Mạng Biên Toàn Cầu (Cloudflare Anycast Network)
 ┌─────────────────────────────────────────────────────────────────┐
 │ CLOUDFLARE: DNS, CDN Edge Cache, WAF, Chống DDoS, Bot Protection│
 └─────────────────────────────────────────────────────────────────┘
         │  (Chỉ cho phép lưu lượng sạch đi qua)
         ▼  (2) Cổng Web Server Máy Chủ (VPS Linux)
 ┌─────────────────────────────────────────────────────────────────┐
 │ NGINX REVERSE PROXY: SSL Offload, Load Balancing, Rate Limiting │
 └─────────────────────────────────────────────────────────────────┘
         │  (Giao tiếp mạng nội bộ Docker Network / Localhost)
         ▼  (3) Tầng Xử lý Nghiệp vụ & Dữ liệu
 ┌─────────────────────────────────────────────────────────────────┐
 │ BACKEND: Spring Boot Containers (Port 8088)                     │
 │ DATA: PostgreSQL (5432) | Redis (6379) | Kafka (9092)           │
 └─────────────────────────────────────────────────────────────────┘
```

---

# CHUYÊN ĐỀ 1: NGINX REVERSE PROXY, LOAD BALANCING & SSL TERMINATION

---

### 1.1. Bản chất Reverse Proxy vs Forward Proxy

| Tiêu chí | Forward Proxy (Proxy xuôi) | Reverse Proxy (Proxy ngược - Nginx) |
| :--- | :--- | :--- |
| **Đại diện cho ai?** | Đại diện cho **Client** (Người dùng). | Đại diện cho **Server** (Hệ thống backend). |
| **Vị trí đứng** | Đứng cùng phía với Client (trong mạng nội bộ Client). | Đứng cùng phía với Server (ở cửa ngõ hệ thống). |
| **Danh tính bị ẩn** | **Giấu IP của Client.** Server bên ngoài chỉ thấy IP của Proxy. | **Giấu IP của Backend Server.** Client chỉ thấy IP của Nginx. |
| **Mục đích chính** | Vượt tường lửa, ẩn danh duyệt web, kiểm soát truy cập internet nhân viên trong công ty. | Bảo mật backend, cân bằng tải, nén dữ liệu, giải mã SSL, chặn request xấu. |
| **Ví dụ thực tế** | VPN, Proxy công ty chặn Facebook/Youtube. | Nginx đứng trước cụm Spring Boot / NodeJS. |

---

### 1.2. Mổ xẻ cấu trúc `nginx.conf`

Một cấu hình Nginx tiêu chuẩn được chia thành các khối (block) cha con có thứ tự phân cấp nghiêm ngặt:

```nginx
events {
    # Số lượng kết nối tối đa 1 worker process có thể xử lý đồng thời
    worker_connections 1024;
}

http {
    # Định nghĩa MIME types để browser biết file nào là json, css, image...
    include       mime.types;
    default_type  application/octet-stream;

    # Khối server đại diện cho một Virtual Host (một Domain hoặc Port)
    server {
        listen 80;
        server_name api.myshop.com;

        # Khối location bắt các đường dẫn URL khớp với mẫu
        location / {
            # proxy_pass: Chuyển tiếp request đến server đích
            proxy_pass http://localhost:8088;

            # Gắn các Header quan trọng để Spring Boot hiểu ngữ cảnh request
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }

        # Bắt riêng đường dẫn tĩnh (ảnh, css) để Nginx tự đọc ổ cứng, không làm phiền Spring Boot
        location /static/ {
            root /var/www/ecommerce;
            expires 30d; # Cache trình duyệt 30 ngày
        }
    }
}
```

---

### 1.3. Cân bằng tải Load Balancing (`upstream`)

Khối `upstream` nhóm nhiều máy chủ backend lại thành một cụm duy nhất. Nginx sẽ phân phối request vào cụm này theo các chiến lược:

```nginx
upstream backend_cluster {
    # 1. Round Robin (Mặc định nếu không ghi gì): Xoay vòng lần lượt A -> B -> C -> A
    
    # 2. least_conn: Ưu tiên server đang xử lý ít kết nối nhất (Khuyên dùng cho API nghiệp vụ nặng nhẹ thất thường)
    least_conn;

    # 3. ip_hash: Băm IP người dùng, cùng 1 IP luôn vào 1 server (Dùng khi session lưu trên RAM server)
    # ip_hash;

    # Cấu hình từng server node:
    server 10.0.0.1:8088 weight=3 max_fails=3 fail_timeout=30s;
    server 10.0.0.2:8088 weight=1 max_fails=3 fail_timeout=30s;
    server 10.0.0.3:8088 backup; # Máy chủ dự phòng, chỉ chạy khi 2 máy trên chết hẳn
}

server {
    listen 80;
    location / {
        proxy_pass http://backend_cluster;
    }
}
```

* **`weight=3`:** Máy mạnh nhận gấp 3 lần lượng request của máy `weight=1`.
* **`max_fails=3 fail_timeout=30s`:** Nếu server thử kết nối lỗi 3 lần, Nginx đánh dấu nó "chết" và ngưng gửi khách vào trong 30 giây (Health Check & Failover tự động).

---

### 1.4. SSL/TLS Termination với Certbot Let's Encrypt

* **Bản chất SSL Termination:** 
  Client gửi HTTPS (Port 443) tới Nginx -> Nginx thực hiện "bắt tay" SSL, giải mã dữ liệu mã hóa ra thành văn bản thuần (Plaintext HTTP) -> Nginx đẩy HTTP thuần vào Spring Boot qua port 8088 nội bộ.
  > **Lợi ích:** Java Virtual Machine (JVM) không cần gánh thuật toán mã hóa RSA/ECDSA tốn CPU, tập trung 100% tài nguyên xử lý nghiệp vụ.

* **Cài đặt miễn phí với Certbot trên Ubuntu/Linux:**
  ```bash
  # 1. Cài certbot cho nginx
  sudo apt install certbot python3-certbot-nginx -y

  # 2. Xin cấp chứng chỉ tự động (Certbot sẽ tự chỉnh sửa file nginx.conf)
  sudo certbot --nginx -d api.myshop.com

  # 3. Kiểm tra tự động gia hạn (Let's Encrypt hết hạn sau 90 ngày)
  sudo certbot renew --dry-run
  ```

---

### 1.5. Tối ưu hiệu năng: Gzip Compression & Client Max Body Size

#### Gzip Compression
Nén JSON API và Text trước khi truyền qua mạng Internet, giúp giảm kích thước payload từ 70% - 80%:
```nginx
http {
    gzip on;
    gzip_vary on;
    gzip_min_length 1024; # Chỉ nén các response > 1KB (nhỏ hơn nén tốn CPU vô ích)
    gzip_proxied any;
    gzip_comp_level 6;    # Mức nén từ 1-9 (mức 6 là điểm cân bằng lý tưởng giữa CPU và tỉ lệ nén)
    gzip_types
        text/plain
        text/css
        text/javascript
        application/json
        application/javascript
        application/x-javascript
        text/xml
        application/xml
        application/xml+rss
        image/svg+xml;
}
```

#### Client Max Body Size (Khắc phục lỗi HTTP 413 Payload Too Large)
Mặc định Nginx chỉ cho phép upload file tối đa **1MB**. Khi người dùng upload ảnh sản phẩm, ảnh đại diện lên S3/RustFS sẽ bị Nginx chặn ngay từ ngoài cửa.
```nginx
# Đặt trong khối http, server hoặc location
client_max_body_size 20M; # Tăng giới hạn lên 20MB
```

---

### 1.6. Phòng thủ tầng Nginx: Rate Limiting (`limit_req`)

Nginx sử dụng thuật toán **Leaky Bucket (Thùng rò rỉ)** để làm mượt hoặc chặn các đợt request dồn dập (DDoS tầng 7, spam bot).

```nginx
# Khai báo vùng nhớ lưu trạng thái IP (10MB lưu được khoảng 160.000 IP)
# Cho phép tốc độ tối đa: 10 request/giây trên 1 IP
limit_req_zone $binary_remote_addr zone=api_limit:10m rate=10r/s;

# Vùng nhớ riêng cực kỳ nghiêm ngặt cho API đăng nhập (1 request/giây)
limit_req_zone $binary_remote_addr zone=login_limit:10m rate=1r/s;

server {
    # Áp dụng cho toàn bộ API chung: cho phép bộc phát (burst) tối đa 20 request
    location /api/ {
        limit_req zone=api_limit burst=20 nodelay;
        proxy_pass http://app:8088;
    }

    # Áp dụng riêng cho API đăng nhập chống Brute-force mật khẩu
    location /api/v1/auth/login {
        limit_req zone=login_limit burst=3 nodelay;
        limit_req_status 429; # Trả về HTTP 429 Too Many Requests khi vượt ngưỡng
        proxy_pass http://app:8088;
    }
}
```
* **`burst=20`:** Cho phép tích lũy tối đa 20 request đến cùng lúc (đáp ứng khi người dùng mở nhiều tab hoặc F5 nhanh).
* **`nodelay`:** Nếu trong phạm vi `burst`, xử lý ngay lập tức mà không bắt client phải chờ. Nếu vượt quá `burst`, trả lỗi `429` ngay.

---

### 1.7. Giải đáp Câu hỏi Phỏng vấn Trọng tâm (Chuyên đề 1)

#### ❓ Q1: Nginx đóng vai trò gì khi đứng trước ứng dụng Spring Boot? Tại sao không cho Client gọi trực tiếp vào port 8080 của Tomcat?

> **Trả lời:**  
> Không nên cho Client gọi trực tiếp vào port 8080 của Tomcat/Spring Boot vì 5 lý do cốt tử sau:
> 
> 1. **Kiến trúc luồng & Vấn đề C10K (Slowloris Attack):**
>    - Embedded Tomcat của Spring Boot hoạt động theo mô hình *Thread-per-request* (mỗi request chiếm 1 thread hệ thống). Mặc dù có Virtual Thread (Java 21), việc giữ hàng chục ngàn kết nối mạng di động chậm chập (slow clients) vẫn gây ngốn tài nguyên Heap và làm cạn kiệt connection pool.
>    - Nginx được viết bằng C dựa trên kiến trúc *Asynchronous Non-blocking Event Loop (epoll)*. Một worker process của Nginx có thể duy trì hàng chục ngàn kết nối đồng thời với vài chục MB RAM, "hứng" toàn bộ kết nối chậm, gom đủ buffer rồi mới bắn chớp nhoáng vào Tomcat.
> 2. **Bảo mật mạng (Attack Surface Reduction):**
>    - Nếu mở port 8080, Tomcat lộ diện trực tiếp trước Internet, dễ bị tấn công khai thác lỗ hổng framework (như Spring4Shell), bị brute-force hoặc quét cổng. Nginx cho phép đóng kín cổng 8080 trong mạng nội bộ Docker.
> 3. **SSL/TLS Offloading:**
>    - Việc bắt tay SSL, trao đổi khóa và mã hóa đối xứng tốn CPU rất lớn. Giao việc này cho Nginx giải mã giúp Spring Boot tập trung 100% CPU chạy Business Logic.
> 4. **Tải tĩnh & Nén dữ liệu (Static Serving & Gzip):**
>    - Nginx xử lý file tĩnh (ảnh, html, js) bằng cơ chế `sendfile()` (zero-copy ở tầng Kernel), bỏ qua tầng user-space của Java, tốc độ nhanh gấp hàng chục lần so với để Spring Boot đọc file từ đĩa.
> 5. **Khả năng mở rộng không downtime (Zero-downtime Deployment & Load Balancing):**
>    - Nginx cho phép thực hiện Rolling Update (khởi động instance Spring Boot mới, kiểm tra healthcheck rồi mới chuyển hướng traffic, không làm gián đoạn người dùng 1 giây nào).

---

#### ❓ Q2: Reverse Proxy khác Forward Proxy ở điểm nào?

> **Trả lời (Bảng so sánh phỏng vấn):**
> 
> | Đặc điểm | Forward Proxy | Reverse Proxy |
> | :--- | :--- | :--- |
> | **Đối tượng bảo vệ** | Bảo vệ và đại diện cho **Client (Người dùng)**. | Bảo vệ và đại diện cho **Server (Hệ thống)**. |
> | **Phía kiểm soát** | Do Client hoặc Quản trị viên mạng nội bộ Client cài đặt. | Do Quản trị viên hệ thống Backend/DevOps cài đặt. |
> | **IP nhìn thấy từ Internet**| Server đích **chỉ thấy IP của Forward Proxy**, không biết IP thật của Client. | Client **chỉ thấy IP của Reverse Proxy**, không biết IP thật của Backend Server. |
> | **Nhiệm vụ chính** | Vượt tường lửa, ẩn danh duyệt web, lọc web cấm trong doanh nghiệp. | Cân bằng tải, SSL Termination, Caching, WAF, Rate Limiting. |
> | **Ví dụ điển hình** | Squid Proxy, VPN, Tor Network. | Nginx, HAProxy, Envoy, Traefik. |

---

#### ❓ Q3: Làm thế nào để Spring Boot nhận đúng IP thật của Client khi đứng sau Nginx (`X-Forwarded-For`, `X-Real-IP`)?

> **Trả lời:**  
> **1. Vấn đề:**  
> Khi request đi qua Nginx, kết nối TCP thực sự được thiết lập giữa *Nginx* và *Spring Boot*. Do đó, nếu gọi `request.getRemoteAddr()`, Spring Boot sẽ luôn trả về `127.0.0.1` hoặc IP container của Nginx thay vì IP của khách hàng.
> 
> **2. Giải pháp 2 bước:**
> 
> * **Bước 1: Cấu hình Nginx gắn các Header chuẩn hóa:**
>   ```nginx
>   proxy_set_header X-Real-IP $remote_addr;
>   proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
>   proxy_set_header X-Forwarded-Proto $scheme;
>   ```
>   - `$remote_addr`: Địa chỉ IP của bên vừa kết nối vào Nginx.
>   - `$proxy_add_x_forwarded_for`: Chuỗi danh sách các IP dạng `client, proxy1, proxy2`.
> 
> * **Bước 2: Cấu hình Spring Boot tự động phân tích Header:**
>   Trong `application.yml`:
>   ```yaml
>   server:
>     forward-headers-strategy: framework # hoặc 'native' nếu dùng Tomcat độc lập
>   ```
>   Khi bật cờ này, Spring Boot sẽ tự động bóc tách header `X-Forwarded-For`, trích xuất đúng Client IP ban đầu và gán vào `request.getRemoteAddr()`, đồng thời nhận diện đúng giao thức HTTPS từ `X-Forwarded-Proto`.

---

# CHUYÊN ĐỀ 2: CLOUDFLARE - DNS, CDN EDGE CACHING & WAF / DDOS

---

### 2.1. Quản lý DNS & Bản chất Chế độ Proxied (Đám mây cam)

Cloudflare hoạt động trên nền tảng **Anycast Network** (hơn 300 thành phố trên 100 quốc gia chia sẻ chung một dải IP).

```text
[ DNS Record trên Cloudflare ]
  - A  api.myshop.com  ->  103.120.45.67 (IP VPS thật)  |  Proxy status: PROXIED (🟠)
```

* **Trạng thái Đám mây xám (DNS Only):**
  - Cloudflare chỉ làm máy chủ DNS phân giải tên miền.
  - Khi ai đó gõ `nslookup api.myshop.com`, kết quả trả về đúng IP thật `103.120.45.67`.
  - Toàn bộ tính năng CDN, WAF, Chống DDoS **hoàn toàn vô hiệu hóa**.
* **Trạng thái Đám mây cam (Proxied):**
  - Khi ai đó phân giải `api.myshop.com`, kết quả trả về là **IP của Cloudflare** (ví dụ `104.21.45.10` / `172.67.180.20`).
  - Toàn bộ gói tin TCP/HTTP buộc phải đi qua cụm máy chủ Cloudflare trước. IP thật của VPS được ẩn giấu 100% khỏi các công cụ quét mạng như Shodan hay Censys.

---

### 2.2. Phân tích 4 chế độ SSL/TLS & Origin Certificate

Trong bảng điều khiển **SSL/TLS** của Cloudflare, có 4 tùy chọn:

```text
1. OFF:
   [Client] ──── HTTP (Không mã hóa) ────> [Cloudflare] ──── HTTP ────> [VPS Nginx]

2. FLEXIBLE:
   [Client] ──── HTTPS (Ổ khóa xanh) ────> [Cloudflare] ──── HTTP ────> [VPS Nginx]

3. FULL:
   [Client] ──── HTTPS (Ổ khóa xanh) ────> [Cloudflare] ──── HTTPS (Không check cert) ────> [VPS Nginx]

4. FULL (STRICT) - CHUẨN DOANH NGHIỆP:
   [Client] ──── HTTPS (Mã hóa) ────> [Cloudflare] ──── HTTPS (Mã hóa đầu cuối) ────> [VPS Nginx]
                                                        (Kiểm tra chứng chỉ hợp lệ)
```

#### Nguồn gốc Lỗi Chuyển hướng Vòng lặp (ERR_TOO_MANY_REDIRECTS)
Lỗi này xảy ra khi bạn bật chế độ **Flexible**:
1. Khách hàng truy cập `https://myshop.com` (Cloudflare nhận bằng HTTPS).
2. Do chọn *Flexible*, Cloudflare kết nối về Nginx/Spring Boot qua **HTTP (port 80)**.
3. Ở phía VPS, Spring Boot hoặc Nginx có cấu hình: *"Hễ thấy ai vào bằng HTTP thì Redirect 301 sang HTTPS"*.
4. Nginx trả về `301 Moved Permanently -> https://myshop.com`.
5. Cloudflare nhận được lệnh 301, lại tiếp tục gửi HTTP về Nginx.
6. Hai bên đập bóng qua lại vô tận -> Trình duyệt báo lỗi **`ERR_TOO_MANY_REDIRECTS`**.

#### Giải pháp tối ưu: Full (Strict) + Cloudflare Origin CA
1. Trên Cloudflare: Vào **SSL/TLS** -> **Origin Server** -> Bấm **Create Certificate** (Hạn dùng 15 năm, miễn phí).
2. Lưu Certificate (`cert.pem`) và Private Key (`key.pem`) lên VPS.
3. Cấu hình Nginx:
   ```nginx
   server {
       listen 443 ssl http2;
       server_name api.myshop.com;
       ssl_certificate     /etc/nginx/certs/cert.pem;
       ssl_certificate_key /etc/nginx/certs/key.pem;
   }
   ```
4. Đổi chế độ SSL trên Cloudflare sang **Full (Strict)**. Hệ thống được bảo mật tuyệt đối từ đầu đến cuối mà không cần lo gia hạn chứng chỉ hàng quý!

---

### 2.3. CDN Edge Caching

Mặc định, Cloudflare tự động cache các file tĩnh dựa trên đuôi mở rộng: `.jpg`, `.png`, `.webp`, `.css`, `.js`, `.pdf`, `.mp4`.

* **Trạng thái Cache (CF-Cache-Status Header):**
  - `HIT`: Dữ liệu được trả thẳng từ RAM/SSD của máy chủ Cloudflare gần khách nhất (thời gian phản hồi < 10ms, VPS không tốn 1 byte băng thông).
  - `MISS`: Dữ liệu chưa có ở Edge, Cloudflare quay về hỏi VPS của bạn, lưu vào cache rồi mới trả cho khách.
  - `BYPASS`: Cloudflare không cache, chuyển thẳng về VPS (mặc định áp dụng cho dynamic API).

#### Thiết lập Cache Rules cho E-Commerce:
Vào Cloudflare -> **Caching** -> **Cache Rules**:
* **Rule 1 (Bypass cho API):** `URI Path starts_with "/api/"` -> **Bypass Cache**.
* **Rule 2 (Cache mạnh cho ảnh sản phẩm trên S3/RustFS):** `URI Path starts_with "/media/"` -> **Eligible for cache**, Edge TTL: `7 days`, Browser TTL: `1 day`.

---

### 2.4. Tường lửa WAF, Chống DDoS & Bot Fight Mode

#### Chống DDoS đa tầng
* **Tầng 3 & 4 (Network & Transport Layer):** Cloudflare tự động hấp thụ các đợt tấn công SYN Flood, UDP Amplification lên tới hàng trăm Gbps mà VPS không hề hay biết.
* **Tầng 7 (Application Layer - HTTP Flood):** 
  - Khi có dấu hiệu bị cào quét hoặc đánh sập web, bật tính năng **Under Attack Mode**.
  - Khách hàng sẽ thấy một màn hình kiểm tra mượt mà (Cloudflare Turnstile) để xác minh không phải bot trước khi được chuyển vào web.

#### Bot Fight Mode & Custom WAF Rules
* **Bot Fight Mode:** Sử dụng mô hình Machine Learning nhận diện hành vi của các trình duyệt không đầu (Headless Chrome, Puppeteer, Selenium) chuyên đi cào trộm giá sản phẩm để chặn đứng.
* **Rate Limiting Rule (Miễn phí):**
  - Vào **Security** -> **WAF** -> **Rate Limiting Rules**.
  - Biểu thức: `http.request.uri.path eq "/api/v1/auth/login"`
  - Quy tắc: Nếu 1 IP gửi > 5 request trong vòng 10 giây -> Chặn truy cập (Block) trong 15 phút.

---

### 2.5. Giải đáp Câu hỏi Phỏng vấn Trọng tâm (Chuyên đề 2)

#### ❓ Q1: Chế độ Proxied (Orange Cloud) của Cloudflare hoạt động như thế nào và tại sao nó lại giúp bảo vệ máy chủ VPS khỏi tấn công DDoS?

> **Trả lời:**  
> **1. Cách thức hoạt động:**  
> Khi bật chế độ Proxied (Đám mây màu cam), Cloudflare hoạt động như một hệ thống **Global Reverse Proxy khổng lồ**. Bản ghi DNS của domain không trỏ về IP của máy chủ VPS gốc, mà trỏ về mạng lưới Anycast IP của Cloudflare.
> 
> **2. Tại sao nó bảo vệ máy chủ VPS khỏi DDoS?**
> - **Ẩn IP gốc (Origin IP Masking):** Kẻ tấn công không thể biết IP thật của VPS để bắn trực tiếp các gói tin phá hoại (nếu lộ IP, hacker có thể bypass Cloudflare để tấn công thẳng vào VPS).
> - **Hấp thụ băng thông khủng (Anycast Routing):** Dung lượng mạng của Cloudflare đạt hơn 280+ Tbps. Khi có đợt tấn công DDoS hàng triệu request/s, lưu lượng tấn công bị xé nhỏ và phân tán ra hơn 300 trung tâm dữ liệu toàn cầu thay vì dồn cục vào một máy chủ duy nhất của bạn.
> - **Tự động lọc gói tin rác tại biên (Edge Scrubbing):** Các cuộc tấn công hạ tầng (SYN Flood, UDP Flood, ICMP) bị máy chủ biên của Cloudflare chặn đứng 100%, chỉ có lưu lượng HTTP/HTTPS hợp lệ được xác thực mới được chuyển tiếp về VPS gốc.

---

#### ❓ Q2: Sự khác biệt cốt lõi giữa các chế độ SSL của Cloudflare: Flexible, Full và Full (Strict)? Khi nào xảy ra lỗi chuyển hướng vòng lặp (Redirect Loop) và cách khắc phục?

> **Trả lời:**  
> **1. Bảng so sánh 3 chế độ:**
> 
> | Tiêu chí | Flexible | Full | Full (Strict) |
> | :--- | :--- | :--- | :--- |
> | **Mã hóa Client ➔ Cloudflare** | Có (HTTPS) | Có (HTTPS) | Có (HTTPS) |
> | **Mã hóa Cloudflare ➔ VPS** | **KHÔNG (HTTP thuần)** | Có (HTTPS) | Có (HTTPS) |
> | **Yêu cầu chứng chỉ trên VPS** | Không cần | Chấp nhận cả chứng chỉ tự ký (Self-signed) | **Bắt buộc chứng chỉ hợp lệ (Trusted CA hoặc Cloudflare Origin CA)** |
> | **Độ an toàn** | Kém (dữ liệu giữa Cloudflare và VPS bị lộ plaintext). | Trung bình | **Tuyệt đối (Chuẩn doanh nghiệp/Ngân hàng)** |
> 
> **2. Nguyên nhân xảy ra lỗi Redirect Loop (`ERR_TOO_MANY_REDIRECTS`):**
> - Lỗi này xảy ra **khi chọn chế độ Flexible** nhưng ở phía máy chủ VPS lại bật tính năng ép buộc HTTPS (Force HTTPS):
>   - Trình duyệt gọi `https://myshop.com`.
>   - Cloudflare nhận bằng HTTPS, nhưng do cài đặt *Flexible*, nó lại gửi request bằng `http://` (Port 80) về máy chủ VPS.
>   - Web server Nginx hoặc Spring Boot Security trên VPS thấy request đến bằng `http://` liền phản hồi mã `301/302 Redirect` yêu cầu Client đổi sang `https://`.
>   - Cloudflare nhận lệnh Redirect, trình duyệt lại gửi lại `https://`, và chu trình này lặp lại vô tận.
> 
> **3. Cách khắc phục triệt để:**
> - Cài đặt chứng chỉ SSL trên Nginx (dùng Cloudflare Origin Certificate miễn phí 15 năm).
> - Chuyển chế độ mã hóa trên Cloudflare từ *Flexible* sang **Full (Strict)**.

---

# 4. MẪU CẤU HÌNH PRODUCTION END-TO-END

Dưới đây là file `default.conf` chuẩn mực của Nginx khi kết hợp cùng **Cloudflare Full (Strict)** và **Spring Boot**:

```nginx
# ==============================================================================
# NGINX PRODUCTION CONFIGURATION FOR CLOUDFLARE + SPRING BOOT
# ==============================================================================

# 1. Định nghĩa cụm backend
upstream spring_backend {
    least_conn;
    server app:8088 max_fails=3 fail_timeout=30s;
    keepalive 32;
}

# 2. Khai báo Rate Limiting
limit_req_zone $binary_remote_addr zone=api_general:10m rate=20r/s;
limit_req_zone $binary_remote_addr zone=auth_login:10m rate=2r/s;

# 3. HTTP Server (Port 80): Chuyển hướng toàn bộ sang HTTPS
server {
    listen 80;
    server_name api.myshop.com;
    return 301 https://$host$request_uri;
}

# 4. HTTPS Server (Port 443)
server {
    listen 443 ssl http2;
    server_name api.myshop.com;

    # Chứng chỉ Cloudflare Origin Certificate (15 năm)
    ssl_certificate     /etc/nginx/certs/cloudflare_cert.pem;
    ssl_certificate_key /etc/nginx/certs/cloudflare_key.pem;

    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 1d;

    # Kích thước file upload tối đa
    client_max_body_size 25M;

    # Nén Gzip
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_comp_level 6;
    gzip_types application/json text/plain text/css application/javascript image/svg+xml;

    # ==========================================================================
    # QUAN TRỌNG: Lấy đúng IP thật của khách hàng qua Cloudflare
    # ==========================================================================
    # Nếu có Cloudflare, IP thật nằm ở Header CF-Connecting-IP
    set_real_ip_from 173.245.48.0/20;
    set_real_ip_from 103.21.244.0/22;
    set_real_ip_from 103.22.200.0/22;
    set_real_ip_from 103.31.4.0/22;
    set_real_ip_from 141.101.64.0/18;
    set_real_ip_from 108.162.192.0/18;
    set_real_ip_from 190.93.240.0/20;
    set_real_ip_from 188.114.96.0/20;
    set_real_ip_from 197.234.240.0/22;
    set_real_ip_from 198.41.128.0/17;
    set_real_ip_from 162.158.0.0/15;
    set_real_ip_from 104.16.0.0/13;
    set_real_ip_from 104.24.0.0/14;
    set_real_ip_from 172.64.0.0/13;
    set_real_ip_from 131.0.72.0/22;
    real_ip_header CF-Connecting-IP;

    # Route đăng nhập: Áp dụng Rate Limiting khắt khe chống brute-force
    location /api/v1/auth/login {
        limit_req zone=auth_login burst=5 nodelay;
        limit_req_status 429;

        proxy_pass http://spring_backend;
        include /etc/nginx/conf.d/proxy_params.common;
    }

    # Route chung toàn bộ API
    location / {
        limit_req zone=api_general burst=30 nodelay;

        proxy_pass http://spring_backend;
        
        # Headers chuyển tiếp
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto https;

        # WebSocket support
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";

        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
}
```
