package model;

public class SimulationRun {
    private String runId;
    private String mechanism; // NO_LOCK, FILE_LOCK, SYNCHRONIZED, OPTIMISTIC
    private int totalOrders;
    private long durationMs;
    private double throughput; // orders per second
    private int doubleAssignments;
    private int oversellErrors;
    private int driverOverloads;
    private double throughputDrop; // percentage drop compared to baseline

    public SimulationRun() {
    }

    public SimulationRun(String runId, String mechanism, int totalOrders, long durationMs, int doubleAssignments, int oversellErrors, int driverOverloads) {
        this.runId = runId;
        this.mechanism = mechanism;
        this.totalOrders = totalOrders;
        this.durationMs = durationMs;
        this.doubleAssignments = doubleAssignments;
        this.oversellErrors = oversellErrors;
        this.driverOverloads = driverOverloads;
        this.throughput = calculateThroughput();
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getMechanism() {
        return mechanism;
    }

    public void setMechanism(String mechanism) {
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

    public double getThroughput() {
        return throughput;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

    public int getDoubleAssignments() {
        return doubleAssignments;
    }

    public void setDoubleAssignments(int doubleAssignments) {
        this.doubleAssignments = doubleAssignments;
    }

    public int getOversellErrors() {
        return oversellErrors;
    }

    public void setOversellErrors(int oversellErrors) {
        this.oversellErrors = oversellErrors;
    }

    public int getDriverOverloads() {
        return driverOverloads;
    }

    public void setDriverOverloads(int driverOverloads) {
        this.driverOverloads = driverOverloads;
    }

    public double getThroughputDrop() {
        return throughputDrop;
    }

    public void setThroughputDrop(double throughputDrop) {
        this.throughputDrop = throughputDrop;
    }

    public double calculateThroughput() {
        if (durationMs <= 0) {
            return 0.0;
        }
        // Throughput = Total Orders / Total Time in Seconds
        this.throughput = (double) totalOrders / (durationMs / 1000.0);
        return this.throughput;
    }

    public double calculateDrop(double baselineThroughput) {
        if (baselineThroughput <= 0) {
            return 0.0;
        }
        // Percentage Drop = ((Baseline - Current) / Baseline) * 100
        this.throughputDrop = ((baselineThroughput - this.throughput) / baselineThroughput) * 100.0;
        return this.throughputDrop;
    }

    @Override
    public String toString() {
        return String.format("SimulationRun[ID=%s, Mechanism=%-12s, Orders=%d, Time=%dms, Throughput=%.2f ops/sec, DoubleAssign=%d, Oversell=%d, Overload=%d, Drop=%.2f%%]",
                runId, mechanism, totalOrders, durationMs, throughput, doubleAssignments, oversellErrors, driverOverloads, throughputDrop);
    }
}
