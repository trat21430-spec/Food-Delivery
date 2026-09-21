/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author HP
 */

import model.Customer;

public class CustomerRepository extends CsvRepository<Customer> {

    public CustomerRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected Customer fromCsvLine(String line) {
        String[] parts = line.split(",");
        Customer customer = new Customer();
        customer.setId(parts[0].trim());
        customer.setName(parts[1].trim());
        customer.setPhone(parts[2].trim());
        customer.setAddress(parts[3].trim());
        return customer;
    }

    @Override
    protected String toCsvLine(Customer customer) {
        return String.join(",", customer.getId(), customer.getName(), customer.getPhone(), customer.getAddress());
    }

    @Override
    protected String getHeader() {
        return "id,name,phone,address";
    }
}
