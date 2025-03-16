package Repository;

import Model.User;
import Support.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing user data in the database.
 * Note: This repository is deprecated as the application now uses staff for authentication.
 * It is kept for backward compatibility.
 */
@Deprecated
public class UserRepository {
    private static DB db;

    public UserRepository() {
        db = DB.getInstance();
    }

    /**
     * Get a user by ID.
     * 
     * @param userId The ID of the user
     * @return An Optional containing the user if found, or empty if not found
     * @throws SQLException If a database error occurs
     */
    public Optional<User> getUserById(int userId) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, username, password, role FROM users WHERE id = ?"
            );
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
            ));
        });
    }

    /**
     * Get a user by username.
     * 
     * @param username The username of the user
     * @return An Optional containing the user if found, or empty if not found
     * @throws SQLException If a database error occurs
     */
    public Optional<User> getUserByUsername(String username) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, username, password, role FROM users WHERE username = ?"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
            ));
        });
    }

    /**
     * Get all users.
     * 
     * @return A list of all users
     * @throws SQLException If a database error occurs
     */
    public List<User> getAllUsers() throws SQLException {
        return db.execute(connection -> {
            List<User> userList = new ArrayList<>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, username, password, role FROM users");

            while (rs.next()) {
                userList.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }

            return userList;
        });
    }

    /**
     * Create a new user.
     * 
     * @param username The username of the user
     * @param password The password of the user
     * @param role The role of the user
     * @return The created user with its ID
     * @throws SQLException If a database error occurs
     */
    public User createUser(String username, String password, String role) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (username, password, role) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return new User(
                        generatedKeys.getInt(1),
                        username,
                        password,
                        role
                );
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        });
    }

    /**
     * Update a user.
     * 
     * @param user The user to update
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updateUser(User user) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE users SET username = ?, role = ? WHERE id = ?"
            );
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getRole());
            stmt.setInt(3, user.getId());

            return stmt.executeUpdate() > 0;
        });
    }

    /**
     * Update a user with a new password.
     * 
     * @param user The user to update
     * @param newPassword The new password
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updateUserWithPassword(User user, String newPassword) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?"
            );
            stmt.setString(1, user.getUsername());
            stmt.setString(2, newPassword);
            stmt.setString(3, user.getRole());
            stmt.setInt(4, user.getId());

            return stmt.executeUpdate() > 0;
        });
    }

    /**
     * Delete a user.
     * 
     * @param userId The ID of the user to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean deleteUser(int userId) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        });
    }

    /**
     * Check if a user with the given username exists.
     * 
     * @param username The username to check
     * @return true if a user with the username exists, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean checkUserExists(String username) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM users WHERE username = ?"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        });
    }

    /**
     * Count the number of users.
     * 
     * @return The number of users
     * @throws SQLException If a database error occurs
     */
    public int countUsers() throws SQLException {
        return db.execute(connection -> {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            rs.next();
            return rs.getInt(1);
        });
    }
}