package model;

import java.time.LocalDateTime;

public class User extends BaseEntity {
    private String username;
    private String password;
    private UserRole role;
    private String linkedEntityId;
    private String displayName;
    private String phone;

    public User() {
        super();
        this.username = "";
        this.password = "";
        this.role = UserRole.CUSTOMER;
        this.linkedEntityId = "";
        this.displayName = "";
        this.phone = "";
    }

    public User(String id, String username, String password, UserRole role,
                String linkedEntityId, String displayName, LocalDateTime createdAt,
                LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.username = username;
        this.password = password;
        this.role = role;
        this.linkedEntityId = linkedEntityId != null ? linkedEntityId : "";
        this.displayName = displayName != null ? displayName : username;
        this.phone = "";
    }

    public User(String id, String username, String password, String phone, String fullName,
                UserRole role, String linkedCustomerId, String linkedDriverId, boolean active) {
        super(id);
        this.username = username;
        this.password = password;
        this.phone = phone != null ? phone : "";
        this.displayName = fullName != null ? fullName : username;
        this.role = role;
        this.linkedEntityId = (role == UserRole.DRIVER) ? (linkedDriverId != null ? linkedDriverId : "")
                                                        : (linkedCustomerId != null ? linkedCustomerId : "");
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return this.role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getLinkedEntityId() {
        return this.linkedEntityId != null ? this.linkedEntityId : "";
    }

    public void setLinkedEntityId(String linkedEntityId) {
        this.linkedEntityId = linkedEntityId != null ? linkedEntityId : "";
    }

    public String getDisplayName() {
        return (this.displayName != null && !this.displayName.isEmpty()) ? this.displayName : this.username;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFullName() {
        return this.getDisplayName();
    }

    public void setFullName(String fullName) {
        this.setDisplayName(fullName);
    }

    public String getPhone() {
        return this.phone != null ? this.phone : "";
    }

    public void setPhone(String phone) {
        this.phone = phone != null ? phone : "";
    }

    public String getLinkedCustomerId() {
        return (this.role != UserRole.DRIVER) ? this.getLinkedEntityId() : "";
    }

    public void setLinkedCustomerId(String cid) {
        if (this.role != UserRole.DRIVER) {
            this.setLinkedEntityId(cid);
        }
    }

    public String getLinkedDriverId() {
        return (this.role == UserRole.DRIVER) ? this.getLinkedEntityId() : "";
    }

    public void setLinkedDriverId(String did) {
        if (this.role == UserRole.DRIVER) {
            this.setLinkedEntityId(did);
        }
    }

    @Override
    public String toCsvLine() {
        String custId = (this.role == UserRole.DRIVER) ? "" : this.getLinkedEntityId();
        String drvId = (this.role == UserRole.DRIVER) ? this.getLinkedEntityId() : "";
        return String.join(",",
                this.getId(),
                this.username,
                this.password,
                this.getPhone(),
                this.getDisplayName(),
                this.role.name(),
                custId,
                drvId,
                "true");
    }

    @Override
    public void fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length >= 9) {
            this.setId(p[0].trim());
            this.username = p[1].trim();
            this.password = p[2].trim();
            this.phone = p[3].trim();
            this.displayName = p[4].trim();
            try {
                this.role = UserRole.valueOf(p[5].trim());
            } catch (Exception e) {
                this.role = UserRole.CUSTOMER;
            }
            String custId = p[6].trim();
            String drvId = p[7].trim();
            if (this.role == UserRole.DRIVER) {
                this.linkedEntityId = drvId;
            } else {
                this.linkedEntityId = custId;
            }
        }
    }
}
