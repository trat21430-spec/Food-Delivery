package view;

import model.SimulationRun;
import java.util.List;

public class SimulatorView {

    public void renderComparisonTable(List<SimulationRun> runs) {
        System.out.println("\n=========================================================================================================================");
        System.out.println("                               CONCURRENCY SIMULATOR COMPARISON BENCHMARK TABLE (8 COLUMNS)");
        System.out.println("=========================================================================================================================");
        System.out.printf("| %-14s | %-8s | %-12s | %-16s | %-13s | %-10s | %-15s | %-10s |\n",
                "Mechanism", "Orders", "Duration(ms)", "Throughput(op/s)", "DoubleAssign", "Oversells", "DriverOverload", "ErrorRate");
        System.out.println("+----------------+----------+--------------+------------------+---------------+------------+-----------------+------------+");

        for (int i = 0; i < runs.size(); i++) {
            SimulationRun run = runs.get(i);
            double errorRate = (run.getTotalErrors() * 100.0) / (double)run.getTotalOrders();

            System.out.printf("| %-14s | %-8d | %-12d | %-16.2f | %-13d | %-10d | %-15d | %-9.1f%% |\n",
                    run.getMechanism().name(),
                    run.getTotalOrders(),
                    run.getDurationMs(),
                    run.getThroughputOpsPerSec(),
                    run.getDoubleAssignments(),
                    run.getOversells(),
                    run.getDriverOverloads(),
                    errorRate
            );
        }
        System.out.println("=========================================================================================================================\n");
    }
}
