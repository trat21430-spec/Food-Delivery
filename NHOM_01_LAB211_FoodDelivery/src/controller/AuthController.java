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
        String normalizedPhone = this.validateUniquePhone(phone, true);
        Customer customer = this.customerController.registerCustomer(name, normalizedPhone, address, 21.03, 105.84);
        this.accountRepository.register(AccountRole.CUSTOMER, customer.getId(), password);
        return new AuthSession(AccountRole.CUSTOMER, customer.getId(), customer.getName());
    }

    public AuthSession registerDriver(String name, String phone, char[] password) {
        this.validatePassword(password);
        String normalizedPhone = this.validateUniquePhone(phone, false);
        Driver driver = this.driverController.createDriver(name, normalizedPhone, 21.03, 105.84);
        this.accountRepository.register(AccountRole.DRIVER, driver.getId(), password);
        return new AuthSession(AccountRole.DRIVER, driver.getId(), driver.getName());
    }

    public AuthSession login(AccountRole role, String phone, char[] password) {
        String normalizedPhone;
        try {
            normalizedPhone = this.normalizePhone(phone);
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (role == AccountRole.CUSTOMER) {
            for (Customer customer : this.customerController.getAllCustomers()) {
                if (this.normalizeStoredPhone(customer.getPhone()).equals(normalizedPhone)
                        && this.accountRepository.authenticate(role, customer.getId(), password)) {
                    return new AuthSession(role, customer.getId(), customer.getName());
                }
            }
            return null;
        }
        if (role == AccountRole.DRIVER) {
            for (Driver driver : this.driverController.getAllDrivers()) {
                if (this.normalizeStoredPhone(driver.getPhone()).equals(normalizedPhone)
                        && this.accountRepository.authenticate(role, driver.getId(), password)) {
                    return new AuthSession(role, driver.getId(), driver.getName());
                }
            }
        }
        return null;
    }

    private String validateUniquePhone(String phone, boolean customerRole) {
        String normalizedPhone = this.normalizePhone(phone);
        if (customerRole) {
            for (Customer customer : this.customerController.getAllCustomers()) {
                if (normalizedPhone.equals(this.normalizeStoredPhone(customer.getPhone()))
                        && this.accountRepository.hasAccount(AccountRole.CUSTOMER, customer.getId())) {
                    throw new IllegalArgumentException("This phone number is already registered.");
                }
            }
        } else {
            for (Driver driver : this.driverController.getAllDrivers()) {
                if (normalizedPhone.equals(this.normalizeStoredPhone(driver.getPhone()))
                        && this.accountRepository.hasAccount(AccountRole.DRIVER, driver.getId())) {
                    throw new IllegalArgumentException("This phone number is already registered.");
                }
            }
        }
        return normalizedPhone;
    }

    private String normalizePhone(String phone) {
        if (phone == null) {
            throw new IllegalArgumentException("Phone number is required.");
        }
        String normalizedPhone = phone.trim().replaceAll("[\\s()-]", "");
        if (normalizedPhone.matches("0[0-9]{9}")
                || normalizedPhone.matches("\\+[1-9][0-9]{7,14}")) {
            return normalizedPhone;
        }
        throw new IllegalArgumentException(
                "Enter a 10-digit Vietnamese number starting with 0 or an international number with country code, such as +14155552671.");
    }

    private String normalizeStoredPhone(String phone) {
        try {
            return this.normalizePhone(phone);
        } catch (IllegalArgumentException e) {
            return phone == null ? "" : phone.trim();
        }
    }

    private void validatePassword(char[] password) {
        if (password == null || password.length < 8) {
            throw new IllegalArgumentException("Password must contain at least 8 characters.");
        }
    }
}
