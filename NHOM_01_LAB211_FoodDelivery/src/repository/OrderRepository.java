package repository;

import model.Order;
import model.OrderStatus;
import model.LockMechanism;
import model.Restaurant;
import model.Driver;
import model.MenuItem;
import model.OrderItem;
import model.GeoUtils;
import exception.DoubleAssignmentException;
import exception.EntityNotFoundException;
import exception.OversellException;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository extends CsvRepository<Order> {

    public OrderRepository() {
        super("data/orders.csv", "id,customerId,restaurantId,driverId,status,totalFoodAmount,deliveryFee,createdAtEpochMs,assignCount,version");
    }

    public OrderRepository(String customFilePath) {
        super(customFilePath, "id,customerId,restaurantId,driverId,status,totalFoodAmount,deliveryFee,createdAtEpochMs,assignCount,version");
    }

    @Override
    public Order createEntity() {
        return new Order();
    }

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
        double foodAmount = (item != null ? item.getPriceVnd() : 35000.0) * quantity;

        // 2. Calculate Haversine distance and real delivery fee
        Restaurant restaurant = restRepo.findById(restaurantId);
        double restLat = restaurant != null ? restaurant.getLatitude() : 13.7700;
        double restLon = restaurant != null ? restaurant.getLongitude() : 109.2200;

        GeoUtils geoUtils = new GeoUtils();
        double deliveryFee = 15000.0; // default base fee

        // 3. Create Order
        String orderId = "O" + System.currentTimeMillis() + "_" + (100 + (int)(Math.random() * 899));
        Order order = new Order(orderId, customerId, restaurantId, "", OrderStatus.PENDING,
                foodAmount, deliveryFee, System.currentTimeMillis(), 0, 1L);
        this.save(order);

        // 4. Save OrderItem
        String orderItemId = "OI" + System.currentTimeMillis() + "_" + (100 + (int)(Math.random() * 899));
        OrderItem orderItem = new OrderItem(orderItemId, orderId, menuItemId, quantity,
                item != null ? item.getPriceVnd() : 35000.0);
        orderItemRepo.save(orderItem);

        // 5. Find Nearest Available Driver using Haversine inside Repository
        Driver driver = driverRepo.findNearestAvailable(restLat, restLon);
        if (driver != null) {
            // Assign Driver to Order (handles Double Assignment check)
            this.assignDriver(orderId, driver.getId(), mechanism);
            // Mark Driver as Busy (handles Driver Overload check)
            driverRepo.markBusy(driver.getId(), orderId, mechanism);
        }

        return this.findById(orderId);
    }

    public boolean dispatchPendingOrder(String orderId, LockMechanism mechanism,
                                        DriverRepository driverRepo, RestaurantRepository restRepo) throws Exception {
        Order order = this.findById(orderId);
        if (order == null) {
            throw new EntityNotFoundException("Order", orderId);
        }

        Restaurant restaurant = restRepo.findById(order.getRestaurantId());
        double restLat = restaurant != null ? restaurant.getLatitude() : 13.7700;
        double restLon = restaurant != null ? restaurant.getLongitude() : 109.2200;

        Driver driver = driverRepo.findNearestAvailable(restLat, restLon);
        if (driver == null) {
            throw new EntityNotFoundException("Driver", "AVAILABLE_NEARBY");
        }

        this.assignDriver(orderId, driver.getId(), mechanism);
        driverRepo.markBusy(driver.getId(), orderId, mechanism);
        return true;
    }

    public List<String> autoDispatchAllPending(LockMechanism mechanism, DriverRepository driverRepo, RestaurantRepository restRepo) {
        List<String> logs = new ArrayList<>();
        List<Order> allOrders = this.findAll();
        for (int i = 0; i < allOrders.size(); i++) {
            Order order = allOrders.get(i);
            if (order.getStatus() == OrderStatus.PENDING || order.getDriverId() == null || order.getDriverId().trim().isEmpty()) {
                try {
                    boolean success = this.dispatchPendingOrder(order.getId(), mechanism, driverRepo, restRepo);
                    if (success) {
                        Order updated = this.findById(order.getId());
                        logs.add("Don hang [" + order.getId() + "] -> Da tu dong gan tai xe [" + updated.getDriverId() + "] thanh cong.");
                    }
                } catch (EntityNotFoundException enfe) {
                    logs.add("Khong tim thay them tai xe ranh tren mang luoi. Tam dung dieu phoi tu dong.");
                    break;
                } catch (Exception e) {
                    logs.add("Don hang [" + order.getId() + "] -> Chua the dieu phoi: " + e.getMessage());
                }
            }
        }
        return logs;
    }

    public boolean dispatchOrderToDriver(String orderId, String driverId, LockMechanism mechanism) throws Exception {
        return this.assignDriver(orderId, driverId, mechanism);
    }

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
