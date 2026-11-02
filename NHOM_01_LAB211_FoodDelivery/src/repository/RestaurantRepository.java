package repository;

import model.Restaurant;

public class RestaurantRepository extends CsvRepository<Restaurant> {

    public RestaurantRepository() {
        super("data/restaurants.csv", "id,name,category,address,latitude,longitude,rating,open");
    }

    public RestaurantRepository(String filePath) {
        super(filePath, "id,name,category,address,latitude,longitude,rating,open");
    }

    @Override
    public Restaurant createEntity() {
        return new Restaurant();
    }
}
