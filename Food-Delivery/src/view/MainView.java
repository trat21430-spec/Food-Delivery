package view;

import controller.OrderController;
import controller.SimulatorController;
import java.util.Scanner;

public class MainView {
   private final OrderController orderController;
   private final SimulatorController simulationController;

   private final OrderView orderView;
   private final MapView mapView;
   private final SimulatorView simulatorView;
   private final ReportView reportView;
   private final Scanner scanner;

   public MainView() {
       this.orderController = new OrderController();
       this.simulationController = new SimulatorController();
       this.orderView = new OrderView();
       this.mapView = new MapView();
       this.simulatorView = new SimulatorView();
       this.reportView = new ReportView();
       this.scanner = new Scanner(System.in);
   }

   public MainView(OrderController orderController, SimulatorController simulatorController) {
       this.orderController = orderController;
       this.simulationController = simulatorController;
       this.orderView = new OrderView();
       this.mapView = new MapView();
       this.simulatorView = new SimulatorView();
       this.reportView = new ReportView();
       this.scanner = new Scanner(System.in);
   }

   public void startMenu() {
       System.out.println("Food Delivery MVC menu is ready.");
   }

   private void handleViewMap() {
   }
}
