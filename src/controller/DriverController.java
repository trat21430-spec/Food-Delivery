package controller;

import model.Driver;
import model.DriverStatus;
import repository.DriverRepository;

import java.util.List;

public class DriverController {
    private DriverRepository driverRepository;

    public DriverController() {
        this.driverRepository = new DriverRepository();
    }

    public DriverController(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver createDriver(String name, String phone, double lat, double lon) {
        List<Driver> all = this.driverRepository.findAll();
        String id = String.format("D%04d", all.size() + 1);
        String plate = "77-L1 " + (100 + (int)(Math.random() * 899)) + "." + (10 + (int)(Math.random() * 89));
        Driver driver = new Driver(id, name, phone, plate, lat, lon, DriverStatus.AVAILABLE, 0, 1L);
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
        return this.driverRepository.findAvailableDrivers();
    }

    public Driver getNearestAvailableDriver(double lat, double lon) {
        return this.driverRepository.findClosestOnlineDriver(lat, lon);
    }

    public boolean updateDriverStatus(String id, DriverStatus status) {
        Driver d = this.driverRepository.findById(id);
        if (d != null) {
            d.setStatus(status);
            d.incrementVersion();
            this.driverRepository.save(d);
            return true;
        }
        return false;
    }

    public boolean updateDriverInfo(String id, String name, String phone, double lat, double lon) {
        Driver d = this.driverRepository.findById(id);
        if (d != null) {
            d.setFullName(name);
            d.setPhone(phone);
            d.setLatitude(lat);
            d.setLongitude(lon);
            d.incrementVersion();
            this.driverRepository.save(d);
            return true;
        }
        return false;
    }

    public boolean deleteDriver(String id) {
        Driver d = this.driverRepository.findById(id);
        if (d != null) {
            this.driverRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
