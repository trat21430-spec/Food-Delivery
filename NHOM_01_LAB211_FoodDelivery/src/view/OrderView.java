package view;

import model.MenuItem;
import model.Order;

import java.util.List;

public class OrderView {

    public void renderMenu(List<MenuItem> items) {
        System.out.println("\n------------------------------------------------------------------");
        System.out.printf("| %-8s | %-24s | %-10s | %-8s |\n", "ID", "Item Name", "Price ($)", "Stock");
        System.out.println("------------------------------------------------------------------");
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            System.out.printf("| %-8s | %-24s | %-10.2f | %-8d |\n",
                    item.getId(), item.getName(), item.getPrice(), item.getStockQty());
        }
        System.out.println("------------------------------------------------------------------\n");
    }

    public void renderOrders(List<Order> orders) {
        System.out.println("\n----------------------------------------------------------------------------------");
        System.out.printf("| %-18s | %-10s | %-10s | %-12s | %-10s |\n", "Order ID", "Customer", "Driver", "Status", "Total ($)");
        System.out.println("----------------------------------------------------------------------------------");
        int limit = Math.min(orders.size(), 20);
        for (int i = 0; i < limit; i++) {
            Order o = orders.get(i);
            System.out.printf("| %-18s | %-10s | %-10s | %-12s | %-10.2f |\n",
                    o.getId(), o.getCustomerId(), o.getDriverId(), o.getStatus().name(), o.getTotalAmount());
        }
        System.out.println("----------------------------------------------------------------------------------\n");
    }
}
