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
<<<<<<< HEAD
import model.LockMechanism;
import model.SimulationRun;
import util.DataGenerator;

import java.util.ArrayList;
import java.util.List;
=======
import model.OrderItem;
import model.OrderStatus;
import model.LockMechanism;
import model.SimulationRun;
import model.GeoUtils;
import util.DataGenerator;
import util.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
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
<<<<<<< HEAD
=======
    private TextUtil textUtil;
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9

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
<<<<<<< HEAD
=======
        this.textUtil = new TextUtil();
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
    }

    @Override
    public void display() {
        boolean running = true;
        while (running) {
            this.printMainMenu();
<<<<<<< HEAD
            System.out.print("Enter your choice [0-9]: ");
=======
            System.out.print("Enter your choice [0-10]: ");
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
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
<<<<<<< HEAD
=======
                case "10":
                    this.handleSystemReportsFlow();
                    break;
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
                case "0":
                    System.out.println("Exiting Food Delivery Network Simulation. Goodbye!");
                    running = false;
                    break;
                default:
<<<<<<< HEAD
                    System.out.println("Invalid selection. Please choose an option from 0 to 9.");
=======
                    System.out.println("Invalid selection. Please choose an option from 0 to 10.");
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=======================================================");
        System.out.println("   FOOD DELIVERY NETWORK SIMULATION - LAB211 (MVC)    ");
        System.out.println("=======================================================");
<<<<<<< HEAD
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
=======
        System.out.println(" 1. Place Order & Auto-Dispatch Driver");
        System.out.println(" 2. Cancel Pending Order");
        System.out.println(" 3. View Recent Orders & Order Receipts");
        System.out.println(" 4. View Restaurant Menu & Update Stock");
        System.out.println(" 5. Driver Fleet Status & Management (Online/Offline)");
        System.out.println(" 6. Display Network ASCII Grid Map & Delivery Routes");
        System.out.println(" 7. Run Simulator Benchmark (Compare 4 Lock Mechanisms)");
        System.out.println(" 8. Generate Physical CSV Dataset (>= 18,000 Rows)");
        System.out.println(" 9. Full CRUD Management (Restaurant, MenuItem, Driver, Order)");
        System.out.println("10. System Reports & Research Analytics");
        System.out.println(" 0. Exit Application");
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
        System.out.println("=======================================================");
    }

    private void handlePlaceOrderFlow() {
<<<<<<< HEAD
        System.out.println("\n--- PLACE ORDER & DISPATCH ---");
=======
        System.out.println("\n-------------------------------------------------------");
        System.out.println("       PLACE ORDER & CONCURRENT DISPATCH FLOW          ");
        System.out.println("-------------------------------------------------------");
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
        List<Customer> customers = this.customerController.getAllCustomers();
        List<Restaurant> restaurants = this.restaurantController.getAllRestaurants();

        if (customers.isEmpty() || restaurants.isEmpty()) {
<<<<<<< HEAD
            System.out.println("Data not found! Please run option 8 to generate dataset first.");
            return;
        }

        Customer cust = customers.get(0);
        Restaurant rest = restaurants.get(0);
        List<MenuItem> menu = this.restaurantController.getMenuByRestaurant(rest.getId());

=======
            System.out.println("Dataset missing! Please run option 8 to generate CSV data first.");
            return;
        }

        // Customer selection
        Customer cust = customers.get(0);
        System.out.printf(Locale.US, "Default Customer: %s (%s) - %s%n", cust.getName(), cust.getId(), cust.getAddress());
        System.out.print("Enter Customer ID to use (press Enter for default [" + cust.getId() + "]): ");
        String custInput = this.scanner.nextLine().trim();
        if (!custInput.isEmpty()) {
            Customer customCust = this.customerController.getCustomerById(custInput);
            if (customCust != null) {
                cust = customCust;
            } else {
                System.out.println("Customer [" + custInput + "] not found. Using default [" + cust.getId() + "].");
            }
        }

        // Restaurant selection
        Restaurant rest = restaurants.get(0);
        System.out.printf(Locale.US, "Default Restaurant: %s (%s) - Rating: %.1f%n", rest.getName(), rest.getId(), rest.getRating());
        System.out.print("Enter Restaurant ID to use (press Enter for default [" + rest.getId() + "]): ");
        String restInput = this.scanner.nextLine().trim();
        if (!restInput.isEmpty()) {
            Restaurant customRest = this.restaurantController.getRestaurantById(restInput);
            if (customRest != null) {
                rest = customRest;
            } else {
                System.out.println("Restaurant [" + restInput + "] not found. Using default [" + rest.getId() + "].");
            }
        }

        // Menu selection
        List<MenuItem> menu = this.restaurantController.getMenuByRestaurant(rest.getId());
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
        if (menu.isEmpty()) {
            System.out.println("No menu items available for restaurant " + rest.getName());
            return;
        }

<<<<<<< HEAD
        this.orderView.renderMenu(menu);
        MenuItem chosenItem = menu.get(0);

        System.out.println("Selected Customer: " + cust.getName() + " (" + cust.getId() + ")");
        System.out.println("Selected Restaurant: " + rest.getName() + " (" + rest.getId() + ")");
        System.out.println("Selected Item: " + chosenItem.getName() + " [Stock: " + chosenItem.getStockQty() + "]");
=======
        System.out.println("\nMenu for " + rest.getName() + ":");
        this.orderView.renderMenu(menu);
        System.out.println("Select a menu item:");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            System.out.printf("%d. [%s] %s (Price: $%.2f, Stock: %d)%n", i + 1, item.getId(), item.getName(), item.getPrice(), item.getStockQty());
        }

        MenuItem chosenItem = menu.get(0);
        System.out.print("Enter item number [1-" + menu.size() + "] or Item ID (press Enter for first [" + chosenItem.getId() + "], 0 to cancel): ");
        String itemChoice = this.scanner.nextLine().trim();
        if (itemChoice.equals("0")) {
            System.out.println("Order cancelled.");
            return;
        } else if (!itemChoice.isEmpty()) {
            try {
                int itemIndex = Integer.parseInt(itemChoice);
                if (itemIndex >= 1 && itemIndex <= menu.size()) {
                    chosenItem = menu.get(itemIndex - 1);
                } else {
                    System.out.println("Invalid number. Using default item [" + chosenItem.getId() + "].");
                }
            } catch (NumberFormatException e) {
                MenuItem customItem = this.restaurantController.getMenuItemById(itemChoice);
                if (customItem != null) {
                    chosenItem = customItem;
                } else {
                    System.out.println("Item [" + itemChoice + "] not found in catalog. Using [" + chosenItem.getId() + "].");
                }
            }
        }

>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
        System.out.print("Enter quantity to order (default 1): ");
        String qStr = this.scanner.nextLine().trim();
        int qty = 1;
        try {
            if (!qStr.isEmpty()) {
<<<<<<< HEAD
                qty = Integer.parseInt(qStr);
            }
        } catch (NumberFormatException ignored) {}

        System.out.println("Select Synchronization Mechanism:");
        System.out.println("1. NO_LOCK (Baseline, Race condition prone)");
        System.out.println("2. SYNCHRONIZED (Fine-grained resource locks)");
=======
                qty = Math.max(1, Integer.parseInt(qStr));
            }
        } catch (NumberFormatException ignored) {}

        System.out.println("\nSelect Synchronization Mechanism:");
        System.out.println("1. NO_LOCK (Baseline, Race condition prone)");
        System.out.println("2. SYNCHRONIZED (Fine-grained resource locks - Default)");
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
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
<<<<<<< HEAD
            System.out.println(">>> Order placed successfully! Order ID: " + order.getId() + ", Status: " + order.getStatus() + ", Assigned Driver: " + order.getDriverId());
=======
            System.out.println("\n>>> ORDER PLACED SUCCESSFULLY! Status: " + order.getStatus() + ", Driver: " + order.getDriverId());

            List<OrderItem> orderItems = this.orderController.getOrderItemsByOrderId(order.getId());
            this.orderView.renderOrderDetail(order, orderItems);

            if (order.getDriverId() != null && !order.getDriverId().isEmpty()) {
                Driver driver = this.driverController.getDriverById(order.getDriverId());
                if (driver != null) {
                    System.out.print("Would you like to visualize this delivery route on the ASCII map? (y/n): ");
                    String visAns = this.scanner.nextLine().trim();
                    if (visAns.equalsIgnoreCase("y")) {
                        this.mapView.renderDeliveryRoute(rest, cust, driver);
                    }
                }
            }
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
        } catch (Exception e) {
            System.out.println(">>> Order processing failed: " + e.getMessage());
        }
    }

    private void handleCancelOrderFlow() {
<<<<<<< HEAD
        System.out.print("\nEnter Order ID to cancel: ");
        String orderId = this.scanner.nextLine().trim();
        try {
            boolean success = this.orderController.cancelPendingOrder(orderId);
            if (success) {
                System.out.println("Order " + orderId + " has been successfully cancelled.");
            }
        } catch (Exception e) {
            System.out.println("Cancel failed: " + e.getMessage());
=======
        System.out.println("\n-------------------------------------------------------");
        System.out.println("                CANCEL PENDING ORDER                   ");
        System.out.println("-------------------------------------------------------");

        List<Order> allOrders = this.orderController.getAllOrders();
        List<Order> pendingOrders = new ArrayList<>();
        for (int i = 0; i < allOrders.size(); i++) {
            Order o = allOrders.get(i);
            if (o.getStatus() == OrderStatus.PENDING) {
                pendingOrders.add(o);
            }
        }

        if (pendingOrders.isEmpty()) {
            System.out.println("No PENDING orders found in the system to cancel.");
        } else {
            System.out.println("Current PENDING Orders in Queue:");
            int showLimit = Math.min(pendingOrders.size(), 5);
            for (int i = 0; i < showLimit; i++) {
                Order p = pendingOrders.get(i);
                System.out.printf(Locale.US, "  [%d] ID: %s | Customer: %s | Restaurant: %s | Amount: $%.2f%n",
                        i + 1, p.getId(), p.getCustomerId(), p.getRestaurantId(), p.getTotalAmount());
            }
        }

        System.out.print("\nEnter Order ID to cancel (or press Enter to return): ");
        String orderId = this.scanner.nextLine().trim();
        if (orderId.isEmpty()) {
            return;
        }

        try {
            boolean success = this.orderController.cancelPendingOrder(orderId);
            if (success) {
                System.out.println(">>> Order [" + orderId + "] has been successfully CANCELLED in CSV storage.");
            }
        } catch (Exception e) {
            System.out.println(">>> Cancel failed: " + e.getMessage());
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
        }
    }

    private void handleViewOrdersFlow() {
        List<Order> orders = this.orderController.getAllOrders();
        this.orderView.renderOrders(orders);
<<<<<<< HEAD
=======

        if (!orders.isEmpty()) {
            System.out.print("Enter Order ID to inspect detailed receipt (or press Enter to return): ");
            String orderId = this.scanner.nextLine().trim();
            if (!orderId.isEmpty()) {
                Order target = this.orderController.getOrderById(orderId);
                if (target != null) {
                    List<OrderItem> items = this.orderController.getOrderItemsByOrderId(target.getId());
                    this.orderView.renderOrderDetail(target, items);
                } else {
                    System.out.println("Order [" + orderId + "] not found.");
                }
            }
        }
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
    }

    private void handleRestaurantMenuFlow() {
        List<Restaurant> rests = this.restaurantController.getAllRestaurants();
        if (rests.isEmpty()) {
            System.out.println("No restaurants available.");
            return;
        }
<<<<<<< HEAD
        Restaurant r = rests.get(0);
=======

        Restaurant r = rests.get(0);
        System.out.printf(Locale.US, "\nDefault Restaurant: %s (%s) - %s%n", r.getName(), r.getId(), r.getAddress());
        System.out.print("Enter Restaurant ID to inspect (press Enter for default [" + r.getId() + "], 'L' to list): ");
        String rInput = this.scanner.nextLine().trim();

        if (rInput.equalsIgnoreCase("l")) {
            System.out.println("\nActive Restaurants Catalog:");
            int limit = Math.min(rests.size(), 10);
            for (int i = 0; i < limit; i++) {
                Restaurant curr = rests.get(i);
                System.out.printf(Locale.US, "  [%s] %-26s | %-28s | Rating: %.1f%n",
                        curr.getId(), this.textUtil.truncate(curr.getName(), 26),
                        this.textUtil.truncate(curr.getAddress(), 28), curr.getRating());
            }
            System.out.print("Select Restaurant ID: ");
            rInput = this.scanner.nextLine().trim();
        }

        if (!rInput.isEmpty()) {
            Restaurant found = this.restaurantController.getRestaurantById(rInput);
            if (found != null) {
                r = found;
            } else {
                System.out.println("Restaurant not found. Using default [" + r.getId() + "].");
            }
        }

>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
        System.out.println("\nShowing menu for: " + r.getName() + " (" + r.getAddress() + ")");
        List<MenuItem> items = this.restaurantController.getMenuByRestaurant(r.getId());
        this.orderView.renderMenu(items);

        System.out.print("Do you want to update stock for an item? (y/n): ");
        String ans = this.scanner.nextLine().trim();
        if (ans.equalsIgnoreCase("y") && !items.isEmpty()) {
            MenuItem target = items.get(0);
<<<<<<< HEAD
=======
            System.out.print("Enter MenuItem ID to update (press Enter for [" + target.getId() + "]): ");
            String mInput = this.scanner.nextLine().trim();
            if (!mInput.isEmpty()) {
                MenuItem foundItem = this.restaurantController.getMenuItemById(mInput);
                if (foundItem != null) {
                    target = foundItem;
                }
            }

>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
            System.out.print("Enter new stock quantity for " + target.getName() + ": ");
            try {
                int newStock = Integer.parseInt(this.scanner.nextLine().trim());
                this.restaurantController.updateStock(target.getId(), newStock);
<<<<<<< HEAD
                System.out.println("Stock updated to " + newStock);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
=======
                System.out.println(">>> Stock updated to " + newStock + " for item [" + target.getId() + "].");
            } catch (NumberFormatException e) {
                System.out.println("Invalid stock number.");
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
            }
        }
    }

    private void handleDriverManagementFlow() {
        List<Driver> drivers = this.driverController.getAllDrivers();
<<<<<<< HEAD
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
=======
        int total = drivers.size();
        int available = 0;
        int busy = 0;
        int offline = 0;

        for (int i = 0; i < drivers.size(); i++) {
            Driver d = drivers.get(i);
            if (d.getStatus() == DriverStatus.AVAILABLE) available++;
            else if (d.getStatus() == DriverStatus.BUSY) busy++;
            else offline++;
        }

        System.out.println("\n=======================================================");
        System.out.println("             DRIVER FLEET MANAGEMENT STATUS            ");
        System.out.println("=======================================================");
        System.out.printf(Locale.US, "  Total Drivers Registered : %,d%n", total);
        System.out.printf(Locale.US, "  [AVAILABLE] Free to pick : %,d%n", available);
        System.out.printf(Locale.US, "  [BUSY] Delivering orders : %,d%n", busy);
        System.out.printf(Locale.US, "  [OFFLINE] Inactive fleet : %,d%n", offline);
        System.out.println("=======================================================");

        if (!drivers.isEmpty()) {
            int showCount = Math.min(drivers.size(), 8);
            System.out.println("Sample Driver Fleet Roster:");
            for (int i = 0; i < showCount; i++) {
                Driver d = drivers.get(i);
                String curr = (d.getCurrentOrderId() == null || d.getCurrentOrderId().isEmpty()) ? "NONE" : d.getCurrentOrderId();
                System.out.printf(Locale.US, "  [%s] %-18s | Phone: %-12s | Status: %-10s | Order: %s%n",
                        d.getId(), this.textUtil.truncate(d.getName(), 18), d.getPhone(), d.getStatus(), curr);
            }

            System.out.print("\nEnter Driver ID to toggle status AVAILABLE <-> OFFLINE (or press Enter to return): ");
            String dId = this.scanner.nextLine().trim();
            if (!dId.isEmpty()) {
                Driver target = this.driverController.getDriverById(dId);
                if (target != null) {
                    DriverStatus next = (target.getStatus() == DriverStatus.AVAILABLE) ? DriverStatus.OFFLINE : DriverStatus.AVAILABLE;
                    this.driverController.updateDriverStatus(target.getId(), next);
                    System.out.println(">>> Driver [" + target.getId() + "] status changed to " + next);
                } else {
                    System.out.println("Driver [" + dId + "] not found.");
                }
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
            }
        }
    }

    private void handleDisplayAsciiMap() {
<<<<<<< HEAD
        List<Restaurant> restaurants = this.restaurantController.getAllRestaurants();
        List<Customer> customers = this.customerController.getAllCustomers();
        List<Driver> drivers = this.driverController.getAllDrivers();
        this.mapView.renderAsciiMap(restaurants, customers, drivers);
=======
        boolean inMapMenu = true;
        while (inMapMenu) {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("             NETWORK ASCII MAP & ROUTING               ");
            System.out.println("-------------------------------------------------------");
            System.out.println("1. Render Full Network Grid (Restaurants, Customers, Drivers)");
            System.out.println("2. Visualize Active Delivery Route for an Order");
            System.out.printf(Locale.US, "3. Adjust Map Grid Dimensions (Current: %d rows x %d cols)%n",
                    this.mapView.getRows(), this.mapView.getCols());
            System.out.println("0. Return to Main Menu");
            System.out.println("-------------------------------------------------------");
            System.out.print("Select Map Option [0-3]: ");
            String mapChoice = this.scanner.nextLine().trim();

            switch (mapChoice) {
                case "1": {
                    List<Restaurant> restaurants = this.restaurantController.getAllRestaurants();
                    List<Customer> customers = this.customerController.getAllCustomers();
                    List<Driver> drivers = this.driverController.getAllDrivers();
                    this.mapView.renderAsciiMap(restaurants, customers, drivers);
                    break;
                }
                case "2": {
                    List<Order> orders = this.orderController.getAllOrders();
                    Order targetOrder = null;
                    for (int i = 0; i < orders.size(); i++) {
                        Order o = orders.get(i);
                        if (o.getDriverId() != null && !o.getDriverId().isEmpty()) {
                            targetOrder = o;
                            break;
                        }
                    }

                    if (targetOrder == null) {
                        System.out.println("No dispatched orders with assigned drivers found to visualize.");
                        break;
                    }

                    System.out.printf(Locale.US, "Sample Order found: %s (Driver: %s, Rest: %s, Cust: %s)%n",
                            targetOrder.getId(), targetOrder.getDriverId(), targetOrder.getRestaurantId(), targetOrder.getCustomerId());
                    System.out.print("Enter Order ID to trace (press Enter for default [" + targetOrder.getId() + "]): ");
                    String oInput = this.scanner.nextLine().trim();
                    if (!oInput.isEmpty()) {
                        Order custom = this.orderController.getOrderById(oInput);
                        if (custom != null) {
                            targetOrder = custom;
                        }
                    }

                    Restaurant r = this.restaurantController.getRestaurantById(targetOrder.getRestaurantId());
                    Customer c = this.customerController.getCustomerById(targetOrder.getCustomerId());
                    Driver d = this.driverController.getDriverById(targetOrder.getDriverId());
                    if (r != null && c != null && d != null) {
                        this.mapView.renderDeliveryRoute(r, c, d);
                    } else {
                        System.out.println("Could not resolve restaurant, customer, or driver for order " + targetOrder.getId());
                    }
                    break;
                }
                case "3": {
                    System.out.print("Enter new rows count (10-30, default 15): ");
                    try {
                        int rNum = Integer.parseInt(this.scanner.nextLine().trim());
                        this.mapView.setRows(Math.max(10, Math.min(30, rNum)));
                    } catch (NumberFormatException ignored) {}

                    System.out.print("Enter new columns count (30-80, default 45): ");
                    try {
                        int cNum = Integer.parseInt(this.scanner.nextLine().trim());
                        this.mapView.setCols(Math.max(30, Math.min(80, cNum)));
                    } catch (NumberFormatException ignored) {}

                    System.out.printf(Locale.US, "Grid adjusted to %d rows x %d cols.%n",
                            this.mapView.getRows(), this.mapView.getCols());
                    break;
                }
                case "0":
                    inMapMenu = false;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
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
<<<<<<< HEAD
                ordersCount = Integer.parseInt(countStr);
=======
                ordersCount = Math.max(10, Integer.parseInt(countStr));
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
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
<<<<<<< HEAD
            System.out.println("\nExecuting simulation with mechanism: " + mech.name() + "...");
=======
            System.out.println("\n>>> Executing simulation with mechanism: " + mech.name() + " (" + ordersCount + " orders)...");
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
            SimulationRun run = this.simulatorController.runSimulation(ordersCount, mech);
            if (run != null) {
                runs.add(run);
                this.reportView.renderSimulationSummary(run);
            }
        }

<<<<<<< HEAD
        this.simulatorView.renderComparisonTable(runs);
=======
        // Render full benchmark suite (Table, Bar Chart, Anomalies, Conclusions)
        this.simulatorView.renderFullBenchmarkReport(runs);
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
    }

    private void handleDataGeneration() {
        System.out.println("\nGenerating fresh physical CSV dataset (>= 18,000 rows)...");
        DataGenerator generator = new DataGenerator();
        generator.generateAllData();
        System.out.println("Data generation completed successfully!");
    }

<<<<<<< HEAD
=======
    private void handleSystemReportsFlow() {
        boolean inReportMenu = true;
        while (inReportMenu) {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("        SYSTEM REPORTS & RESEARCH ANALYTICS            ");
            System.out.println("-------------------------------------------------------");
            System.out.println("1. Complete System Network & Order Audit Report");
            System.out.println("2. Haversine Distance Formula Geodesic Verification");
            System.out.println("3. Physical CSV Storage Anomaly Audit");
            System.out.println("4. Research Big Question Theoretical Summary");
            System.out.println("5. Historical Simulation Runs Log (simulation_runs.csv)");
            System.out.println("0. Return to Main Menu");
            System.out.println("-------------------------------------------------------");
            System.out.print("Select Report Option [0-5]: ");
            String repChoice = this.scanner.nextLine().trim();

            switch (repChoice) {
                case "1": {
                    List<Customer> custs = this.customerController.getAllCustomers();
                    List<Restaurant> rests = this.restaurantController.getAllRestaurants();
                    List<Driver> drivers = this.driverController.getAllDrivers();
                    List<Order> orders = this.orderController.getAllOrders();

                    int avail = 0;
                    int busy = 0;
                    int offline = 0;
                    for (int i = 0; i < drivers.size(); i++) {
                        Driver d = drivers.get(i);
                        if (d.getStatus() == DriverStatus.AVAILABLE) avail++;
                        else if (d.getStatus() == DriverStatus.BUSY) busy++;
                        else offline++;
                    }

                    int pending = 0;
                    int completed = 0;
                    int cancelled = 0;
                    double totalRev = 0.0;
                    for (int i = 0; i < orders.size(); i++) {
                        Order o = orders.get(i);
                        if (o.getStatus() == OrderStatus.PENDING) pending++;
                        else if (o.getStatus() == OrderStatus.DELIVERED) completed++;
                        else if (o.getStatus() == OrderStatus.CANCELLED) cancelled++;
                        totalRev += o.getTotalAmount();
                    }

                    int menuItemCount = rests.size() * 10;
                    this.reportView.renderSystemOverview(
                            custs.size(), rests.size(), menuItemCount, drivers.size(),
                            orders.size(), totalRev,
                            avail, busy, offline,
                            pending, completed, cancelled
                    );
                    break;
                }
                case "2": {
                    GeoUtils geo = new GeoUtils();
                    // Hanoi coordinates: 21.0285 N, 105.8542 E
                    // Ho Chi Minh City coordinates: 10.8231 N, 106.6297 E
                    double dist = geo.haversineDistance(21.0285, 105.8542, 10.8231, 106.6297);
                    this.reportView.renderHaversineAccuracyReport(dist, 1138.0, "Hanoi (21.0285 N, 105.8542 E)", "Ho Chi Minh City (10.8231 N, 106.6297 E)");
                    break;
                }
                case "3": {
                    int doubleAssignments = this.simulatorController.detectDoubleAssignments();
                    int oversells = this.simulatorController.detectOversellItems();
                    int driverOverloads = this.simulatorController.detectDriverOverload();
                    this.reportView.renderConcurrencyAnomaliesAudit(doubleAssignments, oversells, driverOverloads);
                    break;
                }
                case "4": {
                    this.reportView.renderBigQuestionSummary();
                    break;
                }
                case "5": {
                    List<SimulationRun> allRuns = this.simulatorController.getAllSimulationRuns();
                    if (allRuns.isEmpty()) {
                        System.out.println("No simulation runs recorded in data/simulation_runs.csv yet.");
                    } else {
                        System.out.println("\nAll Recorded Simulation Benchmark Runs (" + allRuns.size() + " records):");
                        this.simulatorView.renderComparisonTable(allRuns);
                    }
                    break;
                }
                case "0":
                    inReportMenu = false;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
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
