# LỊCH TRÌNH THỰC HIỆN DỰ ÁN 10 TUẦN — DÀNH CHO NHÓM 6 THÀNH VIÊN
**Môn học:** LAB211 — OOP with Java | FPT University
**Đề tài:** Food Delivery Network Simulation (Hệ thống Mô phỏng Mạng lưới Giao đồ ăn)
**Kiến trúc:** MVC (Model - View - Controller), Concurrency Simulation, Physical CSV Persistence

---

## 👥 1. PHÂN CÔNG VAI TRÒ 6 THÀNH VIÊN (TEAM ROLES & RESPONSIBILITIES)

| Vai trò | Ký hiệu | Thành viên phụ trách | Nhiệm vụ chính |
| :--- | :---: | :--- | :--- |
| **Team Leader / Core Architect** | **M1** | Member 1 | Quản lý tiến độ, thiết kế tổng thể kiến trúc MVC, lớp cơ sở `BaseEntity`, `GeoUtils` (Haversine), generic `CsvRepository<T>`, Code Review & Merge. |
| **Data Engine & Repository Lead** | **M2** | Member 2 | Xây dựng bộ sinh 18.000+ dòng dữ liệu `DataGenerator.java`, `CustomerRepository`, `RestaurantRepository`, `MenuItemRepository` (trừ kho & khóa). |
| **Concurrency & Dispatch Lead** | **M3** | Member 3 | Xây dựng `DriverRepository` (tìm tài xế gần nhất & mark busy), `OrderRepository` (gán đơn & khóa), hệ thống Custom Exceptions đồng bộ. |
| **Simulator & Verification Lead** | **M4** | Member 4 | Xây dựng `SimulatorController` đa luồng (`CustomerThread` + `DispatchThread` qua `CountDownLatch`), các thuật toán hậu kiểm trực tiếp file CSV. |
| **UI/UX & Web Dashboard Developer** | **M5** | Member 5 | Xây dựng giao diện CLI (`MainView`, `MapView` ASCII grid, `SimulatorView` 8 cột), Embedded `WebServer` backend và Web GUI Dashboard (HTML5/Canvas/Chart.js). |
| **Technical Documentation & QA Lead** | **M6** | Member 6 | Đánh giá Benchmark dữ liệu, viết Báo cáo nghiên cứu khoa học `report.md` (15+ trang), Slide thuyết trình `slide_content.md`, sơ đồ UML, tổng hợp AI Logs & đóng gói ZIP. |

---

## 📅 2. LỊCH TRÌNH CHI TIẾT THEO TUẦN (10-WEEK MASTER ROADMAP)

### 🚀 TUẦN 1–2: Phân Tích Yêu Cầu, Thiết Kế Sơ Đồ & Khởi Tạo Data Generator
- **Mục tiêu:** Phân tích 3 rủi ro Data Race, chốt schema 8 file CSV, thiết kế sơ đồ Class/Use Case và sinh thành công ≥ 18.000 dòng dữ liệu.
- **Phân công việc:**
  - **M1:** Phân tích 3 race condition (*Double Assignment*, *Oversell*, *Driver Overload*). Vẽ UML Class Diagram và Use Case Diagram.
  - **M2:** Viết `DataGenerator.java` sinh dữ liệu CSV cho `customers.csv` (2.000 dòng), `restaurants.csv` (200 dòng), `menu_items.csv` (2.000 dòng).
  - **M3:** Sinh dữ liệu CSV cho `drivers.csv` (500 dòng), `orders.csv` (3.000 dòng), `order_items.csv` (9.000 dòng), `delivery_routes.csv` (2.000 dòng).
  - **M4:** Kiểm thử round-trip parse/serialize CSV và đảm bảo tổng số dòng ≥ 18.000 dòng.
  - **M5:** Khởi tạo cấu trúc dự án Java tuân thủ MVC (`src/model`, `src/repository`, `src/controller`, `src/view`).
  - **M6:** Khởi tạo nhật ký tương tác AI Log (`ai_logs/member1_ai_log.md` -> `member6_ai_log.md`) và lập danh mục mốc kiểm soát.
- **Sản phẩm nộp Tuần 2 (Deliverables):**
  - Class Diagram, Use Case Diagram.
  - 8 file CSV trong `data/` với tổng số dòng = 18.700 dòng.

---

### 📦 TUẦN 3: Xây Dựng Tầng Model Layer & GeoUtils
- **Mục tiêu:** Xây dựng hoàn chỉnh các lớp Entity kế thừa từ `BaseEntity` và công thức tính khoảng cách mặt cầu Haversine.
- **Phân công việc:**
  - **M1:** Viết lớp `BaseEntity` (`id`, `createdAt`, `updatedAt`, `version`) và định nghĩa `Enums.java`.
  - **M2:** Lập trình các lớp Entity: `Customer.java`, `Restaurant.java`, `MenuItem.java`.
  - **M3:** Lập trình các lớp Entity: `Driver.java`, `Order.java`, `OrderItem.java`.
  - **M4:** Lập trình các lớp Entity: `DeliveryRoute.java`, `SimulationRun.java`.
  - **M5:** Viết lớp `GeoUtils.java` triển khai công thức Haversine tính khoảng cách tọa độ (lat/lon).
  - **M6:** Viết Unit Test kiểm thử công thức Haversine (Test khoảng cách Hà Nội - TP.HCM ≈ 1.700 km).
- **Sản phẩm nộp Tuần 3:**
  - Tất cả các lớp Model + Unit Test `GeoUtilsTest` pass 100%.

---

### 🗄️ TUẦN 4 (CHECKPOINT THEN CHỐT): Tầng Repository & 4 Cơ Chế Đồng Bộ
- **Mục tiêu:** Xây dựng `CsvRepository<T>` generic và 3 Repository critical (`MenuItemRepository`, `DriverRepository`, `OrderRepository`) ngăn chặn đúng 3 rủi ro Data Race trước khi viết Simulator.
- **Phân công việc:**
  - **M1:** Viết lớp generic `CsvRepository<T>` với các hàm CRUD và quản lý khóa `ConcurrentHashMap keyLocks`, `FileChannel.lock()`.
  - **M2:** Lập trình `MenuItemRepository` với 4 cơ chế trừ kho (`deductStockNoLock`, `deductStockSynchronized`, `deductStockOptimistic`, `deductStockFileLock`).
  - **M3:** Lập trình `DriverRepository` với hàm `findNearestAvailable` (dùng Haversine) và 4 cơ chế đổi trạng thái `markBusy`.
  - **M4:** Lập trình `OrderRepository` với 4 cơ chế gán tài xế `assignDriver`.
  - **M5:** Viết các lớp Custom Exceptions: `OversellException`, `DoubleAssignmentException`, `DriverOverloadException`, `OptimisticLockException`.
  - **M6:** Chạy thử nghiệm thủ công 5-10 threads kiểm thử 3 Repository critical (Đảm bảo khi chạy `SYNCHRONIZED` và `OPTIMISTIC` chỉ có 1 thread thành công, 9 thread ném exception đúng loại).
- **Sản phẩm nộp Tuần 4:**
  - 3 Repository critical hoạt động đúng 4 cơ chế khóa.

---

### ⚙️ TUẦN 5: Tầng Controller Layer & Luồng Đơn Đơn Luồng
- **Mục tiêu:** Xây dựng tầng Controller điều phối nghiệp vụ đơn luồng từ đặt hàng đến gán tài xế.
- **Phân công việc:**
  - **M1 & M3:** Lập trình `OrderController.java` điều phối quy trình `placeOrderAndDispatch` (trừ kho -> tạo order -> tìm tài xế -> gán đơn -> mark busy).
  - **M2:** Lập trình `CustomerController.java` và `RestaurantController.java`.
  - **M4:** Lập trình `DriverController.java`.
  - **M5:** Xây dựng lớp `SimulatorController.java` khung sườn.
  - **M6:** Viết bài kiểm thử đơn luồng (Single-thread Test Case) đảm bảo full flow: Đặt hàng -> Xác nhận -> Dispatch -> Delivered thành công.
- **Sản phẩm nộp Tuần 5:**
  - Full order flow single-thread test case pass.

---

### 🖥️ TUẦN 6: Tầng View Layer CLI & Web Dashboard Backend
- **Mục tiêu:** Hoàn thiện giao diện dòng lệnh CLI và tích hợp Web Backend Server.
- **Phân công việc:**
  - **M5 (Lead):** Viết `MapView.java` (render bản đồ ASCII grid 2D) và `MainView.java` (Menu CLI tương tác).
  - **M1:** Viết `OrderView.java` (hiển thị menu nhà hàng & danh sách đơn hàng).
  - **M2:** Viết `ReportView.java` (in tóm tắt kết quả nghiên cứu).
  - **M3 & M4:** Viết `web.WebServer` (HttpServer backend tích hợp sẵn trong Java SE chạy trên cổng 8080).
  - **M6:** Code review toàn bộ dự án: Đảm bảo logic dispatch/trừ kho **không xuất hiện trong View hoặc Controller** (0% penalty rule).
- **Sản phẩm nộp Tuần 6:**
  - Demo toàn bộ flow trên CLI và Web Backend Server cổng 8080 khởi chạy thành công.

---

### 🛡️ TUẦN 7: Hoàn Thiện 4 Cơ Chế Đồng Bộ Hóa & Kiểm Thử Tải
- **Mục tiêu:** Thử nghiệm độc lập từng cơ chế khóa trên 3 điểm nóng (`menu_items.csv`, `drivers.csv`, `orders.csv`).
- **Phân công việc:**
  - **M1 & M2:** Kiểm thử chuyên sâu cơ chế `OPTIMISTIC` với trường `version`.
  - **M3:** Kiểm thử cơ chế `SYNCHRONIZED` per-resource key.
  - **M4:** Kiểm thử cơ chế `FILE_LOCK` trên `FileChannel`.
  - **M5:** Khóa giao diện UI và thông báo trạng thái lỗi khi phát hiện xung đột.
  - **M6:** Ghi chép dữ liệu số đo thời gian (Duration ms) và lỗi của từng cơ chế.
- **Sản phẩm nộp Tuần 7:**
  - 3 cơ chế đồng bộ chạy ổn định, ném đúng ngoại lệ khi xảy ra xung đột.

---

### ⚡ TUẦN 8 (CHECKPOINT ĐỒNG THỜI ĐA LUỒNG): Simulator Tool & Hậu Kiểm CSV
- **Mục tiêu:** Xây dựng Simulator Tool điều phối 2 loại thread (`CustomerThread` + `DispatchThread`) chạy đồng thời thực sự bằng `CountDownLatch` và các hàm quét hậu kiểm CSV.
- **Phân công việc:**
  - **M4 (Lead):** Hoàn thiện `SimulatorController.java` sử dụng `ExecutorService` và `CountDownLatch` kích hoạt 1.000 đơn hàng đồng thời.
  - **M3:** Viết hàm `detectDoubleAssignments()` quét trực tiếp file `orders.csv`.
  - **M2:** Viết hàm `detectOversellItems()` quét trực tiếp file `menu_items.csv`.
  - **M1:** Viết hàm `detectDriverOverload()` quét trực tiếp file `drivers.csv`.
  - **M5:** Viết `SimulatorView.java` render Bảng so sánh 8 cột chuẩn rubric.
  - **M6:** Chạy thử nghiệm 500 orders x 4 mechanisms, kiểm tra kết quả: `NO_LOCK` phải xuất hiện đủ 3 loại lỗi, `OPTIMISTIC` và `SYNCHRONIZED` phải đạt 0% lỗi.
- **Sản phẩm nộp Tuần 8:**
  - Bảng so sánh 8 cột in chuẩn format trên CLI và Web GUI Dashboard.

---

### 📊 TUẦN 9: Chạy Thực Nghiệm Full & Viết Báo Cáo Nghiên Cứu
- **Mục tiêu:** Chạy thực nghiệm 1.000 orders x 4 mechanisms x 3 lần (lấy trung bình), vẽ biểu đồ và hoàn thiện Báo cáo Word/Slide.
- **Phân công việc:**
  - **M6 (Lead):** Tổng hợp số liệu thực nghiệm, viết báo cáo nghiên cứu khoa học `report.md` / `report.docx` (≥ 15 trang) trả lời Big Question.
  - **M1:** Vẽ biểu đồ so sánh Throughput vs Error Rate giữa 4 cơ chế.
  - **M2 & M3:** Soạn thảo Slide thuyết trình `slide_content.md` / `slide.pptx` (≥ 8 slides).
  - **M4:** Kiểm tra công thức tính % Throughput drop so với baseline `NO_LOCK` (Đảm bảo Optimistic drop < 30%).
  - **M5:** Hoàn thiện giao diện Web Dashboard Frontend (`web/index.html`, `app.js`, `style.css` với biểu đồ Chart.js).
- **Sản phẩm nộp Tuần 9:**
  - File Báo cáo Word/MD + Slide thuyết trình + Số liệu thực nghiệm chứng minh Big Question.

---

### 🎓 TUẦN 10: AI Reflection, Review Toàn Bộ & Đóng Gói ZIP Nộp Bài
- **Mục tiêu:** Hoàn thiện nhật ký AI Log cá nhân (30% tổng điểm), review mã nguồn lần cuối và đóng gói ZIP.
- **Phân công việc:**
  - **Tất cả (M1 - M6):** Mỗi thành viên tự hoàn thiện file AI Log cá nhân (`ai_logs/member1_ai_log.md` -> `member6_ai_log.md`) ghi lại timestamp thực tế, phân tích lỗi sai của AI (synchronized scope, Haversine formula) và bài học kỹ thuật (≥ 500 từ/người).
  - **M1 & M6:** Review toàn bộ mã nguồn lần cuối: Đảm bảo project compile không lỗi, file `README.md` có hướng dẫn chi tiết.
  - **M1:** Nén toàn bộ thư mục thành file `NHOM_01_LAB211_FoodDelivery.zip` đúng cấu trúc quy định và nộp qua hệ thống FPT Learning.
- **Sản phẩm nộp Tuần 10:**
  - File ZIP nộp bài hoàn chỉnh 100%.

---

## 📊 3. MA TRẬN PHÂN CÔNG CÔNG VIỆC (RACI MATRIX)

- **R (Responsible):** Người thực thi chính.
- **A (Accountable):** Người chịu trách nhiệm phê duyệt (Leader M1).
- **C (Consulted):** Người hỗ trợ tư vấn kỹ thuật.
- **I (Informed):** Người nhận thông báo kết quả.

| Đội ngũ / Hạng mục | M1 (Leader) | M2 (Data) | M3 (Dispatch) | M4 (Sim) | M5 (UI/Web) | M6 (Doc/QA) |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| **Phân tích Yêu cầu & Use Case / Class Diagram** | **A/R** | C | C | C | I | R |
| **Sinh 18.000+ dòng dữ liệu (`DataGenerator.java`)** | A | **R** | R | C | I | I |
| **Lập trình Model & GeoUtils (Haversine)** | **A/R** | R | R | R | R | C |
| **Lập trình 3 Repository critical (4 Lock Mechanisms)** | **A** | **R** | **R** | C | I | C |
| **Lập trình OrderController & Flow đơn luồng** | **A** | C | **R** | R | I | I |
| **Lập trình View CLI (ASCII Map & 8-column Table)** | A | I | I | C | **R** | I |
| **Lập trình Simulator Engine (`CountDownLatch`)** | A | C | C | **R** | I | I |
| **Lập trình Các hàm Hậu kiểm Quét trực tiếp CSV** | A | R | R | **R** | I | C |
| **Lập trình Web Dashboard (WebServer & Web GUI)** | A | I | I | C | **R** | I |
| **Viết Báo cáo Nghiên cứu & Slide Thuyết trình** | A | C | C | C | C | **R** |
| **Hoàn thiện AI Reflection Logs & Đóng gói ZIP** | **A/R** | **R** | **R** | **R** | **R** | **R** |
