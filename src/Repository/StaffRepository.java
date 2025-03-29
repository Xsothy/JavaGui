package Repository;

import Model.ExpenseWithStaff;
import Model.Staff;
import Model.StaffWithExpenses;
import Support.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffRepository {

    public StaffRepository() {
    }

    public Optional<Staff> getStaffById(int staffId) {
        return DB.unsafeExecute(
                connection -> {
                    PreparedStatement stmt =
                            connection.prepareStatement(
                                    "SELECT id, name, position, username, password, role FROM staff WHERE id = ?");
                    stmt.setInt(1, staffId);
                    ResultSet rs = stmt.executeQuery();

                    if (!rs.next()) {
                        return Optional.empty();
                    }

                    return Optional.of(
                            new Staff(
                                    rs.getInt("id"),
                                    rs.getString("name"),
                                    rs.getString("position"),
                                    rs.getString("username"),
                                    rs.getString("password"),
                                    rs.getString("role")));
                });
    }

    public Optional<Staff> getStaffByUserName(String userName) {
        return DB.unsafeExecute(
                connection -> {
                    PreparedStatement stmt =
                            connection.prepareStatement(
                                    "SELECT id, name, position, username, password, role FROM staff WHERE username = ?");
                    stmt.setString(1, userName);
                    ResultSet rs = stmt.executeQuery();

                    if (!rs.next()) {
                        return Optional.empty();
                    }

                    return Optional.of(
                            new Staff(
                                    rs.getInt("id"),
                                    rs.getString("name"),
                                    rs.getString("position"),
                                    rs.getString("username"),
                                    rs.getString("password"),
                                    rs.getString("role")));
                });
    }

    public List<Staff> getAllStaff() {
        return DB.unsafeExecute(
                connection -> {
                    List<Staff> staffList = new ArrayList<>();
                    Statement stmt = connection.createStatement();
                    ResultSet rs =
                            stmt.executeQuery("SELECT id, name, position, username, password, role FROM staff");

                    while (rs.next()) {
                        staffList.add(
                                new Staff(
                                        rs.getInt("id"),
                                        rs.getString("name"),
                                        rs.getString("position"),
                                        rs.getString("username"),
                                        rs.getString("password"),
                                        rs.getString("role")));
                    }

                    return staffList;
                });
    }

    public Staff createStaff(
            String name, String position, String userName, String password, String role) {
        return DB.unsafeExecute(
                connection -> {
                    PreparedStatement stmt =
                            connection.prepareStatement(
                                    "INSERT INTO staff (name, position, username, password, role) VALUES (?, ?, ?, ?, ?)",
                                    Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, name);
                    stmt.setString(2, position);
                    stmt.setString(3, userName);
                    stmt.setString(4, password);
                    stmt.setString(5, role);

                    int affectedRows = stmt.executeUpdate();

                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return new Staff(
                                generatedKeys.getInt(1), name, position, userName, password, role);
                    } else {
                        return null;
                    }
                });
    }

    public boolean updateStaff(Staff staff) {
        return DB.unsafeExecute(
                connection -> {
                    PreparedStatement stmt =
                            connection.prepareStatement(
                                    "UPDATE staff SET name = ?, position = ?, username = ?, role = ? WHERE id = ?");
                    stmt.setString(1, staff.getName());
                    stmt.setString(2, staff.getPosition());
                    stmt.setString(3, staff.getUserName());
                    stmt.setString(4, staff.getRole());
                    stmt.setInt(5, staff.getId());

                    return stmt.executeUpdate() > 0;
                });
    }

    public boolean updateStaffWithPassword(Staff staff, String newPassword) {
        return DB.unsafeExecute(
                connection -> {
                    PreparedStatement stmt =
                            connection.prepareStatement(
                                    "UPDATE staff SET name = ?, position = ?, username = ?, password = ?, role = ? WHERE id = ?");
                    stmt.setString(1, staff.getName());
                    stmt.setString(2, staff.getPosition());
                    stmt.setString(3, staff.getUserName());
                    stmt.setString(4, newPassword);
                    stmt.setString(5, staff.getRole());
                    stmt.setInt(6, staff.getId());

                    return stmt.executeUpdate() > 0;
                });
    }

    public boolean deleteStaff(int staffId) {
        return DB.unsafeExecute(
                connection -> {
                    PreparedStatement stmt =
                            connection.prepareStatement("DELETE FROM staff WHERE id = ?");
                    stmt.setInt(1, staffId);
                    return stmt.executeUpdate() > 0;
                });
    }

    public List<Staff> getStaffByRole(String role) {
        return DB.unsafeExecute(
                connection -> {
                    List<Staff> staffList = new ArrayList<>();
                    PreparedStatement stmt =
                            connection.prepareStatement(
                                    "SELECT id, name, position, username, password, role FROM staff WHERE role = ?");
                    stmt.setString(1, role);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        staffList.add(
                                new Staff(
                                        rs.getInt("id"),
                                        rs.getString("name"),
                                        rs.getString("position"),
                                        rs.getString("username"),
                                        rs.getString("password"),
                                        rs.getString("role")));
                    }

                    return staffList;
                });
    }

    public boolean checkStaffExists(String userName) {
        return DB.unsafeExecute(
                connection -> {
                    PreparedStatement stmt =
                            connection.prepareStatement("SELECT COUNT(*) FROM staff WHERE username = ?");
                    stmt.setString(1, userName);
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    return rs.getInt(1) > 0;
                });
    }

    public int countStaff() {
        return DB.unsafeExecute(
                connection -> {
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM staff");

                    if (rs.next()) {
                        return rs.getInt("count");
                    }

                    return 0;
                });
    }

    public Optional<StaffWithExpenses> getStaffWithExpensesById(int staffId) {
        return DB.unsafeExecute(
                connection -> {
                    PreparedStatement staffStmt =
                            connection.prepareStatement(
                                    "SELECT id, name, position, username, password, role FROM staff WHERE id = ?");
                    staffStmt.setInt(1, staffId);
                    ResultSet staffRs = staffStmt.executeQuery();

                    if (!staffRs.next()) {
                        return Optional.empty();
                    }

                    Staff staff =
                            new Staff(
                                    staffRs.getInt("id"),
                                    staffRs.getString("name"),
                                    staffRs.getString("position"),
                                    staffRs.getString("username"),
                                    staffRs.getString("password"),
                                    staffRs.getString("role"));

                    PreparedStatement expensesStmt =
                            connection.prepareStatement(
                                    "SELECT id, date, name, description, amount, picture, staff_id FROM expenses WHERE staff_id = ? ORDER BY id DESC");
                    expensesStmt.setInt(1, staffId);
                    ResultSet expensesRs = expensesStmt.executeQuery();

                    List<ExpenseWithStaff> expenses = new ArrayList<>();

                    while (expensesRs.next()) {
                        Model.Expense expense =
                                new Model.Expense(
                                        expensesRs.getInt("id"),
                                        expensesRs.getDate("date"),
                                        expensesRs.getString("name"),
                                        expensesRs.getString("description"),
                                        expensesRs.getBigDecimal("amount"),
                                        expensesRs.getString("picture"),
                                        expensesRs.getInt("staff_id"));

                        ExpenseWithStaff expenseWithStaff = new ExpenseWithStaff(expense, staff);

                        expenses.add(expenseWithStaff);
                    }

                    return Optional.of(new StaffWithExpenses(staff, expenses));
                });
    }

    public List<StaffWithExpenses> getAllStaffWithExpenses() {
        return DB.unsafeExecute(
                connection -> {
                    Statement staffStmt = connection.createStatement();
                    ResultSet staffRs =
                            staffStmt.executeQuery(
                                    "SELECT id, date, name, position, username, password, role FROM staff ORDER BY id");

                    List<StaffWithExpenses> staffWithExpensesList = new ArrayList<>();

                    while (staffRs.next()) {
                        Staff staff =
                                new Staff(
                                        staffRs.getInt("id"),
                                        staffRs.getString("name"),
                                        staffRs.getString("position"),
                                        staffRs.getString("username"),
                                        staffRs.getString("password"),
                                        staffRs.getString("role"));
                        PreparedStatement expensesStmt =
                                connection.prepareStatement(
                                        "SELECT id, name, description, amount, picture, staff_id FROM expenses WHERE staff_id = ? ORDER BY id DESC");
                        expensesStmt.setInt(1, staff.getId());
                        ResultSet expensesRs = expensesStmt.executeQuery();

                        List<ExpenseWithStaff> expenses = new ArrayList<>();

                        while (expensesRs.next()) {
                            Model.Expense expense =
                                    new Model.Expense(
                                            expensesRs.getInt("id"),
                                            expensesRs.getDate("date"),
                                            expensesRs.getString("name"),
                                            expensesRs.getString("description"),
                                            expensesRs.getBigDecimal("amount"),
                                            expensesRs.getString("picture"),
                                            expensesRs.getInt("staff_id"));

                            ExpenseWithStaff expenseWithStaff = new ExpenseWithStaff(expense, staff);

                            expenses.add(expenseWithStaff);
                        }

                        staffWithExpensesList.add(new StaffWithExpenses(staff, expenses));
                    }

                    return staffWithExpensesList;
                });
    }
}
