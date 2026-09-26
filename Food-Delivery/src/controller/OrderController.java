package controller;

import model.*;
import model.Enums.LockMechanism;
import model.Enums.OrderStatus;
import repository.*;
import exception.*;
import java.util.List;

public class OrderController {
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final DriverRepository driverRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderController() {
        this.orderRepository = new OrderRepository();
        this.menuItemRepository = new MenuItemRepository();
        this.driverRepository = new DriverRepository();
        this.restaurantRepository = new RestaurantRepository();
        this.orderItemRepository = new OrderItemRepository();
    }

    public OrderController(OrderRepository orderRepo, MenuItemRepository itemRepo, DriverRepository driverRepo,
            RestaurantRepository restRepo, OrderItemRepository orderItemRepo) {
        this.orderRepository = orderRepo;
        this.menuItemRepository = itemRepo;
        this.driverRepository = driverRepo;
        this.restaurantRepository = restRepo;
        this.orderItemRepository = orderItemRepo;
    }

    public Order placeOrderAndDispatch(String customerId, String restaurantId, String menuItemId, int quantity,
            LockMechanism mechanism) throws Exception {
        // 1. Deduct stock in MenuItemRepository (handles Oversell check)
        boolean stockDeducted = menuItemRepository.deductStock(menuItemId, quantity, mechanism);
        if (!stockDeducted) {
            throw new OversellException(menuItemId, quantity, 0);
        }
          MenuItem item = menuItemRepository.findById(menuItemId);
        double totalAmount = item != null ? item.getPrice() * quantity : 10.0;
        // 2. Create Pending Order
        String orderId = "O" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        Order order = new Order(orderId, customerId, restaurantId, "", OrderStatus.PENDING, totalAmount, 2.0, java.time.LocalDateTime.now(), java.time.LocalDateTime.now(), 1L);
        orderRepository.save(order);
        // 3. Save OrderItem
        String orderItemId = "OI" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        OrderItem orderItem = new OrderItem(orderItemId, orderId, menuItemId, quantity, item != null ? item.getPrice() : 10.0, java.time.LocalDateTime.now(), java.time.LocalDateTime.now(), 1L);
        orderItemRepository.save(orderItem);
        // 4. Find Nearest Available Driver
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        double restLat = restaurant != null ? restaurant.getLatitude() : 21.0285;
        double restLon = restaurant != null ? restaurant.getLongitude() : 105.8542;
        Driver driver = driverRepository.findNearestAvailable(restLat, restLon);
        if (driver == null) {
            throw new EntityNotFoundException("Driver", "AVAILABLE_NEARBY");
        }
         5. Assign Driver to Order (handles Double Assignment check)
        orderRepository.assignDriver(orderId, driver.getId(), mechanism);
        // 6. Mark Driver as Busy (handles Driver Overload check)
        driverRepository.markBusy(driver.getId(), orderId, mechanism);
        return orderRepository.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
