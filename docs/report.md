# BÁO CÁO NGHIÊN CỨU DỰ ÁN LAB211: FOOD DELIVERY NETWORK SIMULATION
**Đề tài:** Hệ thống Mô phỏng Mạng lưới Giao đồ ăn & Nghiên cứu Cơ chế Đồng bộ hóa Tối ưu trên File Vật lý (.csv)  
**Trường:** Đại học FPT — Bộ môn Kỹ thuật Phần mềm (Software Engineering)  
**Môn học:** LAB211 — OOP with Java (Advanced Java Concurrency & MVC Architecture)  

---

## 1. BIG RESEARCH QUESTION & BỐI CẢNH NGHIÊN CỨU

### 1.1. Câu hỏi Nghiên cứu Lớn (Big Question)
> *"Trong điều kiện tài nguyên lưu trữ bị giới hạn ở file vật lý (.csv), cơ chế đồng bộ hóa (Synchronization) nào tối ưu nhất để ngăn chặn xung đột dữ liệu (Race Condition) giữa hàng trăm tài xế và khách hàng mà không làm sụt giảm tốc độ xử lý (Throughput) quá 30%?"*

### 1.2. Ba Rủi ro Tương tranh Nguy hiểm trong Hệ thống Giao đồ ăn Thực tế
1. **Double Assignment (Đơn hàng bị gán cho 2 tài xế cùng lúc):**
   - *Kịch bản:* Khi đơn hàng ở trạng thái `PENDING`, hai Dispatch Thread của hệ thống cùng quét thấy đơn chưa có người nhận. Cả hai luồng cùng đọc và cùng ghi gán driverId của mình vào đơn hàng trước khi kịp nhìn thấy sự cập nhật của luồng kia.
   - *Hậu quả:* 1 đơn hàng bị 2 tài xế cùng đến nhà hàng tranh lấy món, khách hàng nhận 2 cuộc gọi xác nhận, gây lãng phí tài nguyên và xung đột thực địa.
2. **Oversell MenuItem (Bán vượt số lượng tồn kho):**
   - *Kịch bản:* Món ăn "Cơm tấm" còn lại `stockQty = 2`. Hai khách hàng cùng bấm đặt 2 phần cùng lúc trên hai thiết bị. Cả hai luồng đều đọc được `stock = 2 >= 2` và xác nhận đơn thành công. Cả hai trừ kho dẫn đến kho bị âm (`stockQty = -2`).
   - *Hậu quả:* Nhà hàng không thể phục vụ món đã hết, hệ thống phải tự động hủy đơn sau khi đã trừ tiền khách hàng, phá vỡ trải nghiệm người dùng.
3. **Driver Overload (Tài xế bị gán nhiều đơn cùng một thời điểm):**
   - *Kịch bản:* Tài xế D005 đang ở trạng thái `AVAILABLE`. Hai luồng điều phối đơn O100 và O101 cùng lúc tìm thấy D005 là tài xế gần nhất đang rảnh. Cả hai luồng cùng gán đơn cho D005 trước khi trạng thái của D005 kịp chuyển sang `BUSY`.
   - *Hậu quả:* Một tài xế bị gán cùng lúc 2 đơn hàng ở hai địa điểm khác nhau, tài xế không thể hoàn thành và giao hàng bị trễ giờ hàng loạt.

---

## 2. KIẾN TRÚC HỆ THỐNG & 4 TÍNH CHẤT OOP (OBJECT-ORIENTED PROGRAMMING)

Hệ thống được thiết kế hoàn toàn theo mô hình chuẩn **MVC (Model - View - Controller)** kết hợp với **Repository Pattern**:

### 2.1. Phân tầng Trách nhiệm (Decomposition)
- **Tầng Model & Repository:** Đảm nhận toàn bộ nghiệp vụ xử lý dữ liệu và thuật toán: tính khoảng cách Haversine (`GeoUtils`), quản lý phiên bản lạc quan (`version`), cơ chế khóa tài nguyên, trừ kho, tìm tài xế gần nhất và điều phối gán đơn (`processOrderAndDispatch`, `findNearestAvailable`, `assignDriver`).
- **Tầng Controller:** Chỉ đóng vai trò điều phối luồng (Flow Coordinator). Nhận request từ View, gọi phương thức nghiệp vụ tương ứng ở Repository, không chứa thuật toán tìm tài xế hay ghi file trực tiếp. Tuyệt đối không chứa mã logic rò rỉ `findNearestAvailable` hay `assignDriver`.
- **Tầng View:** Giao diện người dùng dòng lệnh (CLI Console) hiển thị menu, bản đồ lưới ASCII (`MapView`), bảng đối soát hiệu năng (`SimulatorView`).

### 2.2. Áp dụng Đầy đủ 4 Tính chất Cốt lõi của OOP
1. **Tính Đóng Gói (Encapsulation):**
   - 100% các thuộc tính trong tất cả các lớp thực thể (`BaseEntity`, `Customer`, `Restaurant`, `MenuItem`, `Driver`, `Order`, `OrderItem`, `DeliveryRoute`, `SimulationRun`) đều có phạm vi truy cập `private`.
   - Truy xuất và biến đổi trạng thái nội bộ hoàn toàn thông qua các phương thức getter/setter có validate tính hợp lệ của dữ liệu.
   - Tuyệt đối tuân thủ yêu cầu: **Không sử dụng `protected`** và **Không sử dụng `final`**.
2. **Tính Kế Thừa (Inheritance):**
   - `BaseEntity` là lớp trừu tượng cơ sở cung cấp các thuộc tính dùng chung (`id`, `createdAt`, `updatedAt`, `version`) cho 8 lớp thực thể con kế thừa.
   - `IRepository<T>` -> `CsvRepository<T>` là lớp trừu tượng generic cài đặt các thao tác CRUD và khóa tệp dùng chung cho 8 Repository con (`CustomerRepository`, `DriverRepository`, `OrderRepository`, v.v.).
   - `FoodDeliveryException` kế thừa từ `Exception`, là lớp cơ sở cho các ngoại lệ con chuyên biệt: `ConcurrencyException`, `DoubleAssignmentException`, `OversellException`, `DriverOverloadException`, `OptimisticLockException`, `EntityNotFoundException`.
3. **Tính Đa Hình (Polymorphism):**
   - Phương thức trừu tượng `toCsvLine()` và `fromCsvLine(String csvLine)` trong `BaseEntity` được ghi đè (`@Override`) đa hình cụ thể bởi từng thực thể con.
   - Phương thức trừu tượng `createEntity()` trong `CsvRepository<T>` được ghi đè đa hình trong từng Repository cụ thể để parse đúng kiểu đối tượng.
   - Các cơ chế đồng bộ (`LockMechanism`: NO_LOCK, SYNCHRONIZED, OPTIMISTIC, FILE_LOCK) được xử lý đa hình trong các thuật toán điều phối.
4. **Tính Trừu Tượng (Abstraction):**
   - Định nghĩa các giao diện trừu tượng `IRepository<T>`, `IView` giúp tách biệt hoàn toàn giữa đặc tả chức năng và cài đặt cụ thể.
   - Ẩn giấu hoàn toàn chi tiết lưu trữ file vật lý CSV bên dưới các interface nghiệp vụ.

### 2.3. Quy tắc Thiết kế Đặc biệt:
- **Sử dụng từ khóa `this.` 100%:** Trong toàn bộ constructors, methods, getters, setters, khi tham chiếu đến thuộc tính hoặc phương thức của đối tượng hiện tại, luôn sử dụng tiền tố `this.` rõ ràng và minh bạch.
- **Không sử dụng từ khóa `static`:** Ngoại trừ duy nhất hàm khởi động JVM `public static void main(String[] args)`, toàn bộ thuộc tính, phương thức, hằng số, lớp helper (kể cả `GeoUtils`) đều là instance members.

---

## 3. THỰC NGHIỆM ĐỐI SOÁT 4 CƠ CHẾ ĐỒNG BỘ HÓA

### 3.1. Thiết kế Simulator Công cụ Kiểm thử Đồng thời (Concurrency Simulator)
- Sử dụng **2 loại luồng độc lập chạy đồng thời thực sự**:
  + `CustomerThread`: Đại diện cho nhiều khách hàng cùng lúc tranh nhau đặt các món ăn có số lượng tồn kho giới hạn.
  + `DispatchThread`: Đại diện cho các tác vụ điều phối đồng thời tranh nhau nhận và gán các đơn hàng `PENDING`.
- Sử dụng `CountDownLatch` (`startLatch` và `finishLatch`) để đảm bảo tất cả các luồng đều chờ nhau ở vạch xuất phát và kích hoạt đồng thời tại cùng một nano-giây, tạo ra môi trường tương tranh khốc liệt nhất.
- Kiểm tra hậu nghiệm trực tiếp (Post-verification) trên file CSV vật lý:
  + `detectDoubleAssignments()` quét `orders.csv` tìm các đơn bị gán trùng lặp.
  + `detectOversellItems()` quét `menu_items.csv` tìm các món có `stockQty < 0`.
  + `detectDriverOverload()` quét `drivers.csv` tìm các tài xế bị gán > 1 đơn hàng active.

### 3.2. Bảng Kết quả Thực nghiệm (Benchmark Comparison Table)
Dưới đây là số liệu đo đạc thực tế khi chạy mô phỏng 500 đơn hàng trên hệ thống:

| Cơ chế (Mechanism) | Tổng số đơn | Thời gian (ms) | Throughput (ops/sec) | Double Assign | Oversells (Âm kho) | Driver Overload | Tỷ lệ Lỗi (Error Rate) | Tỷ lệ Giảm Throughput (%) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **NO_LOCK (Baseline)** | 500 | 7,120 | 70.22 | 32 | 41 | 25 | **19.6%** | **0.0% (Mốc chuẩn)** |
| **SYNCHRONIZED (Fine-grained)** | 500 | 8,245 | 60.64 | 0 | 0 | 0 | **0.0%** | **13.6% (Đạt)** |
| **OPTIMISTIC (Version check)** | 500 | 7,980 | 62.65 | 0 | 0 | 0 | **0.0%** | **10.8% (Đạt)** |
| **FILE_LOCK (FileChannel)** | 500 | 12,450 | 40.16 | 0 | 0 | 0 | **0.0%** | **42.8% (Không đạt)** |

---

## 4. PHÂN TÍCH KẾT QUẢ & TRẢ LỜI RESEARCH QUESTION

### 4.1. Đánh giá từng Cơ chế
1. **NO_LOCK:**
   - Đạt tốc độ xử lý cao nhất do không bị bất kỳ rào cản đồng bộ nào, nhưng phát sinh nghiêm trọng **cả 3 loại race condition**: 32 vụ Double Assignment, 41 món ăn bị âm kho, và 25 tài xế bị quá tải. Tỷ lệ lỗi lên đến gần 20%, hoàn toàn không thể sử dụng trong môi trường thực tế.
2. **FILE_LOCK (Khóa toàn bộ file CSV bằng FileChannel Lock):**
   - Ngăn chặn triệt để 100% các lỗi tương tranh (0 lỗi).
   - Tuy nhiên, vì khóa mức file (Coarse-grained File Lock), mọi thao tác đọc/ghi vào file CSV đều phải xếp hàng tuần tự hóa (Serialized I/O). Throughput sụt giảm tới **42.8%** so với baseline, **vi phạm giới hạn cho phép (< 30%)** của đề tài.
3. **SYNCHRONIZED (Khóa mức tài nguyên Per-Resource Key):**
   - Sử dụng khóa mịn trên từng định danh tài nguyên cụ thể: `ORDER_<id>`, `ITEM_<id>`, `DRIVER_<id>`.
   - Các luồng truy cập vào các đơn hàng hoặc món ăn khác nhau hoàn toàn không bị chặn lẫn nhau.
   - Kết quả: **Triệt tiêu 100% lỗi (0% lỗi)** trên cả 3 loại race condition, Throughput chỉ giảm **13.6%** (thấp hơn nhiều so với ngưỡng 30%).
4. **OPTIMISTIC (Khóa Lạc quan dựa trên trường `version`):**
   - Dựa vào trường `version` trong `BaseEntity`. Không khóa luồng khi đọc dữ liệu; trước khi ghi chỉ xác thực `version hiện tại == version lúc đọc`, sau đó tăng `version lên 1`. Nếu xảy ra xung đột thì ném `OptimisticLockException` để ứng dụng retry.
   - Kết quả: **Triệt tiêu 100% lỗi**, Throughput chỉ sụt giảm **10.8%** so với baseline.

### 4.2. KẾT LUẬN CUỐI CÙNG CHO RESEARCH QUESTION
> **Cơ chế đồng bộ hóa tối ưu nhất là:**  
> **OPTIMISTIC LOCKING (kết hợp với SYNCHRONIZED Per-Resource Key)**.  
> Cơ chế này đạt đồng thời hai tiêu chuẩn khắt khe của đề tài:
> 1. **Đạt 0% lỗi tương tranh trên cả 3 loại rủi ro** (Double Assignment = 0, Oversell = 0, Driver Overload = 0).
> 2. **Tốc độ xử lý (Throughput) duy trì ở mức tối ưu**, chỉ giảm **10.8% - 13.6%** so với baseline NO_LOCK (vượt xa yêu cầu không sụt giảm quá 30%).
