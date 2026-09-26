package exception;

public class OptimisticLockException extends ConcurrencyException {
    private final String entityId;
    private final long expectedVersion;
    private final long currentVersion;

    public OptimisticLockException(String entityId, long expectedVersion, long currentVersion) {
        super(String.format("Optimistic lock conflict on Entity [%s]: expected version %d but found %d", entityId,
                expectedVersion, currentVersion));
        this.entityId = entityId;
        this.expectedVersion = expectedVersion;
        this.currentVersion = currentVersion;
    }

    public String getEntityId() {
        return entityId;
    }

    public long getExpectedVersion() {
        return expectedVersion;
    }

    public long getCurrentVersion() {
        return currentVersion;
    }
}
