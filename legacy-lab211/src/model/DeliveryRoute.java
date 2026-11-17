package model;

import java.time.LocalDateTime;

public class DeliveryRoute extends BaseEntity {
    private String orderId;
    private String driverId;
    private double pickupDistanceKm;
    private double deliveryDistanceKm;
    private int estimatedMinutes;

    public DeliveryRoute() {
        super();
        this.orderId = "";
        this.driverId = "";
        this.pickupDistanceKm = 0.0;
        this.deliveryDistanceKm = 0.0;
        this.estimatedMinutes = 15;
    }

    public DeliveryRoute(String id, String orderId, String driverId,
                         double pickupDistanceKm, double deliveryDistanceKm, int estimatedMinutes) {
        super(id);
        this.orderId = orderId;
        this.driverId = driverId;
        this.pickupDistanceKm = pickupDistanceKm;
        this.deliveryDistanceKm = deliveryDistanceKm;
        this.estimatedMinutes = estimatedMinutes;
    }

    public DeliveryRoute(String id, String orderId, String driverId, double distanceKm, int estimatedMinutes,
                         String routePathJson, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.orderId = orderId;
        this.driverId = driverId;
        this.pickupDistanceKm = 1.0;
        this.deliveryDistanceKm = distanceKm;
        this.estimatedMinutes = estimatedMinutes;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDriverId() {
        return this.driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public double getPickupDistanceKm() {
        return this.pickupDistanceKm;
    }

    public void setPickupDistanceKm(double pickupDistanceKm) {
        this.pickupDistanceKm = pickupDistanceKm;
    }

    public double getDeliveryDistanceKm() {
        return this.deliveryDistanceKm;
    }

    public void setDeliveryDistanceKm(double deliveryDistanceKm) {
        this.deliveryDistanceKm = deliveryDistanceKm;
    }

    public double getDistanceKm() {
        return this.deliveryDistanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.deliveryDistanceKm = distanceKm;
    }

    public int getEstimatedMinutes() {
        return this.estimatedMinutes;
    }

    public void setEstimatedMinutes(int estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                this.getId(),
                this.orderId,
                this.driverId,
                String.format(java.util.Locale.US, "%.3f", this.pickupDistanceKm),
                String.format(java.util.Locale.US, "%.3f", this.deliveryDistanceKm),
                String.valueOf(this.estimatedMinutes));
    }

    @Override
    public void fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length >= 6) {
            this.setId(p[0].trim());
            this.orderId = p[1].trim();
            this.driverId = p[2].trim();
            this.pickupDistanceKm = Double.parseDouble(p[3].trim());
            this.deliveryDistanceKm = Double.parseDouble(p[4].trim());
            this.estimatedMinutes = Integer.parseInt(p[5].trim());
        }
    }
}
