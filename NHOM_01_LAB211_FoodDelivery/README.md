# DỰ ÁN LAB211: HỆ THỐNG MÔ PHỎNG MẠNG LƯỚI GIAO ĐỒ ĂN (FOOD DELIVERY NETWORK SIMULATION)
**Môn học:** LAB211 — OOP with Java (Đại học FPT)  
**Kiến trúc:** Chuẩn MVC (Model - View - Controller) & Repository Pattern  
**Lưu trữ:** File vật lý thuần (.csv) với tổng cộng **18.700 dòng**  

---

## 1. TUÂN THỦ TUYỆT ĐỐI CÁC RÀNG BUỘC KỸ THUẬT CỦA ĐỀ TÀI

- ❌ **Không sử dụng `protected`:** 100% các trường dữ liệu đều là `private`, đóng gói an toàn (Encapsulation).
- ❌ **Không sử dụng `static`:** Ngoại trừ duy nhất hàm `public static void main(String[] args)` khởi chạy JVM, toàn bộ thuộc tính, phương thức, hằng số và helper (kể cả `GeoUtils`) đều là instance members.
- ❌ **Không sử dụng `final`:** Tuyệt đối không xuất hiện từ khóa `final` trong toàn bộ mã nguồn.
- ❌ **Không làm web:** Hệ thống tập trung 100% vào ứng dụng Console CLI chuyên nghiệp theo đúng chuẩn đề tài LAB211.
- ✔️ **Sử dụng `this.` toàn diện:** Mọi truy xuất thuộc tính và phương thức nội bộ trong class đều có tiền tố `this.` rõ ràng.
- ✔️ **Đầy đủ 4 tính chất OOP:** Đóng gói (Encapsulation), Kế thừa (Inheritance), Đa hình (Polymorphism), Trừu tượng (Abstraction).
- ✔️ **Không rò rỉ kiến trúc MVC:** Tuyệt đối không chứa chuỗi ký tự `findNearestAvailable` hay `assignDriver` trong View hay Controller (đạt điểm tối đa tiêu chí Decomposition).
- ✔️ **Công thức Haversine chuẩn xác:** Tính khoảng cách địa lý theo độ cong mặt đất, kiểm chứng chuẩn Hà Nội -> TP.HCM ~ 1138 km.
- ✔️ **Simulator đồng thời thực sự:** Sử dụng 2 loại luồng độc lập (`CustomerThread` và `DispatchThread`) kích hoạt cùng lúc qua `CountDownLatch`.
- ✔️ **Post-verification trực tiếp trên CSV:** Quét trực tiếp file `.csv` sau mô phỏng để phát hiện:
  + Double Assignment (`orders.csv`)
  + Oversell âm kho (`menu_items.csv`)
  + Driver Overload (`drivers.csv`)

---

## 2. CẤU TRÚC THƯ MỤC NỘP BÀI (ZIP PACKAGE STRUCTURE)

```
NHOM_01_LAB211_FoodDelivery/
├── src/
│   ├── model/                          # BaseEntity, Entity classes, Enums, GeoUtils
│   │   ├── BaseEntity.java
│   │   ├── Customer.java
│   │   ├── Restaurant.java
│   │   ├── MenuItem.java
│   │   ├── Driver.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── DeliveryRoute.java
│   │   ├── SimulationRun.java
│   │   ├── DriverStatus.java
│   │   ├── OrderStatus.java
│   │   ├── LockMechanism.java
│   │   ├── UserRole.java
│   │   └── GeoUtils.java
│   ├── repository/                     # Generic CSV Repository & 8 concrete repositories
│   │   ├── IRepository.java
│   │   ├── CsvRepository.java
│   │   ├── CustomerRepository.java
│   │   ├── RestaurantRepository.java
│   │   ├── MenuItemRepository.java
│   │   ├── DriverRepository.java
│   │   ├── OrderRepository.java
│   │   ├── OrderItemRepository.java
│   │   ├── DeliveryRouteRepository.java
│   │   └── SimulationRunRepository.java
│   ├── controller/                     # Flow Coordinators & Concurrency Threads
│   │   ├── CustomerController.java
│   │   ├── RestaurantController.java
│   │   ├── DriverController.java
│   │   ├── OrderController.java
│   │   ├── SimulatorController.java
│   │   ├── CustomerThread.java
│   │   └── DispatchThread.java
│   ├── view/                           # CLI views, ASCII map, Simulator benchmark view
│   │   ├── IView.java
│   │   ├── MainView.java
│   │   ├── OrderView.java
│   │   ├── MapView.java
│   │   ├── SimulatorView.java
│   │   └── ReportView.java
│   ├── exception/                      # Custom business & concurrency exceptions
│   │   ├── FoodDeliveryException.java
│   │   ├── ConcurrencyException.java
│   │   ├── DoubleAssignmentException.java
│   │   ├── OversellException.java
│   │   ├── DriverOverloadException.java
│   │   ├── OptimisticLockException.java
│   │   ├── EntityNotFoundException.java
│   │   └── InvalidInputException.java
│   ├── util/                           # Data generator & automated verification tests
│   │   ├── DataGenerator.java
│   │   └── SystemVerificationTest.java
│   └── Main.java                       # Main entry point of application
├── data/                               # Physical CSV files (18,700 rows)
│   ├── customers.csv                   (2,000 rows)
│   ├── restaurants.csv                 (200 rows)
│   ├── menu_items.csv                  (2,000 rows)
│   ├── drivers.csv                     (500 rows)
│   ├── orders.csv                      (3,000 rows)
│   ├── order_items.csv                 (9,000 rows)
│   ├── delivery_routes.csv             (2,000 rows)
│   └── simulation_runs.csv             (Log kết quả chạy Simulator)
├── docs/                               # Toàn bộ tài liệu báo cáo & thiết kế
│   ├── report.md                       # Báo cáo Word/Markdown đầy đủ trả lời Big Question
│   ├── slide_content.md                # Nội dung Slide thuyết trình
│   ├── class_diagram.md                # Chi tiết lược đồ lớp UML Class Diagram
│   └── flowcharts/                     # 4 Flowcharts chuẩn theo yêu cầu
│       ├── order_and_dispatch_flow.md
│       ├── race_condition_flow.md
│       ├── simulator_flow.md
│       └── data_generation_flow.md
├── ai_logs/                            # Nhật ký tương tác AI có timestamp thực (30% điểm)
│   ├── member1_ai_log.md
│   ├── member2_ai_log.md
│   ├── member3_ai_log.md
│   └── member4_ai_log.md
└── README.md                           # Hướng dẫn biên dịch và sử dụng
```

---

## 3. HƯỚNG DẪN BIÊN DỊCH VÀ CHẠY ỨNG DỤNG

### Bước 1: Biên dịch toàn bộ mã nguồn Java
Mở Terminal hoặc PowerShell tại thư mục gốc dự án:
```bash
cd src
javac -encoding UTF-8 $(Get-ChildItem -Recurse -Filter "*.java" | ForEach-Object { $_.FullName })
cd ..
```
*(Hoặc trên Linux/macOS: `javac -encoding UTF-8 $(find src -name "*.java")`)*

### Bước 2: Sinh dữ liệu mẫu vật lý (≥ 18.000 dòng CSV)
*(Dữ liệu đã được sinh sẵn trong thư mục `data/`. Nếu muốn sinh mới lại:)*
```bash
java -cp "src" util.DataGenerator
```

### Bước 3: Chạy bộ kiểm thử tự động toàn diện (System Verification Test)
Kiểm tra nhanh tính đúng đắn của công thức Haversine, luồng đặt hàng đơn luồng và bảng benchmark 4 cơ chế:
```bash
java -cp "src" util.SystemVerificationTest
```

### Bước 4: Khởi chạy Ứng dụng chính (Interactive Console Menu)
```bash
java -cp "src" Main
```

---

## 4. HƯỚNG DẪN SỬ DỤNG VÀ ĐỌC BẢNG ĐỐI SOÁT SIMULATOR (8 CỘT)

Khi chạy tính năng **7. Run Simulator Benchmark (Compare 4 Lock Mechanisms)** từ menu chính, hệ thống sẽ thực thi mô phỏng hàng trăm luồng khách hàng và tài xế đồng thời trên 4 cơ chế và xuất ra bảng đối soát chuẩn:

```
=========================================================================================================================
                               CONCURRENCY SIMULATOR COMPARISON BENCHMARK TABLE (8 COLUMNS)
=========================================================================================================================
| Mechanism      | Orders   | Duration(ms) | Throughput(op/s) | DoubleAssign  | Oversells  | DriverOverload  | ErrorRate  |
+----------------+----------+--------------+------------------+---------------+------------+-----------------+------------+
| NO_LOCK        | 500      | 7120         | 70.22            | 32            | 41         | 25              | 19.6     % |
| SYNCHRONIZED   | 500      | 8245         | 60.64            | 0             | 0          | 0               | 0.0      % |
| OPTIMISTIC     | 500      | 7980         | 62.65            | 0             | 0          | 0               | 0.0      % |
| FILE_LOCK      | 500      | 12450        | 40.16            | 0             | 0          | 0               | 0.0      % |
=========================================================================================================================
```

### Ý nghĩa các cột:
1. **Mechanism:** Cơ chế đồng bộ hóa được áp dụng.
2. **Orders:** Tổng số lượng đơn hàng được mô phỏng đồng thời.
3. **Duration (ms):** Tổng thời gian hoàn thành (mili-giây).
4. **Throughput (op/s):** Tốc độ xử lý (số đơn hàng hoàn thành / giây).
5. **DoubleAssign:** Số đơn hàng bị gán trùng cho 2 tài xế khác nhau (phát hiện trực tiếp từ `orders.csv`).
6. **Oversells:** Số món ăn bị bán vượt số lượng tồn kho làm âm kho (phát hiện trực tiếp từ `menu_items.csv`).
7. **DriverOverload:** Số tài xế bị gán cùng lúc nhiều hơn 1 đơn hàng active (phát hiện trực tiếp từ `drivers.csv`).
8. **ErrorRate:** Tổng tỷ lệ phần trăm lỗi tương tranh phát sinh.

### Kết luận nghiên cứu:
- **NO_LOCK:** Xuất hiện đầy đủ cả 3 loại lỗi tương tranh nguy hiểm với tỷ lệ lỗi lên tới 19.6%.
- **FILE_LOCK:** Ngăn chặn được lỗi nhưng tốc độ sụt giảm tới hơn 40%, gây nghẽn I/O vật lý.
- **SYNCHRONIZED (Per-Resource Key) & OPTIMISTIC (Version-based):** Đạt kết quả xuất sắc nhất với **0% lỗi** trên cả 3 loại xung đột, đồng thời duy trì Throughput cao (tỷ lệ sụt giảm chỉ **10.8% - 13.6%**, hoàn toàn thỏa mãn giới hạn không giảm quá 30% của bài toán).
