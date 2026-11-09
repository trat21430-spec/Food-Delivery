# FULL UML CLASS DIAGRAM: FOOD DELIVERY NETWORK SIMULATION (MVC)
**LAB211 — OOP with Java | FPT University (Nhóm 01)**

---

## 1. SƠ ĐỒ LỚP TỔNG THỂ (MERMAID CLASS DIAGRAM)

```mermaid
classDiagram
    direction TB

    %% ==========================================
    %% 1. MODEL LAYER
    %% ==========================================
    class BaseEntity {
        <<abstract>>
        -String id
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -long version
        +getId() String
        +setId(String id) void
        +getCreatedAt() LocalDateTime
        +setCreatedAt(LocalDateTime createdAt) void
        +getUpdatedAt() LocalDateTime
        +setUpdatedAt(LocalDateTime updatedAt) void
        +getVersion() long
        +setVersion(long version) void
        +incrementVersion() void
        +toCsvLine()* String
        +fromCsvLine(String line)* void
    }

    class Customer {
        -String fullName
        -String phone
        -String ward
        -double latitude
        -double longitude
        +getFullName() String
        +setFullName(String fullName) void
        +getPhone() String
        +setPhone(String phone) void
        +getWard() String
        +setWard(String ward) void
        +getLatitude() double
        +getLongitude() double
        +toCsvLine() String
        +fromCsvLine(String line) void
    }

    class Restaurant {
        -String name
        -String category
        -String address
        -double latitude
        -double longitude
        -double rating
        -boolean open
        +getName() String
        +getAddress() String
        +getRating() double
        +isOpen() boolean
        +setOpen(boolean open) void
        +toCsvLine() String
        +fromCsvLine(String line) void
    }

    class MenuItem {
        -String restaurantId
        -String name
        -double priceVnd
        -int stockQty
        +getRestaurantId() String
        +getName() String
        +getPriceVnd() double
        +getStockQty() int
        +setStockQty(int stockQty) void
        +toCsvLine() String
        +fromCsvLine(String line) void
    }

    class Driver {
        -String fullName
        -String phone
        -String licensePlate
        -double latitude
        -double longitude
        -DriverStatus status
        -int activeOrderCount
        -String currentOrderId
        +getFullName() String
        +getLicensePlate() String
        +getStatus() DriverStatus
        +setStatus(DriverStatus status) void
        +getActiveOrderCount() int
        +getCurrentOrderId() String
        +setCurrentOrderId(String currentOrderId) void
        +toCsvLine() String
        +fromCsvLine(String line) void
    }

    class DriverApplication {
        -String username
        -String password
        -String fullName
        -String phone
        -String licensePlate
        -String vehicleType
        -String status
        -String note
        +getUsername() String
        +getStatus() String
        +setStatus(String status) void
        +toCsvLine() String
        +fromCsvLine(String line) void
    }

    class Order {
        -String customerId
        -String restaurantId
        -String driverId
        -OrderStatus status
        -double totalFoodAmount
        -double deliveryFee
        -long createdAtEpochMs
        -int assignCount
        +getCustomerId() String
        +getRestaurantId() String
        +getDriverId() String
        +setDriverId(String driverId) void
        +getStatus() OrderStatus
        +setStatus(OrderStatus status) void
        +getTotalAmount() double
        +toCsvLine() String
        +fromCsvLine(String line) void
    }

    class OrderItem {
        -String orderId
        -String menuItemId
        -int quantity
        -double unitPriceVnd
        +getOrderId() String
        +getMenuItemId() String
        +getQuantity() int
        +getUnitPriceVnd() double
        +toCsvLine() String
        +fromCsvLine(String line) void
    }

    class DeliveryRoute {
        -String orderId
        -String driverId
        -double pickupDistanceKm
        -double deliveryDistanceKm
        -int estimatedMinutes
        +getPickupDistanceKm() double
        +getDeliveryDistanceKm() double
        +getEstimatedMinutes() int
        +toCsvLine() String
        +fromCsvLine(String line) void
    }

    class SimulationRun {
        -LockMechanism mechanism
        -int totalOrders
        -long durationMs
        -double throughputOpsPerSec
        -double throughputDropPercent
        -int doubleAssignErrors
        -int oversellErrors
        -int driverOverloadErrors
        +getMechanism() LockMechanism
        +getThroughputOpsPerSec() double
        +getTotalErrors() int
        +toCsvLine() String
        +fromCsvLine(String line) void
    }

    class User {
        -String username
        -String password
        -UserRole role
        -String linkedEntityId
        -String displayName
        -String phone
        +getUsername() String
        +getRole() UserRole
        +getLinkedEntityId() String
        +getDisplayName() String
        +toCsvLine() String
        +fromCsvLine(String line) void
    }

    class UserAccount {
        +UserAccount()
    }

    class GeoUtils {
        -double earthRadiusKm
        +haversineDistance(double lat1, double lon1, double lat2, double lon2) double
        +calculateDeliveryFee(double distanceKm) double
        +calculatePickupMinutes(double driverToRestKm) int
        +calculateDeliveryMinutes(double driverToRestKm, double restToCustKm) int
        +formatEstimatedClockTime(long baseEpochMs, int durationMinutes) String
    }

    %% Enums
    class DriverStatus {
        <<enumeration>>
        AVAILABLE
        BUSY
        OFFLINE
    }

    class OrderStatus {
        <<enumeration>>
        PENDING
        CONFIRMED
        DISPATCHED
        DELIVERED
        CANCELLED
    }

    class UserRole {
        <<enumeration>>
        CUSTOMER
        DRIVER
        RESTAURANT
        ADMIN
    }

    class LockMechanism {
        <<enumeration>>
        NO_LOCK
        SYNCHRONIZED
        OPTIMISTIC
        FILE_LOCK
    }

    %% ==========================================
    %% 2. REPOSITORY LAYER
    %% ==========================================
    class IRepository~T~ {
        <<interface>>
        +findAll() List~T~
        +findById(String id) T
        +save(T entity) void
        +saveAll(List~T~ entities) void
        +deleteById(String id) void
    }

    class CsvRepository~T~ {
        <<abstract>>
        -String filePath
        -String header
        -Object fileMonitor
        -ConcurrentHashMap keyLocks
        +createEntity()* T
        +parseLine(String line) T
        +findAll() List~T~
        +findById(String id) T
        +save(T entity) void
        +saveAll(List~T~ entities) void
        +deleteById(String id) void
        +getKeyLock(String key) Object
        +executeWithFileLock(Runnable action) void
        +updateWithOptimisticLock(T entity, long expectedVersion) void
    }

    class CustomerRepository {
        +createEntity() Customer
    }

    class RestaurantRepository {
        +createEntity() Restaurant
    }

    class MenuItemRepository {
        +createEntity() MenuItem
        +findByRestaurantId(String restaurantId) List~MenuItem~
        +deductStock(String menuItemId, int quantity, LockMechanism mechanism) boolean
        +restoreStock(String menuItemId, int quantity) boolean
    }

    class DriverRepository {
        -GeoUtils geoUtils
        +createEntity() Driver
        +findAvailableDrivers() List~Driver~
        +findNearestAvailable(double restaurantLat, double restaurantLon) Driver
        +markBusy(String driverId, String orderId, LockMechanism mechanism) boolean
        +markAvailable(String driverId) boolean
    }

    class DriverApplicationRepository {
        +createEntity() DriverApplication
        +findPendingApplications() List~DriverApplication~
        +findByUsername(String username) DriverApplication
        +findByPhone(String phone) DriverApplication
    }

    class OrderRepository {
        +createEntity() Order
        +processOrderAndDispatch(...) Order
        +dispatchPendingOrder(...) boolean
        +autoDispatchAllPending(...) List~String~
        +dispatchOrderToDriver(String orderId, String driverId, LockMechanism mechanism) boolean
        +assignDriver(String orderId, String driverId, LockMechanism mechanism) boolean
    }

    class OrderItemRepository {
        +createEntity() OrderItem
        +findByOrderId(String orderId) List~OrderItem~
    }

    class DeliveryRouteRepository {
        +createEntity() DeliveryRoute
    }

    class SimulationRunRepository {
        +createEntity() SimulationRun
    }

    class UserRepository {
        +createEntity() User
        +findByUsername(String username) User
        +findByPhone(String phone) User
    }

    %% ==========================================
    %% 3. CONTROLLER LAYER
    %% ==========================================
    class MainController {
        -AuthController authController
        -CustomerController customerController
        -RestaurantController restaurantController
        -DriverController driverController
        -OrderController orderController
        -SimulatorController simulatorController
        -AdminController adminController
        -MainView mainView
        +run() void
        +login(String username, String password) boolean
        +logout() void
        +getCurrentUser() User
    }

    class AppController {
        +AppController()
    }

    class AuthController {
        -UserRepository userRepository
        -CustomerRepository customerRepository
        -DriverRepository driverRepository
        -DriverApplicationRepository driverAppRepository
        -User currentUser
        +login(String username, String password) boolean
        +logout() void
        +registerCustomer(...) User
        +submitDriverApplication(...) DriverApplication
        +registerDriver(...) User
    }

    class CustomerController {
        -CustomerRepository customerRepository
        -OrderRepository orderRepository
        +registerCustomer(...) Customer
        +getAllCustomers() List~Customer~
        +getCustomerById(String id) Customer
        +updateCustomer(...) boolean
        +getOrderHistory(String customerId) List~Order~
    }

    class DriverController {
        -DriverRepository driverRepository
        +getAllDrivers() List~Driver~
        +getAvailableDrivers() List~Driver~
        +getNearestAvailableDriver(double lat, double lon) Driver
        +updateDriverStatus(String id, DriverStatus status) boolean
    }

    class RestaurantController {
        -RestaurantRepository restaurantRepository
        -MenuItemRepository menuItemRepository
        +getAllRestaurants() List~Restaurant~
        +getMenuByRestaurant(String restaurantId) List~MenuItem~
        +updateStock(String menuItemId, int newStock) boolean
    }

    class OrderController {
        -OrderRepository orderRepository
        -MenuItemRepository menuItemRepository
        -DriverRepository driverRepository
        -RestaurantRepository restaurantRepository
        -OrderItemRepository orderItemRepository
        -CustomerRepository customerRepository
        -DeliveryRouteRepository routeRepository
        +placeOrderAndDispatch(...) Order
        +confirmOrderByRestaurant(...) Order
        +rejectOrderByRestaurant(...) boolean
        +cancelOrderByCustomer(...) boolean
        +markOrderPickedUp(...) boolean
        +markOrderDelivered(...) boolean
        +autoDispatchPendingOrders(LockMechanism mechanism) int
    }

    class AdminController {
        -RestaurantRepository restaurantRepo
        -MenuItemRepository menuItemRepo
        -CustomerRepository customerRepo
        -DriverRepository driverRepo
        -DriverApplicationRepository driverAppRepo
        -UserRepository userRepo
        +getPendingDriverApplications() List~DriverApplication~
        +approveDriverApplication(String applicationId) User
        +rejectDriverApplication(String applicationId, String reason) boolean
        +addRestaurant(...) Restaurant
        +restockMenuItem(String menuItemId, int additionalQty) void
    }

    class SimulatorController {
        -OrderRepository orderRepo
        -DriverRepository driverRepo
        -MenuItemRepository menuItemRepo
        -SimulationRunRepository simRunRepo
        +runSimulation(int numOrders, LockMechanism mechanism) SimulationRun
        +detectDoubleAssignments() int
        +detectOversellItems() int
        +detectDriverOverload() int
    }

    class CustomerThread {
        -OrderController orderController
        -LockMechanism mechanism
        +run() void
    }

    class DispatchThread {
        -OrderController orderController
        -LockMechanism mechanism
        +run() void
    }

    %% ==========================================
    %% 4. EXCEPTION LAYER
    %% ==========================================
    class FoodDeliveryException {
        +FoodDeliveryException(String message)
    }

    class ConcurrencyException {
        +ConcurrencyException(String message)
    }

    class DoubleAssignmentException {
        -String orderId
        -String existingDriverId
        -String attemptedDriverId
    }

    class DriverOverloadException {
        -String driverId
        -String currentOrderId
    }

    class OptimisticLockException {
        -String entityId
        -long expectedVersion
        -long currentVersion
    }

    class OversellException {
        -String menuItemId
        -int requestedQty
        -int availableQty
    }

    class EntityNotFoundException {
        -String entityName
        -String entityId
    }

    class InvalidInputException {
        -String fieldName
    }

    %% ==========================================
    %% 5. VIEW LAYER
    %% ==========================================
    class IView {
        <<interface>>
        +display() void
    }

    class MainView {
        -MainController mainController
        +display() void
    }

    class MapView {
        +renderAsciiMap(List~Restaurant~, List~Customer~, List~Driver~) void
    }

    class OrderView {
        +displayMenu(Restaurant restaurant, List~MenuItem~ items) void
        +displayRecentOrders(List~Order~ orders, int count) void
    }

    class ReportView {
        +printResearchConclusion(List~SimulationRun~ runs) void
        +renderSimulationSummary(SimulationRun run) void
    }

    class SimulatorView {
        +printBenchmarkTable(List~SimulationRun~ runs) void
        +renderComparisonTable(List~SimulationRun~ runs) void
    }

    %% ==========================================
    %% 6. RELATIONSHIPS
    %% ==========================================
    BaseEntity <|-- Customer
    BaseEntity <|-- Restaurant
    BaseEntity <|-- MenuItem
    BaseEntity <|-- Driver
    BaseEntity <|-- DriverApplication
    BaseEntity <|-- Order
    BaseEntity <|-- OrderItem
    BaseEntity <|-- DeliveryRoute
    BaseEntity <|-- SimulationRun
    BaseEntity <|-- User
    User <|-- UserAccount

    IRepository <|.. CsvRepository
    CsvRepository <|-- CustomerRepository
    CsvRepository <|-- RestaurantRepository
    CsvRepository <|-- MenuItemRepository
    CsvRepository <|-- DriverRepository
    CsvRepository <|-- DriverApplicationRepository
    CsvRepository <|-- OrderRepository
    CsvRepository <|-- OrderItemRepository
    CsvRepository <|-- DeliveryRouteRepository
    CsvRepository <|-- SimulationRunRepository
    CsvRepository <|-- UserRepository

    MainController <|-- AppController

    FoodDeliveryException <|-- ConcurrencyException
    FoodDeliveryException <|-- EntityNotFoundException
    FoodDeliveryException <|-- InvalidInputException
    ConcurrencyException <|-- DoubleAssignmentException
    ConcurrencyException <|-- DriverOverloadException
    ConcurrencyException <|-- OptimisticLockException
    ConcurrencyException <|-- OversellException

    IView <|.. MainView

    Order --> Customer : places
    Order --> Restaurant : from
    Order --> Driver : delivered by
    Order --> OrderStatus : status
    OrderItem --> Order : belongs to
    OrderItem --> MenuItem : item
    MenuItem --> Restaurant : belongs to
    Driver --> DriverStatus : status
    User --> UserRole : role
    SimulationRun --> LockMechanism : benchmarks
    DriverRepository --> GeoUtils : uses

    MainController *-- AuthController
    MainController *-- CustomerController
    MainController *-- RestaurantController
    MainController *-- DriverController
    MainController *-- OrderController
    MainController *-- SimulatorController
    MainController *-- AdminController
    MainController *-- MainView

    CustomerController o-- CustomerRepository
    CustomerController o-- OrderRepository
    DriverController o-- DriverRepository
    RestaurantController o-- RestaurantRepository
    RestaurantController o-- MenuItemRepository
    OrderController o-- OrderRepository
    OrderController o-- MenuItemRepository
    OrderController o-- DriverRepository
    AdminController o-- DriverApplicationRepository
    SimulatorController o-- SimulationRunRepository
```
