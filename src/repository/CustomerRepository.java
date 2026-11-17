package repository;

import model.Customer;

public class CustomerRepository extends CsvRepository<Customer> {

    public CustomerRepository() {
        super("data/customers.csv", "id,fullName,phone,ward,latitude,longitude");
    }

    public CustomerRepository(String filePath) {
        super(filePath, "id,fullName,phone,ward,latitude,longitude");
    }

    @Override
    public Customer createEntity() {
        return new Customer();
    }
}
