package controller;

import model.MenuItem;
import model.Restaurant;
import repository.MenuItemRepository;
import repository.RestaurantRepository;

import java.util.List;

public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantController(RestaurantRepository restaurantRepository,
                                MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    /**
     * Get restaurant by ID.
     */
    public Restaurant getRestaurant(String restaurantId) {

        if (restaurantId == null || restaurantId.isBlank()) {
            return null;
        }

        return restaurantRepository.findById(restaurantId);
    }

    /**
     * Get menu of a restaurant.
     */
    public List<MenuItem> getMenu(String restaurantId) {

        if (restaurantId == null || restaurantId.isBlank()) {
            return List.of();
        }

        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    /**
     * Update stock quantity.
     */
    public boolean updateStock(String menuItemId, int newStock) {

        if (menuItemId == null || menuItemId.isBlank()) {
            return false;
        }

        if (newStock < 0) {
            return false;
        }

        return menuItemRepository.updateStock(
                menuItemId,
                newStock
        );
    }
}