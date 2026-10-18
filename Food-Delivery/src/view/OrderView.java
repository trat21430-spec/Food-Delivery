package view;

import model.MenuItem;
import model.Order;
import java.util.List;

public class OrderView {
    public void displayMenu(String restaurantName, List<MenuItem> items) {
        System.out.println("\n=== MENU FOR RESTAURANT: " + restaurantName + " ===");
        System.out.printf("%-10s | %-25s | %-10s | %-8s | %-8s\n", "ID", "Name", "Price ($)", "Stock", "Version");
        System.out.println("------------------------------------------------------------------");
        for (MenuItem item : items) {
            System.out.printf("%-10s | %-25s | $%-9.2f | %-8d | %-8d\n",
                    item.getId(), item.getName(), item.getPrice(), item.getStockQty(), item.getVersion());
        }
        System.out.println();
    }

    public void displayOrders(List<Order> orders) {
        System.out.println("\n=== SYSTEM ORDERS LIST ===");
        System.out.printf("%-18s | %-12s | %-12s | %-12s | %-12s | %-10s\n", "Order ID", "Customer", "Restaurant",
                "Driver", "Status", "Amount");
        System.out.println("----------------------------------------------------------------------------------------");
        for (Order o : orders) {
            System.out.printf("%-18s | %-12s | %-12s | %-12s | %-12s | $%-9.2f\n",
                    o.getId(), o.getCustomerId(), o.getRestaurantId(),
                    o.getDriverId().isEmpty() ? "UNASSIGNED" : o.getDriverId(), o.getStatus(), o.getTotalAmount());
        }
        System.out.println();
    }
}