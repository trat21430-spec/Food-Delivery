package model;

import java.time.LocalDateTime;
import model.Enums.DriverStatus;

public class Driver extends BaseEntity {
    private String name;
    private String phone;
    private DriverStatus status;
    private double latitude;
    private double longitude;
    private String currentOrderId;

    public Driver() {
        super();
        this.status = DriverStatus.AVAILABLE;
        this.currentOrderId = "";
    }

    public Driver(String id, String name, String phone, DriverStatus status, double latitude, double longitude,
            String currentOrderId, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.name = name;
        this.phone = phone;
        this.status = status != null ? status : DriverStatus.AVAILABLE;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentOrderId = currentOrderId != null ? currentOrderId : "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCurrentOrderId() {
        return currentOrderId;
    }

    public void setCurrentOrderId(String currentOrderId) {
        this.currentOrderId = currentOrderId;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                id,
                escapeCsv(name),
                escapeCsv(phone),
                status.name(),
                String.valueOf(latitude),
                String.valueOf(longitude),
                escapeCsv(currentOrderId),
                createdAt.toString(),
                updatedAt.toString(),
                String.valueOf(version));
    }

    @Override
    public void fromCsvLine(String csvLine) {
        String[] parts = parseCsvLine(csvLine);
        if (parts.length >= 10) {
            this.id = parts[0];
            this.name = parts[1];
            this.phone = parts[2];
            this.status = DriverStatus.valueOf(parts[3]);
            this.latitude = Double.parseDouble(parts[4]);
            this.longitude = Double.parseDouble(parts[5]);
            this.currentOrderId = parts[6];
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