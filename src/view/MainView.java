package view;

import controller.OrderController;
import model.MenuItem;
import model.Order;
import repository.DriverRepository;
import repository.MenuItemRepository;

import java.util.List;
import java.util.Scanner;

public class MainView {
    private final OrderController orderController;
    private final MenuItemRepository menuRepo;
    private final DriverRepository driverRepo;

    public MainView(OrderController orderController, MenuItemRepository menuRepo, DriverRepository driverRepo) {
        this.orderController = orderController;
        this.menuRepo = menuRepo;
        this.driverRepo = driverRepo;
    }

    public void startMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n========= 🍔 FOOD DELIVERY SIMULATION (MVC) =========");
            System.out.println("1. Xem Danh Sach Menu (View Menu)");
            System.out.println("2. Customer Dat Hang (Place Order)");
            System.out.println("3. Restaurant Xac Nhan Don (Confirm Order)");
            System.out.println("4. Dispatcher Dieu Phoi Driver (Dispatch Order)");
            System.out.println("5. Xem Tat Ca Don Hang (View All Orders)");
            System.out.println("0. Thoat (Exit)");
            System.out.print("Chon chuc nang (0-5): ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    displayMenu();
                    break;
                case "2":
                    handlePlaceOrder(scanner);
                    break;
                case "3":
                    handleConfirmOrder(scanner);
                    break;
                case "4":
                    handleDispatchOrder(scanner);
                    break;
                case "5":
                    displayAllOrders();
                    break;
                case "0":
                    System.out.println("👋 Cam on ban da su dung he thong!");
                    return;
                default:
                    System.out.println("⚠️ Lua chon khong hop le, vui long thu lai!");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n--- 🍽️ DANH SÁCH MÓN ĂN ---");
        List<MenuItem> items = menuRepo.findAll();
        for (MenuItem item : items) {
            System.out.printf("ID: %-8s | Ten: %-20s | Gia: %,.0f VNĐ | Stock: %d\n",
                    item.getItemId(), item.getName(), item.getPrice(), item.getStock());
        }
    }

    private void handlePlaceOrder(Scanner scanner) {
        System.out.println("\n--- 🛒 CUSTOMER ĐẶT HÀNG ---");
        displayMenu();
        System.out.print("Nhap Sub Customer ID (vd: CUST01): ");
        String custId = scanner.nextLine();
        System.out.print("Nhap Item ID muon mua: ");
        String itemId = scanner.nextLine();
        System.out.print("Nhap So Luong: ");
        int qty = Integer.parseInt(scanner.nextLine());

        MenuItem item = menuRepo.findById(itemId);
        if (item == null) {
            System.out.println("❌ Khong tim thay mon an!");
            return;
        }

        orderController.placeOrder(custId, item.getRestaurantId(), itemId, qty, item.getPrice());
    }

    private void handleConfirmOrder(Scanner scanner) {
        System.out.println("\n--- ✅ RESTAURANT XÁC NHẬN ĐƠN ---");
        displayAllOrders();
        System.out.print("Nhap Order ID can xac nhan: ");
        String orderId = scanner.nextLine();
        orderController.confirmOrder(orderId);
    }

    private void handleDispatchOrder(Scanner scanner) {
        System.out.println("\n--- 📡 DISPATCHER ĐIỀU PHỐI DRIVER ---");
        displayAllOrders();
        System.out.print("Nhap Order ID can dieu phoi Driver: ");
        String orderId = scanner.nextLine();
        
        // Vi tri Restaurant gia lap
        double restLat = 10.7769;
        double restLon = 10.6900;
        
        orderController.dispatchOrder(orderId, restLat, restLon);
    }

    private void displayAllOrders() {
        System.out.println("\n--- 📦 DANH SÁCH ĐƠN HÀNG ---");
        List<Order> orders = orderController.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Chua co don hang nao.");
            return;
        }
        for (Order o : orders) {
            System.out.printf("Order ID: %-12s | Cust: %-8s | Status: %-10s | Driver: %-8s | Total: %,.0f VNĐ\n",
                    o.getOrderId(), o.getCustomerId(), o.getStatus(),
                    (o.getDriverId() == null ? "None" : o.getDriverId()), o.getTotalAmount());
        }
    }
}
