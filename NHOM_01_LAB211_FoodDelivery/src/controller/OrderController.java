package controller;

import model.Customer;
import model.Restaurant;
import model.MenuItem;
import model.OrderItem;
import model.Order;
import model.OrderStatus;
import model.LockMechanism;
import model.Driver;
import model.GeoUtils;
import repository.OrderRepository;
import repository.MenuItemRepository;
import repository.DriverRepository;
import repository.RestaurantRepository;
import repository.OrderItemRepository;
import repository.CustomerRepository;
import repository.DeliveryRouteRepository;
import exception.FoodDeliveryException;
import exception.InvalidInputException;

import java.util.ArrayList;
import java.util.List;

public class OrderController {
    private OrderRepository orderRepository;
    private MenuItemRepository menuItemRepository;
    private DriverRepository driverRepository;
    private RestaurantRepository restaurantRepository;
    private OrderItemRepository orderItemRepository;
    private CustomerRepository customerRepository;
    private DeliveryRouteRepository routeRepository;
    private boolean autoDispatchEnabled;

    public OrderController() {
        this.orderRepository = new OrderRepository();
        this.menuItemRepository = new MenuItemRepository();
        this.driverRepository = new DriverRepository();
        this.restaurantRepository = new RestaurantRepository();
        this.orderItemRepository = new OrderItemRepository();
        this.customerRepository = new CustomerRepository();
        this.routeRepository = new DeliveryRouteRepository();
        this.autoDispatchEnabled = true;
    }

    public OrderController(OrderRepository orderRepo, MenuItemRepository itemRepo, DriverRepository driverRepo,
                           RestaurantRepository restRepo, OrderItemRepository orderItemRepo) {
        this.orderRepository = orderRepo;
        this.menuItemRepository = itemRepo;
        this.driverRepository = driverRepo;
        this.restaurantRepository = restRepo;
        this.orderItemRepository = orderItemRepo;
        this.customerRepository = new CustomerRepository();
        this.routeRepository = new DeliveryRouteRepository();
        this.autoDispatchEnabled = true;
    }

    public OrderController(OrderRepository orderRepo, OrderItemRepository orderItemRepo,
                           DeliveryRouteRepository routeRepo, DriverRepository driverRepo,
                           MenuItemRepository itemRepo, RestaurantRepository restRepo,
                           CustomerRepository customerRepo) {
        this.orderRepository = orderRepo;
        this.orderItemRepository = orderItemRepo;
        this.routeRepository = routeRepo;
        this.driverRepository = driverRepo;
        this.menuItemRepository = itemRepo;
        this.restaurantRepository = restRepo;
        this.customerRepository = customerRepo;
        this.autoDispatchEnabled = true;
    }

    public boolean isAutoDispatchEnabled() {
        return this.autoDispatchEnabled;
    }

    public void setAutoDispatchEnabled(boolean autoDispatchEnabled) {
        this.autoDispatchEnabled = autoDispatchEnabled;
    }

    /**
     * CREATE Order and coordinates dispatch flow through repository layer (used by Simulator).
     */
    public Order placeOrderAndDispatch(String customerId, String restaurantId, String menuItemId, int quantity, LockMechanism mechanism) throws Exception {
        return this.orderRepository.processOrderAndDispatch(
            customerId,
            restaurantId,
            menuItemId,
            quantity,
            mechanism,
            this.menuItemRepository,
            this.driverRepository,
            this.restaurantRepository,
            this.orderItemRepository
        );
    }

    public Order createAndAutoDispatchOrder(String customerId, String restaurantId, String menuItemId, int quantity, LockMechanism mechanism) throws Exception {
        return this.placeOrderAndDispatch(customerId, restaurantId, menuItemId, quantity, mechanism);
    }

    /**
     * Realistic Customer order flow: Places order with status PENDING, waiting for Restaurant confirmation.
     */
    public Order createCustomerOrder(String customerId, String restaurantId, String menuItemId, int quantity, LockMechanism mechanism) throws Exception {
        if (quantity <= 0) {
            throw new InvalidInputException("So luong mon phai lon hon 0.");
        }
        boolean stockDeducted = this.menuItemRepository.deductStock(menuItemId, quantity, mechanism);
        if (!stockDeducted) {
            throw new exception.OversellException(menuItemId, quantity, 0);
        }

        MenuItem item = this.menuItemRepository.findById(menuItemId);
        double unitPrice = (item != null) ? item.getPriceVnd() : 35000.0;
        double foodAmount = unitPrice * quantity;

        Customer cust = this.customerRepository.findById(customerId);
        Restaurant rest = this.restaurantRepository.findById(restaurantId);
        double cLat = (cust != null) ? cust.getLatitude() : 13.775;
        double cLon = (cust != null) ? cust.getLongitude() : 109.225;
        double rLat = (rest != null) ? rest.getLatitude() : 13.770;
        double rLon = (rest != null) ? rest.getLongitude() : 109.220;

        GeoUtils geo = new GeoUtils();
        double distanceKm = geo.haversine(cLat, cLon, rLat, rLon);
        double deliveryFee = 15000.0 + (distanceKm * 5000.0);

        String orderId = "O" + System.currentTimeMillis() + "_" + (100 + (int)(Math.random() * 899));
        Order order = new Order(orderId, customerId, restaurantId, "", OrderStatus.PENDING,
                foodAmount, deliveryFee, System.currentTimeMillis(), 0, 1L);
        this.orderRepository.save(order);

        String orderItemId = "OI" + System.currentTimeMillis() + "_" + (100 + (int)(Math.random() * 899));
        OrderItem orderItem = new OrderItem(orderItemId, orderId, menuItemId, quantity, unitPrice);
        this.orderItemRepository.save(orderItem);

        return order;
    }

    /**
     * Realistic Restaurant Manager flow: Confirms order, triggers automatic driver dispatch.
     */
    /**
     * Customer places order, system simulates automated restaurant confirmation and immediate driver dispatch.
     */
    public Order placeCustomerOrderAndAutoDispatch(String customerId, String restaurantId, String menuItemId, int quantity, LockMechanism mechanism) throws Exception {
        Order order = this.createCustomerOrder(customerId, restaurantId, menuItemId, quantity, mechanism);
        return this.confirmOrderByRestaurant(order.getId(), restaurantId, mechanism);
    }

    public Order confirmOrderByRestaurant(String orderId, String restaurantId, LockMechanism mechanism) throws Exception {
        Order order = this.orderRepository.findById(orderId);
        if (order == null) {
            throw new InvalidInputException("Khong tim thay don hang: " + orderId);
        }
        if (restaurantId != null && !restaurantId.equalsIgnoreCase(order.getRestaurantId())) {
            throw new InvalidInputException("Ban khong co quyen xac nhan don hang cua nha hang khac.");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidInputException("Don hang khong o trang thai PENDING (Trang thai hien tai: " + order.getStatus() + ").");
        }

        try {
            this.orderRepository.dispatchPendingOrder(orderId, mechanism, this.driverRepository, this.restaurantRepository);
        } catch (exception.EntityNotFoundException enfe) {
            order = this.orderRepository.findById(orderId);
            order.setStatus(OrderStatus.CONFIRMED);
            order.incrementVersion();
            this.orderRepository.save(order);
        }
        return this.orderRepository.findById(orderId);
    }

    /**
     * Realistic Restaurant Manager flow: Rejects order and restores stock back to menu.
     */
    public boolean rejectOrderByRestaurant(String orderId, String restaurantId, String reason) throws Exception {
        Order order = this.orderRepository.findById(orderId);
        if (order == null) {
            throw new InvalidInputException("Khong tim thay don hang: " + orderId);
        }
        if (restaurantId != null && !restaurantId.equalsIgnoreCase(order.getRestaurantId())) {
            throw new InvalidInputException("Ban khong co quyen thao tac tren don hang cua nha hang khac.");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidInputException("Chi co the tu choi don hang dang cho xac nhan (PENDING).");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.incrementVersion();
        this.orderRepository.save(order);

        List<OrderItem> items = this.orderItemRepository.findByOrderId(orderId);
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            this.menuItemRepository.restoreStock(item.getMenuItemId(), item.getQuantity());
        }
        return true;
    }

    /**
     * Customer cancels their own pending order and restores stock.
     */
    public boolean cancelOrderByCustomer(String orderId, String customerId) throws Exception {
        Order order = this.orderRepository.findById(orderId);
        if (order == null) {
            throw new InvalidInputException("Khong tim thay don hang: " + orderId);
        }
        if (customerId != null && !customerId.equalsIgnoreCase(order.getCustomerId())) {
            throw new InvalidInputException("Ban khong co quyen huy don hang cua khach hang khac.");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidInputException("Don hang da duoc tiep nhan hoac dang giao, khong the huy luc nay.");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.incrementVersion();
        this.orderRepository.save(order);

        List<OrderItem> items = this.orderItemRepository.findByOrderId(orderId);
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            this.menuItemRepository.restoreStock(item.getMenuItemId(), item.getQuantity());
        }
        return true;
    }

    /**
     * Driver confirms picked up food at restaurant -> status changes to DISPATCHED.
     */
    public boolean markOrderPickedUp(String orderId, String driverId) throws Exception {
        Order order = this.orderRepository.findById(orderId);
        if (order == null) {
            throw new InvalidInputException("Khong tim thay don hang: " + orderId);
        }
        if (driverId != null && !driverId.equalsIgnoreCase(order.getDriverId())) {
            throw new InvalidInputException("Don hang nay khong duoc phan cong cho ban.");
        }
        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new InvalidInputException("Chi co the lay hang khi don hang o trang thai CONFIRMED.");
        }
        order.setStatus(OrderStatus.DISPATCHED);
        order.incrementVersion();
        this.orderRepository.save(order);
        return true;
    }

    /**
     * Driver confirms successful delivery -> status DELIVERED, driver marked AVAILABLE, triggers auto-dispatch.
     */
    public boolean markOrderDelivered(String orderId, String driverId, LockMechanism mechanism) throws Exception {
        Order order = this.orderRepository.findById(orderId);
        if (order == null) {
            throw new InvalidInputException("Khong tim thay don hang: " + orderId);
        }
        if (driverId != null && !driverId.equalsIgnoreCase(order.getDriverId())) {
            throw new InvalidInputException("Don hang nay khong duoc phan cong cho ban.");
        }
        if (order.getStatus() != OrderStatus.DISPATCHED && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new InvalidInputException("Don hang chua o trang thai dang giao de co the hoan tat.");
        }
        order.setStatus(OrderStatus.DELIVERED);
        order.incrementVersion();
        this.orderRepository.save(order);

        if (driverId != null && !driverId.isEmpty()) {
            this.driverRepository.markAvailable(driverId);
            if (this.autoDispatchEnabled) {
                this.orderRepository.autoDispatchAllPending(mechanism, this.driverRepository, this.restaurantRepository);
            }
        }
        return true;
    }

    public boolean hasActiveDelivery(String driverId) {
        if (driverId == null || driverId.isEmpty()) return false;
        List<Order> orders = this.getOrdersByDriver(driverId);
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getStatus() == OrderStatus.CONFIRMED || o.getStatus() == OrderStatus.DISPATCHED) {
                return true;
            }
        }
        return false;
    }

    public double calculateRestaurantRevenue(String restaurantId) {
        if (restaurantId == null) return 0.0;
        List<Order> list = this.getOrdersByRestaurant(restaurantId);
        double total = 0.0;
        for (int i = 0; i < list.size(); i++) {
            Order o = list.get(i);
            if (o.getStatus() == OrderStatus.DELIVERED) {
                total += o.getTotalFoodAmount();
            }
        }
        return total;
    }

    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    public Order getOrderById(String orderId) {
        return this.orderRepository.findById(orderId);
    }

    public boolean dispatchPending(String orderId, LockMechanism mechanism) throws Exception {
        return this.orderRepository.dispatchPendingOrder(orderId, mechanism, this.driverRepository, this.restaurantRepository);
    }

    public boolean completeDelivery(String orderId, LockMechanism mechanism) throws Exception {
        return this.markOrderDelivered(orderId, null, mechanism);
    }

    public boolean cancelPendingOrder(String orderId) throws FoodDeliveryException {
        Order order = this.orderRepository.findById(orderId);
        if (order == null) {
            throw new InvalidInputException("Order ID " + orderId + " not found.");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidInputException("Cannot cancel order " + orderId + " because status is " + order.getStatus());
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.incrementVersion();
        this.orderRepository.save(order);

        List<OrderItem> items = this.orderItemRepository.findByOrderId(orderId);
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            this.menuItemRepository.restoreStock(item.getMenuItemId(), item.getQuantity());
        }
        return true;
    }

    public boolean cancelOrder(String orderId) throws FoodDeliveryException {
        return this.cancelPendingOrder(orderId);
    }

    public List<String> autoDispatchAllPending(LockMechanism mechanism) {
        return this.orderRepository.autoDispatchAllPending(mechanism, this.driverRepository, this.restaurantRepository);
    }

    public int autoDispatchPendingOrders(LockMechanism mechanism) {
        List<String> logs = this.autoDispatchAllPending(mechanism);
        return logs.size();
    }

    public List<Order> getPendingOrders() {
        List<Order> all = this.orderRepository.findAll();
        List<Order> pending = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            Order o = all.get(i);
            if (o.getStatus() == OrderStatus.PENDING || o.getDriverId() == null || o.getDriverId().trim().isEmpty()) {
                pending.add(o);
            }
        }
        return pending;
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        List<Order> all = this.orderRepository.findAll();
        List<Order> result = new ArrayList<>();
        if (customerId == null) return result;
        for (int i = 0; i < all.size(); i++) {
            Order o = all.get(i);
            if (customerId.equalsIgnoreCase(o.getCustomerId())) {
                result.add(o);
            }
        }
        return result;
    }

    public List<Order> getOrdersByDriver(String driverId) {
        List<Order> all = this.orderRepository.findAll();
        List<Order> result = new ArrayList<>();
        if (driverId == null) return result;
        for (int i = 0; i < all.size(); i++) {
            Order o = all.get(i);
            if (driverId.equalsIgnoreCase(o.getDriverId())) {
                result.add(o);
            }
        }
        return result;
    }

    public List<Order> getOrdersByRestaurant(String restaurantId) {
        List<Order> all = this.orderRepository.findAll();
        List<Order> result = new ArrayList<>();
        if (restaurantId == null) return result;
        for (int i = 0; i < all.size(); i++) {
            Order o = all.get(i);
            if (restaurantId.equalsIgnoreCase(o.getRestaurantId())) {
                result.add(o);
            }
        }
        return result;
    }

    public boolean deleteOrder(String orderId) {
        Order order = this.orderRepository.findById(orderId);
        if (order != null) {
            this.orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }
}
