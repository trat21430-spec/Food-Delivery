# LƯỢC ĐỒ TUẦN TỰ (SEQUENCE DIAGRAMS) - HỆ THỐNG GIAO ĐỒ ĂN LAB211

Tài liệu này cung cấp 2 biểu đồ tuần tự (Sequence Diagram) chuẩn UML (vẽ bằng Mermaid) thể hiện 2 luồng hoạt động cốt lõi nhất của dự án:
1. **Lược đồ 1:** Luồng Đặt hàng và Điều phối tài xế thực tế (Order & Dispatch Flow).
2. **Lược đồ 2:** Luồng Mô phỏng đồng thời và Đối soát lỗi dữ liệu (Concurrent Simulator & Post-Verification Flow).

---

## 1. SEQUENCE DIAGRAM: LUỒNG ĐẶT HÀNG & ĐIỀU PHỐI TÀI XẾ (ORDER & DISPATCH)

Sơ đồ này thể hiện sự tương tác chuẩn theo mô hình MVC và Generic Repository Pattern:
- `OrderView` chỉ nhận input và hiển thị kết quả.
- `OrderController` điều phối luồng gọi hàm từ `OrderRepository`.
- `OrderRepository` đảm nhiệm toàn bộ nghiệp vụ điều phối (Decomposition: tuyệt đối không rò rỉ `findNearestAvailable` lên View/Controller).
- Các Repository thực hiện trừ kho, tính khoảng cách Haversine, gán tài xế theo cơ chế khóa và lưu trữ xuống file `.csv`.

```mermaid
sequenceDiagram
    autonumber
    actor Customer as Khách hàng (User)
    participant View as OrderView
    participant Ctrl as OrderController
    participant OrderRepo as OrderRepository
    participant MenuRepo as MenuItemRepository
    participant RestRepo as RestaurantRepository
    participant DriverRepo as DriverRepository
    participant ItemRepo as OrderItemRepository
    participant CSV as File CSV (Disk)

    Customer->>View: 1. Nhập thông tin (customerId, restaurantId, menuItemId, quantity)
    activate View
    View->>Ctrl: 2. placeOrderAndDispatch(...)
    activate Ctrl
    Ctrl->>OrderRepo: 3. processOrderAndDispatch(customerId, restId, itemId, qty, mechanism, ...)
    activate OrderRepo

    %% Bước 1: Trừ kho
    Note over OrderRepo,MenuRepo: Bước 1: Kiểm tra tồn kho & Trừ kho (Tránh Oversell)
    OrderRepo->>MenuRepo: 4. deductStock(menuItemId, quantity, mechanism)
    activate MenuRepo
    alt Hết hàng (Không đủ tồn kho)
        MenuRepo-->>OrderRepo: false
        OrderRepo-->>Ctrl: throw OversellException
        Ctrl-->>View: Báo lỗi hết món ăn
        View-->>Customer: Hiển thị thông báo thất bại
    else Còn hàng
        MenuRepo->>CSV: Cập nhật stockQty mới vào menu_items.csv
        MenuRepo-->>OrderRepo: true
    end
    deactivate MenuRepo

    %% Bước 2: Tạo đơn PENDING
    Note over OrderRepo,ItemRepo: Bước 2: Khởi tạo Order (PENDING) & OrderItem
    OrderRepo->>CSV: 5. Lưu Order mới (status: PENDING, driverId: "") vào orders.csv
    OrderRepo->>ItemRepo: 6. save(OrderItem)
    activate ItemRepo
    ItemRepo->>CSV: Ghi thông tin chi tiết món vào order_items.csv
    ItemRepo-->>OrderRepo: Hoàn tất lưu OrderItem
    deactivate ItemRepo

    %% Bước 3: Tìm tài xế gần nhất bằng Haversine
    Note over OrderRepo,DriverRepo: Bước 3: Tìm tài xế AVAILABLE gần nhà hàng nhất (Haversine)
    OrderRepo->>RestRepo: 7. findById(restaurantId)
    activate RestRepo
    RestRepo-->>OrderRepo: return Restaurant (lat, lon)
    deactivate RestRepo

    OrderRepo->>DriverRepo: 8. findNearestAvailable(restLat, restLon)
    activate DriverRepo
    DriverRepo->>DriverRepo: Duyệt danh sách Driver AVAILABLE,<br/>tính khoảng cách Haversine nhỏ nhất
    DriverRepo-->>OrderRepo: return Driver gần nhất
    deactivate DriverRepo

    %% Bước 4: Gán tài xế & Cập nhật trạng thái
    Note over OrderRepo,DriverRepo: Bước 4: Khóa & Gán tài xế (Tránh Double Assignment & Overload)
    OrderRepo->>OrderRepo: 9. assignDriver(orderId, driverId, mechanism)
    OrderRepo->>CSV: Cập nhật orders.csv (status: CONFIRMED, driverId)
    
    OrderRepo->>DriverRepo: 10. markBusy(driverId, orderId, mechanism)
    activate DriverRepo
    DriverRepo->>CSV: Cập nhật drivers.csv (status: BUSY, currentOrderId)
    DriverRepo-->>OrderRepo: Xác nhận tài xế đã nhận đơn
    deactivate DriverRepo

    %% Trả kết quả
    OrderRepo-->>Ctrl: 11. return Order hoàn tất (CONFIRMED)
    deactivate OrderRepo
    Ctrl-->>View: 12. Trả Order cho View hiển thị
    deactivate Ctrl
    View-->>Customer: 13. Hiển thị thông tin đơn hàng & Tài xế phục vụ thành công
    deactivate View
```

---

## 2. SEQUENCE DIAGRAM: BỘ MÔ PHỎNG ĐỒNG THỜI & ĐỐI SOÁT CSV (SIMULATOR & POST-VERIFICATION)

Sơ đồ này mô tả cơ chế hoạt động của công cụ Simulator:
- Khởi chạy đồng thời $N$ luồng `CustomerThread` và $M$ luồng `DispatchThread`.
- Kích hoạt đồng thời thực sự qua `CountDownLatch` (`startLatch.countDown()`).
- Thu thập số liệu thời gian thực hiện, Throughput (ops/sec).
- Thực hiện quét đối soát trực tiếp (Post-verification) trên 3 file CSV vật lý để phát hiện các lỗi Race Condition (Double Assignment, Oversell, Driver Overload).

```mermaid
sequenceDiagram
    autonumber
    actor User as Giảng viên / Người dùng
    participant SimView as SimulatorView
    participant SimCtrl as SimulatorController
    participant Latch as CountDownLatch (start/finish)
    participant CustTh as N x CustomerThread
    participant DispTh as M x DispatchThread
    participant OrderCtrl as OrderController
    participant CSV as Physical CSV Files
    participant RepoLog as SimulationRunRepository

    User->>SimView: 1. Chọn số lượng đơn & Cơ chế khóa (NO_LOCK, SYNCHRONIZED, ...)
    activate SimView
    SimView->>SimCtrl: 2. runSimulation(totalOrders, mechanism)
    activate SimCtrl

    %% Thiết lập hạ tầng Concurrency
    Note over SimCtrl,Latch: Khởi tạo CountDownLatch: startLatch(1), finishLatch(N + M)
    SimCtrl->>Latch: 3. new CountDownLatch(1) & CountDownLatch(N)
    
    %% Tạo luồng Customer
    loop Khởi tạo N CustomerThreads
        SimCtrl->>CustTh: 4. new CustomerThread(startLatch, finishLatch, orderCtrl, ...)
        SimCtrl->>CustTh: 5. ct.start() (Luồng rơi vào trạng thái chờ tại startLatch.await())
    end

    %% Kích hoạt ĐUA LUỒNG thực sự
    Note over SimCtrl,DispTh: BẮT ĐẦU ĐUA LUỒNG (TRUE CONCURRENCY)
    SimCtrl->>Latch: 6. startLatch.countDown() (Bắn phát súng lệnh xuất phát)
    
    par Luồng Customer Thread 1
        CustTh->>OrderCtrl: 7. placeOrderAndDispatch(...)
        OrderCtrl->>CSV: Đọc và ghi menu_items.csv
        CustTh->>Latch: 8. finishLatch.countDown()
    and Luồng Customer Thread 2 (Tranh mua cùng món)
        CustTh->>OrderCtrl: 7. placeOrderAndDispatch(...)
        OrderCtrl->>CSV: Ghi đè stockQty dẫn đến âm kho (Oversell)
        CustTh->>Latch: 8. finishLatch.countDown()
    end

    SimCtrl->>Latch: 9. finishLatch.await() (Chờ toàn bộ khách đặt xong)

    %% Kích hoạt Dispatch luồng tài xế tranh giành đơn
    loop Khởi tạo M DispatchThreads tranh chấp cùng 1 tập Order
        SimCtrl->>DispTh: 10. new DispatchThread(...)
        SimCtrl->>DispTh: 11. dt.start()
    end
    SimCtrl->>Latch: 12. dispatchStartLatch.countDown()
    par Luồng Dispatcher Thread 1
        DispTh->>OrderCtrl: 13. dispatchPending(orderId, mechanism)
        OrderCtrl->>CSV: Ghi Driver 1 vào orders.csv
        DispTh->>Latch: 14. dispatchFinishLatch.countDown()
    and Luồng Dispatcher Thread 2 (Tranh gán cùng đơn)
        DispTh->>OrderCtrl: 13. dispatchPending(orderId, mechanism)
        OrderCtrl->>CSV: Ghi đè Driver 2 vào orders.csv (Double Assignment)
        DispTh->>Latch: 14. dispatchFinishLatch.countDown()
    end
    SimCtrl->>Latch: 15. dispatchFinishLatch.await()

    %% Đo đạc hiệu năng
    Note over SimCtrl: Đo durationMs = System.currentTimeMillis() - startTime<br/>Tính Throughput = (totalOrders * 1000.0) / durationMs

    %% Quét Post-Verification trực tiếp file vật lý
    Note over SimCtrl,CSV: POST-VERIFICATION (ĐỐI SOÁT TRỰC TIẾP TRÊN FILE CSV)
    SimCtrl->>CSV: 16. detectDoubleAssignments() -> Quét orders.csv tìm đơn có >1 tài xế
    SimCtrl->>CSV: 17. detectOversellItems() -> Quét menu_items.csv tìm món stockQty < 0
    SimCtrl->>CSV: 18. detectDriverOverload() -> Quét drivers.csv tìm tài xế gán >1 đơn cùng lúc
    
    SimCtrl->>RepoLog: 19. save(SimulationRun) ghi lịch sử vào simulation_runs.csv
    SimCtrl-->>SimView: 20. Trả về kết quả đối soát SimulationRun
    deactivate SimCtrl

    SimView-->>User: 21. Xuất bảng so sánh Benchmark 8 cột trực quan
    deactivate SimView
```
