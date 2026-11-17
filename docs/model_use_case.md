# SƠ ĐỒ VÀ MÔ TẢ USE CASE PHÂN TẦNG MODEL (MODEL LAYER USE CASE)
**LAB211 — OOP with Java | FPT University**

---

## 1. SƠ ĐỒ USE CASE PHẦN MODEL (DARK THEME REFERENCE STYLE)

```mermaid
graph LR
    subgraph SystemBoundary ["Model Layer System"]
        %% Customer Model Use Cases
        MUC01(("Create/Update Customer Profile"))
        
        %% MenuItem Repo Use Cases
        MUC02(("Fetch Menu Items"))
        MUC03(("Access MenuItem Records"))
        MUC04(("Store MenuItem Data"))
        MUC05(("Map Data to Entities"))
        MUC06(("Sync MenuItem Inventory"))

        %% Driver Repo Use Cases
        MUC07(("Manage Driver Assignments"))
        MUC08(("Update Driver Status"))
        MUC09(("Log Driver Logs"))
        MUC10(("Update Driver Availability"))

        %% Order Repo Use Cases
        MUC11(("Process New Orders"))
        MUC12(("Create Order Record"))
        MUC13(("Calculate Order Total"))
        MUC14(("Update Order Status"))
        MUC15(("Check Payment Status"))
        MUC16(("Audit Trail Entry"))
        MUC17(("Record Order Payment"))

        %% CSV Verifier Use Cases
        MUC18(("Validate CSV Menu Data"))
        MUC19(("Parse CSV Format"))
        MUC20(("Check Menu Structure"))
    end

    %% Actors (Vertical Left Margin)
    CustomerModel["👤 Customer Model"]
    MenuItemRepo["👤 MenuItem Repo"]
    DriverRepo["👤 Driver Repo"]
    OrderRepo["👤 Order Repo"]
    CSVVerifier["👤 CSV Verifier"]

    %% Solid Connections from Actors
    CustomerModel --- MUC01

    MenuItemRepo --- MUC02
    MenuItemRepo --- MUC04
    MenuItemRepo --- MUC06

    DriverRepo --- MUC07
    DriverRepo --- MUC10

    OrderRepo --- MUC11
    OrderRepo --- MUC14
    OrderRepo --- MUC17

    CSVVerifier --- MUC18

    %% Dashed Relationships (<<include>>)
    MUC02 -. "<<include>>" .-> MUC03
    MUC04 -. "<<include>>" .-> MUC03
    MUC04 -. "<<include>>" .-> MUC05
    MUC07 -. "<<include>>" .-> MUC08
    MUC07 -. "<<include>>" .-> MUC09
    MUC11 -. "<<include>>" .-> MUC12
    MUC11 -. "<<include>>" .-> MUC13
    MUC14 -. "<<include>>" .-> MUC15
    MUC14 -. "<<include>>" .-> MUC16
    MUC18 -. "<<include>>" .-> MUC19
    MUC18 -. "<<include>>" .-> MUC20
```

---

## 2. CHÚ GIẢI CÁC USE CASE TẦNG MODEL & REPOSITORY

1. **Customer Model:** Thực hiện lưu trữ và truy vấn đối tượng `Customer` từ `customers.csv`.
2. **MenuItem Repo:** Xử lý danh mục thực đơn, trừ kho đồng bộ (`stockQty`) và kiểm tra thuộc tính `version++`.
3. **Driver Repo:** Quản lý gán đơn tài xế, chuyển trạng thái `AVAILABLE` <-> `BUSY` và tính khoảng cách mặt cầu Haversine.
4. **Order Repo:** Khởi tạo giao dịch đơn hàng mới, tính tổng tiền/phí giao hàng và quản lý vòng đời đơn (`PENDING -> CONFIRMED -> DELIVERED`).
5. **CSV Verifier:** Quét và hậu kiểm trực tiếp file CSV phát hiện 3 rủi ro Data Race (*Double Assignment*, *Oversell*, *Driver Overload*).
