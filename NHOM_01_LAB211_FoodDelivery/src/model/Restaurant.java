package model;

import java.time.LocalDateTime;

public class Restaurant extends BaseEntity {
    private String name;
    private String category;
    private String address;
    private double latitude;
    private double longitude;
    private double rating;
    private boolean open;

    public Restaurant() {
        super();
        this.name = "";
        this.category = "Am thuc";
        this.address = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.rating = 5.0;
        this.open = true;
    }

    public Restaurant(String id, String name, String category, String address,
                      double latitude, double longitude, double rating, boolean open) {
        super(id);
        this.name = name;
        this.category = category;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.open = open;
    }

    public Restaurant(String id, String name, String address, double latitude, double longitude,
                      double rating, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.name = name;
        this.category = "Am thuc";
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.open = true;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                this.getId(),
                this.name,
                this.category,
                this.address,
                String.format(java.util.Locale.US, "%.6f", this.latitude),
                String.format(java.util.Locale.US, "%.6f", this.longitude),
                String.format(java.util.Locale.US, "%.1f", this.rating),
                String.valueOf(this.open));
    }

    @Override
    public void fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length >= 8) {
            this.setId(p[0].trim());
            this.name = p[1].trim();
            this.category = p[2].trim();
            this.address = p[3].trim();
            this.latitude = Double.parseDouble(p[4].trim());
            this.longitude = Double.parseDouble(p[5].trim());
            this.rating = Double.parseDouble(p[6].trim());
            this.open = Boolean.parseBoolean(p[7].trim());
        }
    }
}
