package repository;

import model.Driver;
import model.DriverStatus;
import model.GeoUtils;
import model.LockMechanism;
import exception.DriverOverloadException;

import java.util.List;

public class DriverRepository extends CsvRepository<Driver> {
    private GeoUtils geoUtils;

    public DriverRepository() {
        super("data/drivers.csv", "id,name,phone,status,latitude,longitude,currentOrderId,createdAt,updatedAt,version");
        this.geoUtils = new GeoUtils();
    }

    public DriverRepository(String customFilePath) {
        super(customFilePath, "id,name,phone,status,latitude,longitude,currentOrderId,createdAt,updatedAt,version");
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

    private boolean markBusyNoLock(String driverId, String orderId) throws DriverOverloadException {
        Driver driver = this.findById(driverId);
        if (driver == null) {
            return false;
        }

        if (driver.getStatus() == DriverStatus.BUSY) {
            throw new DriverOverloadException(driverId, driver.getCurrentOrderId());
        }

        try {
            Thread.sleep(3);
        } catch (InterruptedException ignored) {
        }

        driver.setStatus(DriverStatus.BUSY);
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

            if (driver.getStatus() == DriverStatus.BUSY) {
                throw new DriverOverloadException(driverId, driver.getCurrentOrderId());
            }

            try {
                Thread.sleep(3);
            } catch (InterruptedException ignored) {
            }

            driver.setStatus(DriverStatus.BUSY);
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

        if (driver.getStatus() == DriverStatus.BUSY) {
            throw new DriverOverloadException(driverId, driver.getCurrentOrderId());
        }

        long expectedVersion = driver.getVersion();
        try {
            Thread.sleep(3);
        } catch (InterruptedException ignored) {
        }

        driver.setStatus(DriverStatus.BUSY);
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

            if (driver.getStatus() == DriverStatus.BUSY) {
                return false;
            }

            try {
                Thread.sleep(3);
            } catch (InterruptedException ignored) {
            }

            driver.setStatus(DriverStatus.BUSY);
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
