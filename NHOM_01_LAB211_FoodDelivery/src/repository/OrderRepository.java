package repository;

import model.Order;
import model.OrderStatus;
import model.LockMechanism;
import model.Restaurant;
import model.Driver;
import model.MenuItem;
import model.OrderItem;
import exception.DoubleAssignmentException;
import exception.EntityNotFoundException;
import exception.OversellException;

import java.time.LocalDateTime;

public class OrderRepository extends CsvRepository<Order> {

    public OrderRepository() {
        super("data/orders.csv", "id,customerId,restaurantId,driverId,status,totalAmount,deliveryFee,createdAt,updatedAt,version");
    }

    public OrderRepository(String customFilePath) {
        super(customFilePath, "id,customerId,restaurantId,driverId,status,totalAmount,deliveryFee,createdAt,updatedAt,version");
    }

    @Override
    public Order createEntity() {
        return new Order();
    }

    /**
     * Dispatch logic and full order processing placed inside Repository Layer as required by architecture specifications.
     */
    public Order processOrderAndDispatch(String customerId, String restaurantId, String menuItemId, int quantity,
                                         LockMechanism mechanism, MenuItemRepository itemRepo,
                                         DriverRepository driverRepo, RestaurantRepository restRepo,
                                         OrderItemRepository orderItemRepo) throws Exception {
        // 1. Deduct stock in MenuItemRepository (handles Oversell check)
        boolean stockDeducted = itemRepo.deductStock(menuItemId, quantity, mechanism);
        if (!stockDeducted) {
            throw new OversellException(menuItemId, quantity, 0);
        }

        MenuItem item = itemRepo.findById(menuItemId);
        double totalAmount = item != null ? item.getPrice() * quantity : 10.0;

        // 2. Create Pending Order
        String orderId = "O" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
        Order order = new Order(orderId, customerId, restaurantId, "", OrderStatus.PENDING, totalAmount, 2.0, LocalDateTime.now(), LocalDateTime.now(), 1L);
        this.save(order);

        // 3. Save OrderItem
        String orderItemId = "OI" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
        OrderItem orderItem = new OrderItem(orderItemId, orderId, menuItemId, quantity, item != null ? item.getPrice() : 10.0, LocalDateTime.now(), LocalDateTime.now(), 1L);
        orderItemRepo.save(orderItem);

        // 4. Find Nearest Available Driver using Haversine inside Repository
        Restaurant restaurant = restRepo.findById(restaurantId);
        double restLat = restaurant != null ? restaurant.getLatitude() : 21.0285;
        double restLon = restaurant != null ? restaurant.getLongitude() : 105.8542;

        Driver driver = driverRepo.findNearestAvailable(restLat, restLon);
        if (driver == null) {
            throw new EntityNotFoundException("Driver", "AVAILABLE_NEARBY");
        }

        // 5. Assign Driver to Order (handles Double Assignment check)
        this.assignDriver(orderId, driver.getId(), mechanism);

        // 6. Mark Driver as Busy (handles Driver Overload check)
        driverRepo.markBusy(driver.getId(), orderId, mechanism);

        return this.findById(orderId);
    }

    /**
     * Dispatches an existing pending order to the nearest available driver.
     */
    public boolean dispatchPendingOrder(String orderId, LockMechanism mechanism,
                                        DriverRepository driverRepo, RestaurantRepository restRepo) throws Exception {
        Order order = this.findById(orderId);
        if (order == null) {
            throw new EntityNotFoundException("Order", orderId);
        }

        Restaurant restaurant = restRepo.findById(order.getRestaurantId());
        double restLat = restaurant != null ? restaurant.getLatitude() : 21.0285;
        double restLon = restaurant != null ? restaurant.getLongitude() : 105.8542;

        Driver driver = driverRepo.findNearestAvailable(restLat, restLon);
        if (driver == null) {
            throw new EntityNotFoundException("Driver", "AVAILABLE_NEARBY");
        }

        this.assignDriver(orderId, driver.getId(), mechanism);
        driverRepo.markBusy(driver.getId(), orderId, mechanism);
        return true;
    }

    /**
     * Assigns a driver to an order using the specified LockMechanism.
     */
    public boolean assignDriver(String orderId, String driverId, LockMechanism mechanism) throws Exception {
        switch (mechanism) {
            case NO_LOCK:
                return this.assignDriverNoLock(orderId, driverId);
            case SYNCHRONIZED:
                return this.assignDriverSynchronized(orderId, driverId);
            case OPTIMISTIC:
                return this.assignDriverOptimistic(orderId, driverId);
            case FILE_LOCK:
                return this.assignDriverFileLock(orderId, driverId);
            default:
                return this.assignDriverNoLock(orderId, driverId);
        }
    }

    // 1. NO_LOCK (Simulates Double Assignment)
    private boolean assignDriverNoLock(String orderId, String driverId) throws DoubleAssignmentException {
        Order order = this.findById(orderId);
        if (order == null) {
            return false;
        }

        if (order.getDriverId() != null && !order.getDriverId().trim().isEmpty()) {
            throw new DoubleAssignmentException(orderId, order.getDriverId(), driverId);
        }

        try {
            Thread.sleep(3);
        } catch (InterruptedException ignored) {
        }

        order.setDriverId(driverId);
        order.setStatus(OrderStatus.CONFIRMED);
        this.save(order);
        return true;
    }

    // 2. SYNCHRONIZED per Order ID Key
    private boolean assignDriverSynchronized(String orderId, String driverId) throws DoubleAssignmentException {
        synchronized (this.getKeyLock("ORDER_" + orderId)) {
            Order order = this.findById(orderId);
            if (order == null) {
                return false;
            }

            if (order.getDriverId() != null && !order.getDriverId().trim().isEmpty()) {
                throw new DoubleAssignmentException(orderId, order.getDriverId(), driverId);
            }

            try {
                Thread.sleep(3);
            } catch (InterruptedException ignored) {
            }

            order.setDriverId(driverId);
            order.setStatus(OrderStatus.CONFIRMED);
            this.save(order);
            return true;
        }
    }

    // 3. OPTIMISTIC
    private boolean assignDriverOptimistic(String orderId, String driverId) throws Exception {
        Order order = this.findById(orderId);
        if (order == null) {
            return false;
        }

        if (order.getDriverId() != null && !order.getDriverId().trim().isEmpty()) {
            throw new DoubleAssignmentException(orderId, order.getDriverId(), driverId);
        }

        long expectedVersion = order.getVersion();
        try {
            Thread.sleep(3);
        } catch (InterruptedException ignored) {
        }

        order.setDriverId(driverId);
        order.setStatus(OrderStatus.CONFIRMED);
        this.updateWithOptimisticLock(order, expectedVersion);
        return true;
    }

    // 4. FILE_LOCK
    private boolean assignDriverFileLock(String orderId, String driverId) throws DoubleAssignmentException {
        Boolean result = this.executeWithFileLock(() -> {
            Order order = this.findById(orderId);
            if (order == null) {
                return false;
            }

            if (order.getDriverId() != null && !order.getDriverId().trim().isEmpty()) {
                return false;
            }

            try {
                Thread.sleep(3);
            } catch (InterruptedException ignored) {
            }

            order.setDriverId(driverId);
            order.setStatus(OrderStatus.CONFIRMED);
            this.save(order);
            return true;
        });

        if (result == null || !result) {
            Order order = this.findById(orderId);
            String existing = order != null ? order.getDriverId() : "UNKNOWN";
            throw new DoubleAssignmentException(orderId, existing, driverId);
        }

        return true;
    }
}
