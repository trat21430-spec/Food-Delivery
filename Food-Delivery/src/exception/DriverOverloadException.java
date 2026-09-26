package exception;

public class DriverOverloadException extends ConcurrencyException {
    private final String driverId;
    private final String currentOrderId;

    public DriverOverloadException(String driverId, String currentOrderId) {
        super(String.format("Driver overload detected on Driver [%s]: already handling order [%s]", driverId,
                currentOrderId));
        this.driverId = driverId;
        this.currentOrderId = currentOrderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getCurrentOrderId() {
        return currentOrderId;
    }
}
