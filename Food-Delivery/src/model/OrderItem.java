package model;

import java.time.LocalDateTime;

public class OrderItem extends BaseEntity {
    private String orderId;
    private String menuItemId;
    private int quantity;
    private double price;

    public OrderItem() {
        super();
    }

    public OrderItem(String id, String orderId, String menuItemId, int quantity, double price, LocalDateTime createdAt,
            LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                id,
                orderId,
                menuItemId,
                String.valueOf(quantity),
                String.valueOf(price),
                createdAt.toString(),
                updatedAt.toString(),
                String.valueOf(version));
    }

    @Override
    public void fromCsvLine(String csvLine) {
        String[] parts = parseCsvLine(csvLine);
        if (parts.length >= 8) {
            this.id = parts[0];
            this.orderId = parts[1];
            this.menuItemId = parts[2];
            this.quantity = Integer.parseInt(parts[3]);
            this.price = Double.parseDouble(parts[4]);
            this.createdAt = LocalDateTime.parse(parts[5]);
            this.updatedAt = LocalDateTime.parse(parts[6]);
            this.version = Long.parseLong(parts[7]);
        }
    }

    private String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}
