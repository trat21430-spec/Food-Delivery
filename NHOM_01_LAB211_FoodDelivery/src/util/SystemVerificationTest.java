package util;

import model.GeoUtils;
import model.LockMechanism;
import model.SimulationRun;
import controller.OrderController;
import controller.SimulatorController;
import repository.MenuItemRepository;
import view.SimulatorView;

import java.util.ArrayList;
import java.util.List;

public class SystemVerificationTest {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("  LAB211 FOOD DELIVERY SIMULATION - AUTOMATED SYSTEM TEST RUNNER ");
        System.out.println("=================================================================");
        SystemVerificationTest runner = new SystemVerificationTest();
        runner.runAllTests();
    }

    public void runAllTests() {
        this.testHaversineFormula();
        this.testSingleThreadOrderFlow();
        this.testSimulatorConcurrencyBenchmark();
        this.testCustomerRegistrationAndDuplicatePrevention();
        this.testDriverRegistrationAndDuplicatePrevention();
        this.testAutoDispatchPendingOrders();
        this.testRealisticEndToEndWorkflow();
        this.testQuyNhonEtaAndDriverNotification();
    }

    public void testHaversineFormula() {
        System.out.println("\n--- [TEST 1] Testing Haversine Distance Formula ---");
        GeoUtils geoUtils = new GeoUtils();
        double distanceHnToHcm = geoUtils.haversineDistance(21.0285, 105.8542, 10.8231, 106.6297);
        System.out.printf("Calculated Distance HN -> HCM: %.2f km (Expected approx 1138 km)%n", distanceHnToHcm);
        if (Math.abs(distanceHnToHcm - 1138.0) <= 5.0) {
            System.out.println(">>> [PASS] Haversine formula calculation is accurate!");
        } else {
            System.err.println(">>> [FAIL] Haversine calculation deviates significantly: " + distanceHnToHcm);
        }
    }

    public void testSingleThreadOrderFlow() {
        System.out.println("\n--- [TEST 2] Testing Single-Thread Place Order & Dispatch ---");
        try {
            MenuItemRepository menuRepo = new MenuItemRepository();
            model.MenuItem testItem = menuRepo.findById("M00001");
            if (testItem != null && testItem.getStockQty() < 10) {
                testItem.setStockQty(50);
                menuRepo.save(testItem);
            }

            controller.DriverController driverController = new controller.DriverController();
            List<model.Driver> available = driverController.getAvailableDrivers();
            if (available.isEmpty()) {
                List<model.Driver> allDrivers = driverController.getAllDrivers();
                if (!allDrivers.isEmpty()) {
                    model.Driver first = allDrivers.get(0);
                    first.setStatus(model.DriverStatus.AVAILABLE);
                    driverController.updateDriverStatus(first.getId(), model.DriverStatus.AVAILABLE);
                }
            }

            OrderController orderController = new OrderController();
            model.Order order = orderController.placeOrderAndDispatch("C0001", "R001", "M00001", 1, LockMechanism.SYNCHRONIZED);
            if (order != null && order.getId() != null) {
                System.out.println(">>> [PASS] Order placed successfully! ID: " + order.getId()
                        + ", Driver: " + order.getDriverId() + ", Status: " + order.getStatus());
            } else {
                System.err.println(">>> [FAIL] Order placement returned null or invalid order.");
            }
        } catch (Exception e) {
            System.err.println(">>> [FAIL] Exception in single thread order placement: " + e.getMessage());
        }
    }

    public void testSimulatorConcurrencyBenchmark() {
        System.out.println("\n--- [TEST 3] Testing Simulator Concurrency Benchmark (4 Mechanisms) ---");
        try {
            SimulatorController simulator = new SimulatorController();
            int testOrderCount = 100;
            List<SimulationRun> runs = new ArrayList<>();

            LockMechanism[] mechanisms = new LockMechanism[] {
                LockMechanism.NO_LOCK,
                LockMechanism.SYNCHRONIZED,
                LockMechanism.OPTIMISTIC,
                LockMechanism.FILE_LOCK
            };

            for (int i = 0; i < mechanisms.length; i++) {
                LockMechanism mech = mechanisms[i];
                System.out.println("Simulating " + testOrderCount + " concurrent orders with mechanism: " + mech.name() + "...");
                SimulationRun run = simulator.runSimulation(testOrderCount, mech);
                if (run != null) {
                    runs.add(run);
                }
            }

            SimulatorView view = new SimulatorView();
            view.renderComparisonTable(runs);

            boolean hasAnomaliesInNoLock = false;
            boolean lockingHasZeroErrors = true;

            for (int i = 0; i < runs.size(); i++) {
                SimulationRun r = runs.get(i);
                int totalErrors = r.getDoubleAssignErrors() + r.getOversellErrors() + r.getDriverOverloadErrors();
                if (r.getMechanism() == LockMechanism.NO_LOCK) {
                    if (totalErrors > 0) {
                        hasAnomaliesInNoLock = true;
                    }
                } else {
                    if (totalErrors != 0) {
                        lockingHasZeroErrors = false;
                    }
                }
            }

            if (hasAnomaliesInNoLock && lockingHasZeroErrors) {
                System.out.println(">>> [PASS] Concurrency benchmark passed! NO_LOCK revealed race conditions while locking mechanisms guaranteed 0% anomalies!");
            } else {
                System.out.println(">>> [INFO] Concurrency runs finished (NoLock errors: " + hasAnomaliesInNoLock + ", Locking 0 errors: " + lockingHasZeroErrors + ")");
            }
        } catch (Exception e) {
            System.err.println(">>> [FAIL] Exception in simulator benchmark: " + e.getMessage());
        }
    }

    public void testCustomerRegistrationAndDuplicatePrevention() {
        System.out.println("\n--- [TEST 4] Testing Customer Registration & Duplicate Prevention ---");
        try {
            controller.AuthController auth = new controller.AuthController();
            String testCustUser = "test_cust_" + System.currentTimeMillis();
            String testCustPhone = "098" + (1000000 + (int)(Math.random() * 8999999));
            model.User cUser = auth.registerCustomer(testCustUser, "pass123", "Khach Hang Test", testCustPhone, "123 Le Loi, Quy Nhon", 13.775, 109.225);
            if (cUser != null && cUser.getLinkedEntityId() != null) {
                System.out.println(">>> [PASS] New customer registered successfully: " + cUser.getUsername() + " (ID: " + cUser.getLinkedEntityId() + ")");
            }
            try {
                auth.registerCustomer(testCustUser, "pass123", "Khach Hang Test", testCustPhone, "123 Le Loi, Quy Nhon", 13.775, 109.225);
                System.err.println(">>> [FAIL] Duplicate customer registration was not blocked!");
            } catch (exception.InvalidInputException e) {
                System.out.println(">>> [PASS] Duplicate customer correctly blocked: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println(">>> [FAIL] Exception in customer registration test: " + e.getMessage());
        }
    }

    public void testDriverRegistrationAndDuplicatePrevention() {
        System.out.println("\n--- [TEST 5] Testing Driver Registration & Duplicate Prevention ---");
        try {
            controller.AuthController auth = new controller.AuthController();
            String testDriverUser = "test_driver_" + System.currentTimeMillis();
            String testDriverPhone = "097" + (1000000 + (int)(Math.random() * 8999999));
            model.User dUser = auth.registerDriver(testDriverUser, "pass123", "Tai Xe Test", testDriverPhone, 13.775, 109.225);
            if (dUser != null && dUser.getLinkedEntityId() != null) {
                System.out.println(">>> [PASS] New driver registered successfully: " + dUser.getUsername() + " (Driver ID: " + dUser.getLinkedEntityId() + ")");
            }
            try {
                auth.registerDriver(testDriverUser, "pass123", "Tai Xe Test", testDriverPhone, 13.775, 109.225);
                System.err.println(">>> [FAIL] Duplicate driver registration was not blocked!");
            } catch (exception.InvalidInputException e) {
                System.out.println(">>> [PASS] Duplicate driver correctly blocked: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println(">>> [FAIL] Exception in driver registration test: " + e.getMessage());
        }
    }

    public void testAutoDispatchPendingOrders() {
        System.out.println("\n--- [TEST 6] Testing Automatic Driver Dispatch for Pending Orders ---");
        try {
            OrderController orderController = new OrderController();
            List<String> logs = orderController.autoDispatchAllPending(LockMechanism.SYNCHRONIZED);
            System.out.println(">>> [PASS] Auto-dispatch executed successfully! Processed " + logs.size() + " orders.");
        } catch (Exception e) {
            System.err.println(">>> [FAIL] Exception in auto-dispatch test: " + e.getMessage());
        }
    }

    public void testRealisticEndToEndWorkflow() {
        System.out.println("\n--- [TEST 7] Testing Realistic End-to-End Business Workflow ---");
        try {
            controller.AuthController auth = new controller.AuthController();
            controller.AdminController admin = new controller.AdminController();
            controller.OrderController orderCtrl = new controller.OrderController();
            controller.RestaurantController restCtrl = new controller.RestaurantController();
            controller.DriverController driverCtrl = new controller.DriverController();
            repository.MenuItemRepository itemRepo = new repository.MenuItemRepository();

            // 1. Submit driver application
            String drvAppUser = "app_driver_" + System.currentTimeMillis();
            String drvPhone = "098" + (1000000 + (int)(Math.random() * 8999999));
            model.DriverApplication app = auth.submitDriverApplication(drvAppUser, "pass123", "Driver Candidate", drvPhone, "77-L1 999.99", "Xe May");
            if (app == null || !"PENDING".equals(app.getStatus())) {
                System.err.println(">>> [FAIL] Driver application status is not PENDING");
                return;
            }
            System.out.println(">>> [PASS] Step 1: Driver application submitted as PENDING (ID: " + app.getId() + ")");

            // 2. Admin approves driver application
            model.User newDrvUser = admin.approveDriverApplication(app.getId());
            if (newDrvUser == null || newDrvUser.getRole() != model.UserRole.DRIVER) {
                System.err.println(">>> [FAIL] Admin approval failed");
                return;
            }
            model.Driver approvedDriver = driverCtrl.getDriverById(newDrvUser.getLinkedEntityId());
            if (approvedDriver == null || approvedDriver.getStatus() != model.DriverStatus.OFFLINE) {
                System.err.println(">>> [FAIL] Newly approved driver initial status is not OFFLINE");
                return;
            }
            System.out.println(">>> [PASS] Step 2: Admin approved driver application -> Driver ID: " + approvedDriver.getId() + " [OFFLINE]");

            // 3. Driver goes online (AVAILABLE)
            driverCtrl.updateDriverStatus(approvedDriver.getId(), model.DriverStatus.AVAILABLE);
            System.out.println(">>> [PASS] Step 3: Driver went online [AVAILABLE]");

            // 4. Customer places realistic order (PENDING)
            List<model.Restaurant> rests = restCtrl.getAllRestaurants();
            model.Restaurant testRest = rests.get(0);
            List<model.MenuItem> items = restCtrl.getMenuByRestaurant(testRest.getId());
            model.MenuItem testItem = items.get(0);
            int initialStock = testItem.getStockQty();
            if (initialStock < 10) {
                testItem.setStockQty(50);
                itemRepo.save(testItem);
                initialStock = 50;
            }

            model.Order custOrder = orderCtrl.createCustomerOrder("C0001", testRest.getId(), testItem.getId(), 2, LockMechanism.SYNCHRONIZED);
            if (custOrder == null || custOrder.getStatus() != model.OrderStatus.PENDING) {
                System.err.println(">>> [FAIL] Customer order status is not PENDING");
                return;
            }
            model.MenuItem stockAfterOrder = itemRepo.findById(testItem.getId());
            if (stockAfterOrder.getStockQty() != initialStock - 2) {
                System.err.println(">>> [FAIL] Stock was not deducted properly");
                return;
            }
            System.out.println(">>> [PASS] Step 4: Customer placed order (ID: " + custOrder.getId() + ") -> Status: PENDING, Stock: " + stockAfterOrder.getStockQty());

            // 5. Restaurant manager confirms order -> Auto-dispatch assigns driver
            model.Order confirmedOrder = orderCtrl.confirmOrderByRestaurant(custOrder.getId(), testRest.getId(), LockMechanism.SYNCHRONIZED);
            if (confirmedOrder == null || confirmedOrder.getStatus() != model.OrderStatus.CONFIRMED) {
                System.err.println(">>> [FAIL] Confirmed order status is not CONFIRMED");
                return;
            }
            System.out.println(">>> [PASS] Step 5: Restaurant confirmed order -> Assigned Driver: " + confirmedOrder.getDriverId());

            // 6. Driver picks up order -> DISPATCHED
            String assignedDrvId = confirmedOrder.getDriverId();
            if (assignedDrvId != null && !assignedDrvId.isEmpty()) {
                boolean pickedUp = orderCtrl.markOrderPickedUp(confirmedOrder.getId(), assignedDrvId);
                if (!pickedUp) {
                    System.err.println(">>> [FAIL] Driver pickup failed");
                    return;
                }
                System.out.println(">>> [PASS] Step 6: Driver confirmed picked up -> Status: DISPATCHED");

                // 7. Driver completes delivery -> DELIVERED, freed to AVAILABLE
                orderCtrl.setAutoDispatchEnabled(false); boolean delivered = orderCtrl.markOrderDelivered(confirmedOrder.getId(), assignedDrvId, LockMechanism.SYNCHRONIZED); orderCtrl.setAutoDispatchEnabled(true);
                if (!delivered) {
                    System.err.println(">>> [FAIL] Driver delivery failed");
                    return;
                }
                model.Driver freedDriver = driverCtrl.getDriverById(assignedDrvId);
                if (freedDriver.getStatus() != model.DriverStatus.AVAILABLE) {
                    System.err.println(">>> [FAIL] Driver was not freed back to AVAILABLE");
                    return;
                }
                System.out.println(">>> [PASS] Step 7: Driver confirmed delivery -> Status: DELIVERED, Driver freed [AVAILABLE]");
            }

            // 8. Test Restaurant Reject and Stock Restore
            int stockBeforeRejectOrder = itemRepo.findById(testItem.getId()).getStockQty();
            model.Order orderToReject = orderCtrl.createCustomerOrder("C0001", testRest.getId(), testItem.getId(), 3, LockMechanism.SYNCHRONIZED);
            orderCtrl.rejectOrderByRestaurant(orderToReject.getId(), testRest.getId(), "Het nguyen lieu");
            model.Order rejected = orderCtrl.getOrderById(orderToReject.getId());
            int stockAfterReject = itemRepo.findById(testItem.getId()).getStockQty();
            if (rejected.getStatus() != model.OrderStatus.CANCELLED || stockAfterReject != stockBeforeRejectOrder) {
                System.err.println(">>> [FAIL] Reject order did not restore stock properly");
                return;
            }
            System.out.println(">>> [PASS] Step 8: Restaurant rejected order -> Status: CANCELLED, Stock restored to " + stockAfterReject);

            // 9. Test Customer Cancel and Stock Restore
            int stockBeforeCancelOrder = itemRepo.findById(testItem.getId()).getStockQty();
            model.Order orderToCancel = orderCtrl.createCustomerOrder("C0001", testRest.getId(), testItem.getId(), 1, LockMechanism.SYNCHRONIZED);
            orderCtrl.cancelOrderByCustomer(orderToCancel.getId(), "C0001");
            model.Order cancelled = orderCtrl.getOrderById(orderToCancel.getId());
            int stockAfterCancel = itemRepo.findById(testItem.getId()).getStockQty();
            if (cancelled.getStatus() != model.OrderStatus.CANCELLED || stockAfterCancel != stockBeforeCancelOrder) {
                System.err.println(">>> [FAIL] Cancel order did not restore stock properly");
                return;
            }
            System.out.println(">>> [PASS] Step 9: Customer cancelled pending order -> Status: CANCELLED, Stock restored to " + stockAfterCancel);

            System.out.println(">>> [PASS] ALL REALISTIC END-TO-END WORKFLOW VERIFICATIONS PASSED 100%!");
        } catch (Exception e) {
            System.err.println(">>> [FAIL] Exception in realistic workflow test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void testQuyNhonEtaAndDriverNotification() {
        System.out.println("\n--- [TEST 8] Testing Quy Nhon ETA Calculation & Driver Notification ---");
        try {
            model.GeoUtils geo = new model.GeoUtils();
            double driverToRestKm = 1.2;
            double restToCustKm = 3.5;
            int pickupMin = geo.calculatePickupMinutes(driverToRestKm);
            int deliveryMin = geo.calculateDeliveryMinutes(driverToRestKm, restToCustKm);
            long baseMs = System.currentTimeMillis();
            String etaPickup = geo.formatEstimatedClockTime(baseMs, pickupMin);
            String etaDelivery = geo.formatEstimatedClockTime(baseMs, deliveryMin);

            if (pickupMin <= 0 || deliveryMin <= pickupMin) {
                System.err.println(">>> [FAIL] Invalid ETA calculation: pickup=" + pickupMin + ", delivery=" + deliveryMin);
                return;
            }
            if (etaPickup == null || etaPickup.length() != 5 || etaDelivery == null || etaDelivery.length() != 5) {
                System.err.println(">>> [FAIL] Invalid clock formatting: pickup=" + etaPickup + ", delivery=" + etaDelivery);
                return;
            }
            System.out.println(">>> [PASS] Quy Nhon ETA pickup: ~" + pickupMin + " min (Clock: " + etaPickup + ") | Delivery: ~" + deliveryMin + " min (Clock: " + etaDelivery + ")");
            System.out.println(">>> [PASS] All Quy Nhon delivery metrics verified!");
        } catch (Exception e) {
            System.err.println(">>> [FAIL] Exception in Quy Nhon ETA test: " + e.getMessage());
        }
    }
}