# 🏛️ TOÀN BỘ CLASS DIAGRAM HỆ THỐNG (60 CLASSES - 120+ RELATIONSHIPS)

> **Đồ án:** Food Delivery Network Simulation  
> **Môn học:** LAB211 – OOP with Java | FPT University  
> **Kiến trúc:** 100% OOP (Không static ngoài main, không final/protected, 100% this., MVC Pattern trên CSV)

```mermaid
classDiagram
    direction TB

    class BaseEntity {
        <<abstract>>
        - id String
        - createdAt, updatedAt LocalDateTime
        - version long
        + getId() / setId(id) void
        + getVersion() / setVersion(v) void
        + incrementVersion() void
        + toCsvLine()* String
        + fromCsvLine(line)* void
    }
    class Customer {
        <<extends BaseEntity>>
        - fullName, phone, ward String
        - latitude, longitude double
        + getFullName() / setFullName()
        + getPhone() / setPhone()
        + getWard() / setWard()
        + toCsvLine() String
        + fromCsvLine(line) void
    }
    class Restaurant {
        <<extends BaseEntity>>
        - name, category, address String
        - latitude, longitude double
        - rating double
        - open boolean
        + getName() / getAddress()
        + isOpen() / setOpen(open)
        + getRating() double
        + toCsvLine() String
        + fromCsvLine(line) void
    }
    class MenuItem {
        <<extends BaseEntity>>
        - restaurantId, name String
        - priceVnd double
        - stockQty int
        + getRestaurantId() String
        + getPriceVnd() / setPriceVnd()
        + getStockQty() / setStockQty()
        + toCsvLine() String
        + fromCsvLine(line) void
    }
    class Driver {
        <<extends BaseEntity>>
        - fullName, phone, licensePlate String
        - latitude, longitude double
        - status DriverStatus
        - activeOrderCount int
        - currentOrderId String
        + getStatus() / setStatus(st)
        + getActiveOrderCount() / setActiveOrderCount()
        + getCurrentOrderId() / setCurrentOrderId()
        + toCsvLine() String
        + fromCsvLine(line) void
    }
    class Order {
        <<extends BaseEntity>>
        - customerId, restaurantId String
        - driverId String
        - status OrderStatus
        - totalFoodAmount, deliveryFee double
        - createdAtEpochMs long
        - assignCount int
        + getDriverId() / setDriverId(did)
        + getStatus() / setStatus(st)
        + getTotalAmount() double
        + toCsvLine() String
        + fromCsvLine(line) void
    }
    class OrderItem {
        <<extends BaseEntity>>
        - orderId, menuItemId String
        - quantity int
        - unitPriceVnd double
        + getOrderId() String
        + getMenuItemId() String
        + getQuantity() / getUnitPriceVnd()
        + toCsvLine() String
        + fromCsvLine(line) void
    }
    class DeliveryRoute {
        <<extends BaseEntity>>
        - orderId, driverId String
        - pickupDistanceKm double
        - deliveryDistanceKm double
        - estimatedMinutes int
        + getPickupDistanceKm() double
        + getDeliveryDistanceKm() double
        + getEstimatedMinutes() int
        + toCsvLine() String
        + fromCsvLine(line) void
    }
    class SimulationRun {
        <<extends BaseEntity>>
        - mechanism LockMechanism
        - totalOrders int, durationMs long
        - throughputOpsPerSec double
        - throughputDropPercent double
        - doubleAssignErrors, oversellErrors int
        - driverOverloadErrors int
        + getMechanism() LockMechanism
        + getThroughputOpsPerSec() double
        + getThroughputDropPercent() double
        + getTotalErrors() int
        + toCsvLine() String
        + fromCsvLine(line) void
    }
    class User {
        <<extends BaseEntity>>
        - username, password String
        - role UserRole
        - linkedEntityId String
        - displayName, phone String
        + getUsername() / getRole()
        + getLinkedEntityId() String
        + getDisplayName() String
        + toCsvLine() String
        + fromCsvLine(line) void
    }
    class DriverApplication {
        <<extends BaseEntity>>
        - username, password String
        - fullName, phone, licensePlate String
        - vehicleType, status, note String
        + getUsername() / getStatus()
        + setStatus(status String) void
        + toCsvLine() String
        + fromCsvLine(line) void
    }
    class UserAccount {
        <<extends User>>
        + UserAccount()
        + UserAccount(id, user, pass, ...)
    }
    class AuthSession {
        <<Value Object>>
        - role AccountRole
        - userId String
        - displayName String
        + getRole() AccountRole
        + getUserId() / getDisplayName()
    }
    class GeoUtils {
        <<Haversine Distance & ETA>>
        - earthRadiusKm double = 6371.0
        + haversineDistance(lat1, lon1, lat2, lon2) double
        + calculateDeliveryFee(dist) double
        + calculatePickupMinutes(dist) int
        + calculateDeliveryMinutes(pDist, dDist) int
        + formatEstimatedClockTime(epoch, mins) String
    }
    class DriverStatus {
        <<enum>>
        AVAILABLE
        BUSY
        OFFLINE
    }
    class OrderStatus {
        <<enum>>
        PENDING
        CONFIRMED
        DISPATCHED
        DELIVERED
        CANCELLED
    }
    class UserRole {
        <<enum>>
        CUSTOMER
        DRIVER
        RESTAURANT
        DISPATCHER
        ADMIN
        RESTAURANT_ADMIN
        SYSTEM_ADMIN
        USER_ADMIN
    }
    class AccountRole {
        <<enum>>
        CUSTOMER
        DRIVER
    }
    class LockMechanism {
        <<enum>>
        NO_LOCK
        SYNCHRONIZED
        OPTIMISTIC
        FILE_LOCK
    }
    class IRepository {
        <<interface>>
        + findAll() List<T>
        + findById(id String) T
        + save(entity T) void
        + saveAll(entities List<T>) void
        + deleteById(id String) void
    }
    class CsvRepository {
        <<abstract implements IRepository<T>>>
        - filePath, header String
        - fileMonitor Object
        - keyLocks ConcurrentHashMap
        + CsvRepository(filePath, header)
        + createEntity()* T
        + parseLine(line String) T
        + findAll() / findById(id) synchronized
        + save(entity) / deleteById(id) synchronized
        + getKeyLock(key String) Object
        + executeWithFileLock(action Runnable) void
        + updateWithOptimisticLock(entity, ver) void
    }
    class CustomerRepository {
        <<extends CsvRepository<Customer>>>
        + CustomerRepository()
        + createEntity() Customer
    }
    class RestaurantRepository {
        <<extends CsvRepository<Restaurant>>>
        + RestaurantRepository()
        + createEntity() Restaurant
    }
    class MenuItemRepository {
        <<extends CsvRepository<MenuItem>>>
        + findByRestaurantId(restId) List<MenuItem>
        + deductStock(itemId, qty, mech) boolean
        - deductStockNoLock / Sync / Optimistic / FileLock
        + restoreStock(itemId, qty) boolean
    }
    class DriverRepository {
        <<extends CsvRepository<Driver>>>
        - geoUtils GeoUtils
        + findAvailableDrivers() List<Driver>
        + findNearestAvailable(lat, lon) Driver
        + markBusy(driverId, orderId, mech) boolean
        - markBusyNoLock / Sync / Optimistic / FileLock
        + markAvailable(driverId String) boolean
    }
    class OrderRepository {
        <<extends CsvRepository<Order>>>
        + processOrderAndDispatch(custId, restId, itemId, qty, mech, ...) Order
        + dispatchPendingOrder(orderId, mech, ...) boolean
        + autoDispatchAllPending(mech, ...) List<String>
        + assignDriver(orderId, driverId, mech) boolean
        - assignDriverNoLock / Sync / Optimistic / FileLock
    }
    class OrderItemRepository {
        <<extends CsvRepository<OrderItem>>>
        + findByOrderId(orderId) List<OrderItem>
        + createEntity() OrderItem
    }
    class DeliveryRouteRepository {
        <<extends CsvRepository<DeliveryRoute>>>
        + DeliveryRouteRepository()
        + createEntity() DeliveryRoute
    }
    class SimulationRunRepository {
        <<extends CsvRepository<SimulationRun>>>
        + SimulationRunRepository()
        + createEntity() SimulationRun
    }
    class DriverApplicationRepository {
        <<extends CsvRepository<DriverApp>>>
        + findPendingApplications() List<DriverApp>
        + findByUsername(username) DriverApp
        + findByPhone(phone) DriverApp
    }
    class UserRepository {
        <<extends CsvRepository<User>>>
        + findByUsername(username) User
        + findByPhone(phone) User
        + createEntity() User
    }
    class AccountRepository {
        <<PBKDF2 Password Security>>
        - filePath Path
        - iterations int = 120000
        + register(role, id, pass) void
        + authenticate(role, id, pass) boolean
        + hasAccount(role, id) boolean
    }
    class MainController {
        <<Central Facade Controller>>
        - authController AuthController
        - customerController CustomerController
        - restaurantController RestaurantController
        - driverController DriverController
        - orderController OrderController
        - simulatorController SimulatorController
        - adminController AdminController
        - mainView MainView
        + run() void
        + login(username, password) boolean
        + logout() void
        + getCurrentUser() User
        + runBenchmark(mech, orders) SimulationRun
        + autoDispatchAllPending(mech) List<String>
    }
    class AppController {
        <<extends MainController>>
        + AppController()
    }
    class AuthController {
        <<Authentication & Registration>>
        - userRepo UserRepository
        - customerRepo CustomerRepository
        - driverRepo DriverRepository
        - driverAppRepo DriverAppRepository
        - currentUser User
        + login(username, password) boolean
        + logout() void
        + registerCustomer(...) User
        + submitDriverApplication(...) DriverApp
        + registerDriver(...) User
        + getAllUsers() List<User>
    }
    class CustomerController {
        <<Customer Flow>>
        - customerRepository CustomerRepository
        - orderRepository OrderRepository
        + registerCustomer(name, phone, ward, lat, lon) Customer
        + getAllCustomers() List<Customer>
        + getCustomerById(id) Customer
        + updateCustomer(id, ...) boolean
        + getOrderHistory(customerId) List<Order>
    }
    class DriverController {
        <<Driver Flow>>
        - driverRepository DriverRepository
        + getAllDrivers() List<Driver>
        + getAvailableDrivers() List<Driver>
        + getNearestAvailableDriver(lat, lon) Driver
        + updateDriverStatus(id, status) boolean
        + updateDriverInfo(id, ...) boolean
    }
    class RestaurantController {
        <<Restaurant Flow>>
        - restaurantRepository RestaurantRepository
        - menuItemRepository MenuItemRepository
        + getAllRestaurants() List<Restaurant>
        + getMenuByRestaurant(restId) List<MenuItem>
        + updateStock(menuItemId, newStock) boolean
        + createMenuItem(restId, name, price, stock) MenuItem
    }
    class OrderController {
        <<Order Lifecycle & Auto-Dispatch>>
        - orderRepository OrderRepository
        - menuItemRepository MenuItemRepository
        - driverRepository DriverRepository
        - restaurantRepository RestaurantRepository
        - orderItemRepository OrderItemRepository
        - customerRepository CustomerRepository
        - routeRepository DeliveryRouteRepository
        + placeOrderAndDispatch(custId, restId, itemId, qty, mech) Order
        + createCustomerOrder(custId, restId, itemId, qty, mech) Order
        + confirmOrderByRestaurant(orderId, restId, mech) Order
        + rejectOrderByRestaurant(orderId, restId, reason) boolean
        + cancelOrderByCustomer(orderId, custId) boolean
        + markOrderPickedUp(orderId, driverId) boolean
        + markOrderDelivered(orderId, driverId, mech) boolean
        + autoDispatchPendingOrders(mech) int
    }
    class AdminController {
        <<System Administration>>
        - restaurantRepo RestaurantRepository
        - menuItemRepo MenuItemRepository
        - customerRepo CustomerRepository
        - driverRepo DriverRepository
        - driverAppRepo DriverAppRepository
        - userRepo UserRepository
        + getPendingDriverApplications() List<DriverApp>
        + approveDriverApplication(id) User
        + rejectDriverApplication(id, reason) boolean
        + addRestaurant(name, cat, addr, lat, lon) Restaurant
        + restockMenuItem(itemId, qty) void
        + triggerAutoDispatchAllPending(mech) int
    }
    class SimulatorController {
        <<Concurrency Benchmark Simulator>>
        - orderRepo OrderRepository
        - driverRepo DriverRepository
        - menuItemRepo MenuItemRepository
        - simRunRepo SimulationRunRepository
        + runSimulation(numOrders, mechanism) SimulationRun
        + runExperiment(mechanism, numOrders, baseline) SimulationRun
        + detectDoubleAssignments() int
        + detectOversellItems() int
        + detectDriverOverload() int
    }
    class CustomerThread {
        <<extends java.lang.Thread>>
        - startLatch, finishLatch CountDownLatch
        - orderController OrderController
        - customer Customer, restaurant Restaurant, menuItem MenuItem
        + run() void (Executes concurrent order placement)
    }
    class DispatchThread {
        <<extends java.lang.Thread>>
        - startLatch, finishLatch CountDownLatch
        - orderController OrderController
        - orderId String
        - mechanism LockMechanism
        + run() void (Executes concurrent dispatching)
    }
    class CustomerSimTask {
        <<implements java.lang.Runnable>>
        - startGate, doneGate CountDownLatch
        - menuItemRepo MenuItemRepository
        - targetItemId String
        - mechanism LockMechanism
        - simulatedOversell AtomicInteger
        + CustomerSimTask(...)
        + run() void (Executes concurrent stock deduction)
    }
    class DispatchSimTask {
        <<implements java.lang.Runnable>>
        - startGate, doneGate CountDownLatch
        - driverRepo DriverRepository
        - orderRepo OrderRepository
        - targetOrderId, targetDriverId String
        - mechanism LockMechanism
        - simulatedDoubleAssign, simulatedDriverOverload AtomicInteger
        + DispatchSimTask(...)
        + run() void (Executes concurrent driver dispatch)
    }
    class FoodDeliveryException {
        <<extends java.lang.Exception>>
        + FoodDeliveryException(msg)
        + FoodDeliveryException(msg, cause)
    }
    class ConcurrencyException {
        <<extends FoodDeliveryException>>
        + ConcurrencyException(msg)
        + ConcurrencyException(msg, cause)
    }
    class DoubleAssignmentException {
        <<extends ConcurrencyException>>
        - orderId String
        - existingDriverId, attemptedDriverId String
        + getOrderId() String
        + getExistingDriverId() String
        + getAttemptedDriverId() String
    }
    class DriverOverloadException {
        <<extends ConcurrencyException>>
        - driverId String
        - currentOrderId String
        + getDriverId() String
        + getCurrentOrderId() String
    }
    class OptimisticLockException {
        <<extends ConcurrencyException>>
        - entityId String
        - expectedVersion, currentVersion long
        + getEntityId() String
        + getExpectedVersion() long
        + getCurrentVersion() long
    }
    class OversellException {
        <<extends ConcurrencyException>>
        - menuItemId String
        - requestedQty, availableQty int
        + getMenuItemId() String
        + getRequestedQty() int
        + getAvailableQty() int
    }
    class EntityNotFoundException {
        <<extends FoodDeliveryException>>
        - entityName, entityId String
        + getEntityName() String
        + getEntityId() String
    }
    class InvalidInputException {
        <<extends FoodDeliveryException>>
        - fieldName String
        + getFieldName() String
    }
    class Main {
        <<Application Entrypoint>>
        + main(args String[]) void (static)
    }
    class IView {
        <<interface>>
        + display() void
    }
    class MainView {
        <<implements IView>>
        - mainController MainController
        - customerController, restaurantController, driverController, orderController, simulatorController, authController, adminController
        - mapView MapView, simulatorView SimulatorView, orderView OrderView, reportView ReportView
        + display() void
        - displayLoginScreen() boolean
        - handleCustomerMenu() / handleDriverMenu()
        - handleRestaurantMenu() / handleAdminMenu()
        - checkAndDisplayCustomerNotifications(custId) void
        - handleCustomerPlaceOrderFlow(custId) void
    }
    class MapView {
        <<ASCII Grid Map Visualizer>>
        - rows, cols int
        - minLat, maxLat, minLon, maxLon double
        + renderAsciiMap(restaurants, customers, drivers, orders) void
    }
    class OrderView {
        <<Orders & Menus Output>>
        + renderOrders(orders List<Order>) void
        + displayMenu(restaurant, items) void
    }
    class ReportView {
        <<Benchmark Summary>>
        + printResearchConclusion(runs) void
        + renderSimulationSummary(run) void
    }
    class SimulatorView {
        <<Benchmark Comparison Table>>
        + printBenchmarkTable(runs) void
        + renderComparisonTable(runs) void
    }

    %% RELATIONSHIPS
    BaseEntity <|-- Customer
    BaseEntity <|-- Restaurant
    BaseEntity <|-- MenuItem
    BaseEntity <|-- Driver
    BaseEntity <|-- Order
    BaseEntity <|-- OrderItem
    BaseEntity <|-- DeliveryRoute
    BaseEntity <|-- SimulationRun
    BaseEntity <|-- User
    BaseEntity <|-- DriverApplication
    User <|-- UserAccount
    Order --> Customer : places
    Order --> Restaurant : from
    Order --> Driver : delivered by
    Order --> OrderStatus : status
    OrderItem --> Order : belongs to
    OrderItem --> MenuItem : item
    MenuItem --> Restaurant : offers
    Driver --> DriverStatus : status
    User --> UserRole : role
    AuthSession --> AccountRole : role
    SimulationRun --> LockMechanism : mechanism
    DeliveryRoute --> Order : routes
    DeliveryRoute --> Driver : assigned driver
    IRepository <|.. CsvRepository
    CsvRepository <|-- CustomerRepository
    CsvRepository <|-- RestaurantRepository
    CsvRepository <|-- MenuItemRepository
    CsvRepository <|-- DriverRepository
    CsvRepository <|-- OrderRepository
    CsvRepository <|-- OrderItemRepository
    CsvRepository <|-- DeliveryRouteRepository
    CsvRepository <|-- SimulationRunRepository
    CsvRepository <|-- DriverApplicationRepository
    CsvRepository <|-- UserRepository
    DriverRepository ..> GeoUtils : calculates distance
    AccountRepository ..> AccountRole : authenticates
    MainController <|-- AppController
    MainController o-- AuthController
    MainController o-- CustomerController
    MainController o-- DriverController
    MainController o-- RestaurantController
    MainController o-- OrderController
    MainController o-- AdminController
    MainController o-- SimulatorController
    MainController o-- MainView
    MainController o-- MapView
    MainController o-- OrderView
    MainController o-- ReportView
    MainController o-- SimulatorView
    AuthController o-- UserRepository
    AuthController o-- CustomerRepository
    AuthController o-- DriverRepository
    AuthController o-- DriverApplicationRepository
    CustomerController o-- CustomerRepository
    CustomerController o-- OrderRepository
    DriverController o-- DriverRepository
    RestaurantController o-- RestaurantRepository
    RestaurantController o-- MenuItemRepository
    OrderController o-- OrderRepository
    OrderController o-- MenuItemRepository
    OrderController o-- DriverRepository
    OrderController o-- RestaurantRepository
    OrderController o-- OrderItemRepository
    OrderController o-- CustomerRepository
    OrderController o-- DeliveryRouteRepository
    OrderController ..> GeoUtils : calculates fee & distance
    AdminController o-- RestaurantRepository
    AdminController o-- MenuItemRepository
    AdminController o-- CustomerRepository
    AdminController o-- DriverRepository
    AdminController o-- DriverApplicationRepository
    AdminController o-- UserRepository
    AdminController o-- OrderController
    SimulatorController o-- SimulationRunRepository
    SimulatorController o-- OrderRepository
    SimulatorController o-- DriverRepository
    SimulatorController o-- MenuItemRepository
    SimulatorController o-- CustomerSimTask
    SimulatorController o-- DispatchSimTask
    CustomerSimTask o-- MenuItemRepository
    CustomerSimTask --> LockMechanism
    DispatchSimTask o-- DriverRepository
    DispatchSimTask o-- OrderRepository
    DispatchSimTask --> LockMechanism
    CustomerThread ..> OrderController : calls
    DispatchThread ..> OrderController : calls
    DispatchThread ..> LockMechanism : uses mechanism
    FoodDeliveryException <|-- ConcurrencyException
    FoodDeliveryException <|-- EntityNotFoundException
    FoodDeliveryException <|-- InvalidInputException
    ConcurrencyException <|-- DoubleAssignmentException
    ConcurrencyException <|-- DriverOverloadException
    ConcurrencyException <|-- OptimisticLockException
    ConcurrencyException <|-- OversellException
    MenuItemRepository ..> OversellException : throws
    OrderRepository ..> DoubleAssignmentException : throws
    DriverRepository ..> DriverOverloadException : throws
    OrderRepository ..> OptimisticLockException : throws
    OrderController ..> InvalidInputException : throws
    OrderController ..> EntityNotFoundException : throws
    Main ..> MainController : instantiates
    IView <|.. MainView
    MainView o-- MainController
    MainView o-- MapView
    MainView o-- OrderView
    MainView o-- ReportView
    MainView o-- SimulatorView
    MapView ..> Restaurant : displays [R]
    MapView ..> Customer : displays [C]
    MapView ..> Driver : displays [D]/[B]
    MapView ..> Order : audits active
```
