package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.SimulationRun;

public class SimulationRunRepository {

    private static final Path FILE = Path.of("data", "simulation_runs.csv");

    public List<SimulationRun> findAll() {
        List<SimulationRun> runs = new ArrayList<>();
        for (String line : readLines()) {
            if (line.isBlank() || line.startsWith("runId")) {
                continue;
            }
            SimulationRun run = new SimulationRun();
            run.fromCsvLine(line);
            runs.add(run);
        }
        return runs;
    }

    public SimulationRun findById(String runId) {
        return findAll().stream()
                .filter(run -> run.getId().equals(runId))
                .findFirst()
                .orElse(null);
    }

    public synchronized void save(SimulationRun simulationRun) {
        if (simulationRun == null) {
            throw new IllegalArgumentException("simulationRun cannot be null");
        }

        List<SimulationRun> runs = findAll();
        runs.removeIf(run -> run.getId().equals(simulationRun.getId()));
        runs.add(simulationRun);

        try {
            Files.createDirectories(FILE.getParent());
            Files.write(FILE, runs.stream().map(SimulationRun::toCsvLine).toList());
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot write simulation_runs.csv", ex);
        }
    }

    private List<String> readLines() {
        try {
            if (!Files.exists(FILE)) {
                return List.of();
            }
            return Files.readAllLines(FILE);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot read simulation_runs.csv", ex);
        }
    }
}
