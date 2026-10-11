package exception;

public class EntityNotFoundException extends Exception {
    private String entityName;
    private String entityId;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityName, String entityId) {
        super(String.format("%s not found with identifier/criteria: [%s]", entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityId() {
        return entityId;
    }
}
