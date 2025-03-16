package Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * User model class.
 * Note: This class is deprecated as the application now uses Staff for authentication.
 * It is kept for backward compatibility.
 */
@Deprecated
public class User {
    private int id;
    private String username;
    private String password;
    private String role;

    /**
     * Creates a new User with the specified ID, username, password, and role.
     * 
     * @param id The user ID
     * @param username The username
     * @param password The password (should be hashed)
     * @param role The user role
     */
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Checks if the provided password matches the stored password.
     * 
     * @param password The password to check
     * @return true if the password matches, false otherwise
     */
    public boolean checkPassword(String password) {
        return this.password.equals(hashPassword(password));
    }

    /**
     * Hashes a password using MD5.
     * Note: MD5 is not secure for password hashing in production.
     * 
     * @param password The password to hash
     * @return The hashed password
     */
    public static String hashPassword(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(password.getBytes());
        byte[] digest = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    /**
     * Gets the user ID.
     * 
     * @return The user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the username.
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password.
     * 
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user role.
     * 
     * @return The user role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the username of this user.
     * 
     * @param username The new username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Sets the role of this user.
     * 
     * @param role The new role
     */
    public void setRole(String role) {
        this.role = role;
    }
}
