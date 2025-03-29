package Repository;

import Model.Expense;
import Model.ExpenseWithStaff;
import Model.Staff;
import Support.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpenseRepository {

    public ExpenseRepository() {}

    public Optional<Expense> getExpenseById(int expenseId) {
        return DB.unsafeExecute(connection -> {
            var stmt = connection.prepareStatement("SELECT * FROM expenses WHERE id = ?");
            stmt.setInt(1, expenseId);
            var rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(
                    new Expense(
                            rs.getInt("id"),
                            rs.getDate("date"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getBigDecimal("amount"),
                            rs.getString("picture"),
                            rs.getInt("staff_id")));
        });
    }

    public List<Expense> getAllExpenses() {
        return DB.unsafeExecute(connection -> {
            List<Expense> expenseList = new ArrayList<Expense>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM expenses");

            while (rs.next()) {
                expenseList.add(
                        new Expense(
                                rs.getInt("id"),
                                rs.getDate("date"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getBigDecimal("amount"),
                                rs.getString("picture"),
                                rs.getInt("staff_id")));
            }

            return expenseList;
        });
    }

    public boolean addExpense(String name, String description, double amount, int staffId) {
        return DB.unsafeExecute(connection -> {
            PreparedStatement stmt =
                    connection.prepareStatement(
                            "INSERT INTO expenses (name, date, description, amount, staff_id) VALUES (?, ?, ?, ?, ?)");
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            stmt.setString(1, name);
            stmt.setDate(2, date);
            stmt.setString(3, description);
            stmt.setDouble(4, amount);
            stmt.setInt(5, staffId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        });
    }

    public boolean updateExpense(
            int expenseId, String name, String description, double amount, int staffId) {
        return DB.unsafeExecute(connection -> {
            PreparedStatement stmt =
                    connection.prepareStatement(
                            "UPDATE expenses SET name = ?, description = ?, amount = ?, staff_id = ? WHERE id = ?");
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, amount);
            stmt.setInt(4, staffId);
            stmt.setInt(5, expenseId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        });
    }

    public boolean deleteExpense(int expenseId) {
        return DB.unsafeExecute(connection -> {
            PreparedStatement stmt =
                    connection.prepareStatement("DELETE FROM expenses WHERE id = ?");
            stmt.setInt(1, expenseId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        });
    }

    public Optional<ExpenseWithStaff> getExpenseWithStaffById(int expenseId) {
        return DB.unsafeExecute(connection -> {
            PreparedStatement stmt =
                    connection.prepareStatement(
                            "SELECT e.*, s.id as s_id, s.name as staff_name, s.position, s.username, s.password, s.role "
                                    + "FROM expenses e "
                                    + "JOIN staff s ON e.staff_id = s.id "
                                    + "WHERE e.id = ?");
            stmt.setInt(1, expenseId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            Expense expense =
                    new Expense(
                            rs.getInt("id"),
                            rs.getDate("date"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getBigDecimal("amount"),
                            rs.getString("picture"),
                            rs.getInt("staff_id"));

            Staff staff =
                    new Staff(
                            rs.getInt("s_id"),
                            rs.getString("staff_name"),
                            rs.getString("position"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"));

            return Optional.of(new ExpenseWithStaff(expense, staff));
        });
    }

    public List<ExpenseWithStaff> searchExpensesWithStaff(String searchTerm) {
        return DB.unsafeExecute(connection -> {
            List<ExpenseWithStaff> results = new ArrayList<ExpenseWithStaff>();

            PreparedStatement stmt =
                    connection.prepareStatement(
                            "SELECT e.*, s.id as s_id, s.name as staff_name, s.position, s.username, s.password, s.role "
                                    + "FROM expenses e "
                                    + "JOIN staff s ON e.staff_id = s.id "
                                    + "WHERE e.name LIKE ? OR s.name LIKE ?");

            String likeParam = "%" + searchTerm + "%";

            stmt.setString(1, likeParam);

            stmt.setString(2, likeParam);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Expense expense =
                        new Expense(
                                rs.getInt("id"),
                                rs.getDate("date"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getBigDecimal("amount"),
                                rs.getString("picture"),
                                rs.getInt("staff_id"));

                Staff staff =
                        new Staff(
                                rs.getInt("s_id"),
                                rs.getString("staff_name"),
                                rs.getString("position"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("role"));

                results.add(new ExpenseWithStaff(expense, staff));
            }

            return results;
        });
    }

    public List<ExpenseWithStaff> getAllExpensesWithStaff() {
        return DB.unsafeExecute(connection -> {
            List<ExpenseWithStaff> expensesWithStaff = new ArrayList<ExpenseWithStaff>();
            Statement stmt = connection.createStatement();
            ResultSet rs =
                    stmt.executeQuery(
                            "SELECT e.*, s.id as s_id, s.name as staff_name, s.position, s.username, s.password, s.role "
                                    + "FROM expenses e "
                                    + "JOIN staff s ON e.staff_id = s.id");

            while (rs.next()) {
                Expense expense =
                        new Expense(
                                rs.getInt("id"),
                                rs.getDate("date"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getBigDecimal("amount"),
                                rs.getString("picture"),
                                rs.getInt("staff_id"));

                Staff staff =
                        new Staff(
                                rs.getInt("s_id"),
                                rs.getString("staff_name"),
                                rs.getString("position"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("role"));

                expensesWithStaff.add(new ExpenseWithStaff(expense, staff));
            }

            return expensesWithStaff;
        });
    }
}
