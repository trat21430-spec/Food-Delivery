package controller;

import model.Driver;
import repository.DriverRepository;
import java.util.List;

public class DriverController {
    private final DriverRepository driverRepository;

    public DriverController() {
        this.driverRepository = new DriverRepository();
    }

    public DriverController(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

<<<<<<< HEAD
    /**
     * Get all drivers.
     */
    public List<Driver> getAllDrivers() {
=======
    public List<Driver getDriverById(String id) {
>>>>>>> cd0cd2a (push code)
        return driverRepository.findAll();
    }

    /**
     * Find driver by ID.
     */
    public Driver getDriverById(String id) {
        return driverRepository.findById(id);
    }

    /**
     * Find the nearest available driver to a given coordinate.
     */
    public Driver findNearestAvailableDriver(double lat, double lon) {
        return driverRepository.findNearestAvailable(lat, lon);   
    }
    }
<<<<<<< HEAD
}
=======
>>>>>>> cd0cd2a (push code)
