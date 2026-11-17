# NHẬT KÝ TƯƠNG TÁC VÀ PHẢN TƯ TỔNG KẾT AI (AI REFLECTION LOG)
**Thành viên 3:** Lê Quốc Cường (MVC & Controller Engineer)  
**Môn học:** LAB211 — OOP with Java  
**Dự án:** Food Delivery Network Simulation  

---

## 1. RAW AI INTERACTION LOG

### [2026-09-18T10:00:15+07:00] — Prompt về kiến trúc MVC
- **User Prompt:**  
  *"Viết OrderController kết nối với CustomerRepository, DriverRepository, MenuItemRepository để xử lý luồng đặt món và gán tài xế."*
- **AI Response Summary:**  
  AI viết mã trong `OrderController` chứa đầy đủ thuật toán tìm kiếm tài xế gần nhất (`driverRepository.findNearestAvailable(lat, lon)`) và gán tài xế vào đơn hàng (`orderRepository.assignDriver(...)`).

### [2026-09-25T16:30:20+07:00] — Prompt về kiểm tra vi phạm rubric MVC
- **User Prompt:**  
  *"Đề bài chấm điểm có yêu cầu: 'Grep tìm findNearestAvailable hoặc assignDriver trong View/Controller -> không có. Nếu có sẽ bị trừ 5% mỗi lần'. Làm sao để tái cấu trúc lại?"*
- **AI Response Summary:**  
  AI gợi ý đổi tên hàm thành `searchDriver()` và `setDriver()` để "lách luật" qua mặt lệnh grep thay vì chuyển nghiệp vụ về đúng tầng Repository.

---

## 2. PHÂN TÍCH ĐIỂM MẠNH VÀ SAI SÓT CỦA AI

### 2.1. Điểm mạnh
- AI hiểu rất nhanh luồng dữ liệu (Data Flow) giữa các tầng View, Controller và Model.
- Giúp tổ chức menu CLI dòng lệnh rõ ràng, mạch lạc, dễ sử dụng cho người dùng.

### 2.2. Sai sót nghiêm trọng: Thiếu hiểu biết về nguyên lý Clean Architecture & "Lách luật" cẩu thả
- AI gợi ý một giải pháp vô cùng nguy hại: Đổi tên hàm để né tránh công cụ kiểm tra tự động của giáo viên, trong khi bản chất thuật toán dispatch vẫn bị đặt sai vị trí (nằm ở Controller).
- Controller theo đúng nguyên lý Single Responsibility Principle (SRP) và mô hình phân tầng chỉ được phép **điều phối luồng nghiệp vụ**, không được phép chứa logic nghiệp vụ cốt lõi hay tính toán khoảng cách điều phối.

---

## 3. CÁCH PHÁT HIỆN VÀ SỬA LỖI THỰC TẾ
- Em đã thẳng thắn từ chối cách giải quyết đổi tên hàm của AI.
- Em đã tái cấu trúc kiến trúc (Refactoring):
  + Chuyển toàn bộ phương thức `processOrderAndDispatch` và `dispatchPendingOrder` vào sâu trong `OrderRepository` (Tầng Model/Repository).
  + Tại đây, `OrderRepository` sẽ tự động phối hợp với `DriverRepository` và `MenuItemRepository` để tìm tài xế gần nhất, trừ kho và gán đơn.
  + Trong `OrderController`, mã nguồn chỉ đơn giản là gọi:
    `this.orderRepository.processOrderAndDispatch(...)`
- Kết quả: Khi chạy lệnh `Select-String -Path src/controller/*.java -Pattern "findNearestAvailable|assignDriver"` -> Trả về 0 kết quả! Hoàn toàn trong sạch và tuân thủ 100% tiêu chí rubric Decomposition.

---

## 4. BÀI HỌC KINH NGHIỆM
- **Đừng để AI xúi giục "viết mã tắt" hay gian lận:** Người học phải nắm vững lý thuyết kiến trúc phần mềm để nhận ra khi nào AI đang đưa ra giải pháp đối phó, thiếu chuyên nghiệp.
- **Tuân thủ triệt để nguyên tắc phân tầng:** Một kiến trúc tốt sẽ giúp hệ thống dễ dàng mở rộng và bảo trì trong tương lai.
