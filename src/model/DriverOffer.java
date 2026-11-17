package model;

/**
 * Driver Offer Object (Trang 13 Master Plan - Mục 6.1 Driver Dispatch Flow).
 * Represents a timed offer (15-30 seconds timeout) sent to a candidate driver.
 */
public class DriverOffer {
    private String offerId;
    private String taskId;
    private String orderId;
    private String driverId;
    private String driverName;
    private String restaurantId;
    private String restaurantName;
    private String pickupAddress;
    private String dropoffAddress;
    private double distanceKm;
    private long shippingFeeVnd;
    private double matchScore;
    private long sentAtMs;
    private long expiresAtMs; // Default 20 seconds from sentAtMs
    private String status; // PENDING, ACCEPTED, REJECTED, EXPIRED

    public DriverOffer() {
        this.sentAtMs = System.currentTimeMillis();
        this.expiresAtMs = this.sentAtMs + (20 * 1000L); // 20s
        this.status = "PENDING";
    }

    public DriverOffer(String offerId, String taskId, String orderId, String driverId, String driverName,
                       String restaurantId, String restaurantName, String pickupAddress, String dropoffAddress,
                       double distanceKm, long shippingFeeVnd, double matchScore) {
        this.offerId = offerId;
        this.taskId = taskId;
        this.orderId = orderId;
        this.driverId = driverId;
        this.driverName = driverName;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.pickupAddress = pickupAddress;
        this.dropoffAddress = dropoffAddress;
        this.distanceKm = distanceKm;
        this.shippingFeeVnd = shippingFeeVnd;
        this.matchScore = matchScore;
        this.sentAtMs = System.currentTimeMillis();
        this.expiresAtMs = this.sentAtMs + (20 * 1000L); // 20 seconds countdown
        this.status = "PENDING";
    }

    public boolean isExpired() {
        return "PENDING".equalsIgnoreCase(this.status) && (System.currentTimeMillis() > this.expiresAtMs);
    }

    public long getRemainingSeconds() {
        long diff = this.expiresAtMs - System.currentTimeMillis();
        return diff > 0 ? (diff / 1000L) : 0L;
    }

    public String getId() { return this.offerId; }
    public String getDeliveryTaskId() { return this.taskId; }
    public String getRestaurantAddress() { return this.pickupAddress; }
    public String getDeliveryAddress() { return this.dropoffAddress; }
    public long getDeliveryFeeVnd() { return this.shippingFeeVnd; }
    public double getEstimatedDistanceKm() { return this.distanceKm; }
    public long getOfferedAtEpochMs() { return this.sentAtMs; }
    public long getExpiresAtEpochMs() { return this.expiresAtMs; }

    public String getOfferId() { return this.offerId; }
    public void setOfferId(String offerId) { this.offerId = offerId; }

    public String getTaskId() { return this.taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getOrderId() { return this.orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getDriverId() { return this.driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }

    public String getDriverName() { return this.driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getRestaurantId() { return this.restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public String getRestaurantName() { return this.restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public String getPickupAddress() { return this.pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getDropoffAddress() { return this.dropoffAddress; }
    public void setDropoffAddress(String dropoffAddress) { this.dropoffAddress = dropoffAddress; }

    public double getDistanceKm() { return this.distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public long getShippingFeeVnd() { return this.shippingFeeVnd; }
    public void setShippingFeeVnd(long shippingFeeVnd) { this.shippingFeeVnd = shippingFeeVnd; }

    public double getMatchScore() { return this.matchScore; }
    public void setMatchScore(double matchScore) { this.matchScore = matchScore; }

    public long getSentAtMs() { return this.sentAtMs; }
    public void setSentAtMs(long sentAtMs) { this.sentAtMs = sentAtMs; }

    public long getExpiresAtMs() { return this.expiresAtMs; }
    public void setExpiresAtMs(long expiresAtMs) { this.expiresAtMs = expiresAtMs; }

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
}
