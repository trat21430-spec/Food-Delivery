package model;

public class UserAccount extends User {

    public UserAccount() {
        super();
    }

    public UserAccount(String id, String username, String password, String phone, String fullName,
                       UserRole role, String linkedCustomerId, String linkedDriverId, boolean active) {
        super(id, username, password, phone, fullName, role, linkedCustomerId, linkedDriverId, active);
    }
}
