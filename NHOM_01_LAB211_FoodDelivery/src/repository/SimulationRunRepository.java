package repository;

import model.SimulationRun;

public class SimulationRunRepository extends CsvRepository<SimulationRun> {

    public SimulationRunRepository() {
        super("data/simulation_runs.csv", "id,mechanism,totalOrders,durationMs,throughputOpsPerSec,doubleAssignments,oversells,driverOverloads,totalErrors,createdAt,updatedAt,version");
    }

    public SimulationRunRepository(String customFilePath) {
        super(customFilePath, "id,mechanism,totalOrders,durationMs,throughputOpsPerSec,doubleAssignments,oversells,driverOverloads,totalErrors,createdAt,updatedAt,version");
    }

    @Override
    public SimulationRun createEntity() {
        return new SimulationRun();
    }
}
