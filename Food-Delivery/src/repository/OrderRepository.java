/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author HP
 */

import exception.DoubleAssignmentException;
import exception.EntityNotFoundException;
import exception.OptimisticLockException;
import model.Order;
import java.util.List;

public class OrderRepository extends CsvRepository<Order> {

    public OrderRepository(String filePath) {
        super(filePath);
    }

    public synchronized boolean assignDriverWithSync(String orderId, String driverId) 
            throws DoubleAssignmentException, EntityNotFoundException {
        List<Order> orders = this.findAll();
        Order order = orders.stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElse(null);

        if (order == null) {
            throw new EntityNotFoundException("Order not found: " + orderId);
        }

        if (order.getDriverId() != null && !order.getDriverId().trim().isEmpty()) {
            throw new DoubleAssignmentException("Order " + orderId + " already assigned to driver: " + order.getDriverId());
        }

        order.setDriverId(driverId);
        order.setStatus("ASSIGNED");
        this.saveAll(orders);
        return true;
    }

    public boolean assignDriverWithOptimistic(String orderId, String driverId, int expectedVersion) 
            throws OptimisticLockException, DoubleAssignmentException, EntityNotFoundException {
        synchronized (this) {
            List<Order> orders = this.findAll();
            Order order = orders.stream()
                    .filter(o -> o.getId().equals(orderId))
                    .findFirst()
                    .orElse(null);

            if (order == null) {
                throw new EntityNotFoundException("Order not found: " + orderId);
            }

            if (order.getVersion() != expectedVersion) {
                throw new OptimisticLockException("Version mismatch for Order " + orderId + 
                        ". Expected: " + expectedVersion + ", Actual: " + order.getVersion());
            }

            if (order.getDriverId() != null && !order.getDriverId().trim().isEmpty()) {
                throw new DoubleAssignmentException("Order " + orderId + " already assigned!");
            }

            order.setDriverId(driverId);
            order.setStatus("ASSIGNED");
            order.setVersion(order.getVersion() + 1);
            this.saveAll(orders);
            return true;
        }
    }

    @Override
    protected Order fromCsvLine(String line) {
        String[] parts = line.split(",");
        Order order = new Order();
        order.setId(parts[0].trim());
        order.setCustomerId(parts[1].trim());
        order.setRestaurantId(parts[2].trim());
        order.setDriverId(parts[3].trim());
        order.setStatus(parts[4].trim());
        order.setTotalAmount(Double.parseDouble(parts[5].trim()));
        if (parts.length > 6) {
            order.setVersion(Integer.parseInt(parts[6].trim()));
        }
        return order;
    }

    @Override
    protected String toCsvLine(Order o) {
        return String.join(",", 
                o.getId(), 
                o.getCustomerId(), 
                o.getRestaurantId(),
                o.getDriverId() == null ? "" : o.getDriverId(), 
                o.getStatus(),
                String.valueOf(o.getTotalAmount()), 
                String.valueOf(o.getVersion()));
    }

    @Override
    protected String getHeader() {
        return "id,customerId,restaurantId,driverId,status,totalAmount,version";
    }
}
