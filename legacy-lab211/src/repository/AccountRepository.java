package repository;

import model.AccountRole;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class AccountRepository {
    private String header;
    private int iterations;
    private int keyLength;
    private int saltLength;
    private Path filePath;
    private SecureRandom secureRandom;

    public AccountRepository() {
        this(Paths.get("data", "accounts.csv"));
    }

    public AccountRepository(Path filePath) {
        this.filePath = filePath;
        this.header = "role,userId,salt,passwordHash";
        this.iterations = 120000;
        this.keyLength = 256;
        this.saltLength = 16;
        this.secureRandom = new SecureRandom();
        this.ensureFileExists();
    }

    public synchronized void register(AccountRole role, String userId, char[] password) {
        if (role == null || userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account role and user ID are required.");
        }
        if (password == null || password.length < 8) {
            throw new IllegalArgumentException("Password must contain at least 8 characters.");
        }
        if (this.findAccount(role, userId) != null) {
            throw new IllegalArgumentException("An account already exists for " + userId + ".");
        }

        byte[] salt = new byte[this.saltLength];
        this.secureRandom.nextBytes(salt);
        byte[] hash = this.hashPassword(password, salt);
        String row = role.name() + "," + userId + ","
                + Base64.getEncoder().encodeToString(salt) + ","
                + Base64.getEncoder().encodeToString(hash);

        try (BufferedWriter writer = Files.newBufferedWriter(this.filePath, StandardCharsets.UTF_8,
                java.nio.file.StandardOpenOption.APPEND)) {
            writer.newLine();
            writer.write(row);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save account credentials.", e);
        }
    }

    public synchronized boolean authenticate(AccountRole role, String userId, char[] password) {
        String[] account = this.findAccount(role, userId);
        if (account == null || password == null) {
            return false;
        }

        try {
            byte[] salt = Base64.getDecoder().decode(account[2]);
            byte[] storedHash = Base64.getDecoder().decode(account[3]);
            byte[] actualHash = this.hashPassword(password, salt);
            return MessageDigest.isEqual(storedHash, actualHash);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Account credentials file contains invalid data.", e);
        }
    }

    public synchronized boolean hasAccount(AccountRole role, String userId) {
        return this.findAccount(role, userId) != null;
    }

    private String[] findAccount(AccountRole role, String userId) {
        if (role == null || userId == null) {
            return null;
        }
        try (BufferedReader reader = Files.newBufferedReader(this.filePath, StandardCharsets.UTF_8)) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length == 4 && role.name().equals(parts[0]) && userId.equals(parts[1])) {
                    return parts;
                }
            }
            return null;
        } catch (IOException e) {
            throw new IllegalStateException("Could not read account credentials.", e);
        }
    }

    private byte[] hashPassword(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, this.iterations, this.keyLength);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Password hashing is unavailable.", e);
        } finally {
            spec.clearPassword();
        }
    }

    private void ensureFileExists() {
        try {
            Path parent = this.filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(this.filePath)) {
                try (BufferedWriter writer = Files.newBufferedWriter(this.filePath, StandardCharsets.UTF_8)) {
                    writer.write(this.header);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not initialize account credentials file.", e);
        }
    }
}
