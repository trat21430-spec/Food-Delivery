package model;

import java.time.LocalDateTime;

public class Order {
    private String orderId;
    private String customerId;
    private String restaurantId;
    private String driverId;
    private OrderStatus status;
    private double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private long version;

    public Order() {
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.version = 1L;
    }

    public Order(String orderId, String customerId, String restaurantId, String driverId, OrderStatus status, double totalAmount, LocalDateTime createdAt, LocalDateTime confirmedAt, long version) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.driverId = driverId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.confirmedAt = confirmedAt;
        this.version = version;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void confirm() {
        this.status = OrderStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
        this.version++;
    }

    public void cancel() {
        if (this.status == OrderStatus.PENDING) {
            this.status = OrderStatus.CANCELLED;
            this.version++;
        }
    }

    public void assignDriver(String driverId) {
        this.driverId = driverId;
        this.status = OrderStatus.ASSIGNED;
        this.version++;
    }

    public void markDelivered() {
        this.status = OrderStatus.DELIVERED;
        this.version++;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", status=" + status +
                ", totalAmount=" + totalAmount +
                ", createdAt=" + createdAt +
                ", version=" + version +
                '}';
    }
}
