<div align="center">

# 🍔🛵 FOOD DELIVERY NETWORK SIMULATION 🛵🍕

### 📋 Phân tích và mô tả hệ thống

![Java](https://img.shields.io/badge/Java-OOP-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MVC](https://img.shields.io/badge/Architecture-MVC-4B8BBE?style=for-the-badge)
![CSV](https://img.shields.io/badge/Storage-CSV-2E8B57?style=for-the-badge&logo=files&logoColor=white)
![Concurrency](https://img.shields.io/badge/Focus-Concurrency-D9534F?style=for-the-badge)
![Course](https://img.shields.io/badge/LAB211-FPT%20University-orange?style=for-the-badge)

</div>

> 🏫 **LAB211 – OOP with Java – FPT University**
>
> 🍱 **Chủ đề:** Food Delivery Network Simulation
>
> 📖 Tài liệu này mô tả bài toán, actor, chức năng, luồng nghiệp vụ, 3 race condition, Use Case và Class Diagram ở mức thiết kế hệ thống.

---

## 📚 Mục lục

| # | Chủ đề |
|:-:|---|
| 1 | 🌐 [Tổng quan hệ thống](#-1-tổng-quan-hệ-thống) |
| 2 | 🎭 [Actor của hệ thống](#-2-actor-của-hệ-thống) |
| 3 | 🔄 [Luồng nghiệp vụ chính](#-3-luồng-nghiệp-vụ-chính) |
| 4 | ⚠️ [Ba Race Condition](#️-4-ba-race-condition) |
| 5 | 🔐 [Các cơ chế đồng bộ cần so sánh](#-5-các-cơ-chế-đồng-bộ-cần-so-sánh) |
| 6 | 🗺️ [Use Case Diagram](#️-6-use-case-diagram) |
| 7 | 🗂️ [Use Case Diagram dạng tổng quát](#️-7-use-case-diagram-dạng-tổng-quát) |
| 8 | 🧩 [Class Diagram](#-8-class-diagram) |
| 9 | 🔗 [Quan hệ giữa các Class](#-9-quan-hệ-giữa-các-class) |
| 10 | 🗄️ [Repository Layer](#️-10-repository-layer) |
| 11 | 🌍 [GeoUtils](#-11-geoutils) |
| 12 | ⚙️ [Simulator](#️-12-simulator) |
| 13 | 🔍 [Post-Verification](#-13-post-verification) |
| 14 | 🏗️ [Kiến trúc tổng thể](#️-14-kiến-trúc-tổng-thể) |
| 15 | 📁 [Các file CSV chính](#-15-các-file-csv-chính) |
| 16 | 🧵 [Mối liên hệ giữa các phần](#-16-mối-liên-hệ-giữa-các-phần) |
| 17 | 🏁 [Kết luận](#-17-kết-luận) |

---

## 🌐 1. Tổng quan hệ thống

Food Delivery Network Simulation là hệ thống mô phỏng quy trình giao đồ ăn giữa Customer, Restaurant và Driver, trong đó Dispatcher chịu trách nhiệm điều phối đơn hàng đến tài xế phù hợp.

Điểm đặc biệt của bài toán là nhiều Customer/Driver có thể hoạt động đồng thời. Dữ liệu được lưu trữ vật lý bằng các file CSV thay vì Database. Vì vậy hệ thống phải xử lý các vấn đề concurrency/race condition khi nhiều thread cùng đọc và ghi dữ liệu.

Mục tiêu chính:

- Quản lý Customer.
- Quản lý Restaurant và MenuItem.
- Quản lý Driver.
- Tạo và xử lý Order.
- Kiểm tra và cập nhật stock.
- Điều phối Driver gần nhất.
- Ngăn chặn Double Assignment.
- Ngăn chặn Oversell.
- Ngăn chặn Driver Overload.
- So sánh các cơ chế đồng bộ.
- Chạy Simulator với nhiều thread.
- Kiểm tra kết quả trực tiếp từ CSV.

---

# 🎭 2. Actor của hệ thống

Hệ thống có 4 actor nghiệp vụ chính:

| Actor | Vai trò |
|---|---|
| 🛍️ **Customer** | Khách hàng đặt và quản lý đơn hàng |
| 🍽️ **Restaurant** | Nhà hàng quản lý menu, stock và xác nhận đơn |
| 🛵 **Driver** | Tài xế nhận và giao đơn |
| 📡 **Dispatcher** | Điều phối đơn và gán tài xế phù hợp |

---

## 🛍️ 2.1. Customer

Customer là người sử dụng hệ thống để đặt đồ ăn.

### ⚙️ Chức năng

- Xem danh sách Restaurant.
- Xem Menu.
- Chọn MenuItem.
- Nhập số lượng.
- Đặt Order.
- Xem trạng thái Order.
- Xem lịch sử Order.
- Hủy Order khi Order còn PENDING.

### 📏 Quy tắc

Customer chỉ được hủy Order khi:

```text
Order.status = PENDING
```

> [!WARNING]
> Sau khi Driver đã nhận/được gán cho Order thì Customer **không được hủy**.

---

## 🍽️ 2.2. Restaurant

Restaurant chịu trách nhiệm quản lý món ăn và xử lý Order.

### ⚙️ Chức năng

- Xem Menu.
- Thêm MenuItem.
- Cập nhật MenuItem.
- Xóa MenuItem.
- Cập nhật stock.
- Xem các Order.
- Xác nhận Order.

### 📏 Quy tắc

> [!IMPORTANT]
> Khi Customer đặt món, hệ thống phải kiểm tra stock. Nếu không đủ stock thì Order bị từ chối.

---

## 🛵 2.3. Driver

Driver là tài xế giao đồ ăn.

### ⚙️ Chức năng

- Cập nhật trạng thái hoạt động.
- Chuyển sang AVAILABLE khi sẵn sàng nhận đơn.
- Xem Order được gán.
- Accept Order.
- Giao Order.
- Hoàn thành Delivery.

### 🔄 Trạng thái

```text
OFFLINE
AVAILABLE
BUSY
```

Luồng trạng thái thông thường:

```text
OFFLINE
   ↓
AVAILABLE
   ↓
BUSY
   ↓
AVAILABLE
```

Một Driver chỉ được xử lý một Order tại một thời điểm.

---

## 📡 2.4. Dispatcher

Dispatcher là thành phần/người điều phối việc giao đơn.

### ⚙️ Chức năng

1. Xem các Order đã được Restaurant xác nhận.
2. Tìm các Driver có trạng thái AVAILABLE.
3. Loại bỏ Driver BUSY hoặc OFFLINE.
4. Tính khoảng cách từ Driver đến Restaurant.
5. Tìm Driver gần nhất.
6. Assign Driver cho Order.
7. Cập nhật trạng thái Order.
8. Cập nhật trạng thái Driver.

### 💡 Ví dụ

Giả sử có:

```text
Order #1001 = CONFIRMED

Driver 01 = AVAILABLE, distance = 1.2 km
Driver 02 = AVAILABLE, distance = 3.5 km
Driver 03 = BUSY,      distance = 0.5 km
```

Dispatcher phải chọn Driver 01.

Kết quả:

```text
Order #1001 = ASSIGNED
Driver 01   = BUSY
```

Driver 03 không được chọn vì đang BUSY dù khoảng cách gần hơn.

### 🏛️ Quy tắc kiến trúc

Hai logic quan trọng:

```text
findNearestAvailable()
assignDriver()
```

> [!IMPORTANT]
> `findNearestAvailable()` và `assignDriver()` **phải** được xử lý ở Repository — **không** đặt logic nghiệp vụ này trong View hoặc Controller.

---

# 🔄 3. Luồng nghiệp vụ chính

## 🛒 3.1. Luồng Customer đặt Order

```text
Customer
   ↓
Chọn Restaurant
   ↓
Xem Menu
   ↓
Chọn MenuItem
   ↓
Nhập Quantity
   ↓
Check Stock
   ↓
Stock đủ?
 ┌─┴─────────────┐
YES              NO
 ↓                ↓
Deduct Stock    Reject Order
 ↓
Create Order
 ↓
Order = PENDING
```

### 📝 Các bước

1. Customer chọn Restaurant.
2. Customer xem Menu.
3. Customer chọn MenuItem.
4. Customer nhập số lượng.
5. Hệ thống kiểm tra stock.
6. Nếu đủ stock, hệ thống trừ stock.
7. Hệ thống tạo Order.
8. Order có trạng thái PENDING.

---

## ✅ 3.2. Luồng Restaurant xác nhận Order

```text
Restaurant
    ↓
View Pending Orders
    ↓
Check Order
    ↓
Confirm Order
    ↓
Order = CONFIRMED
    ↓
Dispatcher xử lý
```

Nếu Order không được xác nhận trong thời gian quy định, Order không tiếp tục theo luồng xác nhận bình thường.

---

## 📍 3.3. Luồng Dispatcher điều phối Driver

```text
CONFIRMED Order
       ↓
Find Available Drivers
       ↓
Filter AVAILABLE
       ↓
Calculate Distance
       ↓
Find Nearest Driver
       ↓
Assign Driver
       ↓
Driver = BUSY
       ↓
Order = ASSIGNED
```

Khoảng cách giữa các vị trí được tính bằng Haversine.

---

## 🚚 3.4. Luồng Driver giao Order

```text
Driver
   ↓
View Assigned Order
   ↓
Accept Order
   ↓
Driver = BUSY
   ↓
Pickup Food
   ↓
Deliver Food
   ↓
Order = DELIVERED
   ↓
Driver = AVAILABLE
```

---

## ❌ 3.5. Luồng Customer hủy Order

```text
Customer
   ↓
Cancel Order
   ↓
Check Order Status
   ↓
PENDING?
 ┌─┴───────┐
YES       NO
 ↓         ↓
Cancel    Reject
Order     Cancel
```

Điều kiện:

```text
Chỉ PENDING mới được Cancel.
```

---

# ⚠️ 4. Ba Race Condition

## 🔀 4.1. Double Assignment

### 📖 Định nghĩa

Hai thread đồng thời gán hai Driver khác nhau cho cùng một Order.

### 💡 Ví dụ

```text
Order #1001 = PENDING

Thread A                  Thread B
   ↓                         ↓
Read Order PENDING       Read Order PENDING
   ↓                         ↓
Assign Driver A          Assign Driver B
   ↓                         ↓
        Cùng xử lý Order #1001
```

Kết quả sai:

```text
Order #1001
 ├── Driver A
 └── Driver B
```

### 🔎 Nguyên nhân

Hai thread đọc trạng thái cũ trước khi thread còn lại hoàn thành việc cập nhật.

### ✅ Cần đảm bảo

```text
Check Order
    +
Assign Driver
    +
Update Order
```

được xử lý an toàn.

---

# 🍱 4.2. Oversell

### 📖 Định nghĩa

Hai hoặc nhiều Customer cùng mua MenuItem trong khi stock không đủ.

### 💡 Ví dụ

```text
Stock = 1

Customer A                Customer B
    ↓                         ↓
Read Stock = 1            Read Stock = 1
    ↓                         ↓
Buy 1                     Buy 1
    ↓                         ↓
Stock = 0                Stock = 0
```

Thực tế chỉ có 1 sản phẩm nhưng hai Customer đều mua thành công.

Trong trường hợp cập nhật không an toàn, stock có thể bị âm.

### ✅ Cần đảm bảo

```text
Check Stock
    ↓
Deduct Stock
    ↓
Save MenuItem
```

được đồng bộ.

`MenuItem.version` được dùng để hỗ trợ Optimistic Locking.

---

# 🥵 4.3. Driver Overload

### 📖 Định nghĩa

Hai Order đồng thời được gán cho cùng một Driver.

### 💡 Ví dụ

```text
Order A ─────┐
             ↓
          Driver 01
             ↑
Order B ─────┘
```

Hai thread cùng thấy:

```text
Driver 01 = AVAILABLE
```

sau đó cùng assign Driver 01.

Kết quả sai:

```text
Driver 01
 ├── Order A
 └── Order B
```

### 📏 Quy tắc

Một Driver chỉ được xử lý một Order tại một thời điểm.

```text
AVAILABLE
    ↓
ASSIGN
    ↓
BUSY
    ↓
COMPLETE
    ↓
AVAILABLE
```

---

# 🔐 5. Các cơ chế đồng bộ cần so sánh

Simulator cần so sánh 4 cơ chế:

```text
1. NO_LOCK
2. FILE_LOCK
3. SYNCHRONIZED
4. OPTIMISTIC
```

### 🔓 NO_LOCK

Không sử dụng cơ chế đồng bộ.

Mục đích là tạo baseline và cho thấy race condition xảy ra.

Kết quả kỳ vọng:

```text
Double Assignment > 0
Oversell > 0
Driver Overload > 0
```

### 🔒 FILE_LOCK

Khóa file khi đọc/ghi để hạn chế truy cập đồng thời.

### 🧵 SYNCHRONIZED

Đồng bộ các critical section bằng Java synchronized/per-resource locking.

### 🚀 OPTIMISTIC

Sử dụng trường `version` để phát hiện dữ liệu đã bị thay đổi bởi thread khác.

Các entity quan trọng:

```text
MenuItem.version
Driver.version
Order.version
```

---

# 🗺️ 6. Use Case Diagram

## 🎭 6.1. Actor

```text
Customer
Restaurant
Driver
Dispatcher
```

## 🛍️ 6.2. Use Case của Customer

```text
Customer
 ├── View Menu
 ├── Place Order
 ├── View Order Status
 ├── View Order History
 └── Cancel Order
```

`Place Order` có:

```text
Place Order
   │
   ├── <<include>> Check Stock
   └── <<include>> Deduct Stock
```

---

## 🍽️ 6.3. Use Case của Restaurant

```text
Restaurant
 ├── Manage Menu
 ├── Update Stock
 ├── View Orders
 └── Confirm Order
```

---

## 🛵 6.4. Use Case của Driver

```text
Driver
 ├── Update Availability
 ├── View Assigned Order
 ├── Accept Order
 └── Deliver Order
```

---

## 📡 6.5. Use Case của Dispatcher

```text
Dispatcher
 └── Dispatch Order
          │
          ├── <<include>> Find Available Driver
          ├── <<include>> Calculate Distance
          ├── <<include>> Find Nearest Driver
          └── <<include>> Assign Driver
```

---

# 🗂️ 7. Use Case Diagram dạng tổng quát

```text
                         FOOD DELIVERY SYSTEM
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│ Customer                                                    │
│   │                                                         │
│   ├──────── (View Menu)                                     │
│   ├──────── (Place Order)                                   │
│   │                │                                        │
│   │                ├── <<include>> Check Stock              │
│   │                └── <<include>> Deduct Stock             │
│   ├──────── (View Order Status)                             │
│   ├──────── (View Order History)                            │
│   └──────── (Cancel Order)                                  │
│                                                             │
│ Restaurant                                                  │
│   │                                                         │
│   ├──────── (Manage Menu)                                   │
│   ├──────── (Update Stock)                                  │
│   ├──────── (View Orders)                                   │
│   └──────── (Confirm Order)                                 │
│                                                             │
│ Driver                                                      │
│   │                                                         │
│   ├──────── (Update Availability)                           │
│   ├──────── (View Assigned Order)                           │
│   ├──────── (Accept Order)                                  │
│   └──────── (Deliver Order)                                 │
│                                                             │
│ Dispatcher                                                  │
│   │                                                         │
│   └──────── (Dispatch Order)                                │
│                    │                                        │
│                    ├── <<include>> Find Available Driver    │
│                    ├── <<include>> Calculate Distance      │
│                    ├── <<include>> Find Nearest Driver     │
│                    └── <<include>> Assign Driver           │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

# 🧩 8. Class Diagram

## 🛍️ 8.1. Customer

```text
Customer
--------------------------------
- customerId : String
- name : String
- phone : String
- email : String
- latitude : double
- longitude : double
--------------------------------
+ placeOrder()
+ viewOrders()
+ cancelOrder()
```

---

## 🍽️ 8.2. Restaurant

```text
Restaurant
--------------------------------
- restaurantId : String
- name : String
- address : String
- latitude : double
- longitude : double
--------------------------------
+ addMenuItem()
+ updateMenuItem()
+ deleteMenuItem()
+ updateStock()
+ confirmOrder()
```

---

## 🍔 8.3. MenuItem

```text
MenuItem
--------------------------------
- itemId : String
- restaurantId : String
- name : String
- price : double
- stock : int
- version : long
--------------------------------
+ isAvailable()
+ deductStock()
+ increaseStock()
```

---

## 🛵 8.4. Driver

```text
Driver
--------------------------------
- driverId : String
- name : String
- phone : String
- latitude : double
- longitude : double
- status : DriverStatus
- version : long
--------------------------------
+ updateLocation()
+ setAvailable()
+ setBusy()
+ acceptOrder()
+ completeDelivery()
```

### 🔄 DriverStatus

```text
OFFLINE
AVAILABLE
BUSY
```

---

## 📦 8.5. Order

```text
Order
--------------------------------
- orderId : String
- customerId : String
- restaurantId : String
- driverId : String
- status : OrderStatus
- totalAmount : double
- createdAt : LocalDateTime
- confirmedAt : LocalDateTime
- version : long
--------------------------------
+ addItem()
+ calculateTotal()
+ confirm()
+ cancel()
+ assignDriver()
+ markDelivered()
```

### 📦 OrderStatus

```text
PENDING
CONFIRMED
ASSIGNED
DELIVERED
CANCELLED
REJECTED
```

---

## 🧾 8.6. OrderItem

```text
OrderItem
--------------------------------
- orderId : String
- menuItemId : String
- quantity : int
- unitPrice : double
--------------------------------
+ getSubtotal() : double
```

Công thức:

```text
subtotal = quantity × unitPrice
```

---

## 🗺️ 8.7. DeliveryRoute

```text
DeliveryRoute
--------------------------------
- routeId : String
- orderId : String
- driverId : String
- distanceKm : double
- estimatedMinutes : int
--------------------------------
+ calculateDistance()
+ estimateTime()
```

---

## 📊 8.8. SimulationRun

```text
SimulationRun
--------------------------------
- runId : String
- mechanism : String
- totalOrders : int
- durationMs : long
- throughput : double
- doubleAssignments : int
- oversellErrors : int
- driverOverloads : int
- throughputDrop : double
--------------------------------
+ calculateThroughput()
+ calculateDrop()
+ saveResult()
```

---

# 🔗 9. Quan hệ giữa các Class

```text
Customer 1 ───────── * Order

Restaurant 1 ─────── * MenuItem

Order 1 ───────────── * OrderItem

MenuItem 1 ────────── * OrderItem

Driver 1 ──────────── * Order

Order 1 ───────────── 1 DeliveryRoute

Driver 1 ──────────── * DeliveryRoute
```

Diễn giải:

- Một Customer có thể có nhiều Order.
- Một Restaurant có nhiều MenuItem.
- Một Order có nhiều OrderItem.
- Một MenuItem có thể xuất hiện trong nhiều OrderItem.
- Một Driver có thể xử lý nhiều Order theo thời gian, nhưng chỉ một Order tại một thời điểm.
- Một Order có một DeliveryRoute.
- Một Driver có nhiều DeliveryRoute theo lịch sử giao hàng.

---

# 🗄️ 10. Repository Layer

Kiến trúc MVC:

```text
View
  ↓
Controller
  ↓
Repository
  ↓
CSV Files
```

Các Repository đề xuất:

```text
CustomerRepository
RestaurantRepository
MenuItemRepository
DriverRepository
OrderRepository
OrderItemRepository
DeliveryRouteRepository
SimulationRunRepository
```

## 🔥 Critical methods

### 📦 OrderRepository

```text
+ createOrder()
+ confirmOrder()
+ cancelOrder()
+ assignDriver()
+ findById()
```

### 🍔 MenuItemRepository

```text
+ checkStock()
+ deductStock()
+ updateStock()
```

### 🚕 DriverRepository

```text
+ findNearestAvailable()
+ assignDriver()
+ updateStatus()
```

Đặc biệt:

```text
findNearestAvailable()
assignDriver()
```

> [!IMPORTANT]
> Đây là **critical business logic** và phải nằm ở Repository — vi phạm điều này sẽ bị trừ điểm trực tiếp khi chấm bài (grep code review).

---

# 🌍 11. GeoUtils

Hệ thống cần tính khoảng cách giữa các vị trí địa lý.

```text
GeoUtils
--------------------------------
+ haversine(lat1, lon1, lat2, lon2)
       : double
--------------------------------
```

Công thức được sử dụng để tính khoảng cách giữa hai tọa độ GPS.

Ví dụ kiểm thử:

```text
Hanoi → Ho Chi Minh City
≈ 1700 km
```

---

# ⚙️ 12. Simulator

Simulator phải mô phỏng concurrency thực tế.

```text
                    Simulator
                        │
             ┌──────────┴──────────┐
             ↓                     ↓
      CustomerThread         DispatchThread
             │                     │
             └──────────┬──────────┘
                        ↓
                 Concurrent Run
                        ↓
              Post Verification
                        ↓
        ┌───────────────┼───────────────┐
        ↓               ↓               ↓
 Double Assignment   Oversell     Driver Overload
```

Simulator phải sử dụng:

```text
CountDownLatch
```

để đồng bộ thời điểm bắt đầu/chạy các thread.

---

# 🔍 13. Post-Verification

Sau khi Simulator chạy xong, hệ thống phải đọc CSV trực tiếp để kiểm tra:

```text
detectDoubleAssignments()
detectOversellItems()
detectDriverOverload()
```

Mục tiêu:

```text
NO_LOCK
→ xuất hiện race condition

SYNCHRONIZED
→ 0 race condition

OPTIMISTIC
→ 0 race condition
```

Sau đó so sánh throughput giữa các cơ chế.

---

# 🏗️ 14. Kiến trúc tổng thể

```text
                     ACTORS
        ┌──────────────┼──────────────┐
        ↓              ↓              ↓
    Customer       Restaurant       Driver
        │              │              │
        └──────────────┼──────────────┘
                       ↓
                 Dispatcher
                       ↓
                   Controller
                       ↓
                  Repository
                       ↓
                 Synchronization
                       ↓
                    CSV Data
                       ↑
                  Simulator
                       │
             ┌─────────┼─────────┐
             ↓         ↓         ↓
         Customer   Dispatch   Verification
          Thread     Thread
```

---

# 📁 15. Các file CSV chính

Hệ thống cần các file:

```text
customers.csv
restaurants.csv
menu_items.csv
drivers.csv
orders.csv
order_items.csv
delivery_routes.csv
simulation_runs.csv
```

Các file này tạo thành physical storage của hệ thống.

---

# 🧵 16. Mối liên hệ giữa các phần

Toàn bộ bài toán có thể hiểu theo chuỗi:

```text
Customer
   ↓
Place Order
   ↓
Check Stock
   ↓
Deduct Stock
   ↓
Order = PENDING
   ↓
Restaurant Confirm
   ↓
Order = CONFIRMED
   ↓
Dispatcher
   ↓
Find AVAILABLE Driver
   ↓
Calculate Haversine Distance
   ↓
Find Nearest Driver
   ↓
Assign Driver
   ↓
Driver = BUSY
   ↓
Driver Accept
   ↓
Deliver
   ↓
Order = DELIVERED
   ↓
Driver = AVAILABLE
```

Trong quá trình nhiều thread thực hiện các thao tác trên, hệ thống phải ngăn:

```text
❌ Double Assignment
❌ Oversell
❌ Driver Overload
```

bằng cách so sánh:

```text
NO_LOCK
FILE_LOCK
SYNCHRONIZED
OPTIMISTIC
```

---

# 🏁 17. Kết luận

Food Delivery Network Simulation không chỉ là hệ thống đặt đồ ăn thông thường. Trọng tâm của bài là mô phỏng các giao dịch xảy ra đồng thời trên dữ liệu CSV và nghiên cứu cách các cơ chế đồng bộ ảnh hưởng đến tính đúng đắn và throughput.

### 🐞 Ba lỗi cần tập trung

```text
1. Double Assignment
2. Oversell
3. Driver Overload
```

### 🔐 Bốn cơ chế cần thử nghiệm

```text
1. NO_LOCK
2. FILE_LOCK
3. SYNCHRONIZED
4. OPTIMISTIC
```

### 🎭 Bốn Actor nghiệp vụ

```text
Customer
Restaurant
Driver
Dispatcher
```

### 🏗️ Kiến trúc

```text
View → Controller → Repository → CSV
```

> [!NOTE]
> Repository chịu trách nhiệm xử lý critical business logic và synchronization; Simulator dùng `CustomerThread` và `DispatchThread` để tạo concurrency, sau đó kiểm tra trực tiếp dữ liệu CSV để xác định race condition.

---

<div align="center">

### 🛵💨 Chúc bạn code đúng deadline và giao hàng thành công! 🍔✅

![Made with](https://img.shields.io/badge/Made%20with-☕%20%2B%20Java-brown?style=flat-square)
![Status](https://img.shields.io/badge/Status-Design%20Phase-yellow?style=flat-square)

</div>
