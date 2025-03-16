package Repository;

import Model.Staff;
import Support.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing staff data in the database.
 */
public class StaffRepository {
    private static DB db;

    public StaffRepository() {
        db = DB.getInstance();
    }

    /**
     * Get a staff member by ID.
     * 
     * @param staffId The ID of the staff member
     * @return An Optional containing the staff member if found, or empty if not found
     * @throws SQLException If a database error occurs
     */
    public Optional<Staff> getStaffById(int staffId) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, position, username, password, role FROM staff WHERE id = ?"
            );
            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new Staff(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("position"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
            ));
        });
    }

    /**
     * Get a staff member by username.
     * 
     * @param userName The username of the staff member
     * @return An Optional containing the staff member if found, or empty if not found
     * @throws SQLException If a database error occurs
     */
    public Optional<Staff> getStaffByUserName(String userName) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, position, username, password, role FROM staff WHERE username = ?"
            );
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new Staff(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("position"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
            ));
        });
    }

    /**
     * Get all staff members.
     * 
     * @return A list of all staff members
     * @throws SQLException If a database error occurs
     */
    public List<Staff> getAllStaff() throws SQLException {
        return db.execute(connection -> {
            List<Staff> staffList = new ArrayList<>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, position, username, password, role FROM staff");

            while (rs.next()) {
                staffList.add(new Staff(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }

            return staffList;
        });
    }

    /**
     * Create a new staff member.
     * 
     * @param name The name of the staff member
     * @param position The position of the staff member
     * @param userName The username of the staff member
     * @param password The password of the staff member
     * @param role The role of the staff member
     * @return The created staff member with its ID
     * @throws SQLException If a database error occurs
     */
    public Staff createStaff(String name, String position, String userName, String password, String role) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO staff (name, position, username, password, role) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, name);
            stmt.setString(2, position);
            stmt.setString(3, userName);
            stmt.setString(4, password);
            stmt.setString(5, role);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating staff failed, no rows affected.");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return new Staff(
                        generatedKeys.getInt(1),
                        name,
                        position,
                        userName,
                        password,
                        role
                );
            } else {
                throw new SQLException("Creating staff failed, no ID obtained.");
            }
        });
    }

    /**
     * Update a staff member.
     * 
     * @param staff The staff member to update
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updateStaff(Staff staff) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE staff SET name = ?, position = ?, username = ?, role = ? WHERE id = ?"
            );
            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getPosition());
            stmt.setString(3, staff.getUserName());
            stmt.setString(4, staff.getRole());
            stmt.setInt(5, staff.getId());

            return stmt.executeUpdate() > 0;
        });
    }

    /**
     * Update a staff member with a new password.
     * 
     * @param staff The staff member to update
     * @param newPassword The new password
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updateStaffWithPassword(Staff staff, String newPassword) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE staff SET name = ?, position = ?, username = ?, password = ?, role = ? WHERE id = ?"
            );
            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getPosition());
            stmt.setString(3, staff.getUserName());
            stmt.setString(4, newPassword);
            stmt.setString(5, staff.getRole());
            stmt.setInt(6, staff.getId());

            return stmt.executeUpdate() > 0;
        });
    }

    /**
     * Delete a staff member.
     * 
     * @param staffId The ID of the staff member to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean deleteStaff(int staffId) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM staff WHERE id = ?");
            stmt.setInt(1, staffId);
            return stmt.executeUpdate() > 0;
        });
    }

    /**
     * Get staff members by role.
     * 
     * @param role The role to filter by
     * @return A list of staff members with the specified role
     * @throws SQLException If a database error occurs
     */
    public List<Staff> getStaffByRole(String role) throws SQLException {
        return db.execute(connection -> {
            List<Staff> staffList = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, position, username, password, role FROM staff WHERE role = ?"
            );
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                staffList.add(new Staff(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }

            return staffList;
        });
    }

    /**
     * Check if a staff member with the given username exists.
     * 
     * @param userName The username to check
     * @return true if a staff member with the username exists, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean checkStaffExists(String userName) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM staff WHERE username = ?"
            );
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        });
    }

    /**
     * Count the number of staff members.
     * 
     * @return The number of staff members
     * @throws SQLException If a database error occurs
     */
    public int countStaff() throws SQLException {
        return db.execute(connection -> {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM staff");
            rs.next();
            return rs.getInt(1);
        });
    }
}