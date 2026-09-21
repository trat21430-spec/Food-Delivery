package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Driver;
import model.Enums.DriverStatus;

public class DriverRepository {

    private static final Path FILE = Path.of("data", "drivers.csv");

    public List<Driver> findAll() {
        List<Driver> drivers = new ArrayList<>();
        for (String line : readLines()) {
            if (line.isBlank() || line.startsWith("driverId")) {
                continue;
            }
            Driver driver = new Driver();
            driver.fromCsvLine(line);
            drivers.add(driver);
        }
        return drivers;
    }

    public Driver findById(String driverId) {
        return findAll().stream()
                .filter(driver -> driver.getId().equals(driverId))
                .findFirst()
                .orElse(null);
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
        writeLines(drivers);
        return true;
    }

    public boolean goOnline(String driverId) {
        return updateStatus(driverId, DriverStatus.AVAILABLE);
    }

    public boolean goOffline(String driverId) {
        return updateStatus(driverId, DriverStatus.OFFLINE);
    }

    private List<String> readLines() {
        try {
            if (!Files.exists(FILE)) {
                return List.of();
            }
            return Files.readAllLines(FILE);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot read drivers.csv", ex);
        }
    }

    private void writeLines(List<Driver> drivers) {
        try {
            Files.createDirectories(FILE.getParent());
            Files.write(FILE, drivers.stream().map(Driver::toCsvLine).toList());
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot write drivers.csv", ex);
        }
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
}
