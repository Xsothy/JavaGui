package Repository;

import Model.User;
import Support.DB;
import Support.Response;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private static DB db;

    public UserRepository() {
        db = DB.getInstance();
    }

    public Response<Optional<User>> getUserByUsername(String username) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, username, password, email, role FROM users WHERE username = ?"
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
                    rs.getString("email"),
                    rs.getString("role")
            ));
        });
    }

    public Response<Optional<User>> getUserById(int userId) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, username, password, email, role FROM users WHERE id = ?"
            );
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(
                    new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("role")
                    )
            );
        });
    }

    public Response<List<User>> getAllUsers() {
        return db.execute(connection -> {
            List<User> users = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, username, password, email, role FROM users"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role")
                );
                users.add(user);
            }

            return users;
        });
    }

    public Response<User> createUser(String username, String password) {
        return this.createUser(username, password, "", "user");
    }

    public Response<User> createUser(String username, String password, String email) {
        return this.createUser(username, password, email, "user");
    }

    public Response<User> createUser(String username, String password, String email, String role) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, username);
            stmt.setString(2, User.hashPassword(password));
            stmt.setString(3, email);
            stmt.setString(4, role);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new User(
                            generatedKeys.getInt(1),
                            username,
                            User.hashPassword(password),
                            email,
                            role
                    );
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        });
    }

    public Response<Boolean> updateUser(User user) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE users SET username = ?, email = ?, role = ? WHERE id = ?"
            );
            stmt.setString(1, user.username);
            stmt.setString(2, user.email);
            stmt.setString(3, user.role);
            stmt.setInt(4, user.id);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        });
    }

    public Response<Boolean> updateUserWithPassword(User user, String newPassword) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE users SET username = ?, password = ?, email = ?, role = ? WHERE id = ?"
            );
            stmt.setString(1, user.username);
            stmt.setString(2, User.hashPassword(newPassword));
            stmt.setString(3, user.email);
            stmt.setString(4, user.role);
            stmt.setInt(5, user.id);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        });
    }

    public Response<Boolean> deleteUser(int userId) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM users WHERE id = ?"
            );
            stmt.setInt(1, userId);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        });
    }

    public Response<Boolean> deleteUserByUsername(String username) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM users WHERE username = ?"
            );
            stmt.setString(1, username);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        });
    }

    public Response<List<User>> getUsersByRole(String role) {
        return db.execute(connection -> {
            List<User> users = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, username, password, email, role FROM users WHERE role = ?"
            );
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role")
                );
                users.add(user);
            }

            return users;
        });
    }

    public Response<Boolean> checkUserExists(String username) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM users WHERE username = ?"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        });
    }

    public Response<Integer> countUsers() {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM users"
            );
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        });
    }
}