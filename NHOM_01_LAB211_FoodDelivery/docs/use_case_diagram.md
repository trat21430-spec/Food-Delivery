# SƠ ĐỒ VÀ MÔ TẢ USE CASE DIAGRAM (CHUẨN THEO MẪU THAM KHẢO CRS)
**LAB211 — OOP with Java | FPT University**

---

## 1. SƠ ĐỒ USE CASE DIAGRAM (ĐÚNG KIỂU DÁNG MẪU CRS THAM KHẢO)

```mermaid
graph LR
    subgraph SystemBoundary ["Food Delivery Simulation (FDS)"]
        %% Customer Use Cases
        UC01(("view available menu"))
        UC02(("track status request"))
        UC03(("send request"))
        UC04(("Place Order"))
        UC05(("Cancel Order"))

        %% Restaurant Use Cases
        UC06(("manage menu items"))
        UC07(("update stock quantity"))

        %% Driver Use Cases
        UC08(("update online status"))
        UC09(("receive dispatched order"))

        %% Dispatcher System Use Cases
        UC10(("auto-dispatch driver"))
        UC11(("run concurrency simulation"))
        UC12(("detect race conditions"))
    end

    %% Actors (Vertical Left Margin)
    Customer["👤 Customer"]
    Restaurant["🏪 Restaurant"]
    Driver["🛵 Driver"]
    Dispatcher["⚙️ Dispatcher System"]

    %% Solid Connections from Actors
    Customer --- UC01
    Customer --- UC02
    Customer --- UC04

    Restaurant --- UC06
    Restaurant --- UC07

    Driver --- UC08
    Driver --- UC09

    Dispatcher --- UC10
    Dispatcher --- UC11

    %% Dashed Relationships (<<include>> and <<extends>>)
    UC02 -. "<<include>>" .-> UC03
    UC05 -. "<<extends>>" .-> UC04
    UC11 -. "<<include>>" .-> UC12
```

---

## 2. BẢNG MÔ TẢ QUAN HỆ VÀ CÁC NỐI PHỤ THUỘC (RELATIONSHIPS)

| Tên Quan Hệ | Từ Use Case (Base) | Đới Use Case (Target) | Loại Quan Hệ | Mô Tả Ý Nghĩa |
| :---: | :--- | :--- | :---: | :--- |
| **1** | `track status request` | `send request` | `<<include>>` | Khi xem trạng thái đơn, hệ thống luôn phải tự động gửi request truy vấn tới server. |
| **2** | `Cancel Order` | `Place Order` | `<<extends>>` | Hủy đơn hàng là một nhánh mở rộng từ quy trình Đặt hàng (chỉ thực hiện khi cần). |
| **3** | `run concurrency simulation` | `detect race conditions` | `<<include>>` | Mô phỏng đa luồng luôn bao gồm khâu tự động hậu kiểm phát hiện lỗi race condition. |
