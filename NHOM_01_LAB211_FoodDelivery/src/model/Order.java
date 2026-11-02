package model;

import java.time.LocalDateTime;

/**
 * Critical Hot-Spot Entity #3: Contains driverId, status, assignHistory, and version.
 */
public class Order extends BaseEntity {
    private String customerId;
    private String restaurantId;
    private String driverId;
    private OrderStatus status;
    private double totalFoodAmount;
    private double deliveryFee;
    private long createdAtEpochMs;
    private int assignCount;

    public Order() {
        super();
        this.customerId = "";
        this.restaurantId = "";
        this.driverId = "";
        this.status = OrderStatus.PENDING;
        this.totalFoodAmount = 0.0;
        this.deliveryFee = 15000.0;
        this.createdAtEpochMs = System.currentTimeMillis();
        this.assignCount = 0;
    }

    public Order(String id, String customerId, String restaurantId, String driverId,
                 OrderStatus status, double totalFoodAmount, double deliveryFee,
                 long createdAtEpochMs, int assignCount, long version) {
        super(id, LocalDateTime.now(), LocalDateTime.now(), version);
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.driverId = driverId == null ? "" : driverId;
        this.status = status;
        this.totalFoodAmount = totalFoodAmount;
        this.deliveryFee = deliveryFee;
        this.createdAtEpochMs = createdAtEpochMs;
        this.assignCount = assignCount;
    }

    public Order(String id, String customerId, String restaurantId, String driverId,
                 OrderStatus status, double totalFoodAmount, double deliveryFee,
                 LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.driverId = driverId == null ? "" : driverId;
        this.status = status;
        this.totalFoodAmount = totalFoodAmount;
        this.deliveryFee = deliveryFee;
        this.createdAtEpochMs = System.currentTimeMillis();
        this.assignCount = (driverId != null && !driverId.isEmpty()) ? 1 : 0;
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
        return this.driverId != null ? this.driverId : "";
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId == null ? "" : driverId;
        if (!this.driverId.isEmpty()) {
            this.assignCount++;
        }
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalFoodAmount() {
        return this.totalFoodAmount;
    }

    public void setTotalFoodAmount(double totalFoodAmount) {
        this.totalFoodAmount = totalFoodAmount;
    }

    public double getDeliveryFee() {
        return this.deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getTotalAmount() {
        return this.totalFoodAmount + this.deliveryFee;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalFoodAmount = totalAmount;
    }

    public long getCreatedAtEpochMs() {
        return this.createdAtEpochMs;
    }

    public void setCreatedAtEpochMs(long createdAtEpochMs) {
        this.createdAtEpochMs = createdAtEpochMs;
    }

    public int getAssignCount() {
        return this.assignCount;
    }

    public void setAssignCount(int assignCount) {
        this.assignCount = assignCount;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                this.getId(),
                this.customerId,
                this.restaurantId,
                (this.driverId == null ? "" : this.driverId),
                this.status.name(),
                String.format(java.util.Locale.US, "%.0f", this.totalFoodAmount),
                String.format(java.util.Locale.US, "%.0f", this.deliveryFee),
                String.valueOf(this.createdAtEpochMs),
                String.valueOf(this.assignCount),
                String.valueOf(this.getVersion()));
    }

    @Override
    public void fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length >= 10) {
            this.setId(p[0].trim());
            this.customerId = p[1].trim();
            this.restaurantId = p[2].trim();
            this.driverId = p[3].trim();
            this.status = OrderStatus.valueOf(p[4].trim());
            this.totalFoodAmount = Double.parseDouble(p[5].trim());
            this.deliveryFee = Double.parseDouble(p[6].trim());
            this.createdAtEpochMs = Long.parseLong(p[7].trim());
            this.assignCount = Integer.parseInt(p[8].trim());
            this.setVersion(Long.parseLong(p[9].trim()));
        }
    }
}
