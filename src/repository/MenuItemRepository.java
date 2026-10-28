package repository;

import model.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MenuItemRepository {
    private final ConcurrentHashMap<String, MenuItem> menuMap = new ConcurrentHashMap<>();

    public MenuItemRepository() {
        // Initialize dummy sample data for testing
        save(new MenuItem("ITEM01", "REST01", "Burger Bo Dac Biet", 75000.0, 10, 1L));
        save(new MenuItem("ITEM02", "REST01", "Pizza Hai San", 120000.0, 5, 1L));
        save(new MenuItem("ITEM03", "REST01", "Tra Sua Tran Chau", 35000.0, 20, 1L));
    }

    public void save(MenuItem item) {
        menuMap.put(item.getItemId(), item);
    }

    public MenuItem findById(String itemId) {
        return menuMap.get(itemId);
    }

    public List<MenuItem> findAll() {
        return new ArrayList<>(menuMap.values());
    }

    /**
     * CRITICAL BUSINESS LOGIC AT REPOSITORY LAYER:
     * Check stock and deduct stock safely (Preventing Oversell race condition).
     */
    public synchronized boolean checkAndDeductStock(String itemId, int quantity) {
        MenuItem item = menuMap.get(itemId);
        if (item != null && item.isAvailable(quantity)) {
            return item.deductStock(quantity);
        }
        return false;
    }
}
