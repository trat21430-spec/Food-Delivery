package model;

import java.time.LocalDateTime;

public class MenuItem extends BaseEntity {
    private String restaurantId;
    private String name;
    private double price;
    private int stockQty;

    public MenuItem() {
        super();
    }

    public MenuItem(String id, String restaurantId, String name, double price, int stockQty, LocalDateTime createdAt,
            LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.stockQty = stockQty;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                id,
                restaurantId,
                escapeCsv(name),
                String.valueOf(price),
                String.valueOf(stockQty),
                createdAt.toString(),
                updatedAt.toString(),
                String.valueOf(version));
    }

    @Override
    public void fromCsvLine(String csvLine) {
        String[] parts = parseCsvLine(csvLine);
        if (parts.length >= 8) {
            this.id = parts[0];
            this.restaurantId = parts[1];
            this.name = parts[2];
            this.price = Double.parseDouble(parts[3]);
            this.stockQty = Integer.parseInt(parts[4]);
            this.createdAt = LocalDateTime.parse(parts[5]);
            this.updatedAt = LocalDateTime.parse(parts[6]);
            this.version = Long.parseLong(parts[7]);
        }
    }

    private String escapeCsv(String val) {
        if (val == null)
            return "";
        if (val.contains(",") || val.contains("\"")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }

    private String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}
