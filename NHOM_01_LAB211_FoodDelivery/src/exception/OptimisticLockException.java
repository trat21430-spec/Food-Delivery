package exception;

@SuppressWarnings("serial")
public class OptimisticLockException extends ConcurrencyException {
    private String entityId;
    private long expectedVersion;
    private long currentVersion;

    public OptimisticLockException(String entityId, long expectedVersion, long currentVersion) {
        super(String.format("Optimistic lock conflict on Entity [%s]: expected version %d but found %d", entityId, expectedVersion, currentVersion));
        this.entityId = entityId;
        this.expectedVersion = expectedVersion;
        this.currentVersion = currentVersion;
    }

    public String getEntityId() {
        return this.entityId;
    }

    public long getExpectedVersion() {
        return this.expectedVersion;
    }

    public long getCurrentVersion() {
        return this.currentVersion;
    }
}
