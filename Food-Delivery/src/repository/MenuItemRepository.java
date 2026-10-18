
package repository;

import model.MenuItem;

public class MenuItemRepository extends CsvRepository<MenuItem> {

    public MenuItemRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected MenuItem fromCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",", -1);

        if (parts.length < 5) {
            return null;
        }

        try {
            MenuItem item = new MenuItem();

            item.setId(parts[0].trim());
            item.setRestaurantId(parts[1].trim());
            item.setName(parts[2].trim());
            item.setPrice(Double.parseDouble(parts[3].trim()));
            item.setStockQty(Integer.parseInt(parts[4].trim()));

            return item;

        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected String toCsvLine(MenuItem item) {
        return String.join(",",
                item.getId(),
                item.getRestaurantId(),
                item.getName(),
                String.valueOf(item.getPrice()),
                String.valueOf(item.getStockQty())
        );
    }

    @Override
    protected String getHeader() {
        return "id,restaurantId,name,price,stockQty";
    }
}
