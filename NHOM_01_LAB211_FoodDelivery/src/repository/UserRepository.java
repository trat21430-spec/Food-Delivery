package repository;

import model.User;
import java.util.List;

public class UserRepository extends CsvRepository<User> {

    public UserRepository() {
        super("data/users.csv", "id,username,password,phone,fullName,role,linkedCustomerId,linkedDriverId,active");
    }

    public UserRepository(String filePath) {
        super(filePath, "id,username,password,phone,fullName,role,linkedCustomerId,linkedDriverId,active");
    }

    @Override
    public User createEntity() {
        return new User();
    }

    public User findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        List<User> all = this.findAll();
        for (int i = 0; i < all.size(); i++) {
            User u = all.get(i);
            if (username.trim().equalsIgnoreCase(u.getUsername().trim())) {
                return u;
            }
        }
        return null;
    }

    public User findByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        List<User> all = this.findAll();
        for (int i = 0; i < all.size(); i++) {
            User u = all.get(i);
            if (phone.trim().equals(u.getPhone().trim())) {
                return u;
            }
        }
        return null;
    }
}
