# Base Database Schema

Dự án sử dụng **PostgreSQL** kết hợp với **Liquibase** để quản lý migration database tự động.

---

## 1. Enums

### `role_enum`
- `ADMIN`: Quản trị viên hệ thống.
- `USER`: Người dùng thông thường.

### `auth_provider_enum`
- `LOCAL`: Đăng ký và đăng nhập qua hệ thống (username/password + email OTP).
- `GOOGLE`: Đăng nhập thông qua Google OAuth2.

---

## 2. Các bảng cơ sở dữ liệu

### Bảng `users`
Bảng lưu trữ thông tin tài khoản người dùng trong hệ thống.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| `id` | `BIGSERIAL` | `PRIMARY KEY` | Khóa chính tự tăng |
| `username` | `VARCHAR(50)` | `NOT NULL, UNIQUE` | Tên đăng nhập |
| `email` | `VARCHAR(50)` | `NOT NULL, UNIQUE` | Địa chỉ email |
| `phone_number` | `VARCHAR(50)` | `NULL` | Số điện thoại |
| `password` | `VARCHAR(255)` | `NULL` | Mật khẩu (đã mã hóa BCrypt, null nếu đăng nhập Google) |
| `role` | `role_enum` | `NOT NULL` | Quyền (`ADMIN`, `USER`) |
| `provider` | `auth_provider_enum` | `NOT NULL, DEFAULT 'LOCAL'` | Phương thức đăng ký (`LOCAL`, `GOOGLE`) |
| `provider_id` | `VARCHAR(255)` | `NULL` | ID định danh từ OAuth Provider |
| `is_deleted` | `BOOLEAN` | `NOT NULL, DEFAULT FALSE` | Cờ xóa mềm (Soft Delete) |
| `created_at` | `TIMESTAMP WITH TIME ZONE` | `DEFAULT CURRENT_TIMESTAMP` | Thời gian tạo |
| `created_by` | `VARCHAR(255)` | `NULL` | Người tạo (JPA Auditing) |
| `updated_at` | `TIMESTAMP WITH TIME ZONE` | `NULL` | Thời gian cập nhật lần cuối |
| `updated_by` | `VARCHAR(255)` | `NULL` | Người cập nhật lần cuối (JPA Auditing) |

**Indexes:**
- `idx_users_provider_provider_id` trên `(provider, provider_id)`.

---

## 3. Quy chuẩn tạo bảng mới (Best Practices)

Khi thêm thực thể nghiệp vụ mới:
1. Mọi bảng nên kế thừa các trường chuẩn từ `BaseEntity` (`id`, `is_deleted`, `created_at`, `created_by`, `updated_at`, `updated_by`).
2. Luôn tạo file changelog mới trong thư mục `src/main/resources/db/changelog/` với cú pháp `NNN-mo-ta.xml` (ví dụ: `003-create-table-orders.xml`).
3. Khai báo file mới vào `src/main/resources/db/master.xml`.
4. Không chỉnh sửa các changelog đã được chạy trên môi trường Database chung.
