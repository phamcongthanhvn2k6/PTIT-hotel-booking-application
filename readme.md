# 🏨 Dự Án Hệ Thống Đặt Phòng Khách Sạn (Hotel Booking System)

Chào mừng bạn đến với tài liệu hướng dẫn và vận hành của dự án **Hệ thống Đặt Phòng Khách Sạn**. Đây là một ứng dụng được xây dựng theo kiến trúc Client-Server hiện đại, đáp ứng đầy đủ các yêu cầu quản lý và đặt phòng trực tuyến tiện dụng, trực quan.

---

## 📋 Mục Lục
1. [Giới Thiệu Chung](#-giới-thiệu-chung)
2. [Cấu Trúc Tổ Chức Mã Nguồn](#-cấu-trúc-tổ-chức-mã-nguồn)
3. [Hướng Dẫn Cài Đặt & Khởi Chạy](#%EF%B8%8F-hướng-dẫn-cài-đặt--khởi-chạy)
4. [Kịch Bản Demo Chi Tiết Các Tính Năng](#-kịch-bản-demo-chi-tiết-các-tính-năng)
5. [Bộ Dữ Liệu Mẫu Cho Các Thao Tác CRUD (Demo)](#-bộ-dữ-liệu-mẫu-cho-các-thao-tác-crud-demo)
6. [Các Điểm Sáng Kỹ Thuật Đáng Chú Ý](#-các-điểm-sáng-kỹ-thuật-đáng-chú-ý)

---

## 📌 Giới Thiệu Chung
Dự án được xây dựng với mục tiêu chuyển đổi số trong lĩnh vực du lịch và lưu trú, giúp người dùng đặt phòng nhanh chóng và quản trị viên quản lý dễ dàng hơn.

*   **Nền tảng di động (Android Client):** Phát triển trên ngôn ngữ Java, giao diện Material Design 3 đẹp mắt, hỗ trợ hiển thị động, tối ưu hóa các thao tác chạm vuốt.
*   **Máy chủ RESTful API (Backend):** Sử dụng framework Java Spring Boot, tích hợp cơ chế bảo mật Spring Security & JWT cho phép phân quyền chi tiết giữa tài khoản thường (`ROLE_USER`) và tài khoản quản trị (`ROLE_ADMIN`).
*   **Cơ sở dữ liệu tập trung:** Sử dụng MySQL chạy trên cổng `3307` giúp lưu trữ và đồng bộ dữ liệu thời gian thực.
*   **Lưu trữ hình ảnh đám mây:** Tích hợp với dịch vụ Cloudinary để upload hình ảnh khách sạn trực tiếp từ thiết bị di động.

---

## 📂 Cấu Trúc Tổ Chức Mã Nguồn

### 1. Phân Hệ Backend (Spring Boot) - `/backend`
Tổ chức dự án theo mô hình phân lớp chuẩn (Layered Architecture):
```
backend/
├── src/main/java/com/phuc/datvekhachsan/backend/
│   ├── BackendApplication.java (File chạy chính khởi tạo Spring Boot)
│   ├── config/ (Cấu hình hệ thống: CORS, WebMvc, Cloudinary)
│   ├── controller/ (Định nghĩa các RESTful Endpoints để Android gọi API)
│   │   ├── AdminController.java (API dành riêng cho Admin: thống kê, quản trị User, Hotel, Room, Booking, Upload file)
│   │   ├── AuthController.java (API đăng nhập, đăng ký tài khoản, sinh Token JWT)
│   │   ├── BookingController.java (API đặt phòng, lấy lịch sử đặt phòng của user)
│   │   ├── HotelController.java (API lấy danh sách khách sạn dành cho khách hàng)
│   │   ├── RoomController.java (API lấy danh sách phòng của khách sạn)
│   │   └── UserController.java (API lấy/cập nhật thông tin tài khoản người dùng)
│   ├── dto/ (Data Transfer Objects để trao đổi dữ liệu an toàn qua API)
│   ├── model/ (Các JPA Entities ánh xạ trực tiếp đến các bảng MySQL: UserEntity, HotelEntity, RoomEntity, BookingEntity...)
│   ├── repository/ (Các Interface JPA Repository cung cấp các phương thức CRUD truy vấn database)
│   ├── security/ (Cấu hình Spring Security, JWT Filter chặn và giải mã Token, mã hóa mật khẩu BCrypt)
│   └── service/ (Xử lý các nghiệp vụ logic chính và dịch vụ tích hợp bên thứ ba như Cloudinary)
└── src/main/resources/
    └── application.properties (Chứa các cấu hình kết nối DB, khóa bí mật JWT, API key Cloudinary)
```

### 2. Phân Hệ Android Client - `/datvekhachsan`
Ứng dụng di động được tổ chức theo các gói chức năng (Packages):
```
datvekhachsan/
└── app/src/main/java/com/phuc/datvekhachsan/
    ├── activity/ (Chứa mã nguồn điều khiển các màn hình giao diện)
    │   ├── IntroActivity.java (Màn hình giới thiệu, chào mừng khi khởi động)
    │   ├── LoginActivity.java / RegisterActivity.java (Màn hình đăng nhập & đăng ký tài khoản)
    │   ├── MainActivity.java (Màn hình chính hiển thị danh mục, danh sách đề xuất)
    │   ├── SearchActivity.java (Màn hình tìm kiếm nâng cao kèm thuật toán tính điểm và bộ lọc)
    │   ├── HotelDetailActivity.java (Xem chi tiết khách sạn, bộ ảnh slider, tiện ích đi kèm)
    │   ├── RoomBookingActivity.java (Màn hình đặt phòng: chọn ngày check-in, hạng phòng và sơ đồ phòng dạng lưới)
    │   ├── PaymentActivity.java / PaymentSuccessActivity.java (Giao diện thanh toán và chúc mừng đặt phòng thành công)
    │   ├── BookingHistoryActivity.java (Xem lịch sử các đơn đặt phòng của tài khoản hiện tại)
    │   ├── FavoriteHotelsActivity.java (Xem danh sách các khách sạn đã lưu yêu thích)
    │   ├── ProfileActivity.java (Trang cá nhân, đổi ảnh đại diện và đăng xuất)
    │   └── admin/ (Thư mục chứa giao diện điều hành dành riêng cho ADMIN)
    │       ├── AdminDashboardActivity.java (Màn hình thống kê doanh thu dự kiến, doanh thu thật & liên kết quản lý)
    │       ├── AdminHotelListActivity.java / AdminHotelEditorActivity.java (Trang xem và thêm/sửa/xóa khách sạn)
    │       ├── AdminRoomListActivity.java / AdminRoomEditorActivity.java (Trang xem và thêm/sửa/xóa phòng của khách sạn)
    │       ├── AdminBookingListActivity.java (Duyệt danh sách đặt phòng và xác nhận thanh toán tại quầy)
    │       └── AdminUserListActivity.java (Xem danh sách tài khoản đã đăng ký trong hệ thống)
    ├── adapter/ (Các lớp Adapter xử lý ánh xạ dữ liệu lên RecyclerView như hiển thị danh sách phòng, khách sạn, ngày, tiện nghi...)
    ├── model/ (Các lớp Java Object đại diện cho thực thể dữ liệu nhận về từ API)
    ├── network/ (Cấu hình RetrofitClient kết nối Server và AuthInterceptor tự động đính kèm Token JWT)
    └── util/ (Lớp tiện ích bổ trợ như AuthManager quản lý phiên đăng nhập)
```

---

## 🛠️ Hướng Dẫn Cài Đặt & Khởi Chạy

### Bước 1: Clone dự án từ GitHub
1. Mở Terminal (hoặc Git Bash / Command Prompt).
2. Chạy lệnh clone để tải toàn bộ mã nguồn về máy:
   ```bash
   git clone <ĐƯỜNG_DẪN_GIT_CỦA_BẠN>
   ```
3. Di chuyển vào thư mục dự án:
   ```bash
   cd PTIT-Android-main
   ```

### Bước 2: Thiết lập Cơ sở dữ liệu MySQL
1. Khởi động MySQL Server của bạn (ví dụ dùng phần mềm **Laragon** hoặc **XAMPP**). Đảm bảo cổng kết nối MySQL là **3307** (nếu dùng cổng `3306` mặc định, vui lòng sửa lại cấu hình trong file [application.properties](file:///c:/PTIT-Android-main/backend/src/main/resources/application.properties)).
2. Tạo một cơ sở dữ liệu trống có tên là: `hotel_booking_db`.
3. Import file cơ sở dữ liệu có sẵn tại thư mục gốc của dự án: [init_db.sql](file:///c:/PTIT-Android-main/init_db.sql).

### Bước 3: Mở & Chạy RESTful API Server (Backend)
1. Bạn có thể mở thư mục gốc bằng các IDE hỗ trợ Java như **IntelliJ IDEA**, **Eclipse** hoặc **Android Studio** để chạy.
2. Mở Terminal tích hợp trong IDE (hoặc terminal ngoài máy tính).
3. Thực thi lệnh sau để di chuyển vào thư mục backend, biên dịch và chạy dự án Spring Boot:
   ```bash
   cd backend
   .\mvnw spring-boot:run
   ```
4. Sau khi khởi chạy thành công, máy chủ API sẽ sẵn sàng tại địa chỉ `http://localhost:8080`.

> [!NOTE]
> Mật khẩu mặc định của tất cả tài khoản có sẵn trong DB đều là `123456` (được lưu dưới dạng mã hóa BCrypt).
> *   **Tài khoản ADMIN:** `admin` | Mật khẩu: `123456`
> *   **Tài khoản USER tiêu chuẩn:** `phamcongt56@gmail.com` | Mật khẩu: `123456`

### Bước 4: Mở & Chạy ứng dụng Android Client
1. Khởi động **Android Studio**.
2. Trên màn hình welcome của Android Studio, chọn **Open** (hoặc **File > Open**).
3. Duyệt đến thư mục dự án vừa clone, chọn thư mục con `/datvekhachsan` và nhấn **OK** để mở.
4. Đợi Android Studio hoàn tất quá trình đồng bộ (Gradle Sync) để tải các thư viện.
5. Chọn thiết bị ảo (Emulator) đã tạo sẵn hoặc kết nối điện thoại Android thật vào máy tính. Nhấn nút **Run (▶)** màu xanh lá trên thanh công cụ để cài đặt và chạy ứng dụng.

> [!IMPORTANT]
> - Thiết bị ảo Android kết nối đến máy chủ localhost thông qua địa chỉ IP đặc biệt: `http://10.0.2.2:8080/`. Địa chỉ này đã được cấu hình sẵn trong [RetrofitClient.java](file:///c:/PTIT-Android-main/datvekhachsan/app/src/main/java/com/phuc/datvekhachsan/network/RetrofitClient.java).
> - Nếu chạy ứng dụng trên **điện thoại Android thật**, bạn cần đảm bảo điện thoại và máy tính chạy backend kết nối chung một mạng Wi-Fi. Sau đó thay đổi giá trị `BASE_URL` trong [RetrofitClient.java](file:///c:/PTIT-Android-main/datvekhachsan/app/src/main/java/com/phuc/datvekhachsan/network/RetrofitClient.java) từ `http://10.0.2.2:8080/` thành địa chỉ IP mạng nội bộ của máy tính bạn (ví dụ: `http://192.168.1.15:8080/`).

---

## 📱 Kịch Bản Demo Chi Tiết Các Tính Năng

### PHẦN I: TRẢI NGHIỆM DÀNH CHO USER (Khách hàng đặt phòng)
*Đăng nhập bằng tài khoản: **`phamcongt56@gmail.com`** / mật khẩu: **`123456`***

1.  **Đăng nhập & Điều hướng:**
    *   Mở ứng dụng, nhấn **Bắt đầu** ở màn hình Intro.
    *   Nhập thông tin tài khoản user. Hệ thống xác thực qua API, lấy Token JWT và lưu vào bộ nhớ SharedPreferences, dẫn người dùng vào trang chủ.
2.  **Khám phá trang chủ:**
    *   Xem danh mục khách sạn đề xuất hàng đầu (Vinpearl, Mường Thanh, Pullman...) hiển thị dưới dạng thẻ lướt ngang và danh sách đứng.
    *   Click vào nút trái tim trên mỗi thẻ khách sạn để thêm vào danh sách yêu thích cá nhân.
3.  **Tìm kiếm nâng cao:**
    *   Click vào thanh tìm kiếm ở trang chủ. Nhập ký tự bất kỳ (ví dụ: `Hà Nội`).
    *   Cơ chế **Debounce 300ms** tự động lọc danh sách sau khi dừng gõ, hạn chế giật lag.
    *   Khách sạn được hiển thị theo thứ tự điểm số từ thuật toán tính độ liên quan (Relevance Score).
4.  **Xem chi tiết & Luồng đặt phòng:**
    *   Click chọn khách sạn **Mường Thanh Luxury**. Xem mô tả, xem slider hình ảnh và các tiện ích (Wifi, Gym, Spa...).
    *   Nhấn nút **Đặt phòng ngay**.
    *   Chọn **Ngày nhận phòng** mong muốn (trong danh sách 14 ngày tới).
    *   Chọn **Hạng phòng** cần đặt (ví dụ: `Standard` hoặc `Deluxe`).
    *   Sơ đồ các phòng thuộc hạng đó sẽ hiện ra dạng lưới:
        *   *Màu xanh lá:* Phòng đang trống.
        *   *Màu xám:* Phòng đã được đặt trước đó (không thể click chọn).
    *   Click chọn một phòng trống (phòng đổi sang màu xanh dương). Tổng giá tiền thanh toán được cập nhật ngay lập tức.
    *   Nhấn **Xác nhận đặt phòng**, chọn hình thức thanh toán và hoàn tất đơn hàng.
5.  **Xem lịch sử:**
    *   Nhấn biểu tượng lịch sử ở thanh điều hướng để xem lại danh sách đơn hàng đã đặt cùng trạng thái tương ứng.

---

### PHẦN II: TRẢI NGHIỆM DÀNH CHO ADMIN (Quản trị viên)
*Đăng nhập bằng tài khoản: **`admin`** / mật khẩu: **`123456`***

1.  **Trang thống kê doanh thu (Dashboard):**
    *   Đăng nhập bằng tài khoản admin sẽ đưa bạn thẳng đến trang **Admin Dashboard**.
    *   Giao diện hiển thị trực quan các thẻ số liệu: *Tổng người dùng*, *Tổng số khách sạn*, *Tổng đơn đặt phòng*.
    *   Thống kê 2 chỉ số doanh thu quan trọng:
        *   **Doanh thu dự kiến (Expected Revenue):** Tổng tiền của tất cả các đơn đặt phòng (ngoại trừ các đơn bị hủy `CANCELLED`).
        *   **Doanh thu thật (Actual Revenue):** Chỉ tính tổng tiền của các đơn đặt phòng đã hoàn tất và được admin xác nhận thanh toán tại quầy (`COMPLETED`).
2.  **Xác nhận thanh toán (Thay đổi doanh thu thật):**
    *   Từ Dashboard, click chọn **Quản lý đặt phòng**.
    *   Nhấn nút **Xác nhận thanh toán** trên đơn đặt phòng mới của khách hàng (đang ở trạng thái `CONFIRMED`).
    *   Trạng thái đơn hàng chuyển sang `COMPLETED`.
    *   Quay lại trang Dashboard, chỉ số **Doanh thu thật** sẽ tăng lên tương ứng với số tiền của đơn hàng đó.
3.  **Quản lý người dùng:**
    *   Xem danh sách tất cả người dùng trong cơ sở dữ liệu.
    *   Cho phép thực hiện thao tác Khóa (LOCK) hoặc Mở khóa (ACTIVE) tài khoản người dùng ngay lập tức.
4.  **Quản lý Khách sạn & Phòng (CRUD):**
    *   Thực hiện thêm mới, sửa đổi thông tin hoặc xóa bớt các khách sạn và phòng nghỉ.
    *   Hỗ trợ upload trực tiếp ảnh chụp khách sạn từ điện thoại lên kho lưu trữ trực tuyến Cloudinary.

---

## 📝 Bộ Dữ Liệu Mẫu Cho Các Thao Tác CRUD (Demo)

Khi thực hiện thuyết trình hoặc demo trực tiếp tính năng thêm, sửa, xóa, bạn hãy sử dụng bộ dữ liệu mẫu dưới đây để thao tác nhanh và tạo hiệu ứng trực quan tốt nhất:

### 1. Thao tác với KHÁCH SẠN (Hotel CRUD)
*Đường dẫn truy cập: **Admin Dashboard** ➔ **Quản lý khách sạn***

#### A. Thêm Khách Sạn Mới (Create)
*Nhấn nút cộng `+` ở góc trên bên phải màn hình danh sách khách sạn.*
*   **Tên khách sạn:** `Khách sạn Grand Vista Hà Nội`
*   **Địa điểm:** `Hà Nội`
*   **Giá phòng tối thiểu:** `1200000` *(Nhập số liền, không nhập ký tự chữ hay dấu chấm)*
*   **Đánh giá (Rating):** `4.8`
*   **Mô tả chi tiết:** `Khách sạn 4 sao cao cấp tọa lạc tại trung tâm quận Ba Đình, Hà Nội. Hệ thống phòng nghỉ sang trọng, cung cấp đầy đủ dịch vụ tiện ích như hồ bơi bốn mùa trong nhà, phòng gym hiện đại và nhà hàng ẩm thực Á - Âu phục vụ 24/7.`
*   **Hình ảnh khách sạn:** Chọn ảnh thực tế từ thư viện điện thoại hoặc sao chép và dán liên kết ảnh này:
    `https://images.unsplash.com/photo-1551882547-ff40c0d589e6?w=800&q=80`
*   *Thao tác:* Nhấn nút **LƯU**. Hệ thống sẽ gọi API thêm khách sạn và tự động làm mới danh sách. Bạn vuốt xuống dưới cùng sẽ thấy khách sạn mới xuất hiện.

#### B. Sửa Thông Tin Khách Sạn (Update)
*Nhấn nút sửa (hình bút chì) bên cạnh khách sạn **Khách sạn Grand Vista Hà Nội** vừa tạo.*
*   **Tên khách sạn:** `Grand Vista Luxury & Spa Hà Nội` *(Sửa lại tên)*
*   **Giá phòng tối thiểu:** `1450000` *(Tăng giá phòng)*
*   **Đánh giá (Rating):** `4.9` *(Tăng rating)*
*   *Thao tác:* Nhấn nút **CẬP NHẬT**. Danh sách sẽ được thay đổi thông tin mới ngay lập tức.

#### C. Xóa Khách Sạn (Delete)
*Nhấn nút xóa (hình thùng rác màu đỏ) tại khách sạn mong muốn.*
*   *Thao tác:* Xác nhận **Đồng ý** trong hộp thoại cảnh báo hiện ra. Khách sạn sẽ bị xóa vĩnh viễn khỏi database và danh sách hiển thị của cả khách hàng.

---

### 2. Thao tác với PHÒNG CỦA KHÁCH SẠN (Room CRUD)
*Đường dẫn truy cập: **Quản lý khách sạn** ➔ Ấn vào tên một khách sạn (ví dụ: **Grand Vista Luxury & Spa Hà Nội**) để vào danh sách quản lý phòng của khách sạn đó.*

#### A. Thêm Phòng Mới (Create)
*Nhấn nút cộng `+` ở góc trên bên phải màn hình quản lý phòng.*
*   **Số phòng (Room Number):** `501`
*   **Loại phòng (Room Type):** Chọn `Deluxe` *(Hoặc chọn VIP, Standard, Suite tùy ý)*
*   **Giá mỗi đêm:** `1500000`
*   **Trạng thái ban đầu:** Chọn `AVAILABLE` *(Sẵn sàng đón khách)*
*   *Thao tác:* Nhấn nút **LƯU**. Phòng `501` sẽ xuất hiện trong danh sách phòng của khách sạn này.

#### B. Sửa Thông Tin Phòng (Update)
*Nhấn trực tiếp vào phòng `501` vừa tạo.*
*   **Số phòng:** `501-VIP` *(Thay đổi số phòng)*
*   **Loại phòng:** Đổi thành `Suite`
*   **Giá mỗi đêm:** `2200000` *(Tăng giá trị phòng)*
*   **Trạng thái phòng:** Đổi thành `AVAILABLE`
*   *Thao tác:* Nhấn nút **CẬP NHẬT**. Giao diện quản lý của Admin sẽ cập nhật ngay lập tức các thông số mới.

#### C. Xóa Phòng (Delete)
*Nhấn biểu tượng xóa (thùng rác) bên cạnh dòng phòng cần xóa.*
*   *Thao tác:* Chọn **Đồng ý** xác nhận xóa. Phòng sẽ biến mất khỏi sơ đồ phòng của khách sạn đó.

---

## 🌟 Các Điểm Sáng Kỹ Thuật Đáng Chú Ý

Khi báo cáo hoặc thuyết trình đồ án trước hội đồng chuyên môn, bạn nên tập trung giới thiệu các kỹ thuật tối ưu hóa cốt lõi đã triển khai trong mã nguồn:

1.  **Cơ chế xác thực không trạng thái (JWT Stateless):**
    *   Hệ thống không sử dụng session truyền thống. Thay vào đó, sau khi người dùng đăng nhập thành công, Server sinh ra chuỗi Token JWT.
    *   Android Client lưu Token này trong `SharedPreferences` thông qua lớp tiện ích `AuthManager`.
    *   Lớp `AuthInterceptor` (OkHttp) tự động can thiệp vào tất cả các yêu cầu tiếp theo để đính kèm Token này vào tiêu đề `Authorization: Bearer <token>`, giúp tối ưu bảo mật và giảm thiểu yêu cầu đăng nhập lại.
2.  **Dashboard Thống Kê Doanh Thu Động:**
    *   Hệ thống tự động phân tách **Doanh thu dự kiến** và **Doanh thu thật** dựa trên vòng đời của một đơn đặt phòng (`CONFIRMED` -> `COMPLETED`). Điều này phản ánh chính xác quy trình quản trị thực tế của các khách sạn.
3.  **Thuật toán Tìm kiếm & Lọc Relevance Score:**
    *   Lớp `SearchEngine` phía Android tự động chấm điểm độ tương quan của từ khóa tìm kiếm dựa trên độ khớp của tên khách sạn (trọng số 50), địa điểm (trọng số 30) và mô tả (trọng số 10) để sắp xếp kết quả tìm kiếm tốt nhất lên đầu.
4.  **Tối ưu hóa hiệu năng bằng Debounce:**
    *   Màn hình tìm kiếm sử dụng cơ chế trì hoãn lệnh (Debounce 300ms) để theo dõi ký tự nhập vào. Chỉ khi người dùng ngưng gõ phím quá 300ms thì bộ tìm kiếm mới thực thi lọc dữ liệu, tránh việc gửi quá nhiều yêu cầu API liên tục gây đơ nghẽn ứng dụng.
5.  **Tích hợp Cloudinary SDK:**
    *   Admin có thể chụp ảnh hoặc chọn ảnh khách sạn từ bộ nhớ máy, ứng dụng di động sẽ mã hóa và tải trực tiếp lên Cloudinary. Server API sẽ lưu trữ URL ảnh trực tuyến này, tránh lưu trữ file cục bộ nặng nề trên máy chủ Database.