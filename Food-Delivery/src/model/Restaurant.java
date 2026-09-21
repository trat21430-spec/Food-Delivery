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
    }

    public Restaurant(String id, String name, String address, double latitude, double longitude, double rating,
            LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                id,
                escapeCsv(name),
                escapeCsv(address),
                String.valueOf(latitude),
                String.valueOf(longitude),
                String.valueOf(rating),
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
            this.address = parts[2];
            this.latitude = Double.parseDouble(parts[3]);
            this.longitude = Double.parseDouble(parts[4]);
            this.rating = Double.parseDouble(parts[5]);
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
