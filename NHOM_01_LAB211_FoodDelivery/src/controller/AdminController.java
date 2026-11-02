package controller;

import model.Restaurant;
import model.MenuItem;
import model.Customer;
import model.Driver;
import model.DriverStatus;
import model.DriverApplication;
import model.User;
import model.UserRole;
import model.LockMechanism;
import repository.RestaurantRepository;
import repository.MenuItemRepository;
import repository.CustomerRepository;
import repository.DriverRepository;
import repository.DriverApplicationRepository;
import repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller handling Admin capabilities strictly per LAB211 requirements.
 * Coordinates user management, restaurant CRUD, driver application approval, and concurrency simulator.
 */
public class AdminController {
    private RestaurantRepository restaurantRepo;
    private MenuItemRepository menuItemRepo;
    private CustomerRepository customerRepo;
    private DriverRepository driverRepo;
    private DriverApplicationRepository driverAppRepo;
    private UserRepository userRepo;
    private OrderController orderController;

    public AdminController() {
        this.restaurantRepo = new RestaurantRepository();
        this.menuItemRepo = new MenuItemRepository();
        this.customerRepo = new CustomerRepository();
        this.driverRepo = new DriverRepository();
        this.driverAppRepo = new DriverApplicationRepository();
        this.userRepo = new UserRepository();
        this.orderController = new OrderController();
    }

    public AdminController(RestaurantRepository restaurantRepo, MenuItemRepository menuItemRepo,
                           CustomerRepository customerRepo, DriverRepository driverRepo,
                           DriverApplicationRepository driverAppRepo, UserRepository userRepo,
                           OrderController orderController) {
        this.restaurantRepo = restaurantRepo;
        this.menuItemRepo = menuItemRepo;
        this.customerRepo = customerRepo;
        this.driverRepo = driverRepo;
        this.driverAppRepo = driverAppRepo;
        this.userRepo = userRepo;
        this.orderController = orderController;
    }

    // Driver Application Management (Section 3.4 & 4.4)
    public List<DriverApplication> getPendingDriverApplications() {
        return this.driverAppRepo.findPendingApplications();
    }

    public List<DriverApplication> getAllDriverApplications() {
        return this.driverAppRepo.findAll();
    }

    public User approveDriverApplication(String applicationId) {
        DriverApplication app = this.driverAppRepo.findById(applicationId);
        if (app == null || !"PENDING".equalsIgnoreCase(app.getStatus())) {
            return null;
        }

        // 1. Create Driver in drivers.csv with initial status OFFLINE (Section 4.4)
        List<Driver> allDrivers = this.driverRepo.findAll();
        String driverId = String.format("D%04d", allDrivers.size() + 1);
        Driver driver = new Driver(driverId, app.getFullName(), app.getPhone(),
                app.getLicensePlate(), 13.775, 109.225, DriverStatus.OFFLINE, 0, 1L);
        this.driverRepo.save(driver);

        // 2. Create User account with role DRIVER
        List<User> allUsers = this.userRepo.findAll();
        String userId = "U_DRV_" + (allUsers.size() + 1);
        User user = new User(userId, app.getUsername(), app.getPassword(),
                UserRole.DRIVER, driverId, app.getFullName(), LocalDateTime.now(), LocalDateTime.now(), 1L);
        user.setPhone(app.getPhone());
        this.userRepo.save(user);

        // 3. Update application status to APPROVED
        app.setStatus("APPROVED");
        app.setNote("Approved by Admin at " + LocalDateTime.now().toString());
        app.incrementVersion();
        this.driverAppRepo.save(app);

        return user;
    }

    public boolean rejectDriverApplication(String applicationId, String reason) {
        DriverApplication app = this.driverAppRepo.findById(applicationId);
        if (app == null || !"PENDING".equalsIgnoreCase(app.getStatus())) {
            return false;
        }
        app.setStatus("REJECTED");
        app.setNote(reason != null ? reason : "Rejected by Admin");
        app.incrementVersion();
        this.driverAppRepo.save(app);
        return true;
    }

    // Restaurant Management (Section 3.4)
    public Restaurant addRestaurant(String name, String category, String address, double lat, double lon) {
        List<Restaurant> all = this.restaurantRepo.findAll();
        String newId = String.format("R%03d", all.size() + 1);
        Restaurant r = new Restaurant(newId, name, category, address, lat, lon, 4.9, true);
        all.add(r);
        this.restaurantRepo.saveAll(all);
        return r;
    }

    public void restockMenuItem(String menuItemId, int additionalQty) {
        List<MenuItem> items = this.menuItemRepo.findAll();
        for (int i = 0; i < items.size(); i++) {
            MenuItem m = items.get(i);
            if (m.getId().equals(menuItemId)) {
                m.setStockQty(m.getStockQty() + additionalQty);
                m.incrementVersion();
                this.menuItemRepo.saveAll(items);
                return;
            }
        }
    }

    public int triggerAutoDispatchAllPending(LockMechanism mechanism) {
        return this.orderController.autoDispatchPendingOrders(mechanism);
    }
}
