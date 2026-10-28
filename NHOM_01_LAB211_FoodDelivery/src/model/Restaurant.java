package model;

import java.time.LocalDateTime;

public class Restaurant extends BaseEntity {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private double rating;

    public Restaurant() {
        super();
        this.name = "";
        this.address = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.rating = 5.0;
    }

    public Restaurant(String id, String name, String address, double latitude, double longitude, double rating, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getRating() {
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
            this.getId(),
            this.escapeCsv(this.name),
            this.escapeCsv(this.address),
            String.valueOf(this.latitude),
            String.valueOf(this.longitude),
            String.valueOf(this.rating),
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
            this.address = parts[2];
            this.latitude = Double.parseDouble(parts[3]);
            this.longitude = Double.parseDouble(parts[4]);
            this.rating = Double.parseDouble(parts[5]);
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
