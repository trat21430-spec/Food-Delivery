package controller;

import model.Customer;
import model.Driver;
import model.DriverStatus;
import model.DriverApplication;
import model.User;
import model.UserRole;
import repository.CustomerRepository;
import repository.DriverRepository;
import repository.DriverApplicationRepository;
import repository.UserRepository;
import exception.InvalidInputException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuthController {
    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private DriverRepository driverRepository;
    private DriverApplicationRepository driverAppRepository;
    private User currentUser;

    public AuthController() {
        this.userRepository = new UserRepository();
        this.customerRepository = new CustomerRepository();
        this.driverRepository = new DriverRepository();
        this.driverAppRepository = new DriverApplicationRepository();
        this.currentUser = null;
    }

    public AuthController(UserRepository userRepo, CustomerRepository customerRepo,
                          DriverRepository driverRepo, DriverApplicationRepository driverAppRepo) {
        this.userRepository = userRepo;
        this.customerRepository = customerRepo;
        this.driverRepository = driverRepo;
        this.driverAppRepository = driverAppRepo;
        this.currentUser = null;
    }

    public UserRepository getUserRepository() {
        return this.userRepository;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public User getCurrentSession() {
        return this.currentUser;
    }

    public boolean isLoggedIn() {
        return this.currentUser != null;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            user = this.userRepository.findByPhone(username);
        }

        if (user != null && password.equals(user.getPassword())) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    public User registerCustomer(String username, String password, String name, String phone, String address, double lat, double lon) throws InvalidInputException {
        if (this.userRepository.findByUsername(username) != null) {
            throw new InvalidInputException("Username already exists: " + username);
        }
        if (this.userRepository.findByPhone(phone) != null) {
            throw new InvalidInputException("Phone number already registered: " + phone);
        }

        List<Customer> allCustomers = this.customerRepository.findAll();
        String customerId = String.format("C%04d", allCustomers.size() + 1);
        Customer customer = new Customer(customerId, name, phone, address, lat, lon);
        this.customerRepository.save(customer);

        List<User> allUsers = this.userRepository.findAll();
        String userId = "U_CUST_" + (allUsers.size() + 1);
        User user = new User(userId, username, password, UserRole.CUSTOMER, customerId, name, LocalDateTime.now(), LocalDateTime.now(), 1L);
        user.setPhone(phone);
        this.userRepository.save(user);

        this.currentUser = user;
        return user;
    }

    /**
     * Submits a pending driver application per Section 4.1 & 4.4.
     * Saved to driver_applications.csv with status PENDING for Admin approval.
     */
    public DriverApplication submitDriverApplication(String username, String password, String fullName,
                                                     String phone, String licensePlate, String vehicleType) throws InvalidInputException {
        if (this.userRepository.findByUsername(username) != null) {
            throw new InvalidInputException("Username already exists in system: " + username);
        }
        if (this.driverAppRepository.findByUsername(username) != null) {
            throw new InvalidInputException("An application with this username is already pending: " + username);
        }
        if (this.userRepository.findByPhone(phone) != null) {
            throw new InvalidInputException("Phone number already registered to another account: " + phone);
        }

        List<DriverApplication> all = this.driverAppRepository.findAll();
        String appId = String.format("APP%03d", all.size() + 1);
        DriverApplication app = new DriverApplication(appId, username, password, fullName, phone, licensePlate, vehicleType, "PENDING", "Cho Admin phe duyet");
        this.driverAppRepository.save(app);
        return app;
    }

    public User registerDriver(String username, String password, String name, String phone, double lat, double lon) throws InvalidInputException {
        if (this.userRepository.findByUsername(username) != null) {
            throw new InvalidInputException("Username already exists: " + username);
        }
        if (this.userRepository.findByPhone(phone) != null) {
            throw new InvalidInputException("Phone number already registered: " + phone);
        }

        List<Driver> allDrivers = this.driverRepository.findAll();
        String driverId = String.format("D%04d", allDrivers.size() + 1);
        String plate = "77-L1 " + (100 + (int)(Math.random() * 899)) + "." + (10 + (int)(Math.random() * 89));
        Driver driver = new Driver(driverId, name, phone, plate, lat, lon, DriverStatus.AVAILABLE, 0, 1L);
        this.driverRepository.save(driver);

        List<User> allUsers = this.userRepository.findAll();
        String userId = "U_DRV_" + (allUsers.size() + 1);
        User user = new User(userId, username, password, UserRole.DRIVER, driverId, name, LocalDateTime.now(), LocalDateTime.now(), 1L);
        user.setPhone(phone);
        this.userRepository.save(user);

        this.currentUser = user;
        return user;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public List<User> getUsersByRole(UserRole role) {
        List<User> all = this.userRepository.findAll();
        List<User> filtered = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            User u = all.get(i);
            if (u.getRole() == role) {
                filtered.add(u);
            }
        }
        return filtered;
    }

    public User createUser(String username, String password, UserRole role, String linkedEntityId, String displayName) {
        List<User> all = this.userRepository.findAll();
        String id = "U_" + (all.size() + 1);
        User u = new User(id, username, password, role, linkedEntityId, displayName, LocalDateTime.now(), LocalDateTime.now(), 1L);
        this.userRepository.save(u);
        return u;
    }

    public User getUserById(String id) {
        return this.userRepository.findById(id);
    }

    public boolean updateUser(String id, String newPassword, UserRole newRole, String newDisplayName) {
        User u = this.userRepository.findById(id);
        if (u != null) {
            if (newPassword != null && !newPassword.isEmpty()) {
                u.setPassword(newPassword);
            }
            if (newRole != null) {
                u.setRole(newRole);
            }
            if (newDisplayName != null && !newDisplayName.isEmpty()) {
                u.setDisplayName(newDisplayName);
            }
            u.incrementVersion();
            this.userRepository.save(u);
            return true;
        }
        return false;
    }

    public boolean deleteUser(String id) {
        User u = this.userRepository.findById(id);
        if (u != null) {
            this.userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
