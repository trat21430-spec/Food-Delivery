package model;

import java.time.LocalDateTime;

public class Driver extends BaseEntity {
    private String name;
    private String phone;
    private DriverStatus status;
    private double latitude;
    private double longitude;
    private String currentOrderId;

    public Driver() {
        super();
        this.name = "";
        this.phone = "";
        this.status = DriverStatus.AVAILABLE;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.currentOrderId = "";
    }

    public Driver(String id, String name, String phone, DriverStatus status, double latitude, double longitude, String currentOrderId, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.name = name;
        this.phone = phone;
        this.status = status != null ? status : DriverStatus.AVAILABLE;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentOrderId = currentOrderId != null ? currentOrderId : "";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public DriverStatus getStatus() {
        return this.status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCurrentOrderId() {
        return this.currentOrderId;
    }

    public void setCurrentOrderId(String currentOrderId) {
        this.currentOrderId = currentOrderId;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
            this.getId(),
            this.escapeCsv(this.name),
            this.escapeCsv(this.phone),
            this.status.name(),
            String.valueOf(this.latitude),
            String.valueOf(this.longitude),
            this.escapeCsv(this.currentOrderId),
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
            this.name = parts[1];
            this.phone = parts[2];
            this.status = DriverStatus.valueOf(parts[3]);
            this.latitude = Double.parseDouble(parts[4]);
            this.longitude = Double.parseDouble(parts[5]);
            this.currentOrderId = parts[6];
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
