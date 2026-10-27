package model;

public class Driver {
    private String driverId;
    private String name;
    private String phone;
    private double latitude;
    private double longitude;
    private DriverStatus status;
    private long version;

    public Driver() {
    }

    public Driver(String driverId, String name, String phone, double latitude, double longitude, DriverStatus status, long version) {
        this.driverId = driverId;
        this.name = name;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.version = version;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void updateLocation(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
        this.version++;
    }

    public void setAvailable() {
        this.status = DriverStatus.AVAILABLE;
        this.version++;
    }

    public void setBusy() {
        this.status = DriverStatus.BUSY;
        this.version++;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverId='" + driverId + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", status=" + status +
                ", version=" + version +
                '}';
    }
}
