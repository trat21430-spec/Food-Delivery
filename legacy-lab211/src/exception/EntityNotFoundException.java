package exception;

@SuppressWarnings("serial")
public class EntityNotFoundException extends FoodDeliveryException {
    private String entityName;
    private String entityId;

    public EntityNotFoundException(String entityName, String entityId) {
        super(String.format("Entity [%s] with ID [%s] not found.", entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public String getEntityId() {
        return this.entityId;
    }
}
