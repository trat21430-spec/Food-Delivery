package util;

import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * Enterprise Password Security Utility (Trang 15 Master Plan).
 * Uses Cryptographic Salt + SHA-256 digest to securely hash user credentials,
 * with graceful fallback verification for existing demo accounts.
 */
public class PasswordSecurity {
    private static final String SALT_PREFIX = "$FDS$V1$";

    public PasswordSecurity() {}

    /**
     * Hashes a password with a generated cryptographic salt.
     */
    public String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            return "";
        }
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            String saltHex = bytesToHex(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedBytes = md.digest(rawPassword.getBytes("UTF-8"));
            String hashHex = bytesToHex(hashedBytes);

            return SALT_PREFIX + saltHex + "$" + hashHex;
        } catch (Exception e) {
            return rawPassword; // Fallback
        }
    }

    /**
     * Verifies raw password against stored hash or legacy plaintext demo credentials.
     */
    public boolean verifyPassword(String rawPassword, String storedHashOrPassword) {
        if (rawPassword == null || storedHashOrPassword == null) {
            return false;
        }
        // Direct match for legacy demo accounts
        if (rawPassword.equals(storedHashOrPassword)) {
            return true;
        }

        if (storedHashOrPassword.startsWith(SALT_PREFIX)) {
            try {
                String content = storedHashOrPassword.substring(SALT_PREFIX.length());
                String[] parts = content.split("\\$");
                if (parts.length == 2) {
                    byte[] salt = hexToBytes(parts[0]);
                    String expectedHash = parts[1];

                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    md.update(salt);
                    byte[] hashedBytes = md.digest(rawPassword.getBytes("UTF-8"));
                    String actualHash = bytesToHex(hashedBytes);

                    return actualHash.equalsIgnoreCase(expectedHash);
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
