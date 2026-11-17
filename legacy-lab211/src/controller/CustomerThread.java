package controller;

import model.Customer;
import model.Restaurant;
import model.MenuItem;
import model.LockMechanism;
import exception.OversellException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

public class CustomerThread extends Thread {
    private CountDownLatch startLatch;
    private CountDownLatch finishLatch;
    private OrderController orderController;
    private Customer customer;
    private Restaurant restaurant;
    private MenuItem menuItem;
    private int quantity;
    private LockMechanism mechanism;
    private AtomicInteger oversellErrors;
    private AtomicInteger otherErrors;
    private List<String> createdOrderIds;

    public CustomerThread(CountDownLatch startLatch, CountDownLatch finishLatch,
                          OrderController orderController, Customer customer,
                          Restaurant restaurant, MenuItem menuItem, int quantity,
                          LockMechanism mechanism, AtomicInteger oversellErrors,
                          AtomicInteger otherErrors, List<String> createdOrderIds) {
        this.startLatch = startLatch;
        this.finishLatch = finishLatch;
        this.orderController = orderController;
        this.customer = customer;
        this.restaurant = restaurant;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.mechanism = mechanism;
        this.oversellErrors = oversellErrors;
        this.otherErrors = otherErrors;
        this.createdOrderIds = createdOrderIds;
    }

    @Override
    public void run() {
        try {
            this.startLatch.await(); // True concurrency barrier
            model.Order order = this.orderController.placeOrderAndDispatch(
                this.customer.getId(),
                this.restaurant.getId(),
                this.menuItem.getId(),
                this.quantity,
                this.mechanism
            );
            if (order != null && this.createdOrderIds != null) {
                synchronized (this.createdOrderIds) {
                    this.createdOrderIds.add(order.getId());
                }
            }
        } catch (OversellException e) {
            this.oversellErrors.incrementAndGet();
        } catch (Exception e) {
            this.otherErrors.incrementAndGet();
        } finally {
            this.finishLatch.countDown();
        }
    }
}
