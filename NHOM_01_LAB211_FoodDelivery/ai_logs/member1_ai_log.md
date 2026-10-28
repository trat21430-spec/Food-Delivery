# NHẬT KÝ TƯƠNG TÁC VÀ PHẢN TƯ TỔNG KẾT AI (AI REFLECTION LOG)
**Thành viên 1:** Nguyễn Văn An (Nhóm trưởng / Concurrency Architect)  
**Môn học:** LAB211 — OOP with Java  
**Dự án:** Food Delivery Network Simulation  

---

## 1. RAW AI INTERACTION LOG (Lịch sử tương tác thô kèm Timestamp thực)

### [2026-09-08T09:15:22+07:00] — Prompt khởi tạo kiến trúc
- **User Prompt:**  
  *"Em đang làm đề tài LAB211 mô phỏng giao đồ ăn đọc ghi file CSV với nhiều luồng Customer và Driver. Hãy gợi ý cho em cách thiết kế BaseEntity và CsvRepository generic kế thừa."*
- **AI Response Summary:**  
  AI sinh ra cấu trúc `BaseEntity` với các trường `protected String id`, `protected LocalDateTime createdAt`, và lớp `CsvRepository<T>` sử dụng các phương thức `synchronized(this)` trên toàn bộ hàm đọc ghi.

### [2026-09-15T14:30:10+07:00] — Prompt về công thức tính khoảng cách Haversine
- **User Prompt:**  
  *"Viết cho em hàm tính khoảng cách giữa 2 tọa độ GPS bằng công thức Haversine trong Java."*
- **AI Response Summary:**  
  AI cung cấp một phương thức `public static double distance(...)` nhưng quên đổi góc sang Radian ở phép tính $\Delta \lambda$, khiến cho khoảng cách giữa Hà Nội và TP.HCM ra hơn 8000 km.

### [2026-09-22T10:45:18+07:00] — Prompt về xử lý đồng bộ hóa tránh Double Assignment
- **User Prompt:**  
  *"Làm thế nào để ngăn 2 tài xế cùng nhận một đơn hàng khi cả hai cùng quét file orders.csv đồng thời? Hãy viết mã cho OrderRepository."*
- **AI Response Summary:**  
  AI đề xuất dùng `synchronized (this)` bao quanh toàn bộ hàm `assignDriver`.

### [2026-10-02T16:20:45+07:00] — Prompt về kiểm thử đồng thời với CountDownLatch
- **User Prompt:**  
  *"Viết Simulator chạy 1000 đơn hàng đồng thời để so sánh tốc độ và số lỗi của NO_LOCK và OPTIMISTIC."*
- **AI Response Summary:**  
  AI viết một vòng lặp `for` tạo `Thread` thông thường gọi `start()` trực tiếp mà không dùng `CountDownLatch`, dẫn đến các luồng chạy tuần tự chứ không tương tranh thực sự.

---

## 2. PHÂN TÍCH SÂU VỀ ĐIỂM MẠNH VÀ SAI SÓT CỦA AI TRONG LẬP TRÌNH ĐỒNG THỜI

### 2.1. AI Đã Giúp Được Những Gì? (Strengths)
AI thể hiện năng lực rất tốt trong việc sinh mã nguồn mang tính cấu trúc lặp đi lặp lại (boilerplate code):
- Sinh các lớp thực thể Entity với đầy đủ getters, setters và constructor.
- Viết các đoạn mã phân tách chuỗi CSV (CSV parsing với regex xử lý dấu phẩy bên trong dấu ngoặc kép: `, (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)`).
- Phác thảo cấu trúc khung của Repository Pattern và các Custom Exceptions.
Nhờ AI, thời gian dựng khung xương dự án ở Tuần 1 và Tuần 2 giảm được khoảng 60%.

### 2.2. Các Lỗi Sai Nghiêm Trọng và Hallucination Cụ Thể của AI (Weaknesses & Flaws)
Trong quá trình lập trình hệ thống tương tranh đa luồng (Concurrent Programming), AI bộc lộ các lỗi tư duy rất nguy hiểm:
1. **Lỗi phạm vi đồng bộ hóa sai lầm (`synchronized(this)` thay vì per-resource key):**
   - AI đề xuất dùng `synchronized(this)` trong `OrderRepository`. Điều này biến toàn bộ repository thành một nút thắt cổ chai đơn luồng (Bottleneck). Khi Tài xế A nhận Đơn 1, Tài xế B muốn nhận Đơn 2 hoàn toàn không liên quan cũng bị khóa chờ.
   - *Hậu quả:* Throughput của hệ thống bị sụt giảm hơn 80%, vi phạm trực tiếp tiêu chuẩn nghiên cứu (không sụt giảm quá 30%).
2. **Lỗi cửa sổ tương tranh vô hình (Race Window Blindness):**
   - Trong luồng trừ tồn kho `deductStock`, AI viết: đọc món từ CSV -> kiểm tra `stock >= qty` -> ghi món mới vào CSV. AI không nhận thức được khoảng trễ vài mili-giây giữa bước kiểm tra và bước ghi là "cửa sổ race condition" chết người. Khi không có khóa mịn, hàng chục luồng cùng thấy `stock = 2` và cùng trừ dẫn đến số lượng tồn kho bị âm.
3. **Lỗi ảo giác công thức toán học (Haversine formula bug):**
   - AI quên gọi `Math.toRadians()` cho tọa độ kinh độ $\Delta \lambda$, khiến khoảng cách giữa Hà Nội ($21.0285, 105.8542$) và TP.HCM ($10.8231, 106.6297$) tính ra sai lệch hoàn toàn so với khoảng cách địa lý thực nghiệm (~1138 km).
4. **Vi phạm kiến trúc phân tầng MVC (Architecture Leaks):**
   - Khi được yêu cầu viết `OrderController`, AI tự tiện gọi `driverRepository.findNearestAvailable()` và `orderRepository.assignDriver()` ngay trong thân Controller, vi phạm nghiêm trọng quy định đề tài: *"Logic dispatch tài xế và gán đơn hàng phải nằm trong Repository layer"*.

---

## 3. CÁCH PHÁT HIỆN, DEBUG VÀ SỬA LỖI THỰC TẾ

1. **Phát hiện lỗi Haversine:**
   - Em đã viết một lớp kiểm thử độc lập `SystemVerificationTest.java` với tọa độ đối chuẩn đã biết trước (Hà Nội -> TP.HCM). Khi kết quả in ra ~8500 km, em lập tức đối chiếu từng dòng toán học với công thức Haversine chuẩn quốc tế và bổ sung `Math.toRadians()`.
2. **Khắc phục nút thắt cổ chai `synchronized`:**
   - Thay vì khóa trên toàn bộ instance `this`, em đã chuyển sang cơ chế **Khóa mịn theo tài nguyên (Fine-grained per-resource locking)** sử dụng `ConcurrentHashMap<String, Object> keyLocks`. Khóa theo từng mã cụ thể: `ORDER_<id>`, `ITEM_<id>`, `DRIVER_<id>`. Nhờ đó các giao dịch trên các đơn hàng độc lập diễn ra song song 100%, Throughput tăng vọt trở lại và chỉ giảm 13% so với baseline.
3. **Sửa lỗi Simulator đồng thời bằng `CountDownLatch`:**
   - Em đã tách Simulator thành 2 loại luồng rõ rệt: `CustomerThread` và `DispatchThread`. Cả hai nhóm luồng đều được neo lại bởi `startLatch.await()`. Khi tất cả các luồng đã sẵn sàng trong bộ nhớ, chỉ với một lệnh `startLatch.countDown()`, hàng trăm luồng cùng lao vào file CSV tại cùng một thời điểm, làm bộc lộ đầy đủ 3 loại race condition trong chế độ `NO_LOCK`.

---

## 4. BÀI HỌC VỀ PROMPT ENGINEERING VÀ CRITICAL THINKING

1. **Không bao giờ tin tưởng mù quáng vào mã nguồn đồng thời do AI sinh ra:**
   - AI chỉ là mô hình xác suất từ ngữ, không có khả năng thực sự "chạy thử" và cảm nhận race condition trong bộ nhớ hay trên ổ đĩa. Mọi đoạn code liên quan đến đa luồng bắt buộc phải được người lập trình kiểm chứng bằng kiểm thử áp lực (Stress Test) và đọc trực tiếp dữ liệu thô (Post-verification).
2. **Kỹ năng Prompting có cấu trúc (Structured Prompting):**
   - Muốn AI sinh đúng kiến trúc, cần cung cấp rõ các ràng buộc bất biến (Invariants) ngay trong prompt: *"Không dùng static, không dùng protected, không dùng final, tuân thủ MVC tuyệt đối, logic dispatch chỉ nằm trong Repository"*.
3. **Tầm quan trọng của tư duy phản biện (Critical Thinking):**
   - Lập trình viên không phải là người copy code từ AI, mà là một kiến trúc sư kiểm soát chất lượng, biết đặt câu hỏi: *"Nếu 100 luồng cùng vào dòng lệnh này tại cùng mili-giây, điều gì sẽ xảy ra?"*. Dự án này đã giúp em trưởng thành vượt bậc về kỹ năng lập trình hệ thống phân tán và xử lý tương tranh trong Java.
