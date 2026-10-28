package repository;

import model.Driver;
import model.DriverStatus;
import util.GeoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DriverRepository {
    private final ConcurrentHashMap<String, Driver> driverMap = new ConcurrentHashMap<>();

    public DriverRepository() {
        // Initialize dummy sample data for testing
        save(new Driver("DRV01", "Nguyen Van A", "0901234567", 10.7769, 106.7009, DriverStatus.AVAILABLE, 1L));
        save(new Driver("DRV02", "Tran Van B", "0902345678", 10.7800, 106.7050, DriverStatus.AVAILABLE, 1L));
        save(new Driver("DRV03", "Le Van C", "0903456789", 10.7700, 106.6900, DriverStatus.BUSY, 1L));
    }

    public void save(Driver driver) {
        driverMap.put(driver.getDriverId(), driver);
    }

    public Driver findById(String driverId) {
        return driverMap.get(driverId);
    }

    public List<Driver> findAll() {
        return new ArrayList<>(driverMap.values());
    }

    /**
     * CRITICAL BUSINESS LOGIC REQUIRED AT REPOSITORY LAYER:
     * Find nearest AVAILABLE driver using Haversine distance formula.
     */
    public synchronized Driver findNearestAvailable(double restLat, double restLon) {
        Driver nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Driver driver : driverMap.values()) {
            if (driver.getStatus() == DriverStatus.AVAILABLE) {
                double dist = GeoUtils.haversine(driver.getLatitude(), driver.getLongitude(), restLat, restLon);
                if (dist < minDistance) {
                    minDistance = dist;
                    nearest = driver;
                }
            }
        }
        return nearest;
    }

    /**
     * CRITICAL BUSINESS LOGIC REQUIRED AT REPOSITORY LAYER:
     * Safely assign driver and change status to BUSY (Preventing Driver Overload).
     */
    public synchronized boolean assignDriver(String driverId) {
        Driver driver = driverMap.get(driverId);
        if (driver != null && driver.getStatus() == DriverStatus.AVAILABLE) {
            driver.setBusy();
            return true;
        }
        return false;
    }
}
