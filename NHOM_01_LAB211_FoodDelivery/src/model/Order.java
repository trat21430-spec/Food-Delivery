package model;

import java.time.LocalDateTime;

public class Order extends BaseEntity {
    private String customerId;
    private String restaurantId;
    private String driverId;
    private OrderStatus status;
    private double totalAmount;
    private double deliveryFee;

    public Order() {
        super();
        this.customerId = "";
        this.restaurantId = "";
        this.status = OrderStatus.PENDING;
        this.driverId = "";
        this.totalAmount = 0.0;
        this.deliveryFee = 0.0;
    }

    public Order(String id, String customerId, String restaurantId, String driverId, OrderStatus status, double totalAmount, double deliveryFee, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.driverId = driverId != null ? driverId : "";
        this.status = status != null ? status : OrderStatus.PENDING;
        this.totalAmount = totalAmount;
        this.deliveryFee = deliveryFee;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRestaurantId() {
        return this.restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDriverId() {
        return this.driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getDeliveryFee() {
        return this.deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
            this.getId(),
            this.customerId,
            this.restaurantId,
            this.escapeCsv(this.driverId),
            this.status.name(),
            String.valueOf(this.totalAmount),
            String.valueOf(this.deliveryFee),
            this.getCreatedAt().toString(),
            this.getUpdatedAt().toString(),
            String.valueOf(this.getVersion())
        );
    }

    @Override
    public void fromCsvLine(String csvLine) {
        String[] parts = this.parseCsvLine(csvLine);
        if (parts.length >= 10) {
            this.setId(parts[0]);
            this.customerId = parts[1];
            this.restaurantId = parts[2];
            this.driverId = parts[3];
            this.status = OrderStatus.valueOf(parts[4]);
            this.totalAmount = Double.parseDouble(parts[5]);
            this.deliveryFee = Double.parseDouble(parts[6]);
            this.setCreatedAt(LocalDateTime.parse(parts[7]));
            this.setUpdatedAt(LocalDateTime.parse(parts[8]));
            this.setVersion(Long.parseLong(parts[9]));
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
