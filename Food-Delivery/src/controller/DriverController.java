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

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver getDriverById(String id) {
        return driverRepository.findById(id);
    }

    public Driver findNearestAvailableDriver(double lat, double lon) {
        return driverRepository.findNearestAvailable(lat, lon);
    }
}