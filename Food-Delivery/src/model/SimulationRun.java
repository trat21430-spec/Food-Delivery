package model;

import java.time.LocalDateTime;
import model.Enums.LockMechanism;

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
    }

    public SimulationRun(String id, LockMechanism mechanism, int totalOrders, long durationMs,
            double throughputOpsPerSec, int doubleAssignments, int oversells, int driverOverloads, int totalErrors,
            LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
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
        return mechanism;
    }

    public void setMechanism(LockMechanism mechanism) {
        this.mechanism = mechanism;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public double getThroughputOpsPerSec() {
        return throughputOpsPerSec;
    }

    public void setThroughputOpsPerSec(double throughputOpsPerSec) {
        this.throughputOpsPerSec = throughputOpsPerSec;
    }

    public int getDoubleAssignments() {
        return doubleAssignments;
    }

    public void setDoubleAssignments(int doubleAssignments) {
        this.doubleAssignments = doubleAssignments;
    }

    public int getOversells() {
        return oversells;
    }

    public void setOversells(int oversells) {
        this.oversells = oversells;
    }

    public int getDriverOverloads() {
        return driverOverloads;
    }

    public void setDriverOverloads(int driverOverloads) {
        this.driverOverloads = driverOverloads;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public void setTotalErrors(int totalErrors) {
        this.totalErrors = totalErrors;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                id,
                mechanism.name(),
                String.valueOf(totalOrders),
                String.valueOf(durationMs),
                String.format("%.2f", throughputOpsPerSec),
                String.valueOf(doubleAssignments),
                String.valueOf(oversells),
                String.valueOf(driverOverloads),
                String.valueOf(totalErrors),
                createdAt.toString(),
                updatedAt.toString(),
                String.valueOf(version));
    }

    @Override
    public void fromCsvLine(String csvLine) {
        String[] parts = parseCsvLine(csvLine);
        if (parts.length >= 12) {
            this.id = parts[0];
            this.mechanism = LockMechanism.valueOf(parts[1]);
            this.totalOrders = Integer.parseInt(parts[2]);
            this.durationMs = Long.parseLong(parts[3]);
            this.throughputOpsPerSec = Double.parseDouble(parts[4]);
            this.doubleAssignments = Integer.parseInt(parts[5]);
            this.oversells = Integer.parseInt(parts[6]);
            this.driverOverloads = Integer.parseInt(parts[7]);
            this.totalErrors = Integer.parseInt(parts[8]);
            this.createdAt = LocalDateTime.parse(parts[9]);
            this.updatedAt = LocalDateTime.parse(parts[10]);
            this.version = Long.parseLong(parts[11]);
        }
    }

    private String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}
