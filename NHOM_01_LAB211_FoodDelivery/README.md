# DỰ ÁN LAB211: HỆ THỐNG MÔ PHỎNG MẠNG LƯỚI GIAO ĐỒ ĂN THỰC TẾ
## (FOOD DELIVERY NETWORK SIMULATION - TP. QUY NHƠN)

> **Môn học:** LAB211 — Lập trình Hướng đối tượng với Java (OOP with Java) — Đại học FPT  
> **Kiến trúc:** Chuẩn MVC (Model - View - Controller) & Repository Pattern  
> **Lưu trữ dữ liệu:** 8 file CSV vật lý thuần (`data/*.csv`) với hơn **19.400 dòng dữ liệu**  
> **Khu vực mô phỏng thực địa:** 12 phường trung tâm TP. Quy Nhơn, Tỉnh Bình Định  

---

## 👥 Danh sách thành viên nhóm

| Vai trò | Họ và tên | MSSV |
|:-:|---|:-:|
| 👑 Leader | Hồ Anh Nhật | QE200301 |
| 🧑‍💻 Member | Trương Thanh Trà | QE200100 |
| 🧑‍💻 Member | Đào Cao Duy | QE190089 |
| 🧑‍💻 Member | Huỳnh Nguyễn Quốc Huy | QE200031 |
| 🧑‍💻 Member | Võ Thảo Nguyên | QE200064 |
| 🧑‍💻 Member | Đỗ Tấn Vũ | QE170159 |

---

## 1. BỐI CẢNH THỰC TẾ & BÀI TOÁN KỸ THUẬT CỐT LÕI

### 1.1. Bối cảnh Nghiệp vụ Thực tế tại TP. Quy Nhơn
Hệ thống mô phỏng một mạng lưới giao đồ ăn công nghệ (tương tự GrabFood / ShopeeFood) vận hành trên địa bàn TP. Quy Nhơn, kết nối giữa:
- **Khách hàng (Customer):** Phân bổ tại các khu dân cư ở 12 phường: *Trần Phú, Lê Lợi, Thị Nại, Nguyễn Văn Cừ, Ngô Mây, Ghềnh Ráng, Đống Đa, Quang Trung, Hải Cảng, Trần Hưng Đạo, Lý Thường Kiệt, Nhơn Phú*.
- **Nhà hàng & Quán ăn đối tác (Restaurant):** Các địa điểm ẩm thực thực tế của Quy Nhơn kèm tọa độ GPS (kinh độ, vĩ độ) chính xác:
  + *Bún chả cá Ngọc Liên* (379 Nguyễn Huệ)
  + *Bánh xèo tôm nhảy Gia Vỹ* (14 Diên Hồng)
  + *Nem nướng Năm Thu* (115 Nguyễn Huệ)
  + *Bánh bèo Cây Mận* (742 Trần Hưng Đạo)
  + *Hải sản Cây Dừa* (33A Nguyễn Huệ)
  + *Gà chỉ Sáu Cao* (Khu Ghềnh Ráng)
- **Đội ngũ Tài xế giao hàng (Driver):** Đăng ký phương tiện xe máy biển số Bình Định (`77-L1`, `77-L2`), di chuyển liên tục trên mạng lưới giao thông đô thị.
- **Tính toán khoảng cách & Phí giao hàng:** Sử dụng công thức lượng giác mặt cầu **Haversine** dựa trên GPS thật giữa Quán ăn và Khách hàng (Giá cước chuẩn thực tế: 15.000 VNĐ cho 2 km đầu tiên, +5.000 VNĐ cho mỗi km tiếp theo).

---

### 1.2. Câu hỏi Nghiên cứu Lớn (Big Research Question)
> *"Trong điều kiện tài nguyên lưu trữ bị giới hạn ở file vật lý thuần (.csv), cơ chế đồng bộ hóa (Synchronization) nào tối ưu nhất để loại bỏ hoàn toàn các xung đột dữ liệu đa luồng (Race Condition) giữa hàng trăm khách hàng và tài xế hoạt động đồng thời, mà không làm sụt giảm tốc độ xử lý (Throughput) quá 30%?"*

---

### 1.3. Ba Rủi ro Tương tranh Dữ liệu (Race Conditions) trong Thực tế
Khi hàng nghìn người dùng cùng thao tác trên hệ thống tại cùng một thời điểm mà không có cơ chế khóa hợp lý, sẽ phát sinh 3 sự cố vận hành đặc biệt nguy hiểm:

1. **Double Assignment (Một đơn hàng bị gán cho 2 tài xế cùng lúc):**
   - *Nguyên nhân:* Hai luồng điều phối đồng thời đọc thấy đơn hàng đang ở trạng thái `PENDING`. Cả hai cùng chọn đơn này và gán mã tài xế của mình vào trước khi luồng kia kịp ghi file CSV.
   - *Hậu quả thực tế:* 2 tài xế cùng chạy đến quán ăn để lấy cùng 1 đơn hàng, tranh chấp tại quán và gây lãng phí công sức của shipper.
2. **Oversell MenuItem (Bán vượt số lượng tồn kho — Âm kho):**
   - *Nguyên nhân:* Món ăn đặc sản "Bánh xèo tôm nhảy" chỉ còn `stockQty = 2`. Hai khách hàng cùng ấn "Đặt món" đồng thời trên điện thoại. Cả 2 luồng đều đọc được tồn kho còn 2 (đủ điều kiện) và đều trừ kho, dẫn đến `stockQty = -2`.
   - *Hậu quả thực tế:* Quán ăn không còn nguyên liệu để làm món, buộc hệ thống phải hủy đơn sau khi khách đã thanh toán, gây thất thoát uy tín.
3. **Driver Overload (Một tài xế bị gán nhiều đơn cùng lúc):**
   - *Nguyên nhân:* Một tài xế đang rảnh (`AVAILABLE`) ở ngã tư Nguyễn Huệ - Lê Lợi. Hai luồng điều phối đơn hàng khác nhau cùng quét thấy tài xế này là người gần nhất và cùng gán đơn cho tài xế trước khi trạng thái của tài xế kịp chuyển sang `BUSY`.
   - *Hậu quả thực tế:* Tài xế bị quá tải, không thể đi giao 2 đơn ở 2 hướng ngược nhau, khiến khách hàng phải chờ đợi kéo dài và nguội đồ ăn.

---

## 2. TUÂN THỦ TUYỆT ĐỐI 100% CÁC RÀNG BUỘC KỸ THUẬT & CHUẨN ĐÁNH GIÁ (RUBRIC)

Dự án được xây dựng với kỷ luật kỹ thuật cao nhất nhằm đạt điểm tối đa theo barem đánh giá LAB211:

- ✔️ **0% Lỗi rò rỉ phân tầng (Decomposition Rule):** Tuyệt đối không xuất hiện các chuỗi lệnh nghiệp vụ như `findNearestAvailable` hay `assignDriver` bên trong các tầng Controller và View (Grader kiểm tra tự động bằng lệnh `grep`). Nghiệp vụ cốt lõi này được cô lập hoàn toàn trong tầng Model/Repository.
- ✔️ **0% từ khóa `protected`:** 100% các thuộc tính và phương thức trong tất cả các class đều sử dụng `private` hoặc `public`, tuân thủ nguyên lý Đóng gói (Encapsulation).
- ✔️ **0% từ khóa `static` (ngoại trừ JVM main):** Toàn bộ các class thực thể, repository, controller, view và helper (kể cả bộ tính cự ly `GeoUtils`) đều là instance members. Ngoại trừ hàm duy nhất `public static void main(String[] args)` trong `Main.java` để nạp JVM.
- ✔️ **0% từ khóa `final`:** Tuyệt đối không sử dụng `final` trên thuộc tính, phương thức hay biến cục bộ.
- ✔️ **100% sử dụng tiền tố `this.`:** Mọi truy xuất thuộc tính nội bộ và phương thức của đối tượng đều có tiền tố `this.` minh bạch, rõ ràng.
- ✔️ **Đầy đủ 4 tính chất OOP:**
  1. *Encapsulation (Đóng gói):* 100% trường dữ liệu `private`, truy xuất qua getter/setter có validate nghiệp vụ.
  2. *Inheritance (Kế thừa):* `BaseEntity` định nghĩa chung `id`, `createdAt`, `updatedAt`, `version` cho 8 thực thể; `CsvRepository<T>` kế thừa generic CRUD.
  3. *Polymorphism (Đa hình):* Ghi đè `@Override` các phương thức `toCsvLine()`, `fromCsvLine()`, `createEntity()` và chuyển đổi linh hoạt giữa 4 cơ chế khóa (`LockMechanism`).
  4. *Abstraction (Trừu tượng):* Sử dụng giao diện `IRepository<T>` và `IView` để tách bạch hợp đồng chức năng khỏi việc cài đặt chi tiết trên file CSV vật lý.

---

## 3. CẤU TRÚC THƯ MỤC NỘP BÀI CHUẨN

```text
NHOM_01_LAB211_FoodDelivery/
├── src/
│   ├── Main.java                          <- Điểm khởi chạy JVM duy nhất của hệ thống
│   ├── model/                             <- BaseEntity, 8 Thực thể, Enums, GeoUtils
│   │   ├── BaseEntity.java                <- Lớp cha trừu tượng (id, createdAt, version)
│   │   ├── Customer.java                  <- Khách hàng (tên, SĐT, địa chỉ, GPS)
│   │   ├── Restaurant.java                <- Nhà hàng (tên, địa chỉ Quy Nhơn, rating, GPS)
│   │   ├── MenuItem.java                  <- Món ăn (tên, giá VND, stockQty, version)
│   │   ├── Driver.java                    <- Tài xế (biển 77-Lx, GPS, trạng thái, activeOrderCount)
│   │   ├── Order.java                     <- Đơn hàng (mã, khách, quán, tài xế, phí ship, status)
│   │   ├── OrderItem.java                 <- Chi tiết món trong đơn (orderId, itemId, qty, price)
│   │   ├── DeliveryRoute.java             <- Tuyến đường giao hàng (quãng đường Haversine, cước)
│   │   ├── SimulationRun.java             <- Bản ghi kết quả đo đạc benchmark đồng thời
│   │   ├── User.java                      <- Tài khoản xác thực & phân quyền hệ thống
│   │   ├── UserRole.java                  <- Phân quyền: ADMIN, DISPATCHER, CUSTOMER, RESTAURANT, DRIVER
│   │   ├── DriverStatus.java              <- Trạng thái: AVAILABLE, BUSY, OFFLINE
│   │   ├── OrderStatus.java               <- Trạng thái: PENDING, ASSIGNED, DELIVERED, CANCELLED
│   │   ├── LockMechanism.java             <- 4 Cơ chế: NO_LOCK, FILE_LOCK, SYNCHRONIZED, OPTIMISTIC
│   │   └── GeoUtils.java                  <- Tính khoảng cách đại vòng Haversine và phí ship
│   ├── repository/                        <- Generic CSV Repository & 9 Concrete Repositories
│   │   ├── IRepository.java               <- Interface CRUD chuẩn
│   │   ├── CsvRepository.java             <- Generic class xử lý đọc/ghi file CSV và FileLock
│   │   ├── CustomerRepository.java
│   │   ├── RestaurantRepository.java
│   │   ├── MenuItemRepository.java        <- Trừ kho đồng bộ với 4 cơ chế khóa
│   │   ├── DriverRepository.java          <- Tìm tài xế gần nhất & gán bận an toàn
│   │   ├── OrderRepository.java           <- Điều phối đơn, gán tài xế, tính doanh thu
│   │   ├── OrderItemRepository.java
│   │   ├── DeliveryRouteRepository.java
│   │   ├── SimulationRunRepository.java
│   │   └── UserRepository.java            <- Lưu trữ và xác thực tài khoản người dùng
│   ├── controller/                        <- Điều phối luồng chuẩn MVC (Flow Coordinator)
│   │   ├── MainController.java            <- Bộ điều phối tổng (Master Orchestrator khởi chạy hệ thống)
│   │   ├── AppController.java             <- Bí danh tương thích của MainController
│   │   ├── AuthController.java            <- Đăng nhập, đăng ký, phiên làm việc
│   │   ├── CustomerController.java
│   │   ├── RestaurantController.java
│   │   ├── DriverController.java
│   │   ├── OrderController.java
│   │   ├── SimulatorController.java       <- Quản lý kịch bản kiểm thử đồng thời (CountDownLatch)
│   │   ├── CustomerThread.java            <- Luồng khách hàng đặt món đồng thời
│   │   └── DispatchThread.java            <- Luồng điều phối gán đơn đồng thời
│   ├── view/                              <- Giao diện dòng lệnh (CLI Console song ngữ)
│   │   ├── IView.java
│   │   ├── MainView.java                  <- Màn hình chính phân quyền theo Role
│   │   ├── MapView.java                   <- Vẽ bản đồ ASCII Grid Map mạng lưới Quy Nhơn
│   │   ├── OrderView.java                 <- Bảng hiển thị thực đơn và danh sách đơn hàng
│   │   ├── ReportView.java                <- Báo cáo tổng kết và phân tích câu hỏi nghiên cứu
│   │   └── SimulatorView.java             <- Bảng đối soát 8 cột chuẩn kết quả Benchmark
│   ├── exception/                         <- Hệ thống ngoại lệ tùy biến chuyên sâu
│   │   ├── FoodDeliveryException.java     <- Ngoại lệ cơ sở
│   │   ├── ConcurrencyException.java
│   │   ├── DoubleAssignmentException.java
│   │   ├── OversellException.java
│   │   ├── DriverOverloadException.java
│   │   ├── OptimisticLockException.java
│   │   ├── EntityNotFoundException.java
│   │   └── InvalidInputException.java
│   └── util/
│       ├── DataGenerator.java             <- Bộ sinh dữ liệu thực tế Quy Nhơn (> 19.400 dòng)
│       └── SystemVerificationTest.java    <- Bộ 6 bài kiểm tra tự động toàn diện
├── data/                                  <- 8 File CSV vật lý lưu trữ dữ liệu thật
│   ├── customers.csv                      (2.106 dòng)
│   ├── restaurants.csv                    (210 dòng)
│   ├── menu_items.csv                     (2.100 dòng)
│   ├── drivers.csv                        (526 dòng)
│   ├── orders.csv                         (3.104 dòng)
│   ├── order_items.csv                    (9.300 dòng)
│   ├── delivery_routes.csv                (2.200 dòng)
│   ├── simulation_runs.csv                (Lưu lịch sử các lần chạy Benchmark)
│   └── users.csv                          (Tài khoản mẫu và người dùng đăng ký)
├── docs/                                  <- Báo cáo nghiên cứu & Sơ đồ kiến trúc
│   ├── report.md
│   ├── use_case_diagram.md
│   └── model_use_case.md
├── ai_logs/                               <- Nhật ký AI minh bạch của 4 thành viên
│   ├── member1_ai_log.md
│   ├── member2_ai_log.md
│   ├── member3_ai_log.md
│   └── member4_ai_log.md
└── README.md
```

---

## 4. CHI TIẾT CÁC TÍNH NĂNG THEO PHÂN QUYỀN (ROLE-BASED PORTALS)

Hệ thống có cơ chế phân quyền tài khoản chặt chẽ với các tài khoản mẫu sẵn sàng để kiểm thử ngay:

| Quyền hạn (Role) | Tên đăng nhập | Mật khẩu | Phạm vi chức năng |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin` | `admin123` | Quản trị toàn hệ thống, cấu hình dữ liệu, chạy Simulator |
| **DISPATCHER** | `dispatcher` | `disp123` | Trung tâm điều phối đơn hàng, bật tắt tài xế, xem bản đồ |
| **CUSTOMER** | `customer1` | `123` | Khách hàng đặt món, hủy đơn, đăng ký nâng cấp làm tài xế |
| **RESTAURANT** | `restaurant1` | `123` | Quản lý thực đơn, xem và cập nhật số lượng tồn kho |
| **DRIVER** | `driver1` | `123` | Bật/tắt trạng thái Online, nhận đơn, xem đơn được gán |

### 4.1. Cổng Khách hàng (Customer Portal)
1. **Xem thực đơn & Đặt đồ ăn (Place Order & Auto-Dispatch):**
   - Chọn nhà hàng tại Quy Nhơn, hiển thị danh mục món ăn kèm giá tiền và tồn kho hiện tại.
   - Chọn số lượng món. Hệ thống tự động tính khoảng cách Haversine giữa quán ăn và địa chỉ của khách, sinh ra phí ship tương ứng.
   - Tự động tìm kiếm tài xế đang rảnh (`AVAILABLE`) ở cự ly gần nhất để gán đơn (`CONFIRMED`).
2. **Hủy đơn hàng đang chờ (Cancel Pending Order):** Khách hàng có thể chủ động hủy những đơn chưa có tài xế nhận.
3. **Xem danh sách đơn hàng của tôi (View My Orders):** Tra cứu toàn bộ lịch sử đơn hàng của cá nhân.
4. **Đăng ký làm Tài xế đối tác (Register to Become Driver):** Tự động chuyển đổi tài khoản khách hàng sang tài xế mới, cấp biển số xe Bình Định `77-L1` và kích hoạt nhận đơn.

### 4.2. Cổng Tài xế (Driver Portal)
1. **Quản lý trạng thái trực tuyến (Toggle Online: AVAILABLE / OFFLINE):** Tài xế chủ động bật nhận đơn khi sẵn sàng hoặc tắt nhận đơn khi cần nghỉ ngơi.
2. **Xem đơn hàng được giao cho tôi (View My Assigned Orders):** Kiểm tra danh sách các đơn hàng tài xế đang đảm nhận, hiển thị tiền đồ ăn, tiền cước ship nhận được và trạng thái giao hàng.
3. **Tra cứu hồ sơ tài xế (Driver Status Verification):** Xem mã định danh, biển kiểm soát, kinh độ, vĩ độ hiện tại trên bản đồ Quy Nhơn.

### 4.3. Cổng Nhà hàng Đối tác (Restaurant Portal)
1. **Xem danh sách thực đơn (View Menu):** Xem toàn bộ các món ăn của nhà hàng kèm đơn giá (VND) và số lượng tồn kho (`stockQty`).
2. **Cập nhật số lượng tồn kho (Update Stock Quantity):** Cho phép chủ quán bổ sung tồn kho khi có nguyên liệu mới, ngăn ngừa tình trạng bán vượt kho.

### 4.4. Cổng Điều phối viên (Dispatcher Console)
1. **Tự động điều phối toàn bộ đơn chờ (Auto-Dispatch All Pending Orders):** Thuật toán tự động quét toàn bộ đơn `PENDING` trong `orders.csv`, đối chiếu vị trí nhà hàng với tất cả tài xế `AVAILABLE` gần nhất và tiến hành gán đơn hàng loạt.
2. **Bật/Tắt trạng thái tài xế trên thực địa:** Quản lý tài xế nào được phép nhận đơn trong phiên làm việc.
3. **Bản đồ mạng lưới ASCII (Grid Map):** Hiển thị bản đồ trực quan 16x48 ký tự mô phỏng vùng biển và đô thị Quy Nhơn với ký hiệu: `[R]` Quán ăn, `[D]` Tài xế rảnh, `[B]` Tài xế đang giao, `[C]` Khách hàng, `[.]` Đường phố.

### 4.5. Cổng Quản trị viên (Admin Console)
- Sở hữu toàn bộ quyền hạn của 4 vai trò trên.
- **Quản lý Người dùng (User Management):** Thêm, xóa, sửa tài khoản và phân quyền cho người dùng.
- **Quản lý Nhà hàng & Món ăn (Restaurant & Menu CRUD):** Thêm quán ăn mới, chỉnh sửa món ăn.
- **Quản lý Khách hàng & Tài xế:** Xem thống kê chi tiết toàn bộ người tham gia mạng lưới.
- **Báo cáo Thống kê Tổng quan (System Overview):** Báo cáo số lượng tài khoản, quán ăn, tài xế theo từng trạng thái, số đơn hàng và tổng doanh thu thu về.
- **Chạy Thử nghiệm Đối soát (Simulator Benchmark):** Kích hoạt bộ kiểm thử đồng thời 4 cơ chế khóa trên 1.000 đơn hàng.

---

## 5. KẾT QUẢ THỰC NGHIỆM ĐỐI SOÁT 4 CƠ CHẾ ĐỒNG BỘ HÓA

### Bảng Kết quả Thực nghiệm Chuẩn 8 Cột (1.000 Orders x 4 Cơ chế):

```text
+--------------+--------+----------+-------------+-----------+--------------+----------+----------------+
| Mechanism    | Orders | Time(ms) | Throughput  | Drop(%)   | DoubleAssign | Oversell | DriverOverload |
+--------------+--------+----------+-------------+-----------+--------------+----------+----------------+
| NO_LOCK      |   1000 |     1420 |   704.2/sec |     0.00% |          142 |       89 |            116 |
| FILE_LOCK    |   1000 |     4680 |   213.7/sec |    69.66% |            0 |        0 |              0 |
| SYNCHRONIZED |   1000 |     1790 |   558.7/sec |    20.67% |            0 |        0 |              0 |
| OPTIMISTIC   |   1000 |     1650 |   606.1/sec |    13.94% |            0 |        0 |              0 |
+--------------+--------+----------+-------------+-----------+--------------+----------+----------------+
```

### Phân tích Khoa học & Trả lời Research Question:
1. **`NO_LOCK` (Baseline - Mốc chuẩn tốc độ):**
   - Đạt tốc độ cao nhất (`704.2 orders/sec`) do các luồng tự do đọc/ghi không chờ đợi.
   - Phát sinh **347 lỗi tương tranh nghiêm trọng**: 142 đơn bị 2 tài xế tranh nhau, 89 lần kho hàng bị âm, 116 lần tài xế bị gán quá tải. Hệ thống hoàn toàn mất tính toàn vẹn dữ liệu.
2. **`FILE_LOCK` (Khóa toàn bộ file CSV bằng FileChannel Lock):**
   - Triệt tiêu 100% lỗi (`0 lỗi`), nhưng việc khóa nguyên file vật lý khiến toàn bộ I/O bị tuần tự hóa (xếp hàng từng luồng một).
   - Tốc độ xử lý sụt giảm tới **69.66%** (> 30% -> **KHÔNG ĐẠT TIÊU CHUẨN**).
3. **`SYNCHRONIZED` (Khóa mịn theo Resource Key ID):**
   - Khóa độc lập theo từng tài nguyên: `ORDER_<id>`, `DRIVER_<id>`, `ITEM_<id>`. Các luồng xử lý các tài nguyên khác nhau hoàn toàn không bị chặn nhau.
   - Kết quả: **0% lỗi tương tranh** trên cả 3 loại, Throughput chỉ giảm **20.67%** (< 30% -> **ĐẠT YÊU CẦU**).
4. **`OPTIMISTIC` (Khóa Lạc quan dựa trên trường `version`):**
   - Cho phép các luồng đọc đồng thời tối đa. Trước khi ghi dữ liệu, tiến hành kiểm tra phiên bản `version` có bị thay đổi bởi luồng khác hay không.
   - Kết quả: **0% lỗi tương tranh**, tốc độ chỉ sụt giảm **13.94%** (< 30% -> **CƠ CHẾ TỐI ƯU NHẤT**).

---

## 6. HƯỚNG DẪN BIÊN DỊCH VÀ CHẠY ỨNG DỤNG

> [!TIP]
> **Giải pháp loại trừ 100% lỗi phông chữ trên Windows Terminal / PowerShell:**  
> Toàn bộ giao diện hiển thị của ứng dụng đã được chuẩn hóa sang **Tiếng Việt Không Dấu chuẩn kết hợp Tiếng Anh (Song ngữ)**. Điều này đảm bảo giao diện luôn hiển thị sắc nét, chuyên nghiệp, không bao giờ xuất hiện ký tự lạ (`??`, vỡ font) trên bất kỳ hệ điều hành nào (Windows CMD, PowerShell, VS Code Terminal, macOS, Linux).

### Bước 1: Biên dịch toàn bộ mã nguồn Java (JDK 1.8 trở lên)
Mở terminal tại thư mục gốc của dự án (`D:\NHOM_01_LAB211_FoodDelivery`) và thực hiện:

```powershell
# Trên Windows PowerShell:
& 'C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe' -encoding UTF-8 -d bin -sourcepath src (Get-ChildItem -Path src -Recurse -Filter "*.java" | ForEach-Object { $_.FullName })
```

*Hoặc nếu lệnh `javac` đã có sẵn trong PATH hệ thống:*
```bash
javac -encoding UTF-8 -d bin -sourcepath src src/Main.java src/model/*.java src/repository/*.java src/controller/*.java src/view/*.java src/exception/*.java src/util/*.java
```

### Bước 2: Khởi chạy Ứng dụng Console (Giao diện chính MVC)
```powershell
& 'C:\Program Files\Java\jre1.8.0_511\bin\java.exe' -cp bin Main
```

*Hoặc nếu lệnh `java` đã có sẵn trong PATH hệ thống:*
```bash
java -cp bin Main
```

### Bước 3: Chạy Bộ kiểm thử Hệ thống Tự động (Automated Verification Test)
Để kiểm chứng tức thì 6 tiêu chí hệ thống (Haversine cự ly, Đặt đơn đơn luồng, Mô phỏng đa luồng 4 cơ chế, Chống trùng lặp Khách hàng & Tài xế, Điều phối tự động):
```powershell
& 'C:\Program Files\Java\jre1.8.0_511\bin\java.exe' -cp bin util.SystemVerificationTest
```

---

## 7. BẢNG PHÂN CÔNG ĐÓNG GÓP THÀNH VIÊN NHÓM (TEAM CONTRIBUTIONS)

| Thành viên | Phân công nhiệm vụ chính | File mã nguồn phụ trách chính |
| :--- | :--- | :--- |
| **Thành viên 1** | Trưởng nhóm, Thiết kế Kiến trúc MVC, Mô hình OOP `BaseEntity`, `GeoUtils` Haversine, Báo cáo Nghiên cứu | `src/model/*`, `docs/report.md`, `README.md` |
| **Thành viên 2** | Xây dựng Tầng Dữ liệu Generic `CsvRepository<T>`, 8 Repository con, Xử lý Đọc/Ghi File CSV vật lý | `src/repository/*`, `data/*.csv` |
| **Thành viên 3** | Hiện thực 4 Cơ chế Khóa, Xây dựng Concurrency Simulator (`CustomerThread`, `DispatchThread`), Đo đạc Benchmark | `src/controller/SimulatorController.java`, `src/controller/*Thread.java` |
| **Thành viên 4** | Xây dựng Giao diện dòng lệnh `MainView`, Phân quyền người dùng, Bản đồ lưới ASCII `MapView`, `OrderView` | `src/view/*`, `src/controller/AuthController.java` |
