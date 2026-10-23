# KẾ HOẠCH PHÂN CÔNG CÁ NHÂN & QUY TRÌNH HỌP GỘP KẾT QUẢ 10 TUẦN (6 THÀNH VIÊN)
**Môn học:** LAB211 — OOP with Java | FPT University
**Đề tài:** Food Delivery Network Simulation
**Số lượng thành viên:** 6 Thành viên (Member 1 -> Member 6)

---

## 📅 TUẦN 1–2: KHỞI TẠO DỰ ÁN, THIẾT KẾ SƠ ĐỒ & BỘ SINH DỮ LIỆU CSV (≥ 18.000 DÒNG)

### 📌 1. Nhiệm vụ cá nhân độc lập (Thứ 2 đến Thứ 6):
- **Member 1 (Leader):**
  - Nghiên cứu 3 rủi ro Data Race (*Double Assignment*, *Oversell*, *Driver Overload*).
  - Khởi tạo Repository Git / Thư mục dự án chuẩn MVC (`src/model`, `src/repository`, `src/controller`, `src/view`, `data`, `docs`, `ai_logs`).
  - Phác thảo UML Class Diagram và Use Case Diagram ban đầu.
- **Member 2:**
  - Viết `DataGenerator.java` lớp nền.
  - Sinh dữ liệu giả lập cho `customers.csv` (2.000 dòng: ID, tên, SĐT, địa chỉ, lat/lon Hà Nội, version=1).
  - Sinh dữ liệu giả lập cho `restaurants.csv` (200 dòng: ID, tên, địa chỉ, lat/lon, rating).
- **Member 3:**
  - Viết logic sinh dữ liệu trong `DataGenerator.java` cho `menu_items.csv` (2.000 món ăn phân bổ cho 200 nhà hàng, `stockQty` từ 50-150, `version=1`).
  - Sinh dữ liệu giả lập cho `drivers.csv` (500 tài xế, status `AVAILABLE`, lat/lon Hà Nội, `currentOrderId=""`, `version=1`).
- **Member 4:**
  - Viết logic sinh dữ liệu trong `DataGenerator.java` cho `orders.csv` (3.000 đơn hàng), `order_items.csv` (9.000 chi tiết đơn), `delivery_routes.csv` (2.000 lộ trình).
  - Đảm bảo tổng số dòng CSV sinh ra ≥ 18.000 dòng.
- **Member 5:**
  - Thiết kế cấu trúc giao diện CLI menu chính (`MainView.java`).
  - Tìm hiểu cách sử dụng `com.sun.net.httpserver.HttpServer` trong Java SE để sẵn sàng dựng Web Backend.
- **Member 6:**
  - Tạo biểu mẫu nhật ký AI Log cho 6 thành viên (`ai_logs/member1_ai_log.md` -> `member6_ai_log.md`).
  - Ghi chép các prompt khởi tạo ban đầu và kiểm tra tính hợp lệ của schema CSV.

### 🤝 2. Lịch họp nhóm & Thảo luận (Thứ 7 - 2 Tiếng):
- **Nội dung họp:**
  1. M1 trình bày thiết kế kiến trúc MVC và giải thích quy tắc 0% penalty (không viết logic dispatch ở View/Controller).
  2. M2, M3, M4 demo chạy thử `DataGenerator.java` trên máy cá nhân.
  3. Kiểm tra kiểm định: Kiểm tra 8 file CSV tạo ra có đủ cột, không bị lỗi dấu phẩy hay xuống dòng sai.

### 🔀 3. Gộp kết quả & Merge Code (Chủ Nhật):
- **Quy trình:** M2, M3, M4 đẩy mã nguồn `DataGenerator.java` lên branch chung. M1 thực hiện code review, merge vào branch `main` và chạy sinh 8 file CSV lưu sẵn vào thư mục `data/`.

---

## 📦 TUẦN 3: XÂY DỰNG TẦNG MODEL LAYER & CÔNG THỨC HAVERSINE

### 📌 1. Nhiệm vụ cá nhân độc lập (Thứ 2 đến Thứ 6):
- **Member 1 (Leader):**
  - Lập trình lớp trừu tượng `BaseEntity.java` (`id`, `createdAt`, `updatedAt`, `version`, `toCsvLine()`, `fromCsvLine()`).
  - Lập trình `Enums.java` (`DriverStatus`, `OrderStatus`, `LockMechanism`).
- **Member 2:**
  - Lập trình 2 lớp Entity: `Customer.java` và `Restaurant.java` kế thừa `BaseEntity`.
  - Viết hàm parse/serialize dòng CSV cho 2 Entity này.
- **Member 3:**
  - Lập trình 2 lớp Entity: `MenuItem.java` và `Driver.java` kế thừa `BaseEntity`.
  - Đảm bảo trường `version` được khởi tạo bằng 1L.
- **Member 4:**
  - Lập trình 3 lớp Entity: `Order.java`, `OrderItem.java`, `DeliveryRoute.java`, `SimulationRun.java`.
- **Member 5:**
  - Viết lớp tiện ích `GeoUtils.java` triển khai công thức Haversine tính khoảng cách giữa 2 tọa độ lat/lon.
- **Member 6:**
  - Viết Unit Test `GeoUtilsTest.java` kiểm thử tính đúng đắn của công thức Haversine (Khoảng cách giữa 2 điểm chuẩn).

### 🤝 2. Lịch họp nhóm & Thảo luận (Thứ 7 - 1.5 Tiếng):
- **Nội dung họp:**
  1. M5 và M6 trình bày kết quả Unit Test công thức Haversine.
  2. M2, M3, M4 demo thử nghiệm parse round-trip: Đọc 1 dòng từ CSV -> Tạo Entity -> Ghi ngược lại CSV (đảm bảo chuỗi ra trùng khớp 100%).

### 🔀 3. Gộp kết quả & Merge Code (Chủ Nhật):
- M1 tổng hợp toàn bộ 8 lớp Entity và `GeoUtils.java` vào gói `src/model/`. Chạy test suite kiểm tra không có lỗi biên dịch.

---

## 🗄️ TUẦN 4: CHECKPOINT THEN CHỐT — REPOSITORY LAYER & 4 CƠ CHẾ ĐỒNG BỘ

### 📌 1. Nhiệm vụ cá nhân độc lập (Thứ 2 đến Thứ 6):
- **Member 1 (Leader):**
  - Lập trình lớp generic `CsvRepository<T>` (`findAll`, `findById`, `save`, `saveAll`, `deleteById`).
  - Triển khai hàm `getKeyLock(key)` dùng `ConcurrentHashMap` và hàm `executeWithFileLock()` dùng `FileChannel.lock()`.
  - Triển khai hàm `updateWithOptimisticLock()` kiểm tra `currentVersion == expectedVersion` và tăng `version++`.
- **Member 2:**
  - Lập trình `MenuItemRepository.java` chứa 4 hàm trừ kho: `deductStockNoLock`, `deductStockSynchronized`, `deductStockOptimistic`, `deductStockFileLock`.
- **Member 3:**
  - Lập trình `DriverRepository.java` chứa hàm `findNearestAvailable()` (dùng `GeoUtils`) và 4 hàm đổi trạng thái: `markBusyNoLock`, `markBusySynchronized`, `markBusyOptimistic`, `markBusyFileLock`.
- **Member 4:**
  - Lập trình `OrderRepository.java` chứa 4 hàm gán tài xế: `assignDriverNoLock`, `assignDriverSynchronized`, `assignDriverOptimistic`, `assignDriverFileLock`.
- **Member 5:**
  - Lập trình 6 lớp Custom Exception trong `src/exception/`: `ConcurrencyException`, `OversellException`, `DoubleAssignmentException`, `DriverOverloadException`, `OptimisticLockException`, `EntityNotFoundException`.
- **Member 6:**
  - Viết script test đa luồng thủ công (5-10 threads) gọi đồng thời vào 3 Repository critical để kiểm thử phát hiện lỗi.

### 🤝 2. Lịch họp nhóm & Thảo luận (Thứ 7 - 2.5 Tiếng - CHECKPOINT QUAN TRỌNG):
- **Nội dung họp:**
  1. Cả nhóm cùng ngồi kiểm thử chéo 3 Repository critical.
  2. Chạy 10 luồng cùng gọi `deductStock` trên món ăn chỉ còn `stockQty = 1`:
     - Khi chạy `NO_LOCK`: Tồn kho bị âm (`stockQty = -9`) -> **ĐẠT (Thấy lỗi race condition)**.
     - Khi chạy `SYNCHRONIZED` hoặc `OPTIMISTIC`: Chỉ 1 luồng thành công, 9 luồng ném `OversellException` / `OptimisticLockException` -> **ĐẠT (Ngăn lỗi thành công)**.

### 🔀 3. Gộp kết quả & Merge Code (Chủ Nhật):
- M1 kiểm tra kỹ lưỡng toàn bộ mã nguồn tầng Repository, đảm bảo không có race window bị bỏ hổng. Merge toàn bộ vào `src/repository/`.

---

## ⚙️ TUẦN 5: TẦNG CONTROLLER LAYER & LUỒNG ĐƠN ĐƠN LUỒNG

### 📌 1. Nhiệm vụ cá nhân độc lập (Thứ 2 đến Thứ 6):
- **Member 1 & Member 3:**
  - Lập trình `OrderController.java` quản lý phương thức `placeOrderAndDispatch(customerId, restaurantId, menuItemId, quantity, mechanism)` điều phối theo đúng thứ tự:
    `MenuItemRepo.deductStock` -> `Save Order` -> `DriverRepo.findNearestAvailable` -> `OrderRepo.assignDriver` -> `DriverRepo.markBusy`.
- **Member 2:**
  - Lập trình `CustomerController.java` và `RestaurantController.java`.
- **Member 4:**
  - Lập trình `DriverController.java`.
- **Member 5:**
  - Chuẩn bị khung lớp `SimulatorController.java`.
- **Member 6:**
  - Viết kịch bản kiểm thử đơn luồng (Single-thread Test Case) đặt 1 đơn hàng từ đầu đến cuối.

### 🤝 2. Lịch họp nhóm & Thảo luận (Thứ 7 - 1.5 Tiếng):
- **Nội dung họp:**
  1. Trình diễn chạy luồng đặt hàng đơn luồng thành công.
  2. M1 kiểm tra mã nguồn Controller: Đảm bảo Controller chỉ đóng vai trò điều phối, gọi đúng method của Repository, không tự tính khoảng cách hay ghi file CSV trực tiếp.

### 🔀 3. Gộp kết quả & Merge Code (Chủ Nhật):
- Merge toàn bộ mã nguồn tầng Controller vào `src/controller/`.

---

## 🖥️ TUẦN 6: TẦNG VIEW LAYER CLI & EMBEDDED WEB SERVER BACKEND

### 📌 1. Nhiệm vụ cá nhân độc lập (Thứ 2 đến Thứ 6):
- **Member 5 (Lead UI):**
  - Lập trình `MapView.java` vẽ bản đồ ASCII Grid 2D 15x45 hiển thị Khách hàng (`C`), Nhà hàng (`R`), Tài xế (`D`).
  - Lập trình `MainView.java` dựng menu CLI tương tác 6 lựa chọn.
- **Member 1:**
  - Lập trình `OrderView.java` hiển thị bảng menu món ăn và bảng danh sách đơn hàng.
- **Member 2:**
  - Lập trình `ReportView.java` in tóm tắt kết quả nghiên cứu.
- **Member 3 & Member 4:**
  - Lập trình `web.WebServer.java` dựng HTTP Server tích hợp sẵn trong Java SE chạy cổng 8080 cung cấp các API REST (`/api/stats`, `/api/map-data`, `/api/simulate`).
- **Member 6:**
  - Tiến hành Code Review toàn bộ dự án đối chiếu với Rubric chấm điểm (Đảm bảo 0% penalty).

### 🤝 2. Lịch họp nhóm & Thảo luận (Thứ 7 - 2 Tiếng):
- **Nội dung họp:**
  1. M5 demo giao diện CLI và bản đồ ASCII Map.
  2. M3 & M4 demo mở trình duyệt web truy cập `http://localhost:8080/api/stats` trả về JSON thành công.

### 🔀 3. Gộp kết quả & Merge Code (Chủ Nhật):
- Merge tầng View và Web Server vào `src/view/` và `src/web/`.

---

## 🛡️ TUẦN 7: KIỂM THỬ THỦ CÔNG 4 CƠ CHẾ ĐỒNG BỘ HÓA

### 📌 1. Nhiệm vụ cá nhân độc lập (Thứ 2 đến Thứ 6):
- **Member 1 & Member 2:**
  - Kiểm thử chuyên sâu cơ chế `OPTIMISTIC` trên 3 điểm nóng (`menu_items.csv`, `drivers.csv`, `orders.csv`).
- **Member 3 & Member 4:**
  - Kiểm thử cơ chế `SYNCHRONIZED` (Per-resource key) và `FILE_LOCK` (`FileChannel`).
- **Member 5:**
  - Xử lý các thông báo lỗi và ngoại lệ hiển thị đẹp mắt trên giao diện CLI/Web khi xảy ra xung đột.
- **Member 6:**
  - Đo đạc thông số thời gian phản hồi (Latency) và lập bảng theo dõi ban đầu.

### 🤝 2. Lịch họp nhóm & Thảo luận (Thứ 7 - 1.5 Tiếng):
- **Nội dung họp:**
  1. Đánh giá độ ổn định của 4 cơ chế khóa.
  2. Thống nhất phương pháp tính thời gian xử lý và tỷ lệ lỗi.

### 🔀 3. Gộp kết quả & Merge Code (Chủ Nhật):
- Cập nhật các bản vá lỗi đồng bộ vào nhánh chính.

---

## ⚡ TUẦN 8: CHECKPOINT ĐỒNG THỜI ĐA LUỒNG — SIMULATOR TOOL & HẬU KIỂM CSV

### 📌 1. Nhiệm vụ cá nhân độc lập (Thứ 2 đến Thứ 6):
- **Member 4 (Lead Sim):**
  - Lập trình `SimulatorController.java` dùng `Executors.newFixedThreadPool` và `CountDownLatch` kích hoạt 1.000 đơn hàng đồng thời thực sự.
- **Member 1:**
  - Viết thuật toán đọc trực tiếp CSV `detectDoubleAssignments()` đếm các `orderId` bị gán cho nhiều tài xế.
- **Member 2:**
  - Viết thuật toán đọc trực tiếp CSV `detectOversellItems()` đếm các món có `stockQty < 0`.
- **Member 3:**
  - Viết thuật toán đọc trực tiếp CSV `detectDriverOverload()` đếm số tài xế nhận > 1 đơn hàng cùng lúc.
- **Member 5:**
  - Viết `SimulatorView.java` in Bảng so sánh 8 cột chuẩn rubric trên CLI và tích hợp giao diện Web Dashboard (`web/index.html`, `app.js`, `style.css`).
- **Member 6:**
  - Chạy thử nghiệm 500-1.000 đơn hàng trên 4 cơ chế khóa, ghi nhận số liệu.

### 🤝 2. Lịch họp nhóm & Thảo luận (Thứ 7 - 2.5 Tiếng - CHECKPOINT QUAN TRỌNG):
- **Nội dung họp:**
  1. Chạy thực nghiệm Simulator Tool ngay tại buổi họp:
     - Cơ chế `NO_LOCK`: Phải xuất hiện cả 3 loại lỗi (`DoubleAssign > 0`, `Oversell > 0`, `DriverOverload > 0`).
     - Cơ chế `OPTIMISTIC` & `SYNCHRONIZED`: Cả 3 loại lỗi phải bằng `0`.
     - Tính Throughput Drop: Optimistic drop ≈ 12.3% (< 30% ngưỡng cho phép).
  2. Kiểm tra format Bảng so sánh 8 cột đúng chuẩn rubric đề bài.

### 🔀 3. Gộp kết quả & Merge Code (Chủ Nhật):
- Merge toàn bộ Simulator Engine và Web Dashboard Frontend vào thư mục gốc dự án.

---

## 📊 TUẦN 9: THỰC NGHIỆM CHÍNH THỨC & VIẾT BÁO CÁO NGHIÊN CỨU (REPORT & SLIDE)

### 📌 1. Nhiệm vụ cá nhân độc lập (Thứ 2 đến Thứ 6):
- **Member 6 (Lead Doc):**
  - Thu thập số liệu thực nghiệm (1.000 orders x 4 mechanisms x 3 lần lấy trung bình).
  - Soạn thảo Báo cáo nghiên cứu khoa học `report.md` / `report.docx` (≥ 15 trang) giải đáp Big Question.
- **Member 1:**
  - Vẽ biểu đồ so sánh Throughput vs Error Rate giữa 4 cơ chế khóa và chèn vào báo cáo.
- **Member 2 & Member 3:**
  - Soạn thảo Slide thuyết trình `slide_content.md` / `slide.pptx` (≥ 8 slides).
- **Member 4:**
  - Kiểm tra tính nhất quán giữa số liệu trong Báo cáo, Slide và kết quả in ra từ Simulator Tool.
- **Member 5:**
  - Hoàn thiện đồ họa biểu đồ Chart.js trên Web Dashboard.

### 🤝 2. Lịch họp nhóm & Thảo luận (Thứ 7 - 2 Tiếng):
- **Nội dung họp:**
  1. M6 trình bày bản thảo Báo cáo Word/MD và Slide thuyết trình.
  2. Cả nhóm góp ý chỉnh sửa nội dung kết luận Big Question và số liệu minh chứng.

### 🔀 3. Gộp kết quả & Merge Code (Chủ Nhật):
- Merge toàn bộ file báo cáo, slide, sơ đồ vào thư mục `docs/`.

---

## 🎓 TUẦN 10: AI REFLECTION, REVIEW TOÀN BỘ & ĐÓNG GÓI ZIP NỘP BÀI

### 📌 1. Nhiệm vụ cá nhân độc lập (Thứ 2 đến Thứ 6):
- **Cả 6 Thành viên (M1 đến M6):**
  - Mỗi người tự hoàn thiện file AI Log cá nhân của mình (`ai_logs/member1_ai_log.md` -> `ai_logs/member6_ai_log.md`).
  - Yêu cầu: Có timestamp thực tế, phân tích sâu lỗi AI (synchronized scope, Haversine formula), bài học kỹ thuật (≥ 500 từ/người).
- **Member 1 & Member 6:**
  - Kiểm tra toàn bộ cấu trúc dự án theo checklist nộp bài.
  - Viết hoàn thiện file `README.md` hướng dẫn compile và khởi chạy.

### 🤝 2. Lịch họp nhóm & Thảo luận (Thứ 7 - 1.5 Tiếng - HỌP TỔNG KẾT):
- **Nội dung họp:**
  1. Kiểm tra 6 file AI Log cá nhân đủ số từ và timestamp thực tế.
  2. Chạy thử nghiệm biên dịch sạch (Clean Compile) từ đầu trên máy mới hoàn toàn:
     ```bash
     javac -d bin -sourcepath src src/Main.java src/DataGenerator.java
     java -cp bin DataGenerator
     java -cp bin Main
     ```
  3. Duyệt duyệt đóng gói file nộp bài.

### 🔀 3. Đóng gói ZIP & Nộp bài (Chủ Nhật):
- Leader (M1) chọn toàn bộ thư mục dự án, nén thành file **`NHOM_01_LAB211_FoodDelivery.zip`** và đại diện nhóm nộp lên hệ thống FPT University Learning trước deadline.
