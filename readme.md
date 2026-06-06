1. GIỚI THIỆU
1.1 Bối cảnh và lý do chọn đề tài
Trong bối cảnh chuyển đổi số mạnh mẽ, nhu cầu đặt phòng khách sạn qua thiết bị di động ngày càng phổ biến. Người dùng có xu hướng ưu tiên các ứng dụng cho phép tìm kiếm, xem thông tin, chọn phòng và đặt phòng nhanh chóng thay vì phải thực hiện thủ công qua điện thoại hoặc trực tiếp tại quầy. Từ thực tiễn đó, nhóm lựa chọn đề tài xây dựng ứng dụng đặt phòng khách sạn nhằm mô phỏng quy trình đặt phòng hiện đại, thuận tiện và dễ sử dụng trên nền tảng Android.
1.2 Tổng quan dự án
Dự án là một ứng dụng Android phát triển bằng Java, tập trung vào trải nghiệm đặt phòng khách sạn với giao diện Material Design 3, hỗ trợ Dark Mode, danh sách khách sạn, tìm kiếm thông minh, xem chi tiết khách sạn, chọn ngày nhận phòng, chọn loại phòng, chọn phòng trực quan và lưu lịch sử đặt phòng cục bộ cho từng tài khoản người dùng. Ứng dụng sử dụng SharedPreferences kết hợp Gson để lưu trữ dữ liệu offline, đồng thời tổ chức giao diện theo các màn hình rõ ràng như MainActivity, HotelDetailActivity, SearchActivity, RoomBookingActivity và BookingHistoryActivity.
1.3 Phạm vi và thông tin dự án

Tiêu chí	Nội dung
Tên đề tài	Xây dựng ứng dụng đặt phòng khách sạn
Phạm vi	Ứng dụng di động Android
Đối tượng sử dụng	Người dùng đăng ký/đăng nhập để tìm và đặt phòng
Nền tảng triển khai	Android
Học phần	Phát triển ứng dụng cho các thiết bị di động
Kiến trúc hệ thống	Client - Server (Android App kết nối RESTful API).
Công nghệ lưu trữ	Cơ sở dữ liệu quan hệ MySQL tập trung.

1.4 Kết quả kỳ vọng
Sau khi hoàn thành, hệ thống cần đáp ứng các tiêu chí sau:
•	Người dùng có thể đăng ký, đăng nhập và sử dụng ứng dụng ổn định. 
•	Người dùng có thể tìm kiếm khách sạn theo tên, vị trí và mô tả. 
•	Người dùng có thể xem thông tin chi tiết khách sạn, tiện ích và ảnh minh họa. 
•	Người dùng có thể chọn ngày nhận phòng, loại phòng và phòng cụ thể. 
•	Lịch sử đặt phòng được lưu riêng cho từng tài khoản để tránh nhầm lẫn dữ liệu. 
•	Giao diện trực quan, dễ thao tác, phù hợp với trải nghiệm di động.
2. MÔ TẢ ĐỀ TÀI
2.1 Tổng quan
Đề tài tập trung xây dựng một ứng dụng đặt phòng khách sạn trên Android, hỗ trợ toàn bộ quy trình cơ bản từ duyệt danh sách khách sạn, xem chi tiết, tìm kiếm nâng cao, chọn phòng và lưu lại lịch sử đặt phòng. Hệ thống sử dụng các mô-đun tách biệt để dễ bảo trì và mở rộng, đồng thời mô phỏng nghiệp vụ đặt phòng thực tế bằng dữ liệu khách sạn Việt Nam có sẵn trong MockData.
2.2 Yêu cầu chính của đề tài
Các yêu cầu chính của đề tài gồm:
•	Hiển thị danh sách khách sạn với thông tin cơ bản như tên, địa điểm, ảnh, đánh giá và giá phòng. 
•	Cho phép tìm kiếm khách sạn thông minh theo nhiều tiêu chí. 
•	Hiển thị chi tiết khách sạn với ảnh slider, tiện ích, mô tả và giá. 
•	Hỗ trợ đặt phòng theo luồng nhiều bước: chọn ngày nhận phòng, chọn loại phòng, chọn phòng cụ thể. 
•	Lưu lịch sử đặt phòng offline theo từng tài khoản người dùng. 
•	Giao diện phải hiện đại, dễ nhìn và hoạt động mượt trên thiết bị Android.
2.3 Các phân hệ chính
STT	Phân hệ	Chức năng chính
1	Quản lý & duyệt khách sạn	Hiển thị danh sách khách sạn đề xuất/phổ biến, hỗ trợ chuyển danh mục.
2	Chi tiết khách sạn	Hiển thị thông tin chi tiết, tiện nghi (Wifi, Gym, Spa, Hồ bơi...) và slider ảnh bằng ViewPager2.
3	Đặt phòng & Chọn phòng	Lọc tầng, chọn ngày nhận phòng, hạng phòng, sơ đồ chọn phòng trực quan và tính tiền real-time.
4	Quản lý người dùng	Đăng ký, đăng nhập và lưu trữ trạng thái phiên làm việc (Session) offline.
5	Lịch sử đặt phòng	Lưu trữ và hiển thị danh sách các phòng đã đặt theo từng tài khoản.
6	Tìm kiếm & Bộ lọc nâng cao	Tìm kiếm thông minh có tính điểm độ tương quan, lọc đa tiêu chí (địa điểm, giá, rating).

3. MỤC TIÊU DỰ ÁN
3.1 Mục tiêu chính
•  Xây dựng ứng dụng Android hoàn chỉnh phục vụ nhu cầu đặt phòng khách sạn. 
•  Thiết kế giao diện rõ ràng, dễ sử dụng và mang tính thẩm mỹ cao. 
•  Áp dụng các kiến thức đã học về lập trình Android, RecyclerView, SharedPreferences, Adapter, Activity, Intent và xử lý dữ liệu JSON. 
•  Tạo ra một sản phẩm có tính thực tế, có thể trình bày và bảo vệ trong học phần.
3.2 Mục tiêu cụ thể
•  Hiển thị danh sách khách sạn mặc định của Việt Nam như Mường Thanh, Vinpearl Resort, Pullman Saigon, InterContinental, Novotel Phú Quốc, Sheraton Hà Nội, Fusion Maia Resort, JW Marriott Phú Quốc và La Siesta Hội An. 
•  Tạo thuật toán tìm kiếm có tính điểm để tăng độ chính xác kết quả. 
•  Hỗ trợ debounce khi tìm kiếm để giảm lag và tăng trải nghiệm nhập liệu. 
•  Tạo luồng đặt phòng nhiều bước, có kiểm tra hợp lệ rõ ràng. 
•  Lưu trữ lịch sử đặt phòng cục bộ theo từng tài khoản bằng SharedPreferences và Gson.
4. KIẾN TRÚC HỆ THỐNG
4.1 Tổng quan kiến trúc
Hệ thống được xây dựng theo mô hình Client - Server gồm 3 thành phần chính:
1.	Mobile Client (Android App): Phát triển bằng Java, chịu trách nhiệm xử lý giao diện người dùng, gửi yêu cầu (HTTP Requests) thông qua thư viện Retrofit 2 và lưu trữ tạm thời Access Token (JWT) trong SharedPreferences.
2.	RESTful API Server (Java Spring Boot): Đóng vai trò là Middleware xử lý các nghiệp vụ (Business Logic). Tích hợp Spring Security và JWT (JSON Web Token) để xác thực, phân quyền các Endpoint API. Sử dụng Spring Data JPA (Hibernate) để tương tác với Database.
3.	Database Server (MySQL DB): Hệ quản trị cơ sở dữ liệu quan hệ, lưu trữ tập trung dữ liệu về Người dùng, Khách sạn, Phòng và Lịch sử đặt phòng.
4.2 Cấu trúc tổ chức mã nguồn
Mã nguồn của dự án được tổ chức theo mô hình phân lớp rõ ràng để dễ bảo trì và mở rộng:
•	com.phuc.datvekhachsan.activity: Chứa các màn hình giao diện chính của ứng dụng như IntroActivity, MainActivity, LoginActivity, RegisterActivity, HotelListActivity, HotelDetailActivity, SearchActivity, RoomBookingActivity và BookingHistoryActivity. 
•	com.phuc.datvekhachsan.adapter: Chứa các Adapter phục vụ hiển thị dữ liệu trên RecyclerView và ViewPager2 như BannerAdapter, HotelListAdapter, DetailImageSliderAdapter, AmenityTagAdapter, FacilityAdapter, CheckInDateAdapter, RoomTypeAdapter, RoomGridAdapter và BookingHistoryAdapter. 
•	com.phuc.datvekhachsan.model: Chứa các lớp mô hình dữ liệu như User, Hotel, Room, Booking, Amenity và SliderItem. 
•	com.phuc.datvekhachsan.util: Chứa các lớp xử lý logic hỗ trợ như AuthManager, BookingManager và SearchEngine. 
•	com.phuc.datvekhachsan.data: Chứa MockData, dùng để giả lập dữ liệu khách sạn và tài khoản mẫu trong giai đoạn phát triển và kiểm thử.
5. CÔNG NGHỆ SỬ DỤNG


Nhóm	Công nghệ	Mô tả
Android Client	Java	Ngôn ngữ phát triển ứng dụng di động chính.
Android Client	Retrofit 2 & OkHttp	Thư viện gọi API, gửi Header chứa JWT Bearer Token lên Server.
Android Client	SharedPreferences	Chỉ dùng để lưu trữ trạng thái đăng nhập và chuỗi Token JWT (hạn chế lưu data cứng).
Backend Server	Java Spring Boot	Framework xây dựng dịch vụ RESTful API phía máy chủ.
Backend Security	Spring Security & JWT	Bộ lọc xác thực (JWT Filter), mã hóa mật khẩu (BCryptPasswordEncoder), phân quyền truy cập Role-based (USER/ADMIN).
Database	MySQL & Spring Data	JPA	Cơ sở dữ liệu quan hệ lưu trữ dữ liệu tập trung, truy vấn thông qua thực thể Entity (JPA).

6. THIẾT KẾ CHỨC NĂNG
6.1 Phân hệ Quản lý Khách sạn
•  Hiển thị danh sách khách sạn phổ biến và đề xuất. 
•  Mỗi khách sạn có ảnh, tên, vị trí, rating, giá phòng và tag tiện ích. 
•  Dữ liệu được hiển thị bằng Adapter để tối ưu hiển thị danh sách.
6.2 Phân hệ Chi tiết Khách sạn
•  Hiển thị mô tả chi tiết, giá phòng, rating và danh sách tiện ích. 
•  Dùng ViewPager2 để hiển thị bộ ảnh khách sạn dạng slider. 
•  Có các tiện ích như Wifi, Gym, Spa, Hồ bơi… kèm icon rõ ràng.
6.3 Phân hệ Đặt Phòng & Chọn Phòng
•  Chọn ngày nhận phòng bằng danh sách 14 ngày kế tiếp. 
•  Chọn hạng phòng theo nhiều mức như Standard, Superior, Deluxe, Suite, VIP Suite, Penthouse. 
•  Hiển thị phòng theo dạng lưới và cho phép chọn/bỏ chọn trực quan. 
•  Mỗi phòng có trạng thái Available, Selected hoặc Unavailable. 
•  Tổng tiền được cập nhật theo thời gian thực.
6.4 Phân hệ Quản lý Người Dùng
Cơ chế xác thực không trạng thái (Stateless Authentication) bằng JWT:
Đăng nhập (Sign In): Thiết bị gửi Username/Password lên API /api/auth/login. Spring Boot kiểm tra, mã hóa kiểm tra mật khẩu bằng BCrypt. Nếu đúng, Server tạo một chuỗi JWT chứa thông tin người dùng và quyền hạn (Role: USER/ADMIN) được ký bằng thuật toán bảo mật (HS256) rồi trả về Client. 
Lưu trữ Token: Android Client nhận JWT và lưu trữ an toàn trong SharedPreferences thông qua lớp AuthManager. 
Ủy quyền (Authorization): Với mọi yêu cầu tiếp theo cần bảo mật (như đặt phòng, xem lịch sử, quản trị khách sạn), ứng dụng Android sẽ đính kèm Token này vào Header của HTTP Request dạng: Authorization: Bearer <JWT_Token> Spring Security trên Server sẽ có một JwtAuthenticationFilter chặn lại để giải mã, kiểm tra tính hợp lệ và phân quyền trước khi cho phép truy cập tài nguyên.
6.5 Phân hệ Lịch sử Đặt Phòng
Phân hệ lịch sử đặt phòng cho phép người dùng xem lại danh sách phòng đã đặt một cách nhanh chóng. Mỗi khi thực hiện đặt phòng thành công, thông tin giao dịch sẽ được lưu trữ tập trung vào bảng bookings trong cơ sở dữ liệu MySQL trên Server. Khi người dùng truy cập màn hình lịch sử đặt phòng (BookingHistoryActivity), ứng dụng Android sẽ gửi yêu cầu HTTP GET kèm theo token JWT đến API /api/bookings/my-history. Server giải mã token để định danh tài khoản, sau đó truy vấn cơ sở dữ liệu MySQL và trả về danh sách lịch sử đặt phòng dưới dạng JSON để hiển thị trực quan lên giao diện.
6.6 Phân hệ Tìm kiếm Thông minh
•  Tìm kiếm theo tên khách sạn, địa điểm, mô tả. 
•  Tính điểm liên quan để xếp kết quả hợp lý. 
•  Hỗ trợ lọc theo giá, rating và địa điểm. 
•  Dùng debounce để tối ưu hiệu năng khi người dùng nhập nhanh.
6.6.1. Cơ chế tìm kiếm có tính điểm
Để nâng cao độ chính xác của kết quả tìm kiếm, nhóm xây dựng lớp SearchEngine với cơ chế tính điểm độ liên quan (Relevance Score). Mỗi khách sạn sẽ được gán một điểm số dựa trên mức độ phù hợp với từ khóa nhập vào của người dùng.
Công thức tính điểm:
S=50⋅Iname+30⋅Ilocation+10⋅Idescription+5⋅Istart_token+2⋅Idesc_token  
Trong đó:
•	I_name = 1 nếu từ khóa khớp tên khách sạn, ngược lại bằng 0. 
•	I_location = 1 nếu khớp vị trí địa lý, ngược lại bằng 0. 
•	I_description = 1 nếu khớp trong mô tả chi tiết, ngược lại bằng 0. 
•	I_start_token = 1 nếu một token con bắt đầu bằng từ khóa. 
•	I_desc_token = 1 nếu token con xuất hiện trong mô tả. 
Sau khi tính điểm cho toàn bộ danh sách, hệ thống sắp xếp giảm dần theo S để đưa những khách sạn phù hợp nhất lên đầu kết quả.
6.6.2. Kỹ thuật Debounce tối ưu hiệu năng nhập liệu
Trong SearchActivity, nhóm triển khai cơ chế Debounce nhằm tránh việc hệ thống liên tục xử lý lại bộ lọc mỗi khi người dùng gõ từng ký tự, từ đó giảm lag giao diện và tối ưu hiệu năng.
Cơ chế được thực hiện bằng TextWatcher kết hợp Handler:
searchInput.addTextChangedListener(new TextWatcher() {
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (searchTask != null) handler.removeCallbacks(searchTask);
    }

    @Override
    public void afterTextChanged(Editable s) {
        searchTask = () -> runSearch();
        handler.postDelayed(searchTask, 300);
    }
});
Nguyên lý hoạt động:
•	Khi người dùng tiếp tục nhập liệu, tác vụ tìm kiếm cũ bị hủy. 
•	Chỉ khi người dùng ngừng nhập trong 300ms, hệ thống mới thực hiện tìm kiếm. 
•	Cách làm này giúp giao diện mượt hơn và giảm số lần xử lý không cần thiết.
7. LUỒNG HOẠT ĐỘNG CHÍNH
7.1 Luồng Đặt Phòng
Bước	Hành động	Mô tả chi tiết
1	Đăng nhập	Người dùng xác thực tài khoản
2	Duyệt khách sạn	Chọn khách sạn từ danh sách
3	Xem chi tiết	Xem ảnh, mô tả, tiện ích, giá
4	Chọn ngày nhận phòng	Chọn trong danh sách ngày tự sinh
5	Chọn loại phòng	Chọn hạng phòng phù hợp
6	Chọn loại phòng cụ thể	Chọn phòng đang còn trống
7	Xác nhận	Kiểm tra dữ liệu và hiển thị hóa đơn
8	Lưu booking	Ghi vào lịch sử đặt phòng của tài khoản

Luồng này được mô tả rõ trong phần đặt phòng của source code, trong đó RoomGridAdapter xử lý click chọn phòng, cập nhật trạng thái phòng và tính tổng tiền trực tiếp trên giao diện. Sau khi xác nhận, ứng dụng hiển thị hóa đơn bằng Material AlertDialog và điều hướng về màn hình chính.
7.2 Luồng Xác Thực
Bước	Thành phần	Hành động
1	LoginActivity	Người dùng nhập thông tin -> Gửi POST Request qua Retrofit.
2	Spring Boot Server	Tiếp nhận yêu cầu -> Spring Security xác thực thông tin tài khoản -> Trả về JWT Token.
3	AuthManager	Đọc Token trả về từ Server -> Lưu Token vào SharedPreferences.
4	Các Request tiếp theo	OkHttp Interceptor tự động đính kèm Token vào Header gửi lên Server.

Cơ chế này giúp người dùng không phải đăng nhập lại liên tục và đảm bảo các màn hình đặt phòng, lịch sử chỉ được truy cập khi đã xác thực.
8. PHÂN QUYỀN NGƯỜI DÙNG
Chức năng	USER	ADMIN
Xem danh sách khách sạn	✔	✔
Tìm kiếm khách sạn	✔	✔
Xem chi tiết khách sạn	✔	✔
Đặt phòng	✔	✔
Xem lịch sử đặt phòng	Cá nhân	Toàn bộ
Quản lý khách sạn	✘	✔
Quản lý tiện ích/phòng	✘	✔
Xem dữ liệu người dùng	✘	✔

Trong source hiện tại, trọng tâm vẫn là trải nghiệm người dùng cuối, nhưng dữ liệu mock và phân luồng màn hình đã cho phép mở rộng sang phần quản trị nếu cần phát triển tiếp.
9. KẾT LUẬN
Dự án Xây dựng ứng dụng đặt phòng khách sạn đã được thiết kế và triển khai theo hướng hoàn chỉnh, có đầy đủ các chức năng cốt lõi như hiển thị khách sạn, tìm kiếm thông minh, xem chi tiết, đặt phòng, lưu lịch sử và quản lý phiên người dùng. Ứng dụng thể hiện rõ sự kết hợp giữa giao diện hiện đại, mô hình dữ liệu rõ ràng và các kỹ thuật xử lý như debounce tìm kiếm, lưu trữ JSON bằng Gson, quản lý trạng thái đăng nhập bằng SharedPreferences và giao diện đặt phòng trực quan.
9.1 Kết quả đạt được
• Hoàn thành giao diện chính, chi tiết và đặt phòng. 
• Tìm kiếm khách sạn có tính điểm và lọc nâng cao. 
• Lưu lịch sử đặt phòng theo từng tài khoản. 
• Giao diện hiện đại, thống nhất theo Material Design 3. 
• Tổ chức code theo hướng dễ mở rộng và bảo trì.
9.2 Hướng phát triển tiếp theo
• Tích hợp cổng thanh toán trực tuyến qua bên thứ ba (như Momo, VNPAY, ZaloPay).
• Xây dựng hệ thống thông báo đẩy (Push Notifications) thời gian thực bằng Firebase Cloud Messaging (FCM) khi đặt phòng thành công. 
• Tối ưu ứng dụng bằng cách thêm cơ chế offline caching (sử dụng Room Database làm bộ nhớ đệm cục bộ) khi mất kết nối mạng.

❌ Chưa có Spring Boot
❌ Chưa có REST API
❌ Chưa có Retrofit
❌ Chưa có MySQL
❌ Chưa có JWT
❌ Chưa có Spring Security
❌ Chưa có BCrypt
❌ Chưa có Entity UserEntity, HotelEntity, BookingEntity
❌ Chưa có phân quyền ROLE_USER / ROLE_ADMIN