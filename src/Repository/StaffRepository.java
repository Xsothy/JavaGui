package Repository;

import Model.Staff;
import Support.DB;
import Support.Response;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffRepository {
    private static DB db;

    public StaffRepository() {
        db = DB.getInstance();
    }

    public Response<Optional<Staff>> getStaffById(int staffId) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, position, user_name, password, role FROM staff WHERE id = ?"
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
                    rs.getString("user_name"),
                    rs.getString("password"),
                    rs.getString("role")
            ));
        });
    }

    public Response<Optional<Staff>> getStaffByUserName(String userName) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, position, user_name, password, role FROM staff WHERE user_name = ?"
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
                    rs.getString("user_name"),
                    rs.getString("password"),
                    rs.getString("role")
            ));
        });
    }

    public Response<List<Staff>> getAllStaff() {
        return db.execute(connection -> {
            List<Staff> staffList = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, position, user_name, password, role FROM staff"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Staff staff = new Staff(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getString("role")
                );
                staffList.add(staff);
            }

            return staffList;
        });
    }

    public Response<Staff> createStaff(String name, String position, String userName, String password, String role) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO staff (name, position, user_name, password, role) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, name);
            stmt.setString(2, position);
            stmt.setString(3, userName);
            stmt.setString(4, Staff.hashPassword(password));
            stmt.setString(5, role);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("Creating staff failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Staff(
                            generatedKeys.getInt(1),
                            name,
                            position,
                            userName,
                            Staff.hashPassword(password),
                            role
                    );
                } else {
                    throw new SQLException("Creating staff failed, no ID obtained.");
                }
            }
        });
    }

    public Response<Boolean> updateStaff(Staff staff) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE staff SET name = ?, position = ?, user_name = ?, role = ? WHERE id = ?"
            );
            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getPosition());
            stmt.setString(3, staff.getUserName());
            stmt.setString(4, staff.getRole());
            stmt.setInt(5, staff.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        });
    }

    public Response<Boolean> updateStaffWithPassword(Staff staff, String newPassword) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE staff SET name = ?, position = ?, user_name = ?, password = ?, role = ? WHERE id = ?"
            );
            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getPosition());
            stmt.setString(3, staff.getUserName());
            stmt.setString(4, Staff.hashPassword(newPassword));
            stmt.setString(5, staff.getRole());
            stmt.setInt(6, staff.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        });
    }

    public Response<Boolean> deleteStaff(int staffId) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM staff WHERE id = ?"
            );
            stmt.setInt(1, staffId);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        });
    }

    public Response<List<Staff>> getStaffByRole(String role) {
        return db.execute(connection -> {
            List<Staff> staffList = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, position, user_name, password, role FROM staff WHERE role = ?"
            );
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Staff staff = new Staff(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getString("role")
                );
                staffList.add(staff);
            }

            return staffList;
        });
    }

    public Response<Boolean> checkStaffExists(String userName) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM staff WHERE user_name = ?"
            );
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        });
    }

    public Response<Integer> countStaff() {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM staff"
            );
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        });
    }
}