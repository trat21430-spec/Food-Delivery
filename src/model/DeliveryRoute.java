package model;

import util.GeoUtils;

public class DeliveryRoute {
    private String routeId;
    private String orderId;
    private String driverId;
    private double distanceKm;
    private int estimatedMinutes;

    public DeliveryRoute() {
    }

    public DeliveryRoute(String routeId, String orderId, String driverId, double distanceKm, int estimatedMinutes) {
        this.routeId = routeId;
        this.orderId = orderId;
        this.driverId = driverId;
        this.distanceKm = distanceKm;
        this.estimatedMinutes = estimatedMinutes;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(int estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        this.distanceKm = GeoUtils.haversine(lat1, lon1, lat2, lon2);
        this.estimatedMinutes = (int) Math.ceil(this.distanceKm * 3.0); // 3 mins per km estimate
        return this.distanceKm;
    }

    @Override
    public String toString() {
        return "DeliveryRoute{" +
                "routeId='" + routeId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", distanceKm=" + distanceKm +
                ", estimatedMinutes=" + estimatedMinutes +
                '}';
    }
}
