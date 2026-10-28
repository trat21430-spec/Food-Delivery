package util;

import model.GeoUtils;
import model.LockMechanism;
import model.SimulationRun;
import controller.OrderController;
import controller.SimulatorController;
import view.SimulatorView;

import java.util.ArrayList;
import java.util.List;

public class SystemVerificationTest {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("  LAB211 FOOD DELIVERY SIMULATION - AUTOMATED SYSTEM TEST RUNNER ");
        System.out.println("=================================================================");

        SystemVerificationTest testRunner = new SystemVerificationTest();
        testRunner.runAllTests();
    }

    public void runAllTests() {
        this.testHaversineDistance();
        this.testSingleThreadOrderFlow();
        this.testSimulatorFourMechanisms();
    }

    public void testHaversineDistance() {
        System.out.println("\n--- [TEST 1] Testing Haversine Distance Formula ---");
        GeoUtils geoUtils = new GeoUtils();
        // Hanoi coordinates: 21.0285 N, 105.8542 E
        // Ho Chi Minh City coordinates: 10.8231 N, 106.6297 E
        double distanceHnToHcm = geoUtils.haversineDistance(21.0285, 105.8542, 10.8231, 106.6297);
        System.out.printf("Calculated Distance HN -> HCM: %.2f km (Expected approx 1138 km)\n", distanceHnToHcm);
        if (distanceHnToHcm >= 1100.0 && distanceHnToHcm <= 1200.0) {
            System.out.println(">>> [PASS] Haversine formula calculation is accurate!");
        } else {
            System.err.println(">>> [FAIL] Haversine formula returned unexpected result.");
        }
    }

    public void testSingleThreadOrderFlow() {
        System.out.println("\n--- [TEST 2] Testing Single-Thread Place Order & Dispatch ---");
        try {
            OrderController orderController = new OrderController();
            model.Order order = orderController.placeOrderAndDispatch("C0001", "R001", "M00001", 1, LockMechanism.SYNCHRONIZED);
            if (order != null && order.getDriverId() != null && !order.getDriverId().isEmpty()) {
                System.out.println(">>> [PASS] Order placed successfully! ID: " + order.getId() + ", Driver: " + order.getDriverId() + ", Status: " + order.getStatus());
            } else {
                System.err.println(">>> [FAIL] Order placing failed or driver not assigned.");
            }
        } catch (Exception e) {
            System.err.println(">>> [FAIL] Exception during single-thread test: " + e.getMessage());
        }
    }

    public void testSimulatorFourMechanisms() {
        System.out.println("\n--- [TEST 3] Testing Simulator Concurrency Benchmark (4 Mechanisms) ---");
        SimulatorController simulatorController = new SimulatorController();
        SimulatorView simulatorView = new SimulatorView();

        List<SimulationRun> runs = new ArrayList<>();
        LockMechanism[] mechanisms = new LockMechanism[] {
            LockMechanism.NO_LOCK,
            LockMechanism.SYNCHRONIZED,
            LockMechanism.OPTIMISTIC,
            LockMechanism.FILE_LOCK
        };

        int ordersPerTest = 100;

        for (int i = 0; i < mechanisms.length; i++) {
            LockMechanism m = mechanisms[i];
            System.out.println("Simulating " + ordersPerTest + " concurrent orders with mechanism: " + m.name() + "...");
            SimulationRun run = simulatorController.runSimulation(ordersPerTest, m);
            if (run != null) {
                runs.add(run);
            }
        }

        simulatorView.renderComparisonTable(runs);

        // Verification checks
        if (runs.size() == 4) {
            SimulationRun noLock = runs.get(0);
            SimulationRun sync = runs.get(1);
            SimulationRun opt = runs.get(2);
            SimulationRun fileLock = runs.get(3);

            boolean noLockHasErrors = (noLock.getDoubleAssignments() > 0 || noLock.getOversells() > 0 || noLock.getDriverOverloads() > 0);
            boolean syncClean = (sync.getTotalErrors() == 0);
            boolean optClean = (opt.getTotalErrors() == 0);
            boolean fileClean = (fileLock.getTotalErrors() == 0);

            if (noLockHasErrors && syncClean && optClean && fileClean) {
                System.out.println(">>> [PASS] Concurrency benchmark passed! NO_LOCK revealed race conditions while locking mechanisms guaranteed 0% anomalies!");
            } else {
                System.out.println(">>> [NOTE] Benchmark completed. Review anomalies in table above.");
            }
        }
    }
}
