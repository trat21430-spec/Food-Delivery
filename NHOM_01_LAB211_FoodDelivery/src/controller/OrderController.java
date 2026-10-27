package controller;

import model.Order;
import model.OrderStatus;
import model.MenuItem;
import model.OrderItem;
import model.LockMechanism;
import repository.OrderRepository;
import repository.MenuItemRepository;
import repository.DriverRepository;
import repository.RestaurantRepository;
import repository.OrderItemRepository;
import exception.FoodDeliveryException;
import exception.InvalidInputException;

import java.util.List;

public class OrderController {
    private OrderRepository orderRepository;
    private MenuItemRepository menuItemRepository;
    private DriverRepository driverRepository;
    private RestaurantRepository restaurantRepository;
    private OrderItemRepository orderItemRepository;

    public OrderController() {
        this.orderRepository = new OrderRepository();
        this.menuItemRepository = new MenuItemRepository();
        this.driverRepository = new DriverRepository();
        this.restaurantRepository = new RestaurantRepository();
        this.orderItemRepository = new OrderItemRepository();
    }

    public OrderController(OrderRepository orderRepo, MenuItemRepository itemRepo, DriverRepository driverRepo, RestaurantRepository restRepo, OrderItemRepository orderItemRepo) {
        this.orderRepository = orderRepo;
        this.menuItemRepository = itemRepo;
        this.driverRepository = driverRepo;
        this.restaurantRepository = restRepo;
        this.orderItemRepository = orderItemRepo;
    }

    // --- ORDER CRUD ---

    /**
     * CREATE Order and coordinates dispatch flow through repository layer.
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

    /**
     * READ Orders
     */
    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    public Order getOrderById(String orderId) {
        return this.orderRepository.findById(orderId);
    }

    /**
     * READ OrderItems for a specific order.
     */
    public List<OrderItem> getOrderItemsByOrderId(String orderId) {
        return this.orderItemRepository.findByOrderId(orderId);
    }

    /**
     * READ MenuItem by ID.
     */
    public MenuItem getMenuItemById(String menuItemId) {
        return this.menuItemRepository.findById(menuItemId);
    }

    /**
     * UPDATE Order: Dispatches a pending order by delegating to the repository layer.
     */
    public boolean dispatchPending(String orderId, LockMechanism mechanism) throws Exception {
        return this.orderRepository.dispatchPendingOrder(orderId, mechanism, this.driverRepository, this.restaurantRepository);
    }

    /**
     * UPDATE Order: Customer cancels an order while it is still PENDING.
     */
    public boolean cancelPendingOrder(String orderId) throws FoodDeliveryException {
        Order order = this.orderRepository.findById(orderId);
        if (order == null) {
            throw new InvalidInputException("Order ID " + orderId + " not found.");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidInputException("Cannot cancel order [%s] because it is already [%s]".replace("%s", orderId));
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.incrementVersion();
        this.orderRepository.save(order);
        return true;
    }

    /**
     * DELETE Order
     */
    public boolean deleteOrder(String orderId) {
        Order order = this.orderRepository.findById(orderId);
        if (order != null) {
            this.orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }
}
