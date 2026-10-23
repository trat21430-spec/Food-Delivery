package controller;

import model.Restaurant;
import model.MenuItem;
import repository.RestaurantRepository;
import repository.MenuItemRepository;

import java.time.LocalDateTime;
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

    // --- RESTAURANT CRUD ---

    public Restaurant createRestaurant(String name, String address, double lat, double lon, double rating) {
        String id = "R" + (100 + (int)(Math.random() * 900));
        Restaurant rest = new Restaurant(id, name, address, lat, lon, rating, LocalDateTime.now(), LocalDateTime.now(), 1L);
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

    // --- MENU ITEM CRUD ---

    public MenuItem createMenuItem(String restaurantId, String name, double price, int stockQty) {
        String id = "M" + (10000 + (int)(Math.random() * 90000));
        MenuItem item = new MenuItem(id, restaurantId, name, price, stockQty, LocalDateTime.now(), LocalDateTime.now(), 1L);
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
            item.setPrice(newPrice);
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
