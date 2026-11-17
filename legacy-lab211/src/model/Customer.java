package model;

import java.time.LocalDateTime;

public class Customer extends BaseEntity {
    private String fullName;
    private String phone;
    private String ward;
    private double latitude;
    private double longitude;

    public Customer() {
        super();
        this.fullName = "";
        this.phone = "";
        this.ward = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public Customer(String id, String fullName, String phone, String ward, double latitude, double longitude) {
        super(id);
        this.fullName = fullName;
        this.phone = phone;
        this.ward = ward;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Customer(String id, String fullName, String phone, String ward, double latitude, double longitude,
                    LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.fullName = fullName;
        this.phone = phone;
        this.ward = ward;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getWard() {
        return this.ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getAddress() {
        return this.ward;
    }

    public void setAddress(String address) {
        this.ward = address;
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

    @Override
    public String toCsvLine() {
        return String.join(",",
                this.getId(),
                this.fullName,
                this.phone,
                this.ward,
                String.format(java.util.Locale.US, "%.6f", this.latitude),
                String.format(java.util.Locale.US, "%.6f", this.longitude));
    }

    @Override
    public void fromCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) return;
        String[] p = line.split(",", -1);
        if (p.length >= 6) {
            this.setId(p[0].trim());
            this.fullName = p[1].trim();
            this.phone = p[2].trim();
            this.longitude = Double.parseDouble(p[p.length - 1].trim());
            this.latitude = Double.parseDouble(p[p.length - 2].trim());
            StringBuilder sb = new StringBuilder();
            for (int i = 3; i < p.length - 2; i++) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(p[i].trim());
            }
            this.ward = sb.toString();
        }
    }
}
