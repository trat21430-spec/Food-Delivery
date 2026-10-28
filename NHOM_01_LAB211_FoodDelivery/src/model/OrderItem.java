package model;

import java.time.LocalDateTime;

public class OrderItem extends BaseEntity {
    private String orderId;
    private String menuItemId;
    private int quantity;
    private double price;

    public OrderItem() {
        super();
        this.orderId = "";
        this.menuItemId = "";
        this.quantity = 0;
        this.price = 0.0;
    }

    public OrderItem(String id, String orderId, String menuItemId, int quantity, double price, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMenuItemId() {
        return this.menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
            this.getId(),
            this.orderId,
            this.menuItemId,
            String.valueOf(this.quantity),
            String.valueOf(this.price),
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
            this.orderId = parts[1];
            this.menuItemId = parts[2];
            this.quantity = Integer.parseInt(parts[3]);
            this.price = Double.parseDouble(parts[4]);
            this.setCreatedAt(LocalDateTime.parse(parts[5]));
            this.setUpdatedAt(LocalDateTime.parse(parts[6]));
            this.setVersion(Long.parseLong(parts[7]));
        }
    }

    private String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}
