package repository;
import model.Customer;
public class CustomerRepository extends CsvRepository<Customer> {
    private static final String FILE_PATH = "data/customers.csv";
    private static final String HEADER = "id,name,phone,address,latitude,longitude,createdAt,updatedAt,version";
    public CustomerRepository() {
        super(FILE_PATH, HEADER);
    }
    public CustomerRepository(String customFilePath) {
        super(customFilePath, HEADER);
    }
    @Override
    protected Customer createEntity() {
        return new Customer();
    }
}