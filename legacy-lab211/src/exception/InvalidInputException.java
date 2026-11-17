package exception;

@SuppressWarnings("serial")
public class InvalidInputException extends FoodDeliveryException {
    private String fieldName;

    public InvalidInputException(String message) {
        super(message);
        this.fieldName = "GENERAL";
    }

    public InvalidInputException(String fieldName, String message) {
        super(String.format("Invalid value for field [%s]: %s", fieldName, message));
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return this.fieldName;
    }
}
