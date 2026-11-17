package exception;

@SuppressWarnings("serial")
public class ConcurrencyException extends FoodDeliveryException {
    public ConcurrencyException(String message) {
        super(message);
    }

    public ConcurrencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
