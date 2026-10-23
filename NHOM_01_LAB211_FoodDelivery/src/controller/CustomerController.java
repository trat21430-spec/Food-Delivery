package controller;

import model.Customer;
import repository.CustomerRepository;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerController {
    private CustomerRepository customerRepository;

    public CustomerController() {
        this.customerRepository = new CustomerRepository();
    }

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer registerCustomer(String name, String phone, String address, double lat, double lon) {
        String id = "C" + System.currentTimeMillis();
        Customer customer = new Customer(id, name, phone, address, lat, lon, LocalDateTime.now(), LocalDateTime.now(), 1L);
        this.customerRepository.save(customer);
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return this.customerRepository.findAll();
    }

    public Customer getCustomerById(String id) {
        return this.customerRepository.findById(id);
    }
}
