package controller;

import model.Customer;
import model.Order;
import repository.CustomerRepository;
import repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class CustomerController {
    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;

    public CustomerController() {
        this.customerRepository = new CustomerRepository();
        this.orderRepository = new OrderRepository();
    }

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = new OrderRepository();
    }

    public CustomerController(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public Customer registerCustomer(String name, String phone, String address, double lat, double lon) {
        List<Customer> all = this.customerRepository.findAll();
        String id = String.format("C%04d", all.size() + 1);
        Customer customer = new Customer(id, name, phone, address, lat, lon);
        this.customerRepository.save(customer);
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return this.customerRepository.findAll();
    }

    public Customer getCustomerById(String id) {
        return this.customerRepository.findById(id);
    }

    public Customer getCustomerByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        List<Customer> all = this.customerRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            Customer c = all.get(i);
            if (phone.trim().equals(c.getPhone().trim())) {
                return c;
            }
        }
        return null;
    }

    public boolean updateCustomer(String id, String name, String phone, String address, double lat, double lon) {
        Customer c = this.customerRepository.findById(id);
        if (c != null) {
            c.setFullName(name);
            c.setPhone(phone);
            c.setWard(address);
            c.setLatitude(lat);
            c.setLongitude(lon);
            c.incrementVersion();
            this.customerRepository.save(c);
            return true;
        }
        return false;
    }

    public boolean deleteCustomer(String id) {
        Customer c = this.customerRepository.findById(id);
        if (c != null) {
            this.customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Order> getOrderHistory(String customerId) {
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
}
