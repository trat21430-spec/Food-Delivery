package model;

import java.time.LocalDateTime;

public class DriverApplication extends BaseEntity {
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String licensePlate;
    private String vehicleType;
    private String status; // PENDING, APPROVED, REJECTED
    private String note;

    public DriverApplication() {
        super();
        this.username = "";
        this.password = "";
        this.fullName = "";
        this.phone = "";
        this.licensePlate = "";
        this.vehicleType = "";
        this.status = "PENDING";
        this.note = "";
    }

    public DriverApplication(String id, String username, String password, String fullName,
                             String phone, String licensePlate, String vehicleType,
                             String status, String note) {
        super(id);
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.status = status != null ? status : "PENDING";
        this.note = note != null ? note : "";
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getVehicleType() {
        return this.vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                this.getId(),
                this.username,
                this.password,
                this.fullName,
                this.phone,
                this.licensePlate,
                this.vehicleType,
                this.status,
                this.note,
                this.getCreatedAt().toString(),
                this.getUpdatedAt().toString(),
                String.valueOf(this.getVersion()));
    }

    @Override
    public void fromCsvLine(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length >= 8) {
            this.setId(parts[0].trim());
            this.username = parts[1].trim();
            this.password = parts[2].trim();
            this.fullName = parts[3].trim();
            this.phone = parts[4].trim();
            this.licensePlate = parts[5].trim();
            this.vehicleType = parts[6].trim();
            this.status = parts[7].trim();
            if (parts.length > 8) this.note = parts[8].trim();
            if (parts.length > 11) {
                try {
                    this.setVersion(Long.parseLong(parts[11].trim()));
                } catch (Exception ignored) {
                }
            }
        }
    }
}
