package view;

import model.SimulationRun;

public class ReportView {

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
        System.out.printf("Throughput              : %.2f ops/sec\n", run.getThroughputOpsPerSec());
        System.out.println("-------------------------------------------------------");
        System.out.println("Concurrency Anomalies Detected (Direct CSV Analysis):");
        System.out.println("  - Double Assignments  : " + run.getDoubleAssignments());
        System.out.println("  - Oversell Items      : " + run.getOversells());
        System.out.println("  - Driver Overloads    : " + run.getDriverOverloads());
        System.out.println("  - Total Faults        : " + run.getTotalErrors());
        System.out.println("=======================================================\n");
    }
}
