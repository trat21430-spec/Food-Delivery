<div align="center">

# ðŸ”ðŸ›µ FOOD DELIVERY NETWORK SIMULATION ðŸ›µðŸ•

### ðŸ“‹ PhÃ¢n tÃ­ch vÃ  mÃ´ táº£ há»‡ thá»‘ng

![Java](https://img.shields.io/badge/Java-OOP-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MVC](https://img.shields.io/badge/Architecture-MVC-4B8BBE?style=for-the-badge)
![CSV](https://img.shields.io/badge/Storage-CSV-2E8B57?style=for-the-badge&logo=files&logoColor=white)
![Concurrency](https://img.shields.io/badge/Focus-Concurrency-D9534F?style=for-the-badge)
![Course](https://img.shields.io/badge/LAB211-FPT%20University-orange?style=for-the-badge)

</div>

> ðŸ« **LAB211 â€“ OOP with Java â€“ FPT University**
>
> ðŸ± **Chá»§ Ä‘á»:** Food Delivery Network Simulation
>
> ðŸ“– TÃ i liá»‡u nÃ y mÃ´ táº£ bÃ i toÃ¡n, actor, chá»©c nÄƒng, luá»“ng nghiá»‡p vá»¥, 3 race condition, Use Case vÃ  Class Diagram á»Ÿ má»©c thiáº¿t káº¿ há»‡ thá»‘ng.

---

## ðŸ‘¥ Danh sÃ¡ch thÃ nh viÃªn nhÃ³m

| Vai trÃ² | Há» vÃ  tÃªn | MSSV |
|:-:|---|:-:|
| ðŸ‘‘ Leader | Há»“ Anh Nháº­t | `QE200301` |
| ðŸ§‘â€ðŸ’» Member | TrÆ°Æ¡ng Thanh TrÃ  | `QE200100` |
| ðŸ§‘â€ðŸ’» Member | ÄÃ o Cao Duy | `QE190089` |
| ðŸ§‘â€ðŸ’» Member | Huá»³nh Nguyá»…n Quá»‘c Huy | `QE200031` |
| ðŸ§‘â€ðŸ’» Member | VÃµ Tháº£o NguyÃªn | `QE200064` |
| ðŸ§‘â€ðŸ’» Member | Äá»— Táº¥n VÅ© | `QE170159` |

---

## ðŸ“š Má»¥c lá»¥c

| # | Chá»§ Ä‘á» |
|:-:|---|
| 1 | ðŸŒ [Tá»•ng quan há»‡ thá»‘ng](#-1-tá»•ng-quan-há»‡-thá»‘ng) |
| 2 | ðŸŽ­ [Actor cá»§a há»‡ thá»‘ng](#-2-actor-cá»§a-há»‡-thá»‘ng) |
| 3 | ðŸ”„ [Luá»“ng nghiá»‡p vá»¥ chÃ­nh](#-3-luá»“ng-nghiá»‡p-vá»¥-chÃ­nh) |
| 4 | âš ï¸ [Ba Race Condition](#ï¸-4-ba-race-condition) |
| 5 | ðŸ” [CÃ¡c cÆ¡ cháº¿ Ä‘á»“ng bá»™ cáº§n so sÃ¡nh](#-5-cÃ¡c-cÆ¡-cháº¿-Ä‘á»“ng-bá»™-cáº§n-so-sÃ¡nh) |
| 6 | ðŸ—ºï¸ [Use Case Diagram](#ï¸-6-use-case-diagram) |
| 7 | ðŸ—‚ï¸ [Use Case Diagram dáº¡ng tá»•ng quÃ¡t](#ï¸-7-use-case-diagram-dáº¡ng-tá»•ng-quÃ¡t) |
| 8 | ðŸ§© [Class Diagram](#-8-class-diagram) |
| 9 | ðŸ”— [Quan há»‡ giá»¯a cÃ¡c Class](#-9-quan-há»‡-giá»¯a-cÃ¡c-class) |
| 10 | ðŸ—„ï¸ [Repository Layer](#ï¸-10-repository-layer) |
| 11 | ðŸŒ [GeoUtils](#-11-geoutils) |
| 12 | âš™ï¸ [Simulator](#ï¸-12-simulator) |
| 13 | ðŸ” [Post-Verification](#-13-post-verification) |
| 14 | ðŸ—ï¸ [Kiáº¿n trÃºc tá»•ng thá»ƒ](#ï¸-14-kiáº¿n-trÃºc-tá»•ng-thá»ƒ) |
| 15 | ðŸ“ [CÃ¡c file CSV chÃ­nh](#-15-cÃ¡c-file-csv-chÃ­nh) |
| 16 | ðŸ§µ [Má»‘i liÃªn há»‡ giá»¯a cÃ¡c pháº§n](#-16-má»‘i-liÃªn-há»‡-giá»¯a-cÃ¡c-pháº§n) |
| 17 | ðŸ [Káº¿t luáº­n](#-17-káº¿t-luáº­n) |

---

## ðŸŒ 1. Tá»•ng quan há»‡ thá»‘ng

Food Delivery Network Simulation lÃ  há»‡ thá»‘ng mÃ´ phá»ng quy trÃ¬nh giao Ä‘á»“ Äƒn giá»¯a Customer, Restaurant vÃ  Driver, trong Ä‘Ã³ Dispatcher chá»‹u trÃ¡ch nhiá»‡m Ä‘iá»u phá»‘i Ä‘Æ¡n hÃ ng Ä‘áº¿n tÃ i xáº¿ phÃ¹ há»£p.

Äiá»ƒm Ä‘áº·c biá»‡t cá»§a bÃ i toÃ¡n lÃ  nhiá»u Customer/Driver cÃ³ thá»ƒ hoáº¡t Ä‘á»™ng Ä‘á»“ng thá»i. Dá»¯ liá»‡u Ä‘Æ°á»£c lÆ°u trá»¯ váº­t lÃ½ báº±ng cÃ¡c file CSV thay vÃ¬ Database. VÃ¬ váº­y há»‡ thá»‘ng pháº£i xá»­ lÃ½ cÃ¡c váº¥n Ä‘á» concurrency/race condition khi nhiá»u thread cÃ¹ng Ä‘á»c vÃ  ghi dá»¯ liá»‡u.

Má»¥c tiÃªu chÃ­nh:

- Quáº£n lÃ½ Customer.
- Quáº£n lÃ½ Restaurant vÃ  MenuItem.
- Quáº£n lÃ½ Driver.
- Táº¡o vÃ  xá»­ lÃ½ Order.
- Kiá»ƒm tra vÃ  cáº­p nháº­t stock.
- Äiá»u phá»‘i Driver gáº§n nháº¥t.
- NgÄƒn cháº·n Double Assignment.
- NgÄƒn cháº·n Oversell.
- NgÄƒn cháº·n Driver Overload.
- So sÃ¡nh cÃ¡c cÆ¡ cháº¿ Ä‘á»“ng bá»™.
- Cháº¡y Simulator vá»›i nhiá»u thread.
- Kiá»ƒm tra káº¿t quáº£ trá»±c tiáº¿p tá»« CSV.

---

# ðŸŽ­ 2. Actor cá»§a há»‡ thá»‘ng

Há»‡ thá»‘ng cÃ³ 4 actor nghiá»‡p vá»¥ chÃ­nh:

| Actor | Vai trÃ² |
|---|---|
| ðŸ›ï¸ **Customer** | KhÃ¡ch hÃ ng Ä‘áº·t vÃ  quáº£n lÃ½ Ä‘Æ¡n hÃ ng |
| ðŸ½ï¸ **Restaurant** | NhÃ  hÃ ng quáº£n lÃ½ menu, stock vÃ  xÃ¡c nháº­n Ä‘Æ¡n |
| ðŸ›µ **Driver** | TÃ i xáº¿ nháº­n vÃ  giao Ä‘Æ¡n |
| ðŸ“¡ **Dispatcher** | Äiá»u phá»‘i Ä‘Æ¡n vÃ  gÃ¡n tÃ i xáº¿ phÃ¹ há»£p |

---

## ðŸ›ï¸ 2.1. Customer

Customer lÃ  ngÆ°á»i sá»­ dá»¥ng há»‡ thá»‘ng Ä‘á»ƒ Ä‘áº·t Ä‘á»“ Äƒn.

### âš™ï¸ Chá»©c nÄƒng

- Xem danh sÃ¡ch Restaurant.
- Xem Menu.
- Chá»n MenuItem.
- Nháº­p sá»‘ lÆ°á»£ng.
- Äáº·t Order.
- Xem tráº¡ng thÃ¡i Order.
- Xem lá»‹ch sá»­ Order.
- Há»§y Order khi Order cÃ²n PENDING.

### ðŸ“ Quy táº¯c

Customer chá»‰ Ä‘Æ°á»£c há»§y Order khi:

```text
Order.status = PENDING
```

> [!WARNING]
> Sau khi Driver Ä‘Ã£ nháº­n/Ä‘Æ°á»£c gÃ¡n cho Order thÃ¬ Customer **khÃ´ng Ä‘Æ°á»£c há»§y**.

---

## ðŸ½ï¸ 2.2. Restaurant

Restaurant chá»‹u trÃ¡ch nhiá»‡m quáº£n lÃ½ mÃ³n Äƒn vÃ  xá»­ lÃ½ Order.

### âš™ï¸ Chá»©c nÄƒng

- Xem Menu.
- ThÃªm MenuItem.
- Cáº­p nháº­t MenuItem.
- XÃ³a MenuItem.
- Cáº­p nháº­t stock.
- Xem cÃ¡c Order.
- XÃ¡c nháº­n Order.

### ðŸ“ Quy táº¯c

> [!IMPORTANT]
> Khi Customer Ä‘áº·t mÃ³n, há»‡ thá»‘ng pháº£i kiá»ƒm tra stock. Náº¿u khÃ´ng Ä‘á»§ stock thÃ¬ Order bá»‹ tá»« chá»‘i.

---

## ðŸ›µ 2.3. Driver

Driver lÃ  tÃ i xáº¿ giao Ä‘á»“ Äƒn.

### âš™ï¸ Chá»©c nÄƒng

- Cáº­p nháº­t tráº¡ng thÃ¡i hoáº¡t Ä‘á»™ng.
- Chuyá»ƒn sang AVAILABLE khi sáºµn sÃ ng nháº­n Ä‘Æ¡n.
- Xem Order Ä‘Æ°á»£c gÃ¡n.
- Accept Order.
- Giao Order.
- HoÃ n thÃ nh Delivery.

### ðŸ”„ Tráº¡ng thÃ¡i

```text
OFFLINE
AVAILABLE
BUSY
```

Luá»“ng tráº¡ng thÃ¡i thÃ´ng thÆ°á»ng:

```text
OFFLINE
   â†“
AVAILABLE
   â†“
BUSY
   â†“
AVAILABLE
```

Má»™t Driver chá»‰ Ä‘Æ°á»£c xá»­ lÃ½ má»™t Order táº¡i má»™t thá»i Ä‘iá»ƒm.

---

## ðŸ“¡ 2.4. Dispatcher

Dispatcher lÃ  thÃ nh pháº§n/ngÆ°á»i Ä‘iá»u phá»‘i viá»‡c giao Ä‘Æ¡n.

### âš™ï¸ Chá»©c nÄƒng

1. Xem cÃ¡c Order Ä‘Ã£ Ä‘Æ°á»£c Restaurant xÃ¡c nháº­n.
2. TÃ¬m cÃ¡c Driver cÃ³ tráº¡ng thÃ¡i AVAILABLE.
3. Loáº¡i bá» Driver BUSY hoáº·c OFFLINE.
4. TÃ­nh khoáº£ng cÃ¡ch tá»« Driver Ä‘áº¿n Restaurant.
5. TÃ¬m Driver gáº§n nháº¥t.
6. Assign Driver cho Order.
7. Cáº­p nháº­t tráº¡ng thÃ¡i Order.
8. Cáº­p nháº­t tráº¡ng thÃ¡i Driver.

### ðŸ’¡ VÃ­ dá»¥

Giáº£ sá»­ cÃ³:

```text
Order #1001 = CONFIRMED

Driver 01 = AVAILABLE, distance = 1.2 km
Driver 02 = AVAILABLE, distance = 3.5 km
Driver 03 = BUSY,      distance = 0.5 km
```

Dispatcher pháº£i chá»n Driver 01.

Káº¿t quáº£:

```text
Order #1001 = ASSIGNED
Driver 01   = BUSY
```

Driver 03 khÃ´ng Ä‘Æ°á»£c chá»n vÃ¬ Ä‘ang BUSY dÃ¹ khoáº£ng cÃ¡ch gáº§n hÆ¡n.

### ðŸ›ï¸ Quy táº¯c kiáº¿n trÃºc

Hai logic quan trá»ng:

```text
findNearestAvailable()
assignDriver()
```

> [!IMPORTANT]
> `findNearestAvailable()` vÃ  `assignDriver()` **pháº£i** Ä‘Æ°á»£c xá»­ lÃ½ á»Ÿ Repository â€” **khÃ´ng** Ä‘áº·t logic nghiá»‡p vá»¥ nÃ y trong View hoáº·c Controller.

---

# ðŸ”„ 3. Luá»“ng nghiá»‡p vá»¥ chÃ­nh

## ðŸ›’ 3.1. Luá»“ng Customer Ä‘áº·t Order

```text
Customer
   â†“
Chá»n Restaurant
   â†“
Xem Menu
   â†“
Chá»n MenuItem
   â†“
Nháº­p Quantity
   â†“
Check Stock
   â†“
Stock Ä‘á»§?
 â”Œâ”€â”´â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
YES              NO
 â†“                â†“
Deduct Stock    Reject Order
 â†“
Create Order
 â†“
Order = PENDING
```

### ðŸ“ CÃ¡c bÆ°á»›c

1. Customer chá»n Restaurant.
2. Customer xem Menu.
3. Customer chá»n MenuItem.
4. Customer nháº­p sá»‘ lÆ°á»£ng.
5. Há»‡ thá»‘ng kiá»ƒm tra stock.
6. Náº¿u Ä‘á»§ stock, há»‡ thá»‘ng trá»« stock.
7. Há»‡ thá»‘ng táº¡o Order.
8. Order cÃ³ tráº¡ng thÃ¡i PENDING.

---

## âœ… 3.2. Luá»“ng Restaurant xÃ¡c nháº­n Order

```text
Restaurant
    â†“
View Pending Orders
    â†“
Check Order
    â†“
Confirm Order
    â†“
Order = CONFIRMED
    â†“
Dispatcher xá»­ lÃ½
```

Náº¿u Order khÃ´ng Ä‘Æ°á»£c xÃ¡c nháº­n trong thá»i gian quy Ä‘á»‹nh, Order khÃ´ng tiáº¿p tá»¥c theo luá»“ng xÃ¡c nháº­n bÃ¬nh thÆ°á»ng.

---

## ðŸ“ 3.3. Luá»“ng Dispatcher Ä‘iá»u phá»‘i Driver

```text
CONFIRMED Order
       â†“
Find Available Drivers
       â†“
Filter AVAILABLE
       â†“
Calculate Distance
       â†“
Find Nearest Driver
       â†“
Assign Driver
       â†“
Driver = BUSY
       â†“
Order = ASSIGNED
```

Khoáº£ng cÃ¡ch giá»¯a cÃ¡c vá»‹ trÃ­ Ä‘Æ°á»£c tÃ­nh báº±ng Haversine.

---

## ðŸšš 3.4. Luá»“ng Driver giao Order

```text
Driver
   â†“
View Assigned Order
   â†“
Accept Order
   â†“
Driver = BUSY
   â†“
Pickup Food
   â†“
Deliver Food
   â†“
Order = DELIVERED
   â†“
Driver = AVAILABLE
```

---

## âŒ 3.5. Luá»“ng Customer há»§y Order

```text
Customer
   â†“
Cancel Order
   â†“
Check Order Status
   â†“
PENDING?
 â”Œâ”€â”´â”€â”€â”€â”€â”€â”€â”€â”
YES       NO
 â†“         â†“
Cancel    Reject
Order     Cancel
```

Äiá»u kiá»‡n:

```text
Chá»‰ PENDING má»›i Ä‘Æ°á»£c Cancel.
```

---

# âš ï¸ 4. Ba Race Condition

## ðŸ”€ 4.1. Double Assignment

### ðŸ“– Äá»‹nh nghÄ©a

Hai thread Ä‘á»“ng thá»i gÃ¡n hai Driver khÃ¡c nhau cho cÃ¹ng má»™t Order.

### ðŸ’¡ VÃ­ dá»¥

```text
Order #1001 = PENDING

Thread A                  Thread B
   â†“                         â†“
Read Order PENDING       Read Order PENDING
   â†“                         â†“
Assign Driver A          Assign Driver B
   â†“                         â†“
        CÃ¹ng xá»­ lÃ½ Order #1001
```

Káº¿t quáº£ sai:

```text
Order #1001
 â”œâ”€â”€ Driver A
 â””â”€â”€ Driver B
```

### ðŸ”Ž NguyÃªn nhÃ¢n

Hai thread Ä‘á»c tráº¡ng thÃ¡i cÅ© trÆ°á»›c khi thread cÃ²n láº¡i hoÃ n thÃ nh viá»‡c cáº­p nháº­t.

### âœ… Cáº§n Ä‘áº£m báº£o

```text
Check Order
    +
Assign Driver
    +
Update Order
```

Ä‘Æ°á»£c xá»­ lÃ½ an toÃ n.

---

# ðŸ± 4.2. Oversell

### ðŸ“– Äá»‹nh nghÄ©a

Hai hoáº·c nhiá»u Customer cÃ¹ng mua MenuItem trong khi stock khÃ´ng Ä‘á»§.

### ðŸ’¡ VÃ­ dá»¥

```text
Stock = 1

Customer A                Customer B
    â†“                         â†“
Read Stock = 1            Read Stock = 1
    â†“                         â†“
Buy 1                     Buy 1
    â†“                         â†“
Stock = 0                Stock = 0
```

Thá»±c táº¿ chá»‰ cÃ³ 1 sáº£n pháº©m nhÆ°ng hai Customer Ä‘á»u mua thÃ nh cÃ´ng.

Trong trÆ°á»ng há»£p cáº­p nháº­t khÃ´ng an toÃ n, stock cÃ³ thá»ƒ bá»‹ Ã¢m.

### âœ… Cáº§n Ä‘áº£m báº£o

```text
Check Stock
    â†“
Deduct Stock
    â†“
Save MenuItem
```

Ä‘Æ°á»£c Ä‘á»“ng bá»™.

`MenuItem.version` Ä‘Æ°á»£c dÃ¹ng Ä‘á»ƒ há»— trá»£ Optimistic Locking.

---

# ðŸ¥µ 4.3. Driver Overload

### ðŸ“– Äá»‹nh nghÄ©a

Hai Order Ä‘á»“ng thá»i Ä‘Æ°á»£c gÃ¡n cho cÃ¹ng má»™t Driver.

### ðŸ’¡ VÃ­ dá»¥

```text
Order A â”€â”€â”€â”€â”€â”
             â†“
          Driver 01
             â†‘
Order B â”€â”€â”€â”€â”€â”˜
```

Hai thread cÃ¹ng tháº¥y:

```text
Driver 01 = AVAILABLE
```

sau Ä‘Ã³ cÃ¹ng assign Driver 01.

Káº¿t quáº£ sai:

```text
Driver 01
 â”œâ”€â”€ Order A
 â””â”€â”€ Order B
```

### ðŸ“ Quy táº¯c

Má»™t Driver chá»‰ Ä‘Æ°á»£c xá»­ lÃ½ má»™t Order táº¡i má»™t thá»i Ä‘iá»ƒm.

```text
AVAILABLE
    â†“
ASSIGN
    â†“
BUSY
    â†“
COMPLETE
    â†“
AVAILABLE
```

---

# ðŸ” 5. CÃ¡c cÆ¡ cháº¿ Ä‘á»“ng bá»™ cáº§n so sÃ¡nh

Simulator cáº§n so sÃ¡nh 4 cÆ¡ cháº¿:

```text
1. NO_LOCK
2. FILE_LOCK
3. SYNCHRONIZED
4. OPTIMISTIC
```

### ðŸ”“ NO_LOCK

KhÃ´ng sá»­ dá»¥ng cÆ¡ cháº¿ Ä‘á»“ng bá»™.

Má»¥c Ä‘Ã­ch lÃ  táº¡o baseline vÃ  cho tháº¥y race condition xáº£y ra.

Káº¿t quáº£ ká»³ vá»ng:

```text
Double Assignment > 0
Oversell > 0
Driver Overload > 0
```

### ðŸ”’ FILE_LOCK

KhÃ³a file khi Ä‘á»c/ghi Ä‘á»ƒ háº¡n cháº¿ truy cáº­p Ä‘á»“ng thá»i.

### ðŸ§µ SYNCHRONIZED

Äá»“ng bá»™ cÃ¡c critical section báº±ng Java synchronized/per-resource locking.

### ðŸš€ OPTIMISTIC

Sá»­ dá»¥ng trÆ°á»ng `version` Ä‘á»ƒ phÃ¡t hiá»‡n dá»¯ liá»‡u Ä‘Ã£ bá»‹ thay Ä‘á»•i bá»Ÿi thread khÃ¡c.

CÃ¡c entity quan trá»ng:

```text
MenuItem.version
Driver.version
Order.version
```

---

# ðŸ—ºï¸ 6. Use Case Diagram

## ðŸŽ­ 6.1. Actor

```text
Customer
Restaurant
Driver
Dispatcher
```

## ðŸ›ï¸ 6.2. Use Case cá»§a Customer

```text
Customer
 â”œâ”€â”€ View Menu
 â”œâ”€â”€ Place Order
 â”œâ”€â”€ View Order Status
 â”œâ”€â”€ View Order History
 â””â”€â”€ Cancel Order
```

`Place Order` cÃ³:

```text
Place Order
   â”‚
   â”œâ”€â”€ <<include>> Check Stock
   â””â”€â”€ <<include>> Deduct Stock
```

---

## ðŸ½ï¸ 6.3. Use Case cá»§a Restaurant

```text
Restaurant
 â”œâ”€â”€ Manage Menu
 â”œâ”€â”€ Update Stock
 â”œâ”€â”€ View Orders
 â””â”€â”€ Confirm Order
```

---

## ðŸ›µ 6.4. Use Case cá»§a Driver

```text
Driver
 â”œâ”€â”€ Update Availability
 â”œâ”€â”€ View Assigned Order
 â”œâ”€â”€ Accept Order
 â””â”€â”€ Deliver Order
```

---

## ðŸ“¡ 6.5. Use Case cá»§a Dispatcher

```text
Dispatcher
 â””â”€â”€ Dispatch Order
          â”‚
          â”œâ”€â”€ <<include>> Find Available Driver
          â”œâ”€â”€ <<include>> Calculate Distance
          â”œâ”€â”€ <<include>> Find Nearest Driver
          â””â”€â”€ <<include>> Assign Driver
```

---

# ðŸ—‚ï¸ 7. Use Case Diagram dáº¡ng tá»•ng quÃ¡t

```text
                         FOOD DELIVERY SYSTEM
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚                                                             â”‚
â”‚ Customer                                                    â”‚
â”‚   â”‚                                                         â”‚
â”‚   â”œâ”€â”€â”€â”€â”€â”€â”€â”€ (View Menu)                                     â”‚
â”‚   â”œâ”€â”€â”€â”€â”€â”€â”€â”€ (Place Order)                                   â”‚
â”‚   â”‚                â”‚                                        â”‚
â”‚   â”‚                â”œâ”€â”€ <<include>> Check Stock              â”‚
â”‚   â”‚                â””â”€â”€ <<include>> Deduct Stock             â”‚
â”‚   â”œâ”€â”€â”€â”€â”€â”€â”€â”€ (View Order Status)                             â”‚
â”‚   â”œâ”€â”€â”€â”€â”€â”€â”€â”€ (View Order History)                            â”‚
â”‚   â””â”€â”€â”€â”€â”€â”€â”€â”€ (Cancel Order)                                  â”‚
â”‚                                                             â”‚
â”‚ Restaurant                                                  â”‚
â”‚   â”‚                                                         â”‚
â”‚   â”œâ”€â”€â”€â”€â”€â”€â”€â”€ (Manage Menu)                                   â”‚
â”‚   â”œâ”€â”€â”€â”€â”€â”€â”€â”€ (Update Stock)                                  â”‚
â”‚   â”œâ”€â”€â”€â”€â”€â”€â”€â”€ (View Orders)                                   â”‚
â”‚   â””â”€â”€â”€â”€â”€â”€â”€â”€ (Confirm Order)                                 â”‚
â”‚                                                             â”‚
â”‚ Driver                                                      â”‚
â”‚   â”‚                                                         â”‚
â”‚   â”œâ”€â”€â”€â”€â”€â”€â”€â”€ (Update Availability)                           â”‚
â”‚   â”œâ”€â”€â”€â”€â”€â”€â”€â”€ (View Assigned Order)                           â”‚
â”‚   â”œâ”€â”€â”€â”€â”€â”€â”€â”€ (Accept Order)                                  â”‚
â”‚   â””â”€â”€â”€â”€â”€â”€â”€â”€ (Deliver Order)                                 â”‚
â”‚                                                             â”‚
â”‚ Dispatcher                                                  â”‚
â”‚   â”‚                                                         â”‚
â”‚   â””â”€â”€â”€â”€â”€â”€â”€â”€ (Dispatch Order)                                â”‚
â”‚                    â”‚                                        â”‚
â”‚                    â”œâ”€â”€ <<include>> Find Available Driver    â”‚
â”‚                    â”œâ”€â”€ <<include>> Calculate Distance      â”‚
â”‚                    â”œâ”€â”€ <<include>> Find Nearest Driver     â”‚
â”‚                    â””â”€â”€ <<include>> Assign Driver           â”‚
â”‚                                                             â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
```

---

# ðŸ§© 8. Class Diagram

## ðŸ›ï¸ 8.1. Customer

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

## ðŸ½ï¸ 8.2. Restaurant

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

## ðŸ” 8.3. MenuItem

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

## ðŸ›µ 8.4. Driver

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

### ðŸ”„ DriverStatus

```text
OFFLINE
AVAILABLE
BUSY
```

---

## ðŸ“¦ 8.5. Order

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

### ðŸ“¦ OrderStatus

```text
PENDING
CONFIRMED
ASSIGNED
DELIVERED
CANCELLED
REJECTED
```

---

## ðŸ§¾ 8.6. OrderItem

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

CÃ´ng thá»©c:

```text
subtotal = quantity Ã— unitPrice
```

---

## ðŸ—ºï¸ 8.7. DeliveryRoute

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

## ðŸ“Š 8.8. SimulationRun

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

# ðŸ”— 9. Quan há»‡ giá»¯a cÃ¡c Class

```text
Customer 1 â”€â”€â”€â”€â”€â”€â”€â”€â”€ * Order

Restaurant 1 â”€â”€â”€â”€â”€â”€â”€ * MenuItem

Order 1 â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ * OrderItem

MenuItem 1 â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ * OrderItem

Driver 1 â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ * Order

Order 1 â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ 1 DeliveryRoute

Driver 1 â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ * DeliveryRoute
```

Diá»…n giáº£i:

- Má»™t Customer cÃ³ thá»ƒ cÃ³ nhiá»u Order.
- Má»™t Restaurant cÃ³ nhiá»u MenuItem.
- Má»™t Order cÃ³ nhiá»u OrderItem.
- Má»™t MenuItem cÃ³ thá»ƒ xuáº¥t hiá»‡n trong nhiá»u OrderItem.
- Má»™t Driver cÃ³ thá»ƒ xá»­ lÃ½ nhiá»u Order theo thá»i gian, nhÆ°ng chá»‰ má»™t Order táº¡i má»™t thá»i Ä‘iá»ƒm.
- Má»™t Order cÃ³ má»™t DeliveryRoute.
- Má»™t Driver cÃ³ nhiá»u DeliveryRoute theo lá»‹ch sá»­ giao hÃ ng.

---

# ðŸ—„ï¸ 10. Repository Layer

Kiáº¿n trÃºc MVC:

```text
View
  â†“
Controller
  â†“
Repository
  â†“
CSV Files
```

CÃ¡c Repository Ä‘á» xuáº¥t:

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

## ðŸ”¥ Critical methods

### ðŸ“¦ OrderRepository

```text
+ createOrder()
+ confirmOrder()
+ cancelOrder()
+ assignDriver()
+ findById()
```

### ðŸ” MenuItemRepository

```text
+ checkStock()
+ deductStock()
+ updateStock()
```

### ðŸš• DriverRepository

```text
+ findNearestAvailable()
+ assignDriver()
+ updateStatus()
```

Äáº·c biá»‡t:

```text
findNearestAvailable()
assignDriver()
```

> [!IMPORTANT]
> ÄÃ¢y lÃ  **critical business logic** vÃ  pháº£i náº±m á»Ÿ Repository â€” vi pháº¡m Ä‘iá»u nÃ y sáº½ bá»‹ trá»« Ä‘iá»ƒm trá»±c tiáº¿p khi cháº¥m bÃ i (grep code review).

---

# ðŸŒ 11. GeoUtils

Há»‡ thá»‘ng cáº§n tÃ­nh khoáº£ng cÃ¡ch giá»¯a cÃ¡c vá»‹ trÃ­ Ä‘á»‹a lÃ½.

```text
GeoUtils
--------------------------------
+ haversine(lat1, lon1, lat2, lon2)
       : double
--------------------------------
```

CÃ´ng thá»©c Ä‘Æ°á»£c sá»­ dá»¥ng Ä‘á»ƒ tÃ­nh khoáº£ng cÃ¡ch giá»¯a hai tá»a Ä‘á»™ GPS.

VÃ­ dá»¥ kiá»ƒm thá»­:

```text
Hanoi â†’ Ho Chi Minh City
â‰ˆ 1700 km
```

---

# âš™ï¸ 12. Simulator

Simulator pháº£i mÃ´ phá»ng concurrency thá»±c táº¿.

```text
                    Simulator
                        â”‚
             â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”´â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
             â†“                     â†“
      CustomerThread         DispatchThread
             â”‚                     â”‚
             â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                        â†“
                 Concurrent Run
                        â†“
              Post Verification
                        â†“
        â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
        â†“               â†“               â†“
 Double Assignment   Oversell     Driver Overload
```

Simulator pháº£i sá»­ dá»¥ng:

```text
CountDownLatch
```

Ä‘á»ƒ Ä‘á»“ng bá»™ thá»i Ä‘iá»ƒm báº¯t Ä‘áº§u/cháº¡y cÃ¡c thread.

---

# ðŸ” 13. Post-Verification

Sau khi Simulator cháº¡y xong, há»‡ thá»‘ng pháº£i Ä‘á»c CSV trá»±c tiáº¿p Ä‘á»ƒ kiá»ƒm tra:

```text
detectDoubleAssignments()
detectOversellItems()
detectDriverOverload()
```

Má»¥c tiÃªu:

```text
NO_LOCK
â†’ xuáº¥t hiá»‡n race condition

SYNCHRONIZED
â†’ 0 race condition

OPTIMISTIC
â†’ 0 race condition
```

Sau Ä‘Ã³ so sÃ¡nh throughput giá»¯a cÃ¡c cÆ¡ cháº¿.

---

# ðŸ—ï¸ 14. Kiáº¿n trÃºc tá»•ng thá»ƒ

```text
                     ACTORS
        â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
        â†“              â†“              â†“
    Customer       Restaurant       Driver
        â”‚              â”‚              â”‚
        â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                       â†“
                 Dispatcher
                       â†“
                   Controller
                       â†“
                  Repository
                       â†“
                 Synchronization
                       â†“
                    CSV Data
                       â†‘
                  Simulator
                       â”‚
             â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
             â†“         â†“         â†“
         Customer   Dispatch   Verification
          Thread     Thread
```

---

# ðŸ“ 15. CÃ¡c file CSV chÃ­nh

Há»‡ thá»‘ng cáº§n cÃ¡c file:

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

CÃ¡c file nÃ y táº¡o thÃ nh physical storage cá»§a há»‡ thá»‘ng.

---

# ðŸ§µ 16. Má»‘i liÃªn há»‡ giá»¯a cÃ¡c pháº§n

ToÃ n bá»™ bÃ i toÃ¡n cÃ³ thá»ƒ hiá»ƒu theo chuá»—i:

```text
Customer
   â†“
Place Order
   â†“
Check Stock
   â†“
Deduct Stock
   â†“
Order = PENDING
   â†“
Restaurant Confirm
   â†“
Order = CONFIRMED
   â†“
Dispatcher
   â†“
Find AVAILABLE Driver
   â†“
Calculate Haversine Distance
   â†“
Find Nearest Driver
   â†“
Assign Driver
   â†“
Driver = BUSY
   â†“
Driver Accept
   â†“
Deliver
   â†“
Order = DELIVERED
   â†“
Driver = AVAILABLE
```

Trong quÃ¡ trÃ¬nh nhiá»u thread thá»±c hiá»‡n cÃ¡c thao tÃ¡c trÃªn, há»‡ thá»‘ng pháº£i ngÄƒn:

```text
âŒ Double Assignment
âŒ Oversell
âŒ Driver Overload
```

báº±ng cÃ¡ch so sÃ¡nh:

```text
NO_LOCK
FILE_LOCK
SYNCHRONIZED
OPTIMISTIC
```

---

# ðŸ 17. Káº¿t luáº­n

Food Delivery Network Simulation khÃ´ng chá»‰ lÃ  há»‡ thá»‘ng Ä‘áº·t Ä‘á»“ Äƒn thÃ´ng thÆ°á»ng. Trá»ng tÃ¢m cá»§a bÃ i lÃ  mÃ´ phá»ng cÃ¡c giao dá»‹ch xáº£y ra Ä‘á»“ng thá»i trÃªn dá»¯ liá»‡u CSV vÃ  nghiÃªn cá»©u cÃ¡ch cÃ¡c cÆ¡ cháº¿ Ä‘á»“ng bá»™ áº£nh hÆ°á»Ÿng Ä‘áº¿n tÃ­nh Ä‘Ãºng Ä‘áº¯n vÃ  throughput.

### ðŸž Ba lá»—i cáº§n táº­p trung

```text
1. Double Assignment
2. Oversell
3. Driver Overload
```

### ðŸ” Bá»‘n cÆ¡ cháº¿ cáº§n thá»­ nghiá»‡m

```text
1. NO_LOCK
2. FILE_LOCK
3. SYNCHRONIZED
4. OPTIMISTIC
```

### ðŸŽ­ Bá»‘n Actor nghiá»‡p vá»¥

```text
Customer
Restaurant
Driver
Dispatcher
```

### ðŸ—ï¸ Kiáº¿n trÃºc

```text
View â†’ Controller â†’ Repository â†’ CSV
```

> [!NOTE]
> Repository chá»‹u trÃ¡ch nhiá»‡m xá»­ lÃ½ critical business logic vÃ  synchronization; Simulator dÃ¹ng `CustomerThread` vÃ  `DispatchThread` Ä‘á»ƒ táº¡o concurrency, sau Ä‘Ã³ kiá»ƒm tra trá»±c tiáº¿p dá»¯ liá»‡u CSV Ä‘á»ƒ xÃ¡c Ä‘á»‹nh race condition.

---

<div align="center">

### ðŸ›µðŸ’¨ ChÃºc báº¡n code Ä‘Ãºng deadline vÃ  giao hÃ ng thÃ nh cÃ´ng! ðŸ”âœ…

![Made with](https://img.shields.io/badge/Made%20with-â˜•%20%2B%20Java-brown?style=flat-square)
![Status](https://img.shields.io/badge/Status-Design%20Phase-yellow?style=flat-square)

</div>


---

# 🚀 PHẦN II: MASTER BLUEPRINT & HƯỚNG DẪN THỰC THI CHI TIẾT (NHÓM 6 THÀNH VIÊN)
> **Tài liệu chuẩn hóa:** Phân công chi tiết 6 thành viên, Data Contract 8 file CSV, cơ chế khóa AI và Checklist nghiệm thu đạt điểm tối đa.

---

# ðŸ›µ FPT UNIVERSITY â€” LAB211: FOOD DELIVERY NETWORK SIMULATION
## ðŸ“‹ Há»† THá»NG MÃ” PHá»ŽNG Máº NG LÆ¯á»šI GIAO Äá»’ Ä‚N & BÃ€I TOÃN Äá»’NG Bá»˜ HÃ“A Dá»® LIá»†U CSV
> **MÃ´n há»c:** LAB211 â€” OOP with Java (Java OOP NÃ¢ng cao â€” Semester 3)  
> **NgÃ´n ngá»¯:** Pure Java SE (Chá»‰ sá»­ dá»¥ng thÆ° viá»‡n chuáº©n cá»§a JDK â€” KhÃ´ng dÃ¹ng lib ngoÃ i)  
> **Kiáº¿n trÃºc báº¯t buá»™c:** MVC (Model - View - Controller) phÃ¢n táº§ng nghiÃªm ngáº·t  
> **Thá»i gian:** 10 Tuáº§n | **Quy mÃ´:** NhÃ³m **6 ThÃ nh ViÃªn (Group Standard)**  
> **Sáº£n pháº©m ná»™p:** MÃ£ nguá»“n Java + Dá»¯ liá»‡u CSV (â‰¥ 18.000 dÃ²ng) + BÃ¡o cÃ¡o Word/Slide + 4 Flowcharts + 6 File AI Log cÃ¡ nhÃ¢n (30% Ä‘iá»ƒm)

---

## ðŸ“‘ Má»¤C Lá»¤C TOÃ€N DIá»†N
1. [Báº£n Äá»“ Kiá»ƒm ToÃ¡n & Äá»‘i Chiáº¿u NghiÃªm Ngáº·t Rubric LAB211](#-1-báº£n-Ä‘á»“-kiá»ƒm-toÃ¡n--Ä‘á»‘i-chiáº¿u-nghiÃªm-ngáº·t-rubric-lab211)
2. [Big Question & 3 Loáº¡i Race Condition Cá»‘t LÃµi](#-2-big-question--3-loáº¡i-race-condition-cá»‘t-lÃµi)
3. [Báº£n Há»£p Äá»“ng Dá»¯ Liá»‡u CSV Chuáº©n (Data Contract & 8 Schemas)](#-3-báº£n-há»£p-Ä‘á»“ng-dá»¯-liá»‡u-csv-chuáº©n-data-contract--8-schemas)
4. [Äáº·c Táº£ 4 Actors & ToÃ n Bá»™ Use Cases Nghiá»‡p Vá»¥](#-4-Ä‘áº·c-táº£-4-actors--toÃ n-bá»™-use-cases-nghiá»‡p-vá»¥)
5. [Äáº·c Táº£ Ká»¹ Thuáº­t 4 CÆ¡ Cháº¿ Äá»“ng Bá»™ HÃ³a & Thuáº­t ToÃ¡n Retry](#-5-Ä‘áº·c-táº£-ká»¹-thuáº­t-4-cÆ¡-cháº¿-Ä‘á»“ng-bá»™-hÃ³a--thuáº­t-toÃ¡n-retry)
6. [Bá»™ Quy Táº¯c PhÃ²ng Chá»‘ng 5 Cáº¡m Báº«y Ká»¹ Thuáº­t Cháº¿t NgÆ°á»i (Advanced Pitfalls)](#-6-bá»™-quy-táº¯c-phÃ²ng-chá»‘ng-5-cáº¡m-báº«y-ká»¹-thuáº­t-cháº¿t-ngÆ°á»i-advanced-pitfalls)
7. [Kiáº¿n TrÃºc Lá»›p & Thiáº¿t Káº¿ UML Chi Tiáº¿t (Class Specifications)](#-7-kiáº¿n-trÃºc-lá»›p--thiáº¿t-káº¿-uml-chi-tiáº¿t-class-specifications)
8. [Bá»‘n Flowchart Chuáº©n Ná»™p BÃ¡o CÃ¡o (Mermaid Diagrams)](#-8-bá»‘n-flowchart-chuáº©n-ná»™p-bÃ¡o-cÃ¡o-mermaid-diagrams)
9. [Báº£ng PhÃ¢n CÃ´ng Nhiá»‡m Vá»¥ 6 ThÃ nh ViÃªn & Ma Tráº­n 10 Tuáº§n (Sprint Matrix)](#-9-báº£ng-phÃ¢n-cÃ´ng-nhiá»‡m-vá»¥-6-thÃ nh-viÃªn--ma-tráº­n-10-tuáº§n-sprint-matrix)
10. [4 NguyÃªn Táº¯c ThÃ©p Phá»‘i Há»£p & Quy TrÃ¬nh Git](#-10-4-nguyÃªn-táº¯c-thÃ©p-phá»‘i-há»£p--quy-trÃ¬nh-git)
11. [CÆ¡ Cháº¿ "KhÃ³a AI VÃ o Flow Theo Quy Luáº­t" (AI Guardrails)](#-11-cÆ¡-cháº¿-khÃ³a-ai-vÃ o-flow-theo-quy-luáº­t-ai-guardrails)
12. [Bá»™ 6 Prompt Máº«u ÄÃ³ng GÃ³i RiÃªng Cho 6 ThÃ nh ViÃªn](#-12-bá»™-6-prompt-máº«u-Ä‘Ã³ng-gÃ³i-riÃªng-cho-6-thÃ nh-viÃªn)
13. [HÆ°á»›ng Dáº«n AI Log CÃ¡ NhÃ¢n Äáº¡t Má»©c Xuáº¥t Sáº¯c (27-30% Äiá»ƒm)](#-13-hÆ°á»›ng-dáº«n-ai-log-cÃ¡-nhÃ¢n-Ä‘áº¡t-má»©c-xuáº¥t-sáº¯c-27-30-Ä‘iá»ƒm)
14. [HÆ°á»›ng Dáº«n BiÃªn Dá»‹ch, Cháº¡y Thá»­ & Cáº¥u TrÃºc ÄÃ³ng GÃ³i Ná»™p BÃ i (ZIP)](#-14-hÆ°á»›ng-dáº«n-biÃªn-dá»‹ch-cháº¡y-thá»­--cáº¥u-trÃºc-Ä‘Ã³ng-gÃ³i-ná»™p-bÃ i-zip)
15. [Checklist Nghiá»‡m Thu & Bá»™ TiÃªu ChÃ­ Rubric (DoD)](#-15-checklist-nghiá»‡m-thu--bá»™-tiÃªu-chÃ­-rubric-dod)

---

## ðŸ” 1. Báº¢N Äá»’ KIá»‚M TOÃN & Äá»I CHIáº¾U NGHIÃŠM NGáº¶T RUBRIC LAB211

Báº£ng kiá»ƒm toÃ¡n Ä‘á»‘i chiáº¿u tá»«ng tiÃªu chÃ­ Rubric cá»§a FPT University Ä‘á»ƒ Ä‘áº£m báº£o Ä‘áº¡t Ä‘iá»ƒm tá»‘i Ä‘a:

| STT | TiÃªu chÃ­ Rubric | Trá»ng sá»‘ | YÃªu cáº§u theo Ä‘á» bÃ i LAB211 | Giáº£i phÃ¡p hiá»‡n thá»±c trong Blueprint | Tráº¡ng thÃ¡i |
| :---: | :--- | :---: | :--- | :--- | :---: |
| 1 | **Decomposition** | **20%** | PhÃ¢n rÃ£ SRP rÃµ rÃ ng. ToÃ n bá»™ dispatch logic & tÃ¬m tÃ i xáº¿ gáº§n nháº¥t **chá»‰ á»Ÿ Repository**, khÃ´ng á»Ÿ Controller/View. XÃ¡c Ä‘á»‹nh Ä‘Ãºng 8 Entity vÃ  quan há»‡. | `GeoUtils` tÃ¡ch riÃªng. `OrderRepository`, `DriverRepository` giá»¯ toÃ n bá»™ logic dispatch. Controller chá»‰ gá»i Repo. | âœ… 100% Äáº¡t |
| 2 | **Abstraction** | **15%** | UML Class Diagram Ä‘áº§y Ä‘á»§ (visibility `+/-/#`, types, multiplicity). 4 Flowcharts. Káº¿ thá»«a `BaseEntity`. Generic `CsvRepository<T>`. DÃ¹ng Enum thay String. | Thiáº¿t káº¿ `BaseEntity`, `IRepository<T>`, `CsvRepository<T>`, 4 Flowchart chi tiáº¿t, Enums cho Driver/Order/Lock. | âœ… 100% Äáº¡t |
| 3 | **Algorithm Design** | **15%** | CRUD Ä‘áº§y Ä‘á»§. CÃ´ng thá»©c Haversine chuáº©n. `findNearestAvailable` Ä‘Ãºng. File I/O try-with-resources. Äá»§ $\ge 5$ custom exceptions. `DataGenerator` $\ge 18.000$ dÃ²ng. | `GeoUtils.haversine` $R=6371\text{km}$. Äá»§ 5 custom exceptions. `DataGenerator.java` sinh 18.000 dÃ²ng chuáº©n format. | âœ… 100% Äáº¡t |
| 4 | **Simulator Design** | **20%** | Äa luá»“ng thá»±c thá»¥ (`CustomerThread` + `DispatchThread`). DÃ¹ng `CountDownLatch`. Äá»§ 4 cÆ¡ cháº¿ khÃ³a. `VerificationService` Ä‘á»c trá»±c tiáº¿p CSV. `NO_LOCK` cÃ³ Ä‘á»§ 3 loáº¡i lá»—i. Báº£ng 8 cá»™t. | Dual-thread pool + 2 `CountDownLatch`. Äá»c trá»±c tiáº¿p CSV Ä‘áº¿m 3 lá»—i. Xuáº¥t báº£ng ASCII 8 cá»™t cÃ³ % drop Throughput. | âœ… 100% Äáº¡t |
| 5 | **AI Reflection** | **30%** | AI Log cÃ¡ nhÃ¢n cÃ³ timestamp thá»±c qua 10 tuáº§n. Reflection $\ge 500$ tá»«/ngÆ°á»i phÃ¢n tÃ­ch sÃ¢u hallucination concurrency cá»§a AI. | 6 file log cÃ¡ nhÃ¢n trong `ai_logs/`. HÆ°á»›ng dáº«n chi tiáº¿t phÃ¢n tÃ­ch lá»—i global lock vÃ  file race window. | âœ… 100% Äáº¡t |
| 6 | **Business Constraints** | **Bonus** | ÄÆ¡n hÃ ng timeout 5 phÃºt, Há»§y Ä‘Æ¡n khi `PENDING` (hoÃ n stock), cháº·n há»§y khi tÃ i xáº¿ Ä‘Ã£ nháº­n, 1 tÃ i xáº¿ = 1 Ä‘Æ¡n. | Hiá»‡n thá»±c Ä‘áº§y Ä‘á»§ use case 5-minute timeout, rollback kho, vÃ  exception cháº·n há»§y khi `ASSIGNED`. | âœ… 100% Äáº¡t |

---

## ðŸ”¬ 2. BIG QUESTION & 3 LOáº I RACE CONDITION Cá»T LÃ•I

> [!IMPORTANT]
> **RESEARCH QUESTION (CÃ‚U Há»ŽI NGHIÃŠN Cá»¨U Lá»šN):**  
> *"Trong Ä‘iá»u kiá»‡n tÃ i nguyÃªn lÆ°u trá»¯ bá»‹ giá»›i háº¡n á»Ÿ file váº­t lÃ½ (.csv), cÆ¡ cháº¿ Ä‘á»“ng bá»™ hÃ³a (Synchronization) nÃ o tá»‘i Æ°u nháº¥t Ä‘á»ƒ ngÄƒn cháº·n xung Ä‘á»™t dá»¯ liá»‡u (Race Condition) giá»¯a hÃ ng trÄƒm tÃ i xáº¿ vÃ  khÃ¡ch hÃ ng mÃ  khÃ´ng lÃ m sá»¥t giáº£m tá»‘c Ä‘á»™ xá»­ lÃ½ (Throughput) quÃ¡ 30% so vá»›i baseline?"*

### 2.1. PhÃ¢n TÃ­ch 3 Loáº¡i Race Condition Nguy Hiá»ƒm Nháº¥t
1. **ðŸ›µ Rá»§i ro 1: ÄÆ¡n hÃ ng bá»‹ nháº­n bá»Ÿi 2 tÃ i xáº¿ (Double Assignment)**  
   - *Ká»‹ch báº£n:* Driver Thread-A vÃ  Driver Thread-B cÃ¹ng Ä‘á»c `Order[O001].status = PENDING`. Cáº£ hai tháº¥y Ä‘Æ¡n chÆ°a cÃ³ tÃ i xáº¿, cÃ¹ng ghi `driverId = A` (hoáº·c B).  
   - *Háº­u quáº£:* 1 Ä‘Æ¡n hÃ ng cÃ³ 2 tÃ i xáº¿ tranh nhau Ä‘áº¿n láº¥y, khÃ¡ch hÃ ng nháº­n 2 cuá»™c gá»i, nhÃ  hÃ ng bá»‘i rá»‘i.
2. **ðŸ± Rá»§i ro 2: BÃ¡n vÆ°á»£t sá»‘ lÆ°á»£ng tá»“n kho (Oversell MenuItem)**  
   - *Ká»‹ch báº£n:* MÃ³n "CÆ¡m táº¥m" cÃ²n `stockQty = 2`. Customer Thread-1 vÃ  Thread-2 cÃ¹ng Ä‘áº·t 2 pháº§n cÃ¹ng lÃºc, cáº£ hai Ä‘á»c `stock = 2`, cáº£ hai xÃ¡c nháº­n Ä‘Æ¡n.  
   - *Háº­u quáº£:* NhÃ  hÃ ng nháº­n 2 Ä‘Æ¡n (cáº§n 4 pháº§n) trong khi chá»‰ cÃ³ 2 $\rightarrow$ `stockQty = -2` (Ã¢m kho). Pháº£i há»§y Ä‘Æ¡n sau khi Ä‘Ã£ thanh toÃ¡n.
3. **ðŸ”„ Rá»§i ro 3: TÃ i xáº¿ bá»‹ gÃ¡n nhiá»u Ä‘Æ¡n cÃ¹ng lÃºc (Driver Overload)**  
   - *Ká»‹ch báº£n:* TÃ i xáº¿ D005 Ä‘ang `status = AVAILABLE`. Thread-X gÃ¡n Ä‘Æ¡n O100, Thread-Y gÃ¡n Ä‘Æ¡n O101, cáº£ hai Ä‘á»c `status = AVAILABLE` trÆ°á»›c khi báº¥t ká»³ thread nÃ o ká»‹p ghi `BUSY`.  
   - *Háº­u quáº£:* TÃ i xáº¿ bá»‹ assign 2 Ä‘Æ¡n cÃ¹ng 1 thá»i Ä‘iá»ƒm $\rightarrow$ khÃ´ng thá»ƒ xá»­ lÃ½ Ä‘á»“ng thá»i.

### 2.2. Bá»‘n CÆ¡ Cháº¿ Äá»“ng Bá»™ HÃ³a Cáº§n Thá»±c Nghiá»‡m So SÃ¡nh
- **`NO_LOCK` (Baseline):** Cháº¡y song song khÃ´ng khÃ³a Ä‘á»ƒ chá»©ng minh sá»± xuáº¥t hiá»‡n cá»§a 3 loáº¡i lá»—i.
- **`FILE_LOCK`:** KhÃ³a á»Ÿ cáº¥p Ä‘á»™ file CSV váº­t lÃ½ thÃ´ng qua `FileChannel.lock()`.
- **`SYNCHRONIZED` (Fine-grained):** KhÃ³a má»‹n theo tá»«ng tÃ i nguyÃªn (Per-Resource Lock dÃ¹ng `ConcurrentHashMap<String, Object>` theo ID). Tuyá»‡t Ä‘á»‘i **KHÃ”NG** dÃ¹ng `synchronized(this)`.
- **`OPTIMISTIC` (Optimistic Concurrency Control):** Dá»±a vÃ o trÆ°á»ng `version` trong Entity. Äá»c `version`, khi ghi kiá»ƒm tra láº¡i `version` trong file: náº¿u khá»›p thÃ¬ tÄƒng `version + 1` vÃ  ghi; náº¿u khÃ´ng khá»›p thÃ¬ nÃ©m `OptimisticLockException` Ä‘á»ƒ thread tiáº¿n hÃ nh Retry.

---

## ðŸ“‘ 3. Báº¢N Há»¢P Äá»’NG Dá»® LIá»†U CSV CHUáº¨N (DATA CONTRACT & 8 SCHEMAS)

ToÃ n bá»™ dá»¯ liá»‡u lÆ°u trong thÆ° má»¥c `data/`. Má»i Entity káº¿ thá»«a `BaseEntity` vÃ  cÃ³ 2 phÆ°Æ¡ng thá»©c: `toCsvLine()` vÃ  `fromCsvLine(String line)`.

```
data/
â”œâ”€â”€ customers.csv         (â‰¥ 2.000 dÃ²ng)
â”œâ”€â”€ restaurants.csv       (â‰¥ 200 dÃ²ng)
â”œâ”€â”€ menu_items.csv        (â‰¥ 2.000 dÃ²ng)  â† [CRITICAL HOT SPOT: cÃ³ version]
â”œâ”€â”€ drivers.csv           (â‰¥ 500 dÃ²ng)    â† [CRITICAL HOT SPOT: cÃ³ version]
â”œâ”€â”€ orders.csv            (â‰¥ 3.000 dÃ²ng)  â† [CRITICAL HOT SPOT: cÃ³ version]
â”œâ”€â”€ order_items.csv       (â‰¥ 9.000 dÃ²ng)
â”œâ”€â”€ delivery_routes.csv   (â‰¥ 2.000 dÃ²ng)
â””â”€â”€ simulation_runs.csv   (LÆ°u káº¿t quáº£ cháº¡y benchmark thá»±c nghiá»‡m)
```

### Chi tiáº¿t 8 Schema CSV & RÃ ng Buá»™c:
1. **`customers.csv`**: `customerId,name,phone,email,latitude,longitude,createdAt`
2. **`restaurants.csv`**: `restaurantId,name,address,latitude,longitude,rating,isActive`
3. **`menu_items.csv` (CRITICAL)**: `itemId,restaurantId,name,price,stockQty,version`
   - *LÆ°u Ã½:* `stockQty >= 0`, `version` (int) tÄƒng 1 sau má»—i láº§n trá»« kho thÃ nh cÃ´ng.
4. **`drivers.csv` (CRITICAL)**: `driverId,name,phone,latitude,longitude,status,currentOrderId,version`
   - *LÆ°u Ã½:* `status` $\in$ `{AVAILABLE, BUSY, OFFLINE}`. `version` (int).
5. **`orders.csv` (CRITICAL)**: `orderId,customerId,restaurantId,driverId,status,totalAmount,createdAt,version`
   - *LÆ°u Ã½:* `status` $\in$ `{PENDING, ASSIGNED, PICKED_UP, DELIVERED, CANCELLED, EXPIRED}`. `version` (int).
6. **`order_items.csv` (â‰¥ 9.500 dÃ²ng)**: `orderItemId,orderId,itemId,quantity,unitPrice`
7. **`delivery_routes.csv`**: `routeId,orderId,driverId,distanceKm,estimatedMinutes,actualMinutes`
8. **`simulation_runs.csv`**: `runId,mechanism,totalOrders,threadCount,durationMs,throughputOps,doubleAssignments,oversellErrors,driverOverloads`

---

## ðŸ‘¥ 4. Äáº¶C Táº¢ 4 ACTORS & TOÃ€N Bá»˜ USE CASES NGHIá»†P Vá»¤

Theo yÃªu cáº§u tuáº§n 1-2 cá»§a Ä‘á» bÃ i, nhÃ³m pháº£i xÃ¢y dá»±ng Use Case Diagram cho **4 Actors**:

```
                               â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
                               â”‚           FOOD DELIVERY NETWORK SIMULATION             â”‚
                               â”‚                                                        â”‚
   â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”            â”‚   (UC1: Xem Menu & Äáº·t mÃ³n nhiá»u nhÃ  hÃ ng)             â”‚
   â”‚   CUSTOMER   â”‚â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€>â”‚   (UC2: Há»§y Ä‘Æ¡n hÃ ng khi cÃ²n PENDING)                  â”‚
   â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜            â”‚   (UC3: Xem lá»‹ch sá»­ & tráº¡ng thÃ¡i Ä‘Æ¡n hÃ ng)             â”‚
                               â”‚                                                        â”‚
   â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”            â”‚   (UC4: Cáº­p nháº­t tá»“n kho MenuItem)                     â”‚
   â”‚  RESTAURANT  â”‚â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€>â”‚   (UC5: Quáº£n lÃ½ danh má»¥c mÃ³n Äƒn)                       â”‚
   â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜            â”‚                                                        â”‚
                               â”‚   (UC6: Báº­t/Táº¯t tráº¡ng thÃ¡i AVAILABLE / OFFLINE)        â”‚
   â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”            â”‚   (UC7: Cáº­p nháº­t tá»a Ä‘á»™ GPS thá»i gian thá»±c)            â”‚
   â”‚    DRIVER    â”‚â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€>â”‚   (UC8: Nháº­n Ä‘Æ¡n & cáº­p nháº­t PICKED_UP -> DELIVERED)    â”‚
   â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜            â”‚                                                        â”‚
                               â”‚   (UC9: Tá»± Ä‘á»™ng Dispatch tÃ i xáº¿ gáº§n nháº¥t)              â”‚
   â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”            â”‚   (UC10: Xá»­ lÃ½ háº¿t háº¡n Ä‘Æ¡n hÃ ng sau 5 phÃºt)            â”‚
   â”‚  DISPATCHER  â”‚â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€>â”‚   (UC11: Äiá»u phá»‘i 4 cÆ¡ cháº¿ Ä‘á»“ng bá»™ hÃ³a)               â”‚
   â”‚   (SYSTEM)   â”‚            â”‚   (UC12: Cháº¡y Simulator Ä‘a luá»“ng & Ä‘o Throughput)      â”‚
   â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜            â”‚   (UC13: Tháº©m Ä‘á»‹nh Ä‘á»™c láº­p trá»±c tiáº¿p tá»« CSV)           â”‚
                               â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
```

### Chi Tiáº¿t Nghiá»‡p Vá»¥ Äáº·c Biá»‡t:
1. **RÃ ng buá»™c Háº¿t Háº¡n ÄÆ¡n HÃ ng 5 PhÃºt (5-Minute Timeout Rule):**
   - ÄÆ¡n hÃ ng sau khi táº¡o cÃ³ tráº¡ng thÃ¡i `PENDING` vÃ  ghi nháº­n `createdAt`.
   - Dispatcher quÃ©t cÃ¡c Ä‘Æ¡n `PENDING`: Náº¿u `currentTime - createdAt > 5 phÃºt` mÃ  chÆ°a tÃ¬m Ä‘Æ°á»£c tÃ i xáº¿ $\rightarrow$ Chuyá»ƒn tráº¡ng thÃ¡i Ä‘Æ¡n sang `EXPIRED`, gá»i `menuItemRepository.restoreStock()` hoÃ n tráº£ láº¡i sá»‘ lÆ°á»£ng mÃ³n Äƒn vÃ o kho.
2. **RÃ ng buá»™c Há»§y ÄÆ¡n HÃ ng (Order Cancellation Rule):**
   - KhÃ¡ch chá»‰ Ä‘Æ°á»£c há»§y khi `status == PENDING`.
   - Náº¿u Ä‘Æ¡n Ä‘Ã£ cÃ³ tÃ i xáº¿ nháº­n (`ASSIGNED` hoáº·c `PICKED_UP`), há»‡ thá»‘ng tá»« chá»‘i vÃ  nÃ©m `OrderCancellationException`.
3. **RÃ ng buá»™c TÃ­nh Nháº¥t QuÃ¡n 1-1:**
   - 1 Ä‘Æ¡n hÃ ng chá»‰ Ä‘Æ°á»£c nháº­n bá»Ÿi Ä‘Ãºng 1 tÃ i xáº¿.
   - 1 tÃ i xáº¿ khi Ä‘ang `BUSY` tuyá»‡t Ä‘á»‘i khÃ´ng Ä‘Æ°á»£c gÃ¡n thÃªm báº¥t ká»³ Ä‘Æ¡n nÃ o khÃ¡c.

---

## ðŸ”’ 5. Äáº¶C Táº¢ Ká»¸ THUáº¬T 4 CÆ  CHáº¾ Äá»’NG Bá»˜ HÃ“A & THUáº¬T TOÃN RETRY

### 5.1. CÆ¡ cháº¿ 1: `NO_LOCK` (Baseline)
- Äá»c file CSV, sá»­a Ä‘á»•i trÃªn bá»™ nhá»›, ghi Ä‘Ã¨ láº¡i file CSV mÃ  khÃ´ng qua báº¥t ká»³ cÆ¡ cháº¿ khÃ³a nÃ o.
- DÃ¹ng Ä‘á»ƒ Ä‘o Throughput tráº§n (tá»‘c Ä‘á»™ tá»‘i Ä‘a) vÃ  lÃ m cÆ¡ sá»Ÿ Ä‘o lÆ°á»ng 3 loáº¡i race condition.

### 5.2. CÆ¡ cháº¿ 2: `FILE_LOCK` (Cáº¥p Ä‘á»™ File)
- Sá»­ dá»¥ng `java.nio.channels.FileChannel` vÃ  `FileLock`:
  ```java
  try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.READ, StandardOpenOption.WRITE);
       FileLock lock = channel.lock()) {
      // Äá»c vÃ  ghi file CSV an toÃ n tuyá»‡t Ä‘á»‘i
  }
  ```
- *Äáº·c Ä‘iá»ƒm:* An toÃ n nhÆ°ng lÃ m ngháº½n I/O toÃ n bá»™ file, Throughput sá»¥t giáº£m máº¡nh (> 50%).

### 5.3. CÆ¡ cháº¿ 3: `SYNCHRONIZED` (Per-Resource Fine-Grained)
- Sá»­ dá»¥ng `ConcurrentHashMap<String, Object>` Ä‘á»ƒ quáº£n lÃ½ khÃ³a theo tá»«ng ID tÃ i nguyÃªn:
  ```java
  public class ResourceLockManager {
      private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();
      public Object getLock(String key) {
          return locks.computeIfAbsent(key, k -> new Object());
      }
  }
  ```
- PhÃ¢n vÃ¹ng khÃ³a theo tiá»n tá»‘: `ORDER_O001`, `DRIVER_D005`, `ITEM_IT001`. Khi thao tÃ¡c vá»›i Ä‘Æ¡n O001, cÃ¡c Ä‘Æ¡n O002, O003 váº«n Ä‘Æ°á»£c xá»­ lÃ½ song song mÃ  khÃ´ng bá»‹ cháº·n.
- Tuyá»‡t Ä‘á»‘i **KHÃ”NG** dÃ¹ng `synchronized(this)` trÃªn instance cá»§a Repository.

### 5.4. CÆ¡ cháº¿ 4: `OPTIMISTIC` (Optimistic Concurrency Control & Retry)
- Khi Ä‘á»c Ä‘á»‘i tÆ°á»£ng: láº¥y `currentVersion = entity.getVersion()`.
- Khi ghi: Má»Ÿ file kiá»ƒm tra xem dÃ²ng Ä‘Ã³ cÃ³ `version == currentVersion` khÃ´ng:
  - Náº¿u khá»›p: Cáº­p nháº­t dá»¯ liá»‡u, tÄƒng `version = currentVersion + 1`, ghi Ä‘Ã¨ dÃ²ng.
  - Náº¿u khÃ´ng khá»›p: NÃ©m `OptimisticLockException`.
- **Thuáº­t toÃ¡n Retry vá»›i Exponential Jitter Backoff:**
  ```java
  int retries = 0;
  final int MAX_RETRIES = 5;
  while (retries < MAX_RETRIES) {
      try {
          orderRepository.assignDriverWithOptimistic(orderId, driverId);
          break; // ThÃ nh cÃ´ng
      } catch (OptimisticLockException e) {
          retries++;
          Thread.sleep(10 + new Random().nextInt(20)); // Exponential Jitter Backoff
          if (retries >= MAX_RETRIES) throw new MaxRetryExceededException("VÆ°á»£t quÃ¡ sá»‘ láº§n thá»­ láº¡i cho Ä‘Æ¡n: " + orderId);
      }
  }
  ```

---

## âš¡ 6. Bá»˜ QUY Táº®C PHÃ’NG CHá»NG 5 Cáº M BáºªY Ká»¸ THUáº¬T CHáº¾T NGÆ¯á»œI (ADVANCED PITFALLS)

Trong láº­p trÃ¬nh Ä‘á»“ng thá»i vÃ  xá»­ lÃ½ tá»‡p CSV thuáº§n trÃªn Java, cÃ³ 5 cáº¡m báº«y chÃ­ máº¡ng mÃ  giáº£ng viÃªn thÆ°á»ng xoÃ¡y sÃ¢u khi váº¥n Ä‘Ã¡p báº£o vá»‡ Ä‘á»“ Ã¡n:

### âš ï¸ Cáº¡m báº«y 1: Deadlock khi khÃ³a Ä‘á»“ng thá»i 2 tÃ i nguyÃªn (Order & Driver)
- **TÃ¬nh huá»‘ng:** Thread A gÃ¡n Ä‘Æ¡n O001 cho Driver D005 (khÃ³a O001 trÆ°á»›c, rá»“i xin khÃ³a D005). CÃ¹ng lÃºc, Thread B xá»­ lÃ½ sá»± kiá»‡n Driver D005 tá»« chá»‘i Ä‘Æ¡n O001 (khÃ³a D005 trÆ°á»›c, rá»“i xin khÃ³a O001). Cáº£ 2 thread giá»¯ 1 khÃ³a vÃ  chá» khÃ³a cÃ²n láº¡i $\rightarrow$ **DEADLOCK TOÃ€N Há»† THá»NG**.
- **Giáº£i phÃ¡p triá»‡t Ä‘á»ƒ:** **Global Lock Ordering (KhÃ³a theo thá»© tá»± tá»« Ä‘iá»ƒn cá»‘ Ä‘á»‹nh)**:
  ```java
  String key1 = "ORDER_" + orderId;
  String key2 = "DRIVER_" + driverId;
  String firstLock = key1.compareTo(key2) < 0 ? key1 : key2;
  String secondLock = key1.compareTo(key2) < 0 ? key2 : key1;
  synchronized (lockManager.getLock(firstLock)) {
      synchronized (lockManager.getLock(secondLock)) {
          // Thao tÃ¡c an toÃ n 100%, khÃ´ng bao giá» xáº£y ra Deadlock
      }
  }
  ```

### âš ï¸ Cáº¡m báº«y 2: PhÃ¢n tÃ¡ch CSV cÃ³ chá»©a dáº¥u pháº©y trong ná»™i dung (Quoted String)
- **TÃ¬nh huá»‘ng:** TÃªn nhÃ  hÃ ng hoáº·c Ä‘á»‹a chá»‰ chá»©a dáº¥u pháº©y (vÃ­ dá»¥: `"Com Tam Cali, 123 Nguyen Hue, Q1"`). Náº¿u dÃ¹ng `line.split(",")`, dÃ²ng sáº½ bá»‹ tÃ¡ch thÃ nh 8-9 cá»™t thay vÃ¬ 7 cá»™t $\rightarrow$ gÃ¢y `ArrayIndexOutOfBoundsException` hoáº·c lá»‡ch toÃ n bá»™ cá»™t tá»a Ä‘á»™.
- **Giáº£i phÃ¡p triá»‡t Ä‘á»ƒ:** Viáº¿t hÃ m parse há»— trá»£ ngoáº·c kÃ©p hoáº·c chuáº©n hÃ³a trong `DataGenerator` khÃ´ng sinh dáº¥u pháº©y trong chuá»—i.
  ```java
  public static String[] parseCsvLine(String line) {
      List<String> tokens = new ArrayList<>();
      StringBuilder sb = new StringBuilder();
      boolean inQuotes = false;
      for (char c : line.toCharArray()) {
          if (c == '\"') inQuotes = !inQuotes;
          else if (c == ',' && !inQuotes) {
              tokens.add(sb.toString().trim());
              sb.setLength(0);
          } else sb.append(c);
      }
      tokens.add(sb.toString().trim());
      return tokens.toArray(new String[0]);
  }
  ```

### âš ï¸ Cáº¡m báº«y 3: TÃ­nh toÃ n váº¹n dá»¯ liá»‡u giá»¯a cÃ¡c file (Cross-File Integrity)
- **TÃ¬nh huá»‘ng:** Má»™t Ä‘Æ¡n hÃ ng cá»§a nhÃ  hÃ ng R0001 nhÆ°ng trong `order_items.csv` láº¡i chá»©a mÃ³n Äƒn IT0500 cá»§a nhÃ  hÃ ng R0005 (khÃ¡ch Ä‘áº·t chÃ©o mÃ³n cá»§a 2 quÃ¡n khÃ¡c nhau trong 1 Ä‘Æ¡n).
- **Giáº£i phÃ¡p triá»‡t Ä‘á»ƒ:** Trong `DataGenerator.java` vÃ  `OrderController.placeOrder()`, báº¯t buá»™c kiá»ƒm tra `menuItem.getRestaurantId().equals(order.getRestaurantId())`.

### âš ï¸ Cáº¡m báº«y 4: Ãnh xáº¡ chuáº©n hÃ³a tá»a Ä‘á»™ GPS lÃªn lÆ°á»›i báº£n Ä‘á»“ ASCII 20x20
- **TÃ¬nh huá»‘ng:** Tá»a Ä‘á»™ thá»±c táº¿ TP.HCM (VÄ© Ä‘á»™: $10.70 - 10.85$, Kinh Ä‘á»™: $106.60 - 106.75$) náº¿u khÃ´ng cÃ³ cÃ´ng thá»©c chiáº¿u (Projection) thÃ¬ khÃ´ng thá»ƒ hiá»ƒn thá»‹ trÃªn ma tráº­n $20 \times 20$ cá»§a `MapView`.
- **Giáº£i phÃ¡p triá»‡t Ä‘á»ƒ:** Ãp dá»¥ng cÃ´ng thá»©c chuáº©n hÃ³a Min-Max:
  $$\text{gridX} = \min\left(19, \max\left(0, \left\lfloor \frac{\text{lon} - \text{minLon}}{\text{maxLon} - \text{minLon}} \times 19 \right\rfloor\right)\right)$$
  $$\text{gridY} = \min\left(19, \max\left(0, \left\lfloor \frac{\text{lat} - \text{minLat}}{\text{maxLat} - \text{minLat}} \times 19 \right\rfloor\right)\right)$$

### âš ï¸ Cáº¡m báº«y 5: RÃ² rá»‰ Buffer khi Simulator káº¿t thÃºc (File Flush & Sync)
- **TÃ¬nh huá»‘ng:** CÃ¡c thread ghi dá»¯ liá»‡u báº±ng `BufferedWriter`, khi hoÃ n thÃ nh náº¿u chÆ°a gá»i `.flush()` hoáº·c `.close()`, dá»¯ liá»‡u váº«n náº±m trÃªn bá»™ Ä‘á»‡m RAM cá»§a há»‡ Ä‘iá»u hÃ nh. Khi `VerificationService` Ä‘á»c láº¡i file CSV ngay sau Ä‘Ã³ sáº½ bá»‹ thiáº¿u dÃ²ng hoáº·c Ä‘á»c ra file rá»—ng.
- **Giáº£i phÃ¡p triá»‡t Ä‘á»ƒ:** Má»i thao tÃ¡c ghi trong Repository pháº£i náº±m trong khá»‘i `try-with-resources` hoáº·c gá»i tÆ°á»ng minh `bw.flush()` trÆ°á»›c khi thread bÃ¡o cÃ¡o hoÃ n thÃ nh.

---

## ðŸ› 7. KIáº¾N TRÃšC Lá»šP & THIáº¾T Káº¾ UML CHI TIáº¾T (CLASS SPECIFICATIONS)

> [!WARNING]
> **QUY Táº®C PHÃ‚N Táº¦NG MVC (Báº®T BUá»˜C):**
> - **MODEL LAYER:** Äá»‹nh nghÄ©a thá»±c thá»ƒ, enum, thuáº­t toÃ¡n Haversine.
> - **REPOSITORY LAYER:** Náº¯m giá»¯ toÃ n bá»™ logic truy xuáº¥t CSV, trá»« kho, tÃ¬m tÃ i xáº¿ gáº§n nháº¥t, gÃ¡n Ä‘Æ¡n, vÃ  cÃ¡c cÆ¡ cháº¿ khÃ³a.
> - **CONTROLLER LAYER:** Chá»‰ Ä‘iá»u phá»‘i request/response, chá»n cÆ¡ cháº¿ khÃ³a. KhÃ´ng chá»©a logic nghiá»‡p vá»¥!
> - **VIEW LAYER:** Chá»‰ in giao diá»‡n Console, báº£n Ä‘á»“ ASCII vÃ  nháº­n input.

### 7.1. Model Layer (src/model/)
```
+-------------------------------------------------------------+
| <<abstract>> BaseEntity                                     |
+-------------------------------------------------------------+
| # id: String                                                |
| # version: int                                              |
| # createdAt: String                                         |
+-------------------------------------------------------------+
| + getId(): String                                           |
| + getVersion(): int                                         |
| + setVersion(int version): void                             |
| + incrementVersion(): void                                  |
| + {abstract} toCsvLine(): String                            |
| + {abstract} fromCsvLine(String line): void                 |
+-------------------------------------------------------------+
          â–²
          |
  +-------+-------+--------------------+--------------------+
  |               |                    |                    |
Customer     Restaurant             MenuItem              Driver
- name       - name                 - restaurantId       - name, phone
- phone      - address              - name, price        - lat, lon
- email      - lat, lon             - stockQty           - status (DriverStatus)
- lat, lon   - rating, isActive                          - currentOrderId
```

### 7.2. Repository Layer (src/repository/)
```
+----------------------------------------------------------------------------------------------------+
| OrderRepository                                                                                    |
+----------------------------------------------------------------------------------------------------+
| + assignDriverWithSync(orderId: String, driverId: String): void                                    |
| + assignDriverWithOptimistic(orderId: String, driverId: String): void                              |
| + cancelOrder(orderId: String): boolean                                                            |
| + checkAndExpireOrders(timeoutMinutes: int): List<Order>                                           |
| + findPendingOrders(): List<Order>                                                                 |
+----------------------------------------------------------------------------------------------------+

+----------------------------------------------------------------------------------------------------+
| DriverRepository                                                                                   |
+----------------------------------------------------------------------------------------------------+
| + findNearestAvailable(restaurantLat: double, restaurantLon: double): Optional<Driver>            |
| + markBusyWithSync(driverId: String, orderId: String): void                                        |
| + markBusyWithOptimistic(driverId: String, orderId: String): void                                  |
| + markAvailable(driverId: String): void                                                            |
| + updateLocation(driverId: String, lat: double, lon: double): void                                 |
+----------------------------------------------------------------------------------------------------+

+----------------------------------------------------------------------------------------------------+
| MenuItemRepository                                                                                 |
+----------------------------------------------------------------------------------------------------+
| + deductStockWithSync(itemId: String, quantity: int): void                                         |
| + deductStockWithOptimistic(itemId: String, quantity: int): void                                   |
| + restoreStock(itemId: String, quantity: int): void                                               |
| + validateStock(items: Map<String, Integer>): boolean                                              |
+----------------------------------------------------------------------------------------------------+
```

### 7.3. Concurrency Lock Layer (src/repository/lock/)
```
+-------------------------------------------------------------+
| <<interface>> ILockManager                                  |
+-------------------------------------------------------------+
| + acquireLock(String resourceId): AutoCloseable             |
+-------------------------------------------------------------+
          â–²
          |
  +-------+---------------------------+
  |                                   |
FileLockManager                ResourceLockManager
- locks: Map<String, FileChannel> - keyLocks: ConcurrentHashMap<String, Object>
+ acquireLock(filePath)        + acquireLock(resourceId)
```

### 7.4. Simulator Layer (src/simulator/)
```
+-------------------------------------------------------------+
| SimulationEngine                                            |
+-------------------------------------------------------------+
| - executor: ExecutorService                                 |
| - readyLatch: CountDownLatch                                |
| - startLatch: CountDownLatch                                |
+-------------------------------------------------------------+
| + runBenchmark(int orderCount, LockMechanism mech): SimRun  |
+-------------------------------------------------------------+

+-------------------------------------------------------------+
| VerificationService                                         |
+-------------------------------------------------------------+
| + detectDoubleAssignments(Path orderCsv): int               |
| + detectOversellItems(Path menuCsv): int                    |
| + detectDriverOverload(Path orderCsv, Path driverCsv): int  |
+-------------------------------------------------------------+
```

---

## ðŸ“Š 8. Bá»N FLOWCHART CHUáº¨N Ná»˜P BÃO CÃO (MERMAID DIAGRAMS)

NhÃ³m pháº£i ná»™p 4 flowchart nÃ y trong thÆ° má»¥c `docs/flowcharts/` cá»§a file ZIP bÃ i lÃ m:

### 8.1. Flowchart 1: Luá»“ng Äáº·t HÃ ng, Há»§y ÄÆ¡n & Dispatch TÃ i Xáº¿ (Order & Dispatch Flow)
```mermaid
flowchart TD
    Start([KhÃ¡ch hÃ ng gá»­i yÃªu cáº§u Ä‘áº·t mÃ³n]) --> ValidateStock{Kiá»ƒm tra tá»“n kho MenuItem?}
    ValidateStock -- KhÃ´ng Ä‘á»§ tá»“n kho --> ThrowOversell[BÃ¡o lá»—i: OversellException & Tá»« chá»‘i Ä‘Æ¡n]
    ValidateStock -- Äá»§ tá»“n kho --> DeductStock[Trá»« stockQty trong menu_items.csv]
    DeductStock --> CreateOrder[Táº¡o Ä‘Æ¡n hÃ ng má»›i status = PENDING]
    
    CreateOrder --> CustomerAction{KhÃ¡ch cÃ³ yÃªu cáº§u Há»¦Y Ä‘Æ¡n?}
    CustomerAction -- CÃ³ --> CheckPending{ÄÆ¡n cÃ²n PENDING?}
    CheckPending -- ÄÃºng --> DoCancel[Äá»•i status = CANCELLED & HoÃ n tráº£ stock mÃ³n Äƒn]
    CheckPending -- Sai (ÄÃ£ cÃ³ tÃ i xáº¿) --> RejectCancel[BÃ¡o lá»—i: OrderCancellationException]
    
    CustomerAction -- KhÃ´ng --> TriggerDispatch[Há»‡ thá»‘ng tá»± Ä‘á»™ng Dispatch tÃ i xáº¿]
    TriggerDispatch --> CheckTimeout{ÄÆ¡n PENDING quÃ¡ 5 phÃºt?}
    CheckTimeout -- ÄÃºng --> AutoExpire[Äá»•i status = EXPIRED & HoÃ n tráº£ stock kho]
    CheckTimeout -- KhÃ´ng --> FindDrivers[Lá»c danh sÃ¡ch Driver cÃ³ status == AVAILABLE]
    
    FindDrivers --> CalcDistance[DÃ¹ng GeoUtils.haversine tÃ­nh khoáº£ng cÃ¡ch tá»›i nhÃ  hÃ ng]
    CalcDistance --> PickNearest[Chá»n tÃ i xáº¿ gáº§n nháº¥t]
    
    PickNearest --> CheckRace{ÄÆ¡n cÃ²n PENDING & TÃ i xáº¿ cÃ²n AVAILABLE?}
    CheckRace -- KhÃ´ng (Xung Ä‘á»™t) --> RetryDispatch[Chá»n tÃ i xáº¿ khÃ¡c hoáº·c xáº¿p hÃ ng Ä‘á»£i]
    CheckRace -- ÄÃºng --> Assign[GÃ¡n driverId vÃ o Order -> status = ASSIGNED]
    Assign --> SetBusy[Cáº­p nháº­t Driver -> status = BUSY]
    SetBusy --> PickedUp[TÃ i xáº¿ nháº­n mÃ³n -> status = PICKED_UP]
    PickedUp --> Delivered[Giao thÃ nh cÃ´ng -> status = DELIVERED & Driver = AVAILABLE]
    Delivered --> End([Káº¿t thÃºc quy trÃ¬nh])
    DoCancel --> End
    AutoExpire --> End
    ThrowOversell --> End
    RejectCancel --> TriggerDispatch
```

### 8.2. Flowchart 2: Luá»“ng NgÄƒn Race Condition â€” So SÃ¡nh 4 CÆ¡ Cháº¿ (Race Condition Flow)
```mermaid
flowchart TD
    Start([Thao tÃ¡c Ghi vÃ o CSV: Trá»« kho / GÃ¡n xe]) --> CheckMech{CÆ¡ cháº¿ Ä‘á»“ng bá»™ Ä‘Æ°á»£c chá»n?}
    
    CheckMech -- NO_LOCK --> NoLockWrite[Äá»c file -> Ghi Ä‘Ã¨ trá»±c tiáº¿p -> Bá» qua kiá»ƒm tra version]
    NoLockWrite --> ConflictPotential[Dáº«n Ä‘áº¿n: Double Assignment, Oversell, Driver Overload]
    
    CheckMech -- FILE_LOCK --> AcquireFileLock[Má»Ÿ FileChannel & gá»i lock trÃªn file CSV]
    AcquireFileLock --> WriteCsv1[Ghi dá»¯ liá»‡u an toÃ n vÃ o file váº­t lÃ½]
    WriteCsv1 --> ReleaseFileLock[Giáº£i phÃ³ng FileLock]
    ReleaseFileLock --> HighLatency[Hiá»‡u nÄƒng cháº­m do ngháº½n I/O toÃ n bá»™ file]
    
    CheckMech -- SYNCHRONIZED --> GetResourceKey[XÃ¡c Ä‘á»‹nh Key tÃ i nguyÃªn: ORDER_id, DRIVER_id, ITEM_id]
    GetResourceKey --> AcquireKeyLock[KhÃ³a monitor cá»§a Object tÆ°Æ¡ng á»©ng trong ConcurrentHashMap]
    AcquireKeyLock --> CriticalSection[Thao tÃ¡c Ä‘á»c & ghi dá»¯ liá»‡u trÃªn tÃ i nguyÃªn Ä‘Ã³]
    CriticalSection --> ReleaseKeyLock[ThoÃ¡t khá»‘i synchronized]
    ReleaseKeyLock --> HighThroughput[KhÃ´ng xung Ä‘á»™t & Throughput cao]
    
    CheckMech -- OPTIMISTIC --> ReadVersion[Äá»c thá»±c thá»ƒ kÃ¨m currentVersion tá»« CSV]
    ReadVersion --> ModifyMemory[Cáº­p nháº­t dá»¯ liá»‡u trÃªn bá»™ nhá»›]
    ModifyMemory --> CompareVersion{version trong file CSV cÃ²n báº±ng currentVersion?}
    CompareVersion -- ÄÃºng --> Commit[Ghi dá»¯ liá»‡u & tÄƒng version lÃªn 1]
    CompareVersion -- Sai (Bá»‹ thread khÃ¡c sá»­a trÆ°á»›c) --> ThrowOptimistic[NÃ©m OptimisticLockException -> Luá»“ng Retry vá»›i Backoff]
    Commit --> ZeroError[Äáº¡t 0% lá»—i & Tá»‘i Æ°u Throughput]
```

### 8.3. Flowchart 3: Luá»“ng Simulator Tool â€” Äá»“ng Thá»i N Threads (Simulator Tool Flow)
```mermaid
flowchart TD
    Start([Khá»Ÿi Ä‘á»™ng Simulator Tool]) --> InputParams[Nháº­p: Sá»‘ lÆ°á»£ng Ä‘Æ¡n 500/1000 & Chá»n LockMechanism]
    InputParams --> InitPool[Táº¡o ExecutorService & 2 CountDownLatch: readyLatch, startLatch]
    InitPool --> SpawnThreads[Khá»Ÿi táº¡o 50 CustomerThreads & 20 DispatchThreads]
    SpawnThreads --> WaitReady[Táº¥t cáº£ threads gá»i readyLatch.countDown & chá» táº¡i startLatch.await]
    
    WaitReady --> AllReady{readyLatch == 0?}
    AllReady -- ÄÃºng --> TriggerFire[startLatch.countDown: Táº¤T Cáº¢ LUá»’NG Báº®T Äáº¦U Äá»’NG THá»œI]
    TriggerFire --> RecordStartTime[Báº¯t Ä‘áº§u Ä‘o thá»i gian nanoTime]
    
    RecordStartTime --> ParallelExec[CÃ¡c luá»“ng Ä‘ua nhau Ä‘á»c/ghi cÃ¡c file CSV]
    ParallelExec --> WaitFinish[Chá» táº¥t cáº£ luá»“ng hoÃ n thÃ nh executor.awaitTermination]
    WaitFinish --> RecordEndTime[Láº¥y thá»i gian káº¿t thÃºc & TÃ­nh Throughput ops/sec]
    
    RecordEndTime --> DirectVerification[VerificationService: Äá»c trá»±c tiáº¿p cÃ¡c file CSV váº­t lÃ½]
    DirectVerification --> ScanOrders[QuÃ©t orders.csv: Äáº¿m sá»‘ Ä‘Æ¡n bá»‹ Double Assignment]
    DirectVerification --> ScanMenu[QuÃ©t menu_items.csv: Äáº¿m sá»‘ mÃ³n cÃ³ stockQty Ã¢m - Oversell]
    DirectVerification --> ScanDrivers[Äá»‘i chiáº¿u orders vÃ  drivers: Äáº¿m tÃ i xáº¿ nháº­n 2 Ä‘Æ¡n - Overload]
    
    ScanOrders & ScanMenu & ScanDrivers --> AggregateResult[Tá»•ng há»£p dá»¯ liá»‡u káº¿t quáº£ thá»±c nghiá»‡m]
    AggregateResult --> SaveRunCsv[LÆ°u káº¿t quáº£ vÃ o simulation_runs.csv]
    SaveRunCsv --> RenderView[SimulatorView: In báº£ng so sÃ¡nh 8 cá»™t chuáº©n format]
    RenderView --> End([Káº¿t thÃºc kiá»ƒm thá»­])
```

### 8.4. Flowchart 4: Luá»“ng Sinh Dá»¯ Liá»‡u Tá»± Äá»™ng (Data Generator Flow)
```mermaid
flowchart TD
    Start([Cháº¡y DataGenerator.java]) --> CleanDir[Táº¡o hoáº·c lÃ m sáº¡ch thÆ° má»¥c data/]
    CleanDir --> GenCustomers[Sinh 2.000 Customers: Tá»a Ä‘á»™ ngáº«u nhiÃªn quanh TP.HCM/HÃ  Ná»™i]
    GenCustomers --> GenRestaurants[Sinh 200 Restaurants: Tá»a Ä‘á»™ trung tÃ¢m áº©m thá»±c]
    GenRestaurants --> GenMenuItems[Sinh 2.500 MenuItems: Gáº¯n vá»›i restaurantId, stockQty 10-50, version=0]
    GenMenuItems --> GenDrivers[Sinh 600 Drivers: Tá»a Ä‘á»™ ráº£i rÃ¡c quanh nhÃ  hÃ ng, status=AVAILABLE, version=0]
    GenDrivers --> GenOrders[Sinh 3.000 Orders lá»‹ch sá»­: PENDING, DELIVERED, version=0]
    GenOrders --> GenOrderItems[Sinh 9.500 OrderItems: Má»—i Ä‘Æ¡n cÃ³ 2-5 mÃ³n Äƒn, tÃ­nh Ä‘Ãºng unitPrice]
    GenOrderItems --> GenRoutes[Sinh 2.000 DeliveryRoutes: TÃ­nh distanceKm qua GeoUtils.haversine]
    GenRoutes --> VerifyTotal{Tá»•ng sá»‘ dÃ²ng >= 18.000 & KhÃ´ng lá»—i foreign key?}
    VerifyTotal -- Há»£p lá»‡ --> Success([Ghi thÃ nh cÃ´ng 8 file CSV])
    VerifyTotal -- Lá»—i --> Rollback[BÃ¡o lá»—i dá»¯ liá»‡u khÃ´ng nháº¥t quÃ¡n]
```

---

## ðŸ‘¥ 9. Báº¢NG PHÃ‚N CÃ”NG NHIá»†M Vá»¤ 6 THÃ€NH VIÃŠN & MA TRáº¬N 10 TUáº¦N (SPRINT MATRIX)

```
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚             THÃ€NH VIÃŠN 1: TEAM LEADER & SOFTWARE ARCHITECT             â”‚
â”‚    Base Architecture + Generic CsvRepository<T> + Git & Standard Modelsâ”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                                    â”‚ Cung cáº¥p BaseEntity & Generic CSV I/O
            â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”´â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
            â–¼                                               â–¼
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”               â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚  THÃ€NH VIÃŠN 2: CONCURRENCY    â”‚               â”‚  THÃ€NH VIÃŠN 3: HOT-SPOT REPOS â”‚
â”‚  & LOCK STRATEGY SPECIALIST   â”‚               â”‚  & GEO-SPATIAL ENGINE         â”‚
â”‚  4 CÆ¡ cháº¿ khÃ³a & LockManager  â”‚               â”‚  3 Critical Repos & Haversine â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜               â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                â”‚ Káº¿t há»£p cÆ¡ cháº¿ Ä‘á»“ng bá»™ hÃ³a vÃ o Repository     â”‚
                â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                                        â–¼
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚             THÃ€NH VIÃŠN 4: SIMULATOR ENGINE & CONCURRENCY ORCHESTRATOR  â”‚
â”‚      CustomerThread + DispatchThread + CountDownLatch + Benchmark      â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                                    â”‚ Dá»¯ liá»‡u sinh ra sau khi cháº¡y Simulator
            â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”´â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
            â–¼                                               â–¼
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”               â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚  THÃ€NH VIÃŠN 5: VERIFICATION   â”‚               â”‚  THÃ€NH VIÃŠN 6: CONTROLLERS,   â”‚
â”‚  & BIG DATA GENERATOR (CSV)   â”‚               â”‚  ASCII MAP & SIMULATOR VIEW   â”‚
â”‚  Äá»c trá»±c tiáº¿p CSV kiá»ƒm lá»—i   â”‚               â”‚  Äiá»u phá»‘i MVC & Báº£ng 8 cá»™t   â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜               â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
```

### 9.1. TrÃ¡ch Nhiá»‡m Chi Tiáº¿t Tá»«ng ThÃ nh ViÃªn

#### ðŸ‘¤ THÃ€NH VIÃŠN 1: LEADER & ARCHITECT (KIáº¾N TRÃšC & Ná»€N Táº¢NG)
- Quáº£n trá»‹ dá»± Ã¡n: Setup repo Git, branch protection trÃªn nhÃ¡nh `main`, code review.
- XÃ¢y dá»±ng `BaseEntity.java` (id, version, createdAt) vÃ  cÃ¡c Entity chuáº©n: `Customer`, `Restaurant`, `DeliveryRoute`.
- XÃ¢y dá»±ng Generic `IRepository<T>` vÃ  `CsvRepository<T extends BaseEntity>` xá»­ lÃ½ Ä‘á»c/ghi CSV báº±ng `try-with-resources`.
- XÃ¢y dá»±ng cÃ¡c Enum: `DriverStatus`, `OrderStatus`.

#### ðŸ‘¤ THÃ€NH VIÃŠN 2: CONCURRENCY SPECIALIST (Ká»¸ SÆ¯ Äá»’NG Bá»˜ HÃ“A)
- XÃ¢y dá»±ng Enum `LockMechanism`: `NO_LOCK`, `FILE_LOCK`, `SYNCHRONIZED`, `OPTIMISTIC`.
- Hiá»‡n thá»±c `ILockManager.java`, `FileLockManager.java` (FileChannel.lock) vÃ  `ResourceLockManager.java` (ConcurrentHashMap per-resource key).
- Hiá»‡n thá»±c háº¡ táº§ng kiá»ƒm tra version vÃ  thuáº­t toÃ¡n Retry cá»§a Optimistic Locking.
- Ãp dá»¥ng Global Lock Ordering Ä‘á»ƒ triá»‡t tiÃªu Deadlock giá»¯a Order vÃ  Driver.
- XÃ¢y dá»±ng há»‡ thá»‘ng 5 Custom Exceptions: `DoubleAssignmentException`, `OversellException`, `DriverOverloadException`, `OptimisticLockException`, `OrderCancellationException`.

#### ðŸ‘¤ THÃ€NH VIÃŠN 3: HOT-SPOT REPOS & GEO-SPATIAL (3 REPO ÄIá»‚M NÃ“NG & Tá»ŒA Äá»˜)
- Thuáº­t toÃ¡n `GeoUtils.java`: Hiá»‡n thá»±c cÃ´ng thá»©c Haversine chuáº©n ($R = 6371\text{ km}$), Unit Test khoáº£ng cÃ¡ch HÃ  Ná»™i - SÃ i GÃ²n $\approx 1130-1150\text{ km}$.
- XÃ¢y dá»±ng 3 Hot-Spot Entity: `MenuItem`, `Driver`, `Order`, `OrderItem` (cÃ³ trÆ°á»ng `version`).
- Hiá»‡n thá»±c 3 Critical Repositories tÃ­ch há»£p 4 cÆ¡ cháº¿ khÃ³a tá»« TV2:
  - `MenuItemRepository`: `deductStockWithSync`, `deductStockWithOptimistic`, `restoreStock`.
  - `DriverRepository`: `findNearestAvailable`, `markBusyWithSync`, `markBusyWithOptimistic`, `markAvailable`.
  - `OrderRepository`: `assignDriverWithSync`, `assignDriverWithOptimistic`, `cancelOrder`, `checkAndExpireOrders`.

#### ðŸ‘¤ THÃ€NH VIÃŠN 4: SIMULATOR ENGINE (Bá»˜ MÃ” PHá»ŽNG ÄA LUá»’NG & BENCHMARK)
- XÃ¢y dá»±ng `CustomerThread.java` (mÃ´ phá»ng Ä‘áº·t mÃ³n liÃªn tá»¥c) vÃ  `DispatchThread.java` (mÃ´ phá»ng quÃ©t vÃ  gÃ¡n xe liÃªn tá»¥c).
- XÃ¢y dá»±ng `SimulationEngine.java` vá»›i `ExecutorService` vÃ  2 `CountDownLatch` (`readyLatch`, `startLatch`).
- Äo Ä‘áº¡c thá»i gian cháº¡y chÃ­nh xÃ¡c `System.nanoTime()`, tÃ­nh toÃ¡n Throughput (ops/sec) vÃ  % sá»¥t giáº£m Throughput so vá»›i baseline `NO_LOCK`.
- Cháº¡y thá»±c nghiá»‡m 3 láº§n láº¥y giÃ¡ trá»‹ trung bÃ¬nh (theo yÃªu cáº§u Tuáº§n 9), lÆ°u káº¿t quáº£ vÃ o `simulation_runs.csv`.

#### ðŸ‘¤ THÃ€NH VIÃŠN 5: VERIFICATION & BIG DATA GENERATOR (KIá»‚M Äá»ŠNH & Dá»® LIá»†U)
- XÃ¢y dá»±ng `DataGenerator.java`: Tá»± Ä‘á»™ng sinh $\ge 18.000$ dÃ²ng dá»¯ liá»‡u máº«u chuáº©n cho 8 file CSV vá»›i tá»a Ä‘á»™ thá»±c táº¿ vÃ  khÃ³a ngoáº¡i khá»›p nhau.
- XÃ¢y dá»±ng `VerificationService.java`: Bá»™ tháº©m Ä‘á»‹nh Ä‘á»™c láº­p Ä‘á»c trá»±c tiáº¿p cÃ¡c file CSV váº­t lÃ½ sau khi cháº¡y:
  - `detectDoubleAssignments()`
  - `detectOversellItems()`
  - `detectDriverOverload()`

#### ðŸ‘¤ THÃ€NH VIÃŠN 6: CONTROLLERS & ASCII VIEWS (ÄIá»€U PHá»I & GIAO DIá»†N)
- XÃ¢y dá»±ng Controller Layer chuáº©n MVC: `OrderController`, `DriverController`, `RestaurantController`, `SimulatorController` (tuyá»‡t Ä‘á»‘i khÃ´ng chá»©a business logic).
- XÃ¢y dá»±ng View Console tÆ°Æ¡ng tÃ¡c: `MainView.java`, `OrderView.java`.
- XÃ¢y dá»±ng `MapView.java`: Váº½ lÆ°á»›i tá»a Ä‘á»™ ASCII $20 \times 20$ hiá»ƒn thá»‹ vá»‹ trÃ­ thá»i gian thá»±c cá»§a KhÃ¡ch hÃ ng (`C`), NhÃ  hÃ ng (`R`), vÃ  TÃ i xáº¿ (`D`) qua cÃ´ng thá»©c chiáº¿u Min-Max.
- XÃ¢y dá»±ng `SimulatorView.java`: Hiá»ƒn thá»‹ báº£ng so sÃ¡nh thá»±c nghiá»‡m chuáº©n 8 cá»™t.

---

### 9.2. Ma Tráº­n Phá»‘i Há»£p 10 Tuáº§n (10-Week Sprint Matrix)

| Tuáº§n | Má»¥c tiÃªu Tuáº§n (Milestone) | TV1 | TV2 | TV3 | TV4 | TV5 | TV6 |
| :---: | :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| **T1** | PhÃ¢n tÃ­ch Ä‘á» bÃ i, váº½ Use Case & Class Diagram | Leader setup | PhÃ¢n tÃ­ch 3 Race | PhÃ¢n tÃ­ch 8 Schema | PhÃ¢n tÃ­ch Simulator | Thiáº¿t káº¿ Schema CSV | Thiáº¿t káº¿ UI Menu |
| **T2** | HoÃ n thiá»‡n Schema CSV & Viáº¿t DataGenerator | Setup Git Repo | Táº¡o 5 Exceptions | Viáº¿t GeoUtils | Thiáº¿t káº¿ Latch | **Viáº¿t DataGen (18k dÃ²ng)** | PhÃ¡c tháº£o ASCII Map |
| **T3** | Model Layer + Generic CsvRepository | **BaseEntity, Repo generic**| Táº¡o LockMechanism | **Hot-Spot Entities** | Model SimulationRun | Test parse CSV round-trip | Táº¡o Menu Ä‘iá»u hÆ°á»›ng |
| **T4** | Critical Repositories (Checkpoint sá»‘ng cÃ²n) | Test Repo generic | **Viáº¿t LockManager** | **Viáº¿t 3 Critical Repos** | Viáº¿t thread giáº£ láº­p Ä‘Æ¡n | Test tÃ­nh nháº¥t quÃ¡n Data | Ná»‘i Controller cÆ¡ báº£n |
| **T5** | Controller Layer & Single Thread Flow | Code review | Há»— trá»£ sync logic | Test single thread | Äo thá»i gian cháº¡y | Viáº¿t hÃ m check lá»—i CSV | **HoÃ n thiá»‡n 4 Controllers** |
| **T6** | View Layer + ASCII Map + MVC Wiring | Quáº£n trá»‹ merge PR | Test thá»­ 5 threads | Tá»‘i Æ°u Haversine | Test cháº¡y nhiá»u Ä‘Æ¡n | Test Ä‘á»c lá»—i CSV | **Váº½ MapView ASCII 20x20** |
| **T7** | HoÃ n thiá»‡n 4 Synchronization Mechanisms | Review kiáº¿n trÃºc | **HoÃ n thiá»‡n 4 CÆ¡ cháº¿**| **TÃ­ch há»£p 4 CÆ¡ cháº¿** | Thá»­ nghiá»‡m 10 threads | Viáº¿t VerificationService | HoÃ n thiá»‡n MainView |
| **T8** | Simulator Engine Äa luá»“ng (CountDownLatch) | Quáº£n trá»‹ test | Kiá»ƒm tra dead-lock | Tá»‘i Æ°u Hot-Spot | **HoÃ n thiá»‡n Simulator** | **Cháº¡y VerificationService**| **SimulatorView báº£ng 8 cá»™t**|
| **T9** | Cháº¡y Thá»±c Nghiá»‡m (1000 orders x 3 láº§n) | Viáº¿t bÃ¡o cÃ¡o Word | PhÃ¢n tÃ­ch káº¿t quáº£ khÃ³a | Thá»‘ng kÃª tá»a Ä‘á»™ | TÃ­nh Throughput trung bÃ¬nh | Thá»‘ng kÃª sá»‘ lÆ°á»£ng lá»—i | **LÃ m Slide thuyáº¿t trÃ¬nh** |
| **T10**| ÄÃ³ng gÃ³i ZIP, AI Log cÃ¡ nhÃ¢n & Ná»™p bÃ i | **ÄÃ³ng gÃ³i ZIP** | **Ná»™p AI Log cÃ¡ nhÃ¢n** | **Ná»™p AI Log cÃ¡ nhÃ¢n** | **Ná»™p AI Log cÃ¡ nhÃ¢n** | **Ná»™p AI Log cÃ¡ nhÃ¢n** | **Ná»™p AI Log cÃ¡ nhÃ¢n** |

---

## ðŸ›¡ 10. 4 NGUYÃŠN Táº®C THÃ‰P PHá»I Há»¢P & QUY TRÃŒNH GIT

1. **NguyÃªn Táº¯c KhÃ´ng ThÆ° Viá»‡n NgoÃ i (Pure Java SE Rule):**  
   ToÃ n bá»™ mÃ£ nguá»“n pháº£i biÃªn dá»‹ch báº±ng lá»‡nh `javac` cá»§a JDK chuáº©n. Tuyá»‡t Ä‘á»‘i khÃ´ng cÃ i thÃªm dependency (Lombok, Gson, Jackson, Spring...).
2. **NguyÃªn Táº¯c Ranh Giá»›i MVC Tuyá»‡t Äá»‘i (Strict MVC Boundary Rule):**  
   Controller chá»‰ Ä‘iá»u phá»‘i, View chá»‰ in mÃ n hÃ¬nh. Má»i tÃ­nh toÃ¡n tá»a Ä‘á»™, tÃ¬m kiáº¿m tÃ i xáº¿, trá»« tá»“n kho **báº¯t buá»™c náº±m trong Repository hoáº·c Util**. Vi pháº¡m sáº½ bá»‹ trá»« 5% má»—i láº§n phÃ¡t hiá»‡n!
3. **NguyÃªn Táº¯c Tháº©m Äá»‹nh Trá»±c Tiáº¿p Tá»« File (CSV Verification Rule):**  
   Káº¿t quáº£ Ä‘o Ä‘áº¿m lá»—i cá»§a Simulator pháº£i Ä‘Æ°á»£c Ä‘á»c tá»« file `.csv` váº­t lÃ½ sau khi cháº¡y, khÃ´ng Ä‘Æ°á»£c Ä‘áº¿m qua biáº¿n RAM.
4. **Quy TrÃ¬nh Git & NhÃ¡nh TÃ­nh NÄƒng:**  
   - KhÃ³a nhÃ¡nh `main`. Má»—i thÃ nh viÃªn code trÃªn nhÃ¡nh riÃªng: `feature/tv<sá»‘>-<tÃªn-chá»©c-nÄƒng>`.
   - Táº¡o Pull Request (PR) $\rightarrow$ Leader (TV1) review vÃ  cháº¡y thá»­ khÃ´ng cÃ³ lá»—i má»›i Ä‘Æ°á»£c merge.

---

## ðŸ”’ 11. CÆ  CHáº¾ "KHÃ“A AI VÃ€O FLOW THEO QUY LUáº¬T" (AI GUARDRAILS)

### 11.1. Báº«y Lá»—i Nguy Hiá»ƒm Khi DÃ¹ng AI Trong BÃ i ToÃ¡n Concurrency LAB211
1. **DÃ¹ng sai pháº¡m vi khÃ³a (`synchronized(this)`):** AI thÆ°á»ng thÃªm `synchronized` vÃ o method cá»§a Repository, táº¡o thÃ nh Global Lock lÃ m táº¯c ngháº½n toÃ n bá»™ luá»“ng, throughput sá»¥t giáº£m tá»›i 90%.
2. **Vi pháº¡m kiáº¿n trÃºc MVC (-5% má»—i láº§n phÃ¡t hiá»‡n):** AI tiá»‡n tay viáº¿t logic tÃ¬m tÃ i xáº¿ hoáº·c gá»i Haversine ngay trong `Controller`.
3. **QuÃªn `CountDownLatch` (-8% Ä‘iá»ƒm):** AI dÃ¹ng `Thread.sleep()` hoáº·c vÃ²ng láº·p tuáº§n tá»± khiáº¿n cÃ¡c thread khÃ´ng thá»±c sá»± Ä‘á»“ng thá»i, dáº«n Ä‘áº¿n káº¿t quáº£ `NO_LOCK` khÃ´ng phÃ¡t sinh lá»—i.
4. **Sai cÃ´ng thá»©c Haversine (-3% Ä‘iá»ƒm):** AI nháº§m láº«n sin/cos hoáº·c quÃªn Ä‘á»•i sang Radian, dáº«n Ä‘áº¿n tÃ­nh khoáº£ng cÃ¡ch sai hoÃ n toÃ n.
5. **DÃ¹ng thÆ° viá»‡n ngoÃ i (0% toÃ n bÃ i):** AI tá»± Ã½ import Lombok (`@Data`, `@AllArgsConstructor`) hoáº·c Gson.

### 11.2. MASTER AI PROMPT GUARDRAIL (Báº®T BUá»˜C COPY Äáº¦U Má»–I ÄOáº N CHAT)

```markdown
Báº N LÃ€ TRá»¢ LÃ Láº¬P TRÃŒNH CHUYÃŠN SÃ‚U CHO Äá»’ ÃN LAB211: "FOOD DELIVERY NETWORK SIMULATION" (JAVA OOP NÃ‚NG CAO - FPT UNIVERSITY).

Báº N Báº®T BUá»˜C TUÃ‚N THá»¦ 100% CÃC NGUYÃŠN Táº®C THÃ‰P SAU ÄÃ‚Y VÃ€ KHÃ”NG ÄÆ¯á»¢C VI PHáº M:

1. RÃ€NG BUá»˜C CÃ”NG NGHá»†:
   - CHá»ˆ Sá»¬ Dá»¤NG JAVA THUáº¦N (Java SE JDK 8/11/17).
   - TUYá»†T Äá»I KHÃ”NG dÃ¹ng thÆ° viá»‡n bÃªn ngoÃ i: KHÃ”NG Lombok, KHÃ”NG Gson, KHÃ”NG Jackson, KHÃ”NG Spring, KHÃ”NG Apache Commons.
   - Thao tÃ¡c file CSV chá»‰ dÃ¹ng BufferedReader, BufferedWriter trong try-with-resources.

2. RÃ€NG BUá»˜C KIáº¾N TRÃšC MVC (TRá»ª -5% Náº¾U VI PHáº M):
   - Logic tÃ¬m tÃ i xáº¿ gáº§n nháº¥t (findNearestAvailable), trá»« tá»“n kho (deductStock), gÃ¡n Ä‘Æ¡n (assignDriver), tÃ­nh khoáº£ng cÃ¡ch Haversine Báº®T BUá»˜C Náº°M TRONG REPOSITORY HOáº¶C UTIL.
   - Controller CHá»ˆ Ä‘iá»u phá»‘i flow, KHÃ”NG chá»©a business logic, KHÃ”NG Ä‘á»c/ghi CSV trá»±c tiáº¿p.
   - View CHá»ˆ nháº­n input vÃ  hiá»ƒn thá»‹ Console ASCII.

3. RÃ€NG BUá»˜C CONCURRENCY & SIMULATOR (TRá»ª -13% Náº¾U VI PHáº M):
   - Simulator Báº®T BUá»˜C cÃ³ 2 loáº¡i thread: CustomerThread vÃ  DispatchThread.
   - Báº®T BUá»˜C dÃ¹ng java.util.concurrent.CountDownLatch Ä‘á»ƒ kÃ­ch hoáº¡t Ä‘á»“ng thá»i hÃ ng trÄƒm thread cÃ¹ng mili-giÃ¢y.
   - Äá»‘i vá»›i cÆ¡ cháº¿ SYNCHRONIZED: KHÃ”NG ÄÆ¯á»¢C dÃ¹ng synchronized(this), Báº®T BUá»˜C dÃ¹ng Per-Resource Lock (ConcurrentHashMap quáº£n lÃ½ Object lock theo Key ID). Khi khÃ³a nhiá»u tÃ i nguyÃªn, Ã¡p dá»¥ng Global Lock Ordering Ä‘á»ƒ chá»‘ng Deadlock.
   - Äáº¿m lá»—i Double Assignment, Oversell, Driver Overload Báº®T BUá»˜C Ä‘á»c trá»±c tiáº¿p tá»« file CSV váº­t lÃ½ sau khi cháº¡y xong.

4. Báº¢N Há»¢P Äá»’NG Dá»® LIá»†U (DATA CONTRACT):
   - 3 Entity MenuItem, Driver, Order Báº®T BUá»˜C cÃ³ trÆ°á»ng `version` (int) Ä‘á»ƒ há»— trá»£ Optimistic Locking.
   - TÃªn trÆ°á»ng vÃ  cáº¥u trÃºc dÃ²ng CSV pháº£i tuyá»‡t Ä‘á»‘i chuáº©n xÃ¡c theo schema 8 file.

XÃ¡c nháº­n hiá»ƒu rÃµ cÃ¡c quy luáº­t trÃªn vÃ  sáºµn sÃ ng há»— trá»£ thÃ nh viÃªn nhÃ³m theo Ä‘Ãºng pháº¡m vi Ä‘Æ°á»£c giao!
```

---

## ðŸ¤– 12. Bá»˜ 6 PROMPT MáºªU ÄÃ“NG GÃ“I RIÃŠNG CHO 6 THÃ€NH VIÃŠN

### ðŸ‘¤ 12.1. Prompt KhÃ³a AI cho TV1 (Architecture & Base Models)
```markdown
[DÃ¡n Master AI Prompt á»Ÿ trÃªn]
TÃ´i lÃ  THÃ€NH VIÃŠN 1 (Leader - Phá»¥ trÃ¡ch Base Architecture & Base Models).
HÃ£y giÃºp tÃ´i viáº¿t:
1. `BaseEntity.java` trá»«u tÆ°á»£ng vá»›i cÃ¡c trÆ°á»ng: id (String), version (int), createdAt (String).
2. CÃ¡c Entity: `Customer.java`, `Restaurant.java`, `DeliveryRoute.java` káº¿ thá»«a BaseEntity, cÃ³ Ä‘áº§y Ä‘á»§ getter/setter, `toCsvLine()` vÃ  `fromCsvLine(String line)`.
3. Enum `DriverStatus.java` (AVAILABLE, BUSY, OFFLINE) vÃ  `OrderStatus.java` (PENDING, ASSIGNED, PICKED_UP, DELIVERED, CANCELLED, EXPIRED).
4. Generic Interface `IRepository.java` vÃ  Generic Class `CsvRepository<T extends BaseEntity>` xá»­ lÃ½ CRUD vá»›i file CSV qua try-with-resources.
5. CÃ¡c Repository: `CustomerRepository`, `RestaurantRepository`, `DeliveryRouteRepository`.
Code Java thuáº§n, khÃ´ng Lombok, Ä‘Ãºng chuáº©n OOP.
```

---

### ðŸ‘¤ 12.2. Prompt KhÃ³a AI cho TV2 (Concurrency & Lock Mechanisms)
```markdown
[DÃ¡n Master AI Prompt á»Ÿ trÃªn]
TÃ´i lÃ  THÃ€NH VIÃŠN 2 (Phá»¥ trÃ¡ch Concurrency & 4 CÆ¡ cháº¿ khÃ³a).
HÃ£y giÃºp tÃ´i viáº¿t:
1. Enum `LockMechanism.java` gá»“m: NO_LOCK, FILE_LOCK, SYNCHRONIZED, OPTIMISTIC.
2. `ILockManager.java`: Interface chung cho cÆ¡ cháº¿ khÃ³a.
3. `FileLockManager.java`: CÆ¡ cháº¿ khÃ³a file CSV váº­t lÃ½ dÃ¹ng FileChannel.open() vÃ  FileLock.
4. `ResourceLockManager.java`: CÆ¡ cháº¿ Per-Resource Lock dÃ¹ng ConcurrentHashMap<String, Object> khÃ³a má»‹n theo ID tÃ i nguyÃªn (Order ID, Driver ID, Item ID), Ã¡p dá»¥ng Global Lock Ordering trÃ¡nh Deadlock.
5. CÆ¡ cháº¿ Optimistic Lock: Kiá»ƒm tra version trÆ°á»›c khi cáº­p nháº­t, náº¿u version lá»‡ch thÃ¬ nÃ©m OptimisticLockException, cÃ³ hÃ m Retry vá»›i Exponential Jitter Backoff.
6. 5 Custom Exceptions: `DoubleAssignmentException`, `OversellException`, `DriverOverloadException`, `OptimisticLockException`, `OrderCancellationException`.
```

---

### ðŸ‘¤ 12.3. Prompt KhÃ³a AI cho TV3 (Hot-Spot Repositories & Haversine)
```markdown
[DÃ¡n Master AI Prompt á»Ÿ trÃªn]
TÃ´i lÃ  THÃ€NH VIÃŠN 3 (Phá»¥ trÃ¡ch 3 Critical Repositories & Thuáº­t toÃ¡n Tá»a Ä‘á»™).
HÃ£y giÃºp tÃ´i viáº¿t:
1. `GeoUtils.java`: CÃ´ng thá»©c Haversine tÃ­nh khoáº£ng cÃ¡ch giá»¯a 2 tá»a Ä‘á»™ GPS (BÃ¡n kÃ­nh R = 6371 km). Viáº¿t hÃ m test khoáº£ng cÃ¡ch HÃ  Ná»™i - SÃ i GÃ²n ra khoáº£ng 1130-1150km.
2. CÃ¡c Hot-Spot Entity: `MenuItem.java`, `Driver.java`, `Order.java`, `OrderItem.java` (cÃ³ trÆ°á»ng version int, toCsvLine, fromCsvLine).
3. 3 Critical Repositories:
   - `MenuItemRepository.java`: deductStockWithSync, deductStockWithOptimistic, restoreStock.
   - `DriverRepository.java`: findNearestAvailable(lat, lon), markBusyWithSync, markBusyWithOptimistic, markAvailable.
   - `OrderRepository.java`: assignDriverWithSync, assignDriverWithOptimistic, cancelOrder, checkAndExpireOrders (timeout 5 phÃºt).
   TÃ­ch há»£p cáº£ 4 cÆ¡ cháº¿ khÃ³a tá»« TV2.
```

---

### ðŸ‘¤ 12.4. Prompt KhÃ³a AI cho TV4 (Simulator Engine & Benchmark)
```markdown
[DÃ¡n Master AI Prompt á»Ÿ trÃªn]
TÃ´i lÃ  THÃ€NH VIÃŠN 4 (Phá»¥ trÃ¡ch Simulator Engine & Concurrency Orchestrator).
HÃ£y giÃºp tÃ´i viáº¿t:
1. `CustomerThread.java`: Luá»“ng khÃ¡ch hÃ ng liÃªn tá»¥c gá»­i yÃªu cáº§u Ä‘áº·t mÃ³n (placeOrder).
2. `DispatchThread.java`: Luá»“ng há»‡ thá»‘ng liÃªn tá»¥c tÃ¬m tÃ i xáº¿ gáº§n nháº¥t vÃ  gÃ¡n Ä‘Æ¡n (assignDriver).
3. `SimulationEngine.java`:
   - DÃ¹ng ThreadPool (ExecutorService) vÃ  2 CountDownLatch: readyLatch vÃ  startLatch Ä‘á»ƒ cÃ¡c thread xuáº¥t phÃ¡t Ä‘á»“ng thá»i 100%.
   - Nháº­n tham sá»‘: tá»•ng sá»‘ Ä‘Æ¡n hÃ ng (orders), cÆ¡ cháº¿ khÃ³a (LockMechanism).
   - Äo thá»i gian cháº¡y chÃ­nh xÃ¡c báº±ng System.nanoTime(), tÃ­nh Throughput (ops/sec) vÃ  % Throughput Drop so vá»›i baseline NO_LOCK.
   - Cháº¡y 3 láº§n láº¥y giÃ¡ trá»‹ trung bÃ¬nh theo yÃªu cáº§u bÃ¡o cÃ¡o Tuáº§n 9.
4. `SimulationRun.java` vÃ  `SimulationRunRepository.java` Ä‘á»ƒ lÆ°u láº¡i káº¿t quáº£ thá»±c nghiá»‡m vÃ o simulation_runs.csv.
```

---

### ðŸ‘¤ 12.5. Prompt KhÃ³a AI cho TV5 (Verification & Big Data Generator)
```markdown
[DÃ¡n Master AI Prompt á»Ÿ trÃªn]
TÃ´i lÃ  THÃ€NH VIÃŠN 5 (Phá»¥ trÃ¡ch Verification & Data Generator).
HÃ£y giÃºp tÃ´i viáº¿t:
1. `DataGenerator.java`: Sinh tá»± Ä‘á»™ng â‰¥ 18.000 dÃ²ng dá»¯ liá»‡u máº«u chuáº©n cho 8 file CSV:
   - customers.csv (2000 dÃ²ng), restaurants.csv (200 dÃ²ng), menu_items.csv (2500 dÃ²ng), drivers.csv (600 dÃ²ng), orders.csv (3000 dÃ²ng), order_items.csv (â‰¥ 9500 dÃ²ng), delivery_routes.csv (2000 dÃ²ng).
   - Tá»a Ä‘á»™ thá»±c táº¿ khu vá»±c TP.HCM/HÃ  Ná»™i, Ä‘áº£m báº£o khÃ³a ngoáº¡i giá»¯a cÃ¡c file khá»›p nhau 100% (mÃ³n Äƒn trong order_items pháº£i Ä‘Ãºng cá»§a restaurant Ä‘Ã³).
2. `VerificationService.java`: Bá»™ tháº©m Ä‘á»‹nh Ä‘á»™c láº­p Ä‘á»c trá»±c tiáº¿p tá»« file CSV váº­t lÃ½ sau khi cháº¡y:
   - detectDoubleAssignments(): QuÃ©t orders.csv Ä‘áº¿m sá»‘ Ä‘Æ¡n cÃ³ nhiá»u tÃ i xáº¿ gÃ¡n.
   - detectOversellItems(): QuÃ©t menu_items.csv Ä‘áº¿m sá»‘ mÃ³n cÃ³ stockQty < 0.
   - detectDriverOverload(): Äá»‘i chiáº¿u orders.csv vÃ  drivers.csv Ä‘áº¿m sá»‘ tÃ i xáº¿ bá»‹ gÃ¡n cÃ¹ng lÃºc â‰¥ 2 Ä‘Æ¡n.
```

---

### ðŸ‘¤ 12.6. Prompt KhÃ³a AI cho TV6 (MVC Controllers, ASCII Map & Simulator View)
```markdown
[DÃ¡n Master AI Prompt á»Ÿ trÃªn]
TÃ´i lÃ  THÃ€NH VIÃŠN 6 (Phá»¥ trÃ¡ch Controllers & Console UI View).
HÃ£y giÃºp tÃ´i viáº¿t:
1. CÃ¡c Controller chuáº©n MVC: `OrderController.java`, `DriverController.java`, `RestaurantController.java`, `SimulatorController.java`. Tuyá»‡t Ä‘á»‘i khÃ´ng chá»©a logic nghiá»‡p vá»¥ hay Ä‘á»c/ghi file CSV, chá»‰ Ä‘iá»u phá»‘i giá»¯a View vÃ  Repository.
2. `MainView.java`: Menu tÆ°Æ¡ng tÃ¡c Console trá»±c quan.
3. `MapView.java`: Váº½ lÆ°á»›i tá»a Ä‘á»™ ASCII 20x20 hiá»ƒn thá»‹ KhÃ¡ch hÃ ng (C), NhÃ  hÃ ng (R), TÃ i xáº¿ (D) qua cÃ´ng thá»©c chiáº¿u Min-Max.
4. `SimulatorView.java`: Hiá»ƒn thá»‹ báº£ng so sÃ¡nh thá»±c nghiá»‡m chuáº©n 8 cá»™t:
   | Mechanism | Orders | TimeMs | Throughput | Double Assign | Oversell | DriverOverload | % Drop |
```

---

## ðŸ“ 13. HÆ¯á»šNG DáºªN AI LOG CÃ NHÃ‚N Äáº T Má»¨C XUáº¤T Sáº®C (27-30% ÄIá»‚M)

> [!CAUTION]
> **TIÃŠU CHÃ ÄIá»‚M CÃ NHÃ‚N (30% ÄIá»‚M Sá»):**  
> Má»—i thÃ nh viÃªn **báº¯t buá»™c** pháº£i ná»™p 1 file log cÃ¡ nhÃ¢n trong thÆ° má»¥c `ai_logs/member<X>_ai_log.md` ghi láº¡i toÃ n bá»™ lá»‹ch sá»­ tÆ°Æ¡ng tÃ¡c vá»›i AI kÃ¨m timestamp thá»±c táº¿ qua 10 tuáº§n. ThÃ nh viÃªn nÃ o thiáº¿u file nÃ y sáº½ bá»‹ **0 Ä‘iá»ƒm** pháº§n AI Reflection dÃ¹ Ä‘iá»ƒm nhÃ³m cÃ³ Ä‘áº¡t 100%!

### Máº«u File AI Log Chuáº©n (`ai_logs/member1_ai_log.md`):
```markdown
# AI LOG â€” THÃ€NH VIÃŠN 1: NGUYá»„N VÄ‚N A (MSSV: SE170001)

## PhiÃªn lÃ m viá»‡c ngÃ y: 2026-09-12 09:15:30
- **Má»¥c tiÃªu:** Viáº¿t generic CsvRepository trong Java Ä‘á»ƒ Ä‘á»c ghi file CSV.
- **Prompt:** "Viáº¿t generic CsvRepository trong Java Ä‘á»ƒ Ä‘á»c ghi file CSV."
- **AI Tráº£ lá»i:** [TrÃ­ch Ä‘oáº¡n code AI]
- **Lá»—i phÃ¡t hiá»‡n:** AI dÃ¹ng FileWriter mÃ  khÃ´ng bá»c trong try-with-resources, cÃ³ nguy cÆ¡ rÃ² rá»‰ file descriptor khi cÃ³ nhiá»u luá»“ng cÃ¹ng má»Ÿ file.
- **CÃ¡ch sá»­a cá»§a tÃ´i:** Sá»­a láº¡i dÃ¹ng `try (BufferedWriter bw = Files.newBufferedWriter(path))`.

## PhiÃªn lÃ m viá»‡c ngÃ y: 2026-09-25 15:40:12
- **Má»¥c tiÃªu:** Parse dÃ²ng CSV sang Entity an toÃ n.
- **Prompt:** "LÃ m sao Ä‘á»ƒ map dÃ²ng CSV thÃ nh Entity mÃ  khÃ´ng bá»‹ IndexOutOfBoundsException?"
- **AI Tráº£ lá»i:** AI Ä‘á» xuáº¥t dÃ¹ng regex split(",").
- **Lá»—i phÃ¡t hiá»‡n:** Khi dÃ²ng CSV cÃ³ trÆ°á»ng rá»—ng á»Ÿ cuá»‘i, split(",") máº·c Ä‘á»‹nh sáº½ bá» qua cÃ¡c pháº§n tá»­ rá»—ng, lÃ m máº£ng bá»‹ thiáº¿u Ä‘á»™ dÃ i.
- **CÃ¡ch sá»­a cá»§a tÃ´i:** DÃ¹ng `line.split(",", -1)` Ä‘á»ƒ giá»¯ nguyÃªn cÃ¡c trÆ°á»ng rá»—ng cuá»‘i dÃ²ng.
```

---

## ðŸ’» 14. HÆ¯á»šNG DáºªN BIÃŠN Dá»ŠCH, CHáº Y THá»¬ & Cáº¤U TRÃšC ÄÃ“NG GÃ“I Ná»˜P BÃ€I (ZIP)

### 14.1. Lá»‡nh BiÃªn Dá»‹ch & Cháº¡y Báº±ng CMD / PowerShell (Pure Java SE)
```bash
# 1. Di chuyá»ƒn vÃ o thÆ° má»¥c gá»‘c dá»± Ã¡n
cd NHOM_01_LAB211_FoodDelivery

# 2. BiÃªn dá»‹ch toÃ n bá»™ mÃ£ nguá»“n vÃ o thÆ° má»¥c bin/
javac -d bin -sourcepath src src/model/entity/*.java src/repository/base/*.java src/repository/critical/*.java src/controller/*.java src/view/*.java src/simulator/*.java src/util/*.java

# 3. Cháº¡y chÆ°Æ¡ng trÃ¬nh sinh dá»¯ liá»‡u máº«u (â‰¥ 18.000 dÃ²ng CSV)
java -cp bin util.DataGenerator

# 4. Khá»Ÿi cháº¡y á»©ng dá»¥ng chÃ­nh (Console UI)
java -cp bin view.MainView
```

### 14.2. Cáº¥u TrÃºc File ZIP Ná»™p BÃ i Äáº¡t Chuáº©n 100%
```text
NHOM_01_LAB211_FoodDelivery.zip
â”œâ”€â”€ src/
â”‚   â”œâ”€â”€ model/               â† Entity, Enum, BaseEntity, GeoUtils
â”‚   â”œâ”€â”€ repository/          â† CsvRepository<T>, 7 Repo cá»¥ thá»ƒ, LockManagers
â”‚   â”œâ”€â”€ controller/          â† OrderController, DriverController, SimulatorController...
â”‚   â”œâ”€â”€ view/                â† MainView, OrderView, MapView, SimulatorView
â”‚   â”œâ”€â”€ simulator/           â† CustomerThread, DispatchThread, SimulationEngine, VerificationService
â”‚   â”œâ”€â”€ exception/           â† 5 Custom Exceptions
â”‚   â””â”€â”€ util/                â† DataGenerator.java, IdGenerator.java
â”œâ”€â”€ data/
â”‚   â”œâ”€â”€ customers.csv         (â‰¥ 2.000 dÃ²ng)
â”‚   â”œâ”€â”€ restaurants.csv       (â‰¥ 200 dÃ²ng)
â”‚   â”œâ”€â”€ menu_items.csv        (â‰¥ 2.000 dÃ²ng)
â”‚   â”œâ”€â”€ drivers.csv           (â‰¥ 500 dÃ²ng)
â”‚   â”œâ”€â”€ orders.csv            (â‰¥ 3.000 dÃ²ng)
â”‚   â”œâ”€â”€ order_items.csv       (â‰¥ 9.000 dÃ²ng)
â”‚   â”œâ”€â”€ delivery_routes.csv   (â‰¥ 2.000 dÃ²ng)
â”‚   â””â”€â”€ simulation_runs.csv   (káº¿t quáº£ Simulator)
â”œâ”€â”€ docs/
â”‚   â”œâ”€â”€ report.docx           â† BÃ¡o cÃ¡o Word Ä‘áº§y Ä‘á»§ theo máº«u FPTU
â”‚   â”œâ”€â”€ slide.pptx            â† Slide thuyáº¿t trÃ¬nh báº£o vá»‡ Ä‘á»“ Ã¡n
â”‚   â”œâ”€â”€ class_diagram.png     â† UML Class Diagram xuáº¥t tá»« cÃ´ng cá»¥
â”‚   â””â”€â”€ flowcharts/           â† 4 flowchart PNG/PDF (Order&Dispatch, RaceCondition, Simulator, DataGen)
â”œâ”€â”€ ai_logs/
â”‚   â”œâ”€â”€ member1_ai_log.md     â† AI Log cÃ¡ nhÃ¢n (raw, timestamp thá»±c)
â”‚   â”œâ”€â”€ member2_ai_log.md
â”‚   â”œâ”€â”€ member3_ai_log.md
â”‚   â”œâ”€â”€ member4_ai_log.md
â”‚   â”œâ”€â”€ member5_ai_log.md
â”‚   â””â”€â”€ member6_ai_log.md
â””â”€â”€ README.md                 â† HÆ°á»›ng dáº«n biÃªn dá»‹ch, cháº¡y DataGenerator, cháº¡y Simulator 4 cÆ¡ cháº¿
```

---

## ðŸ 15. CHECKLIST NGHIá»†M THU & Bá»˜ TIÃŠU CHÃ RUBRIC (DoD)

| TiÃªu ChÃ­ Rubric | Trá»ng Sá»‘ | YÃªu Cáº§u ÄÃ¡nh GiÃ¡ | ThÃ nh ViÃªn Phá»¥ TrÃ¡ch | Tráº¡ng ThÃ¡i |
| :--- | :---: | :--- | :---: | :---: |
| **1. Decomposition** | **20%** | TÃ¡ch Ä‘Ãºng cÃ¡c lá»›p SRP. Logic dispatch vÃ  trá»« kho chá»‰ náº±m trong Repository â€” tuyá»‡t Ä‘á»‘i khÃ´ng á»Ÿ Controller/View. XÃ¡c Ä‘á»‹nh Ä‘Ãºng 8 Entity vÃ  quan há»‡. | TV1, TV3, TV6 | [ ] Äáº¡t chuáº©n |
| **2. Abstraction** | **15%** | UML Class Diagram Ä‘áº§y Ä‘á»§ + 4 Flowcharts Ä‘Ãºng chuáº©n. Káº¿ thá»«a `BaseEntity`. `CsvRepository<T>` dÃ¹ng cho 7 Entity. DÃ¹ng Enum thay String. | TV1, TV2, Cáº£ nhÃ³m | [ ] Äáº¡t chuáº©n |
| **3. Algorithm Design** | **15%** | CRUD Ä‘áº§y Ä‘á»§. Haversine distance Ä‘Ãºng cÃ´ng thá»©c. `findNearestAvailable` Ä‘Ãºng. File I/O try-with-resources. Äá»§ $\ge 5$ custom exceptions. `DataGenerator` $\ge 18.000$ dÃ²ng. | TV3, TV2, TV5 | [ ] Äáº¡t chuáº©n |
| **4. Simulator Design** | **20%** | Simulator cÃ³ 2 loáº¡i thread Ä‘á»“ng thá»i qua `CountDownLatch`. Äá»§ 4 cÆ¡ cháº¿ khÃ³a. `VerificationService` Ä‘á»c trá»±c tiáº¿p CSV. `NO_LOCK` cÃ³ Ä‘á»§ 3 loáº¡i lá»—i. Báº£ng so sÃ¡nh 8 cá»™t. | TV4, TV5, TV6 | [ ] Äáº¡t chuáº©n |
| **5. AI Reflection** | **30%** | Äá»§ 6 file AI Log cÃ¡ nhÃ¢n cÃ³ timestamp thá»±c táº¿ qua 10 tuáº§n. BÃ i Reflection $\ge 500$ tá»«/ngÆ°á»i phÃ¢n tÃ­ch sÃ¢u lá»—i concurrency cá»§a AI. | Cáº£ 6 thÃ nh viÃªn | [ ] Äáº¡t chuáº©n |
| **Tá»”NG Cá»˜NG** | **100%** | **Sáº´N SÃ€NG Äáº T ÄIá»‚M TUYá»†T Äá»I Báº¢O Vá»† Äá»’ ÃN LAB211** | Cáº£ nhÃ³m Group 1 | [ ] Sáº´N SÃ€NG |
