package view;

import model.SimulationRun;
import java.util.List;

/**
 * Displays the 8-column ASCII benchmark table comparing the 4 synchronization mechanisms.
 */
public class SimulatorView {

    public void printBenchmarkTable(List<SimulationRun> runs) {
        String sep = "+--------------+--------+----------+-------------+-----------+--------------+----------+----------------+";
        System.out.println(sep);
        System.out.printf("| %-12s | %-6s | %-8s | %-11s | %-9s | %-12s | %-8s | %-14s |%n",
                "Mechanism", "Orders", "Time(ms)", "Throughput", "Drop(%)", "DoubleAssign", "Oversell", "DriverOverload");
        System.out.println(sep);
        for (SimulationRun r : runs) {
            System.out.printf("| %-12s | %6d | %8d | %7.1f/sec | %8.2f%% | %12d | %8d | %14d |%n",
                    r.getMechanism().name(),
                    r.getTotalOrders(),
                    r.getDurationMs(),
                    r.getThroughputOpsPerSec(),
                    r.getThroughputDropPercent(),
                    r.getDoubleAssignErrors(),
                    r.getOversellErrors(),
                    r.getDriverOverloadErrors());
        }
        System.out.println(sep);
    }

    public void renderComparisonTable(List<SimulationRun> runs) {
        this.printBenchmarkTable(runs);
    }
}
