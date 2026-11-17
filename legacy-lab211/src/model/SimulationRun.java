package model;

import java.time.LocalDateTime;

public class SimulationRun extends BaseEntity {
    private LockMechanism mechanism;
    private int totalOrders;
    private long durationMs;
    private double throughputOpsPerSec;
    private double throughputDropPercent;
    private int doubleAssignErrors;
    private int oversellErrors;
    private int driverOverloadErrors;

    public SimulationRun() {
        super();
        this.mechanism = LockMechanism.NO_LOCK;
        this.totalOrders = 0;
        this.durationMs = 0L;
        this.throughputOpsPerSec = 0.0;
        this.throughputDropPercent = 0.0;
        this.doubleAssignErrors = 0;
        this.oversellErrors = 0;
        this.driverOverloadErrors = 0;
    }

    public SimulationRun(String id, LockMechanism mechanism, int totalOrders, long durationMs,
                         double throughputOpsPerSec, double throughputDropPercent,
                         int doubleAssignErrors, int oversellErrors, int driverOverloadErrors) {
        super(id);
        this.mechanism = mechanism;
        this.totalOrders = totalOrders;
        this.durationMs = durationMs;
        this.throughputOpsPerSec = throughputOpsPerSec;
        this.throughputDropPercent = throughputDropPercent;
        this.doubleAssignErrors = doubleAssignErrors;
        this.oversellErrors = oversellErrors;
        this.driverOverloadErrors = driverOverloadErrors;
    }

    public SimulationRun(String id, LockMechanism mechanism, int totalOrders, long durationMs,
                         double throughputOpsPerSec, int doubleAssignments, int oversells, int driverOverloads,
                         int totalErrors, LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.mechanism = mechanism;
        this.totalOrders = totalOrders;
        this.durationMs = durationMs;
        this.throughputOpsPerSec = throughputOpsPerSec;
        this.throughputDropPercent = 0.0;
        this.doubleAssignErrors = doubleAssignments;
        this.oversellErrors = oversells;
        this.driverOverloadErrors = driverOverloads;
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

    public double getThroughputDropPercent() {
        return this.throughputDropPercent;
    }

    public void setThroughputDropPercent(double throughputDropPercent) {
        this.throughputDropPercent = throughputDropPercent;
    }

    public int getDoubleAssignErrors() {
        return this.doubleAssignErrors;
    }

    public void setDoubleAssignErrors(int doubleAssignErrors) {
        this.doubleAssignErrors = doubleAssignErrors;
    }

    public int getDoubleAssignments() {
        return this.doubleAssignErrors;
    }

    public void setDoubleAssignments(int doubleAssignments) {
        this.doubleAssignErrors = doubleAssignments;
    }

    public int getOversellErrors() {
        return this.oversellErrors;
    }

    public void setOversellErrors(int oversellErrors) {
        this.oversellErrors = oversellErrors;
    }

    public int getOversells() {
        return this.oversellErrors;
    }

    public void setOversells(int oversells) {
        this.oversellErrors = oversells;
    }

    public int getDriverOverloadErrors() {
        return this.driverOverloadErrors;
    }

    public void setDriverOverloadErrors(int driverOverloadErrors) {
        this.driverOverloadErrors = driverOverloadErrors;
    }

    public int getDriverOverloads() {
        return this.driverOverloadErrors;
    }

    public void setDriverOverloads(int driverOverloads) {
        this.driverOverloadErrors = driverOverloads;
    }

    public int getTotalErrors() {
        return this.doubleAssignErrors + this.oversellErrors + this.driverOverloadErrors;
    }

    public void setTotalErrors(int totalErrors) {
        // derived
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                this.getId(),
                this.mechanism.name(),
                String.valueOf(this.totalOrders),
                String.valueOf(this.durationMs),
                String.format(java.util.Locale.US, "%.2f", this.throughputOpsPerSec),
                String.format(java.util.Locale.US, "%.2f", this.throughputDropPercent),
                String.valueOf(this.doubleAssignErrors),
                String.valueOf(this.oversellErrors),
                String.valueOf(this.driverOverloadErrors));
    }

    @Override
    public void fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length >= 9) {
            this.setId(p[0].trim());
            this.mechanism = LockMechanism.valueOf(p[1].trim());
            this.totalOrders = Integer.parseInt(p[2].trim());
            this.durationMs = Long.parseLong(p[3].trim());
            this.throughputOpsPerSec = Double.parseDouble(p[4].trim());
            this.throughputDropPercent = Double.parseDouble(p[5].trim());
            this.doubleAssignErrors = Integer.parseInt(p[6].trim());
            this.oversellErrors = Integer.parseInt(p[7].trim());
            this.driverOverloadErrors = Integer.parseInt(p[8].trim());
        }
    }
}
