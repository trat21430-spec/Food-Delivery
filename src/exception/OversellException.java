package exception;

@SuppressWarnings("serial")
public class OversellException extends ConcurrencyException {
    private String menuItemId;
    private int requestedQty;
    private int availableQty;

    public OversellException(String menuItemId, int requestedQty, int availableQty) {
        super(String.format("Oversell detected for MenuItem [%s]: requested=%d, available=%d", menuItemId, requestedQty, availableQty));
        this.menuItemId = menuItemId;
        this.requestedQty = requestedQty;
        this.availableQty = availableQty;
    }

    public String getMenuItemId() {
        return this.menuItemId;
    }

    public int getRequestedQty() {
        return this.requestedQty;
    }

    public int getAvailableQty() {
        return this.availableQty;
    }
}
