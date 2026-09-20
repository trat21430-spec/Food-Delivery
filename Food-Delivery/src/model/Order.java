package model;

import java.time.LocalDateTime;
import model.Enums.OrderStatus;

public class Order extends BaseEntity {
    private String customerId;
    private String restaurantId;
    private String driverId;
    private OrderStatus status;
    private double totalAmount;
    private double deliveryFee;

    public Order() {
        super();
        this.status = OrderStatus.PENDING;
        this.driverId = "";
    }

    public Order(String id, String customerId, String restaurantId, String driverId, OrderStatus status,
            double totalAmount, double deliveryFee, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.driverId = driverId != null ? driverId : "";
        this.status = status != null ? status : OrderStatus.PENDING;
        this.totalAmount = totalAmount;
        this.deliveryFee = deliveryFee;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                id,
                customerId,
                restaurantId,
                escapeCsv(driverId),
                status.name(),
                String.valueOf(totalAmount),
                String.valueOf(deliveryFee),
                createdAt.toString(),
                updatedAt.toString(),
                String.valueOf(version));
    }

    @Override
    public void fromCsvLine(String csvLine) {
        String[] parts = parseCsvLine(csvLine);
        if (parts.length >= 10) {
            this.id = parts[0];
            this.customerId = parts[1];
            this.restaurantId = parts[2];
            this.driverId = parts[3];
            this.status = OrderStatus.valueOf(parts[4]);
            this.totalAmount = Double.parseDouble(parts[5]);
            this.deliveryFee = Double.parseDouble(parts[6]);
            this.createdAt = LocalDateTime.parse(parts[7]);
            this.updatedAt = LocalDateTime.parse(parts[8]);
            this.version = Long.parseLong(parts[9]);
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
