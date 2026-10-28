package repository;

import model.Restaurant;

public class RestaurantRepository extends CsvRepository<Restaurant> {

    public RestaurantRepository() {
        super("data/restaurants.csv", "id,name,address,latitude,longitude,rating,createdAt,updatedAt,version");
    }

    public RestaurantRepository(String customFilePath) {
        super(customFilePath, "id,name,address,latitude,longitude,rating,createdAt,updatedAt,version");
    }

    @Override
    public Restaurant createEntity() {
        return new Restaurant();
    }
}
