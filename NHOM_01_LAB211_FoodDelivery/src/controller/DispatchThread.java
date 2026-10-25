// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package controller;

import java.time.LocalDateTime;
import java.util.List;
import model.Customer;
import repository.CustomerRepository;

public class CustomerController {
    private CustomerRepository customerRepository;

    public CustomerController() {
        this.customerRepository = new CustomerRepository();
    }

    public CustomerController(CustomerRepository var1) {
        this.customerRepository = var1;
    }

    public Customer registerCustomer(String var1, String var2, String var3, double var4, double var6) {
        String var8 = "C" + System.currentTimeMillis();
        Customer var9 = new Customer(var8, var1, var2, var3, var4, var6, LocalDateTime.now(), LocalDateTime.now(), 1L);
        this.customerRepository.save(var9);
        return var9;
    }

    public List<Customer> getAllCustomers() {
        return this.customerRepository.findAll();
    }

    public Customer getCustomerById(String var1) {
        return (Customer) this.customerRepository.findById(var1);
    }
}
