package repository;

import model.SimulationRun;

public class SimulationRunRepository extends CsvRepository<SimulationRun> {

    public SimulationRunRepository() {
        super("data/simulation_runs.csv", "id,mechanism,totalOrders,durationMs,throughputOpsPerSec,throughputDropPercent,doubleAssignErrors,oversellErrors,driverOverloadErrors");
    }

    public SimulationRunRepository(String filePath) {
        super(filePath, "id,mechanism,totalOrders,durationMs,throughputOpsPerSec,throughputDropPercent,doubleAssignErrors,oversellErrors,driverOverloadErrors");
    }

    @Override
    public SimulationRun createEntity() {
        return new SimulationRun();
    }
}
