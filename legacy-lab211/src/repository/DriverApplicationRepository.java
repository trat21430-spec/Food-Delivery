package repository;

import model.DriverApplication;
import java.util.ArrayList;
import java.util.List;

public class DriverApplicationRepository extends CsvRepository<DriverApplication> {

    public DriverApplicationRepository() {
        super("data/driver_applications.csv", "id,username,password,fullName,phone,licensePlate,vehicleType,status,note,createdAt,updatedAt,version");
    }

    public DriverApplicationRepository(String filePath) {
        super(filePath, "id,username,password,fullName,phone,licensePlate,vehicleType,status,note,createdAt,updatedAt,version");
    }

    @Override
    public DriverApplication createEntity() {
        return new DriverApplication();
    }

    public List<DriverApplication> findPendingApplications() {
        List<DriverApplication> all = this.findAll();
        List<DriverApplication> pending = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            DriverApplication app = all.get(i);
            if ("PENDING".equalsIgnoreCase(app.getStatus())) {
                pending.add(app);
            }
        }
        return pending;
    }

    public DriverApplication findByUsername(String username) {
        if (username == null) return null;
        List<DriverApplication> all = this.findAll();
        for (int i = 0; i < all.size(); i++) {
            DriverApplication app = all.get(i);
            if (username.equalsIgnoreCase(app.getUsername())) {
                return app;
            }
        }
        return null;
    }

    public DriverApplication findByPhone(String phone) {
        if (phone == null) return null;
        List<DriverApplication> all = this.findAll();
        for (int i = 0; i < all.size(); i++) {
            DriverApplication app = all.get(i);
            if (phone.equals(app.getPhone())) {
                return app;
            }
        }
        return null;
    }
}
