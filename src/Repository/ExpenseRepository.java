package Repository;

import Model.Expense;
import Support.DB;
import Support.Response;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpenseRepository {
    private static DB db;

    public ExpenseRepository() {
        db = DB.getInstance();
    }

    public Response<Optional<Expense>> getExpenseById(int expenseId) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, description, amount, picture, sid FROM expenses WHERE id = ?"
            );
            stmt.setInt(1, expenseId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new Expense(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBigDecimal("amount"),
                    rs.getString("picture"),
                    rs.getInt("sid")
            ));
        });
    }

    public Response<List<Expense>> getAllExpenses() {
        return db.execute(connection -> {
            List<Expense> expenses = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, description, amount, picture, sid FROM expenses"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("amount"),
                        rs.getString("picture"),
                        rs.getInt("sid")
                );
                expenses.add(expense);
            }

            return expenses;
        });
    }

    public Response<List<Expense>> getExpensesByStaffId(int staffId) {
        return db.execute(connection -> {
            List<Expense> expenses = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, description, amount, picture, sid FROM expenses WHERE sid = ?"
            );
            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("amount"),
                        rs.getString("picture"),
                        rs.getInt("sid")
                );
                expenses.add(expense);
            }

            return expenses;
        });
    }

    public Response<Expense> createExpense(String name, String description, BigDecimal amount, String picture, int staffId) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO expenses (name, description, amount, picture, sid) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setBigDecimal(3, amount);
            stmt.setString(4, picture);
            stmt.setInt(5, staffId);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("Creating expense failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Expense(
                            generatedKeys.getInt(1),
                            name,
                            description,
                            amount,
                            picture,
                            staffId
                    );
                } else {
                    throw new SQLException("Creating expense failed, no ID obtained.");
                }
            }
        });
    }

    public Response<Boolean> updateExpense(Expense expense) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE expenses SET name = ?, description = ?, amount = ?, picture = ?, sid = ? WHERE id = ?"
            );
            stmt.setString(1, expense.getName());
            stmt.setString(2, expense.getDescription());
            stmt.setBigDecimal(3, expense.getAmount());
            stmt.setString(4, expense.getPicture());
            stmt.setInt(5, expense.getStaffId());
            stmt.setInt(6, expense.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        });
    }

    public Response<Boolean> deleteExpense(int expenseId) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM expenses WHERE id = ?"
            );
            stmt.setInt(1, expenseId);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        });
    }

    public Response<Boolean> deleteExpensesByStaffId(int staffId) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM expenses WHERE sid = ?"
            );
            stmt.setInt(1, staffId);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        });
    }

    public Response<BigDecimal> getTotalExpenseAmount() {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT SUM(amount) as total FROM expenses"
            );
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("total");
            }
            return BigDecimal.ZERO;
        });
    }

    public Response<BigDecimal> getTotalExpenseAmountByStaffId(int staffId) {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT SUM(amount) as total FROM expenses WHERE sid = ?"
            );
            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                BigDecimal result = rs.getBigDecimal("total");
                return result != null ? result : BigDecimal.ZERO;
            }
            return BigDecimal.ZERO;
        });
    }

    public Response<Integer> countExpenses() {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM expenses"
            );
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        });
    }
}