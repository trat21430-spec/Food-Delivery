# NHẬT KÝ TƯƠNG TÁC VÀ PHẢN TƯ TỔNG KẾT AI (AI REFLECTION LOG)
**Thành viên 4:** Phạm Hải Đăng (View, UI & ASCII Grid Specialist)  
**Môn học:** LAB211 — OOP with Java  
**Dự án:** Food Delivery Network Simulation  

---

## 1. RAW AI INTERACTION LOG

### [2026-09-20T14:10:00+07:00] — Prompt về vẽ bản đồ ASCII
- **User Prompt:**  
  *"Viết MapView vẽ bản đồ ASCII hiển thị vị trí các nhà hàng R, khách hàng C và tài xế D trên lưới toạ độ 15 dòng 45 cột dựa trên kinh độ và vĩ độ ở Hà Nội."*
- **AI Response Summary:**  
  AI viết mã sử dụng các biến `private static final int ROWS = 15`, `private static final double MIN_LAT = 21.000` và tính sai trục Y (bị đảo ngược hướng Bắc - Nam).

### [2026-10-04T10:15:20+07:00] — Prompt về loại bỏ static và final
- **User Prompt:**  
  *"Hệ thống yêu cầu không được dùng từ khóa final, không dùng static (trừ hàm main), và phải dùng this đầy đủ. Hãy sửa lại MapView."*
- **AI Response Summary:**  
  AI sửa nhưng vẫn giữ lại các hằng số với từ khóa `final` vì cho rằng đây là quy ước chuẩn trong Java.

---

## 2. PHÂN TÍCH ĐIỂM MẠNH VÀ SAI SÓT CỦA AI

### 2.1. Điểm mạnh
- AI vẽ các bảng ASCII rất đẹp và cân đối bằng phương thức `System.out.printf`.
- Giúp thiết kế giao diện dòng lệnh thân thiện, hiển thị trực quan các menu và trạng thái đơn hàng.

### 2.2. Sai sót: Cố chấp với định kiến huấn luyện và lỗi tính toán tọa độ
- AI có thói quen cố hữu là tự động thêm `static` và `final` cho bất kỳ biến nào có vẻ là hằng số cấu hình. Dù người dùng đã nhắc nhở, AI vẫn thường xuyên lặp lại thói quen này do trọng số dữ liệu huấn luyện (Pre-training bias).
- Trong công thức chiếu tọa độ địa lý lên màn hình máy tính: Vĩ độ (Latitude) tăng dần khi đi lên phía Bắc, nhưng dòng của ma trận hiển thị (Row index) lại tăng dần khi đi xuống phía dưới màn hình. AI quên đảo ngược trục Y khiến bản đồ bị lộn ngược Bắc - Nam.

---

## 3. CÁCH PHÁT HIỆN VÀ SỬA LỖI THỰC TẾ
- Em phát hiện bản đồ bị ngược khi thấy các nhà hàng ở phía Bắc Hà Nội lại bị in ở đáy màn hình.
- Em đã chỉnh lại công thức chuẩn:
  `int r = (int)((1.0 - ratio) * (this.rows - 1));`
  giúp trục Y được định hướng chính xác theo la bàn địa lý.
- Tự tay rà soát và loại bỏ triệt để mọi từ khóa `static`, `final`, `protected` trong toàn bộ lớp View, chuyển các biến cấu hình thành thuộc tính instance bình thường và dùng `this.` tường minh trong toàn bộ constructor và phương thức.

---

## 4. BÀI HỌC KINH NGHIỆM
- **Không phụ thuộc vào phím tắt của AI:** Phải luôn tự tay kiểm tra mã nguồn bằng các công cụ kiểm tra tự động (Grep, Regular Expressions) để đảm bảo tuân thủ 100% các yêu cầu khắt khe của dự án.
- **Tư duy hình học không gian trong lập trình:** Sự kết hợp giữa thuật toán chiếu tọa độ và giao diện dòng lệnh đòi hỏi người lập trình phải hiểu sâu về toán học và hệ tọa độ máy tính.
