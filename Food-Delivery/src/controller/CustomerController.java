package controller;

import model.Customer;
import model.Order;
import repository.CustomerRepository;
import repository.OrderRepository;

import java.util.List;

public class CustomerController {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerController(CustomerRepository customerRepository,
                              OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Register a new customer.
     */
    public boolean register(Customer customer) {

        if (customer == null) {
            return false;
        }

        return customerRepository.save(customer);
    }

    /**
     * Find customer by ID.
     */
    public Customer getCustomerById(String customerId) {

        if (customerId == null || customerId.isBlank()) {
            return null;
        }

        return customerRepository.findById(customerId);
    }

    /**
     * Get order history of a customer.
     */
    public List<Order> getOrderHistory(String customerId) {

        if (customerId == null || customerId.isBlank()) {
            return List.of();
        }

        return orderRepository.findByCustomerId(customerId);
    }
}