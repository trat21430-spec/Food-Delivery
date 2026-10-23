package model;

import java.time.LocalDateTime;

public class MenuItem extends BaseEntity {
    private String restaurantId;
    private String name;
    private double price;
    private int stockQty;

    public MenuItem() {
        super();
        this.restaurantId = "";
        this.name = "";
        this.price = 0.0;
        this.stockQty = 0;
    }

    public MenuItem(String id, String restaurantId, String name, double price, int stockQty, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.stockQty = stockQty;
    }

    public String getRestaurantId() {
        return this.restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQty() {
        return this.stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
            this.getId(),
            this.restaurantId,
            this.escapeCsv(this.name),
            String.valueOf(this.price),
            String.valueOf(this.stockQty),
            this.getCreatedAt().toString(),
            this.getUpdatedAt().toString(),
            String.valueOf(this.getVersion())
        );
    }

    @Override
    public void fromCsvLine(String csvLine) {
        String[] parts = this.parseCsvLine(csvLine);
        if (parts.length >= 8) {
            this.setId(parts[0]);
            this.restaurantId = parts[1];
            this.name = parts[2];
            this.price = Double.parseDouble(parts[3]);
            this.stockQty = Integer.parseInt(parts[4]);
            this.setCreatedAt(LocalDateTime.parse(parts[5]));
            this.setUpdatedAt(LocalDateTime.parse(parts[6]));
            this.setVersion(Long.parseLong(parts[7]));
        }
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }

    private String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}
