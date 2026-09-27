package model;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private String restaurantId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private final List<MenuItem> menuItems;

    public Restaurant() {
        this.menuItems = new ArrayList<>();
    }

    public Restaurant(String restaurantId, String name, String address, double latitude, double longitude) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.menuItems = new ArrayList<>();
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    // --- BUSINESS METHODS FROM CLASS DIAGRAM ---

    public void addMenuItem(MenuItem item) {
        if (item != null) {
            menuItems.add(item);
        }
    }

    public void updateMenuItem(MenuItem updatedItem) {
        for (int i = 0; i < menuItems.size(); i++) {
            if (menuItems.get(i).getItemId().equals(updatedItem.getItemId())) {
                menuItems.set(i, updatedItem);
                return;
            }
        }
    }

    public void deleteMenuItem(String itemId) {
        menuItems.removeIf(item -> item.getItemId().equals(itemId));
    }

    public void updateStock(String itemId, int newStock) {
        for (MenuItem item : menuItems) {
            if (item.getItemId().equals(itemId)) {
                item.setStock(newStock);
                return;
            }
        }
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurantId='" + restaurantId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", menuItemsCount=" + menuItems.size() +
                '}';
    }
}
