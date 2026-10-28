package util;

import model.Customer;
import model.Restaurant;
import model.MenuItem;
import model.Driver;
import model.DriverStatus;
import model.Order;
import model.OrderStatus;
import model.OrderItem;
import model.DeliveryRoute;
import repository.CustomerRepository;
import repository.RestaurantRepository;
import repository.MenuItemRepository;
import repository.DriverRepository;
import repository.OrderRepository;
import repository.OrderItemRepository;
import repository.DeliveryRouteRepository;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    private String dataDir;

    public DataGenerator() {
        this.dataDir = "data";
    }

    public static void main(String[] args) {
        System.out.println("=== STARTING DATA GENERATOR FOR LAB211 FOOD DELIVERY NETWORK ===");
        DataGenerator generator = new DataGenerator();
        generator.generateAllData();
        System.out.println("=== DATA GENERATION COMPLETE! ===");
    }

    public void generateAllData() {
        File dir = new File(this.dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        Random rand = new Random(42);

        // 1. Generate Customers (2,000 rows)
        System.out.println("Generating customers.csv (2,000 rows)...");
        CustomerRepository customerRepo = new CustomerRepository();
        List<Customer> customers = new ArrayList<>();
        String[] firstNames = new String[] {"Nguyen", "Tran", "Le", "Pham", "Hoang", "Vu", "Vn", "Dinh", "Bui", "Do"};
        String[] lastNames = new String[] {"An", "Binh", "Cuong", "Dung", "Em", "Phuong", "Giang", "Hoa", "Hung", "Khanh", "Linh", "Minh", "Nam"};

        for (int i = 1; i <= 2000; i++) {
            String id = String.format("C%04d", i);
            String name = firstNames[rand.nextInt(firstNames.length)] + " " + lastNames[rand.nextInt(lastNames.length)];
            String phone = "09" + String.format("%08d", rand.nextInt(100000000));
            String address = (1 + rand.nextInt(500)) + " Street " + (1 + rand.nextInt(50)) + ", Hanoi";
            double lat = 21.000 + (rand.nextDouble() * 0.060);
            double lon = 105.800 + (rand.nextDouble() * 0.100);

            customers.add(new Customer(id, name, phone, address, lat, lon, LocalDateTime.now().minusDays(rand.nextInt(30)), LocalDateTime.now(), 1L));
        }
        customerRepo.saveAll(customers);

        // 2. Generate Restaurants (200 rows)
        System.out.println("Generating restaurants.csv (200 rows)...");
        RestaurantRepository restaurantRepo = new RestaurantRepository();
        List<Restaurant> restaurants = new ArrayList<>();
        String[] restTypes = new String[] {"Pho", "Bun Cha", "Com Tam", "Banh Mi", "Pizza", "Burger", "Milktea", "Sushi", "BBQ", "Hotpot"};

        for (int i = 1; i <= 200; i++) {
            String id = String.format("R%03d", i);
            String name = restTypes[rand.nextInt(restTypes.length)] + " " + (100 + i);
            String address = (1 + rand.nextInt(300)) + " Nguyen Trai, Hanoi";
            double lat = 21.010 + (rand.nextDouble() * 0.040);
            double lon = 105.810 + (rand.nextDouble() * 0.080);
            double rating = 3.5 + (rand.nextDouble() * 1.5);

            restaurants.add(new Restaurant(id, name, address, lat, lon, Math.round(rating * 10.0) / 10.0, LocalDateTime.now().minusDays(rand.nextInt(60)), LocalDateTime.now(), 1L));
        }
        restaurantRepo.saveAll(restaurants);

        // 3. Generate Menu Items (2,000 rows, 10 items per restaurant)
        System.out.println("Generating menu_items.csv (2,000 rows)...");
        MenuItemRepository itemRepo = new MenuItemRepository();
        List<MenuItem> menuItems = new ArrayList<>();
        String[] foodPrefixes = new String[] {"Special", "Crispy", "Grilled", "Traditional", "Spicy", "Deluxe", "Fresh", "Roasted", "Steamed", "Golden"};
        String[] foodBases = new String[] {"Noodles", "Rice Dish", "Sandwich", "Soup", "Combo", "Salad", "Beverage", "Roll", "Dessert", "Platter"};

        int itemCounter = 1;
        for (int r = 0; r < restaurants.size(); r++) {
            Restaurant rest = restaurants.get(r);
            for (int k = 1; k <= 10; k++) {
                String id = String.format("M%05d", itemCounter++);
                String name = foodPrefixes[rand.nextInt(foodPrefixes.length)] + " " + foodBases[rand.nextInt(foodBases.length)] + " " + k;
                double price = 2.0 + (rand.nextDouble() * 18.0);
                int stock = 20 + rand.nextInt(150);

                menuItems.add(new MenuItem(id, rest.getId(), name, Math.round(price * 100.0) / 100.0, stock, LocalDateTime.now().minusDays(rand.nextInt(30)), LocalDateTime.now(), 1L));
            }
        }
        itemRepo.saveAll(menuItems);

        // 4. Generate Drivers (500 rows)
        System.out.println("Generating drivers.csv (500 rows)...");
        DriverRepository driverRepo = new DriverRepository();
        List<Driver> drivers = new ArrayList<>();

        for (int i = 1; i <= 500; i++) {
            String id = String.format("D%04d", i);
            String name = "Driver " + lastNames[rand.nextInt(lastNames.length)] + " " + i;
            String phone = "08" + String.format("%08d", rand.nextInt(100000000));
            double lat = 21.005 + (rand.nextDouble() * 0.050);
            double lon = 105.805 + (rand.nextDouble() * 0.090);
            DriverStatus status = (i <= 400) ? DriverStatus.AVAILABLE : DriverStatus.BUSY;

            drivers.add(new Driver(id, name, phone, status, lat, lon, "", LocalDateTime.now().minusDays(rand.nextInt(45)), LocalDateTime.now(), 1L));
        }
        driverRepo.saveAll(drivers);

        // 5. Generate Orders (3,000 rows)
        System.out.println("Generating orders.csv (3,000 rows)...");
        OrderRepository orderRepo = new OrderRepository();
        List<Order> orders = new ArrayList<>();

        for (int i = 1; i <= 3000; i++) {
            String id = String.format("O%05d", i);
            String customerId = String.format("C%04d", 1 + rand.nextInt(2000));
            String restaurantId = String.format("R%03d", 1 + rand.nextInt(200));
            String driverId = String.format("D%04d", 1 + rand.nextInt(500));
            OrderStatus status = OrderStatus.DELIVERED;
            if (i > 2900) status = OrderStatus.PENDING;
            else if (i > 2800) status = OrderStatus.CONFIRMED;

            double totalAmount = 10.0 + (rand.nextDouble() * 40.0);
            double fee = 1.5 + (rand.nextDouble() * 3.5);

            orders.add(new Order(id, customerId, restaurantId, (status == OrderStatus.PENDING ? "" : driverId), status, Math.round(totalAmount * 100.0) / 100.0, Math.round(fee * 100.0) / 100.0, LocalDateTime.now().minusDays(rand.nextInt(15)), LocalDateTime.now(), 1L));
        }
        orderRepo.saveAll(orders);

        // 6. Generate Order Items (9,000 rows: exactly 3 items per order)
        System.out.println("Generating order_items.csv (9,000 rows)...");
        OrderItemRepository orderItemRepo = new OrderItemRepository();
        List<OrderItem> orderItems = new ArrayList<>();

        int oiCounter = 1;
        for (int i = 1; i <= 3000; i++) {
            String orderId = String.format("O%05d", i);
            for (int k = 1; k <= 3; k++) {
                String id = String.format("OI%06d", oiCounter++);
                String menuItemId = String.format("M%05d", 1 + rand.nextInt(2000));
                int qty = 1 + rand.nextInt(3);
                double price = 5.0 + (rand.nextDouble() * 15.0);

                orderItems.add(new OrderItem(id, orderId, menuItemId, qty, Math.round(price * 100.0) / 100.0, LocalDateTime.now().minusDays(rand.nextInt(15)), LocalDateTime.now(), 1L));
            }
        }
        orderItemRepo.saveAll(orderItems);

        // 7. Generate Delivery Routes (2,000 rows)
        System.out.println("Generating delivery_routes.csv (2,000 rows)...");
        DeliveryRouteRepository routeRepo = new DeliveryRouteRepository();
        List<DeliveryRoute> routes = new ArrayList<>();

        for (int i = 1; i <= 2000; i++) {
            String id = String.format("RT%05d", i);
            String orderId = String.format("O%05d", i);
            String driverId = String.format("D%04d", 1 + rand.nextInt(500));
            double dist = 1.0 + (rand.nextDouble() * 8.0);
            int minutes = (int)(dist * 3.5) + rand.nextInt(10);
            String json = "[{\"lat\":21.02,\"lon\":105.83},{\"lat\":21.03,\"lon\":105.84}]";

            routes.add(new DeliveryRoute(id, orderId, driverId, Math.round(dist * 10.0) / 10.0, minutes, json, LocalDateTime.now().minusDays(rand.nextInt(15)), LocalDateTime.now(), 1L));
        }
        routeRepo.saveAll(routes);

        System.out.println("Total Rows Generated: " + (customers.size() + restaurants.size() + menuItems.size() + drivers.size() + orders.size() + orderItems.size() + routes.size()));
    }
}
