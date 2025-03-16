package Controller;

import Model.User;
import Repository.UserRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing user operations.
 * @deprecated This class is deprecated in favor of StaffController. It is kept for backward compatibility.
 */
@Deprecated
public class UserController {
    private static final UserRepository userRepository = new UserRepository();
    
    /**
     * Validates user login credentials.
     * 
     * @param username The username
     * @param password The password
     * @return An Optional containing the user if authentication is successful, or empty if not
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     */
    public static Optional<User> login(String username, String password) throws IllegalArgumentException, SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        Optional<User> userOpt = userRepository.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            return Optional.empty(); // User not found
        }
        
        User user = userOpt.get();
        if (!user.checkPassword(password)) {
            return Optional.empty(); // Invalid password
        }
        
        return Optional.of(user);
    }
    
    /**
     * Registers a new user.
     * 
     * @param username The username
     * @param password The password
     * @param role The user role
     * @return The created user
     * @throws IllegalArgumentException If the input is invalid or the username already exists
     * @throws SQLException If a database error occurs
     */
    public static User register(String username, String password, String role) 
            throws IllegalArgumentException, SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
        
        // Check if username already exists
        boolean exists = userRepository.checkUserExists(username);
        if (exists) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        return userRepository.createUser(username, password, role);
    }
    
    /**
     * Updates a user's profile.
     * 
     * @param id The user ID
     * @param username The new username
     * @param role The new role
     * @return true if the update was successful, false otherwise
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     */
    public static boolean updateProfile(int id, String username, String role) 
            throws IllegalArgumentException, SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
        
        // Check if user exists
        Optional<User> userOpt = userRepository.getUserById(id);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        User user = userOpt.get();
        user.setUsername(username);
        user.setRole(role);
        
        return userRepository.updateUser(user);
    }
    
    /**
     * Gets a user by ID.
     * 
     * @param id The user ID
     * @return An Optional containing the user if found, or empty if not found
     * @throws SQLException If a database error occurs
     */
    public Optional<User> getUserById(int id) throws SQLException {
        if (id <= 0) {
            return Optional.empty();
        }
        return userRepository.getUserById(id);
    }
    
    /**
     * Gets all users.
     * 
     * @return A list of all users
     * @throws SQLException If a database error occurs
     */
    public List<User> getAllUsers() throws SQLException {
        return userRepository.getAllUsers();
    }
}
