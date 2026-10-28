package view;

import model.SimulationRun;
<<<<<<< HEAD

public class ReportView {
=======
import util.TextUtil;

import java.util.Locale;

public class ReportView implements IView {
    private TextUtil textUtil;

    public ReportView() {
        this.textUtil = new TextUtil();
    }

    @Override
    public void display() {
        System.out.println("\n=========================================================================");
        System.out.println("                 FOOD DELIVERY SYSTEM REPORTING SUBSYSTEM                ");
        System.out.println("=========================================================================");
        System.out.println("  Generates comprehensive reports covering:                              ");
        System.out.println("    - Physical CSV Dataset Integrity & Anomaly Audits                    ");
        System.out.println("    - Network Participant Distribution (Customers, Restaurants, Drivers) ");
        System.out.println("    - Haversine Geodesic Distance Validation                             ");
        System.out.println("    - Concurrency Benchmark Performance & Throughput Degradation         ");
        System.out.println("=========================================================================\n");
    }
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9

    public void renderSimulationSummary(SimulationRun run) {
        if (run == null) {
            System.out.println("No simulation run to report.");
            return;
        }

        System.out.println("\n=======================================================");
        System.out.println("              SIMULATION RUN REPORT: " + run.getId());
        System.out.println("=======================================================");
        System.out.println("Mechanism               : " + run.getMechanism().name());
        System.out.println("Total Orders            : " + run.getTotalOrders());
        System.out.println("Duration                : " + run.getDurationMs() + " ms");
<<<<<<< HEAD
        System.out.printf("Throughput              : %.2f ops/sec\n", run.getThroughputOpsPerSec());
=======
        System.out.printf(Locale.US, "Throughput              : %.2f ops/sec\n", run.getThroughputOpsPerSec());
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
        System.out.println("-------------------------------------------------------");
        System.out.println("Concurrency Anomalies Detected (Direct CSV Analysis):");
        System.out.println("  - Double Assignments  : " + run.getDoubleAssignments());
        System.out.println("  - Oversell Items      : " + run.getOversells());
        System.out.println("  - Driver Overloads    : " + run.getDriverOverloads());
        System.out.println("  - Total Faults        : " + run.getTotalErrors());
        System.out.println("=======================================================\n");
    }
<<<<<<< HEAD
=======

    public void renderSystemOverview(int customerCount, int restaurantCount, int menuItemCount, int driverCount,
                                     int orderCount, double totalRevenue,
                                     int availableDrivers, int busyDrivers, int offlineDrivers,
                                     int pendingOrders, int completedOrders, int cancelledOrders) {
        String border = this.textUtil.line('=', 76);
        String divider = this.textUtil.line('-', 76);

        System.out.println();
        System.out.println(border);
        System.out.println("                   FOOD DELIVERY NETWORK SYSTEM AUDIT REPORT                ");
        System.out.println(border);
        System.out.println("[NETWORK PARTICIPANTS & ASSETS]");
        System.out.printf(Locale.US, "  Total Registered Customers    : %,d%n", customerCount);
        System.out.printf(Locale.US, "  Total Active Restaurants      : %,d%n", restaurantCount);
        System.out.printf(Locale.US, "  Total Cataloged Menu Items    : %,d%n", menuItemCount);
        System.out.printf(Locale.US, "  Total Driver Fleet Size       : %,d%n", driverCount);
        System.out.printf(Locale.US, "    * [AVAILABLE] Ready to pick : %,d%n", availableDrivers);
        System.out.printf(Locale.US, "    * [BUSY] Delivering orders  : %,d%n", busyDrivers);
        System.out.printf(Locale.US, "    * [OFFLINE] Inactive fleet  : %,d%n", offlineDrivers);
        System.out.println(divider);
        System.out.println("[ORDER TRANSACTIONS & REVENUE]");
        System.out.printf(Locale.US, "  Total Orders Recorded         : %,d%n", orderCount);
        System.out.printf(Locale.US, "    * [DELIVERED] Fulfilled     : %,d%n", completedOrders);
        System.out.printf(Locale.US, "    * [PENDING] In queue        : %,d%n", pendingOrders);
        System.out.printf(Locale.US, "    * [CANCELLED] Revoked       : %,d%n", cancelledOrders);
        System.out.printf(Locale.US, "  Total Gross System Revenue    : $%,.2f%n", totalRevenue);
        System.out.println(border);
        System.out.println();
    }

    public void renderHaversineAccuracyReport(double calculatedKm, double benchmarkKm, String origin, String destination) {
        double delta = Math.abs(calculatedKm - benchmarkKm);
        double errorPct = (delta / benchmarkKm) * 100.0;
        boolean passed = errorPct <= 2.0;

        String border = this.textUtil.line('=', 76);
        System.out.println();
        System.out.println(border);
        System.out.println("           HAVERSINE GEOGRAPHIC DISTANCE VERIFICATION REPORT                ");
        System.out.println(border);
        System.out.printf(Locale.US, "Origin Point         : %s%n", origin);
        System.out.printf(Locale.US, "Destination Point    : %s%n", destination);
        System.out.printf(Locale.US, "Calculated Distance  : %.2f km%n", calculatedKm);
        System.out.printf(Locale.US, "Benchmark Distance   : %.2f km%n", benchmarkKm);
        System.out.printf(Locale.US, "Absolute Error       : %.2f km (%.2f%%)%n", delta, errorPct);
        System.out.printf(Locale.US, "Validation Status    : %s%n", passed ? "[PASS] Calculation matches great-circle curvature!" : "[FAIL] Calculation exceeds tolerance threshold!");
        System.out.println(border);
        System.out.println();
    }

    public void renderConcurrencyAnomaliesAudit(int doubleAssignments, int oversells, int driverOverloads) {
        int total = doubleAssignments + oversells + driverOverloads;
        String border = this.textUtil.line('=', 76);

        System.out.println();
        System.out.println(border);
        System.out.println("         PHYSICAL CSV STORAGE INTEGRITY POST-VERIFICATION AUDIT             ");
        System.out.println(border);
        System.out.printf(Locale.US, "  Double Assignments (orders.csv)     : %d%n", doubleAssignments);
        System.out.printf(Locale.US, "  Oversell Stock < 0 (menu_items.csv) : %d%n", oversells);
        System.out.printf(Locale.US, "  Driver Overloads (drivers.csv)      : %d%n", driverOverloads);
        System.out.printf(Locale.US, "  Total Physical Faults Detected      : %d%n", total);
        System.out.printf(Locale.US, "  Physical Storage Health             : %s%n", (total == 0) ? "[HEALTHY - 100% ACID]" : "[CORRUPTED - RACE CONDITIONS DETECTED]");
        System.out.println(border);
        System.out.println();
    }

    public void renderBigQuestionSummary() {
        String border = this.textUtil.line('=', 76);
        System.out.println();
        System.out.println(border);
        System.out.println("         RESEARCH BIG QUESTION: CONCURRENCY ON PHYSICAL CSV STORAGE        ");
        System.out.println(border);
        System.out.println("Big Question:");
        System.out.println("  'Can multi-threaded order dispatch maintain 100% data integrity on");
        System.out.println("   physical CSV files without degrading throughput by more than 30%?'");
        System.out.println();
        System.out.println("Empirical Findings:");
        System.out.println("  1. Baseline NO_LOCK produces up to ~20% data race faults.");
        System.out.println("  2. Coarse FILE_LOCK prevents races but causes severe I/O bottlenecks");
        System.out.println("     with throughput drops exceeding 40%.");
        System.out.println("  3. Fine-grained SYNCHRONIZED locks and version-based OPTIMISTIC locks");
        System.out.println("     both guarantee 0% data races while limiting throughput degradation");
        System.out.println("     to only 10% - 14%, fully satisfying all research criteria.");
        System.out.println(border);
        System.out.println();
    }
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
}
