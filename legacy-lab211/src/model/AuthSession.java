package model;

public class AuthSession {
    private AccountRole role;
    private String userId;
    private String displayName;

    public AuthSession(AccountRole role, String userId, String displayName) {
        this.role = role;
        this.userId = userId;
        this.displayName = displayName;
    }

    public AccountRole getRole() {
        return this.role;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
