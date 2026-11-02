package controller;

import model.Restaurant;
import model.MenuItem;
import repository.RestaurantRepository;
import repository.MenuItemRepository;

import java.util.List;

public class RestaurantController {
    private RestaurantRepository restaurantRepository;
    private MenuItemRepository menuItemRepository;

    public RestaurantController() {
        this.restaurantRepository = new RestaurantRepository();
        this.menuItemRepository = new MenuItemRepository();
    }

    public RestaurantController(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public Restaurant createRestaurant(String name, String address, double lat, double lon, double rating) {
        List<Restaurant> all = this.restaurantRepository.findAll();
        String id = String.format("R%03d", all.size() + 1);
        Restaurant rest = new Restaurant(id, name, "Am thuc", address, lat, lon, rating, true);
        this.restaurantRepository.save(rest);
        return rest;
    }

    public List<Restaurant> getAllRestaurants() {
        return this.restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(String id) {
        return this.restaurantRepository.findById(id);
    }

    public boolean updateRestaurant(String id, String newName, String newAddress, double newRating) {
        Restaurant rest = this.restaurantRepository.findById(id);
        if (rest != null) {
            rest.setName(newName);
            rest.setAddress(newAddress);
            rest.setRating(newRating);
            rest.incrementVersion();
            this.restaurantRepository.save(rest);
            return true;
        }
        return false;
    }

    public boolean deleteRestaurant(String id) {
        Restaurant rest = this.restaurantRepository.findById(id);
        if (rest != null) {
            this.restaurantRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public MenuItem createMenuItem(String restaurantId, String name, double price, int stockQty) {
        List<MenuItem> all = this.menuItemRepository.findAll();
        String id = String.format("M%04d", all.size() + 1);
        MenuItem item = new MenuItem(id, restaurantId, name, price, stockQty, 1L);
        this.menuItemRepository.save(item);
        return item;
    }

    public List<MenuItem> getMenuByRestaurant(String restaurantId) {
        return this.menuItemRepository.findByRestaurantId(restaurantId);
    }

    public MenuItem getMenuItemById(String id) {
        return this.menuItemRepository.findById(id);
    }

    public boolean updateMenuItem(String id, String newName, double newPrice, int newStock) {
        MenuItem item = this.menuItemRepository.findById(id);
        if (item != null) {
            item.setName(newName);
            item.setPriceVnd(newPrice);
            item.setStockQty(newStock);
            item.incrementVersion();
            this.menuItemRepository.save(item);
            return true;
        }
        return false;
    }

    public boolean updateStock(String menuItemId, int newStock) {
        MenuItem item = this.menuItemRepository.findById(menuItemId);
        if (item != null) {
            item.setStockQty(newStock);
            item.incrementVersion();
            this.menuItemRepository.save(item);
            return true;
        }
        return false;
    }

    public boolean deleteMenuItem(String id) {
        MenuItem item = this.menuItemRepository.findById(id);
        if (item != null) {
            this.menuItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
