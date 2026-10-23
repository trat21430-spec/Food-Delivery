package repository;

import model.Customer;

public class CustomerRepository extends CsvRepository<Customer> {

    public CustomerRepository() {
        super("data/customers.csv", "id,name,phone,address,latitude,longitude,createdAt,updatedAt,version");
    }

    public CustomerRepository(String customFilePath) {
        super(customFilePath, "id,name,phone,address,latitude,longitude,createdAt,updatedAt,version");
    }

    @Override
    public Customer createEntity() {
        return new Customer();
    }
}
