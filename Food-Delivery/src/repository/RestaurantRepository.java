package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.Restaurant;

public class RestaurantRepository {

    private static final Path FILE = Path.of("data", "restaurants.csv");

    public List<Restaurant> findAll() {
        List<Restaurant> restaurants = new ArrayList<>();
        for (String line : readLines()) {
            if (line.isBlank() || line.startsWith("restaurantId")) {
                continue;
            }
            Restaurant restaurant = new Restaurant();
            restaurant.fromCsvLine(line);
            restaurants.add(restaurant);
        }
        return restaurants;
    }

    public Restaurant findById(String restaurantId) {
        return findAll().stream()
                .filter(restaurant -> restaurant.getId().equals(restaurantId))
                .findFirst()
                .orElse(null);
    }

    private List<String> readLines() {
        try {
            if (!Files.exists(FILE)) {
                return List.of();
            }
            return Files.readAllLines(FILE);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot read restaurants.csv", ex);
        }
    }
}
