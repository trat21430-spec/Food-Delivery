package model;

import java.time.LocalDateTime;

public class SimulationRun extends BaseEntity {
    private LockMechanism mechanism;
    private int totalOrders;
    private long durationMs;
    private double throughputOpsPerSec;
    private int doubleAssignments;
    private int oversells;
    private int driverOverloads;
    private int totalErrors;

    public SimulationRun() {
        super();
        this.mechanism = LockMechanism.NO_LOCK;
        this.totalOrders = 0;
        this.durationMs = 0L;
        this.throughputOpsPerSec = 0.0;
        this.doubleAssignments = 0;
        this.oversells = 0;
        this.driverOverloads = 0;
        this.totalErrors = 0;
    }

    public SimulationRun(String id, LockMechanism mechanism, int totalOrders, long durationMs, double throughputOpsPerSec, int doubleAssignments, int oversells, int driverOverloads, int totalErrors, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.mechanism = mechanism;
        this.totalOrders = totalOrders;
        this.durationMs = durationMs;
        this.throughputOpsPerSec = throughputOpsPerSec;
        this.doubleAssignments = doubleAssignments;
        this.oversells = oversells;
        this.driverOverloads = driverOverloads;
        this.totalErrors = totalErrors;
    }

    public LockMechanism getMechanism() {
        return this.mechanism;
    }

    public void setMechanism(LockMechanism mechanism) {
        this.mechanism = mechanism;
    }

    public int getTotalOrders() {
        return this.totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getDurationMs() {
        return this.durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public double getThroughputOpsPerSec() {
        return this.throughputOpsPerSec;
    }

    public void setThroughputOpsPerSec(double throughputOpsPerSec) {
        this.throughputOpsPerSec = throughputOpsPerSec;
    }

    public int getDoubleAssignments() {
        return this.doubleAssignments;
    }

    public void setDoubleAssignments(int doubleAssignments) {
        this.doubleAssignments = doubleAssignments;
    }

    public int getOversells() {
        return this.oversells;
    }

    public void setOversells(int oversells) {
        this.oversells = oversells;
    }

    public int getDriverOverloads() {
        return this.driverOverloads;
    }

    public void setDriverOverloads(int driverOverloads) {
        this.driverOverloads = driverOverloads;
    }

    public int getTotalErrors() {
        return this.totalErrors;
    }

    public void setTotalErrors(int totalErrors) {
        this.totalErrors = totalErrors;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
            this.getId(),
            this.mechanism.name(),
            String.valueOf(this.totalOrders),
            String.valueOf(this.durationMs),
            String.format(java.util.Locale.US, "%.2f", this.throughputOpsPerSec),
            String.valueOf(this.doubleAssignments),
            String.valueOf(this.oversells),
            String.valueOf(this.driverOverloads),
            String.valueOf(this.totalErrors),
            this.getCreatedAt().toString(),
            this.getUpdatedAt().toString(),
            String.valueOf(this.getVersion())
        );
    }

    @Override
    public void fromCsvLine(String csvLine) {
        String[] parts = this.parseCsvLine(csvLine);
        if (parts.length >= 12) {
            this.setId(parts[0]);
            this.mechanism = LockMechanism.valueOf(parts[1]);
            this.totalOrders = Integer.parseInt(parts[2]);
            this.durationMs = Long.parseLong(parts[3]);
            this.throughputOpsPerSec = Double.parseDouble(parts[4]);
            this.doubleAssignments = Integer.parseInt(parts[5]);
            this.oversells = Integer.parseInt(parts[6]);
            this.driverOverloads = Integer.parseInt(parts[7]);
            this.totalErrors = Integer.parseInt(parts[8]);
            this.setCreatedAt(LocalDateTime.parse(parts[9]));
            this.setUpdatedAt(LocalDateTime.parse(parts[10]));
            this.setVersion(Long.parseLong(parts[11]));
        }
    }

    private String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}
