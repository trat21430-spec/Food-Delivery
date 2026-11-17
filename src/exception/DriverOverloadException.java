package exception;

@SuppressWarnings("serial")
public class DriverOverloadException extends ConcurrencyException {
    private String driverId;
    private String currentOrderId;

    public DriverOverloadException(String driverId, String currentOrderId) {
        super(String.format("Driver overload detected on Driver [%s]: already handling order [%s]", driverId, currentOrderId));
        this.driverId = driverId;
        this.currentOrderId = currentOrderId;
    }

    public String getDriverId() {
        return this.driverId;
    }

    public String getCurrentOrderId() {
        return this.currentOrderId;
    }
}
