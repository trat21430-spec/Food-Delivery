package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Driver;
import model.Enums.DriverStatus;
import model.Enums.LockMechanism;

public class DriverRepository extends CsvRepository<Driver> {

    public DriverRepository() {
        super("data/drivers.csv");
    }

    public Driver findNearestAvailable(double latitude, double longitude) {
        return findAll().stream()
                .filter(driver -> driver.getStatus() == DriverStatus.AVAILABLE)
                .min(Comparator.comparingDouble(driver -> distance(
                        latitude,
                        longitude,
                        driver.getLatitude(),
                        driver.getLongitude())))
                .orElse(null);
    }

    public synchronized boolean assignDriver(String driverId) {
        return updateStatus(driverId, DriverStatus.BUSY);
    }

    public synchronized boolean assignDriver(String driverId, LockMechanism mechanism) {
        return updateStatus(driverId, DriverStatus.BUSY);
    }

    public synchronized boolean markBusy(String driverId, String orderId, LockMechanism mechanism) {
        List<Driver> drivers = findAll();
        Driver driver = drivers.stream().filter(item -> item.getId().equals(driverId)).findFirst().orElse(null);
        if (driver == null) {
            return false;
        }
        driver.setStatus(DriverStatus.BUSY);
        driver.setCurrentOrderId(orderId);
        saveAll(drivers);
        return true;
    }

    public synchronized boolean updateStatus(String driverId, DriverStatus status) {
        List<Driver> drivers = findAll();
        Driver driver = drivers.stream()
                .filter(item -> item.getId().equals(driverId))
                .findFirst()
                .orElse(null);

        if (driver == null || status == null) {
            return false;
        }

        driver.setStatus(status);
        saveAll(drivers);
        return true;
    }

    public boolean goOnline(String driverId) {
        return updateStatus(driverId, DriverStatus.AVAILABLE);
    }

    public boolean goOffline(String driverId) {
        return updateStatus(driverId, DriverStatus.OFFLINE);
    }

    public synchronized boolean updateStatus(String driverId, DriverStatus status, LockMechanism mechanism) {
        return updateStatus(driverId, status);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371 * c;
    }

    @Override
    protected Driver fromCsvLine(String line) {
        Driver driver = new Driver();
        String[] parts = line.split(",(?=(?:[^"]*\"[^"]*\")*[^"]*$)");
        if (parts.length < 10) {
            return driver;
        }
        driver.setId(parts[0].trim());
        driver.setName(parts[1].trim());
        driver.setPhone(parts[2].trim());
        driver.setStatus(DriverStatus.valueOf(parts[3].trim()));
        driver.setLatitude(Double.parseDouble(parts[4].trim()));
        driver.setLongitude(Double.parseDouble(parts[5].trim()));
        driver.setCurrentOrderId(parts[6].trim());
        driver.setCreatedAt(java.time.LocalDateTime.parse(parts[7].trim()));
        driver.setUpdatedAt(java.time.LocalDateTime.parse(parts[8].trim()));
        driver.setVersion(Long.parseLong(parts[9].trim()));
        return driver;
    }

    @Override
    protected String toCsvLine(Driver driver) {
        return String.join(",",
                driver.getId(),
                driver.getName(),
                driver.getPhone(),
                driver.getStatus().name(),
                String.valueOf(driver.getLatitude()),
                String.valueOf(driver.getLongitude()),
                driver.getCurrentOrderId(),
                driver.getCreatedAt().toString(),
                driver.getUpdatedAt().toString(),
                String.valueOf(driver.getVersion()));
    }

    @Override
    protected String getHeader() {
        return "id,name,phone,status,latitude,longitude,currentOrderId,createdAt,updatedAt,version";
    }
}
