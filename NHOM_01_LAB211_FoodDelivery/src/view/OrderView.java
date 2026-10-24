package view;

import model.MenuItem;
import model.Order;
import model.OrderItem;
import util.TextUtil;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderView {
    private TextUtil textUtil;
    private int menuWidth;
    private int ordersWidth;
    private int receiptWidth;
    private int maxOrderRows;

    public OrderView() {
        this.textUtil = new TextUtil();
        this.menuWidth = 63;
        this.ordersWidth = 76;
        this.receiptWidth = 71;
        this.maxOrderRows = 20;
    }

    public void renderMenu(List<MenuItem> items) {
        if (items == null || items.isEmpty()) {
            System.out.println("No menu items available.");
            return;
        }

        String border = this.textUtil.line('-', this.menuWidth);
        System.out.println();
        System.out.println(border);
        System.out.printf(Locale.US, "| %-8s | %-24s | %-10s | %-8s |%n", "ID", "Item Name", "Price ($)", "Stock");
        System.out.println(border);
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            System.out.printf(Locale.US, "| %-8s | %-24s | %-10.2f | %-8d |%n",
                    item.getId(),
                    this.textUtil.truncate(item.getName(), 24),
                    item.getPrice(),
                    item.getStockQty());
        }
        System.out.println(border);
        System.out.println();
    }

    public void renderOrders(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        String border = this.textUtil.line('-', this.ordersWidth);
        System.out.println();
        System.out.println(border);
        System.out.printf(Locale.US, "| %-18s | %-10s | %-10s | %-12s | %-10s |%n",
                "Order ID", "Customer", "Driver", "Status", "Total ($)");
        System.out.println(border);
        int limit = Math.min(orders.size(), this.maxOrderRows);
        for (int i = 0; i < limit; i++) {
            Order o = orders.get(i);
            String driver = (o.getDriverId() == null || o.getDriverId().isEmpty()) ? "UNASSIGNED" : o.getDriverId();
            String status = (o.getStatus() == null) ? "UNKNOWN" : o.getStatus().name();
            System.out.printf(Locale.US, "| %-18s | %-10s | %-10s | %-12s | %-10.2f |%n",
                    this.textUtil.truncate(o.getId(), 18),
                    this.textUtil.truncate(o.getCustomerId(), 10),
                    this.textUtil.truncate(driver, 10),
                    this.textUtil.truncate(status, 12),
                    o.getTotalAmount());
        }
        System.out.println(border);
        System.out.println();
    }

    public void renderOrderDetail(Order order, List<OrderItem> items) {
        this.renderOrderDetail(order, items, null);
    }

    public void renderOrderDetail(Order order, List<OrderItem> items, Map<String, String> menuItemNames) {
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        String doubleLine = this.textUtil.line('=', this.receiptWidth);
        String singleLine = this.textUtil.line('-', this.receiptWidth);
        String driver = (order.getDriverId() == null || order.getDriverId().isEmpty()) ? "UNASSIGNED" : order.getDriverId();

        System.out.println();
        System.out.println(doubleLine);
        System.out.println("                         ORDER RECEIPT                         ");
        System.out.println(doubleLine);
        System.out.println("Order ID     : " + order.getId());
        System.out.println("Customer ID  : " + order.getCustomerId());
        System.out.println("Restaurant ID: " + order.getRestaurantId());
        System.out.println("Driver ID    : " + driver);
        System.out.println("Status       : " + order.getStatus());
        System.out.println("Placed At    : " + order.getCreatedAt());
        System.out.println(singleLine);
        System.out.printf(Locale.US, "| %-28s | %-8s | %-10s | %-10s |%n", "Item Name", "Quantity", "Price ($)", "Subtotal");
        System.out.println(singleLine);
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                OrderItem item = items.get(i);
                String name = (menuItemNames == null) ? item.getMenuItemId()
                        : menuItemNames.getOrDefault(item.getMenuItemId(), item.getMenuItemId());
                double subtotal = item.getPrice() * item.getQuantity();
                System.out.printf(Locale.US, "| %-28s | %-8d | %-10.2f | %-10.2f |%n",
                        this.textUtil.truncate(name, 28),
                        item.getQuantity(),
                        item.getPrice(),
                        subtotal);
            }
        }
        System.out.println(singleLine);
        System.out.printf(Locale.US, "TOTAL AMOUNT : $%.2f%n", order.getTotalAmount());
        System.out.println(doubleLine);
        System.out.println();
    }
}

