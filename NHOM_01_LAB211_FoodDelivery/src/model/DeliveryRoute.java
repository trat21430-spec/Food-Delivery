package model;

import java.time.LocalDateTime;

public class DeliveryRoute extends BaseEntity {
    private String orderId;
    private String driverId;
    private double distanceKm;
    private int estimatedMinutes;
    private String routePathJson;

    public DeliveryRoute() {
        super();
        this.orderId = "";
        this.driverId = "";
        this.distanceKm = 0.0;
        this.estimatedMinutes = 0;
        this.routePathJson = "[]";
    }

    public DeliveryRoute(String id, String orderId, String driverId, double distanceKm, int estimatedMinutes, String routePathJson, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.orderId = orderId;
        this.driverId = driverId;
        this.distanceKm = distanceKm;
        this.estimatedMinutes = estimatedMinutes;
        this.routePathJson = routePathJson != null ? routePathJson : "[]";
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

    public double getDistanceKm() {
        return this.distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public int getEstimatedMinutes() {
        return this.estimatedMinutes;
    }

    public void setEstimatedMinutes(int estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public String getRoutePathJson() {
        return this.routePathJson;
    }

    public void setRoutePathJson(String routePathJson) {
        this.routePathJson = routePathJson;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
            this.getId(),
            this.orderId,
            this.driverId,
            String.valueOf(this.distanceKm),
            String.valueOf(this.estimatedMinutes),
            this.escapeCsv(this.routePathJson),
            this.getCreatedAt().toString(),
            this.getUpdatedAt().toString(),
            String.valueOf(this.getVersion())
        );
    }

    @Override
    public void fromCsvLine(String csvLine) {
        String[] parts = this.parseCsvLine(csvLine);
        if (parts.length >= 9) {
            this.setId(parts[0]);
            this.orderId = parts[1];
            this.driverId = parts[2];
            this.distanceKm = Double.parseDouble(parts[3]);
            this.estimatedMinutes = Integer.parseInt(parts[4]);
            this.routePathJson = parts[5];
            this.setCreatedAt(LocalDateTime.parse(parts[6]));
            this.setUpdatedAt(LocalDateTime.parse(parts[7]));
            this.setVersion(Long.parseLong(parts[8]));
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
