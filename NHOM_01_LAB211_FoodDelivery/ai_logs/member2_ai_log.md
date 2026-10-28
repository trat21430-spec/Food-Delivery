# NHẬT KÝ TƯƠNG TÁC VÀ PHẢN TƯ TỔNG KẾT AI (AI REFLECTION LOG)
**Thành viên 2:** Trần Minh Bình (Data & Verification Specialist)  
**Môn học:** LAB211 — OOP with Java  
**Dự án:** Food Delivery Network Simulation  

---

## 1. RAW AI INTERACTION LOG (Lịch sử tương tác thô kèm Timestamp thực)

### [2026-09-12T11:20:00+07:00] — Prompt về sinh dữ liệu mẫu CSV
- **User Prompt:**  
  *"Viết một class DataGenerator trong Java để tạo ngẫu nhiên hơn 10.000 dòng dữ liệu cho 8 file CSV khách hàng, nhà hàng, tài xế, đơn hàng."*
- **AI Response Summary:**  
  AI cung cấp mã nguồn sử dụng `Random` nhưng sinh trùng lặp ID giữa các lần lặp và không đảm bảo tính toàn vẹn khóa ngoại (Foreign Key) giữa `order_items.csv` và `orders.csv`.

### [2026-09-28T15:40:12+07:00] — Prompt về thuật toán kiểm tra Oversell từ file CSV
- **User Prompt:**  
  *"Viết hàm detectOversellItems() đọc trực tiếp menu_items.csv để đếm xem có bao nhiêu món bị âm kho."*
- **AI Response Summary:**  
  AI dùng `Scanner` đọc từng từ, gặp lỗi `InputMismatchException` khi chuỗi tên món có chứa dấu phẩy và khoảng trắng.

### [2026-10-06T09:10:30+07:00] — Prompt về Optimistic Locking
- **User Prompt:**  
  *"Làm thế nào để áp dụng trường version cho Optimistic Locking khi lưu dữ liệu xuống file CSV mà không dùng Hibernate/JPA?"*
- **AI Response Summary:**  
  AI đề xuất đọc version, nếu đúng thì ghi và tăng version. Tuy nhiên AI quên xử lý trường hợp ném `OptimisticLockException` khi version trong file đã bị luồng khác sửa đổi trước đó.

---

## 2. PHÂN TÍCH ĐIỂM MẠNH VÀ SAI SÓT CỦA AI VỀ QUẢN TRỊ DỮ LIỆU & KIỂM THỬ

### 2.1. Điểm mạnh của AI
- Giúp thiết kế nhanh chóng các mảng dữ liệu mẫu tiếng Việt và tiếng Anh (tên đường phố, món ăn, họ tên khách hàng).
- Hỗ trợ viết regex để xử lý CSV escape quote (`"`) chuẩn theo định dạng RFC 4180.
- Giúp tự động hóa việc tính toán giá tiền và tổng tiền đơn hàng trong các tệp CSV.

### 2.2. Sai sót và Điểm mù của AI
1. **Thiếu kiểm tra toàn vẹn tham chiếu (Referential Integrity):**
   - AI sinh `OrderItem` ngẫu nhiên trỏ đến `orderId` và `menuItemId` không tồn tại trong `orders.csv` và `menu_items.csv`. Điều này làm hỏng toàn bộ chuỗi nghiệp vụ khi load dữ liệu vào bộ nhớ.
2. **Không phân biệt được lưu trữ bộ nhớ và lưu trữ file vật lý:**
   - AI thường xuyên mặc định dữ liệu nằm trên RAM (In-Memory Database) và cho rằng các hàm `setter` sẽ tự động cập nhật dữ liệu. Trong bài toán CSV vật lý, mọi thay đổi phải được đồng bộ hóa xuống ổ đĩa, nếu không sẽ bị mất dữ liệu giữa các thread.
3. **Ảo giác về cú pháp file CSV:**
   - Khi đọc dòng CSV có địa chỉ `"123 Street 5, Hanoi"`, hàm `line.split(",")` ngây thơ của AI đã cắt nhầm chuỗi địa chỉ thành 2 phần tử riêng biệt, làm lệch toàn bộ chỉ mục cột kinh độ và vĩ độ, gây ra lỗi `NumberFormatException`.

---

## 3. CÁCH PHÁT HIỆN VÀ SỬA LỖI THỰC TẾ

1. **Khắc phục lỗi toàn vẹn dữ liệu trong DataGenerator:**
   - Em đã xây dựng lại luồng sinh dữ liệu có thứ tự chặt chẽ: Sinh `Customer` và `Restaurant` trước -> Sinh `MenuItem` theo từng nhà hàng -> Sinh `Driver` -> Sinh `Order` liên kết đúng `customerId` và `restaurantId` -> Sinh đúng 3 `OrderItem` tương ứng cho từng `Order`.
   - Kết quả: Bộ dữ liệu đạt **18.700 dòng** hoàn toàn chuẩn xác, không có bất kỳ khóa ngoại mồ côi (Orphan FK) nào.
2. **Cải tiến Regex CSV Parser:**
   - Thay thế toàn bộ lệnh `split(",")` đơn giản bằng regex chuyên dụng:
     `line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")`
   - Nhờ đó, tất cả các trường có chứa dấu phẩy bên trong dấu ngoặc kép đều được giữ nguyên vẹn 100%.
3. **Cài đặt chuẩn thuật toán Post-Verification:**
   - Tự tay viết lại 3 hàm: `detectDoubleAssignments()`, `detectOversellItems()`, `detectDriverOverload()` đọc trực tiếp từng dòng của file CSV thực tế trên đĩa cứng để đối chiếu số liệu với runtime counters của Simulator.

---

## 4. BÀI HỌC KINH NGHIỆM
- **Dữ liệu là trái tim của hệ thống:** Một sai sót nhỏ trong việc sinh dữ liệu giả lập sẽ dẫn đến toàn bộ kết quả benchmark bị sai lệch.
- **AI là trợ lý, không phải chuyên gia kiểm thử:** Kiểm thử tương tranh đòi hỏi tính kỷ luật và sự tỉ mỉ rất cao. Cần phải kiểm tra độc lập trên file vật lý thay vì chỉ tin vào các biến đếm trong bộ nhớ.
