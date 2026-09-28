package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.Enums.LockMechanism;
import model.MenuItem;

public class MenuItemRepository extends CsvRepository<MenuItem> {

    public MenuItemRepository() {
        super("data/menu_items.csv");
    }

    public List<MenuItem> findByRestaurantId(String restaurantId) {
        return findAll().stream()
                .filter(item -> item.getRestaurantId() != null && item.getRestaurantId().equals(restaurantId))
                .toList();
    }

    public boolean checkStock(String itemId, int quantity) {
        MenuItem item = findById(itemId);
        return item != null && quantity > 0 && item.getStockQty() >= quantity;
    }

    public synchronized boolean deductStock(String itemId, int quantity) {
        List<MenuItem> items = findAll();
        MenuItem item = items.stream()
                .filter(menuItem -> menuItem.getId().equals(itemId))
                .findFirst()
                .orElse(null);

        if (item == null || quantity <= 0 || item.getStockQty() < quantity) {
            return false;
        }

        item.setStockQty(item.getStockQty() - quantity);
        saveAll(items);
        return true;
    }

    public synchronized boolean deductStock(String itemId, int quantity, LockMechanism mechanism) {
        return deductStock(itemId, quantity);
    }

    public synchronized boolean updateStock(String itemId, int newStock) {
        if (newStock < 0) {
            return false;
        }

        List<MenuItem> items = findAll();
        MenuItem item = items.stream()
                .filter(menuItem -> menuItem.getId().equals(itemId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            return false;
        }

        item.setStockQty(newStock);
        saveAll(items);
        return true;
    }

    public synchronized boolean updateStock(String itemId, int newStock, LockMechanism mechanism) {
        return updateStock(itemId, newStock);
    }

    @Override
    protected MenuItem fromCsvLine(String line) {
        MenuItem item = new MenuItem();
        String[] parts = line.split(",(?=(?:[^"]*\"[^"]*\")*[^"]*$)");
        if (parts.length < 8) {
            return item;
        }
        item.setId(parts[0].trim());
        item.setRestaurantId(parts[1].trim());
        item.setName(parts[2].trim());
        item.setPrice(Double.parseDouble(parts[3].trim()));
        item.setStockQty(Integer.parseInt(parts[4].trim()));
        item.setCreatedAt(java.time.LocalDateTime.parse(parts[5].trim()));
        item.setUpdatedAt(java.time.LocalDateTime.parse(parts[6].trim()));
        item.setVersion(Long.parseLong(parts[7].trim()));
        return item;
    }

    @Override
    protected String toCsvLine(MenuItem item) {
        return String.join(",",
                item.getId(),
                item.getRestaurantId(),
                item.getName(),
                String.valueOf(item.getPrice()),
                String.valueOf(item.getStockQty()),
                item.getCreatedAt().toString(),
                item.getUpdatedAt().toString(),
                String.valueOf(item.getVersion()));
    }

    @Override
    protected String getHeader() {
        return "id,restaurantId,name,price,stockQty,createdAt,updatedAt,version";
    }
}
