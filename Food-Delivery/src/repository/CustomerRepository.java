package repository;

import model.Customer;

/**
 * Repository for Customer entities backed by customers.csv.
 */
public class CustomerRepository extends CsvRepository<Customer> {

    @Override
    protected String getCsvFilePath() {
        return "data/customers.csv";
    }

    @Override
    protected String getCsvHeader() {
        return "id,name,phone,address,latitude,longitude,createdAt,updatedAt,version";
    }

    @Override
    protected Customer createEmpty() {
        return new Customer();
    }
}
