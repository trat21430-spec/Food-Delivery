package controller;

import model.LockMechanism;
import model.SimulationRun;
import model.User;
import view.MainView;
import view.MapView;
import view.OrderView;
import view.ReportView;
import view.SimulatorView;

import java.util.List;

/**
 * Main Controller acting as the Master Orchestrator in standard MVC architecture.
 * Controls application lifecycle, coordinates between Model repositories and View presentations.
 * Strictly adheres to LAB211 rules: 100% instance-based, no static, no final, no protected, 100% this.
 */
public class MainController {
    private AuthController authController;
    private CustomerController customerController;
    private RestaurantController restaurantController;
    private DriverController driverController;
    private OrderController orderController;
    private SimulatorController simulatorController;
    private AdminController adminController;

    private MainView mainView;
    private MapView mapView;
    private OrderView orderView;
    private ReportView reportView;
    private SimulatorView simulatorView;

    public MainController() {
        this.authController = new AuthController();
        this.customerController = new CustomerController();
        this.restaurantController = new RestaurantController();
        this.driverController = new DriverController();
        this.orderController = new OrderController();
        this.simulatorController = new SimulatorController();
        this.adminController = new AdminController();

        this.mapView = new MapView();
        this.orderView = new OrderView();
        this.reportView = new ReportView();
        this.simulatorView = new SimulatorView();
        this.mainView = new MainView(this);
    }

    public MainController(AuthController authController, CustomerController customerController,
                          RestaurantController restaurantController, DriverController driverController,
                          OrderController orderController, SimulatorController simulatorController,
                          AdminController adminController) {
        this.authController = authController;
        this.customerController = customerController;
        this.restaurantController = restaurantController;
        this.driverController = driverController;
        this.orderController = orderController;
        this.simulatorController = simulatorController;
        this.adminController = adminController;

        this.mapView = new MapView();
        this.orderView = new OrderView();
        this.reportView = new ReportView();
        this.simulatorView = new SimulatorView();
        this.mainView = new MainView(this);
    }

    /**
     * Entrypoint of the Controller-Driven lifecycle.
     */
    public void run() {
        this.start();
    }

    public void start() {
        this.mainView.display();
    }

    public AuthController getAuthController() {
        return this.authController;
    }

    public CustomerController getCustomerController() {
        return this.customerController;
    }

    public RestaurantController getRestaurantController() {
        return this.restaurantController;
    }

    public DriverController getDriverController() {
        return this.driverController;
    }

    public OrderController getOrderController() {
        return this.orderController;
    }

    public SimulatorController getSimulatorController() {
        return this.simulatorController;
    }

    public AdminController getAdminController() {
        return this.adminController;
    }

    public MainView getMainView() {
        return this.mainView;
    }

    public MapView getMapView() {
        return this.mapView;
    }

    public OrderView getOrderView() {
        return this.orderView;
    }

    public ReportView getReportView() {
        return this.reportView;
    }

    public SimulatorView getSimulatorView() {
        return this.simulatorView;
    }

    public boolean login(String username, String password) {
        return this.authController.login(username, password);
    }

    public void logout() {
        this.authController.logout();
    }

    public User getCurrentUser() {
        return this.authController.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return this.authController.isLoggedIn();
    }

    public SimulationRun runBenchmark(LockMechanism mechanism, int numOrders) {
        return this.simulatorController.runSimulation(numOrders, mechanism);
    }

    public List<String> autoDispatchAllPending(LockMechanism mechanism) {
        return this.orderController.autoDispatchAllPending(mechanism);
    }
}
