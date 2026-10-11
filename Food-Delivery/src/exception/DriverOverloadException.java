package exception;

public class DriverOverloadException extends ConcurrencyException {
    private String driverId;
    private String currentOrderId;
    private String attemptedOrderId;

    public DriverOverloadException(String message) {
        super(message);
    }

    public DriverOverloadException(String driverId, String currentOrderId, String attemptedOrderId) {
        super(String.format("Driver overload detected on Driver [%s]: already assigned to Order [%s], attempted by Order [%s]",
                driverId, currentOrderId, attemptedOrderId));
        this.driverId = driverId;
        this.currentOrderId = currentOrderId;
        this.attemptedOrderId = attemptedOrderId;
    }

    public DriverOverloadException(String driverId, String message) {
        super(message);
        this.driverId = driverId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getCurrentOrderId() {
        return currentOrderId;
    }

    public String getAttemptedOrderId() {
        return attemptedOrderId;
    }
}