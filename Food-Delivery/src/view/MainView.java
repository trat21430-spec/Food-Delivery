package View;

import controller.OrderController;
import controller.SimulatorController;
import java.util.Scanner;

public class MainView {
   private OrderController orderController;
   private SimulatorController simulationController;
   
   private OrderView orderView;
   private MapView mapNew;
   private SimulatorView simulatorView;
   private ReportView reportView;
   private Scanner scanner;
   
   public MainView(OrderController orderController, SimulatorController simulatorController) {
       this.orderController = orderController;
       this.simulatorController = simulatorController;
       
       this.orderView = new OrderView();
       this.mapView = new MapView();
       this.simulatorView = new SimulatorView();
       this.reportView = new ReportView();
       
       this.scanner = new Scanner(System.in);
   }
   
   private void handleViewMap() {
       //orderView.displayRestaurants(...);
       //mapView.renderMap(...);
   }
   
   //... (Loops starts() must be keep)
}
