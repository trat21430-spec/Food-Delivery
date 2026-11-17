# TÀI LIỆU CHI TIẾT THUỘC TÍNH VÀ PHƯƠNG THỨC TẤT CẢ CÁC CLASS (UML SPECIFICATION)
> **Dự án:** Food Delivery Network Simulation (LAB211)  
> **Kiến trúc:** Model - View - Controller (MVC) + Generic CSV Repository  
> **Mục đích:** Đặc tả chính xác 100% thuộc tính (Attributes), phương thức (Methods), kiểu dữ liệu và mối quan hệ theo đúng mã nguồn dự án để phục vụ vẽ UML Class Diagram (trên draw.io, StarUML, Visual Paradigm,...).

---

## MỤC LỤC CÁC PACKAGE
1. [Package `model`](#1-package-model)
2. [Package `repository`](#2-package-repository)
3. [Package `controller`](#3-package-controller)
4. [Package `exception`](#4-package-exception)
5. [Package `view`](#5-package-view)
6. [Root / Entry Point (`Main`)](#6-root--entry-point)
7. [Bảng tóm tắt quan hệ giữa các Class (Relationships Summary)](#7-bang-tom-tat-quan-he-giua-cac-class)

---

## 1. PACKAGE `model`

### 1.1. `abstract class BaseEntity`
- **Kế thừa:** Lớp gốc cho toàn bộ thực thể trong hệ thống.
- **Thuộc tính (Attributes):**
  - `- id: String`
  - `- createdAt: LocalDateTime`
  - `- updatedAt: LocalDateTime`
  - `- version: long`
- **Hàm khởi tạo (Constructors):**
  - `+ BaseEntity()`
  - `+ BaseEntity(id: String)`
  - `+ BaseEntity(id: String, createdAt: LocalDateTime, updatedAt: LocalDateTime, version: long)`
- **Phương thức (Methods):**
  - `+ getId(): String`
  - `+ setId(id: String): void`
  - `+ getCreatedAt(): LocalDateTime`
  - `+ setCreatedAt(createdAt: LocalDateTime): void`
  - `+ getUpdatedAt(): LocalDateTime`
  - `+ setUpdatedAt(updatedAt: LocalDateTime): void`
  - `+ getVersion(): long`
  - `+ setVersion(version: long): void`
  - `+ incrementVersion(): void`
  - `+ toCsvLine(): String` *(abstract)*
  - `+ fromCsvLine(line: String): void` *(abstract)*

---

### 1.2. `class Customer extends BaseEntity`
- **Thuộc tính (Attributes):**
  - `- fullName: String`
  - `- phone: String`
  - `- ward: String`
  - `- latitude: double`
  - `- longitude: double`
- **Hàm khởi tạo (Constructors):**
  - `+ Customer()`
  - `+ Customer(id: String, fullName: String, phone: String, ward: String, latitude: double, longitude: double)`
  - `+ Customer(id: String, fullName: String, phone: String, ward: String, latitude: double, longitude: double, createdAt: LocalDateTime, updatedAt: LocalDateTime, version: long)`
- **Phương thức (Methods):**
  - `+ getFullName(): String`
  - `+ setFullName(fullName: String): void`
  - `+ getName(): String`
  - `+ setName(name: String): void`
  - `+ getPhone(): String`
  - `+ setPhone(phone: String): void`
  - `+ getWard(): String`
  - `+ setWard(ward: String): void`
  - `+ getAddress(): String`
  - `+ setAddress(address: String): void`
  - `+ getLatitude(): double`
  - `+ setLatitude(latitude: double): void`
  - `+ getLongitude(): double`
  - `+ setLongitude(longitude: double): void`
  - `+ toCsvLine(): String`
  - `+ fromCsvLine(line: String): void`

---

### 1.3. `class Restaurant extends BaseEntity`
- **Thuộc tính (Attributes):**
  - `- name: String`
  - `- category: String`
  - `- address: String`
  - `- latitude: double`
  - `- longitude: double`
  - `- rating: double`
  - `- open: boolean`
- **Hàm khởi tạo (Constructors):**
  - `+ Restaurant()`
  - `+ Restaurant(id: String, name: String, category: String, address: String, latitude: double, longitude: double, rating: double, open: boolean)`
  - `+ Restaurant(id: String, name: String, address: String, latitude: double, longitude: double, rating: double, createdAt: LocalDateTime, updatedAt: LocalDateTime, version: long)`
- **Phương thức (Methods):**
  - `+ getName(): String`
  - `+ setName(name: String): void`
  - `+ getCategory(): String`
  - `+ setCategory(category: String): void`
  - `+ getAddress(): String`
  - `+ setAddress(address: String): void`
  - `+ getLatitude(): double`
  - `+ setLatitude(latitude: double): void`
  - `+ getLongitude(): double`
  - `+ setLongitude(longitude: double): void`
  - `+ getRating(): double`
  - `+ setRating(rating: double): void`
  - `+ isOpen(): boolean`
  - `+ setOpen(open: boolean): void`
  - `+ toCsvLine(): String`
  - `+ fromCsvLine(line: String): void`

---

### 1.4. `class MenuItem extends BaseEntity`
- **Thuộc tính (Attributes):**
  - `- restaurantId: String`
  - `- name: String`
  - `- priceVnd: double`
  - `- stockQty: int`
- **Hàm khởi tạo (Constructors):**
  - `+ MenuItem()`
  - `+ MenuItem(id: String, restaurantId: String, name: String, priceVnd: double, stockQty: int, version: long)`
  - `+ MenuItem(id: String, restaurantId: String, name: String, priceVnd: double, stockQty: int, createdAt: LocalDateTime, updatedAt: LocalDateTime, version: long)`
- **Phương thức (Methods):**
  - `+ getRestaurantId(): String`
  - `+ setRestaurantId(restaurantId: String): void`
  - `+ getName(): String`
  - `+ setName(name: String): void`
  - `+ getPriceVnd(): double`
  - `+ setPriceVnd(priceVnd: double): void`
  - `+ getPrice(): double`
  - `+ setPrice(price: double): void`
  - `+ getStockQty(): int`
  - `+ setStockQty(stockQty: int): void`
  - `+ toCsvLine(): String`
  - `+ fromCsvLine(line: String): void`

---

### 1.5. `class Driver extends BaseEntity`
- **Thuộc tính (Attributes):**
  - `- fullName: String`
  - `- phone: String`
  - `- licensePlate: String`
  - `- latitude: double`
  - `- longitude: double`
  - `- status: DriverStatus`
  - `- activeOrderCount: int`
  - `- currentOrderId: String`
- **Hàm khởi tạo (Constructors):**
  - `+ Driver()`
  - `+ Driver(id: String, fullName: String, phone: String, licensePlate: String, latitude: double, longitude: double, status: DriverStatus, activeOrderCount: int, version: long)`
  - `+ Driver(id: String, name: String, phone: String, status: DriverStatus, latitude: double, longitude: double, currentOrderId: String, createdAt: LocalDateTime, updatedAt: LocalDateTime, version: long)`
- **Phương thức (Methods):**
  - `+ getFullName(): String`
  - `+ setFullName(fullName: String): void`
  - `+ getName(): String`
  - `+ setName(name: String): void`
  - `+ getPhone(): String`
  - `+ setPhone(phone: String): void`
  - `+ getLicensePlate(): String`
  - `+ setLicensePlate(licensePlate: String): void`
  - `+ getLatitude(): double`
  - `+ setLatitude(latitude: double): void`
  - `+ getLongitude(): double`
  - `+ setLongitude(longitude: double): void`
  - `+ getStatus(): DriverStatus`
  - `+ setStatus(status: DriverStatus): void`
  - `+ getActiveOrderCount(): int`
  - `+ setActiveOrderCount(activeOrderCount: int): void`
  - `+ getCurrentOrderId(): String`
  - `+ setCurrentOrderId(currentOrderId: String): void`
  - `+ toCsvLine(): String`
  - `+ fromCsvLine(line: String): void`

---

### 1.6. `class DriverApplication extends BaseEntity`
- **Thuộc tính (Attributes):**
  - `- username: String`
  - `- password: String`
  - `- fullName: String`
  - `- phone: String`
  - `- licensePlate: String`
  - `- vehicleType: String`
  - `- status: String`
  - `- note: String`
- **Hàm khởi tạo (Constructors):**
  - `+ DriverApplication()`
  - `+ DriverApplication(id: String, username: String, password: String, fullName: String, phone: String, licensePlate: String, vehicleType: String, status: String, note: String)`
- **Phương thức (Methods):**
  - `+ getUsername(): String`
  - `+ setUsername(username: String): void`
  - `+ getPassword(): String`
  - `+ setPassword(password: String): void`
  - `+ getFullName(): String`
  - `+ setFullName(fullName: String): void`
  - `+ getPhone(): String`
  - `+ setPhone(phone: String): void`
  - `+ getLicensePlate(): String`
  - `+ setLicensePlate(licensePlate: String): void`
  - `+ getVehicleType(): String`
  - `+ setVehicleType(vehicleType: String): void`
  - `+ getStatus(): String`
  - `+ setStatus(status: String): void`
  - `+ getNote(): String`
  - `+ setNote(note: String): void`
  - `+ toCsvLine(): String`
  - `+ fromCsvLine(line: String): void`

---

### 1.7. `class Order extends BaseEntity`
- **Thuộc tính (Attributes):**
  - `- customerId: String`
  - `- restaurantId: String`
  - `- driverId: String`
  - `- status: OrderStatus`
  - `- totalFoodAmount: double`
  - `- deliveryFee: double`
  - `- createdAtEpochMs: long`
  - `- assignCount: int`
- **Hàm khởi tạo (Constructors):**
  - `+ Order()`
  - `+ Order(id: String, customerId: String, restaurantId: String, driverId: String, status: OrderStatus, totalFoodAmount: double, deliveryFee: double, createdAtEpochMs: long, assignCount: int, version: long)`
  - `+ Order(id: String, customerId: String, restaurantId: String, driverId: String, status: OrderStatus, totalFoodAmount: double, deliveryFee: double, createdAt: LocalDateTime, updatedAt: LocalDateTime, version: long)`
- **Phương thức (Methods):**
  - `+ getCustomerId(): String`
  - `+ setCustomerId(customerId: String): void`
  - `+ getRestaurantId(): String`
  - `+ setRestaurantId(restaurantId: String): void`
  - `+ getDriverId(): String`
  - `+ setDriverId(driverId: String): void`
  - `+ getStatus(): OrderStatus`
  - `+ setStatus(status: OrderStatus): void`
  - `+ getTotalFoodAmount(): double`
  - `+ setTotalFoodAmount(totalFoodAmount: double): void`
  - `+ getDeliveryFee(): double`
  - `+ setDeliveryFee(deliveryFee: double): void`
  - `+ getTotalAmount(): double`
  - `+ setTotalAmount(totalAmount: double): void`
  - `+ getCreatedAtEpochMs(): long`
  - `+ setCreatedAtEpochMs(createdAtEpochMs: long): void`
  - `+ getAssignCount(): int`
  - `+ setAssignCount(assignCount: int): void`
  - `+ toCsvLine(): String`
  - `+ fromCsvLine(line: String): void`

---

### 1.8. `class OrderItem extends BaseEntity`
- **Thuộc tính (Attributes):**
  - `- orderId: String`
  - `- menuItemId: String`
  - `- quantity: int`
  - `- unitPriceVnd: double`
- **Hàm khởi tạo (Constructors):**
  - `+ OrderItem()`
  - `+ OrderItem(id: String, orderId: String, menuItemId: String, quantity: int, unitPriceVnd: double)`
  - `+ OrderItem(id: String, orderId: String, menuItemId: String, quantity: int, unitPriceVnd: double, createdAt: LocalDateTime, updatedAt: LocalDateTime, version: long)`
- **Phương thức (Methods):**
  - `+ getOrderId(): String`
  - `+ setOrderId(orderId: String): void`
  - `+ getMenuItemId(): String`
  - `+ setMenuItemId(menuItemId: String): void`
  - `+ getQuantity(): int`
  - `+ setQuantity(quantity: int): void`
  - `+ getUnitPriceVnd(): double`
  - `+ setUnitPriceVnd(unitPriceVnd: double): void`
  - `+ getPrice(): double`
  - `+ setPrice(price: double): void`
  - `+ toCsvLine(): String`
  - `+ fromCsvLine(line: String): void`

---

### 1.9. `class DeliveryRoute extends BaseEntity`
- **Thuộc tính (Attributes):**
  - `- orderId: String`
  - `- driverId: String`
  - `- pickupDistanceKm: double`
  - `- deliveryDistanceKm: double`
  - `- estimatedMinutes: int`
- **Hàm khởi tạo (Constructors):**
  - `+ DeliveryRoute()`
  - `+ DeliveryRoute(id: String, orderId: String, driverId: String, pickupDistanceKm: double, deliveryDistanceKm: double, estimatedMinutes: int)`
  - `+ DeliveryRoute(id: String, orderId: String, driverId: String, distanceKm: double, estimatedMinutes: int, routePathJson: String, createdAt: LocalDateTime, updatedAt: LocalDateTime, version: long)`
- **Phương thức (Methods):**
  - `+ getOrderId(): String`
  - `+ setOrderId(orderId: String): void`
  - `+ getDriverId(): String`
  - `+ setDriverId(driverId: String): void`
  - `+ getPickupDistanceKm(): double`
  - `+ setPickupDistanceKm(pickupDistanceKm: double): void`
  - `+ getDeliveryDistanceKm(): double`
  - `+ setDeliveryDistanceKm(deliveryDistanceKm: double): void`
  - `+ getDistanceKm(): double`
  - `+ setDistanceKm(distanceKm: double): void`
  - `+ getEstimatedMinutes(): int`
  - `+ setEstimatedMinutes(estimatedMinutes: int): void`
  - `+ toCsvLine(): String`
  - `+ fromCsvLine(line: String): void`

---

### 1.10. `class SimulationRun extends BaseEntity`
- **Thuộc tính (Attributes):**
  - `- mechanism: LockMechanism`
  - `- totalOrders: int`
  - `- durationMs: long`
  - `- throughputOpsPerSec: double`
  - `- throughputDropPercent: double`
  - `- doubleAssignErrors: int`
  - `- oversellErrors: int`
  - `- driverOverloadErrors: int`
- **Hàm khởi tạo (Constructors):**
  - `+ SimulationRun()`
  - `+ SimulationRun(id: String, mechanism: LockMechanism, totalOrders: int, durationMs: long, throughputOpsPerSec: double, throughputDropPercent: double, doubleAssignErrors: int, oversellErrors: int, driverOverloadErrors: int)`
  - `+ SimulationRun(id: String, mechanism: LockMechanism, totalOrders: int, durationMs: long, throughputOpsPerSec: double, doubleAssignments: int, oversells: int, driverOverloads: int, totalErrors: int, createdAt: LocalDateTime, updatedAt: LocalDateTime, version: long)`
- **Phương thức (Methods):**
  - `+ getMechanism(): LockMechanism`
  - `+ setMechanism(mechanism: LockMechanism): void`
  - `+ getTotalOrders(): int`
  - `+ setTotalOrders(totalOrders: int): void`
  - `+ getDurationMs(): long`
  - `+ setDurationMs(durationMs: long): void`
  - `+ getThroughputOpsPerSec(): double`
  - `+ setThroughputOpsPerSec(throughputOpsPerSec: double): void`
  - `+ getThroughputDropPercent(): double`
  - `+ setThroughputDropPercent(throughputDropPercent: double): void`
  - `+ getDoubleAssignErrors(): int`
  - `+ setDoubleAssignErrors(doubleAssignErrors: int): void`
  - `+ getDoubleAssignments(): int`
  - `+ setDoubleAssignments(doubleAssignments: int): void`
  - `+ getOversellErrors(): int`
  - `+ setOversellErrors(oversellErrors: int): void`
  - `+ getOversells(): int`
  - `+ setOversells(oversells: int): void`
  - `+ getDriverOverloadErrors(): int`
  - `+ setDriverOverloadErrors(driverOverloadErrors: int): void`
  - `+ getDriverOverloads(): int`
  - `+ setDriverOverloads(driverOverloads: int): void`
  - `+ getTotalErrors(): int`
  - `+ setTotalErrors(totalErrors: int): void`
  - `+ toCsvLine(): String`
  - `+ fromCsvLine(line: String): void`

---

### 1.11. `class User extends BaseEntity`
- **Thuộc tính (Attributes):**
  - `- username: String`
  - `- password: String`
  - `- role: UserRole`
  - `- linkedEntityId: String`
  - `- displayName: String`
  - `- phone: String`
- **Hàm khởi tạo (Constructors):**
  - `+ User()`
  - `+ User(id: String, username: String, password: String, role: UserRole, linkedEntityId: String, displayName: String, createdAt: LocalDateTime, updatedAt: LocalDateTime, version: long)`
  - `+ User(id: String, username: String, password: String, phone: String, fullName: String, role: UserRole, linkedCustomerId: String, linkedDriverId: String, active: boolean)`
- **Phương thức (Methods):**
  - `+ getUsername(): String`
  - `+ setUsername(username: String): void`
  - `+ getPassword(): String`
  - `+ setPassword(password: String): void`
  - `+ getRole(): UserRole`
  - `+ setRole(role: UserRole): void`
  - `+ getLinkedEntityId(): String`
  - `+ setLinkedEntityId(linkedEntityId: String): void`
  - `+ getDisplayName(): String`
  - `+ setDisplayName(displayName: String): void`
  - `+ getFullName(): String`
  - `+ setFullName(fullName: String): void`
  - `+ getPhone(): String`
  - `+ setPhone(phone: String): void`
  - `+ getLinkedCustomerId(): String`
  - `+ setLinkedCustomerId(cid: String): void`
  - `+ getLinkedDriverId(): String`
  - `+ setLinkedDriverId(did: String): void`
  - `+ toCsvLine(): String`
  - `+ fromCsvLine(line: String): void`

---

### 1.12. `class UserAccount extends User`
- **Kế thừa:** `extends User`
- **Hàm khởi tạo (Constructors):**
  - `+ UserAccount()`
  - `+ UserAccount(id: String, username: String, password: String, phone: String, fullName: String, role: UserRole, linkedCustomerId: String, linkedDriverId: String, active: boolean)`

---

### 1.13. `class GeoUtils`
- **Thuộc tính (Attributes):**
  - `- earthRadiusKm: double`
- **Hàm khởi tạo (Constructors):**
  - `+ GeoUtils()`
  - `+ GeoUtils(earthRadiusKm: double)`
- **Phương thức (Methods):**
  - `+ getEarthRadiusKm(): double`
  - `+ setEarthRadiusKm(earthRadiusKm: double): void`
  - `+ haversineDistance(lat1: double, lon1: double, lat2: double, lon2: double): double`
  - `+ haversine(lat1: double, lon1: double, lat2: double, lon2: double): double`
  - `+ calculateDeliveryFee(distanceKm: double): double`
  - `+ getFee(distanceKm: double): double`
  - `+ calculatePickupMinutes(driverToRestKm: double): int`
  - `+ calculateDeliveryMinutes(driverToRestKm: double, restToCustKm: double): int`
  - `+ formatEstimatedClockTime(baseEpochMs: long, durationMinutes: int): String`
  - `+ formatEstimatedClockTime(baseTime: LocalDateTime, durationMinutes: int): String`

---

### 1.14. `class AuthSession`
- **Thuộc tính (Attributes):**
  - `- role: AccountRole`
  - `- userId: String`
  - `- displayName: String`
- **Hàm khởi tạo (Constructors):**
  - `+ AuthSession(role: AccountRole, userId: String, displayName: String)`
- **Phương thức (Methods):**
  - `+ getRole(): AccountRole`
  - `+ getUserId(): String`
  - `+ getDisplayName(): String`

### 1.15. Các Enumeration trong `package model`
- `enum AccountRole`: `CUSTOMER`, `DRIVER`
- `enum DriverStatus`: `AVAILABLE`, `BUSY`, `OFFLINE`
- `enum OrderStatus`: `PENDING`, `CONFIRMED`, `DISPATCHED`, `DELIVERED`, `CANCELLED`
- `enum UserRole`: `CUSTOMER`, `DRIVER`, `RESTAURANT`, `RESTAURANT_ADMIN`, `SYSTEM_ADMIN`, `USER_ADMIN`, `DISPATCHER`, `ADMIN`
- `enum LockMechanism`: `NO_LOCK`, `SYNCHRONIZED`, `OPTIMISTIC`, `FILE_LOCK`

---

## 2. PACKAGE `repository`

### 2.1. `interface IRepository<T extends BaseEntity>`
- **Phương thức (Methods):**
  - `+ findAll(): List<T>`
  - `+ findById(id: String): T`
  - `+ save(entity: T): void`
  - `+ saveAll(entities: List<T>): void`
  - `+ deleteById(id: String): void`

---

### 2.2. `abstract class CsvRepository<T extends BaseEntity> implements IRepository<T>`
- **Thuộc tính (Attributes):**
  - `- filePath: String`
  - `- header: String`
  - `- fileMonitor: Object`
  - `- keyLocks: ConcurrentHashMap<String, Object>`
- **Hàm khởi tạo (Constructors):**
  - `+ CsvRepository(filePath: String, header: String)`
- **Phương thức (Methods):**
  - `+ getFilePath(): String`
  - `+ setFilePath(filePath: String): void`
  - `+ getHeader(): String`
  - `+ setHeader(header: String): void`
  - `+ createEntity(): T` *(abstract)*
  - `+ parseLine(line: String): T`
  - `- ensureFileExists(): void`
  - `+ findAll(): List<T>` *(synchronized)*
  - `+ findById(id: String): T` *(synchronized)*
  - `+ findByIdOptional(id: String): Optional<T>`
  - `+ save(entity: T): void` *(synchronized)*
  - `+ saveAll(entities: List<T>): void` *(synchronized)*
  - `+ deleteById(id: String): void` *(synchronized)*
  - `+ getKeyLock(key: String): Object`
  - `+ executeWithFileLock(action: Runnable): void`
  - `+ executeWithFileLock(action: Supplier<R>): R`
  - `+ updateWithOptimisticLock(entity: T, expectedVersion: long): void` *(throws OptimisticLockException)*

---

### 2.3. `class CustomerRepository extends CsvRepository<Customer>`
- **Hàm khởi tạo (Constructors):**
  - `+ CustomerRepository()`
  - `+ CustomerRepository(filePath: String)`
- **Phương thức (Methods):**
  - `+ createEntity(): Customer`

---

### 2.4. `class RestaurantRepository extends CsvRepository<Restaurant>`
- **Hàm khởi tạo (Constructors):**
  - `+ RestaurantRepository()`
  - `+ RestaurantRepository(filePath: String)`
- **Phương thức (Methods):**
  - `+ createEntity(): Restaurant`

---

### 2.5. `class MenuItemRepository extends CsvRepository<MenuItem>`
- **Hàm khởi tạo (Constructors):**
  - `+ MenuItemRepository()`
  - `+ MenuItemRepository(customFilePath: String)`
- **Phương thức (Methods):**
  - `+ createEntity(): MenuItem`
  - `+ findById(id: String): MenuItem`
  - `+ findByRestaurantId(restaurantId: String): List<MenuItem>`
  - `+ deductStock(menuItemId: String, quantity: int, mechanism: LockMechanism): boolean` *(throws Exception)*
  - `- deductStockNoLock(menuItemId: String, quantity: int): boolean` *(throws OversellException)*
  - `- deductStockSynchronized(menuItemId: String, quantity: int): boolean` *(throws OversellException)*
  - `- deductStockOptimistic(menuItemId: String, quantity: int): boolean` *(throws Exception)*
  - `- deductStockFileLock(menuItemId: String, quantity: int): boolean` *(throws OversellException)*
  - `+ restoreStock(menuItemId: String, quantity: int): boolean`

---

### 2.6. `class DriverRepository extends CsvRepository<Driver>`
- **Thuộc tính (Attributes):**
  - `- geoUtils: GeoUtils`
- **Hàm khởi tạo (Constructors):**
  - `+ DriverRepository()`
  - `+ DriverRepository(customFilePath: String)`
- **Phương thức (Methods):**
  - `+ createEntity(): Driver`
  - `+ getGeoUtils(): GeoUtils`
  - `+ setGeoUtils(geoUtils: GeoUtils): void`
  - `+ findByPhone(phone: String): Driver`
  - `+ findAvailableDrivers(): List<Driver>`
  - `+ findClosestOnlineDriver(restaurantLat: double, restaurantLon: double): Driver`
  - `+ findNearestAvailable(restaurantLat: double, restaurantLon: double): Driver`
  - `+ markBusy(driverId: String, mechanism: LockMechanism): boolean` *(throws Exception)*
  - `+ markBusy(driverId: String, orderId: String, mechanism: LockMechanism): boolean` *(throws Exception)*
  - `+ markAvailable(driverId: String): boolean`
  - `- markBusyNoLock(driverId: String, orderId: String): boolean` *(throws DriverOverloadException)*
  - `- markBusySynchronized(driverId: String, orderId: String): boolean` *(throws DriverOverloadException)*
  - `- markBusyOptimistic(driverId: String, orderId: String): boolean` *(throws Exception)*
  - `- markBusyFileLock(driverId: String, orderId: String): boolean` *(throws DriverOverloadException)*

---

### 2.7. `class DriverApplicationRepository extends CsvRepository<DriverApplication>`
- **Hàm khởi tạo (Constructors):**
  - `+ DriverApplicationRepository()`
  - `+ DriverApplicationRepository(filePath: String)`
- **Phương thức (Methods):**
  - `+ createEntity(): DriverApplication`
  - `+ findPendingApplications(): List<DriverApplication>`
  - `+ findByUsername(username: String): DriverApplication`
  - `+ findByPhone(phone: String): DriverApplication`

---

### 2.8. `class OrderRepository extends CsvRepository<Order>`
- **Hàm khởi tạo (Constructors):**
  - `+ OrderRepository()`
  - `+ OrderRepository(customFilePath: String)`
- **Phương thức (Methods):**
  - `+ createEntity(): Order`
  - `+ processOrderAndDispatch(customerId: String, restaurantId: String, menuItemId: String, quantity: int, mechanism: LockMechanism, itemRepo: MenuItemRepository, driverRepo: DriverRepository, restRepo: RestaurantRepository): Order` *(throws Exception)*
  - `+ dispatchPendingOrder(orderId: String, mechanism: LockMechanism, driverRepo: DriverRepository, restRepo: RestaurantRepository): boolean` *(throws Exception)*
  - `+ autoDispatchAllPending(mechanism: LockMechanism, driverRepo: DriverRepository, restRepo: RestaurantRepository): List<String>`
  - `+ dispatchOrderToDriver(orderId: String, driverId: String, mechanism: LockMechanism): boolean` *(throws Exception)*
  - `+ assignDriver(orderId: String, driverId: String, mechanism: LockMechanism): boolean` *(throws Exception)*
  - `- assignDriverNoLock(orderId: String, driverId: String): boolean` *(throws DoubleAssignmentException)*
  - `- assignDriverSynchronized(orderId: String, driverId: String): boolean` *(throws DoubleAssignmentException)*
  - `- assignDriverOptimistic(orderId: String, driverId: String): boolean` *(throws Exception)*
  - `- assignDriverFileLock(orderId: String, driverId: String): boolean` *(throws DoubleAssignmentException)*

---

### 2.9. `class OrderItemRepository extends CsvRepository<OrderItem>`
- **Hàm khởi tạo (Constructors):**
  - `+ OrderItemRepository()`
  - `+ OrderItemRepository(filePath: String)`
- **Phương thức (Methods):**
  - `+ createEntity(): OrderItem`
  - `+ findByOrderId(orderId: String): List<OrderItem>`

---

### 2.10. `class DeliveryRouteRepository extends CsvRepository<DeliveryRoute>`
- **Hàm khởi tạo (Constructors):**
  - `+ DeliveryRouteRepository()`
  - `+ DeliveryRouteRepository(filePath: String)`
- **Phương thức (Methods):**
  - `+ createEntity(): DeliveryRoute`

---

### 2.11. `class SimulationRunRepository extends CsvRepository<SimulationRun>`
- **Hàm khởi tạo (Constructors):**
  - `+ SimulationRunRepository()`
  - `+ SimulationRunRepository(filePath: String)`
- **Phương thức (Methods):**
  - `+ createEntity(): SimulationRun`

---

### 2.12. `class UserRepository extends CsvRepository<User>`
- **Hàm khởi tạo (Constructors):**
  - `+ UserRepository()`
  - `+ UserRepository(filePath: String)`
- **Phương thức (Methods):**
  - `+ createEntity(): User`
  - `+ findByUsername(username: String): User`
  - `+ findByPhone(phone: String): User`

---

### 2.13. `class AccountRepository`
- **Thuộc tính (Attributes):**
  - `- header: String`
  - `- iterations: int`
  - `- keyLength: int`
  - `- saltLength: int`
  - `- filePath: Path`
  - `- secureRandom: SecureRandom`
- **Hàm khởi tạo (Constructors):**
  - `+ AccountRepository()`
  - `+ AccountRepository(filePath: Path)`
- **Phương thức (Methods):**
  - `+ register(role: AccountRole, userId: String, password: char[]): void` *(synchronized)*
  - `+ authenticate(role: AccountRole, userId: String, password: char[]): boolean` *(synchronized)*
  - `+ hasAccount(role: AccountRole, userId: String): boolean` *(synchronized)*
  - `- findAccount(role: AccountRole, userId: String): String[]`
  - `- hashPassword(password: char[], salt: byte[]): byte[]`
  - `- ensureFileExists(): void`

---

## 3. PACKAGE `controller`

### 3.1. `class MainController`
- **Thuộc tính (Attributes):**
  - `- authController: AuthController`
  - `- customerController: CustomerController`
  - `- restaurantController: RestaurantController`
  - `- driverController: DriverController`
  - `- orderController: OrderController`
  - `- simulatorController: SimulatorController`
  - `- adminController: AdminController`
  - `- mainView: MainView`
  - `- mapView: MapView`
  - `- orderView: OrderView`
  - `- reportView: ReportView`
  - `- simulatorView: SimulatorView`
- **Hàm khởi tạo (Constructors):**
  - `+ MainController()`
  - `+ MainController(authController: AuthController, customerController: CustomerController, restaurantController: RestaurantController, driverController: DriverController, orderController: OrderController, simulatorController: SimulatorController, adminController: AdminController)`
- **Phương thức (Methods):**
  - `+ run(): void`
  - `+ start(): void`
  - `+ getAuthController(): AuthController`
  - `+ getCustomerController(): CustomerController`
  - `+ getRestaurantController(): RestaurantController`
  - `+ getDriverController(): DriverController`
  - `+ getOrderController(): OrderController`
  - `+ getSimulatorController(): SimulatorController`
  - `+ getAdminController(): AdminController`
  - `+ getMainView(): MainView`
  - `+ getMapView(): MapView`
  - `+ getOrderView(): OrderView`
  - `+ getReportView(): ReportView`
  - `+ getSimulatorView(): SimulatorView`
  - `+ login(username: String, password: String): boolean`
  - `+ logout(): void`
  - `+ getCurrentUser(): User`
  - `+ isLoggedIn(): boolean`
  - `+ runBenchmark(mechanism: LockMechanism, numOrders: int): SimulationRun`
  - `+ autoDispatchAllPending(mechanism: LockMechanism): List<String>`

---

### 3.2. `class AppController extends MainController`
- **Hàm khởi tạo (Constructors):**
  - `+ AppController()`

---

### 3.3. `class AuthController`
- **Thuộc tính (Attributes):**
  - `- userRepository: UserRepository`
  - `- customerRepository: CustomerRepository`
  - `- driverRepository: DriverRepository`
  - `- driverAppRepository: DriverApplicationRepository`
  - `- currentUser: User`
- **Hàm khởi tạo (Constructors):**
  - `+ AuthController()`
  - `+ AuthController(userRepo: UserRepository, customerRepo: CustomerRepository, driverRepo: DriverRepository, driverAppRepo: DriverApplicationRepository)`
- **Phương thức (Methods):**
  - `+ getUserRepository(): UserRepository`
  - `+ getCurrentUser(): User`
  - `+ getCurrentSession(): User`
  - `+ isLoggedIn(): boolean`
  - `+ logout(): void`
  - `+ login(username: String, password: String): boolean`
  - `+ registerCustomer(username: String, password: String, name: String, phone: String, address: String, lat: double, lon: double): User` *(throws InvalidInputException)*
  - `+ submitDriverApplication(username: String, password: String, fullName: String, phone: String, licensePlate: String, vehicleType: String, note: String): DriverApplication` *(throws InvalidInputException)*
  - `+ registerDriver(username: String, password: String, name: String, phone: String, lat: double, lon: double): User` *(throws InvalidInputException)*
  - `+ getAllUsers(): List<User>`
  - `+ getUsersByRole(role: UserRole): List<User>`
  - `+ createUser(username: String, password: String, role: UserRole, linkedEntityId: String, displayName: String): User`
  - `+ getUserById(id: String): User`
  - `+ updateUser(id: String, newPassword: String, newRole: UserRole, newDisplayName: String): boolean`
  - `+ deleteUser(id: String): boolean`

---

### 3.4. `class CustomerController`
- **Thuộc tính (Attributes):**
  - `- customerRepository: CustomerRepository`
  - `- orderRepository: OrderRepository`
- **Hàm khởi tạo (Constructors):**
  - `+ CustomerController()`
  - `+ CustomerController(customerRepository: CustomerRepository)`
  - `+ CustomerController(customerRepository: CustomerRepository, orderRepository: OrderRepository)`
- **Phương thức (Methods):**
  - `+ registerCustomer(name: String, phone: String, address: String, lat: double, lon: double): Customer`
  - `+ getAllCustomers(): List<Customer>`
  - `+ getCustomerById(id: String): Customer`
  - `+ getCustomerByPhone(phone: String): Customer`
  - `+ updateCustomer(id: String, name: String, phone: String, address: String, lat: double, lon: double): boolean`
  - `+ deleteCustomer(id: String): boolean`
  - `+ getOrderHistory(customerId: String): List<Order>`

---

### 3.5. `class DriverController`
- **Thuộc tính (Attributes):**
  - `- driverRepository: DriverRepository`
- **Hàm khởi tạo (Constructors):**
  - `+ DriverController()`
  - `+ DriverController(driverRepository: DriverRepository)`
- **Phương thức (Methods):**
  - `+ createDriver(name: String, phone: String, lat: double, lon: double): Driver`
  - `+ getAllDrivers(): List<Driver>`
  - `+ getDriverById(id: String): Driver`
  - `+ getAvailableDrivers(): List<Driver>`
  - `+ getNearestAvailableDriver(lat: double, lon: double): Driver`
  - `+ updateDriverStatus(id: String, status: DriverStatus): boolean`
  - `+ updateDriverInfo(id: String, name: String, phone: String, lat: double, lon: double): boolean`
  - `+ deleteDriver(id: String): boolean`

---

### 3.6. `class RestaurantController`
- **Thuộc tính (Attributes):**
  - `- restaurantRepository: RestaurantRepository`
  - `- menuItemRepository: MenuItemRepository`
- **Hàm khởi tạo (Constructors):**
  - `+ RestaurantController()`
  - `+ RestaurantController(restaurantRepository: RestaurantRepository, menuItemRepository: MenuItemRepository)`
- **Phương thức (Methods):**
  - `+ createRestaurant(name: String, address: String, lat: double, lon: double, rating: double): Restaurant`
  - `+ getAllRestaurants(): List<Restaurant>`
  - `+ getRestaurantById(id: String): Restaurant`
  - `+ updateRestaurant(id: String, newName: String, newAddress: double, newRating: double): boolean`
  - `+ deleteRestaurant(id: String): boolean`
  - `+ createMenuItem(restaurantId: String, name: String, price: double, stockQty: int): MenuItem`
  - `+ getMenuByRestaurant(restaurantId: String): List<MenuItem>`
  - `+ getMenuItemById(id: String): MenuItem`
  - `+ updateMenuItem(id: String, newName: String, newPrice: double, newStock: int): boolean`
  - `+ updateStock(menuItemId: String, newStock: int): boolean`
  - `+ deleteMenuItem(id: String): boolean`

---

### 3.7. `class OrderController`
- **Thuộc tính (Attributes):**
  - `- orderRepository: OrderRepository`
  - `- menuItemRepository: MenuItemRepository`
  - `- driverRepository: DriverRepository`
  - `- restaurantRepository: RestaurantRepository`
  - `- orderItemRepository: OrderItemRepository`
  - `- customerRepository: CustomerRepository`
  - `- routeRepository: DeliveryRouteRepository`
  - `- autoDispatchEnabled: boolean`
- **Hàm khởi tạo (Constructors):**
  - `+ OrderController()`
  - `+ OrderController(orderRepo: OrderRepository, itemRepo: MenuItemRepository, driverRepo: DriverRepository, restRepo: RestaurantRepository)`
  - `+ OrderController(orderRepo: OrderRepository, orderItemRepo: OrderItemRepository, itemRepo: MenuItemRepository, driverRepo: DriverRepository, restRepo: RestaurantRepository, custRepo: CustomerRepository, routeRepo: DeliveryRouteRepository)`
- **Phương thức (Methods):**
  - `+ isAutoDispatchEnabled(): boolean`
  - `+ setAutoDispatchEnabled(autoDispatchEnabled: boolean): void`
  - `+ placeOrderAndDispatch(customerId: String, restaurantId: String, menuItemId: String, quantity: int, mechanism: LockMechanism): Order` *(throws Exception)*
  - `+ createAndAutoDispatchOrder(customerId: String, restaurantId: String, menuItemId: String, quantity: int, mechanism: LockMechanism): Order` *(throws Exception)*
  - `+ createCustomerOrder(customerId: String, restaurantId: String, menuItemId: String, quantity: int, mechanism: LockMechanism): Order` *(throws Exception)*
  - `+ placeCustomerOrderAndAutoDispatch(customerId: String, restaurantId: String, menuItemId: String, quantity: int, mechanism: LockMechanism): Order` *(throws Exception)*
  - `+ confirmOrderByRestaurant(orderId: String, restaurantId: String, mechanism: LockMechanism): Order` *(throws Exception)*
  - `+ rejectOrderByRestaurant(orderId: String, restaurantId: String, reason: String): boolean` *(throws Exception)*
  - `+ cancelOrderByCustomer(orderId: String, customerId: String): boolean` *(throws Exception)*
  - `+ markOrderPickedUp(orderId: String, driverId: String): boolean` *(throws Exception)*
  - `+ markOrderDelivered(orderId: String, driverId: String, mechanism: LockMechanism): boolean` *(throws Exception)*
  - `+ hasActiveDelivery(driverId: String): boolean`
  - `+ calculateRestaurantRevenue(restaurantId: String): double`
  - `+ getAllOrders(): List<Order>`
  - `+ getOrderById(orderId: String): Order`
  - `+ dispatchPending(orderId: String, mechanism: LockMechanism): boolean` *(throws Exception)*
  - `+ completeDelivery(orderId: String, mechanism: LockMechanism): boolean` *(throws Exception)*
  - `+ cancelPendingOrder(orderId: String): boolean` *(throws FoodDeliveryException)*
  - `+ cancelOrder(orderId: String): boolean` *(throws FoodDeliveryException)*
  - `+ autoDispatchAllPending(mechanism: LockMechanism): List<String>`
  - `+ autoDispatchPendingOrders(mechanism: LockMechanism): int`
  - `+ getPendingOrders(): List<Order>`
  - `+ getOrdersByCustomer(customerId: String): List<Order>`
  - `+ getOrdersByDriver(driverId: String): List<Order>`
  - `+ getOrdersByRestaurant(restaurantId: String): List<Order>`
  - `+ deleteOrder(orderId: String): boolean`

---

### 3.8. `class AdminController`
- **Thuộc tính (Attributes):**
  - `- restaurantRepo: RestaurantRepository`
  - `- menuItemRepo: MenuItemRepository`
  - `- customerRepo: CustomerRepository`
  - `- driverRepo: DriverRepository`
  - `- driverAppRepo: DriverApplicationRepository`
  - `- userRepo: UserRepository`
  - `- orderController: OrderController`
- **Hàm khởi tạo (Constructors):**
  - `+ AdminController()`
  - `+ AdminController(restaurantRepo: RestaurantRepository, menuItemRepo: MenuItemRepository, customerRepo: CustomerRepository, driverRepo: DriverRepository, driverAppRepo: DriverApplicationRepository, userRepo: UserRepository, orderController: OrderController)`
- **Phương thức (Methods):**
  - `+ getPendingDriverApplications(): List<DriverApplication>`
  - `+ getAllDriverApplications(): List<DriverApplication>`
  - `+ approveDriverApplication(applicationId: String): User`
  - `+ rejectDriverApplication(applicationId: String, reason: String): boolean`
  - `+ addRestaurant(name: String, category: String, address: String, lat: double, lon: double): Restaurant`
  - `+ restockMenuItem(menuItemId: String, additionalQty: int): void`
  - `+ triggerAutoDispatchAllPending(mechanism: LockMechanism): int`

---

### 3.9. `class SimulatorController`
- **Thuộc tính (Attributes):**
  - `- orderRepo: OrderRepository`
  - `- driverRepo: DriverRepository`
  - `- menuItemRepo: MenuItemRepository`
  - `- simRunRepo: SimulationRunRepository`
- **Hàm khởi tạo (Constructors):**
  - `+ SimulatorController()`
  - `+ SimulatorController(orderRepo: OrderRepository, driverRepo: DriverRepository, menuItemRepo: MenuItemRepository, simRunRepo: SimulationRunRepository)`
- **Phương thức (Methods):**
  - `+ runSimulation(numOrders: int, mechanism: LockMechanism): SimulationRun`
  - `+ runExperiment(mechanism: LockMechanism, numOrders: int, baselineThroughput: double): SimulationRun`
  - `+ detectDoubleAssignments(): int`
  - `+ detectOversellItems(): int`
  - `+ detectDriverOverload(): int`

#### Inner Tasks (trong `SimulatorController.java`):
- `class CustomerSimTask implements Runnable`:
  - `- startGate: CountDownLatch`, `- doneGate: CountDownLatch`, `- menuItemRepo: MenuItemRepository`, `- targetItemId: String`, `- mechanism: LockMechanism`, `- simulatedOversell: AtomicInteger`
  - `+ run(): void`
- `class DispatchSimTask implements Runnable`:
  - `- startGate: CountDownLatch`, `- doneGate: CountDownLatch`, `- driverRepo: DriverRepository`, `- orderRepo: OrderRepository`, `- targetOrderId: String`, `- targetDriverId: String`, `- mechanism: LockMechanism`, `- simulatedDoubleAssign: AtomicInteger`, `- simulatedDriverOverload: AtomicInteger`
  - `+ run(): void`

---

### 3.10. `class CustomerThread extends Thread`
- **Thuộc tính (Attributes):**
  - `- startLatch: CountDownLatch`
  - `- finishLatch: CountDownLatch`
  - `- orderController: OrderController`
  - `- customer: Customer`
  - `- restaurant: Restaurant`
  - `- menuItem: MenuItem`
  - `- quantity: int`
  - `- mechanism: LockMechanism`
  - `- oversellErrors: AtomicInteger`
  - `- otherErrors: AtomicInteger`
  - `- createdOrderIds: List<String>`
- **Hàm khởi tạo (Constructors):**
  - `+ CustomerThread(startLatch: CountDownLatch, finishLatch: CountDownLatch, orderController: OrderController, customer: Customer, restaurant: Restaurant, menuItem: MenuItem, quantity: int, mechanism: LockMechanism, oversellErrors: AtomicInteger, otherErrors: AtomicInteger, createdOrderIds: List<String>)`
- **Phương thức (Methods):**
  - `+ run(): void`

---

### 3.11. `class DispatchThread extends Thread`
- **Thuộc tính (Attributes):**
  - `- startLatch: CountDownLatch`
  - `- finishLatch: CountDownLatch`
  - `- orderController: OrderController`
  - `- orderId: String`
  - `- mechanism: LockMechanism`
  - `- doubleAssignmentErrors: AtomicInteger`
  - `- driverOverloadErrors: AtomicInteger`
  - `- otherErrors: AtomicInteger`
- **Hàm khởi tạo (Constructors):**
  - `+ DispatchThread(startLatch: CountDownLatch, finishLatch: CountDownLatch, orderController: OrderController, orderId: String, mechanism: LockMechanism, doubleAssignmentErrors: AtomicInteger, driverOverloadErrors: AtomicInteger, otherErrors: AtomicInteger)`
- **Phương thức (Methods):**
  - `+ run(): void`

---

## 4. PACKAGE `exception`

### 4.1. `class FoodDeliveryException extends Exception`
- **Hàm khởi tạo (Constructors):**
  - `+ FoodDeliveryException(message: String)`
  - `+ FoodDeliveryException(message: String, cause: Throwable)`

### 4.2. `class ConcurrencyException extends FoodDeliveryException`
- **Hàm khởi tạo (Constructors):**
  - `+ ConcurrencyException(message: String)`
  - `+ ConcurrencyException(message: String, cause: Throwable)`

### 4.3. `class DoubleAssignmentException extends ConcurrencyException`
- **Thuộc tính (Attributes):**
  - `- orderId: String`
  - `- existingDriverId: String`
  - `- attemptedDriverId: String`
- **Hàm khởi tạo (Constructors):**
  - `+ DoubleAssignmentException(orderId: String, existingDriverId: String, attemptedDriverId: String)`
- **Phương thức (Methods):**
  - `+ getOrderId(): String`
  - `+ getExistingDriverId(): String`
  - `+ getAttemptedDriverId(): String`

### 4.4. `class DriverOverloadException extends ConcurrencyException`
- **Thuộc tính (Attributes):**
  - `- driverId: String`
  - `- currentOrderId: String`
- **Hàm khởi tạo (Constructors):**
  - `+ DriverOverloadException(driverId: String, currentOrderId: String)`
- **Phương thức (Methods):**
  - `+ getDriverId(): String`
  - `+ getCurrentOrderId(): String`

### 4.5. `class OptimisticLockException extends ConcurrencyException`
- **Thuộc tính (Attributes):**
  - `- entityId: String`
  - `- expectedVersion: long`
  - `- currentVersion: long`
- **Hàm khởi tạo (Constructors):**
  - `+ OptimisticLockException(entityId: String, expectedVersion: long, currentVersion: long)`
- **Phương thức (Methods):**
  - `+ getEntityId(): String`
  - `+ getExpectedVersion(): long`
  - `+ getCurrentVersion(): long`

### 4.6. `class OversellException extends ConcurrencyException`
- **Thuộc tính (Attributes):**
  - `- menuItemId: String`
  - `- requestedQty: int`
  - `- availableQty: int`
- **Hàm khởi tạo (Constructors):**
  - `+ OversellException(menuItemId: String, requestedQty: int, availableQty: int)`
- **Phương thức (Methods):**
  - `+ getMenuItemId(): String`
  - `+ getRequestedQty(): int`
  - `+ getAvailableQty(): int`

### 4.7. `class EntityNotFoundException extends FoodDeliveryException`
- **Thuộc tính (Attributes):**
  - `- entityName: String`
  - `- entityId: String`
- **Hàm khởi tạo (Constructors):**
  - `+ EntityNotFoundException(entityName: String, entityId: String)`
- **Phương thức (Methods):**
  - `+ getEntityName(): String`
  - `+ getEntityId(): String`

### 4.8. `class InvalidInputException extends FoodDeliveryException`
- **Thuộc tính (Attributes):**
  - `- fieldName: String`
- **Hàm khởi tạo (Constructors):**
  - `+ InvalidInputException(message: String)`
  - `+ InvalidInputException(fieldName: String, message: String)`
- **Phương thức (Methods):**
  - `+ getFieldName(): String`

---

## 5. PACKAGE `view`

### 5.1. `interface IView`
- `+ display(): void`

### 5.2. `class MainView implements IView`
- **Thuộc tính (Attributes):**
  - `- customerController: CustomerController`
  - `- restaurantController: RestaurantController`
  - `- driverController: DriverController`
  - `- orderController: OrderController`
  - `- simulatorController: SimulatorController`
  - `- authController: AuthController`
  - `- adminController: AdminController`
  - `- mapView: MapView`
  - `- simulatorView: SimulatorView`
  - `- orderView: OrderView`
  - `- reportView: ReportView`
  - `- scanner: Scanner`
  - `- mainController: MainController`
- **Hàm khởi tạo (Constructors):**
  - `+ MainView()`
  - `+ MainView(mainController: MainController)`
- **Phương thức chính (Methods):**
  - `+ display(): void`
  - `- displayLoginScreen(): boolean`
  - `- handleLoginFlow(): boolean`
  - `- handleCustomerRegistrationFlow(): void`
  - `- handleDriverApplicationSubmissionFlow(): void`
  - `- handleDisplaySampleAccounts(): void`
  - `- handleRoleMenu(user: User): void`
  - `- handleCustomerMenu(user: User): void`
  - `- checkAndDisplayCustomerNotifications(customerId: String): void`
  - `- handleCustomerBrowseRestaurants(): void`
  - `- handleCustomerPlaceOrderFlow(customerId: String): void`
  - `- handleViewMyOrdersFlow(customerId: String): void`
  - `- handleLiveOrderTrackerDetail(order: Order, customerId: String): void`
  - `- simulateDeliveryProgress(order: Order): void`
  - `- handleCustomerCancelOrderFlow(customerId: String): void`
  - `- handleCustomerProfileFlow(user: User): void`
  - `- handleDriverMenu(user: User): void`
  - `- handleDriverProfileFlow(user: User): void`
  - `- handleDriverStatusToggleFlow(driverId: String): void`
  - `- handleViewDriverOrdersFlow(driverId: String): void`
  - `- handleDriverPickUpFlow(driverId: String): void`
  - `- handleDriverDeliverFlow(driverId: String): void`
  - `- handleRestaurantMenu(user: User): void`
  - `- handleRestaurantViewMenuFlow(restaurantId: String, rest: Restaurant): void`
  - `- handleRestaurantUpdateStockFlow(restaurantId: String): void`
  - `- handleRestaurantViewIncomingOrdersFlow(restaurantId: String): void`
  - `- handleRestaurantConfirmOrderFlow(restaurantId: String): void`
  - `- handleRestaurantRejectOrderFlow(restaurantId: String): void`
  - `- handleRestaurantStatsFlow(restaurantId: String, restName: String): void`
  - `- handleAdminMenu(user: User): void`
  - `- handleDriverApplicationsReviewFlow(): void`
  - `- handleDispatcherMenu(user: User): void`
  - `- handleViewOrdersFlow(): void`
  - `- handleDisplayAsciiMap(): void`
  - `- handleRunSimulatorBenchmark(): void`
  - `- handleDataGeneration(): void`
  - `- handleAutoDispatchPendingFlow(): void`
  - `- handleDriverManagementFlow(): void`
  - `- handleUserManagementFlow(): void`
  - `- displayUserTable(users: List<User>): void`
  - `- handleRestaurantManagementFlow(): void`
  - `- handleCustomerManagementFlow(): void`
  - `- handleSystemOverviewFlow(): void`

### 5.3. `class MapView`
- **Thuộc tính (Attributes):**
  - `- rows: int`
  - `- cols: int`
  - `- minLat: double`
  - `- maxLat: double`
  - `- minLon: double`
  - `- maxLon: double`
- **Hàm khởi tạo (Constructors):**
  - `+ MapView()`
- **Phương thức (Methods):**
  - `+ renderAsciiMap(restaurants: List<Restaurant>, customers: List<Customer>, drivers: List<Driver>): void`
  - `- plot(grid: char[][], lat: double, lon: double, symbol: char): void`

### 5.4. `class OrderView`
- **Phương thức (Methods):**
  - `+ renderMenu(items: List<MenuItem>): void`
  - `+ displayMenu(restaurant: Restaurant, items: List<MenuItem>): void`
  - `+ renderOrders(orders: List<Order>): void`
  - `+ displayRecentOrders(orders: List<Order>, count: int): void`

### 5.5. `class ReportView`
- **Phương thức (Methods):**
  - `+ printResearchConclusion(runs: List<SimulationRun>): void`
  - `+ renderSimulationSummary(run: SimulationRun): void`

### 5.6. `class SimulatorView`
- **Phương thức (Methods):**
  - `+ printBenchmarkTable(runs: List<SimulationRun>): void`
  - `+ renderComparisonTable(runs: List<SimulationRun>): void`

---

## 6. ROOT / ENTRY POINT

### `class Main`
- **Phương thức (Methods):**
  - `+ main(args: String[]): void` *(static)*

---

## 7. BẢNG TÓM TẮT QUAN HỆ GIỮA CÁC CLASS (RELATIONSHIPS SUMMARY)

Khi vẽ trên draw.io / StarUML, các mối quan hệ được nối như sau:

| Kiểu quan hệ (UML) | Từ Class (Source) | Đến Class (Target) | Ý nghĩa / Ghi chú |
|---|---|---|---|
| **Inheritance** (`extends` - mũi tên tam giác rỗng nét liền) | `Customer`, `Restaurant`, `MenuItem`, `Driver`, `DriverApplication`, `Order`, `OrderItem`, `DeliveryRoute`, `SimulationRun`, `User` | `BaseEntity` | Tất cả Entity kế thừa các trường chung (`id`, `createdAt`, `updatedAt`, `version`). |
| **Inheritance** (`extends`) | `UserAccount` | `User` | Mở rộng tài khoản người dùng. |
| **Inheritance** (`extends`) | `CustomerRepository`, `RestaurantRepository`, `MenuItemRepository`, `DriverRepository`, `DriverApplicationRepository`, `OrderRepository`, `OrderItemRepository`, `DeliveryRouteRepository`, `SimulationRunRepository`, `UserRepository` | `CsvRepository<T>` | Tầng Repository kế thừa thao tác file CSV. |
| **Realization** (`implements` - mũi tên tam giác rỗng nét đứt) | `CsvRepository<T>` | `IRepository<T>` | Thực thi interface CRUD generic. |
| **Realization** (`implements`) | `MainView` | `IView` | Thực thi interface hiển thị View. |
| **Inheritance** (`extends`) | `ConcurrencyException`, `EntityNotFoundException`, `InvalidInputException` | `FoodDeliveryException` | Phân cấp ngoại lệ của hệ thống. |
| **Inheritance** (`extends`) | `DoubleAssignmentException`, `DriverOverloadException`, `OptimisticLockException`, `OversellException` | `ConcurrencyException` | 4 ngoại lệ tương ứng với các tình huống tranh chấp dữ liệu. |
| **Inheritance** (`extends`) | `CustomerThread`, `DispatchThread` | `java.lang.Thread` | Các luồng mô phỏng khách hàng và điều phối. |
| **Inheritance** (`extends`) | `AppController` | `MainController` | Kế thừa / alias của controller chính. |
| **Aggregation / Composition** (nét liền có hình thoi) | `MainController` | `AuthController`, `CustomerController`, `RestaurantController`, `DriverController`, `OrderController`, `SimulatorController`, `AdminController`, `MainView`, `MapView`, `OrderView`, `ReportView`, `SimulatorView` | MainController chứa và quản lý toàn bộ Controllers và Views. |
| **Aggregation** (nét liền có hình thoi rỗng) | `CustomerController` | `CustomerRepository`, `OrderRepository` | Controller sở hữu các repository tương ứng. |
| **Aggregation** | `OrderController` | `OrderRepository`, `MenuItemRepository`, `DriverRepository`, `RestaurantRepository`, `OrderItemRepository`, `CustomerRepository`, `DeliveryRouteRepository` | Controller xử lý chu trình đặt & giao đơn hàng. |
| **Aggregation** | `DriverRepository` | `GeoUtils` | Dùng GeoUtils để tính khoảng cách Haversine và tìm tài xế gần nhất. |
| **Association / Dependency** | `Order` | `Customer`, `Restaurant`, `Driver` | Thông qua các khóa ngoại `customerId`, `restaurantId`, `driverId`. |
| **Association** | `OrderItem` | `Order`, `MenuItem` | Thông qua `orderId`, `menuItemId`. |
| **Association** | `MenuItem` | `Restaurant` | Thông qua `restaurantId`. |
| **Association** | `Driver` | `DriverStatus` | Sở hữu trạng thái `status`. |
| **Association** | `Order` | `OrderStatus` | Sở hữu trạng thái `status`. |
| **Association** | `User` | `UserRole` | Sở hữu vai trò người dùng `role`. |
| **Association** | `SimulationRun` | `LockMechanism` | Ghi nhận cơ chế khóa đã chạy. |
