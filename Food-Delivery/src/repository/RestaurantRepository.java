package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.Restaurant;

public class RestaurantRepository extends CsvRepository<Restaurant> {

    public RestaurantRepository() {
        super("data/restaurants.csv");
    }

    @Override
    protected Restaurant fromCsvLine(String line) {
        Restaurant restaurant = new Restaurant();
        String[] parts = line.split(",(?=(?:[^"]*\"[^"]*\")*[^"]*$)");
        if (parts.length < 9) {
            return restaurant;
        }
        restaurant.setId(parts[0].trim());
        restaurant.setName(parts[1].trim());
        restaurant.setAddress(parts[2].trim());
        restaurant.setLatitude(Double.parseDouble(parts[3].trim()));
        restaurant.setLongitude(Double.parseDouble(parts[4].trim()));
        restaurant.setRating(Double.parseDouble(parts[5].trim()));
        restaurant.setCreatedAt(java.time.LocalDateTime.parse(parts[6].trim()));
        restaurant.setUpdatedAt(java.time.LocalDateTime.parse(parts[7].trim()));
        restaurant.setVersion(Long.parseLong(parts[8].trim()));
        return restaurant;
    }

    @Override
    protected String toCsvLine(Restaurant restaurant) {
        return String.join(",",
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                String.valueOf(restaurant.getLatitude()),
                String.valueOf(restaurant.getLongitude()),
                String.valueOf(restaurant.getRating()),
                restaurant.getCreatedAt().toString(),
                restaurant.getUpdatedAt().toString(),
                String.valueOf(restaurant.getVersion()));
    }

    @Override
    protected String getHeader() {
        return "id,name,address,latitude,longitude,rating,createdAt,updatedAt,version";
    }
}
