package repository;

import model.Order;
import model.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class OrderRepository {
    private final ConcurrentHashMap<String, Order> orderMap = new ConcurrentHashMap<>();

    public void save(Order order) {
        orderMap.put(order.getOrderId(), order);
    }

    public Order findById(String orderId) {
        return orderMap.get(orderId);
    }

    public List<Order> findAll() {
        return new ArrayList<>(orderMap.values());
    }

    public synchronized List<Order> findByStatus(OrderStatus status) {
        List<Order> result = new ArrayList<>();
        for (Order o : orderMap.values()) {
            if (o.getStatus() == status) {
                result.add(o);
            }
        }
        return result;
    }

    public synchronized boolean confirmOrder(String orderId) {
        Order order = orderMap.get(orderId);
        if (order != null && order.getStatus() == OrderStatus.PENDING) {
            order.confirm();
            return true;
        }
        return false;
    }

    public synchronized boolean assignDriverToOrder(String orderId, String driverId) {
        Order order = orderMap.get(orderId);
        if (order != null && (order.getStatus() == OrderStatus.CONFIRMED || order.getStatus() == OrderStatus.PENDING)) {
            order.assignDriver(driverId);
            return true;
        }
        return false;
    }
}
