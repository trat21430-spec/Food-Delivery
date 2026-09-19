package controller;

import model.LockMechanism;
import model.Order;
import model.OrderItem;

import repository.OrderRepository;

import java.util.List;

public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Create a new order.
     */
    public Order placeOrder(String customerId,
                            String restaurantId,
                            List<OrderItem> items) {

        if (customerId == null || customerId.isBlank()) {
            return null;
        }

        if (restaurantId == null || restaurantId.isBlank()) {
            return null;
        }

        if (items == null || items.isEmpty()) {
            return null;
        }

        return orderRepository.createOrder(
                customerId,
                restaurantId,
                items
        );
    }

    /**
     * Confirm an order.
     */
    public boolean confirmOrder(String orderId) {

        if (orderId == null || orderId.isBlank()) {
            return false;
        }

        return orderRepository.confirmOrder(orderId);
    }

    /**
     * Cancel an order.
     *
     * Repository checks whether cancellation is allowed.
     */
    public boolean cancelOrder(String orderId) {

        if (orderId == null || orderId.isBlank()) {
            return false;
        }

        return orderRepository.cancelOrder(orderId);
    }

    /**
     * Dispatch driver for an order.
     *
     * Controller chooses the synchronization mechanism.
     * Actual assignment logic remains in Repository.
     */
    public boolean dispatchDriver(String orderId,
                                  LockMechanism mechanism) {

        if (orderId == null || orderId.isBlank()) {
            return false;
        }

        if (mechanism == null) {
            mechanism = LockMechanism.NO_LOCK;
        }

        switch (mechanism) {

            case NO_LOCK:
                return orderRepository.assignDriverNoLock(orderId);

            case FILE_LOCK:
                return orderRepository.assignDriverWithFileLock(orderId);

            case SYNCHRONIZED:
                return orderRepository.assignDriverWithSync(orderId);

            case OPTIMISTIC:
                return orderRepository.assignDriverWithOptimistic(orderId);

            default:
                return false;
        }
    }

    /**
     * Get order by ID.
     */
    public Order getOrder(String orderId) {

        if (orderId == null || orderId.isBlank()) {
            return null;
        }

        return orderRepository.findById(orderId);
    }

    /**
     * Mark order as delivered.
     */
    public boolean markDelivered(String orderId) {

        if (orderId == null || orderId.isBlank()) {
            return false;
        }

        return orderRepository.markDelivered(orderId);
    }

    /**
     * Complete normal order flow.
     */
    public boolean completeOrder(String orderId,
                                 LockMechanism mechanism) {

        if (!confirmOrder(orderId)) {
            return false;
        }

        if (!dispatchDriver(orderId, mechanism)) {
            return false;
        }

        return markDelivered(orderId);
    }
}