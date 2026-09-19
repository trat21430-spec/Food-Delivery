package controller;

import model.Driver;
import repository.DriverRepository;

public class DriverController {

    private final DriverRepository driverRepository;

    public DriverController(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    /**
     * Set driver status to ONLINE / AVAILABLE.
     */
    public boolean goOnline(String driverId) {

        if (driverId == null || driverId.isBlank()) {
            return false;
        }

        return driverRepository.goOnline(driverId);
    }

    /**
     * Set driver status to OFFLINE.
     */
    public boolean goOffline(String driverId) {

        if (driverId == null || driverId.isBlank()) {
            return false;
        }

        return driverRepository.goOffline(driverId);
    }

    /**
     * Find nearest available driver.
     *
     * IMPORTANT:
     * Controller does NOT calculate distance.
     * DriverRepository handles the actual logic.
     */
    public Driver findNearestDriver(double latitude,
                                    double longitude) {

        return driverRepository.findNearestAvailable(
                latitude,
                longitude
        );
    }

    /**
     * Get driver by ID.
     */
    public Driver getDriver(String driverId) {

        if (driverId == null || driverId.isBlank()) {
            return null;
        }

        return driverRepository.findById(driverId);
    }
}