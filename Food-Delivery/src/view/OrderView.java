package View;

import java.util.List;

public class OrderView {
    public void displayRestaurants(List<Restautant>restaurants) {
        System.out.println("=== Restaurants list ===");
        System.out.printf("%-10s | %-20s | %-30s\n", "ID", "Restaurant name", "Locate");
        System.out.println("-------------------------------------");
        System.out.println("-------------------------------------");
    }
    
    public void displayMenu(String restaurantName, List<MenuItem> menuItems) {
        System.out.println("\n=== Menu: " + restaurantName + " ===");
        System.out.printf("%-10s | %-25s |%-10s |%-10s | %-10s\n", "FoodCode", "FoodName", "Price($)", "Inventory");
    }
    
    public void displayOrderReceipt(Order order, List<OrderItem> items) {
        System.out.println("\n==================== Receiption Order ============");
        System.out.println("OrderCode: " + order.getId());
        System.out.println("====================================================");
    }
    
    private String trucate(String text, int maxLen) {
        if (text == null) return "";
        if (text.length() <= maxLen) return text;
        return text.substring(0, maxLen - 3) + "...";
    }
}
