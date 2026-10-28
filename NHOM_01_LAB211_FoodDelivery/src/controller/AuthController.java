package controller;

import model.AccountRole;
import model.AuthSession;
import model.Customer;
import model.Driver;
import repository.AccountRepository;

public class AuthController {
    private AccountRepository accountRepository;
    private CustomerController customerController;
    private DriverController driverController;

    public AuthController() {
        this.accountRepository = new AccountRepository();
        this.customerController = new CustomerController();
        this.driverController = new DriverController();
    }

    public AuthSession registerCustomer(String name, String phone, String address, char[] password) {
        this.validatePassword(password);
        this.validateUniquePhone(phone, true);
        Customer customer = this.customerController.registerCustomer(name, phone, address, 21.03, 105.84);
        this.accountRepository.register(AccountRole.CUSTOMER, customer.getId(), password);
        return new AuthSession(AccountRole.CUSTOMER, customer.getId(), customer.getName());
    }

    public AuthSession registerDriver(String name, String phone, char[] password) {
        this.validatePassword(password);
        this.validateUniquePhone(phone, false);
        Driver driver = this.driverController.createDriver(name, phone, 21.03, 105.84);
        this.accountRepository.register(AccountRole.DRIVER, driver.getId(), password);
        return new AuthSession(AccountRole.DRIVER, driver.getId(), driver.getName());
    }

    public AuthSession login(AccountRole role, String phone, char[] password) {
        if (role == AccountRole.CUSTOMER) {
            for (Customer customer : this.customerController.getAllCustomers()) {
                if (customer.getPhone().equals(phone)
                        && this.accountRepository.authenticate(role, customer.getId(), password)) {
                    return new AuthSession(role, customer.getId(), customer.getName());
                }
            }
            return null;
        }
        if (role == AccountRole.DRIVER) {
            for (Driver driver : this.driverController.getAllDrivers()) {
                if (driver.getPhone().equals(phone)
                        && this.accountRepository.authenticate(role, driver.getId(), password)) {
                    return new AuthSession(role, driver.getId(), driver.getName());
                }
            }
        }
        return null;
    }

    private void validateUniquePhone(String phone, boolean customerRole) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required.");
        }
        if (customerRole) {
            for (Customer customer : this.customerController.getAllCustomers()) {
                if (phone.trim().equals(customer.getPhone())
                        && this.accountRepository.hasAccount(AccountRole.CUSTOMER, customer.getId())) {
                    throw new IllegalArgumentException("This phone number is already registered.");
                }
            }
        } else {
            for (Driver driver : this.driverController.getAllDrivers()) {
                if (phone.trim().equals(driver.getPhone())
                        && this.accountRepository.hasAccount(AccountRole.DRIVER, driver.getId())) {
                    throw new IllegalArgumentException("This phone number is already registered.");
                }
            }
        }
    }

    private void validatePassword(char[] password) {
        if (password == null || password.length < 8) {
            throw new IllegalArgumentException("Password must contain at least 8 characters.");
        }
    }
}
