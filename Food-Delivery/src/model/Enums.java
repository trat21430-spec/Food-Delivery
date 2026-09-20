package model;

public class Enums {
    public enum DriverStatus {
        AVAILABLE,
        BUSY,
        OFFLINE
    }

    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        DISPATCHED,
        DELIVERED,
        CANCELLED
    }

    public enum LockMechanism {
        NO_LOCK,
        SYNCHRONIZED,
        OPTIMISTIC,
        FILE_LOCK
    }
}