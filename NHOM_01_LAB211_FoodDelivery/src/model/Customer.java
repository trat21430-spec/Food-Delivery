package model;

import java.time.LocalDateTime;

public class Customer extends BaseEntity {
    private String name;
    private String phone;
    private String address;
    private double latitude;
    private double longitude;

    public Customer() {
        super();
        this.name = "";
        this.phone = "";
        this.address = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public Customer(String id, String name, String phone, String address, double latitude, double longitude, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
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
            this.escapeCsv(this.name),
            this.escapeCsv(this.phone),
            this.escapeCsv(this.address),
            String.valueOf(this.latitude),
            String.valueOf(this.longitude),
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
            this.name = parts[1];
            this.phone = parts[2];
            this.address = parts[3];
            this.latitude = Double.parseDouble(parts[4]);
            this.longitude = Double.parseDouble(parts[5]);
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
