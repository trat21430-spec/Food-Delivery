
package exception;

public class OversellException extends ConcurrencyException {
    private final String menuItemId;
    private final int requestedQty;
    private final int availableQty;

    public OversellException(String menuItemId, int requestedQty, int availableQty) {
        super(String.format("Oversell detected for MenuItem [%s]: requested=%d, available=%d", menuItemId, requestedQty,
                availableQty));
        this.menuItemId = menuItemId;
        this.requestedQty = requestedQty;
        this.availableQty = availableQty;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public int getRequestedQty() {
        return requestedQty;
    }

    public int getAvailableQty() {
        return availableQty;
    }
}