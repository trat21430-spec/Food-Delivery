package View;

import model.SimulationRun;
import java.util.List;

public class ReportView {
   public void displayReportSummary(List<SimulaitonRun> runs) {
       System.out.println("\n=== Summary history of simulation ===");
   }    
   
   public void displayThroughputChart("List<SimulationRun> runs") {
       System.out.println("\n Chart THROUGHPUT Follows Real-Time (ASCII) ===");
       // TODO: Draw Chart
   }
}
