package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * GENERATES 100% COMPLETE, STANDARDIZED 60-CLASS UML DIAGRAM MATCHING EXACT PRODUCTION CODE.
 * Covers EVERY class, interface, enum, and nested runnable task in src/.
 * Features:
 *  1. Multi-Tab Draw.io (.drawio) with 6 Tabs (Full Overview + 5 Isolated Package Tabs).
 *  2. Infinite Canvas (page="0") to eliminate coordinate shifting and page break slicing.
 *  3. Interactive SVG Pan-Zoom HTML Viewer with instant search, layer jumping, and auto-fit.
 *  4. Modular Draw.io and SVG files in docs/diagrams/ for lightweight opening.
 *  5. Segmented and Complete Mermaid diagrams in docs/class_diagram.md.
 */
public class DiagramGenerator {

    static class UmlClass {
        String id;
        String name;
        String sub;
        String[] attrs;
        String[] meths;
        String pkg;
        int x, y, w, h;

        UmlClass(String id, String name, String sub, String[] attrs, String[] meths, String pkg, int x, int y, int w, int h) {
            this.id = id;
            this.name = name;
            this.sub = sub;
            this.attrs = attrs;
            this.meths = meths;
            this.pkg = pkg;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        int cx() { return x + w / 2; }
        int cy() { return y + h / 2; }
        int top() { return y; }
        int bottom() { return y + h; }
        int left() { return x; }
        int right() { return x + w; }
    }

    static class UmlEdge {
        String id;
        String sourceId;
        String targetId;
        String type; // "inherit", "realize", "aggr", "assoc", "depend"
        String label;

        UmlEdge(String id, String sourceId, String targetId, String type, String label) {
            this.id = id;
            this.sourceId = sourceId;
            this.targetId = targetId;
            this.type = type;
            this.label = label;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== GENERATING EXACT 60-CLASS UML CLASS DIAGRAM (MULTI-TAB, MODULAR & INTERACTIVE) ===");
        try {
            List<UmlClass> classes = buildClasses();
            List<UmlEdge> edges = buildEdges();

            // 1. Generate Full SVG and Interactive HTML Pan-Zoom
            generateSvgAndHtml(classes, edges);

            // 2. Generate Multi-Tab Draw.io (Overview + 5 Layers)
            generateMultiTabDrawio(classes, edges);

            // 3. Generate Modular Files (0_Full, 1_Model, 2_Repo, 3_Ctrl, 4_Ex, 5_View)
            generateModularDiagrams(classes, edges);

            // 4. Generate Complete & Segmented Mermaid Markdown
            generateMermaidMarkdown(classes, edges);

            System.out.println(">>> COMPLETED ALL 60 CLASSES & 112 RELATIONSHIPS GENERATION IN ALL FORMATS! <<<");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<UmlClass> buildClasses() {
        List<UmlClass> list = new ArrayList<>();

        // ==========================================
        // 1. MODEL LAYER (19 Classes/Enums)
        // ==========================================
        list.add(new UmlClass("BaseEntity", "BaseEntity", "<<abstract>>",
                new String[]{"- id: String", "- createdAt, updatedAt: LocalDateTime", "- version: long"},
                new String[]{"+ getId() / setId(id): void", "+ getVersion() / setVersion(v): void", "+ incrementVersion(): void", "+ toCsvLine()*: String", "+ fromCsvLine(line)*: void"},
                "pkg_model", 580, 150, 280, 150));

        // Entities Row 1
        list.add(new UmlClass("Customer", "Customer", "extends BaseEntity",
                new String[]{"- fullName, phone, ward: String", "- latitude, longitude: double"},
                new String[]{"+ getFullName() / setFullName()", "+ getPhone() / setPhone()", "+ getWard() / setWard()", "+ toCsvLine(): String", "+ fromCsvLine(line): void"},
                "pkg_model", 60, 350, 230, 160));

        list.add(new UmlClass("Restaurant", "Restaurant", "extends BaseEntity",
                new String[]{"- name, category, address: String", "- latitude, longitude: double", "- rating: double", "- open: boolean"},
                new String[]{"+ getName() / getAddress()", "+ isOpen() / setOpen(open)", "+ getRating(): double", "+ toCsvLine(): String", "+ fromCsvLine(line): void"},
                "pkg_model", 320, 350, 230, 160));

        list.add(new UmlClass("MenuItem", "MenuItem", "extends BaseEntity",
                new String[]{"- restaurantId, name: String", "- priceVnd: double", "- stockQty: int"},
                new String[]{"+ getRestaurantId(): String", "+ getPriceVnd() / setPriceVnd()", "+ getStockQty() / setStockQty()", "+ toCsvLine(): String", "+ fromCsvLine(line): void"},
                "pkg_model", 580, 350, 230, 160));

        list.add(new UmlClass("Driver", "Driver", "extends BaseEntity",
                new String[]{"- fullName, phone, licensePlate: String", "- latitude, longitude: double", "- status: DriverStatus", "- activeOrderCount: int", "- currentOrderId: String"},
                new String[]{"+ getStatus() / setStatus(st)", "+ getActiveOrderCount() / setActiveOrderCount()", "+ getCurrentOrderId() / setCurrentOrderId()", "+ toCsvLine(): String", "+ fromCsvLine(line): void"},
                "pkg_model", 840, 350, 240, 160));

        list.add(new UmlClass("Order", "Order", "extends BaseEntity",
                new String[]{"- customerId, restaurantId: String", "- driverId: String", "- status: OrderStatus", "- totalFoodAmount, deliveryFee: double", "- createdAtEpochMs: long", "- assignCount: int"},
                new String[]{"+ getDriverId() / setDriverId(did)", "+ getStatus() / setStatus(st)", "+ getTotalAmount(): double", "+ toCsvLine(): String", "+ fromCsvLine(line): void"},
                "pkg_model", 1110, 350, 250, 170));

        // Entities Row 2
        list.add(new UmlClass("OrderItem", "OrderItem", "extends BaseEntity",
                new String[]{"- orderId, menuItemId: String", "- quantity: int", "- unitPriceVnd: double"},
                new String[]{"+ getOrderId(): String", "+ getMenuItemId(): String", "+ getQuantity() / getUnitPriceVnd()", "+ toCsvLine(): String", "+ fromCsvLine(line): void"},
                "pkg_model", 60, 560, 230, 140));

        list.add(new UmlClass("DeliveryRoute", "DeliveryRoute", "extends BaseEntity",
                new String[]{"- orderId, driverId: String", "- pickupDistanceKm: double", "- deliveryDistanceKm: double", "- estimatedMinutes: int"},
                new String[]{"+ getPickupDistanceKm(): double", "+ getDeliveryDistanceKm(): double", "+ getEstimatedMinutes(): int", "+ toCsvLine(): String", "+ fromCsvLine(line): void"},
                "pkg_model", 320, 560, 230, 140));

        list.add(new UmlClass("SimulationRun", "SimulationRun", "extends BaseEntity",
                new String[]{"- mechanism: LockMechanism", "- totalOrders: int, durationMs: long", "- throughputOpsPerSec: double", "- throughputDropPercent: double", "- doubleAssignErrors, oversellErrors: int", "- driverOverloadErrors: int"},
                new String[]{"+ getMechanism(): LockMechanism", "+ getThroughputOpsPerSec(): double", "+ getThroughputDropPercent(): double", "+ getTotalErrors(): int", "+ toCsvLine(): String", "+ fromCsvLine(line): void"},
                "pkg_model", 580, 560, 230, 160));

        list.add(new UmlClass("User", "User", "extends BaseEntity",
                new String[]{"- username, password: String", "- role: UserRole", "- linkedEntityId: String", "- displayName, phone: String"},
                new String[]{"+ getUsername() / getRole()", "+ getLinkedEntityId(): String", "+ getDisplayName(): String", "+ toCsvLine(): String", "+ fromCsvLine(line): void"},
                "pkg_model", 840, 560, 240, 160));

        list.add(new UmlClass("DriverApplication", "DriverApplication", "extends BaseEntity",
                new String[]{"- username, password: String", "- fullName, phone, licensePlate: String", "- vehicleType, status, note: String"},
                new String[]{"+ getUsername() / getStatus()", "+ setStatus(status: String): void", "+ toCsvLine(): String", "+ fromCsvLine(line): void"},
                "pkg_model", 1110, 560, 250, 150));

        // Row 3
        list.add(new UmlClass("UserAccount", "UserAccount", "extends User",
                new String[]{}, new String[]{"+ UserAccount()", "+ UserAccount(id, user, pass, ...)"},
                "pkg_model", 840, 750, 240, 70));

        list.add(new UmlClass("AuthSession", "AuthSession", "Value Object",
                new String[]{"- role: AccountRole", "- userId: String", "- displayName: String"},
                new String[]{"+ getRole(): AccountRole", "+ getUserId() / getDisplayName()"},
                "pkg_model", 1110, 740, 250, 90));

        list.add(new UmlClass("GeoUtils", "GeoUtils", "Haversine Distance & ETA",
                new String[]{"- earthRadiusKm: double = 6371.0"},
                new String[]{"+ haversineDistance(lat1, lon1, lat2, lon2): double", "+ calculateDeliveryFee(dist): double", "+ calculatePickupMinutes(dist): int", "+ calculateDeliveryMinutes(pDist, dDist): int", "+ formatEstimatedClockTime(epoch, mins): String"},
                "pkg_model", 60, 740, 240, 160));

        // Enums in Model
        list.add(new UmlClass("DriverStatus", "DriverStatus", "<<enum>>",
                new String[]{"AVAILABLE", "BUSY", "OFFLINE"}, new String[]{}, "pkg_model", 330, 740, 180, 90));

        list.add(new UmlClass("OrderStatus", "OrderStatus", "<<enum>>",
                new String[]{"PENDING", "CONFIRMED", "DISPATCHED", "DELIVERED", "CANCELLED"}, new String[]{}, "pkg_model", 540, 740, 220, 110));

        list.add(new UmlClass("UserRole", "UserRole", "<<enum>>",
                new String[]{"CUSTOMER", "DRIVER", "RESTAURANT", "DISPATCHER", "ADMIN", "RESTAURANT_ADMIN", "SYSTEM_ADMIN", "USER_ADMIN"}, new String[]{}, "pkg_model", 330, 870, 200, 140));

        list.add(new UmlClass("AccountRole", "AccountRole", "<<enum>>",
                new String[]{"CUSTOMER", "DRIVER"}, new String[]{}, "pkg_model", 560, 880, 160, 80));

        list.add(new UmlClass("LockMechanism", "LockMechanism", "<<enum>>",
                new String[]{"NO_LOCK", "SYNCHRONIZED", "OPTIMISTIC", "FILE_LOCK"}, new String[]{}, "pkg_model", 750, 870, 200, 100));

        // ==========================================
        // 2. REPOSITORY LAYER (13 Classes/Interfaces)
        // ==========================================
        list.add(new UmlClass("IRepository", "IRepository<T>", "<<interface>>",
                new String[]{},
                new String[]{"+ findAll(): List<T>", "+ findById(id: String): T", "+ save(entity: T): void", "+ saveAll(entities: List<T>): void", "+ deleteById(id: String): void"},
                "pkg_repo", 1520, 150, 260, 140));

        list.add(new UmlClass("CsvRepository", "CsvRepository<T extends BaseEntity>", "<<abstract>> implements IRepository<T>",
                new String[]{"- filePath, header: String", "- fileMonitor: Object", "- keyLocks: ConcurrentHashMap"},
                new String[]{"+ CsvRepository(filePath, header)", "+ createEntity()*: T", "+ parseLine(line: String): T", "+ findAll() / findById(id): synchronized", "+ save(entity) / deleteById(id): synchronized", "+ getKeyLock(key: String): Object", "+ executeWithFileLock(action: Runnable): void", "+ updateWithOptimisticLock(entity, ver): void"},
                "pkg_repo", 1820, 150, 420, 210));

        list.add(new UmlClass("CustomerRepository", "CustomerRepository", "extends CsvRepository<Customer>",
                new String[]{}, new String[]{"+ CustomerRepository()", "+ createEntity(): Customer"}, "pkg_repo", 1520, 390, 220, 90));

        list.add(new UmlClass("RestaurantRepository", "RestaurantRepository", "extends CsvRepository<Restaurant>",
                new String[]{}, new String[]{"+ RestaurantRepository()", "+ createEntity(): Restaurant"}, "pkg_repo", 1770, 390, 220, 90));

        list.add(new UmlClass("MenuItemRepository", "MenuItemRepository", "extends CsvRepository<MenuItem>",
                new String[]{},
                new String[]{"+ findByRestaurantId(restId): List<MenuItem>", "+ deductStock(itemId, qty, mech): boolean", "- deductStockNoLock / Sync / Optimistic / FileLock", "+ restoreStock(itemId, qty): boolean"},
                "pkg_repo", 2020, 390, 310, 150));

        list.add(new UmlClass("DriverRepository", "DriverRepository", "extends CsvRepository<Driver>",
                new String[]{"- geoUtils: GeoUtils"},
                new String[]{"+ findAvailableDrivers(): List<Driver>", "+ findNearestAvailable(lat, lon): Driver", "+ markBusy(driverId, orderId, mech): boolean", "- markBusyNoLock / Sync / Optimistic / FileLock", "+ markAvailable(driverId: String): boolean"},
                "pkg_repo", 2360, 390, 320, 150));

        list.add(new UmlClass("OrderRepository", "OrderRepository", "extends CsvRepository<Order>",
                new String[]{},
                new String[]{"+ processOrderAndDispatch(custId, restId, itemId, qty, mech, ...): Order", "+ dispatchPendingOrder(orderId, mech, ...): boolean", "+ autoDispatchAllPending(mech, ...): List<String>", "+ assignDriver(orderId, driverId, mech): boolean", "- assignDriverNoLock / Sync / Optimistic / FileLock"},
                "pkg_repo", 2710, 390, 400, 160));

        list.add(new UmlClass("OrderItemRepository", "OrderItemRepository", "extends CsvRepository<OrderItem>",
                new String[]{}, new String[]{"+ findByOrderId(orderId): List<OrderItem>", "+ createEntity(): OrderItem"}, "pkg_repo", 1520, 580, 220, 90));

        list.add(new UmlClass("DeliveryRouteRepository", "DeliveryRouteRepository", "extends CsvRepository<DeliveryRoute>",
                new String[]{}, new String[]{"+ DeliveryRouteRepository()", "+ createEntity(): DeliveryRoute"}, "pkg_repo", 1770, 580, 220, 90));

        list.add(new UmlClass("SimulationRunRepository", "SimulationRunRepository", "extends CsvRepository<SimulationRun>",
                new String[]{}, new String[]{"+ SimulationRunRepository()", "+ createEntity(): SimulationRun"}, "pkg_repo", 2020, 580, 220, 90));

        list.add(new UmlClass("DriverApplicationRepository", "DriverAppRepository", "extends CsvRepository<DriverApp>",
                new String[]{}, new String[]{"+ findPendingApplications(): List<DriverApp>", "+ findByUsername(username): DriverApp", "+ findByPhone(phone): DriverApp"}, "pkg_repo", 2270, 580, 270, 120));

        list.add(new UmlClass("UserRepository", "UserRepository", "extends CsvRepository<User>",
                new String[]{}, new String[]{"+ findByUsername(username): User", "+ findByPhone(phone): User", "+ createEntity(): User"}, "pkg_repo", 2570, 580, 240, 110));

        list.add(new UmlClass("AccountRepository", "AccountRepository", "PBKDF2 Password Security",
                new String[]{"- filePath: Path", "- iterations: int = 120000"},
                new String[]{"+ register(role, id, pass): void", "+ authenticate(role, id, pass): boolean", "+ hasAccount(role, id): boolean"},
                "pkg_repo", 2840, 580, 260, 130));

        // ==========================================
        // 3. CONTROLLER LAYER (13 Classes/Tasks)
        // ==========================================
        list.add(new UmlClass("MainController", "MainController", "Central Facade Controller",
                new String[]{
                        "- authController: AuthController", "- customerController: CustomerController",
                        "- restaurantController: RestaurantController", "- driverController: DriverController",
                        "- orderController: OrderController", "- simulatorController: SimulatorController",
                        "- adminController: AdminController", "- mainView: MainView"
                },
                new String[]{
                        "+ run(): void", "+ login(username, password): boolean", "+ logout(): void",
                        "+ getCurrentUser(): User", "+ runBenchmark(mech, orders): SimulationRun",
                        "+ autoDispatchAllPending(mech): List<String>"
                },
                "pkg_ctrl", 60, 1460, 340, 230));

        list.add(new UmlClass("AppController", "AppController", "extends MainController",
                new String[]{}, new String[]{"+ AppController()"}, "pkg_ctrl", 430, 1460, 200, 70));

        list.add(new UmlClass("AuthController", "AuthController", "Authentication & Registration",
                new String[]{"- userRepo: UserRepository", "- customerRepo: CustomerRepository", "- driverRepo: DriverRepository", "- driverAppRepo: DriverAppRepository", "- currentUser: User"},
                new String[]{"+ login(username, password): boolean", "+ logout(): void", "+ registerCustomer(...): User", "+ submitDriverApplication(...): DriverApp", "+ registerDriver(...): User", "+ getAllUsers(): List<User>"},
                "pkg_ctrl", 430, 1550, 360, 190));

        list.add(new UmlClass("CustomerController", "CustomerController", "Customer Flow",
                new String[]{"- customerRepository: CustomerRepository", "- orderRepository: OrderRepository"},
                new String[]{"+ registerCustomer(name, phone, ward, lat, lon): Customer", "+ getAllCustomers(): List<Customer>", "+ getCustomerById(id): Customer", "+ updateCustomer(id, ...): boolean", "+ getOrderHistory(customerId): List<Order>"},
                "pkg_ctrl", 820, 1460, 330, 170));

        list.add(new UmlClass("DriverController", "DriverController", "Driver Flow",
                new String[]{"- driverRepository: DriverRepository"},
                new String[]{"+ getAllDrivers(): List<Driver>", "+ getAvailableDrivers(): List<Driver>", "+ getNearestAvailableDriver(lat, lon): Driver", "+ updateDriverStatus(id, status): boolean", "+ updateDriverInfo(id, ...): boolean"},
                "pkg_ctrl", 1180, 1460, 330, 170));

        list.add(new UmlClass("RestaurantController", "RestaurantController", "Restaurant Flow",
                new String[]{"- restaurantRepository: RestaurantRepository", "- menuItemRepository: MenuItemRepository"},
                new String[]{"+ getAllRestaurants(): List<Restaurant>", "+ getMenuByRestaurant(restId): List<MenuItem>", "+ updateStock(menuItemId, newStock): boolean", "+ createMenuItem(restId, name, price, stock): MenuItem"},
                "pkg_ctrl", 1540, 1460, 330, 170));

        list.add(new UmlClass("OrderController", "OrderController", "Order Lifecycle & Auto-Dispatch",
                new String[]{
                        "- orderRepository: OrderRepository", "- menuItemRepository: MenuItemRepository",
                        "- driverRepository: DriverRepository", "- restaurantRepository: RestaurantRepository",
                        "- orderItemRepository: OrderItemRepository", "- customerRepository: CustomerRepository",
                        "- routeRepository: DeliveryRouteRepository"
                },
                new String[]{
                        "+ placeOrderAndDispatch(custId, restId, itemId, qty, [mech]): Order",
                        "+ createCustomerOrder(custId, restId, itemId, qty, [mech]): Order",
                        "+ confirmOrderByRestaurant(orderId, restId, [mech]): Order",
                        "+ rejectOrderByRestaurant(orderId, restId, reason): boolean",
                        "+ cancelOrderByCustomer(orderId, custId): boolean",
                        "+ markOrderPickedUp(orderId, driverId): boolean",
                        "+ markOrderDelivered(orderId, driverId, [mech]): boolean",
                        "+ autoDispatchPendingOrders([mech]): int",
                        "+ getRouteRepository(): DeliveryRouteRepository"
                },
                "pkg_ctrl", 60, 1770, 480, 250));

        list.add(new UmlClass("AdminController", "AdminController", "System Administration",
                new String[]{"- restaurantRepo: RestaurantRepository", "- menuItemRepo: MenuItemRepository", "- customerRepo: CustomerRepository", "- driverRepo: DriverRepository", "- driverAppRepo: DriverAppRepository", "- userRepo: UserRepository"},
                new String[]{"+ getPendingDriverApplications(): List<DriverApp>", "+ approveDriverApplication(id): User", "+ rejectDriverApplication(id, reason): boolean", "+ addRestaurant(name, cat, addr, lat, lon): Restaurant", "+ restockMenuItem(itemId, qty): void", "+ triggerAutoDispatchAllPending(mech): int", "+ getCustomerRepo(): CustomerRepository"},
                "pkg_ctrl", 570, 1770, 410, 200));

        list.add(new UmlClass("SimulatorController", "SimulatorController", "Concurrency Benchmark Simulator",
                new String[]{"- orderRepo: OrderRepository", "- driverRepo: DriverRepository", "- menuItemRepo: MenuItemRepository", "- simRunRepo: SimulationRunRepository"},
                new String[]{"+ runSimulation(numOrders, mechanism): SimulationRun", "+ runExperiment(mechanism, numOrders, baseline): SimulationRun", "+ detectDoubleAssignments(): int", "+ detectOversellItems(): int", "+ detectDriverOverload(): int"},
                "pkg_ctrl", 1010, 1770, 380, 180));

        list.add(new UmlClass("CustomerThread", "CustomerThread", "extends java.lang.Thread",
                new String[]{"- startLatch, finishLatch: CountDownLatch", "- orderController: OrderController", "- customer: Customer, restaurant: Restaurant, menuItem: MenuItem"},
                new String[]{"+ run(): void (Executes concurrent order placement)"}, "pkg_ctrl", 1420, 1770, 380, 110));

        list.add(new UmlClass("DispatchThread", "DispatchThread", "extends java.lang.Thread",
                new String[]{"- startLatch, finishLatch: CountDownLatch", "- orderController: OrderController", "- orderId: String", "- mechanism: LockMechanism"},
                new String[]{"+ run(): void (Executes concurrent dispatching)"}, "pkg_ctrl", 1420, 1900, 380, 110));

        list.add(new UmlClass("CustomerSimTask", "CustomerSimTask", "implements java.lang.Runnable",
                new String[]{"- startGate, doneGate: CountDownLatch", "- menuItemRepo: MenuItemRepository", "- targetItemId: String", "- mechanism: LockMechanism", "- simulatedOversell: AtomicInteger"},
                new String[]{"+ CustomerSimTask(...)", "+ run(): void (Executes concurrent stock deduction)"},
                "pkg_ctrl", 1420, 2030, 380, 120));

        list.add(new UmlClass("DispatchSimTask", "DispatchSimTask", "implements java.lang.Runnable",
                new String[]{"- startGate, doneGate: CountDownLatch", "- driverRepo: DriverRepository", "- orderRepo: OrderRepository", "- targetOrderId, targetDriverId: String", "- mechanism: LockMechanism", "- simulatedDoubleAssign, simulatedDriverOverload: AtomicInteger"},
                new String[]{"+ DispatchSimTask(...)", "+ run(): void (Executes concurrent driver dispatch)"},
                "pkg_ctrl", 1420, 2170, 380, 130));

        // ==========================================
        // 4. EXCEPTION LAYER (8 Classes)
        // ==========================================
        list.add(new UmlClass("FoodDeliveryException", "FoodDeliveryException", "extends java.lang.Exception",
                new String[]{}, new String[]{"+ FoodDeliveryException(msg)", "+ FoodDeliveryException(msg, cause)"}, "pkg_ex", 2260, 1460, 260, 80));

        list.add(new UmlClass("ConcurrencyException", "ConcurrencyException", "extends FoodDeliveryException",
                new String[]{}, new String[]{"+ ConcurrencyException(msg)", "+ ConcurrencyException(msg, cause)"}, "pkg_ex", 2260, 1570, 260, 80));

        list.add(new UmlClass("DoubleAssignmentException", "DoubleAssignmentException", "extends ConcurrencyException",
                new String[]{"- orderId: String", "- existingDriverId, attemptedDriverId: String"},
                new String[]{"+ getOrderId(): String", "+ getExistingDriverId(): String", "+ getAttemptedDriverId(): String"}, "pkg_ex", 2050, 1680, 260, 120));

        list.add(new UmlClass("DriverOverloadException", "DriverOverloadException", "extends ConcurrencyException",
                new String[]{"- driverId: String", "- currentOrderId: String"},
                new String[]{"+ getDriverId(): String", "+ getCurrentOrderId(): String"}, "pkg_ex", 2340, 1680, 260, 120));

        list.add(new UmlClass("OptimisticLockException", "OptimisticLockException", "extends ConcurrencyException",
                new String[]{"- entityId: String", "- expectedVersion, currentVersion: long"},
                new String[]{"+ getEntityId(): String", "+ getExpectedVersion(): long", "+ getCurrentVersion(): long"}, "pkg_ex", 2050, 1820, 260, 120));

        list.add(new UmlClass("OversellException", "OversellException", "extends ConcurrencyException",
                new String[]{"- menuItemId: String", "- requestedQty, availableQty: int"},
                new String[]{"+ getMenuItemId(): String", "+ getRequestedQty(): int", "+ getAvailableQty(): int"}, "pkg_ex", 2340, 1820, 260, 120));

        list.add(new UmlClass("EntityNotFoundException", "EntityNotFoundException", "extends FoodDeliveryException",
                new String[]{"- entityName, entityId: String"},
                new String[]{"+ getEntityName(): String", "+ getEntityId(): String"}, "pkg_ex", 2050, 1960, 260, 100));

        list.add(new UmlClass("InvalidInputException", "InvalidInputException", "extends FoodDeliveryException",
                new String[]{"- fieldName: String"},
                new String[]{"+ getFieldName(): String"}, "pkg_ex", 2340, 1960, 260, 100));

        // ==========================================
        // 5. VIEW LAYER & ENTRYPOINT (7 Classes/Interfaces)
        // ==========================================
        list.add(new UmlClass("Main", "Main", "Application Entrypoint",
                new String[]{}, new String[]{"+ main(args: String[]): void (static)"}, "pkg_view", 2800, 1460, 220, 80));

        list.add(new UmlClass("IView", "IView", "<<interface>>",
                new String[]{}, new String[]{"+ display(): void"}, "pkg_view", 3050, 1460, 200, 80));

        list.add(new UmlClass("MainView", "MainView", "implements IView",
                new String[]{
                        "- mainController: MainController", "- customerController, restaurantController, driverController, orderController, simulatorController, authController, adminController",
                        "- mapView: MapView, simulatorView: SimulatorView, orderView: OrderView, reportView: ReportView"
                },
                new String[]{
                        "+ display(): void", "- displayLoginScreen(): boolean",
                        "- handleCustomerMenu() / handleDriverMenu()",
                        "- handleRestaurantMenu() / handleAdminMenu()",
                        "- checkAndDisplayCustomerNotifications(custId): void",
                        "- handleCustomerPlaceOrderFlow(custId): void"
                },
                "pkg_view", 2800, 1560, 450, 240));

        list.add(new UmlClass("MapView", "MapView", "ASCII Grid Map Visualizer",
                new String[]{"- rows, cols: int", "- minLat, maxLat, minLon, maxLon: double"},
                new String[]{"+ renderAsciiMap(restaurants, customers, drivers, orders): void"}, "pkg_view", 2800, 1830, 250, 100));

        list.add(new UmlClass("OrderView", "OrderView", "Orders & Menus Output",
                new String[]{}, new String[]{"+ renderOrders(orders: List<Order>): void", "+ displayMenu(restaurant, items): void"}, "pkg_view", 3080, 1830, 250, 100));

        list.add(new UmlClass("ReportView", "ReportView", "Benchmark Summary",
                new String[]{}, new String[]{"+ printResearchConclusion(runs): void", "+ renderSimulationSummary(run): void"}, "pkg_view", 2800, 1950, 250, 100));

        list.add(new UmlClass("SimulatorView", "SimulatorView", "Benchmark Comparison Table",
                new String[]{}, new String[]{"+ printBenchmarkTable(runs): void", "+ renderComparisonTable(runs): void"}, "pkg_view", 3080, 1950, 250, 100));

        return list;
    }

    private static List<UmlEdge> buildEdges() {
        List<UmlEdge> edges = new ArrayList<>();

        // 1. Model Layer: Inheritance to BaseEntity
        edges.add(new UmlEdge("e_cust_base", "Customer", "BaseEntity", "inherit", ""));
        edges.add(new UmlEdge("e_rest_base", "Restaurant", "BaseEntity", "inherit", ""));
        edges.add(new UmlEdge("e_item_base", "MenuItem", "BaseEntity", "inherit", ""));
        edges.add(new UmlEdge("e_drv_base", "Driver", "BaseEntity", "inherit", ""));
        edges.add(new UmlEdge("e_ord_base", "Order", "BaseEntity", "inherit", ""));
        edges.add(new UmlEdge("e_orditem_base", "OrderItem", "BaseEntity", "inherit", ""));
        edges.add(new UmlEdge("e_route_base", "DeliveryRoute", "BaseEntity", "inherit", ""));
        edges.add(new UmlEdge("e_sim_base", "SimulationRun", "BaseEntity", "inherit", ""));
        edges.add(new UmlEdge("e_user_base", "User", "BaseEntity", "inherit", ""));
        edges.add(new UmlEdge("e_drvapp_base", "DriverApplication", "BaseEntity", "inherit", ""));
        edges.add(new UmlEdge("e_acc_user", "UserAccount", "User", "inherit", ""));

        // 2. Model Associations (Foreign Keys & Enums)
        edges.add(new UmlEdge("e_ord_cust", "Order", "Customer", "assoc", "places"));
        edges.add(new UmlEdge("e_ord_rest", "Order", "Restaurant", "assoc", "from"));
        edges.add(new UmlEdge("e_ord_drv", "Order", "Driver", "assoc", "delivered by"));
        edges.add(new UmlEdge("e_ord_st", "Order", "OrderStatus", "assoc", "status"));
        edges.add(new UmlEdge("e_orditem_ord", "OrderItem", "Order", "assoc", "belongs to"));
        edges.add(new UmlEdge("e_orditem_item", "OrderItem", "MenuItem", "assoc", "item"));
        edges.add(new UmlEdge("e_item_rest", "MenuItem", "Restaurant", "assoc", "offers"));
        edges.add(new UmlEdge("e_drv_st", "Driver", "DriverStatus", "assoc", "status"));
        edges.add(new UmlEdge("e_user_role", "User", "UserRole", "assoc", "role"));
        edges.add(new UmlEdge("e_session_role", "AuthSession", "AccountRole", "assoc", "role"));
        edges.add(new UmlEdge("e_sim_lock", "SimulationRun", "LockMechanism", "assoc", "mechanism"));
        edges.add(new UmlEdge("e_route_ord", "DeliveryRoute", "Order", "assoc", "routes"));
        edges.add(new UmlEdge("e_route_drv", "DeliveryRoute", "Driver", "assoc", "assigned driver"));

        // 3. Repository Layer: Realization & Inheritance
        edges.add(new UmlEdge("e_csv_irepo", "CsvRepository", "IRepository", "realize", ""));
        edges.add(new UmlEdge("e_custrepo_csv", "CustomerRepository", "CsvRepository", "inherit", ""));
        edges.add(new UmlEdge("e_restrepo_csv", "RestaurantRepository", "CsvRepository", "inherit", ""));
        edges.add(new UmlEdge("e_itemrepo_csv", "MenuItemRepository", "CsvRepository", "inherit", ""));
        edges.add(new UmlEdge("e_drvrepo_csv", "DriverRepository", "CsvRepository", "inherit", ""));
        edges.add(new UmlEdge("e_ordrepo_csv", "OrderRepository", "CsvRepository", "inherit", ""));
        edges.add(new UmlEdge("e_orditemrepo_csv", "OrderItemRepository", "CsvRepository", "inherit", ""));
        edges.add(new UmlEdge("e_routerepo_csv", "DeliveryRouteRepository", "CsvRepository", "inherit", ""));
        edges.add(new UmlEdge("e_simrepo_csv", "SimulationRunRepository", "CsvRepository", "inherit", ""));
        edges.add(new UmlEdge("e_drvapprepo_csv", "DriverApplicationRepository", "CsvRepository", "inherit", ""));
        edges.add(new UmlEdge("e_userrepo_csv", "UserRepository", "CsvRepository", "inherit", ""));

        // Repositories using Models & Utilities
        edges.add(new UmlEdge("e_drvrepo_geo", "DriverRepository", "GeoUtils", "depend", "calculates distance"));
        edges.add(new UmlEdge("e_accrepo_role", "AccountRepository", "AccountRole", "depend", "authenticates"));

        // 4. Controller Layer: Aggregations & Composition
        edges.add(new UmlEdge("e_app_mc", "AppController", "MainController", "inherit", ""));
        edges.add(new UmlEdge("e_mc_auth", "MainController", "AuthController", "aggr", ""));
        edges.add(new UmlEdge("e_mc_cust", "MainController", "CustomerController", "aggr", ""));
        edges.add(new UmlEdge("e_mc_drv", "MainController", "DriverController", "aggr", ""));
        edges.add(new UmlEdge("e_mc_rest", "MainController", "RestaurantController", "aggr", ""));
        edges.add(new UmlEdge("e_mc_ord", "MainController", "OrderController", "aggr", ""));
        edges.add(new UmlEdge("e_mc_adm", "MainController", "AdminController", "aggr", ""));
        edges.add(new UmlEdge("e_mc_sim", "MainController", "SimulatorController", "aggr", ""));
        edges.add(new UmlEdge("e_mc_view", "MainController", "MainView", "aggr", ""));
        edges.add(new UmlEdge("e_mc_map", "MainController", "MapView", "aggr", ""));
        edges.add(new UmlEdge("e_mc_oview", "MainController", "OrderView", "aggr", ""));
        edges.add(new UmlEdge("e_mc_rview", "MainController", "ReportView", "aggr", ""));
        edges.add(new UmlEdge("e_mc_sview", "MainController", "SimulatorView", "aggr", ""));

        // Controllers to Repositories
        edges.add(new UmlEdge("e_auth_userrepo", "AuthController", "UserRepository", "aggr", ""));
        edges.add(new UmlEdge("e_auth_custrepo", "AuthController", "CustomerRepository", "aggr", ""));
        edges.add(new UmlEdge("e_auth_drvrepo", "AuthController", "DriverRepository", "aggr", ""));
        edges.add(new UmlEdge("e_auth_apprepo", "AuthController", "DriverApplicationRepository", "aggr", ""));

        edges.add(new UmlEdge("e_custctrl_custrepo", "CustomerController", "CustomerRepository", "aggr", ""));
        edges.add(new UmlEdge("e_custctrl_ordrepo", "CustomerController", "OrderRepository", "aggr", ""));

        edges.add(new UmlEdge("e_drvctrl_drvrepo", "DriverController", "DriverRepository", "aggr", ""));

        edges.add(new UmlEdge("e_restctrl_restrepo", "RestaurantController", "RestaurantRepository", "aggr", ""));
        edges.add(new UmlEdge("e_restctrl_itemrepo", "RestaurantController", "MenuItemRepository", "aggr", ""));

        edges.add(new UmlEdge("e_ordctrl_ordrepo", "OrderController", "OrderRepository", "aggr", ""));
        edges.add(new UmlEdge("e_ordctrl_itemrepo", "OrderController", "MenuItemRepository", "aggr", ""));
        edges.add(new UmlEdge("e_ordctrl_drvrepo", "OrderController", "DriverRepository", "aggr", ""));
        edges.add(new UmlEdge("e_ordctrl_restrepo", "OrderController", "RestaurantRepository", "aggr", ""));
        edges.add(new UmlEdge("e_ordctrl_itemrepo_oi", "OrderController", "OrderItemRepository", "aggr", ""));
        edges.add(new UmlEdge("e_ordctrl_custrepo", "OrderController", "CustomerRepository", "aggr", ""));
        edges.add(new UmlEdge("e_ordctrl_routerepo", "OrderController", "DeliveryRouteRepository", "aggr", ""));
        edges.add(new UmlEdge("e_ordctrl_geo", "OrderController", "GeoUtils", "depend", "calculates fee & distance"));

        edges.add(new UmlEdge("e_admctrl_restrepo", "AdminController", "RestaurantRepository", "aggr", ""));
        edges.add(new UmlEdge("e_admctrl_itemrepo", "AdminController", "MenuItemRepository", "aggr", ""));
        edges.add(new UmlEdge("e_admctrl_custrepo", "AdminController", "CustomerRepository", "aggr", ""));
        edges.add(new UmlEdge("e_admctrl_drvrepo", "AdminController", "DriverRepository", "aggr", ""));
        edges.add(new UmlEdge("e_admctrl_apprepo", "AdminController", "DriverApplicationRepository", "aggr", ""));
        edges.add(new UmlEdge("e_admctrl_userrepo", "AdminController", "UserRepository", "aggr", ""));
        edges.add(new UmlEdge("e_admctrl_ordctrl", "AdminController", "OrderController", "aggr", ""));

        edges.add(new UmlEdge("e_simctrl_simrepo", "SimulatorController", "SimulationRunRepository", "aggr", ""));
        edges.add(new UmlEdge("e_simctrl_ordrepo", "SimulatorController", "OrderRepository", "aggr", ""));
        edges.add(new UmlEdge("e_simctrl_drvrepo", "SimulatorController", "DriverRepository", "aggr", ""));
        edges.add(new UmlEdge("e_simctrl_itemrepo", "SimulatorController", "MenuItemRepository", "aggr", ""));
        edges.add(new UmlEdge("e_simctrl_custtask", "SimulatorController", "CustomerSimTask", "aggr", "creates task"));
        edges.add(new UmlEdge("e_simctrl_disptask", "SimulatorController", "DispatchSimTask", "aggr", "creates task"));

        edges.add(new UmlEdge("e_custsim_itemrepo", "CustomerSimTask", "MenuItemRepository", "aggr", ""));
        edges.add(new UmlEdge("e_custsim_lock", "CustomerSimTask", "LockMechanism", "assoc", ""));

        edges.add(new UmlEdge("e_dispsim_drvrepo", "DispatchSimTask", "DriverRepository", "aggr", ""));
        edges.add(new UmlEdge("e_dispsim_ordrepo", "DispatchSimTask", "OrderRepository", "aggr", ""));
        edges.add(new UmlEdge("e_dispsim_lock", "DispatchSimTask", "LockMechanism", "assoc", ""));

        edges.add(new UmlEdge("e_custthread_ordctrl", "CustomerThread", "OrderController", "depend", "calls"));
        edges.add(new UmlEdge("e_dispthread_ordctrl", "DispatchThread", "OrderController", "depend", "calls"));
        edges.add(new UmlEdge("e_dispthread_lock", "DispatchThread", "LockMechanism", "depend", "uses mechanism"));

        // 5. Exception Layer: Inheritance
        edges.add(new UmlEdge("e_conc_food", "ConcurrencyException", "FoodDeliveryException", "inherit", ""));
        edges.add(new UmlEdge("e_notfound_food", "EntityNotFoundException", "FoodDeliveryException", "inherit", ""));
        edges.add(new UmlEdge("e_invalid_food", "InvalidInputException", "FoodDeliveryException", "inherit", ""));

        edges.add(new UmlEdge("e_da_conc", "DoubleAssignmentException", "ConcurrencyException", "inherit", ""));
        edges.add(new UmlEdge("e_do_conc", "DriverOverloadException", "ConcurrencyException", "inherit", ""));
        edges.add(new UmlEdge("e_opt_conc", "OptimisticLockException", "ConcurrencyException", "inherit", ""));
        edges.add(new UmlEdge("e_ov_conc", "OversellException", "ConcurrencyException", "inherit", ""));

        // Repositories & Controllers throwing Exceptions
        edges.add(new UmlEdge("e_itemrepo_ov", "MenuItemRepository", "OversellException", "depend", "throws"));
        edges.add(new UmlEdge("e_ordrepo_da", "OrderRepository", "DoubleAssignmentException", "depend", "throws"));
        edges.add(new UmlEdge("e_drvrepo_do", "DriverRepository", "DriverOverloadException", "depend", "throws"));
        edges.add(new UmlEdge("e_ordrepo_opt", "OrderRepository", "OptimisticLockException", "depend", "throws"));
        edges.add(new UmlEdge("e_ordctrl_inv", "OrderController", "InvalidInputException", "depend", "throws"));
        edges.add(new UmlEdge("e_ordctrl_enf", "OrderController", "EntityNotFoundException", "depend", "throws"));

        // 6. View Layer & Entrypoint
        edges.add(new UmlEdge("e_main_mc", "Main", "MainController", "depend", "instantiates"));
        edges.add(new UmlEdge("e_mv_iview", "MainView", "IView", "realize", ""));
        edges.add(new UmlEdge("e_mv_mc", "MainView", "MainController", "aggr", ""));
        edges.add(new UmlEdge("e_mv_map", "MainView", "MapView", "aggr", ""));
        edges.add(new UmlEdge("e_mv_order", "MainView", "OrderView", "aggr", ""));
        edges.add(new UmlEdge("e_mv_report", "MainView", "ReportView", "aggr", ""));
        edges.add(new UmlEdge("e_mv_sim", "MainView", "SimulatorView", "aggr", ""));

        edges.add(new UmlEdge("e_map_rest", "MapView", "Restaurant", "depend", "displays [R]"));
        edges.add(new UmlEdge("e_map_cust", "MapView", "Customer", "depend", "displays [C]"));
        edges.add(new UmlEdge("e_map_drv", "MapView", "Driver", "depend", "displays [D]/[B]"));
        edges.add(new UmlEdge("e_map_ord", "MapView", "Order", "depend", "audits active"));

        return edges;
    }

    private static UmlClass findClass(List<UmlClass> list, String id) {
        for (UmlClass c : list) {
            if (c.id.equals(id)) return c;
        }
        return null;
    }

    private static void generateSvgAndHtml(List<UmlClass> classes, List<UmlEdge> edges) throws Exception {
        int width = 3600;
        int height = 2500;
        String svgContent = renderSvgContent(classes, edges, width, height, true);

        // Save SVG
        writeFile(new File("docs/Full_Class_Diagram.svg"), svgContent);
        writeFile(new File("../Lab211-Group1/ClassDiagram/Full_Class_Diagram.svg"), svgContent);

        // Save Full-Featured Interactive HTML Viewer with Native SVG ViewBox Pan-Zoom
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html lang=\"vi\">\n<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("  <title>Full 60-Class UML Class Diagram - Food Delivery LAB211</title>\n");
        html.append("  <style>\n");
        html.append("    * { box-sizing: border-box; margin: 0; padding: 0; }\n");
        html.append("    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #0f172a; color: #f8fafc; height: 100vh; display: flex; flex-direction: column; overflow: hidden; }\n");
        html.append("    .navbar { height: 60px; background: #1e293b; border-bottom: 1px solid #334155; display: flex; align-items: center; justify-content: space-between; padding: 0 16px; gap: 12px; z-index: 1000; box-shadow: 0 4px 12px rgba(0,0,0,0.3); flex-shrink: 0; }\n");
        html.append("    .title-group { display: flex; align-items: center; gap: 10px; }\n");
        html.append("    .title-group h1 { font-size: 16px; font-weight: 700; color: #f8fafc; letter-spacing: 0.5px; }\n");
        html.append("    .badge { background: #3b82f6; color: #fff; font-size: 11px; font-weight: 700; padding: 3px 8px; border-radius: 9999px; text-transform: uppercase; }\n");
        html.append("    .controls { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }\n");
        html.append("    .btn { background: #334155; color: #f8fafc; border: 1px solid #475569; padding: 6px 12px; border-radius: 6px; font-size: 12.5px; font-weight: 600; cursor: pointer; transition: all 0.15s ease; display: inline-flex; align-items: center; gap: 4px; }\n");
        html.append("    .btn:hover { background: #475569; border-color: #64748b; }\n");
        html.append("    .btn.active { background: #2563eb; border-color: #3b82f6; color: #ffffff; }\n");
        html.append("    .btn-action { background: #059669; border-color: #10b981; }\n");
        html.append("    .btn-action:hover { background: #10b981; }\n");
        html.append("    .search-box { position: relative; display: flex; align-items: center; }\n");
        html.append("    .search-input { background: #0f172a; border: 1px solid #475569; color: #f8fafc; padding: 6px 12px; border-radius: 6px; font-size: 12.5px; width: 180px; outline: none; transition: 0.2s; }\n");
        html.append("    .search-input:focus { border-color: #3b82f6; width: 220px; box-shadow: 0 0 0 2px rgba(59,130,246,0.3); }\n");
        html.append("    .viewer-container { flex: 1; width: 100%; height: calc(100vh - 60px); position: relative; background: #ffffff; overflow: hidden; cursor: grab; user-select: none; }\n");
        html.append("    .viewer-container:active { cursor: grabbing; }\n");
        html.append("    svg#main-svg { width: 100%; height: 100%; display: block; }\n");
        html.append("    .highlighted rect.class-box { fill: #fef08a !important; stroke: #ca8a04 !important; stroke-width: 3px !important; }\n");
        html.append("    .hud-tip { position: absolute; bottom: 12px; left: 16px; background: rgba(15, 23, 42, 0.85); backdrop-filter: blur(4px); padding: 6px 12px; border-radius: 6px; font-size: 11.5px; color: #94a3b8; border: 1px solid #334155; pointer-events: none; }\n");
        html.append("    .hud-scale { position: absolute; bottom: 12px; right: 16px; background: rgba(15, 23, 42, 0.85); backdrop-filter: blur(4px); padding: 6px 12px; border-radius: 6px; font-size: 11.5px; font-weight: 600; color: #38bdf8; border: 1px solid #334155; pointer-events: none; }\n");
        html.append("  </style>\n");
        html.append("</head>\n<body>\n");
        html.append("  <div class=\"navbar\">\n");
        html.append("    <div class=\"title-group\">\n");
        html.append("      <h1>🏛️ FOOD DELIVERY — UML CLASS DIAGRAM</h1>\n");
        html.append("      <span class=\"badge\">60 CLASSES &bull; 100% CODE</span>\n");
        html.append("    </div>\n");
        html.append("    <div class=\"controls\">\n");
        html.append("      <button class=\"btn active\" id=\"btn-all\" onclick=\"jumpTo('all')\">🌐 Toàn Bộ (60)</button>\n");
        html.append("      <button class=\"btn\" id=\"btn-model\" onclick=\"jumpTo('model')\">1. Model (19)</button>\n");
        html.append("      <button class=\"btn\" id=\"btn-repo\" onclick=\"jumpTo('repo')\">2. Repository (13)</button>\n");
        html.append("      <button class=\"btn\" id=\"btn-ctrl\" onclick=\"jumpTo('ctrl')\">3. Controller (13)</button>\n");
        html.append("      <button class=\"btn\" id=\"btn-ex\" onclick=\"jumpTo('ex')\">4. Exception (8)</button>\n");
        html.append("      <button class=\"btn\" id=\"btn-view\" onclick=\"jumpTo('view')\">5. View (7)</button>\n");
        html.append("      <div class=\"search-box\">\n");
        html.append("        <input type=\"text\" id=\"searchInput\" class=\"search-input\" placeholder=\"🔎 Tìm Class...\" oninput=\"searchClass(this.value)\">\n");
        html.append("      </div>\n");
        html.append("      <button class=\"btn\" onclick=\"zoomDelta(0.8)\">➕</button>\n");
        html.append("      <button class=\"btn\" onclick=\"zoomDelta(1.25)\">➖</button>\n");
        html.append("      <button class=\"btn\" onclick=\"fitScreen()\">🎯 Fit</button>\n");
        html.append("      <button class=\"btn btn-action\" onclick=\"window.print()\">🖨️ In / PDF</button>\n");
        html.append("    </div>\n");
        html.append("  </div>\n");
        html.append("  <div class=\"viewer-container\" id=\"container\">\n");
        html.append(svgContent);
        html.append("    <div class=\"hud-tip\">💡 Kéo chuột để di chuyển (Pan) &bull; Lăn chuột để phóng to/thu nhỏ tại con trỏ &bull; Nhấp đúp để Reset</div>\n");
        html.append("    <div class=\"hud-scale\" id=\"scaleIndicator\">Zoom: 100%</div>\n");
        html.append("  </div>\n");
        html.append("  <script>\n");
        html.append("    const svg = document.getElementById('main-svg');\n");
        html.append("    const container = document.getElementById('container');\n");
        html.append("    const scaleIndicator = document.getElementById('scaleIndicator');\n");
        html.append("    let vb = { x: 0, y: 0, w: 3600, h: 2500 };\n");
        html.append("    const defaultVb = { x: 0, y: 0, w: 3600, h: 2500 };\n");
        html.append("    const layers = {\n");
        html.append("      all: { x: 0, y: 0, w: 3600, h: 2500 },\n");
        html.append("      model: { x: 20, y: 80, w: 1400, h: 1270 },\n");
        html.append("      repo: { x: 1440, y: 80, w: 2130, h: 1270 },\n");
        html.append("      ctrl: { x: 20, y: 1360, w: 1970, h: 1110 },\n");
        html.append("      ex: { x: 2000, y: 1360, w: 770, h: 1110 },\n");
        html.append("      view: { x: 2780, y: 1360, w: 790, h: 1110 }\n");
        html.append("    };\n");
        html.append("    function updateViewBox() {\n");
        html.append("      svg.setAttribute('viewBox', `${vb.x} ${vb.y} ${vb.w} ${vb.h}`);\n");
        html.append("      const zoom = Math.round((3600 / vb.w) * 100);\n");
        html.append("      scaleIndicator.innerText = `Zoom: ${zoom}%`;\n");
        html.append("    }\n");
        html.append("    function fitScreen() { jumpTo('all'); }\n");
        html.append("    function jumpTo(layerKey) {\n");
        html.append("      const target = layers[layerKey];\n");
        html.append("      if (!target) return;\n");
        html.append("      document.querySelectorAll('.controls .btn').forEach(b => b.classList.remove('active'));\n");
        html.append("      const btn = document.getElementById('btn-' + layerKey);\n");
        html.append("      if (btn) btn.classList.add('active');\n");
        html.append("      animateViewBox(target);\n");
        html.append("    }\n");
        html.append("    function animateViewBox(tgt) {\n");
        html.append("      const start = { ...vb };\n");
        html.append("      const startTime = performance.now();\n");
        html.append("      const duration = 250;\n");
        html.append("      function step(now) {\n");
        html.append("        const p = Math.min((now - startTime) / duration, 1);\n");
        html.append("        const ease = 0.5 - Math.cos(p * Math.PI) / 2;\n");
        html.append("        vb.x = start.x + (tgt.x - start.x) * ease;\n");
        html.append("        vb.y = start.y + (tgt.y - start.y) * ease;\n");
        html.append("        vb.w = start.w + (tgt.w - start.w) * ease;\n");
        html.append("        vb.h = start.h + (tgt.h - start.h) * ease;\n");
        html.append("        updateViewBox();\n");
        html.append("        if (p < 1) requestAnimationFrame(step);\n");
        html.append("      }\n");
        html.append("      requestAnimationFrame(step);\n");
        html.append("    }\n");
        html.append("    function zoomDelta(factor) {\n");
        html.append("      const cx = vb.x + vb.w / 2;\n");
        html.append("      const cy = vb.y + vb.h / 2;\n");
        html.append("      vb.w *= factor;\n");
        html.append("      vb.h *= factor;\n");
        html.append("      vb.x = cx - vb.w / 2;\n");
        html.append("      vb.y = cy - vb.h / 2;\n");
        html.append("      updateViewBox();\n");
        html.append("    }\n");
        html.append("    let isDragging = false, startX = 0, startY = 0;\n");
        html.append("    container.addEventListener('mousedown', (e) => {\n");
        html.append("      if (e.button !== 0) return;\n");
        html.append("      isDragging = true;\n");
        html.append("      startX = e.clientX;\n");
        html.append("      startY = e.clientY;\n");
        html.append("    });\n");
        html.append("    window.addEventListener('mousemove', (e) => {\n");
        html.append("      if (!isDragging) return;\n");
        html.append("      const rect = container.getBoundingClientRect();\n");
        html.append("      const dx = (e.clientX - startX) * (vb.w / rect.width);\n");
        html.append("      const dy = (e.clientY - startY) * (vb.h / rect.height);\n");
        html.append("      vb.x -= dx;\n");
        html.append("      vb.y -= dy;\n");
        html.append("      startX = e.clientX;\n");
        html.append("      startY = e.clientY;\n");
        html.append("      updateViewBox();\n");
        html.append("    });\n");
        html.append("    window.addEventListener('mouseup', () => { isDragging = false; });\n");
        html.append("    container.addEventListener('wheel', (e) => {\n");
        html.append("      e.preventDefault();\n");
        html.append("      const factor = e.deltaY < 0 ? 0.85 : 1.15;\n");
        html.append("      const rect = container.getBoundingClientRect();\n");
        html.append("      const mouseX = e.clientX - rect.left;\n");
        html.append("      const mouseY = e.clientY - rect.top;\n");
        html.append("      const svgX = vb.x + (mouseX / rect.width) * vb.w;\n");
        html.append("      const svgY = vb.y + (mouseY / rect.height) * vb.h;\n");
        html.append("      vb.w *= factor;\n");
        html.append("      vb.h *= factor;\n");
        html.append("      vb.x = svgX - (mouseX / rect.width) * vb.w;\n");
        html.append("      vb.y = svgY - (mouseY / rect.height) * vb.h;\n");
        html.append("      updateViewBox();\n");
        html.append("    }, { passive: false });\n");
        html.append("    container.addEventListener('dblclick', () => { jumpTo('all'); });\n");
        html.append("    function searchClass(query) {\n");
        html.append("      const q = query.trim().toLowerCase();\n");
        html.append("      document.querySelectorAll('.class-node').forEach(node => {\n");
        html.append("        node.classList.remove('highlighted');\n");
        html.append("      });\n");
        html.append("      if (!q) return;\n");
        html.append("      let firstMatch = null;\n");
        html.append("      document.querySelectorAll('.class-node').forEach(node => {\n");
        html.append("        const id = node.getAttribute('data-id').toLowerCase();\n");
        html.append("        if (id.includes(q)) {\n");
        html.append("          node.classList.add('highlighted');\n");
        html.append("          if (!firstMatch) firstMatch = node;\n");
        html.append("        }\n");
        html.append("      });\n");
        html.append("      if (firstMatch) {\n");
        html.append("        const cx = parseFloat(firstMatch.getAttribute('data-cx'));\n");
        html.append("        const cy = parseFloat(firstMatch.getAttribute('data-cy'));\n");
        html.append("        const w = 700, h = 500;\n");
        html.append("        animateViewBox({ x: cx - w/2, y: cy - h/2, w: w, h: h });\n");
        html.append("      }\n");
        html.append("    }\n");
        html.append("    updateViewBox();\n");
        html.append("  </script>\n");
        html.append("</body>\n</html>\n");

        writeFile(new File("docs/class_diagram_view.html"), html.toString());
        writeFile(new File("../Lab211-Group1/ClassDiagram/class_diagram_view.html"), html.toString());
    }

    private static String renderSvgContent(List<UmlClass> classes, List<UmlEdge> edges, int width, int height, boolean full) {
        StringBuilder svg = new StringBuilder();
        svg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        svg.append("<svg id=\"main-svg\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 ").append(width).append(" ").append(height).append("\" width=\"100%\" height=\"100%\">\n");
        svg.append("  <defs>\n");
        svg.append("    <style>\n");
        svg.append("      .canvas-bg { fill: #ffffff; }\n");
        svg.append("      .pkg-box { fill: #ffffff; stroke: #000000; stroke-width: 1.8; stroke-dasharray: 6,4; rx: 8; }\n");
        svg.append("      .pkg-title { font-family: 'Segoe UI', Arial, sans-serif; font-size: 18px; font-weight: bold; fill: #000000; }\n");
        svg.append("      .class-box { fill: #ffffff; stroke: #000000; stroke-width: 1.5; }\n");
        svg.append("      .head-title { fill: #000000; font-family: 'Segoe UI', Arial, sans-serif; font-size: 13px; font-weight: bold; text-anchor: middle; }\n");
        svg.append("      .head-sub { fill: #4b5563; font-family: 'Segoe UI', Arial, sans-serif; font-size: 10.5px; font-style: italic; text-anchor: middle; }\n");
        svg.append("      .txt-attr { fill: #111827; font-family: 'Consolas', 'Courier New', monospace; font-size: 10.5px; }\n");
        svg.append("      .txt-meth { fill: #111827; font-family: 'Consolas', 'Courier New', monospace; font-size: 10.5px; }\n");
        svg.append("      .sep { stroke: #000000; stroke-width: 1; }\n");
        svg.append("      .edge-inherit { fill: none; stroke: #000000; stroke-width: 1.6; }\n");
        svg.append("      .edge-realize { fill: none; stroke: #000000; stroke-width: 1.6; stroke-dasharray: 5,4; }\n");
        svg.append("      .edge-assoc { fill: none; stroke: #1f2937; stroke-width: 1.4; }\n");
        svg.append("      .edge-aggr { fill: none; stroke: #000000; stroke-width: 1.5; }\n");
        svg.append("      .edge-depend { fill: none; stroke: #374151; stroke-width: 1.4; stroke-dasharray: 4,4; }\n");
        svg.append("      .edge-label { fill: #111827; font-family: 'Segoe UI', Arial, sans-serif; font-size: 10px; font-style: italic; text-anchor: middle; }\n");
        svg.append("      .title-main { fill: #000000; font-family: 'Segoe UI', Arial, sans-serif; font-size: 26px; font-weight: bold; text-anchor: middle; }\n");
        svg.append("      .title-sub { fill: #4b5563; font-family: 'Segoe UI', Arial, sans-serif; font-size: 14px; text-anchor: middle; }\n");
        svg.append("    </style>\n");

        // Markers
        svg.append("    <marker id=\"uml-inherit\" viewBox=\"0 0 12 12\" refX=\"11\" refY=\"6\" markerWidth=\"10\" markerHeight=\"10\" orient=\"auto\">\n");
        svg.append("      <polygon points=\"1 1, 11 6, 1 11\" fill=\"#ffffff\" stroke=\"#000000\" stroke-width=\"1.5\"/>\n");
        svg.append("    </marker>\n");
        svg.append("    <marker id=\"uml-assoc\" viewBox=\"0 0 10 10\" refX=\"9\" refY=\"5\" markerWidth=\"7\" markerHeight=\"7\" orient=\"auto\">\n");
        svg.append("      <polyline points=\"1 1, 9 5, 1 9\" fill=\"none\" stroke=\"#000000\" stroke-width=\"1.5\"/>\n");
        svg.append("    </marker>\n");
        svg.append("    <marker id=\"uml-diamond\" viewBox=\"0 0 16 10\" refX=\"1\" refY=\"5\" markerWidth=\"12\" markerHeight=\"8\" orient=\"auto\">\n");
        svg.append("      <polygon points=\"1 5, 8 1, 15 5, 8 9\" fill=\"#ffffff\" stroke=\"#000000\" stroke-width=\"1.5\"/>\n");
        svg.append("    </marker>\n");
        svg.append("  </defs>\n");

        svg.append("  <rect width=\"").append(width).append("\" height=\"").append(height).append("\" class=\"canvas-bg\" />\n");

        if (full) {
            svg.append("  <text x=\"").append(width / 2).append("\" y=\"40\" class=\"title-main\">FOOD DELIVERY NETWORK SIMULATION — FULL UML CLASS DIAGRAM (60 CLASSES)</text>\n");
            svg.append("  <text x=\"").append(width / 2).append("\" y=\"65\" class=\"title-sub\">LAB211 OOP with Java | Full System Entities, Repositories, Controllers, Exceptions &amp; Views (Monochrome)</text>\n");

            // Package Frames
            svg.append("  <rect x=\"30\" y=\"90\" width=\"1380\" height=\"1250\" class=\"pkg-box\" />\n");
            svg.append("  <text x=\"50\" y=\"118\" class=\"pkg-title\">1. PACKAGE: model (Entities, Enums, Utils)</text>\n");

            svg.append("  <rect x=\"1450\" y=\"90\" width=\"2110\" height=\"1250\" class=\"pkg-box\" />\n");
            svg.append("  <text x=\"1470\" y=\"118\" class=\"pkg-title\">2. PACKAGE: repository (Generic CSV &amp; Physical File I/O)</text>\n");

            svg.append("  <rect x=\"30\" y=\"1370\" width=\"1950\" height=\"1090\" class=\"pkg-box\" />\n");
            svg.append("  <text x=\"50\" y=\"1398\" class=\"pkg-title\">3. PACKAGE: controller (MVC Flow &amp; Threads)</text>\n");

            svg.append("  <rect x=\"2010\" y=\"1370\" width=\"750\" height=\"1090\" class=\"pkg-box\" />\n");
            svg.append("  <text x=\"2030\" y=\"1398\" class=\"pkg-title\">4. PACKAGE: exception (Concurrency &amp; Validation)</text>\n");

            svg.append("  <rect x=\"2790\" y=\"1370\" width=\"770\" height=\"1090\" class=\"pkg-box\" />\n");
            svg.append("  <text x=\"2810\" y=\"1398\" class=\"pkg-title\">5. PACKAGE: view &amp; Application Entrypoint</text>\n");
        }

        // Render Classes
        for (UmlClass c : classes) {
            svg.append("  <g class=\"class-node\" id=\"node-").append(c.id).append("\" data-id=\"").append(c.id).append("\" data-cx=\"").append(c.cx()).append("\" data-cy=\"").append(c.cy()).append("\" transform=\"translate(").append(c.x).append(", ").append(c.y).append(")\">\n");
            svg.append("    <rect x=\"0\" y=\"0\" width=\"").append(c.w).append("\" height=\"").append(c.h).append("\" class=\"class-box\" />\n");
            svg.append("    <text x=\"").append(c.w / 2).append("\" y=\"20\" class=\"head-title\">").append(escapeXml(c.name)).append("</text>\n");
            if (c.sub != null && !c.sub.isEmpty()) {
                svg.append("    <text x=\"").append(c.w / 2).append("\" y=\"32\" class=\"head-sub\">").append(escapeXml(c.sub)).append("</text>\n");
            }
            svg.append("    <line x1=\"0\" y1=\"38\" x2=\"").append(c.w).append("\" y2=\"38\" class=\"sep\" />\n");

            int curY = 52;
            for (String a : c.attrs) {
                svg.append("    <text x=\"8\" y=\"").append(curY).append("\" class=\"txt-attr\">").append(escapeXml(a)).append("</text>\n");
                curY += 15;
            }

            if (c.meths != null && c.meths.length > 0) {
                svg.append("    <line x1=\"0\" y1=\"").append(curY).append("\" x2=\"").append(c.w).append("\" y2=\"").append(curY).append("\" class=\"sep\" />\n");
                curY += 15;
                for (String m : c.meths) {
                    svg.append("    <text x=\"8\" y=\"").append(curY).append("\" class=\"txt-meth\">").append(escapeXml(m)).append("</text>\n");
                    curY += 15;
                }
            }
            svg.append("  </g>\n");
        }

        // Render Edges
        for (UmlEdge e : edges) {
            UmlClass src = findClass(classes, e.sourceId);
            UmlClass tgt = findClass(classes, e.targetId);
            if (src == null || tgt == null) continue;

            int sx = src.cx();
            int sy = (src.cy() < tgt.cy()) ? src.bottom() : src.top();
            int tx = tgt.cx();
            int ty = (src.cy() < tgt.cy()) ? tgt.top() : tgt.bottom();

            String cls;
            String marker;
            if ("inherit".equals(e.type)) {
                cls = "edge-inherit";
                marker = "marker-end=\"url(#uml-inherit)\"";
            } else if ("realize".equals(e.type)) {
                cls = "edge-realize";
                marker = "marker-end=\"url(#uml-inherit)\"";
            } else if ("aggr".equals(e.type)) {
                cls = "edge-aggr";
                marker = "marker-start=\"url(#uml-diamond)\"";
            } else if ("depend".equals(e.type)) {
                cls = "edge-depend";
                marker = "marker-end=\"url(#uml-assoc)\"";
            } else {
                cls = "edge-assoc";
                marker = "marker-end=\"url(#uml-assoc)\"";
            }

            svg.append("  <path d=\"M ").append(sx).append(" ").append(sy).append(" L ").append(tx).append(" ").append(ty)
                    .append("\" class=\"").append(cls).append("\" ").append(marker).append(" />\n");

            if (e.label != null && !e.label.isEmpty()) {
                int mx = (sx + tx) / 2;
                int my = (sy + ty) / 2 - 4;
                svg.append("  <text x=\"").append(mx).append("\" y=\"").append(my).append("\" class=\"edge-label\">")
                        .append(escapeXml(e.label)).append("</text>\n");
            }
        }

        svg.append("</svg>\n");
        return svg.toString();
    }

    private static void generateMultiTabDrawio(List<UmlClass> classes, List<UmlEdge> edges) throws Exception {
        StringBuilder xml = new StringBuilder();
        xml.append("<mxfile host=\"app.diagrams.net\" modified=\"2026-11-11T08:00:00.000Z\" agent=\"Antigravity\" version=\"22.0.0\" type=\"device\">\n");

        // Tab 0: Toàn bộ Hệ Thống (60 Classes)
        xml.append(buildDrawioDiagramXml("FullArchitecture", "0. Toàn bộ Hệ Thống (60 Classes)", classes, edges, true));

        // Tab 1: Model Layer (19 Classes)
        List<UmlClass> modelClasses = filterClassesByPkg(classes, "pkg_model");
        List<UmlEdge> modelEdges = filterEdgesByClasses(edges, modelClasses);
        xml.append(buildDrawioDiagramXml("ModelLayer", "1. Model Layer (19 Classes)", modelClasses, modelEdges, false));

        // Tab 2: Repository Layer (13 Classes)
        List<UmlClass> repoClasses = filterClassesByPkg(classes, "pkg_repo");
        List<UmlEdge> repoEdges = filterEdgesByClasses(edges, repoClasses);
        xml.append(buildDrawioDiagramXml("RepoLayer", "2. Repository Layer (13 Classes)", repoClasses, repoEdges, false));

        // Tab 3: Controller Layer (13 Classes)
        List<UmlClass> ctrlClasses = filterClassesByPkg(classes, "pkg_ctrl");
        List<UmlEdge> ctrlEdges = filterEdgesByClasses(edges, ctrlClasses);
        xml.append(buildDrawioDiagramXml("CtrlLayer", "3. Controller Layer (13 Classes)", ctrlClasses, ctrlEdges, false));

        // Tab 4: Exception Layer (8 Classes)
        List<UmlClass> exClasses = filterClassesByPkg(classes, "pkg_ex");
        List<UmlEdge> exEdges = filterEdgesByClasses(edges, exClasses);
        xml.append(buildDrawioDiagramXml("ExLayer", "4. Exception Layer (8 Classes)", exClasses, exEdges, false));

        // Tab 5: View Layer (7 Classes)
        List<UmlClass> viewClasses = filterClassesByPkg(classes, "pkg_view");
        List<UmlEdge> viewEdges = filterEdgesByClasses(edges, viewClasses);
        xml.append(buildDrawioDiagramXml("ViewLayer", "5. View Layer (7 Classes)", viewClasses, viewEdges, false));

        xml.append("</mxfile>\n");

        writeFile(new File("docs/Full_Class_Diagram.drawio"), xml.toString());
        writeFile(new File("../Lab211-Group1/ClassDiagram/Full_Class_Diagram.drawio"), xml.toString());
    }

    private static String buildDrawioDiagramXml(String diagId, String diagName, List<UmlClass> classes, List<UmlEdge> edges, boolean isFull) {
        StringBuilder sb = new StringBuilder();
        sb.append("  <diagram id=\"").append(diagId).append("\" name=\"").append(escapeXml(diagName)).append("\">\n");
        sb.append("    <mxGraphModel dx=\"0\" dy=\"0\" grid=\"1\" gridSize=\"10\" guides=\"1\" tooltips=\"1\" connect=\"1\" arrows=\"1\" fold=\"1\" page=\"0\" pageScale=\"1\" math=\"0\" shadow=\"0\">\n");
        sb.append("      <root>\n");
        sb.append("        <mxCell id=\"0\" />\n");
        sb.append("        <mxCell id=\"1\" parent=\"0\" />\n");

        if (isFull) {
            // Group Containers
            sb.append("        <mxCell id=\"pkg_model\" value=\"1. Model Layer (Entities, Enums, Utils)\" style=\"swimlane;html=1;startSize=26;fillColor=#ffffff;strokeColor=#000000;fontColor=#000000;fontStyle=1;fontSize=14;strokeWidth=1.5;\" vertex=\"1\" parent=\"1\">\n");
            sb.append("          <mxGeometry x=\"30\" y=\"90\" width=\"1380\" height=\"1250\" as=\"geometry\" />\n");
            sb.append("        </mxCell>\n");

            sb.append("        <mxCell id=\"pkg_repo\" value=\"2. Repository Layer (Generic CSV &amp; Physical File I/O)\" style=\"swimlane;html=1;startSize=26;fillColor=#ffffff;strokeColor=#000000;fontColor=#000000;fontStyle=1;fontSize=14;strokeWidth=1.5;\" vertex=\"1\" parent=\"1\">\n");
            sb.append("          <mxGeometry x=\"1450\" y=\"90\" width=\"2110\" height=\"1250\" as=\"geometry\" />\n");
            sb.append("        </mxCell>\n");

            sb.append("        <mxCell id=\"pkg_ctrl\" value=\"3. Controller Layer (MVC Flow &amp; Threads)\" style=\"swimlane;html=1;startSize=26;fillColor=#ffffff;strokeColor=#000000;fontColor=#000000;fontStyle=1;fontSize=14;strokeWidth=1.5;\" vertex=\"1\" parent=\"1\">\n");
            sb.append("          <mxGeometry x=\"30\" y=\"1370\" width=\"1950\" height=\"1090\" as=\"geometry\" />\n");
            sb.append("        </mxCell>\n");

            sb.append("        <mxCell id=\"pkg_ex\" value=\"4. Exception Layer (Concurrency &amp; Validation)\" style=\"swimlane;html=1;startSize=26;fillColor=#ffffff;strokeColor=#000000;fontColor=#000000;fontStyle=1;fontSize=14;strokeWidth=1.5;\" vertex=\"1\" parent=\"1\">\n");
            sb.append("          <mxGeometry x=\"2010\" y=\"1370\" width=\"750\" height=\"1090\" as=\"geometry\" />\n");
            sb.append("        </mxCell>\n");

            sb.append("        <mxCell id=\"pkg_view\" value=\"5. View Layer &amp; Application Entrypoint\" style=\"swimlane;html=1;startSize=26;fillColor=#ffffff;strokeColor=#000000;fontColor=#000000;fontStyle=1;fontSize=14;strokeWidth=1.5;\" vertex=\"1\" parent=\"1\">\n");
            sb.append("          <mxGeometry x=\"2790\" y=\"1370\" width=\"770\" height=\"1090\" as=\"geometry\" />\n");
            sb.append("        </mxCell>\n");
        }

        // Classes
        for (UmlClass c : classes) {
            StringBuilder val = new StringBuilder();
            val.append("<p style=\"margin:0px;padding-top:4px;text-align:center;\"><b>").append(escapeXml(c.name)).append("</b>");
            if (c.sub != null && !c.sub.isEmpty()) {
                val.append("<br><i style=\"font-size:10px;color:#4b5563;\">").append(escapeXml(c.sub)).append("</i>");
            }
            val.append("</p>");

            if (c.attrs != null && c.attrs.length > 0) {
                val.append("<hr size=\"1\" style=\"border-color:#000000;\">");
                val.append("<p style=\"margin:0px;padding-left:6px;font-family:Courier New;font-size:11px;\">");
                for (String a : c.attrs) {
                    val.append(escapeXml(a)).append("<br>");
                }
                val.append("</p>");
            }

            if (c.meths != null && c.meths.length > 0) {
                val.append("<hr size=\"1\" style=\"border-color:#000000;\">");
                val.append("<p style=\"margin:0px;padding-left:6px;font-family:Courier New;font-size:11px;\">");
                for (String m : c.meths) {
                    val.append(escapeXml(m)).append("<br>");
                }
                val.append("</p>");
            }

            sb.append("        <mxCell id=\"").append(c.id).append("\" value=\"").append(escapeXml(val.toString())).append("\" ");
            sb.append("style=\"verticalAlign=top;align=left;overflow=fill;fontSize=12;fontFamily=Helvetica;html=1;whiteSpace=wrap;fillColor=#ffffff;strokeColor=#000000;fontColor=#000000;strokeWidth=1.5;\" ");
            sb.append("vertex=\"1\" parent=\"1\">\n");
            sb.append("          <mxGeometry x=\"").append(c.x).append("\" y=\"").append(c.y).append("\" width=\"").append(c.w).append("\" height=\"").append(c.h).append("\" as=\"geometry\" />\n");
            sb.append("        </mxCell>\n");
        }

        // Edges
        for (UmlEdge e : edges) {
            String style;
            if ("inherit".equals(e.type)) {
                style = "edgeStyle=orthogonalEdgeStyle;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;endArrow=block;endFill=0;strokeColor=#000000;strokeWidth=1.5;";
            } else if ("realize".equals(e.type)) {
                style = "edgeStyle=orthogonalEdgeStyle;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;dashed=1;endArrow=block;endFill=0;strokeColor=#000000;strokeWidth=1.5;";
            } else if ("aggr".equals(e.type)) {
                style = "edgeStyle=orthogonalEdgeStyle;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;startArrow=diamond;startFill=0;endArrow=none;strokeColor=#000000;strokeWidth=1.5;";
            } else if ("depend".equals(e.type)) {
                style = "edgeStyle=orthogonalEdgeStyle;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;dashed=1;endArrow=open;endFill=0;strokeColor=#000000;strokeWidth=1.2;";
            } else {
                style = "edgeStyle=orthogonalEdgeStyle;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;endArrow=open;endFill=0;strokeColor=#000000;strokeWidth=1.2;";
            }

            sb.append("        <mxCell id=\"").append(e.id).append("\" value=\"").append(escapeXml(e.label)).append("\" ");
            sb.append("style=\"").append(style).append("fontColor=#000000;fontSize=10;\" ");
            sb.append("edge=\"1\" parent=\"1\" source=\"").append(e.sourceId).append("\" target=\"").append(e.targetId).append("\">\n");
            sb.append("          <mxGeometry relative=\"1\" as=\"geometry\" />\n");
            sb.append("        </mxCell>\n");
        }

        sb.append("      </root>\n");
        sb.append("    </mxGraphModel>\n");
        sb.append("  </diagram>\n");
        return sb.toString();
    }

    private static void generateModularDiagrams(List<UmlClass> classes, List<UmlEdge> edges) throws Exception {
        File dir = new File("docs/diagrams");
        if (!dir.exists()) dir.mkdirs();

        String[] pkgKeys = {"pkg_model", "pkg_repo", "pkg_ctrl", "pkg_ex", "pkg_view"};
        String[] fileNames = {"1_Model_Layer", "2_Repository_Layer", "3_Controller_Layer", "4_Exception_Layer", "5_View_Layer"};
        String[] titles = {"1. Model Layer (19 Classes)", "2. Repository Layer (13 Classes)", "3. Controller Layer (13 Classes)", "4. Exception Layer (8 Classes)", "5. View Layer (7 Classes)"};

        for (int i = 0; i < pkgKeys.length; i++) {
            List<UmlClass> subClasses = filterClassesByPkg(classes, pkgKeys[i]);
            List<UmlEdge> subEdges = filterEdgesByClasses(edges, subClasses);

            // Generate Draw.io
            StringBuilder xml = new StringBuilder();
            xml.append("<mxfile host=\"app.diagrams.net\" modified=\"2026-11-11T08:00:00.000Z\" agent=\"Antigravity\" version=\"22.0.0\" type=\"device\">\n");
            xml.append(buildDrawioDiagramXml("Diag_" + fileNames[i], titles[i], subClasses, subEdges, false));
            xml.append("</mxfile>\n");
            writeFile(new File("docs/diagrams/" + fileNames[i] + ".drawio"), xml.toString());

            // Generate SVG
            String svg = renderSvgContent(subClasses, subEdges, 3600, 2500, false);
            writeFile(new File("docs/diagrams/" + fileNames[i] + ".svg"), svg);
        }

        // Copy full to diagrams as well
        writeFile(new File("docs/diagrams/0_Full_Architecture.drawio"), new File("docs/Full_Class_Diagram.drawio"));
        writeFile(new File("docs/diagrams/0_Full_Architecture.svg"), new File("docs/Full_Class_Diagram.svg"));
    }

    private static List<UmlClass> filterClassesByPkg(List<UmlClass> classes, String pkg) {
        List<UmlClass> res = new ArrayList<>();
        for (UmlClass c : classes) {
            if (pkg.equals(c.pkg)) res.add(c);
        }
        return res;
    }

    private static List<UmlEdge> filterEdgesByClasses(List<UmlEdge> edges, List<UmlClass> classes) {
        List<UmlEdge> res = new ArrayList<>();
        for (UmlEdge e : edges) {
            boolean hasSrc = false, hasTgt = false;
            for (UmlClass c : classes) {
                if (c.id.equals(e.sourceId)) hasSrc = true;
                if (c.id.equals(e.targetId)) hasTgt = true;
            }
            if (hasSrc && hasTgt) res.add(e);
        }
        return res;
    }

    private static void generateMermaidMarkdown(List<UmlClass> classes, List<UmlEdge> edges) throws Exception {
        StringBuilder md = new StringBuilder();
        md.append("# 🏛️ TOÀN BỘ CLASS DIAGRAM HỆ THỐNG (60 CLASSES - 112 RELATIONSHIPS)\n\n");
        md.append("> **Đồ án:** Food Delivery Network Simulation  \n");
        md.append("> **Môn học:** LAB211 – OOP with Java | FPT University  \n");
        md.append("> **Kiến trúc:** 100% OOP (Không static ngoài main, không final/protected, 100% this., MVC Pattern trên CSV)\n\n");

        md.append("## 📑 MỤC LỤC & PHÂN PHIA PHÂN HỆ\n");
        md.append("1. [Phân hệ 1: Model Layer (19 Classes & Enums)](#1-phân-hệ-1-model-layer-19-classes--enums)\n");
        md.append("2. [Phân hệ 2: Repository Layer (13 Classes)](#2-phân-hệ-2-repository-layer-13-classes)\n");
        md.append("3. [Phân hệ 3: Controller Layer (13 Classes)](#3-phân-hệ-3-controller-layer-13-classes)\n");
        md.append("4. [Phân hệ 4: Exception Layer (8 Classes)](#4-phân-hệ-4-exception-layer-8-classes)\n");
        md.append("5. [Phân hệ 5: View Layer & Main (7 Classes)](#5-phân-hệ-5-view-layer--main-7-classes)\n");
        md.append("6. [Sơ đồ Tổng Thể Toàn Hệ Thống](#6-sơ-đồ-tổng-thể-toàn-hệ-thống)\n\n");

        String[] pkgKeys = {"pkg_model", "pkg_repo", "pkg_ctrl", "pkg_ex", "pkg_view"};
        String[] titles = {
                "### 1. Phân hệ 1: Model Layer (19 Classes & Enums)",
                "### 2. Phân hệ 2: Repository Layer (13 Classes)",
                "### 3. Phân hệ 3: Controller Layer (13 Classes)",
                "### 4. Phân hệ 4: Exception Layer (8 Classes)",
                "### 5. Phân hệ 5: View Layer & Main (7 Classes)"
        };

        for (int i = 0; i < pkgKeys.length; i++) {
            List<UmlClass> subClasses = filterClassesByPkg(classes, pkgKeys[i]);
            List<UmlEdge> subEdges = filterEdgesByClasses(edges, subClasses);

            md.append(titles[i]).append("\n\n```mermaid\nclassDiagram\n    direction TB\n\n");
            renderMermaidClassesAndEdges(md, subClasses, subEdges);
            md.append("```\n\n");
        }

        md.append("### 6. Sơ đồ Tổng Thể Toàn Hệ Thống (Full 60 Classes)\n\n```mermaid\nclassDiagram\n    direction TB\n\n");
        renderMermaidClassesAndEdges(md, classes, edges);
        md.append("```\n");

        writeFile(new File("docs/class_diagram.md"), md.toString());
        writeFile(new File("../Lab211-Group1/ClassDiagram/class_diagram.md"), md.toString());
    }

    private static void renderMermaidClassesAndEdges(StringBuilder md, List<UmlClass> classes, List<UmlEdge> edges) {
        for (UmlClass c : classes) {
            md.append("    class ").append(c.id).append(" {\n");
            if (c.sub != null && !c.sub.isEmpty()) {
                md.append("        <<").append(c.sub.replace("<<", "").replace(">>", "")).append(">>\n");
            }
            if (c.attrs != null) {
                for (String a : c.attrs) {
                    md.append("        ").append(a.replace(":", "").replace("<", "~").replace(">", "~")).append("\n");
                }
            }
            if (c.meths != null) {
                for (String m : c.meths) {
                    md.append("        ").append(m.replace(":", "").replace("<", "~").replace(">", "~").replace("*", "")).append("\n");
                }
            }
            md.append("    }\n");
        }

        md.append("\n    %% RELATIONSHIPS\n");
        for (UmlEdge e : edges) {
            String op;
            if ("inherit".equals(e.type)) {
                op = " <|-- ";
                md.append("    ").append(e.targetId).append(op).append(e.sourceId).append("\n");
            } else if ("realize".equals(e.type)) {
                op = " <|.. ";
                md.append("    ").append(e.targetId).append(op).append(e.sourceId).append("\n");
            } else if ("aggr".equals(e.type)) {
                op = " o-- ";
                md.append("    ").append(e.sourceId).append(op).append(e.targetId).append("\n");
            } else if ("depend".equals(e.type)) {
                op = " ..> ";
                md.append("    ").append(e.sourceId).append(op).append(e.targetId)
                        .append(e.label.isEmpty() ? "" : " : " + e.label).append("\n");
            } else {
                op = " --> ";
                md.append("    ").append(e.sourceId).append(op).append(e.targetId)
                        .append(e.label.isEmpty() ? "" : " : " + e.label).append("\n");
            }
        }
    }

    private static String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private static void writeFile(File file, String content) throws Exception {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            bw.write(content);
        }
    }

    private static void writeFile(File dest, File src) throws Exception {
        if (dest.getParentFile() != null && !dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        byte[] bytes = java.nio.file.Files.readAllBytes(src.toPath());
        java.nio.file.Files.write(dest.toPath(), bytes);
    }
}
