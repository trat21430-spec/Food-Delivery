package model;

import java.time.LocalDateTime;

/**
 * Critical Hot-Spot Entity #2: Contains DriverStatus, activeOrderCount, and version.
 */
public class Driver extends BaseEntity {
    private String fullName;
    private String phone;
    private String licensePlate;
    private double latitude;
    private double longitude;
    private DriverStatus status;
    private int activeOrderCount;
    private String currentOrderId;

    public Driver() {
        super();
        this.fullName = "";
        this.phone = "";
        this.licensePlate = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.status = DriverStatus.AVAILABLE;
        this.activeOrderCount = 0;
        this.currentOrderId = "";
    }

    public Driver(String id, String fullName, String phone, String licensePlate,
                  double latitude, double longitude, DriverStatus status, int activeOrderCount, long version) {
        super(id, LocalDateTime.now(), LocalDateTime.now(), version);
        this.fullName = fullName;
        this.phone = phone;
        this.licensePlate = licensePlate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.activeOrderCount = activeOrderCount;
        this.currentOrderId = "";
    }

    public Driver(String id, String name, String phone, DriverStatus status,
                  double latitude, double longitude, String currentOrderId,
                  LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.fullName = name;
        this.phone = phone;
        this.licensePlate = "77-L1 " + (100 + (int)(Math.random() * 899)) + "." + (10 + (int)(Math.random() * 89));
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.activeOrderCount = (status == DriverStatus.BUSY ? 1 : 0);
        this.currentOrderId = currentOrderId != null ? currentOrderId : "";
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return this.fullName;
    }

    public void setName(String name) {
        this.fullName = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicensePlate() {
        return this.licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
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

    public DriverStatus getStatus() {
        return this.status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
        if (status == DriverStatus.AVAILABLE || status == DriverStatus.OFFLINE) {
            this.activeOrderCount = 0;
            this.currentOrderId = "";
        } else if (status == DriverStatus.BUSY) {
            this.activeOrderCount = 1;
        }
    }

    public int getActiveOrderCount() {
        return this.activeOrderCount;
    }

    public void setActiveOrderCount(int activeOrderCount) {
        this.activeOrderCount = activeOrderCount;
    }

    public String getCurrentOrderId() {
        return this.currentOrderId != null ? this.currentOrderId : "";
    }

    public void setCurrentOrderId(String currentOrderId) {
        this.currentOrderId = currentOrderId;
        if (currentOrderId != null && !currentOrderId.trim().isEmpty()) {
            this.activeOrderCount = 1;
        }
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                this.getId(),
                this.fullName,
                this.phone,
                this.licensePlate,
                String.format(java.util.Locale.US, "%.6f", this.latitude),
                String.format(java.util.Locale.US, "%.6f", this.longitude),
                this.status.name(),
                String.valueOf(this.activeOrderCount),
                String.valueOf(this.getVersion()));
    }

    @Override
    public void fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length >= 9) {
            this.setId(p[0].trim());
            this.fullName = p[1].trim();
            this.phone = p[2].trim();
            this.licensePlate = p[3].trim();
            this.latitude = Double.parseDouble(p[4].trim());
            this.longitude = Double.parseDouble(p[5].trim());
            this.status = DriverStatus.valueOf(p[6].trim());
            this.activeOrderCount = Integer.parseInt(p[7].trim());
            this.setVersion(Long.parseLong(p[8].trim()));
        }
    }
}
