package view;

import model.MenuItem;
import model.Order;
import model.Restaurant;

import java.util.List;

public class OrderView {

    public void renderMenu(List<MenuItem> items) {
        System.out.println("\n------------------------------------------------------------------");
        System.out.printf("| %-8s | %-28s | %-12s | %-8s |\n", "Ma Mon", "Ten Mon An", "Gia (VND)", "Ton Kho");
        System.out.println("------------------------------------------------------------------");
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            System.out.printf("| %-8s | %-28s | %10.0f VND | %8d |\n",
                    item.getId(), item.getName(), item.getPriceVnd(), item.getStockQty());
        }
        System.out.println("------------------------------------------------------------------\n");
    }

    public void displayMenu(Restaurant restaurant, List<MenuItem> items) {
        if (restaurant != null) {
            System.out.println("\n>>> THUC DON QUAN: [" + restaurant.getId() + "] " + restaurant.getName()
                    + " (" + restaurant.getAddress() + " - Danh gia: " + restaurant.getRating() + "/5.0)");
        }
        this.renderMenu(items);
    }

    public void renderOrders(List<Order> orders) {
        this.displayRecentOrders(orders, 20);
    }

    public void displayRecentOrders(List<Order> orders, int count) {
        System.out.println("\n----------------------------------------------------------------------------------------------------");
        System.out.printf("| %-22s | %-10s | %-10s | %-12s | %-14s | %-12s |\n",
                "Ma Don Hang", "Khach Hang", "Tai Xe", "Trang Thai", "Tien Mon", "Phi Ship");
        System.out.println("----------------------------------------------------------------------------------------------------");
        int limit = Math.min(orders.size(), count);
        for (int i = 0; i < limit; i++) {
            Order o = orders.get(i);
            String driverDisplay = (o.getDriverId() == null || o.getDriverId().isEmpty()) ? "(Cho gan)" : o.getDriverId();
            System.out.printf("| %-22s | %-10s | %-10s | %-12s | %10.0f VND | %8.0f VND |\n",
                    o.getId(), o.getCustomerId(), driverDisplay, o.getStatus().name(),
                    o.getTotalFoodAmount(), o.getDeliveryFee());
        }
        System.out.println("----------------------------------------------------------------------------------------------------\n");
    }
}
