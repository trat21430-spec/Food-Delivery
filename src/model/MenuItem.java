package model;

public class MenuItem {
    private String itemId;
    private String restaurantId;
    private String name;
    private double price;
    private int stock;
    private long version;

    public MenuItem() {
    }

    public MenuItem(String itemId, String restaurantId, String name, double price, int stock, long version) {
        this.itemId = itemId;
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.version = version;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public boolean isAvailable(int quantity) {
        return this.stock >= quantity;
    }

    public boolean deductStock(int quantity) {
        if (isAvailable(quantity)) {
            this.stock -= quantity;
            this.version++;
            return true;
        }
        return false;
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
        this.version++;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "itemId='" + itemId + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", version=" + version +
                '}';
    }
}
