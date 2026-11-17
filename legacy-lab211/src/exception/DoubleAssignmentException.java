package exception;

@SuppressWarnings("serial")
public class DoubleAssignmentException extends ConcurrencyException {
    private String orderId;
    private String existingDriverId;
    private String attemptedDriverId;

    public DoubleAssignmentException(String orderId, String existingDriverId, String attemptedDriverId) {
        super(String.format("Double assignment detected on Order [%s]: already assigned to [%s], attempted by [%s]", orderId, existingDriverId, attemptedDriverId));
        this.orderId = orderId;
        this.existingDriverId = existingDriverId;
        this.attemptedDriverId = attemptedDriverId;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public String getExistingDriverId() {
        return this.existingDriverId;
    }

    public String getAttemptedDriverId() {
        return this.attemptedDriverId;
    }
}
