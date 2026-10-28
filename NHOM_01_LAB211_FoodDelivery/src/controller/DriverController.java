package controller;

import model.Driver;
import model.DriverStatus;
import repository.DriverRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DriverController {
    private DriverRepository driverRepository;

    public DriverController() {
        this.driverRepository = new DriverRepository();
    }

    public DriverController(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    // --- DRIVER CRUD ---

    public Driver createDriver(String name, String phone, double lat, double lon) {
        String id = "D" + (1000 + (int)(Math.random() * 9000));
        Driver driver = new Driver(id, name, phone, DriverStatus.AVAILABLE, lat, lon, "", LocalDateTime.now(), LocalDateTime.now(), 1L);
        this.driverRepository.save(driver);
        return driver;
    }

    public List<Driver> getAllDrivers() {
        return this.driverRepository.findAll();
    }

    public Driver getDriverById(String id) {
        return this.driverRepository.findById(id);
    }

    public List<Driver> getAvailableDrivers() {
        List<Driver> all = this.driverRepository.findAll();
        List<Driver> available = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            Driver d = all.get(i);
            if (d.getStatus() == DriverStatus.AVAILABLE) {
                available.add(d);
            }
        }
        return available;
    }

    public boolean updateDriverStatus(String driverId, DriverStatus newStatus) {
        Driver driver = this.driverRepository.findById(driverId);
        if (driver != null) {
            driver.setStatus(newStatus);
            if (newStatus == DriverStatus.AVAILABLE) {
                driver.setCurrentOrderId("");
            }
            driver.incrementVersion();
            this.driverRepository.save(driver);
            return true;
        }
        return false;
    }

    public boolean updateDriverInfo(String driverId, String newName, String newPhone, double newLat, double newLon) {
        Driver driver = this.driverRepository.findById(driverId);
        if (driver != null) {
            driver.setName(newName);
            driver.setPhone(newPhone);
            driver.setLatitude(newLat);
            driver.setLongitude(newLon);
            driver.incrementVersion();
            this.driverRepository.save(driver);
            return true;
        }
        return false;
    }

    public boolean deleteDriver(String id) {
        Driver driver = this.driverRepository.findById(id);
        if (driver != null) {
            this.driverRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
