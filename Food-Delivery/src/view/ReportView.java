package view;

import java.util.List;
import model.SimulationRun;

public class ReportView {
   public void displayReportSummary(List<SimulationRun> runs) {
       System.out.println("\n=== Summary history of simulation ===");
   }

   public void displayThroughputChart(List<SimulationRun> runs) {
       System.out.println("\n Chart THROUGHPUT Follows Real-Time (ASCII) ===");
   }
}
