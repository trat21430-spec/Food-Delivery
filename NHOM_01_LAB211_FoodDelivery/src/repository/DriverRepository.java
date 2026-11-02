package repository;

import model.Driver;
import model.DriverStatus;
import model.GeoUtils;
import model.LockMechanism;
import exception.DriverOverloadException;

import java.util.ArrayList;
import java.util.List;

public class DriverRepository extends CsvRepository<Driver> {
    private GeoUtils geoUtils;

    public DriverRepository() {
        super("data/drivers.csv", "id,fullName,phone,licensePlate,latitude,longitude,status,activeOrderCount,version");
        this.geoUtils = new GeoUtils();
    }

    public DriverRepository(String customFilePath) {
        super(customFilePath, "id,fullName,phone,licensePlate,latitude,longitude,status,activeOrderCount,version");
        this.geoUtils = new GeoUtils();
    }

    @Override
    public Driver createEntity() {
        return new Driver();
    }

    public GeoUtils getGeoUtils() {
        return this.geoUtils;
    }

    public void setGeoUtils(GeoUtils geoUtils) {
        this.geoUtils = geoUtils;
    }

    public Driver findByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        List<Driver> allDrivers = this.findAll();
        for (int i = 0; i < allDrivers.size(); i++) {
            Driver d = allDrivers.get(i);
            if (d.getPhone() != null && d.getPhone().trim().equals(phone.trim())) {
                return d;
            }
        }
        return null;
    }

    public List<Driver> findAvailableDrivers() {
        List<Driver> allDrivers = this.findAll();
        List<Driver> available = new ArrayList<>();
        for (int i = 0; i < allDrivers.size(); i++) {
            Driver d = allDrivers.get(i);
            if (d.getStatus() == DriverStatus.AVAILABLE) {
                available.add(d);
            }
        }
        return available;
    }

    public Driver findClosestOnlineDriver(double restaurantLat, double restaurantLon) {
        return this.findNearestAvailable(restaurantLat, restaurantLon);
    }

    public Driver findNearestAvailable(double restaurantLat, double restaurantLon) {
        List<Driver> allDrivers = this.findAll();
        Driver nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < allDrivers.size(); i++) {
            Driver driver = allDrivers.get(i);
            if (driver.getStatus() == DriverStatus.AVAILABLE) {
                double distance = this.geoUtils.haversineDistance(restaurantLat, restaurantLon, driver.getLatitude(), driver.getLongitude());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = driver;
                }
            }
        }
        return nearest;
    }

    public boolean markBusy(String driverId, LockMechanism mechanism) throws Exception {
        return this.markBusy(driverId, "ORDER_SIM", mechanism);
    }

    public boolean markBusy(String driverId, String orderId, LockMechanism mechanism) throws Exception {
        switch (mechanism) {
            case NO_LOCK:
                return this.markBusyNoLock(driverId, orderId);
            case SYNCHRONIZED:
                return this.markBusySynchronized(driverId, orderId);
            case OPTIMISTIC:
                return this.markBusyOptimistic(driverId, orderId);
            case FILE_LOCK:
                return this.markBusyFileLock(driverId, orderId);
            default:
                return this.markBusyNoLock(driverId, orderId);
        }
    }

    public boolean markAvailable(String driverId) {
        Driver driver = this.findById(driverId);
        if (driver != null) {
            driver.setStatus(DriverStatus.AVAILABLE);
            driver.setActiveOrderCount(0);
            driver.setCurrentOrderId("");
            driver.incrementVersion();
            this.save(driver);
            return true;
        }
        return false;
    }

    private boolean markBusyNoLock(String driverId, String orderId) throws DriverOverloadException {
        Driver driver = this.findById(driverId);
        if (driver == null) {
            return false;
        }

        if (driver.getStatus() == DriverStatus.BUSY || driver.getActiveOrderCount() > 0) {
            throw new DriverOverloadException(driverId, driver.getCurrentOrderId());
        }

        try {
            Thread.sleep(3);
        } catch (InterruptedException ignored) {
        }

        driver.setStatus(DriverStatus.BUSY);
        driver.setActiveOrderCount(driver.getActiveOrderCount() + 1);
        driver.setCurrentOrderId(orderId);
        this.save(driver);
        return true;
    }

    private boolean markBusySynchronized(String driverId, String orderId) throws DriverOverloadException {
        synchronized (this.getKeyLock("DRIVER_" + driverId)) {
            Driver driver = this.findById(driverId);
            if (driver == null) {
                return false;
            }

            if (driver.getStatus() == DriverStatus.BUSY || driver.getActiveOrderCount() > 0) {
                throw new DriverOverloadException(driverId, driver.getCurrentOrderId());
            }

            try {
                Thread.sleep(3);
            } catch (InterruptedException ignored) {
            }

            driver.setStatus(DriverStatus.BUSY);
            driver.setActiveOrderCount(1);
            driver.setCurrentOrderId(orderId);
            this.save(driver);
            return true;
        }
    }

    private boolean markBusyOptimistic(String driverId, String orderId) throws Exception {
        Driver driver = this.findById(driverId);
        if (driver == null) {
            return false;
        }

        if (driver.getStatus() == DriverStatus.BUSY || driver.getActiveOrderCount() > 0) {
            throw new DriverOverloadException(driverId, driver.getCurrentOrderId());
        }

        long expectedVersion = driver.getVersion();
        try {
            Thread.sleep(3);
        } catch (InterruptedException ignored) {
        }

        driver.setStatus(DriverStatus.BUSY);
        driver.setActiveOrderCount(1);
        driver.setCurrentOrderId(orderId);
        this.updateWithOptimisticLock(driver, expectedVersion);
        return true;
    }

    private boolean markBusyFileLock(String driverId, String orderId) throws DriverOverloadException {
        Boolean result = this.executeWithFileLock(() -> {
            Driver driver = this.findById(driverId);
            if (driver == null) {
                return false;
            }

            if (driver.getStatus() == DriverStatus.BUSY || driver.getActiveOrderCount() > 0) {
                return false;
            }

            try {
                Thread.sleep(3);
            } catch (InterruptedException ignored) {
            }

            driver.setStatus(DriverStatus.BUSY);
            driver.setActiveOrderCount(1);
            driver.setCurrentOrderId(orderId);
            this.save(driver);
            return true;
        });

        if (result == null || !result) {
            Driver driver = this.findById(driverId);
            String currentOrder = driver != null ? driver.getCurrentOrderId() : "UNKNOWN";
            throw new DriverOverloadException(driverId, currentOrder);
        }

        return true;
    }
}
