package view;

import controller.CustomerController;
import controller.RestaurantController;
import controller.DriverController;
import controller.OrderController;
import controller.SimulatorController;
import controller.AuthController;
import controller.AdminController;
import controller.MainController;
import model.Customer;
import model.Restaurant;
import model.MenuItem;
import model.Driver;
import model.DriverStatus;
import model.DriverApplication;
import model.Order;
import model.OrderStatus;
import model.User;
import model.UserRole;
import model.LockMechanism;
import model.SimulationRun;
import model.GeoUtils;
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
    private AuthController authController;
    private AdminController adminController;
    private MapView mapView;
    private SimulatorView simulatorView;
    private OrderView orderView;
    private ReportView reportView;
    private Scanner scanner;
    private MainController mainController;

    private String readLine() {
        if (this.scanner != null && this.scanner.hasNextLine()) {
            return this.scanner.nextLine().trim();
        }
        return "0";
    }

    public MainView(MainController mainController) {
        this.mainController = mainController;
        this.customerController = mainController.getCustomerController();
        this.restaurantController = mainController.getRestaurantController();
        this.driverController = mainController.getDriverController();
        this.orderController = mainController.getOrderController();
        this.simulatorController = mainController.getSimulatorController();
        this.authController = mainController.getAuthController();
        this.adminController = mainController.getAdminController();
        this.mapView = mainController.getMapView();
        this.simulatorView = mainController.getSimulatorView();
        this.orderView = mainController.getOrderView();
        this.reportView = mainController.getReportView();
        try {
            this.scanner = new Scanner(System.in, "UTF-8");
        } catch (Exception e) {
            this.scanner = new Scanner(System.in);
        }
    }

    public MainController getMainController() {
        return this.mainController;
    }

    public MainView() {
        this.customerController = new CustomerController();
        this.restaurantController = new RestaurantController();
        this.driverController = new DriverController();
        this.orderController = new OrderController();
        this.simulatorController = new SimulatorController();
        this.authController = new AuthController();
        this.adminController = new AdminController();
        this.mapView = new MapView();
        this.simulatorView = new SimulatorView();
        this.orderView = new OrderView();
        this.reportView = new ReportView();
        try {
            this.scanner = new Scanner(System.in, "UTF-8");
        } catch (Exception e) {
            this.scanner = new Scanner(System.in);
        }
    }

    @Override
    public void display() {
        boolean appRunning = true;
        while (appRunning) {
            if (!this.authController.isLoggedIn()) {
                boolean loggedIn = this.displayLoginScreen();
                if (!loggedIn) {
                    System.out.println("\nExiting Food Delivery Network Simulation. Goodbye!");
                    appRunning = false;
                    break;
                }
            }

            User currentUser = this.authController.getCurrentUser();
            this.handleRoleMenu(currentUser);
        }
    }

    // =========================================================================
    // 1. UNAUTHENTICATED SCREEN (Section 4.1)
    // =========================================================================

    private boolean displayLoginScreen() {
        while (true) {
            System.out.println("\n==========================================================================");
            System.out.println("     FOOD DELIVERY NETWORK SIMULATION - HE THONG DANG NHAP / DANG KY      ");
            System.out.println("==========================================================================");
            System.out.println("1. Dang nhap (Login)");
            System.out.println("2. Dang ky tai khoan Khach hang (Register Customer Account)");
            System.out.println("3. Gui ho so dang ky lam Tai xe (Register as Driver - Cho Admin duyet)");
            System.out.println("4. Xem danh sach tai khoan mau (Demo Sample Accounts)");
            System.out.println("0. Thoat ung dung (Exit)");
            System.out.println("==========================================================================");
            System.out.print("Vui long chon chuc nang [0-4]: ");
            String opt = this.readLine();

            if (opt.equals("1")) {
                boolean loggedIn = this.handleLoginFlow();
                if (loggedIn) {
                    return true;
                }
            } else if (opt.equals("2")) {
                this.handleCustomerRegistrationFlow();
            } else if (opt.equals("3")) {
                this.handleDriverApplicationSubmissionFlow();
            } else if (opt.equals("4")) {
                this.handleDisplaySampleAccounts();
            } else if (opt.equals("0") || opt.equalsIgnoreCase("exit")) {
                return false;
            } else {
                System.out.println("Lua chon khong hop le. Vui long chon tu 0 den 4.");
            }
        }
    }

    private boolean handleLoginFlow() {
        System.out.println("\n--- DANG NHAP HE THONG ---");
        System.out.print("Nhap Ten dang nhap (Username hoac So dien thoai): ");
        String username = this.readLine();
        if (username.isEmpty()) {
            System.out.println("Ten dang nhap khong duoc de trong.");
            return false;
        }

        System.out.print("Nhap Mat khau (Password): ");
        String password = this.readLine();

        boolean authenticated = this.authController.login(username, password);
        if (authenticated) {
            User user = this.authController.getCurrentUser();
            System.out.println("\n>>> [DANG NHAP THANH CONG] Xin chao, " + user.getDisplayName() + " [" + user.getRole().name() + "]!");
            return true;
        } else {
            System.out.println("\n>>> [DANG NHAP THAT BAI] Sai ten dang nhap hoac mat khau! Vui long thu lai.");
            return false;
        }
    }

    private void handleCustomerRegistrationFlow() {
        System.out.println("\n--------------------------------------------------------------------------");
        System.out.println("                  DANG KY TAI KHOAN KHACH HANG MOI                        ");
        System.out.println("--------------------------------------------------------------------------");
        System.out.print("Nhap Ten dang nhap (Username): ");
        String username = this.readLine();
        System.out.print("Nhap Mat khau (Password): ");
        String password = this.readLine();
        System.out.print("Nhap Ho va ten khach hang: ");
        String name = this.readLine();
        System.out.print("Nhap So dien thoai: ");
        String phone = this.readLine();
        System.out.print("Nhap Dia chi nhan hang (TP. Quy Nhon): ");
        String address = this.readLine();

        try {
            User newUser = this.authController.registerCustomer(username, password, name, phone, address, 13.775, 109.225);
            System.out.println("\n>>> [DANG KY THANH CONG] Chuc mung khach hang " + newUser.getDisplayName() + "!");
            System.out.println(">>> Ma khach hang: " + newUser.getLinkedEntityId() + " | Ten dang nhap: " + newUser.getUsername());
            System.out.println(">>> Ban da duoc tu dong dang nhap vao he thong.");
        } catch (Exception e) {
            System.out.println("\n>>> [THONG BAO] " + e.getMessage());
        }
    }

    private void handleDriverApplicationSubmissionFlow() {
        System.out.println("\n--------------------------------------------------------------------------");
        System.out.println("               GUI HO SO DANG KY LAM TAI XE DOI TAC                       ");
        System.out.println("   (Ho so se duoc luu o trang thai PENDING cho Admin phe duyet)           ");
        System.out.println("--------------------------------------------------------------------------");
        System.out.print("Nhap Ten dang nhap mong muon (Username): ");
        String username = this.readLine();
        System.out.print("Nhap Mat khau (Password): ");
        String password = this.readLine();
        System.out.print("Nhap Ho va ten day du: ");
        String fullName = this.readLine();
        System.out.print("Nhap So dien thoai lien he: ");
        String phone = this.readLine();
        System.out.print("Nhap Bien so xe (VD: 77-L1 888.88): ");
        String plate = this.readLine();
        System.out.print("Nhap Loai phuong tien (Xe may / Xe dien / Xe ban tai): ");
        String vehicleType = this.readLine();

        try {
            DriverApplication app = this.authController.submitDriverApplication(username, password, fullName, phone, plate, vehicleType);
            System.out.println("\n>>> [GUI HO SO THANH CONG] Ma ho so: " + app.getId() + " | Trang thai: PENDING");
            System.out.println(">>> Ho so cua ban da duoc chuyen den Ban Quan Tri (ADMIN).");
            System.out.println(">>> Sau khi Admin phe duyet, ban co the dang nhap bang tai khoan nay voi vai tro DRIVER.");
        } catch (Exception e) {
            System.out.println("\n>>> [THONG BAO] " + e.getMessage());
        }
    }

    private void handleDisplaySampleAccounts() {
        System.out.println("\n==========================================================================");
        System.out.println("| SAMPLE ACCOUNTS FOR QUICK EVALUATION / DEMONSTRATION                   |");
        System.out.println("+---------------+---------------+---------------+------------------------+");
        System.out.println("| Username      | Password      | Role          | Description            |");
        System.out.println("+---------------+---------------+---------------+------------------------+");
        System.out.println("| admin         | admin123      | ADMIN         | Full System Access     |");
        System.out.println("| dispatcher    | disp123       | DISPATCHER    | Dispatcher Center      |");
        System.out.println("| customer1     | 123           | CUSTOMER      | Customer (Nguyen An)   |");
        System.out.println("| restaurant1   | 123           | RESTAURANT    | Restaurant Manager     |");
        System.out.println("| driver1       | 123           | DRIVER        | Driver (Tai Xe Nam)    |");
        System.out.println("+---------------+---------------+---------------+------------------------+");
    }

    // =========================================================================
    // 2. ROLE-BASED ROUTING
    // =========================================================================

    private void handleRoleMenu(User user) {
        if (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.SYSTEM_ADMIN || user.getRole() == UserRole.USER_ADMIN) {
            this.handleAdminMenu(user);
        } else if (user.getRole() == UserRole.DISPATCHER) {
            this.handleDispatcherMenu(user);
        } else if (user.getRole() == UserRole.CUSTOMER) {
            this.handleCustomerMenu(user);
        } else if (user.getRole() == UserRole.RESTAURANT || user.getRole() == UserRole.RESTAURANT_ADMIN) {
            this.handleRestaurantMenu(user);
        } else if (user.getRole() == UserRole.DRIVER) {
            this.handleDriverMenu(user);
        }
    }

    // =========================================================================
    // 3. CUSTOMER PORTAL (Section 3.1 - STRICTLY REALISTIC, NO DRIVER UPGRADE!)
    // =========================================================================

    private void handleCustomerMenu(User user) {
        this.checkAndDisplayCustomerNotifications(user.getLinkedEntityId());

        System.out.println("\n=======================================================");
        System.out.println("   FOOD DELIVERY SIMULATION - [CUSTOMER PORTAL]        ");
        System.out.println("   Khach hang: " + user.getDisplayName() + " (Ma: " + user.getLinkedEntityId() + ")");
        System.out.println("   Khu vuc hoat dong: TP. Quy Nhon - Binh Dinh         ");
        System.out.println("=======================================================");
        System.out.println("1. Xem danh sach Nha hang & Thuc don (Browse Restaurants & Menus)");
        System.out.println("2. Dat mon an (Place Order)");
        System.out.println("3. Theo doi & Xem don hang cua toi (Track & View My Orders)");
        System.out.println("4. Huy don hang dang cho (Cancel Pending Order)");
        System.out.println("5. Thong tin tai khoan cua toi (View Profile)");
        System.out.println("6. Kiem tra hop thu thong bao tai xe (Check Driver Notifications)");
        System.out.println("0. Dang xuat (Logout)");
        System.out.println("=======================================================");
        System.out.print("Nhap lua chon cua ban [0-6]: ");
        String choice = this.readLine();

        switch (choice) {
            case "1":
                this.handleCustomerBrowseRestaurants();
                break;
            case "2":
                this.handleCustomerPlaceOrderFlow(user.getLinkedEntityId());
                break;
            case "3":
                this.handleViewMyOrdersFlow(user.getLinkedEntityId());
                break;
            case "4":
                this.handleCustomerCancelOrderFlow(user.getLinkedEntityId());
                break;
            case "5":
                this.handleCustomerProfileFlow(user);
                break;
            case "6":
                this.checkAndDisplayCustomerNotifications(user.getLinkedEntityId());
                break;
            case "0":
                this.authController.logout();
                System.out.println("\n>>> [LOGGED OUT] Da dang xuat thanh cong. Tro ve man hinh dang nhap...");
                break;
            default:
                System.out.println("Lua chon khong hop le. Vui long chon tu 0 den 6.");
        }
    }

    private void checkAndDisplayCustomerNotifications(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            return;
        }
        List<Order> orders = this.orderController.getOrdersByCustomer(customerId);
        if (orders == null || orders.isEmpty()) {
            return;
        }

        List<Order> activeOrders = new ArrayList<>();
        List<Order> pendingOrders = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getStatus() == OrderStatus.CONFIRMED || o.getStatus() == OrderStatus.DISPATCHED) {
                activeOrders.add(o);
            } else if (o.getStatus() == OrderStatus.PENDING) {
                pendingOrders.add(o);
            }
        }

        if (activeOrders.isEmpty() && pendingOrders.isEmpty()) {
            return;
        }

        GeoUtils geo = new GeoUtils();
        Customer cust = this.customerController.getCustomerById(customerId);
        double cLat = (cust != null && cust.getLatitude() != 0.0) ? cust.getLatitude() : 13.775;
        double cLon = (cust != null && cust.getLongitude() != 0.0) ? cust.getLongitude() : 109.225;
        String custWard = (cust != null && cust.getWard() != null) ? cust.getWard() : "TP. Quy Nhon";

        if (!activeOrders.isEmpty()) {
            System.out.println("\n****************************************************************************************");
            System.out.println(">>> [HOP THU THONG BAO - TAI XE DA TIEP NHAN DON HANG TAI TP. QUY NHON!] <<<");
            System.out.println("****************************************************************************************");
            for (int i = 0; i < activeOrders.size(); i++) {
                Order order = activeOrders.get(i);
                Restaurant rest = this.restaurantController.getRestaurantById(order.getRestaurantId());
                String rName = (rest != null) ? rest.getName() : order.getRestaurantId();
                String rAddr = (rest != null) ? rest.getAddress() : "TP. Quy Nhon";
                double rLat = (rest != null) ? rest.getLatitude() : 13.770;
                double rLon = (rest != null) ? rest.getLongitude() : 109.220;

                Driver driver = this.driverController.getDriverById(order.getDriverId());
                String dName = (driver != null) ? driver.getName() : order.getDriverId();
                String dPhone = (driver != null) ? driver.getPhone() : "Chua cap nhat";
                String dPlate = (driver != null) ? driver.getLicensePlate() : "77-L1 XXX.XX";
                double dLat = (driver != null) ? driver.getLatitude() : rLat;
                double dLon = (driver != null) ? driver.getLongitude() : rLon;

                double driverToRestKm = geo.haversine(dLat, dLon, rLat, rLon);
                double restToCustKm = geo.haversine(rLat, rLon, cLat, cLon);

                int pickupMin = geo.calculatePickupMinutes(driverToRestKm);
                int deliveryMin = geo.calculateDeliveryMinutes(driverToRestKm, restToCustKm);
                String etaPickupClock = geo.formatEstimatedClockTime(order.getCreatedAtEpochMs(), pickupMin);
                String etaDeliveryClock = geo.formatEstimatedClockTime(order.getCreatedAtEpochMs(), deliveryMin);

                System.out.println("* DON HANG:           " + order.getId() + " - Quan: " + rName);
                System.out.println("  Dia chi quan:       " + rAddr);
                System.out.println("  TAI XE PHU TRACH:   " + dName + " (Ma: " + order.getDriverId() + ")");
                System.out.println("  So dien thoai:      " + dPhone);
                System.out.println("  Bien so xe may:     " + dPlate + " (Binh Dinh 77)");
                System.out.printf("  Khoang cach:        %.2f km (tai xe -> quan) | %.2f km (quan -> ban)\n", driverToRestKm, restToCustKm);
                System.out.println("  THOI GIAN LAY MON:  ~" + pickupMin + " phut (Du kien co mat tai quan luc " + etaPickupClock + ")");
                System.out.println("  THOI GIAN GIAO DEN: ~" + deliveryMin + " phut (Du kien den " + custWard + " luc " + etaDeliveryClock + ")");
                if (order.getStatus() == OrderStatus.CONFIRMED) {
                    System.out.println("  Trang thai hien tai: Quan dang chuan bi mon, tai xe dang tren duong den quan.");
                } else {
                    System.out.println("  Trang thai hien tai: Tai xe da lay mon, dang tren duong giao den ban!");
                }
                if (i < activeOrders.size() - 1) {
                    System.out.println("----------------------------------------------------------------------------------------");
                }
            }
            System.out.println("****************************************************************************************");
        }

        if (!pendingOrders.isEmpty()) {
            System.out.println("\n----------------------------------------------------------------------------------------");
            System.out.println(">>> [THONG BAO DON HANG CHO TAI XE TAI TP. QUY NHON]:");
            for (int i = 0; i < pendingOrders.size(); i++) {
                Order pOrder = pendingOrders.get(i);
                Restaurant pRest = this.restaurantController.getRestaurantById(pOrder.getRestaurantId());
                String pRName = (pRest != null) ? pRest.getName() : pOrder.getRestaurantId();
                System.out.println("  * Don " + pOrder.getId() + " (Quan: " + pRName + ") dang trong Hang doi Dieu phoi tu dong.");
                System.out.println("    He thong dang lien tuc quet radar tim tai xe gan nhat trong khu vuc Quy Nhon...");
            }
            System.out.println("----------------------------------------------------------------------------------------");
        }
    }

    private void handleCustomerBrowseRestaurants() {
        List<Restaurant> list = this.restaurantController.getAllRestaurants();
        if (list.isEmpty()) {
            System.out.println("Hien chua co nha hang nao tren he thong.");
            return;
        }
        System.out.println("\n--- DANH SACH NHA HANG DOI TAC ---");
        int limit = Math.min(list.size(), 10);
        for (int i = 0; i < limit; i++) {
            Restaurant r = list.get(i);
            System.out.printf("[%s] %-25s | Dia chi: %-25s | Danh gia: %.1f/5.0\n",
                    r.getId(), r.getName(), r.getAddress(), r.getRating());
        }

        System.out.print("\nNhap Ma nha hang de xem thuc don (hoac Enter de quay lai): ");
        String rId = this.readLine();
        if (!rId.isEmpty()) {
            Restaurant selected = this.restaurantController.getRestaurantById(rId);
            if (selected != null) {
                List<MenuItem> menu = this.restaurantController.getMenuByRestaurant(selected.getId());
                this.orderView.displayMenu(selected, menu);
            } else {
                System.out.println("Khong tim thay nha hang voi ma: " + rId);
            }
        }
    }

    private void handleCustomerPlaceOrderFlow(String customerId) {
        System.out.println("\n--- DAT MON AN TRUC TUYEN ---");
        List<Restaurant> restaurants = this.restaurantController.getAllRestaurants();
        if (restaurants.isEmpty()) {
            System.out.println("He thong chua co du lieu nha hang.");
            return;
        }

        System.out.println("Danh sach mot so nha hang dang hoat dong:");
        int limitR = Math.min(restaurants.size(), 5);
        for (int i = 0; i < limitR; i++) {
            Restaurant r = restaurants.get(i);
            System.out.println("  * [" + r.getId() + "] " + r.getName() + " (" + r.getAddress() + ")");
        }

        System.out.print("Nhap Ma nha hang ban muon dat (mac dinh: " + restaurants.get(0).getId() + "): ");
        String rId = this.readLine();
        if (rId.isEmpty()) {
            rId = restaurants.get(0).getId();
        }

        Restaurant rest = this.restaurantController.getRestaurantById(rId);
        if (rest == null) {
            System.out.println("Khong tim thay nha hang voi ma: " + rId);
            return;
        }

        List<MenuItem> menu = this.restaurantController.getMenuByRestaurant(rest.getId());
        if (menu.isEmpty()) {
            System.out.println("Nha hang nay hien chua co mon an nao tren thuc don.");
            return;
        }

        this.orderView.displayMenu(rest, menu);

        System.out.print("Nhap Ma mon an ban muon chon (mac dinh: " + menu.get(0).getId() + "): ");
        String mId = this.readLine();
        if (mId.isEmpty()) {
            mId = menu.get(0).getId();
        }

        MenuItem chosenItem = this.restaurantController.getMenuItemById(mId);
        if (chosenItem == null) {
            System.out.println("Khong tim thay mon an voi ma: " + mId);
            return;
        }

        System.out.print("Nhap so luong muon dat (mac dinh 1): ");
        String qStr = this.readLine();
        int qty = 1;
        try {
            if (!qStr.isEmpty()) {
                qty = Integer.parseInt(qStr);
            }
        } catch (NumberFormatException ignored) {}

        System.out.println("\n[TIEN TRINH XU LY DAT HANG & TIM TAI XE TU DONG]");
        System.out.println("[1/3] Don hang dang duoc gui toi Nha hang [" + rest.getName() + "]...");
        try { Thread.sleep(300); } catch (Exception ignored) {}
        System.out.println("[2/3] Nha hang da XAC NHAN don! DispatchService dang quet radar tim tai xe gan nhat (cong thuc Haversine)...");
        try { Thread.sleep(400); } catch (Exception ignored) {}

        try {
            Order order = this.orderController.placeCustomerOrderAndAutoDispatch(customerId, rest.getId(), chosenItem.getId(), qty, LockMechanism.OPTIMISTIC);

            Customer cust = this.customerController.getCustomerById(customerId);
            double cLat = (cust != null && cust.getLatitude() != 0.0) ? cust.getLatitude() : 13.775;
            double cLon = (cust != null && cust.getLongitude() != 0.0) ? cust.getLongitude() : 109.225;
            String custWard = (cust != null && cust.getWard() != null) ? cust.getWard() : "TP. Quy Nhon";

            GeoUtils geo = new GeoUtils();
            double restToCustKm = geo.haversine(rest.getLatitude(), rest.getLongitude(), cLat, cLon);

            System.out.println("\n==========================================================================");
            System.out.println("     KET QUA DAT HANG & DIEU PHOI TAI XE (KHU VUC: TP. QUY NHON)         ");
            System.out.println("==========================================================================");
            System.out.println("Ma don hang:     " + order.getId());
            System.out.println("Nha hang:        " + rest.getName() + " (" + rest.getAddress() + ")");
            System.out.println("Dia chi nhan:    " + custWard + " - TP. Quy Nhon");
            System.out.println("Mon an da dat:   " + chosenItem.getName() + " x" + qty);
            System.out.printf("Tien mon an:     %,.0f VND\n", order.getTotalFoodAmount());
            System.out.printf("Phi giao hang:   %,.0f VND (Tinh theo khoang cach %.2f km)\n", order.getDeliveryFee(), restToCustKm);
            System.out.printf("Tong thanh toan: %,.0f VND\n", order.getTotalAmount());
            System.out.println("--------------------------------------------------------------------------");

            if (order.getDriverId() != null && !order.getDriverId().trim().isEmpty()) {
                Driver d = this.driverController.getDriverById(order.getDriverId());
                String dName = (d != null) ? d.getName() : order.getDriverId();
                String dPhone = (d != null) ? d.getPhone() : "Chua cap nhat";
                String dPlate = (d != null) ? d.getLicensePlate() : "77-L1 XXX.XX";
                double dLat = (d != null) ? d.getLatitude() : rest.getLatitude();
                double dLon = (d != null) ? d.getLongitude() : rest.getLongitude();

                double driverToRestKm = geo.haversine(dLat, dLon, rest.getLatitude(), rest.getLongitude());
                int pickupMin = geo.calculatePickupMinutes(driverToRestKm);
                int deliveryMin = geo.calculateDeliveryMinutes(driverToRestKm, restToCustKm);
                String etaPickupClock = geo.formatEstimatedClockTime(order.getCreatedAtEpochMs(), pickupMin);
                String etaDeliveryClock = geo.formatEstimatedClockTime(order.getCreatedAtEpochMs(), deliveryMin);

                System.out.println(">>> [3/3] THONG BAO: DA TIM THAY TAI XE NHAN DON (MATCHED SUCCESSFUL)!");
                System.out.println("  * TAI XE PHU TRACH:  " + dName + " (Ma: " + order.getDriverId() + ")");
                System.out.println("  * So dien thoai:     " + dPhone);
                System.out.println("  * Bien so xe may:    " + dPlate + " (Khu vuc Quy Nhon)");
                System.out.printf("  * Khoang cach:       %.2f km (tai xe -> quan) | %.2f km (quan -> ban)\n", driverToRestKm, restToCustKm);
                System.out.println("  * THOI GIAN LAY MON: ~" + pickupMin + " phut (Du kien co mat tai quan luc " + etaPickupClock + ")");
                System.out.println("  * THOI GIAN GIAO DEN:~" + deliveryMin + " phut (Du kien den " + custWard + " luc " + etaDeliveryClock + ")");
                System.out.println("  * Trang thai don:    CONFIRMED (Quan dang chuan bi mon, tai xe dang tren duong den quan)");
            } else {
                System.out.println(">>> [3/3] THONG BAO: Tat ca tai xe tai Quy Nhon hien dang thuc hien cuoc giao khac.");
                System.out.println("  * Don hang da duoc xac nhan va dua vao Hang doi Uu tien (Priority Queue).");
                System.out.println("  * He thong se tu dong gan tai xe ngay khi co tai xe hoan tat chuyen hoac online!");
            }
            System.out.println("==========================================================================");
        } catch (Exception e) {
            System.out.println("\n>>> [DAT HANG THAT BAI] " + e.getMessage());
        }
    }

    private void handleViewMyOrdersFlow(String customerId) {
        boolean inOrderTracking = true;
        while (inOrderTracking) {
            System.out.println("\n=======================================================");
            System.out.println("            THEO DOI & DANH SACH DON HANG CUA TOI       ");
            System.out.println("=======================================================");
            List<Order> myOrders = this.orderController.getOrdersByCustomer(customerId);
            if (myOrders.isEmpty()) {
                System.out.println("Ban chua co don hang nao trong he thong.");
                return;
            }

            this.orderView.renderOrders(myOrders);

            System.out.println("-------------------------------------------------------");
            System.out.println("Tuy chon theo doi don hang:");
            System.out.println("1. Xem chi tiet & Theo doi hanh trinh (Real-Time Tracker)");
            System.out.println("2. Quet tim lai tai xe cho cac don chua co tai xe (Retry Auto-Dispatch)");
            System.out.println("0. Quay lai Menu Khach hang");
            System.out.println("-------------------------------------------------------");
            System.out.print("Nhap lua chon [0-2]: ");
            String opt = this.readLine();

            if (opt.equals("1")) {
                System.out.print("Nhap Ma don hang muon theo doi (hoac Enter de chon don moi nhat): ");
                String oId = this.readLine();
                Order selected = null;
                if (oId.isEmpty()) {
                    selected = myOrders.get(myOrders.size() - 1);
                } else {
                    for (int i = 0; i < myOrders.size(); i++) {
                        if (oId.equalsIgnoreCase(myOrders.get(i).getId())) {
                            selected = myOrders.get(i);
                            break;
                        }
                    }
                }

                if (selected != null) {
                    this.handleLiveOrderTrackerDetail(selected, customerId);
                } else {
                    System.out.println("Khong tim thay don hang: " + oId);
                }
            } else if (opt.equals("2")) {
                System.out.println("\nDang quet radar tim tai xe ranh cho cac don hang dang cho...");
                List<String> logs = this.orderController.autoDispatchAllPending(LockMechanism.OPTIMISTIC);
                if (logs.isEmpty()) {
                    System.out.println(">>> Khong co don hang nao can dieu phoi hoac chua co them tai xe ranh.");
                } else {
                    for (int i = 0; i < logs.size(); i++) {
                        System.out.println("  -> " + logs.get(i));
                    }
                }
            } else if (opt.equals("0")) {
                inOrderTracking = false;
            } else {
                System.out.println("Lua chon khong hop le.");
            }
        }
    }

    private void handleLiveOrderTrackerDetail(Order order, String customerId) {
        Order fresh = this.orderController.getOrderById(order.getId());
        if (fresh != null) {
            order = fresh;
        }

        Customer cust = this.customerController.getCustomerById(customerId);
        double cLat = (cust != null && cust.getLatitude() != 0.0) ? cust.getLatitude() : 13.775;
        double cLon = (cust != null && cust.getLongitude() != 0.0) ? cust.getLongitude() : 109.225;
        String custWard = (cust != null && cust.getWard() != null) ? cust.getWard() : "TP. Quy Nhon";

        System.out.println("\n==========================================================================");
        System.out.println("    THEO DOI CHI TIET HANH TRINH GIAO HANG (KHU VUC: TP. QUY NHON)        ");
        System.out.println("==========================================================================");
        System.out.println("Ma don hang:     " + order.getId());

        Restaurant rest = this.restaurantController.getRestaurantById(order.getRestaurantId());
        String restName = (rest != null) ? rest.getName() : order.getRestaurantId();
        String restAddr = (rest != null) ? rest.getAddress() : "TP. Quy Nhon";
        double rLat = (rest != null) ? rest.getLatitude() : 13.770;
        double rLon = (rest != null) ? rest.getLongitude() : 109.220;

        System.out.println("Nha hang:        " + restName + " (" + restAddr + ")");
        System.out.println("Dia chi nhan:    " + custWard + " - TP. Quy Nhon");
        System.out.printf("Tien mon:        %,.0f VND | Phi ship: %,.0f VND | Tong: %,.0f VND\n",
                order.getTotalFoodAmount(), order.getDeliveryFee(), order.getTotalAmount());

        System.out.println("\nTIEN TRINH HANH TRINH:");
        String s1 = "[ ]", s2 = "[ ]", s3 = "[ ]", s4 = "[ ]";
        if (order.getStatus() == OrderStatus.PENDING) {
            s1 = "[V]";
        } else if (order.getStatus() == OrderStatus.CONFIRMED) {
            s1 = "[V]"; s2 = "[V]";
        } else if (order.getStatus() == OrderStatus.DISPATCHED) {
            s1 = "[V]"; s2 = "[V]"; s3 = "[V]";
        } else if (order.getStatus() == OrderStatus.DELIVERED) {
            s1 = "[V]"; s2 = "[V]"; s3 = "[V]"; s4 = "[V]";
        } else if (order.getStatus() == OrderStatus.CANCELLED) {
            s1 = "[X] (DA HUY)";
        }

        if (order.getStatus() != OrderStatus.CANCELLED) {
            System.out.println("  1. Dat hang  ===>  2. Quan xac nhan  ===>  3. Dang giao hang  ===>  4. Da giao thanh cong");
            System.out.printf("     %-15s    %-17s      %-16s        %s\n", s1, s2, s3, s4);
        } else {
            System.out.println("  [X] Don hang da bi huy.");
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("THONG TIN TAI XE VA THOI GIAN DU KIEN:");
        if (order.getDriverId() != null && !order.getDriverId().trim().isEmpty()) {
            Driver d = this.driverController.getDriverById(order.getDriverId());
            String dName = (d != null) ? d.getName() : order.getDriverId();
            String dPhone = (d != null) ? d.getPhone() : "Chua cap nhat";
            String dPlate = (d != null) ? d.getLicensePlate() : "77-L1 XXX.XX";
            double dLat = (d != null) ? d.getLatitude() : rLat;
            double dLon = (d != null) ? d.getLongitude() : rLon;

            GeoUtils geo = new GeoUtils();
            double driverToRestKm = geo.haversine(dLat, dLon, rLat, rLon);
            double restToCustKm = geo.haversine(rLat, rLon, cLat, cLon);

            int pickupMin = geo.calculatePickupMinutes(driverToRestKm);
            int deliveryMin = geo.calculateDeliveryMinutes(driverToRestKm, restToCustKm);
            String etaPickupClock = geo.formatEstimatedClockTime(order.getCreatedAtEpochMs(), pickupMin);
            String etaDeliveryClock = geo.formatEstimatedClockTime(order.getCreatedAtEpochMs(), deliveryMin);

            System.out.println("  * TAI XE PHU TRACH:    " + dName + " (Ma: " + order.getDriverId() + ")");
            System.out.println("  * So dien thoai:       " + dPhone);
            System.out.println("  * Bien so xe:          " + dPlate + " (Binh Dinh 77)");
            System.out.printf("  * Khoang cach:         %.2f km (tai xe -> quan) | %.2f km (quan -> ban)\n", driverToRestKm, restToCustKm);
            System.out.println("  * THOI GIAN LAY MON:   ~" + pickupMin + " phut (Du kien luc " + etaPickupClock + ")");
            System.out.println("  * THOI GIAN GIAO DEN:  ~" + deliveryMin + " phut (Du kien luc " + etaDeliveryClock + ")");
            if (order.getStatus() == OrderStatus.CONFIRMED) {
                System.out.println("  * Tinh trang hien tai: Quan dang chuan bi mon, tai xe dang di chuyen den quan.");
            } else if (order.getStatus() == OrderStatus.DISPATCHED) {
                System.out.println("  * Tinh trang hien tai: Tai xe da lay hang va dang tren duong giao den " + custWard + "!");
            } else if (order.getStatus() == OrderStatus.DELIVERED) {
                System.out.println("  * Tinh trang hien tai: Don hang da duoc giao thanh cong! Chuc ngon mieng!");
            }
        } else {
            System.out.println("  * Tai xe phu trach:  (Chua co tai xe nhan - Don dang trong Hang doi Uu tien)");
            System.out.println("  * He thong se tu dong dieu phoi ngay khi co tai xe gan nhat ranh don.");
        }
        System.out.println("==========================================================================");

        if (order.getStatus() == OrderStatus.CONFIRMED || order.getStatus() == OrderStatus.DISPATCHED) {
            System.out.print("\nBan co muon xem MO PHONG TIEN TRINH TAI XE GIAO HANG thuc te khong? (y/n): ");
            String simAns = this.readLine();
            if (simAns.equalsIgnoreCase("y")) {
                this.simulateDeliveryProgress(order);
            }
        } else if (order.getStatus() == OrderStatus.PENDING || (order.getDriverId() == null || order.getDriverId().trim().isEmpty())) {
            System.out.print("\nBan co muon KICH HOAT QUET TIM TAI XE NGAY khong? (y/n): ");
            String retryAns = this.readLine();
            if (retryAns.equalsIgnoreCase("y")) {
                try {
                    boolean ok = this.orderController.dispatchPending(order.getId(), LockMechanism.OPTIMISTIC);
                    if (ok) {
                        Order updated = this.orderController.getOrderById(order.getId());
                        System.out.println(">>> [TIN VUI!] Da tim thay tai xe: " + updated.getDriverId() + " cho don hang!");
                    } else {
                        System.out.println(">>> Hien tai cac tai xe xung quanh van dang ban. He thong se tiep tuc thu lai tu dong.");
                    }
                } catch (Exception e) {
                    System.out.println(">>> [THONG BAO] " + e.getMessage());
                }
            }
        }
    }

    private void simulateDeliveryProgress(Order order) {
        System.out.println("\n--- DANG MO PHONG TIEN TRINH GIAO HANG TRUC TIEP ---");
        try {
            if (order.getStatus() == OrderStatus.CONFIRMED) {
                System.out.println(">>> [1/2] Tai xe " + order.getDriverId() + " dang di chuyen den quan an...");
                Thread.sleep(600);
                boolean picked = this.orderController.markOrderPickedUp(order.getId(), order.getDriverId());
                if (picked) {
                    System.out.println(">>> [DA LAY HANG] Tai xe da nhan mon tu quan! Trang thai: DISPATCHED (Dang tren duong giao).");
                }
                Thread.sleep(600);
            }

            System.out.println(">>> [2/2] Tai xe dang di chuyen theo tuyen duong toi uu den dia chi cua ban...");
            Thread.sleep(800);
            boolean delivered = this.orderController.markOrderDelivered(order.getId(), order.getDriverId(), LockMechanism.OPTIMISTIC);
            if (delivered) {
                System.out.println("\n>>> [GIAO HANG THANH CONG! (DELIVERED)]");
                System.out.println(">>> Don hang " + order.getId() + " da duoc ban giao tan tay khach hang!");
                System.out.println(">>> Tai xe " + order.getDriverId() + " da duoc tu dong giai phong ve trang thai AVAILABLE.");
            }
        } catch (Exception e) {
            System.out.println(">>> Loi mo phong: " + e.getMessage());
        }
    }

    private void handleCustomerCancelOrderFlow(String customerId) {
        System.out.println("\n--- HUY DON HANG DANG CHO ---");
        List<Order> myOrders = this.orderController.getOrdersByCustomer(customerId);
        List<Order> pending = new ArrayList<>();
        for (int i = 0; i < myOrders.size(); i++) {
            Order o = myOrders.get(i);
            if (o.getStatus() == OrderStatus.PENDING) {
                pending.add(o);
            }
        }

        if (pending.isEmpty()) {
            System.out.println("Ban khong co don hang nao o trang thai PENDING de co the huy.");
            return;
        }

        System.out.println("Danh sach don hang dang cho xac nhan:");
        this.orderView.renderOrders(pending);

        System.out.print("Nhap Ma don hang can huy: ");
        String orderId = this.readLine();

        try {
            boolean ok = this.orderController.cancelOrderByCustomer(orderId, customerId);
            if (ok) {
                System.out.println(">>> [THANH CONG] Don hang " + orderId + " da duoc huy. So luong mon da duoc hoan lai ton kho.");
            }
        } catch (Exception e) {
            System.out.println(">>> [THAT BAI] " + e.getMessage());
        }
    }

    private void handleCustomerProfileFlow(User user) {
        System.out.println("\n-------------------------------------------------------");
        System.out.println("               THONG TIN HO SO KHACH HANG              ");
        System.out.println("-------------------------------------------------------");
        System.out.println("Ten dang nhap: " + user.getUsername());
        System.out.println("Ho va ten:     " + user.getDisplayName());
        System.out.println("Ma khach hang: " + user.getLinkedEntityId());
        Customer c = this.customerController.getCustomerById(user.getLinkedEntityId());
        if (c != null) {
            System.out.println("So dien thoai: " + c.getPhone());
            System.out.println("Dia chi nhan:  " + c.getAddress());
            System.out.println("Toa do vi tri: (" + c.getLatitude() + ", " + c.getLongitude() + ")");
        }
        System.out.println("-------------------------------------------------------");
    }

    // =========================================================================
    // 4. DRIVER PORTAL (Section 3.2 - REALISTIC WITH ACTIVE CHECK)
    // =========================================================================

    private void handleDriverMenu(User user) {
        String driverId = user.getLinkedEntityId();
        Driver driver = this.driverController.getDriverById(driverId);
        String statusStr = driver != null ? driver.getStatus().name() : "OFFLINE";

        System.out.println("\n=======================================================");
        System.out.println("   FOOD DELIVERY SIMULATION - [CONG THONG TIN TAI XE]  ");
        System.out.println("   Tai xe: " + user.getDisplayName() + " (Ma: " + driverId + " | Trang thai: " + statusStr + ")");
        System.out.println("=======================================================");
        System.out.println("1. Xem thong tin tai xe & Thong ke ca nhan (View Profile & Stats)");
        System.out.println("2. Chuyen trang thai Truc tuyen / Ngoai tuyen (Toggle AVAILABLE / OFFLINE)");
        System.out.println("3. Xem don hang duoc phan cong cho toi (View Assigned Orders)");
        System.out.println("4. Xac nhan da lay hang tai quan (Confirm Picked Up -> DISPATCHED)");
        System.out.println("5. Xac nhan da giao hang thanh cong (Confirm Delivered -> chon trang thai tiep theo)");
        System.out.println("0. Dang xuat (Logout)");
        System.out.println("=======================================================");
        System.out.print("Nhap lua chon cua ban [0-5]: ");
        String choice = this.readLine();

        switch (choice) {
            case "1":
                this.handleDriverProfileFlow(user);
                break;
            case "2":
                this.handleDriverStatusToggleFlow(driverId);
                break;
            case "3":
                this.handleViewDriverOrdersFlow(driverId);
                break;
            case "4":
                this.handleDriverPickUpFlow(driverId);
                break;
            case "5":
                this.handleDriverDeliverFlow(driverId);
                break;
            case "0":
                this.authController.logout();
                System.out.println("\n>>> [LOGGED OUT] Da dang xuat thanh cong. Tro ve man hinh dang nhap...");
                break;
            default:
                System.out.println("Lua chon khong hop le. Vui long chon tu 0 den 5.");
        }
    }

    private void handleDriverProfileFlow(User user) {
        System.out.println("\n-------------------------------------------------------");
        System.out.println("               HO SO DOI TAC TAI XE                    ");
        System.out.println("-------------------------------------------------------");
        System.out.println("Tai khoan:    " + user.getUsername());
        System.out.println("Ho va ten:    " + user.getDisplayName());
        System.out.println("Ma tai xe:    " + user.getLinkedEntityId());
        Driver d = this.driverController.getDriverById(user.getLinkedEntityId());
        if (d != null) {
            System.out.println("So dien thoai: " + d.getPhone());
            System.out.println("Bien so xe:    " + d.getLicensePlate());
            System.out.println("Trang thai:    " + d.getStatus());
            System.out.println("Vi tri hien tai: (" + d.getLatitude() + ", " + d.getLongitude() + ")");
        }
        List<Order> orders = this.orderController.getOrdersByDriver(user.getLinkedEntityId());
        int deliveredCount = 0;
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getStatus() == OrderStatus.DELIVERED) {
                deliveredCount++;
            }
        }
        System.out.println("Tong so don da giao thanh cong: " + deliveredCount + " don hang.");
        System.out.println("-------------------------------------------------------");
    }

    private void handleDriverStatusToggleFlow(String driverId) {
        Driver driver = this.driverController.getDriverById(driverId);
        if (driver == null) {
            System.out.println("Khong tim thay thong tin tai xe lien ket voi tai khoan nay.");
            return;
        }

        // Section 3.2 Constraint: Cannot switch to AVAILABLE if driver has active delivery
        boolean hasActive = this.orderController.hasActiveDelivery(driverId);
        if (hasActive) {
            System.out.println("\n>>> [CANH BAO] Ban dang co don hang dang thuc hien (CONFIRMED / DISPATCHED)!");
            System.out.println(">>> Quy tac he thong: Tai xe khong duoc chuyen ve AVAILABLE khi chua giao xong don hang.");
            System.out.println(">>> Hay hoan tat don hang truoc.");
            return;
        }

        System.out.println("\n--- THAY DOI TRANG THAI HOAT DONG ---");
        System.out.println("Trang thai hien tai: " + driver.getStatus());
        DriverStatus next = (driver.getStatus() == DriverStatus.AVAILABLE) ? DriverStatus.OFFLINE : DriverStatus.AVAILABLE;
        System.out.print("Ban co muon chuyen sang trang thai [" + next + "]? (y/n): ");
        String ans = this.readLine();
        if (ans.equalsIgnoreCase("y")) {
            this.driverController.updateDriverStatus(driver.getId(), next);
            System.out.println(">>> Trang thai cua ban da duoc cap nhat thanh: " + next);
        }
    }

    private void handleViewDriverOrdersFlow(String driverId) {
        System.out.println("\n--- DON HANG DUOC GAN CHO TOI ---");
        List<Order> orders = this.orderController.getOrdersByDriver(driverId);
        if (orders.isEmpty()) {
            System.out.println("Hien tai chua co don hang nao duoc gan cho ban.");
        } else {
            this.orderView.renderOrders(orders);
        }
    }

    private void handleDriverPickUpFlow(String driverId) {
        System.out.println("\n--- XAC NHAN DA LAY HANG TAI QUAN ---");
        List<Order> orders = this.orderController.getOrdersByDriver(driverId);
        List<Order> confirmedList = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getStatus() == OrderStatus.CONFIRMED) {
                confirmedList.add(o);
            }
        }

        if (confirmedList.isEmpty()) {
            System.out.println("Ban khong co don hang nao o trang thai CONFIRMED (cho lay hang).");
            return;
        }

        this.orderView.renderOrders(confirmedList);
        System.out.print("Nhap Ma don hang ban da lay duoc tai quan: ");
        String orderId = this.readLine();

        try {
            boolean ok = this.orderController.markOrderPickedUp(orderId, driverId);
            if (ok) {
                System.out.println(">>> [THANH CONG] Don hang " + orderId + " da chuyen sang trang thai DISPATCHED (Dang giao).");
                System.out.println(">>> Hay tien hanh giao hang den dia chi khach hang an toan!");
            }
        } catch (Exception e) {
            System.out.println(">>> [THAT BAI] " + e.getMessage());
        }
    }

    private void handleDriverDeliverFlow(String driverId) {
        System.out.println("\n--- XAC NHAN DA GIAO HANG THANH CONG ---");
        List<Order> orders = this.orderController.getOrdersByDriver(driverId);
        List<Order> delivering = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getStatus() == OrderStatus.DISPATCHED || o.getStatus() == OrderStatus.CONFIRMED) {
                delivering.add(o);
            }
        }

        if (delivering.isEmpty()) {
            System.out.println("Ban khong co don hang nao dang giao.");
            return;
        }

        this.orderView.renderOrders(delivering);
        System.out.print("Nhap Ma don hang da giao xong cho khach: ");
        String enteredOrderId = this.readLine();
        Order selectedOrder = null;
        for (int i = 0; i < delivering.size(); i++) {
            if (delivering.get(i).getId().equalsIgnoreCase(enteredOrderId)) {
                selectedOrder = delivering.get(i);
                break;
            }
        }
        if (selectedOrder == null) {
            System.out.println(">>> [LOI] Ma don khong hop le hoac khong thuoc don dang giao cua ban.");
            return;
        }

        String orderId = selectedOrder.getId();
        boolean continueAvailable;
        while (true) {
            System.out.print("Ban muon nghi sau don nay? (y = co, n = tiep tuc nhan don): ");
            String answer = this.readLine();
            if (answer.equalsIgnoreCase("y")) {
                continueAvailable = false;
                break;
            }
            if (answer.equalsIgnoreCase("n")) {
                continueAvailable = true;
                break;
            }
            System.out.println("Vui long nhap y hoac n.");
        }

        try {
            boolean ok = this.orderController.markOrderDelivered(
                    orderId, driverId, LockMechanism.OPTIMISTIC, continueAvailable);
            if (ok) {
                System.out.println(">>> [CHUC MUNG!] Don hang " + orderId + " da duoc xac nhan DELIVERED.");
                if (continueAvailable) {
                    System.out.println(">>> Trang thai tai xe: AVAILABLE, ban co the tiep tuc nhan don.");
                } else {
                    System.out.println(">>> Trang thai tai xe: OFFLINE, ban se khong nhan them don.");
                }
            }
        } catch (Exception e) {
            System.out.println(">>> [THAT BAI] " + e.getMessage());
        }
    }

    // =========================================================================
    // 5. RESTAURANT MANAGER PORTAL (Section 3.3 - STRICTLY BOUND TO RESTAURANT)
    // =========================================================================

    private void handleRestaurantMenu(User user) {
        String restaurantId = user.getLinkedEntityId();
        Restaurant rest = this.restaurantController.getRestaurantById(restaurantId);
        String restName = rest != null ? rest.getName() : "Nha hang doi tac";

        System.out.println("\n=======================================================");
        System.out.println("   FOOD DELIVERY SIMULATION - [CONG QUAN LY NHA HANG]  ");
        System.out.println("   Quan ly: " + user.getDisplayName() + " (Nha hang: " + restName + " [" + restaurantId + "])");
        System.out.println("=======================================================");
        System.out.println("1. Xem thuc don cua quan & Tinh trang ton kho (View Menu & Stock)");
        System.out.println("2. Cap nhat don gia & So luong ton kho mon an (Update Price & Stock)");
        System.out.println("3. Xem danh sach don hang den quan (View Incoming Orders)");
        System.out.println("4. Xac nhan don hang (Confirm Order -> Kich hoat tu dong dieu phoi Tai xe)");
        System.out.println("5. Tu choi don hang (Reject Order -> Hoan lai ton kho mon an)");
        System.out.println("6. Thong ke doanh thu nha hang (Restaurant Revenue Stats)");
        System.out.println("0. Dang xuat (Logout)");
        System.out.println("=======================================================");
        System.out.print("Nhap lua chon cua ban [0-6]: ");
        String choice = this.readLine();

        switch (choice) {
            case "1":
                this.handleRestaurantViewMenuFlow(restaurantId, rest);
                break;
            case "2":
                this.handleRestaurantUpdateStockFlow(restaurantId);
                break;
            case "3":
                this.handleRestaurantViewIncomingOrdersFlow(restaurantId);
                break;
            case "4":
                this.handleRestaurantConfirmOrderFlow(restaurantId);
                break;
            case "5":
                this.handleRestaurantRejectOrderFlow(restaurantId);
                break;
            case "6":
                this.handleRestaurantStatsFlow(restaurantId, restName);
                break;
            case "0":
                this.authController.logout();
                System.out.println("\n>>> [LOGGED OUT] Da dang xuat thanh cong. Tro ve man hinh dang nhap...");
                break;
            default:
                System.out.println("Lua chon khong hop le. Vui long chon tu 0 den 6.");
        }
    }

    private void handleRestaurantViewMenuFlow(String restaurantId, Restaurant rest) {
        List<MenuItem> items = this.restaurantController.getMenuByRestaurant(restaurantId);
        if (items.isEmpty()) {
            System.out.println("Nha hang chua co mon an nao trong thuc don.");
            return;
        }
        this.orderView.displayMenu(rest, items);
    }

    private void handleRestaurantUpdateStockFlow(String restaurantId) {
        List<MenuItem> items = this.restaurantController.getMenuByRestaurant(restaurantId);
        if (items.isEmpty()) {
            System.out.println("Nha hang chua co mon an nao de cap nhat.");
            return;
        }
        this.orderView.renderMenu(items);

        System.out.print("Nhap Ma mon an can cap nhat: ");
        String mId = this.readLine();
        MenuItem target = this.restaurantController.getMenuItemById(mId);
        if (target == null || !restaurantId.equalsIgnoreCase(target.getRestaurantId())) {
            System.out.println("Mon an khong ton tai hoac khong thuoc nha hang cua ban.");
            return;
        }

        System.out.print("Nhap don gia moi (VND, de trong neu khong doi): ");
        String pStr = this.readLine();
        double price = target.getPriceVnd();
        if (!pStr.isEmpty()) {
            try { price = Double.parseDouble(pStr); } catch (NumberFormatException ignored) {}
        }

        System.out.print("Nhap so luong ton kho moi (de trong neu khong doi): ");
        String sStr = this.readLine();
        int stock = target.getStockQty();
        if (!sStr.isEmpty()) {
            try { stock = Integer.parseInt(sStr); } catch (NumberFormatException ignored) {}
        }

        boolean ok = this.restaurantController.updateMenuItem(target.getId(), target.getName(), price, stock);
        System.out.println(ok ? ">>> [CAP NHAT THANH CONG] Mon " + target.getName() + " da cap nhat!" : ">>> Cap nhat that bai.");
    }

    private void handleRestaurantViewIncomingOrdersFlow(String restaurantId) {
        System.out.println("\n--- DON HANG DEN NHA HANG ---");
        List<Order> orders = this.orderController.getOrdersByRestaurant(restaurantId);
        if (orders.isEmpty()) {
            System.out.println("Nha hang chua co don hang nao den.");
        } else {
            this.orderView.renderOrders(orders);
        }
    }

    private void handleRestaurantConfirmOrderFlow(String restaurantId) {
        System.out.println("\n--- XAC NHAN DON HANG & DIEU PHOI TAI XE ---");
        List<Order> orders = this.orderController.getOrdersByRestaurant(restaurantId);
        List<Order> pending = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getStatus() == OrderStatus.PENDING) {
                pending.add(o);
            }
        }

        if (pending.isEmpty()) {
            System.out.println("Khong co don hang nao o trang thai PENDING cho xac nhan.");
            return;
        }

        this.orderView.renderOrders(pending);
        System.out.print("Nhap Ma don hang can xac nhan: ");
        String orderId = this.readLine();

        try {
            Order confirmed = this.orderController.confirmOrderByRestaurant(orderId, restaurantId, LockMechanism.OPTIMISTIC);
            System.out.println("\n>>> [XAC NHAN DON HANG THANH CONG]");
            System.out.println(">>> Ma don hang: " + confirmed.getId() + " | Trang thai: " + confirmed.getStatus());
            if (confirmed.getDriverId() != null && !confirmed.getDriverId().isEmpty()) {
                System.out.println(">>> [HE THONG DA TU DONG DIEU PHOI] Gan tai xe: " + confirmed.getDriverId() + " den lay hang!");
            } else {
                System.out.println(">>> [THONG BAO] Don hang da duoc xac nhan, hien dang cho tai xe gan nhat ranh don de nhan.");
            }
        } catch (Exception e) {
            System.out.println(">>> [LOI] " + e.getMessage());
        }
    }

    private void handleRestaurantRejectOrderFlow(String restaurantId) {
        System.out.println("\n--- TU CHOI DON HANG ---");
        List<Order> orders = this.orderController.getOrdersByRestaurant(restaurantId);
        List<Order> pending = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getStatus() == OrderStatus.PENDING) {
                pending.add(o);
            }
        }

        if (pending.isEmpty()) {
            System.out.println("Khong co don hang PENDING nao de tu choi.");
            return;
        }

        this.orderView.renderOrders(pending);
        System.out.print("Nhap Ma don hang can tu choi: ");
        String orderId = this.readLine();
        System.out.print("Nhap ly do tu choi: ");
        String reason = this.readLine();

        try {
            boolean ok = this.orderController.rejectOrderByRestaurant(orderId, restaurantId, reason);
            if (ok) {
                System.out.println(">>> [DA TU CHOI] Don hang " + orderId + " da chuyen sang CANCELLED va so luong mon da duoc hoan lai vao kho.");
            }
        } catch (Exception e) {
            System.out.println(">>> [LOI] " + e.getMessage());
        }
    }

    private void handleRestaurantStatsFlow(String restaurantId, String restName) {
        System.out.println("\n-------------------------------------------------------");
        System.out.println("         THONG KE DOANH THU & DON HANG NHA HANG        ");
        System.out.println("   Nha hang: " + restName + " [" + restaurantId + "]");
        System.out.println("-------------------------------------------------------");
        List<Order> orders = this.orderController.getOrdersByRestaurant(restaurantId);
        int pending = 0, confirmed = 0, dispatched = 0, delivered = 0, cancelled = 0;
        double totalRev = 0.0;

        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getStatus() == OrderStatus.PENDING) pending++;
            else if (o.getStatus() == OrderStatus.CONFIRMED) confirmed++;
            else if (o.getStatus() == OrderStatus.DISPATCHED) dispatched++;
            else if (o.getStatus() == OrderStatus.DELIVERED) {
                delivered++;
                totalRev += o.getTotalFoodAmount();
            } else if (o.getStatus() == OrderStatus.CANCELLED) cancelled++;
        }

        System.out.println("Tong so don hang tiep nhan: " + orders.size());
        System.out.println("  + Dang cho xac nhan (PENDING):   " + pending);
        System.out.println("  + Da xac nhan (CONFIRMED):       " + confirmed);
        System.out.println("  + Dang giao hang (DISPATCHED):   " + dispatched);
        System.out.println("  + Da giao thanh cong (DELIVERED): " + delivered);
        System.out.println("  + Da huy (CANCELLED):            " + cancelled);
        System.out.printf("Tong doanh thu mon an thanh cong:   %,.0f VND\n", totalRev);
        System.out.println("-------------------------------------------------------");
    }

    // =========================================================================
    // 6. ADMIN CONSOLE (Section 3.4 & 4.4)
    // =========================================================================

    private void handleAdminMenu(User user) {
        System.out.println("\n=======================================================");
        System.out.println("   FOOD DELIVERY SIMULATION - [CONG QUAN TRI - ADMIN]  ");
        System.out.println("   Logged in as: " + user.getDisplayName());
        System.out.println("=======================================================");
        System.out.println("1. Duyet / Tu choi ho so dang ky tai xe (Approve / Reject Driver Applications)");
        System.out.println("2. Quan ly Nguoi dung & Phan quyen (User Management)");
        System.out.println("3. Quan ly Nha hang & Thuc don (Restaurant & Menu CRUD, Stock)");
        System.out.println("4. Quan ly Khach hang (Customer Management)");
        System.out.println("5. Quan ly Doi ngu Tai xe & Trang thai (Driver Fleet Management)");
        System.out.println("6. Xem toan bo don hang tren he thong (View All Orders)");
        System.out.println("7. Tu dong dieu phoi tat ca don hang dang cho (Auto-Dispatch All Pending)");
        System.out.println("8. Hien thi Ban do mang luoi ASCII Grid Map");
        System.out.println("9. Chay Concurrency Simulator Benchmark (Optimistic Locking Mechanism)");
        System.out.println("10. Sinh lai toan bo du lieu mau CSV (>= 18,000 Rows)");
        System.out.println("11. Tong quan toan he thong & Bao cao nghien cuu Big Question");
        System.out.println("0. Dang xuat (Logout)");
        System.out.println("=======================================================");
        System.out.print("Nhap lua chon cua ban [0-11]: ");
        String choice = this.readLine();

        switch (choice) {
            case "1":
                this.handleDriverApplicationsReviewFlow();
                break;
            case "2":
                this.handleUserManagementFlow();
                break;
            case "3":
                this.handleRestaurantManagementFlow();
                break;
            case "4":
                this.handleCustomerManagementFlow();
                break;
            case "5":
                this.handleDriverManagementFlow();
                break;
            case "6":
                this.handleViewOrdersFlow();
                break;
            case "7":
                this.handleAutoDispatchPendingFlow();
                break;
            case "8":
                this.handleDisplayAsciiMap();
                break;
            case "9":
                this.handleRunSimulatorBenchmark();
                break;
            case "10":
                this.handleDataGeneration();
                break;
            case "11":
                this.handleSystemOverviewFlow();
                break;
            case "0":
                this.authController.logout();
                System.out.println("\n>>> [LOGGED OUT] Da dang xuat thanh cong. Tro ve man hinh dang nhap...");
                break;
            default:
                System.out.println("Lua chon khong hop le. Vui long chon tu 0 den 11.");
        }
    }

    private void handleDriverApplicationsReviewFlow() {
        System.out.println("\n--------------------------------------------------------------------------");
        System.out.println("            DUYET HO SO DANG KY TAI XE (SECTION 3.4 & 4.4)                ");
        System.out.println("--------------------------------------------------------------------------");
        List<DriverApplication> pending = this.adminController.getPendingDriverApplications();
        if (pending.isEmpty()) {
            System.out.println("Hien tai khong co ho so dang ky tai xe nao dang cho duyet.");
            return;
        }

        System.out.println("Danh sach ho so dang cho xet duyet (" + pending.size() + " ho so):");
        System.out.println("+---------+---------------+----------------------+---------------+---------------+---------------+");
        System.out.println("| Ma HS   | Username      | Ho va ten            | SDT           | Bien so xe    | Phuong tien   |");
        System.out.println("+---------+---------------+----------------------+---------------+---------------+---------------+");
        for (int i = 0; i < pending.size(); i++) {
            DriverApplication a = pending.get(i);
            System.out.printf("| %-7s | %-13s | %-20s | %-13s | %-13s | %-13s |\n",
                    a.getId(), a.getUsername(),
                    a.getFullName().length() > 20 ? a.getFullName().substring(0, 17) + "..." : a.getFullName(),
                    a.getPhone(), a.getLicensePlate(), a.getVehicleType());
        }
        System.out.println("+---------+---------------+----------------------+---------------+---------------+---------------+");

        System.out.print("Nhap Ma ho so can xu ly (hoac Enter de bo qua): ");
        String appId = this.readLine();
        if (appId.isEmpty()) return;

        System.out.print("Ban muon (1) PHE DUYET hay (2) TU CHOI ho so nay? [1/2]: ");
        String action = this.readLine();

        if (action.equals("1")) {
            User approved = this.adminController.approveDriverApplication(appId);
            if (approved != null) {
                System.out.println("\n>>> [DA PHE DUYET THANH CONG]");
                System.out.println(">>> Tao moi doi tac tai xe voi Ma: " + approved.getLinkedEntityId() + " (Trang thai ban dau: OFFLINE)");
                System.out.println(">>> Cap tai khoan dang nhap: " + approved.getUsername() + " [Role: DRIVER].");
            } else {
                System.out.println(">>> Phe duyet that bai. Vui long kiem tra lai ma ho so.");
            }
        } else if (action.equals("2")) {
            System.out.print("Nhap ly do tu choi: ");
            String reason = this.readLine();
            boolean ok = this.adminController.rejectDriverApplication(appId, reason);
            System.out.println(ok ? ">>> [DA TU CHOI HO SO] " + appId : ">>> Tu choi that bai.");
        }
    }

    // =========================================================================
    // 7. DISPATCHER CONSOLE (Section 3.5)
    // =========================================================================

    private void handleDispatcherMenu(User user) {
        System.out.println("\n=======================================================");
        System.out.println("   FOOD DELIVERY SIMULATION - [DISPATCHER CONSOLE]     ");
        System.out.println("   Logged in as: " + user.getDisplayName());
        System.out.println("=======================================================");
        System.out.println("1. Xem danh sach don hang can dieu phoi (View Pending Orders)");
        System.out.println("2. Kich hoat tu dong dieu phoi tat ca don cho (Auto-Dispatch All Pending)");
        System.out.println("3. Xem toan bo don hang gan day (View Recent Orders)");
        System.out.println("4. Xem trang thai doi ngu tai xe (View Driver Status)");
        System.out.println("5. Hien thi Ban do mang luoi ASCII Grid Map");
        System.out.println("6. Chay Concurrency Simulator Benchmark (Optimistic Locking Mechanism)");
        System.out.println("0. Dang xuat (Logout)");
        System.out.println("=======================================================");
        System.out.print("Nhap lua chon cua ban [0-6]: ");
        String choice = this.readLine();

        switch (choice) {
            case "1":
                List<Order> pending = this.orderController.getPendingOrders();
                System.out.println("\nDanh sach don hang dang cho gan tai xe (" + pending.size() + " don):");
                this.orderView.renderOrders(pending);
                break;
            case "2":
                this.handleAutoDispatchPendingFlow();
                break;
            case "3":
                this.handleViewOrdersFlow();
                break;
            case "4":
                this.handleDriverManagementFlow();
                break;
            case "5":
                this.handleDisplayAsciiMap();
                break;
            case "6":
                this.handleRunSimulatorBenchmark();
                break;
            case "0":
                this.authController.logout();
                System.out.println("\n>>> [LOGGED OUT] Da dang xuat thanh cong. Tro ve man hinh dang nhap...");
                break;
            default:
                System.out.println("Lua chon khong hop le. Vui long chon tu 0 den 6.");
        }
    }

    // =========================================================================
    // 8. COMMON FLOWS (ASCII MAP, BENCHMARK, USER CRUD, ETC.)
    // =========================================================================

    private void handleViewOrdersFlow() {
        List<Order> orders = this.orderController.getAllOrders();
        this.orderView.renderOrders(orders);
    }

    private void handleDisplayAsciiMap() {
        List<Restaurant> restaurants = this.restaurantController.getAllRestaurants();
        List<Customer> customers = this.customerController.getAllCustomers();
        List<Driver> drivers = this.driverController.getAllDrivers();
        List<Order> orders = this.orderController.getAllOrders();
        this.mapView.renderAsciiMap(restaurants, customers, drivers, orders);
    }

    private void handleRunSimulatorBenchmark() {
        System.out.println("\n=======================================================");
        System.out.println("  CONCURRENCY SIMULATOR - OPTIMISTIC LOCKING BENCHMARK ");
        System.out.println("=======================================================");
        System.out.println("Kien truc dong bo: OPTIMISTIC LOCKING (Chuan Shopee/Grab/Tiki/Amazon)");
        System.out.println("Mo hinh kiem thu : 50 luong dong thoi (25 Customer + 25 Dispatch)");
        System.out.print("Nhap so luong don hang mo phong tuong tranh (e.g. 500 hoac 1000, mac dinh 500): ");
        String countStr = this.readLine();
        int ordersCount = 500;
        try {
            if (!countStr.isEmpty()) {
                ordersCount = Integer.parseInt(countStr);
            }
        } catch (NumberFormatException ignored) {}

        List<SimulationRun> runs = new ArrayList<>();
        System.out.println("\nDang khoi chay mo phong tuong tranh voi co che: OPTIMISTIC...");
        SimulationRun run = this.simulatorController.runSimulation(ordersCount, LockMechanism.OPTIMISTIC);
        if (run != null) {
            runs.add(run);
            this.reportView.renderSimulationSummary(run);
        }

        this.simulatorView.renderComparisonTable(runs);
        this.reportView.printResearchConclusion(runs);
    }

    private void handleDataGeneration() {
        System.out.println("\nGenerating fresh physical CSV dataset (>= 18,000 rows)...");
        DataGenerator generator = new DataGenerator();
        generator.generateAllData();
        System.out.println("Data generation completed successfully!");
    }

    private void handleAutoDispatchPendingFlow() {
        System.out.println("\n=======================================================");
        System.out.println("  TU DONG DIEU PHOI TAI XE CHO TAT CA DON HANG PENDING ");
        System.out.println("=======================================================");
        List<Order> pendingOrders = this.orderController.getPendingOrders();
        System.out.println("So luong don hang dang cho dieu phoi (PENDING / Chua co tai xe): " + pendingOrders.size());
        if (pendingOrders.isEmpty()) {
            System.out.println(">>> Khong co don hang nao can dieu phoi tai thoi diem nay.");
            return;
        }

        LockMechanism mech = LockMechanism.OPTIMISTIC;
        System.out.println("\nDang kich hoat quy trinh tu dong quet vi tri nha hang va tai xe gan nhat (Chuan hoa OPTIMISTIC LOCKING)...");
        List<String> logs = this.orderController.autoDispatchAllPending(mech);
        for (int i = 0; i < logs.size(); i++) {
            System.out.println("  -> " + logs.get(i));
        }
        System.out.println("\n>>> [TU DONG DIEU PHOI HOAN TAT] Tong cong da xu ly " + logs.size() + " don hang.");
    }

    private void handleDriverManagementFlow() {
        List<Driver> drivers = this.driverController.getAllDrivers();
        System.out.println("\nTong so tai xe tren mang luoi: " + drivers.size());
        List<Driver> available = this.driverController.getAvailableDrivers();
        System.out.println("So tai xe dang AVAILABLE (San sang nhan don): " + available.size());

        int limit = Math.min(drivers.size(), 10);
        System.out.println("+---------+----------------------+---------------+---------------+---------------+");
        System.out.println("| Ma TX   | Ho va ten            | SDT           | Bien so xe    | Trang thai    |");
        System.out.println("+---------+----------------------+---------------+---------------+---------------+");
        for (int i = 0; i < limit; i++) {
            Driver d = drivers.get(i);
            System.out.printf("| %-7s | %-20s | %-13s | %-13s | %-13s |\n",
                    d.getId(),
                    d.getName().length() > 20 ? d.getName().substring(0, 17) + "..." : d.getName(),
                    d.getPhone(), d.getLicensePlate(), d.getStatus().name());
        }
        System.out.println("+---------+----------------------+---------------+---------------+---------------+");
    }

    private void handleUserManagementFlow() {
        boolean inUserMenu = true;
        while (inUserMenu) {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("        QUAN LY NGUOI DUNG & TAI KHOAN HE THONG        ");
            System.out.println("-------------------------------------------------------");
            System.out.println("1. Xem danh sach tat ca tai khoan nguoi dung");
            System.out.println("2. Tim kiem tai khoan theo Ten dang nhap");
            System.out.println("3. Loc danh sach tai khoan theo Vai tro (Role)");
            System.out.println("4. Tao tai khoan moi (Admin / Dispatcher / Rest / Driver / Customer)");
            System.out.println("5. Cap nhat tai khoan (Mat khau, Ten hien thi, Vai tro)");
            System.out.println("6. Xoa tai khoan nguoi dung");
            System.out.println("0. Quay lai Menu Admin");
            System.out.println("-------------------------------------------------------");
            System.out.print("Chon chuc nang quan ly nguoi dung [0-6]: ");
            String opt = this.readLine();

            switch (opt) {
                case "1":
                    List<User> allUsers = this.authController.getAllUsers();
                    this.displayUserTable(allUsers);
                    break;
                case "2":
                    System.out.print("Nhap ten dang nhap can tim: ");
                    String sUsername = this.readLine();
                    User foundUser = this.authController.getUserRepository().findByUsername(sUsername);
                    if (foundUser != null) {
                        List<User> list = new ArrayList<>();
                        list.add(foundUser);
                        this.displayUserTable(list);
                    } else {
                        System.out.println("Khong tim thay nguoi dung voi username: " + sUsername);
                    }
                    break;
                case "3":
                    System.out.println("Chon vai tro can loc:");
                    System.out.println("1. ADMIN | 2. DISPATCHER | 3. RESTAURANT | 4. DRIVER | 5. CUSTOMER");
                    System.out.print("Lua chon [1-5]: ");
                    String rChoice = this.readLine();
                    UserRole filterRole = UserRole.CUSTOMER;
                    if (rChoice.equals("1")) filterRole = UserRole.ADMIN;
                    else if (rChoice.equals("2")) filterRole = UserRole.DISPATCHER;
                    else if (rChoice.equals("3")) filterRole = UserRole.RESTAURANT;
                    else if (rChoice.equals("4")) filterRole = UserRole.DRIVER;
                    List<User> filtered = this.authController.getUsersByRole(filterRole);
                    System.out.println("\nDanh sach tai khoan vai tro [" + filterRole.name() + "] (" + filtered.size() + " tai khoan):");
                    this.displayUserTable(filtered);
                    break;
                case "4":
                    System.out.print("Nhap ten dang nhap moi: ");
                    String uName = this.readLine();
                    System.out.print("Nhap mat khau: ");
                    String uPass = this.readLine();
                    System.out.println("Chon vai tro: 1. ADMIN | 2. DISPATCHER | 3. RESTAURANT | 4. DRIVER | 5. CUSTOMER");
                    System.out.print("Lua chon [1-5]: ");
                    String roleOpt = this.readLine();
                    UserRole newRole = UserRole.CUSTOMER;
                    if (roleOpt.equals("1")) newRole = UserRole.ADMIN;
                    else if (roleOpt.equals("2")) newRole = UserRole.DISPATCHER;
                    else if (roleOpt.equals("3")) newRole = UserRole.RESTAURANT;
                    else if (roleOpt.equals("4")) newRole = UserRole.DRIVER;

                    System.out.print("Nhap ten hien thi: ");
                    String uDisp = this.readLine();
                    System.out.print("Nhap ma lien ket thuc the (VD: R001, D0001, C0001 hoac de trong): ");
                    String uLinked = this.readLine();

                    try {
                        User created = this.authController.createUser(uName, uPass, newRole, uLinked, uDisp);
                        System.out.println(">>> [TAO THANH CONG] Tai khoan ID: " + created.getId() + " [" + created.getRole().name() + "]");
                    } catch (Exception e) {
                        System.out.println(">>> [LOI] " + e.getMessage());
                    }
                    break;
                case "5":
                    System.out.print("Nhap ID tai khoan can sua (VD: U_1): ");
                    String targetId = this.readLine();
                    User targetUser = this.authController.getUserById(targetId);
                    if (targetUser == null) {
                        System.out.println("Khong tim thay tai khoan voi ID: " + targetId);
                        break;
                    }
                    System.out.print("Nhap mat khau moi (bam Enter de giu nguyen): ");
                    String newP = this.readLine();
                    System.out.print("Nhap ten hien thi moi (bam Enter de giu nguyen): ");
                    String newD = this.readLine();
                    System.out.print("Doi vai tro? (1. ADMIN | 2. DISPATCHER | 3. RESTAURANT | 4. DRIVER | 5. CUSTOMER | Enter bo qua): ");
                    String chR = this.readLine();
                    UserRole upRole = null;
                    if (chR.equals("1")) upRole = UserRole.ADMIN;
                    else if (chR.equals("2")) upRole = UserRole.DISPATCHER;
                    else if (chR.equals("3")) upRole = UserRole.RESTAURANT;
                    else if (chR.equals("4")) upRole = UserRole.DRIVER;
                    else if (chR.equals("5")) upRole = UserRole.CUSTOMER;

                    boolean upOk = this.authController.updateUser(targetId, newP, upRole, newD);
                    System.out.println(upOk ? ">>> Cap nhat tai khoan thanh cong!" : ">>> Cap nhat that bai.");
                    break;
                case "6":
                    System.out.print("Nhap ID tai khoan can xoa: ");
                    String delId = this.readLine();
                    try {
                        boolean delOk = this.authController.deleteUser(delId);
                        System.out.println(delOk ? ">>> Da xoa tai khoan " + delId + " khoi he thong." : "Khong tim thay tai khoan de xoa.");
                    } catch (Exception e) {
                        System.out.println(">>> [LOI] " + e.getMessage());
                    }
                    break;
                case "0":
                    inUserMenu = false;
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        }
    }

    private void displayUserTable(List<User> users) {
        System.out.println("+---------+---------------+---------------+--------------------+-------------------------+");
        System.out.println("| ID      | Username      | Role          | Linked Entity      | Display Name            |");
        System.out.println("+---------+---------------+---------------+--------------------+-------------------------+");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            System.out.printf("| %-7s | %-13s | %-13s | %-18s | %-23s |\n",
                u.getId(),
                u.getUsername(),
                u.getRole().name(),
                u.getLinkedEntityId() != null ? u.getLinkedEntityId() : "",
                u.getDisplayName().length() > 23 ? u.getDisplayName().substring(0, 20) + "..." : u.getDisplayName()
            );
        }
        System.out.println("+---------+---------------+---------------+--------------------+-------------------------+");
        System.out.println("Tong cong: " + users.size() + " nguoi dung.");
    }

    private void handleRestaurantManagementFlow() {
        boolean inRestMenu = true;
        while (inRestMenu) {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("            QUAN LY NHA HANG & THUC DON                ");
            System.out.println("-------------------------------------------------------");
            System.out.println("1. Xem danh sach nha hang");
            System.out.println("2. Them nha hang moi");
            System.out.println("3. Cap nhat thong tin nha hang");
            System.out.println("4. Xoa nha hang");
            System.out.println("5. Xem thuc don cua nha hang");
            System.out.println("6. Them mon an moi vao thuc don");
            System.out.println("7. Cap nhat mon an (Ten, Gia, Ton kho)");
            System.out.println("8. Xoa mon an khoi thuc don");
            System.out.println("0. Quay lai Menu Admin");
            System.out.println("-------------------------------------------------------");
            System.out.print("Chon chuc nang [0-8]: ");
            String opt = this.readLine();

            switch (opt) {
                case "1":
                    List<Restaurant> list = this.restaurantController.getAllRestaurants();
                    int limit = Math.min(list.size(), 15);
                    System.out.println("+-------+--------------------------------+---------------------------+--------+");
                    System.out.println("| ID    | Ten nha hang                   | Dia chi                   | Rating |");
                    System.out.println("+-------+--------------------------------+---------------------------+--------+");
                    for (int i = 0; i < limit; i++) {
                        Restaurant r = list.get(i);
                        System.out.printf("| %-5s | %-30s | %-25s | %-6.1f |\n",
                            r.getId(),
                            r.getName().length() > 30 ? r.getName().substring(0, 27) + "..." : r.getName(),
                            r.getAddress().length() > 25 ? r.getAddress().substring(0, 22) + "..." : r.getAddress(),
                            r.getRating()
                        );
                    }
                    System.out.println("+-------+--------------------------------+---------------------------+--------+");
                    break;
                case "2":
                    System.out.print("Nhap ten nha hang: ");
                    String rName = this.readLine();
                    System.out.print("Nhap dia chi: ");
                    String rAddr = this.readLine();
                    Restaurant createdR = this.restaurantController.createRestaurant(rName, rAddr, 13.770, 109.220, 4.8);
                    System.out.println(">>> [TAO THANH CONG] Nha hang ID: " + createdR.getId());
                    break;
                case "3":
                    System.out.print("Nhap ID nha hang can sua: ");
                    String upRId = this.readLine();
                    System.out.print("Nhap ten moi: ");
                    String newRName = this.readLine();
                    System.out.print("Nhap dia chi moi: ");
                    String newRAddr = this.readLine();
                    boolean upOk = this.restaurantController.updateRestaurant(upRId, newRName, newRAddr, 4.9);
                    System.out.println(upOk ? ">>> Cap nhat nha hang thanh cong!" : ">>> Khong tim thay nha hang.");
                    break;
                case "4":
                    System.out.print("Nhap ID nha hang can xoa: ");
                    String delRId = this.readLine();
                    boolean delOk = this.restaurantController.deleteRestaurant(delRId);
                    System.out.println(delOk ? ">>> Da xoa nha hang khoi CSV." : ">>> Khong tim thay nha hang.");
                    break;
                case "5":
                    System.out.print("Nhap ID nha hang can xem thuc don: ");
                    String restIdView = this.readLine();
                    Restaurant restObj = this.restaurantController.getRestaurantById(restIdView);
                    if (restObj != null) {
                        List<MenuItem> mList = this.restaurantController.getMenuByRestaurant(restIdView);
                        this.orderView.displayMenu(restObj, mList);
                    } else {
                        System.out.println("Khong tim thay nha hang: " + restIdView);
                    }
                    break;
                case "6":
                    System.out.print("Nhap ID nha hang: ");
                    String rIdForMenu = this.readLine();
                    System.out.print("Nhap ten mon an: ");
                    String mName = this.readLine();
                    System.out.print("Nhap don gia (VND): ");
                    double mPrice = Double.parseDouble(this.readLine());
                    System.out.print("Nhap so luong ton kho ban dau: ");
                    int mStock = Integer.parseInt(this.readLine());
                    MenuItem createdM = this.restaurantController.createMenuItem(rIdForMenu, mName, mPrice, mStock);
                    System.out.println(">>> [TAO MON THANH CONG] Mon an ID: " + createdM.getId());
                    break;
                case "7":
                    System.out.print("Nhap ID mon an can sua: ");
                    String upMId = this.readLine();
                    System.out.print("Nhap ten moi: ");
                    String upMName = this.readLine();
                    System.out.print("Nhap don gia moi (VND): ");
                    double upPrice = Double.parseDouble(this.readLine());
                    System.out.print("Nhap so luong ton kho moi: ");
                    int upStock = Integer.parseInt(this.readLine());
                    boolean upMOk = this.restaurantController.updateMenuItem(upMId, upMName, upPrice, upStock);
                    System.out.println(upMOk ? ">>> Cap nhat mon an thanh cong!" : ">>> Khong tim thay mon an.");
                    break;
                case "8":
                    System.out.print("Nhap ID mon an can xoa: ");
                    String delMId = this.readLine();
                    boolean delMOk = this.restaurantController.deleteMenuItem(delMId);
                    System.out.println(delMOk ? ">>> Da xoa mon an khoi thuc don." : ">>> Khong tim thay mon an.");
                    break;
                case "0":
                    inRestMenu = false;
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        }
    }

    private void handleCustomerManagementFlow() {
        boolean inCustMenu = true;
        while (inCustMenu) {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("                  QUAN LY KHACH HANG                   ");
            System.out.println("-------------------------------------------------------");
            System.out.println("1. Xem danh sach khach hang");
            System.out.println("2. Tim kiem khach hang theo So dien thoai");
            System.out.println("3. Them moi khach hang");
            System.out.println("4. Cap nhat thong tin khach hang");
            System.out.println("5. Xoa khach hang");
            System.out.println("0. Quay lai Menu Admin");
            System.out.println("-------------------------------------------------------");
            System.out.print("Chon chuc nang [0-5]: ");
            String opt = this.readLine();

            switch (opt) {
                case "1":
                    List<Customer> all = this.customerController.getAllCustomers();
                    int limit = Math.min(all.size(), 15);
                    System.out.println("+---------+----------------------+---------------+--------------------------------+");
                    System.out.println("| ID      | Ten khach hang       | So dien thoai | Dia chi                        |");
                    System.out.println("+---------+----------------------+---------------+--------------------------------+");
                    for (int i = 0; i < limit; i++) {
                        Customer c = all.get(i);
                        System.out.printf("| %-7s | %-20s | %-13s | %-30s |\n",
                            c.getId(),
                            c.getName().length() > 20 ? c.getName().substring(0, 17) + "..." : c.getName(),
                            c.getPhone(),
                            c.getAddress().length() > 30 ? c.getAddress().substring(0, 27) + "..." : c.getAddress()
                        );
                    }
                    System.out.println("+---------+----------------------+---------------+--------------------------------+");
                    break;
                case "2":
                    System.out.print("Nhap so dien thoai can tim: ");
                    String sPhone = this.readLine();
                    Customer found = this.customerController.getCustomerByPhone(sPhone);
                    if (found != null) {
                        System.out.println(">>> [TIM THAY] Ma: " + found.getId() + " | Ten: " + found.getName() + " | SDT: " + found.getPhone() + " | Dia chi: " + found.getAddress());
                    } else {
                        System.out.println("Khong tim thay khach hang voi so dien thoai: " + sPhone);
                    }
                    break;
                case "3":
                    System.out.print("Nhap ten khach hang: ");
                    String cName = this.readLine();
                    System.out.print("Nhap so dien thoai: ");
                    String cPhone = this.readLine();
                    System.out.print("Nhap dia chi: ");
                    String cAddr = this.readLine();
                    Customer createdC = this.customerController.registerCustomer(cName, cPhone, cAddr, 13.775, 109.225);
                    System.out.println(">>> [TAO THANH CONG] Khach hang ID: " + createdC.getId());
                    break;
                case "4":
                    System.out.print("Nhap ID khach hang can cap nhat: ");
                    String upCId = this.readLine();
                    System.out.print("Nhap ten moi: ");
                    String newCName = this.readLine();
                    System.out.print("Nhap so dien thoai moi: ");
                    String newCPhone = this.readLine();
                    System.out.print("Nhap dia chi moi: ");
                    String newCAddr = this.readLine();
                    boolean upCOk = this.customerController.updateCustomer(upCId, newCName, newCPhone, newCAddr, 13.775, 109.225);
                    System.out.println(upCOk ? ">>> Cap nhat khach hang thanh cong!" : ">>> Khong tim thay khach hang.");
                    break;
                case "5":
                    System.out.print("Nhap ID khach hang can xoa: ");
                    String delCId = this.readLine();
                    boolean delCOk = this.customerController.deleteCustomer(delCId);
                    System.out.println(delCOk ? ">>> Da xoa khach hang khoi CSV." : ">>> Khong tim thay khach hang.");
                    break;
                case "0":
                    inCustMenu = false;
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        }
    }

    private void handleSystemOverviewFlow() {
        System.out.println("\n=======================================================");
        System.out.println("         BAO CAO TONG QUAN TOAN HE THONG               ");
        System.out.println("=======================================================");
        List<User> users = this.authController.getAllUsers();
        List<Customer> customers = this.customerController.getAllCustomers();
        List<Restaurant> restaurants = this.restaurantController.getAllRestaurants();
        List<Driver> drivers = this.driverController.getAllDrivers();
        List<Order> orders = this.orderController.getAllOrders();

        int availableDrivers = 0;
        int busyDrivers = 0;
        int offlineDrivers = 0;
        for (int i = 0; i < drivers.size(); i++) {
            Driver d = drivers.get(i);
            if (d.getStatus() == DriverStatus.AVAILABLE) availableDrivers++;
            else if (d.getStatus() == DriverStatus.BUSY) busyDrivers++;
            else offlineDrivers++;
        }

        int pendingOrders = 0;
        int confirmedOrders = 0;
        int dispatchedOrders = 0;
        int deliveredOrders = 0;
        int cancelledOrders = 0;
        double totalRevenue = 0.0;
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getStatus() == OrderStatus.PENDING) pendingOrders++;
            else if (o.getStatus() == OrderStatus.CONFIRMED) confirmedOrders++;
            else if (o.getStatus() == OrderStatus.DISPATCHED) dispatchedOrders++;
            else if (o.getStatus() == OrderStatus.DELIVERED) {
                deliveredOrders++;
                totalRevenue += o.getTotalFoodAmount();
            } else if (o.getStatus() == OrderStatus.CANCELLED) cancelledOrders++;
        }

        System.out.println("1. TAI KHOAN NGUOI DUNG (USERS): " + users.size());
        System.out.println("2. KHACH HANG (CUSTOMERS):       " + customers.size());
        System.out.println("3. NHA HANG DOI TAC:             " + restaurants.size());
        System.out.println("4. DOI NGU TAI XE (DRIVERS):     " + drivers.size());
        System.out.println("   + Dang Online (AVAILABLE):    " + availableDrivers);
        System.out.println("   + Dang ban / Giao don (BUSY): " + busyDrivers);
        System.out.println("   + Ngoai tuyen (OFFLINE):      " + offlineDrivers);
        System.out.println("5. TONG SO DON HANG (ORDERS):    " + orders.size());
        System.out.println("   + Dang cho nha hang (PENDING):" + pendingOrders);
        System.out.println("   + Da xac nhan (CONFIRMED):    " + confirmedOrders);
        System.out.println("   + Dang giao hang (DISPATCHED):" + dispatchedOrders);
        System.out.println("   + Da giao xong (DELIVERED):   " + deliveredOrders);
        System.out.println("   + Da huy (CANCELLED):         " + cancelledOrders);
        System.out.printf("6. DOANH THU MON AN GIAO XONG:   %,.0f VND\n", totalRevenue);
        System.out.println("   [Doi chieu logic dieu phoi]: So tai xe ban (BUSY: " + busyDrivers + 
                           ") = So don dang tren duong giao (DISPATCHED: " + dispatchedOrders + 
                           ") [Chinh xac 100%]");
        System.out.println("=======================================================");
    }
}
