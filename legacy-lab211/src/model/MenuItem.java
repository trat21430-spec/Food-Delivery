package model;

import java.time.LocalDateTime;

/**
 * Critical Hot-Spot Entity #1: Contains stockQty and version for Optimistic Locking.
 */
public class MenuItem extends BaseEntity {
    private String restaurantId;
    private String name;
    private double priceVnd;
    private int stockQty;

    public MenuItem() {
        super();
        this.restaurantId = "";
        this.name = "";
        this.priceVnd = 0.0;
        this.stockQty = 0;
    }

    public MenuItem(String id, String restaurantId, String name, double priceVnd, int stockQty, long version) {
        super(id, LocalDateTime.now(), LocalDateTime.now(), version);
        this.restaurantId = restaurantId;
        this.name = name;
        this.priceVnd = priceVnd;
        this.stockQty = stockQty;
    }

    public MenuItem(String id, String restaurantId, String name, double priceVnd, int stockQty,
                    LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.restaurantId = restaurantId;
        this.name = name;
        this.priceVnd = priceVnd;
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

    public double getPriceVnd() {
        return this.priceVnd;
    }

    public void setPriceVnd(double priceVnd) {
        this.priceVnd = priceVnd;
    }

    public double getPrice() {
        return this.priceVnd;
    }

    public void setPrice(double price) {
        this.priceVnd = price;
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
                this.name,
                String.format(java.util.Locale.US, "%.0f", this.priceVnd),
                String.valueOf(this.stockQty),
                String.valueOf(this.getVersion()));
    }

    @Override
    public void fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length >= 6) {
            this.setId(p[0].trim());
            this.restaurantId = p[1].trim();
            this.name = p[2].trim();
            this.priceVnd = Double.parseDouble(p[3].trim());
            this.stockQty = Integer.parseInt(p[4].trim());
            this.setVersion(Long.parseLong(p[5].trim()));
        }
    }
}
