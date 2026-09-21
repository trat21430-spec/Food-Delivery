package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.MenuItem;

public class MenuItemRepository {

    private static final Path FILE = Path.of("data", "menu_items.csv");

    public List<MenuItem> findAll() {
        List<MenuItem> items = new ArrayList<>();
        for (String line : readLines()) {
            if (line.isBlank() || line.startsWith("itemId")) {
                continue;
            }
            MenuItem item = new MenuItem();
            item.fromCsvLine(line);
            items.add(item);
        }
        return items;
    }

    public MenuItem findById(String itemId) {
        return findAll().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElse(null);
    }

    public List<MenuItem> findByRestaurantId(String restaurantId) {
        return findAll().stream()
                .filter(item -> item.getRestaurantId().equals(restaurantId))
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
        writeLines(items);
        return true;
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
        writeLines(items);
        return true;
    }

    private List<String> readLines() {
        try {
            if (!Files.exists(FILE)) {
                return List.of();
            }
            return Files.readAllLines(FILE);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot read menu_items.csv", ex);
        }
    }

    private void writeLines(List<MenuItem> items) {
        try {
            Files.createDirectories(FILE.getParent());
            Files.write(FILE, items.stream().map(MenuItem::toCsvLine).toList());
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot write menu_items.csv", ex);
        }
    }
}
