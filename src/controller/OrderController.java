package controller;

import model.Driver;
import model.Order;
import model.OrderStatus;
import repository.DriverRepository;
import repository.MenuItemRepository;
import repository.OrderRepository;

import java.util.List;
import java.util.UUID;

public class OrderController {
    private final OrderRepository orderRepo;
    private final MenuItemRepository menuRepo;
    private final DriverRepository driverRepo;

    public OrderController(OrderRepository orderRepo, MenuItemRepository menuRepo, DriverRepository driverRepo) {
        this.orderRepo = orderRepo;
        this.menuRepo = menuRepo;
        this.driverRepo = driverRepo;
    }

    public Order placeOrder(String customerId, String restaurantId, String itemId, int quantity, double unitPrice) {
        // Step 1: Check & deduct stock at repository level
        boolean stockDeducted = menuRepo.checkAndDeductStock(itemId, quantity);
        if (!stockDeducted) {
            System.out.println("❌ Place Order Failed: Item out of stock or insufficient quantity!");
            return null;
        }

        // Step 2: Create new Order
        Order order = new Order();
        order.setOrderId("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setCustomerId(customerId);
        order.setRestaurantId(restaurantId);
        order.setTotalAmount(quantity * unitPrice);

        orderRepo.save(order);
        System.out.println("✅ Order Placed Successfully: " + order.getOrderId() + " [Status: " + order.getStatus() + "]");
        return order;
    }

    public boolean confirmOrder(String orderId) {
        boolean success = orderRepo.confirmOrder(orderId);
        if (success) {
            System.out.println("✅ Restaurant confirmed Order: " + orderId);
        } else {
            System.out.println("❌ Confirmation failed for Order: " + orderId);
        }
        return success;
    }

    public boolean dispatchOrder(String orderId, double restLat, double restLon) {
        // Step 1: Find nearest available driver at Repository layer
        Driver nearestDriver = driverRepo.findNearestAvailable(restLat, restLon);
        if (nearestDriver == null) {
            System.out.println("❌ Dispatch Failed: No AVAILABLE driver near restaurant!");
            return false;
        }

        // Step 2: Assign driver to Order and update status
        boolean assigned = driverRepo.assignDriver(nearestDriver.getDriverId());
        if (assigned) {
            orderRepo.assignDriverToOrder(orderId, nearestDriver.getDriverId());
            System.out.println("✅ Dispatch Success: Assigned Order " + orderId + " to Driver " + nearestDriver.getName() + " (" + nearestDriver.getDriverId() + ")");
            return true;
        } else {
            System.out.println("❌ Dispatch Failed: Driver was assigned by another thread!");
            return false;
        }
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
}
