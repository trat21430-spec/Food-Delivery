package controller;

import model.Restaurant;
import model.MenuItem;
import repository.RestaurantRepository;
import repository.MenuItemRepository;
import java.util.List;

public class RestaurantController {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantController() {
        this.restaurantRepository = new RestaurantRepository();
        this.menuItemRepository = new MenuItemRepository();
    }

    public RestaurantController(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(String id) {
        return restaurantRepository.findById(id);
    }

    public List<MenuItem> getMenuByRestaurant(String restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
}