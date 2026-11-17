package model;

import java.time.LocalDateTime;

public class OrderItem extends BaseEntity {
    private String orderId;
    private String menuItemId;
    private int quantity;
    private double unitPriceVnd;

    public OrderItem() {
        super();
        this.orderId = "";
        this.menuItemId = "";
        this.quantity = 0;
        this.unitPriceVnd = 0.0;
    }

    public OrderItem(String id, String orderId, String menuItemId, int quantity, double unitPriceVnd) {
        super(id);
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.unitPriceVnd = unitPriceVnd;
    }

    public OrderItem(String id, String orderId, String menuItemId, int quantity, double unitPriceVnd,
                     LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.unitPriceVnd = unitPriceVnd;
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

    public double getUnitPriceVnd() {
        return this.unitPriceVnd;
    }

    public void setUnitPriceVnd(double unitPriceVnd) {
        this.unitPriceVnd = unitPriceVnd;
    }

    public double getPrice() {
        return this.unitPriceVnd;
    }

    public void setPrice(double price) {
        this.unitPriceVnd = price;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                this.getId(),
                this.orderId,
                this.menuItemId,
                String.valueOf(this.quantity),
                String.format(java.util.Locale.US, "%.0f", this.unitPriceVnd));
    }

    @Override
    public void fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length >= 5) {
            this.setId(p[0].trim());
            this.orderId = p[1].trim();
            this.menuItemId = p[2].trim();
            this.quantity = Integer.parseInt(p[3].trim());
            this.unitPriceVnd = Double.parseDouble(p[4].trim());
        }
    }
}
