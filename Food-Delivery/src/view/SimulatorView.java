package View;

import java.util.List;

public class SimulatorView {

    public void displayBenchmarkHeader() {
        System.out.println("\n================================ REPORT BENCHMARK ================================");
        printSeparator();
        System.out.printf("| %-15s | %-12s | %-10s | %-15s | %-10s |\n", 
                          "Lock Mechanism", "Number Of Orders", "RunningTime(ms)", "Throughput(req/s)", "Oversell Error");
        printSeparator();
    }

    public void displayResultRow(Object res) { 
        // TODO: Print details simulation ouput
    }

    public void displayBenchmarkFooter() {
        printSeparator();
        System.out.println("Note: Oversell Error > 0 that mean that lock mechanism doesn't safe.");
    }

    private void printSeparator() {
        System.out.println("+-----------------+--------------+------------+-----------------+------------+");
    }

    public void displayConclusion(List<Object> results) {
        System.out.println("\n[Conclusion]:");
        // TODO: Print Conclusion
    }
}