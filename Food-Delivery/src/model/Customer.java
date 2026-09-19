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
    }

    public Customer(String id, String name, String phone, String address, double latitude, double longitude,
            LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                id,
                escapeCsv(name),
                escapeCsv(phone),
                escapeCsv(address),
                String.valueOf(latitude),
                String.valueOf(longitude),
                createdAt.toString(),
                updatedAt.toString(),
                String.valueOf(version));
    }

    @Override
    public void fromCsvLine(String csvLine) {
        String[] parts = parseCsvLine(csvLine);
        if (parts.length >= 9) {
            this.id = parts[0];
            this.name = parts[1];
            this.phone = parts[2];
            this.address = parts[3];
            this.latitude = Double.parseDouble(parts[4]);
            this.longitude = Double.parseDouble(parts[5]);
            this.createdAt = LocalDateTime.parse(parts[6]);
            this.updatedAt = LocalDateTime.parse(parts[7]);
            this.version = Long.parseLong(parts[8]);
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
