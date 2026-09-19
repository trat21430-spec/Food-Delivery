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
        @Override
    public String toCsvLine() {
        return String.join(",",
            id,
            orderId,
            driverId,
            String.valueOf(distanceKm),
            String.valueOf(estimatedMinutes),
            escapeCsv(routePathJson),
            createdAt.toString(),
            updatedAt.toString(),
            String.valueOf(version)
        );
    }
    @Override
    public void fromCsvLine(String csvLine) {
        String[] parts = parseCsvLine(csvLine);
        if (parts.length >= 9) {
            this.id = parts[0];
            this.orderId = parts[1];
            this.driverId = parts[2];
            this.distanceKm = Double.parseDouble(parts[3]);
            this.estimatedMinutes = Integer.parseInt(parts[4]);
            this.routePathJson = parts[5];
            this.createdAt = LocalDateTime.parse(parts[6]);
            this.updatedAt = LocalDateTime.parse(parts[7]);
            this.version = Long.parseLong(parts[8]);
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
