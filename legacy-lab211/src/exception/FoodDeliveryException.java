package exception;

@SuppressWarnings("serial")
public class FoodDeliveryException extends Exception {
    public FoodDeliveryException(String message) {
        super(message);
    }

    public FoodDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
