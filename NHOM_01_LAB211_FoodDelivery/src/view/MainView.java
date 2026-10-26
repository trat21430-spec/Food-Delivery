package view;

import controller.CustomerController;
import controller.RestaurantController;
import controller.DriverController;
import controller.OrderController;
import controller.SimulatorController;
import model.Customer;
import model.Restaurant;
import model.MenuItem;
import model.Driver;
import model.DriverStatus;
import model.Order;
import model.LockMechanism;
import model.SimulationRun;
import util.DataGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainView implements IView {
    private CustomerController customerController;
    private RestaurantController restaurantController;
    private DriverController driverController;
    private OrderController orderController;
    private SimulatorController simulatorController;
    private MapView mapView;
    private SimulatorView simulatorView;
    private OrderView orderView;
    private ReportView reportView;
    private Scanner scanner;

    public MainView() {
        this.customerController = new CustomerController();
        this.restaurantController = new RestaurantController();
        this.driverController = new DriverController();
        this.orderController = new OrderController();
        this.simulatorController = new SimulatorController();
        this.mapView = new MapView();
        this.simulatorView = new SimulatorView();
        this.orderView = new OrderView();
        this.reportView = new ReportView();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void display() {
        boolean running = true;
        while (running) {
            this.printMainMenu();
            System.out.print("Enter your choice [0-9]: ");
            String choice = this.scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    this.handlePlaceOrderFlow();
                    break;
                case "2":
                    this.handleCancelOrderFlow();
                    break;
                case "3":
                    this.handleViewOrdersFlow();
                    break;
                case "4":
                    this.handleRestaurantMenuFlow();
                    break;
                case "5":
                    this.handleDriverManagementFlow();
                    break;
                case "6":
                    this.handleDisplayAsciiMap();
                    break;
                case "7":
                    this.handleRunSimulatorBenchmark();
                    break;
                case "8":
                    this.handleDataGeneration();
                    break;
                case "9":
                    this.handleCrudSubMenu();
                    break;
                case "0":
                    System.out.println("Exiting Food Delivery Network Simulation. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please choose an option from 0 to 9.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=======================================================");
        System.out.println("   FOOD DELIVERY NETWORK SIMULATION - LAB211 (MVC)    ");
        System.out.println("=======================================================");
        System.out.println("1. Place Order & Auto-Dispatch Driver");
        System.out.println("2. Cancel Pending Order");
        System.out.println("3. View Recent Orders");
        System.out.println("4. View Restaurant Menu & Update Stock");
        System.out.println("5. Driver Status Management (Online/Offline)");
        System.out.println("6. Display Network ASCII Grid Map");
        System.out.println("7. Run Simulator Benchmark (Compare 4 Lock Mechanisms)");
        System.out.println("8. Generate Physical CSV Dataset (>= 18,000 Rows)");
        System.out.println("9. Full CRUD Management (Restaurant, MenuItem, Driver, Order)");
        System.out.println("0. Exit Application");
        System.out.println("=======================================================");
    }

    private void handlePlaceOrderFlow() {
        System.out.println("\n--- PLACE ORDER & DISPATCH ---");
        List<Customer> customers = this.customerController.getAllCustomers();
        List<Restaurant> restaurants = this.restaurantController.getAllRestaurants();

        if (customers.isEmpty() || restaurants.isEmpty()) {
            System.out.println("Data not found! Please run option 8 to generate dataset first.");
            return;
        }

        Customer cust = customers.get(0);
        Restaurant rest = restaurants.get(0);
        List<MenuItem> menu = this.restaurantController.getMenuByRestaurant(rest.getId());

        if (menu.isEmpty()) {
            System.out.println("No menu items available for restaurant " + rest.getName());
            return;
        }

        this.orderView.renderMenu(menu);
        System.out.println("Select a menu item:");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            System.out.printf("%d. %s (Stock: %d)%n", i + 1, item.getName(), item.getStockQty());
        }

        MenuItem chosenItem = null;
        while (chosenItem == null) {
            System.out.print("Enter item number [1-" + menu.size() + "] (0 to cancel): ");
            String itemChoice = this.scanner.nextLine().trim();
            try {
                int itemIndex = Integer.parseInt(itemChoice);
                if (itemIndex == 0) {
                    System.out.println("Order cancelled.");
                    return;
                }
                if (itemIndex >= 1 && itemIndex <= menu.size()) {
                    chosenItem = menu.get(itemIndex - 1);
                } else {
                    System.out.println("Invalid item number. Choose a number shown in the menu.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        System.out.println("Selected Customer: " + cust.getName() + " (" + cust.getId() + ")");
        System.out.println("Selected Restaurant: " + rest.getName() + " (" + rest.getId() + ")");
        System.out.println("Selected Item: " + chosenItem.getName() + " [Stock: " + chosenItem.getStockQty() + "]");
        System.out.print("Enter quantity to order (default 1): ");
        String qStr = this.scanner.nextLine().trim();
        int qty = 1;
        try {
            if (!qStr.isEmpty()) {
                qty = Integer.parseInt(qStr);
            }
        } catch (NumberFormatException ignored) {}

        System.out.println("Select Synchronization Mechanism:");
        System.out.println("1. NO_LOCK (Baseline, Race condition prone)");
        System.out.println("2. SYNCHRONIZED (Fine-grained resource locks)");
        System.out.println("3. OPTIMISTIC (Version-based validation)");
        System.out.println("4. FILE_LOCK (FileChannel physical locks)");
        System.out.print("Choice [1-4]: ");
        String mChoice = this.scanner.nextLine().trim();
        LockMechanism mech = LockMechanism.SYNCHRONIZED;
        if (mChoice.equals("1")) mech = LockMechanism.NO_LOCK;
        else if (mChoice.equals("3")) mech = LockMechanism.OPTIMISTIC;
        else if (mChoice.equals("4")) mech = LockMechanism.FILE_LOCK;

        try {
            Order order = this.orderController.placeOrderAndDispatch(cust.getId(), rest.getId(), chosenItem.getId(), qty, mech);
            System.out.println(">>> Order placed successfully! Order ID: " + order.getId() + ", Status: " + order.getStatus() + ", Assigned Driver: " + order.getDriverId());
        } catch (Exception e) {
            System.out.println(">>> Order processing failed: " + e.getMessage());
        }
    }

    private void handleCancelOrderFlow() {
        System.out.print("\nEnter Order ID to cancel: ");
        String orderId = this.scanner.nextLine().trim();
        try {
            boolean success = this.orderController.cancelPendingOrder(orderId);
            if (success) {
                System.out.println("Order " + orderId + " has been successfully cancelled.");
            }
        } catch (Exception e) {
            System.out.println("Cancel failed: " + e.getMessage());
        }
    }

    private void handleViewOrdersFlow() {
        List<Order> orders = this.orderController.getAllOrders();
        this.orderView.renderOrders(orders);
    }

    private void handleRestaurantMenuFlow() {
        List<Restaurant> rests = this.restaurantController.getAllRestaurants();
        if (rests.isEmpty()) {
            System.out.println("No restaurants available.");
            return;
        }
        Restaurant r = rests.get(0);
        System.out.println("\nShowing menu for: " + r.getName() + " (" + r.getAddress() + ")");
        List<MenuItem> items = this.restaurantController.getMenuByRestaurant(r.getId());
        this.orderView.renderMenu(items);

        System.out.print("Do you want to update stock for an item? (y/n): ");
        String ans = this.scanner.nextLine().trim();
        if (ans.equalsIgnoreCase("y") && !items.isEmpty()) {
            MenuItem target = items.get(0);
            System.out.print("Enter new stock quantity for " + target.getName() + ": ");
            try {
                int newStock = Integer.parseInt(this.scanner.nextLine().trim());
                this.restaurantController.updateStock(target.getId(), newStock);
                System.out.println("Stock updated to " + newStock);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }

    private void handleDriverManagementFlow() {
        List<Driver> drivers = this.driverController.getAllDrivers();
        System.out.println("\nTotal drivers in network: " + drivers.size());
        List<Driver> available = this.driverController.getAvailableDrivers();
        System.out.println("Available drivers count: " + available.size());

        if (!drivers.isEmpty()) {
            Driver d = drivers.get(0);
            System.out.println("Sample Driver: " + d.getName() + " [" + d.getStatus() + "] Current Order: " + d.getCurrentOrderId());
            System.out.print("Toggle status between AVAILABLE and OFFLINE? (y/n): ");
            String ans = this.scanner.nextLine().trim();
            if (ans.equalsIgnoreCase("y")) {
                DriverStatus next = (d.getStatus() == DriverStatus.AVAILABLE) ? DriverStatus.OFFLINE : DriverStatus.AVAILABLE;
                this.driverController.updateDriverStatus(d.getId(), next);
                System.out.println("Driver status changed to " + next);
            }
        }
    }

    private void handleDisplayAsciiMap() {
        List<Restaurant> restaurants = this.restaurantController.getAllRestaurants();
        List<Customer> customers = this.customerController.getAllCustomers();
        List<Driver> drivers = this.driverController.getAllDrivers();
        this.mapView.renderAsciiMap(restaurants, customers, drivers);
    }

    private void handleRunSimulatorBenchmark() {
        System.out.println("\n=======================================================");
        System.out.println("  RUNNING RESEARCH CONCURRENCY SIMULATOR BENCHMARK     ");
        System.out.println("=======================================================");
        System.out.print("Enter number of concurrent orders to simulate per mechanism (e.g. 500 or 1000): ");
        String countStr = this.scanner.nextLine().trim();
        int ordersCount = 500;
        try {
            if (!countStr.isEmpty()) {
                ordersCount = Integer.parseInt(countStr);
            }
        } catch (NumberFormatException ignored) {}

        List<SimulationRun> runs = new ArrayList<>();
        LockMechanism[] mechanisms = new LockMechanism[] {
            LockMechanism.NO_LOCK,
            LockMechanism.SYNCHRONIZED,
            LockMechanism.OPTIMISTIC,
            LockMechanism.FILE_LOCK
        };

        for (int i = 0; i < mechanisms.length; i++) {
            LockMechanism mech = mechanisms[i];
            System.out.println("\nExecuting simulation with mechanism: " + mech.name() + "...");
            SimulationRun run = this.simulatorController.runSimulation(ordersCount, mech);
            if (run != null) {
                runs.add(run);
                this.reportView.renderSimulationSummary(run);
            }
        }

        this.simulatorView.renderComparisonTable(runs);
    }

    private void handleDataGeneration() {
        System.out.println("\nGenerating fresh physical CSV dataset (>= 18,000 rows)...");
        DataGenerator generator = new DataGenerator();
        generator.generateAllData();
        System.out.println("Data generation completed successfully!");
    }

    // --- FULL CRUD SUBMENU ---

    private void handleCrudSubMenu() {
        boolean inCrud = true;
        while (inCrud) {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("      FULL CRUD MANAGEMENT (RUBRIC CRITERION 3)        ");
            System.out.println("-------------------------------------------------------");
            System.out.println("1. Create New Restaurant");
            System.out.println("2. Read / Find Restaurant by ID");
            System.out.println("3. Update Restaurant Information");
            System.out.println("4. Delete Restaurant by ID");
            System.out.println("5. Create New MenuItem for Restaurant");
            System.out.println("6. Read / Find MenuItem by ID");
            System.out.println("7. Update MenuItem (Name, Price, Stock)");
            System.out.println("8. Delete MenuItem by ID");
            System.out.println("9. Create New Driver");
            System.out.println("10. Read / Find Driver by ID");
            System.out.println("11. Update Driver Information / Status");
            System.out.println("12. Delete Driver by ID");
            System.out.println("13. Delete Order by ID");
            System.out.println("0. Return to Main Menu");
            System.out.println("-------------------------------------------------------");
            System.out.print("Select CRUD action [0-13]: ");
            String crudChoice = this.scanner.nextLine().trim();

            switch (crudChoice) {
                case "1":
                    System.out.print("Enter restaurant name: ");
                    String rName = this.scanner.nextLine().trim();
                    System.out.print("Enter restaurant address: ");
                    String rAddr = this.scanner.nextLine().trim();
                    Restaurant createdR = this.restaurantController.createRestaurant(rName, rAddr, 21.03, 105.84, 4.8);
                    System.out.println(">>> [CREATE] Restaurant created with ID: " + createdR.getId());
                    break;
                case "2":
                    System.out.print("Enter restaurant ID to read: ");
                    String findRId = this.scanner.nextLine().trim();
                    Restaurant foundR = this.restaurantController.getRestaurantById(findRId);
                    if (foundR != null) {
                        System.out.println(">>> [READ] Found: " + foundR.getName() + " | Address: " + foundR.getAddress() + " | Rating: " + foundR.getRating());
                    } else {
                        System.out.println("Restaurant not found.");
                    }
                    break;
                case "3":
                    System.out.print("Enter restaurant ID to update: ");
                    String upRId = this.scanner.nextLine().trim();
                    System.out.print("Enter new name: ");
                    String newRName = this.scanner.nextLine().trim();
                    System.out.print("Enter new address: ");
                    String newRAddr = this.scanner.nextLine().trim();
                    boolean upRSuccess = this.restaurantController.updateRestaurant(upRId, newRName, newRAddr, 4.9);
                    System.out.println(upRSuccess ? ">>> [UPDATE] Restaurant updated." : "Failed to update restaurant.");
                    break;
                case "4":
                    System.out.print("Enter restaurant ID to delete: ");
                    String delRId = this.scanner.nextLine().trim();
                    boolean delRSuccess = this.restaurantController.deleteRestaurant(delRId);
                    System.out.println(delRSuccess ? ">>> [DELETE] Restaurant deleted from CSV." : "Restaurant not found.");
                    break;
                case "5":
                    System.out.print("Enter restaurant ID: ");
                    String restIdForMenu = this.scanner.nextLine().trim();
                    System.out.print("Enter item name: ");
                    String mName = this.scanner.nextLine().trim();
                    System.out.print("Enter item price: ");
                    double mPrice = Double.parseDouble(this.scanner.nextLine().trim());
                    System.out.print("Enter item stock: ");
                    int mStock = Integer.parseInt(this.scanner.nextLine().trim());
                    MenuItem createdM = this.restaurantController.createMenuItem(restIdForMenu, mName, mPrice, mStock);
                    System.out.println(">>> [CREATE] MenuItem created with ID: " + createdM.getId());
                    break;
                case "6":
                    System.out.print("Enter item ID to read: ");
                    String mIdFind = this.scanner.nextLine().trim();
                    MenuItem foundM = this.restaurantController.getMenuItemById(mIdFind);
                    if (foundM != null) {
                        System.out.println(">>> [READ] Found: " + foundM.getName() + " | Price: $" + foundM.getPrice() + " | Stock: " + foundM.getStockQty());
                    } else {
                        System.out.println("Item not found.");
                    }
                    break;
                case "7":
                    System.out.print("Enter item ID to update: ");
                    String upMId = this.scanner.nextLine().trim();
                    System.out.print("Enter new item name: ");
                    String upMName = this.scanner.nextLine().trim();
                    System.out.print("Enter new price: ");
                    double upMPrice = Double.parseDouble(this.scanner.nextLine().trim());
                    System.out.print("Enter new stock quantity: ");
                    int upMStock = Integer.parseInt(this.scanner.nextLine().trim());
                    boolean upMSuccess = this.restaurantController.updateMenuItem(upMId, upMName, upMPrice, upMStock);
                    System.out.println(upMSuccess ? ">>> [UPDATE] MenuItem updated." : "Item not found.");
                    break;
                case "8":
                    System.out.print("Enter item ID to delete: ");
                    String delMId = this.scanner.nextLine().trim();
                    boolean delMSuccess = this.restaurantController.deleteMenuItem(delMId);
                    System.out.println(delMSuccess ? ">>> [DELETE] MenuItem deleted from CSV." : "Item not found.");
                    break;
                case "9":
                    System.out.print("Enter driver name: ");
                    String dName = this.scanner.nextLine().trim();
                    System.out.print("Enter driver phone: ");
                    String dPhone = this.scanner.nextLine().trim();
                    Driver createdD = this.driverController.createDriver(dName, dPhone, 21.025, 105.850);
                    System.out.println(">>> [CREATE] Driver registered with ID: " + createdD.getId());
                    break;
                case "10":
                    System.out.print("Enter driver ID to read: ");
                    String dIdFind = this.scanner.nextLine().trim();
                    Driver foundD = this.driverController.getDriverById(dIdFind);
                    if (foundD != null) {
                        System.out.println(">>> [READ] Driver: " + foundD.getName() + " | Phone: " + foundD.getPhone() + " | Status: " + foundD.getStatus());
                    } else {
                        System.out.println("Driver not found.");
                    }
                    break;
                case "11":
                    System.out.print("Enter driver ID to update: ");
                    String upDId = this.scanner.nextLine().trim();
                    System.out.print("Enter new name: ");
                    String upDName = this.scanner.nextLine().trim();
                    System.out.print("Enter new phone: ");
                    String upDPhone = this.scanner.nextLine().trim();
                    boolean upDSuccess = this.driverController.updateDriverInfo(upDId, upDName, upDPhone, 21.026, 105.852);
                    System.out.println(upDSuccess ? ">>> [UPDATE] Driver updated." : "Driver not found.");
                    break;
                case "12":
                    System.out.print("Enter driver ID to delete: ");
                    String delDId = this.scanner.nextLine().trim();
                    boolean delDSuccess = this.driverController.deleteDriver(delDId);
                    System.out.println(delDSuccess ? ">>> [DELETE] Driver deleted from CSV." : "Driver not found.");
                    break;
                case "13":
                    System.out.print("Enter order ID to delete: ");
                    String delOId = this.scanner.nextLine().trim();
                    boolean delOSuccess = this.orderController.deleteOrder(delOId);
                    System.out.println(delOSuccess ? ">>> [DELETE] Order deleted from CSV." : "Order not found.");
                    break;
                case "0":
                    inCrud = false;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
    }
}
