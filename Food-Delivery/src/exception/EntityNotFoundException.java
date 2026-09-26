package exception;

public class EntityNotFoundException extends Exception {
    private final String entityName;
    private final String entityId;

    public EntityNotFoundException(String entityName, String entityId) {
        super(String.format("Entity [%s] with ID [%s] not found.", entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityId() {
        return entityId;
    }
}
