package exception;

public class DoubleAssignmentException extends ConcurrencyException {
    private final String orderId;
    private final String existingDriverId;
    private final String attemptedDriverId;

    public DoubleAssignmentException(String orderId, String existingDriverId, String attemptedDriverId) {
        super(String.format("Double assignment detected on Order [%s]: already assigned to [%s], attempted by [%s]",
                orderId, existingDriverId, attemptedDriverId));
        this.orderId = orderId;
        this.existingDriverId = existingDriverId;
        this.attemptedDriverId = attemptedDriverId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getExistingDriverId() {
        return existingDriverId;
    }

    public String getAttemptedDriverId() {
        return attemptedDriverId;
    }
}
