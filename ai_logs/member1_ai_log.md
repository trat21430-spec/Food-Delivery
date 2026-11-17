# AI LOG & AI REFLECTION (CÁ NHÂN) — LAB211 FOOD DELIVERY SIMULATION

## Phần 1: Nhật ký tương tác AI (Có Timestamp thực tế)

### [2026-10-27 20:15:10] Prompt 1 — Thiết kế công thức Haversine trong `GeoUtils.java`
- **Prompt:** "Viết hàm tính khoảng cách Haversine giữa 2 tọa độ GPS (lat, lon) bằng Java cho hệ thống giao đồ ăn."
- **AI Output ban đầu:** AI trả về công thức nhưng quên đổi `lat1` và `lat2` sang Radian khi nhân `Math.cos(lat1) * Math.cos(lat2)` (chỉ đổi `dLat` và `dLon`).
- **Phát hiện & Cách sửa:** Khi chạy Unit Test khoảng cách Hà Nội (21.0285, 105.8542) -> TP.HCM (10.8231, 106.6297), kết quả ra lệch hơn 400km so với thực tế (~1,138km đường chim bay / ~1,700km đường bộ). Đã sửa lại bằng `Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))`.

### [2026-10-27 21:05:42] Prompt 2 — Đồng bộ hóa `assignDriverWithSync` trong `OrderRepository.java`
- **Prompt:** "Làm sao để chặn 2 tài xế nhận cùng 1 đơn hàng trên file orders.csv mà không làm chậm các đơn khác?"
- **AI Output ban đầu:** AI đặt từ khóa `public synchronized void assignDriver(...)` ở mức method (tương đương `synchronized(this)`).
- **Phát hiện & Cách sửa:** Khi dùng `synchronized(this)`, toàn bộ instance `OrderRepository` bị khóa cho mọi đơn hàng khác nhau (giống hệt khóa toàn cục), làm throughput giảm hơn 55%. Đã sửa lại bằng `ConcurrentHashMap<String, Object> resourceLocks` để chỉ khóa trên key `"ORDER_" + orderId`, kết hợp khóa ngắn trên `fileMonitor` khi flush xuống đĩa.

### [2026-10-27 21:48:19] Prompt 3 — Thiết kế `SimulatorController` với `CountDownLatch`
- **Prompt:** "Viết Simulator chạy 1000 đơn hàng để test Race Condition."
- **AI Output ban đầu:** AI chỉ tạo 1 vòng lặp submit `DispatchThread` tuần tự mà không dùng `CountDownLatch` và không tách riêng `CustomerThread` (đặt món trừ kho) với `DispatchThread` (gán tài xế).
- **Phát hiện & Cách sửa:** Nếu không có `CountDownLatch(1)` làm cổng xuất phát (`startGate.await()`), các thread tạo trước chạy xong trước khi thread sau kịp khởi tạo, dẫn đến `NO_LOCK` không tái hiện đủ cả 3 lỗi. Đã bổ sung `startGate` và `doneGate`, đồng thời cho `CustomerThread` và `DispatchThread` xuất phát cùng một mili-giây.

## Phần 2: Bài học rút ra (AI Reflection)
1. **AI hỗ trợ rất tốt phần Boilerplate:** Sinh nhanh cấu trúc `BaseEntity`, `toCsvLine()`, `fromCsvLine()` và bảng hiển thị ASCII.
2. **Điểm yếu chí mạng của AI trong lập trình đa luồng (Concurrent Programming):** AI thường xuyên nhầm lẫn giữa **khóa toàn cục (`synchronized(this)`)** và **khóa theo tài nguyên (`per-resource lock`)**, hoặc tạo ra **Race Window** giữa bước đọc (`findById`) và bước ghi (`saveAll`) khi thao tác với file vật lý `.csv`. Nếu không tự kiểm chứng bằng `CountDownLatch` và các hàm hậu kiểm đọc trực tiếp từ CSV (`detectDoubleAssignments`, `detectOversellItems`, `detectDriverOverload`), lập trình viên rất dễ bị đánh lừa rằng code đã an toàn.
