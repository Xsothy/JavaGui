package Repository;

import Model.Expense;
import Support.DB;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing expense data in the database.
 */
public class ExpenseRepository {
    private static DB db;

    public ExpenseRepository() {
        db = DB.getInstance();
    }

    /**
     * Get an expense by ID.
     * 
     * @param expenseId The ID of the expense
     * @return An Optional containing the expense if found, or empty if not found
     * @throws SQLException If a database error occurs
     */
    public Optional<Expense> getExpenseById(int expenseId) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, description, amount, picture, staff_id FROM expenses WHERE id = ?"
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
                    rs.getInt("staff_id")
            ));
        });
    }

    /**
     * Get all expenses.
     * 
     * @return A list of all expenses
     * @throws SQLException If a database error occurs
     */
    public List<Expense> getAllExpenses() throws SQLException {
        return db.execute(connection -> {
            List<Expense> expenseList = new ArrayList<>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, description, amount, picture, staff_id FROM expenses");

            while (rs.next()) {
                expenseList.add(new Expense(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("amount"),
                        rs.getString("picture"),
                        rs.getInt("staff_id")
                ));
            }

            return expenseList;
        });
    }

    /**
     * Create a new expense.
     * 
     * @param name The name of the expense
     * @param description The description of the expense
     * @param amount The amount of the expense
     * @param picture The picture URL of the expense
     * @param staffId The ID of the staff member who created the expense
     * @return The created expense with its ID
     * @throws SQLException If a database error occurs
     */
    public Expense createExpense(String name, String description, BigDecimal amount, String picture, int staffId) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO expenses (name, description, amount, picture, staff_id) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setBigDecimal(3, amount);
            stmt.setString(4, picture);
            stmt.setInt(5, staffId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating expense failed, no rows affected.");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
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
        });
    }

    /**
     * Update an expense.
     * 
     * @param expense The expense to update
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updateExpense(Expense expense) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE expenses SET name = ?, description = ?, amount = ?, picture = ?, staff_id = ? WHERE id = ?"
            );
            stmt.setString(1, expense.getName());
            stmt.setString(2, expense.getDescription());
            stmt.setBigDecimal(3, expense.getAmount());
            stmt.setString(4, expense.getPicture());
            stmt.setInt(5, expense.getStaffId());
            stmt.setInt(6, expense.getId());

            return stmt.executeUpdate() > 0;
        });
    }

    /**
     * Delete an expense.
     * 
     * @param expenseId The ID of the expense to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean deleteExpense(int expenseId) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM expenses WHERE id = ?");
            stmt.setInt(1, expenseId);
            return stmt.executeUpdate() > 0;
        });
    }

    /**
     * Get expenses by staff ID.
     * 
     * @param staffId The ID of the staff member
     * @return A list of expenses created by the staff member
     * @throws SQLException If a database error occurs
     */
    public List<Expense> getExpensesByStaffId(int staffId) throws SQLException {
        return db.execute(connection -> {
            List<Expense> expenseList = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, description, amount, picture, staff_id FROM expenses WHERE staff_id = ?"
            );
            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                expenseList.add(new Expense(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("amount"),
                        rs.getString("picture"),
                        rs.getInt("staff_id")
                ));
            }

            return expenseList;
        });
    }

    /**
     * Get the count of all expenses.
     * 
     * @return The count of expenses
     * @throws SQLException If a database error occurs
     */
    public int countExpenses() throws SQLException {
        return db.execute(connection -> {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM expenses");
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
            return 0;
        });
    }

    /**
     * Get an expense by ID with its related staff information.
     * 
     * @param expenseId The ID of the expense
     * @return A map containing the expense and its related staff information
     * @throws SQLException If a database error occurs
     */
    public Optional<ExpenseWithStaff> getExpenseWithStaffById(int expenseId) throws SQLException {
        return db.execute(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT e.id, e.name, e.description, e.amount, e.picture, e.staff_id, " +
                    "s.name as staff_name, s.position as staff_position, s.username as staff_username, s.role as staff_role " +
                    "FROM expenses e " +
                    "JOIN staff s ON e.staff_id = s.id " +
                    "WHERE e.id = ?"
            );
            stmt.setInt(1, expenseId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            Expense expense = new Expense(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBigDecimal("amount"),
                rs.getString("picture"),
                rs.getInt("staff_id")
            );
            
            ExpenseWithStaff expenseWithStaff = new ExpenseWithStaff(
                expense,
                rs.getString("staff_name"),
                rs.getString("staff_position"),
                rs.getString("staff_username"),
                rs.getString("staff_role")
            );
            
            return Optional.of(expenseWithStaff);
        });
    }
    
    /**
     * Get all expenses with their related staff information.
     * 
     * @return A list of maps containing expenses and their related staff information
     * @throws SQLException If a database error occurs
     */
    public List<ExpenseWithStaff> getAllExpensesWithStaff() throws SQLException {
        return db.execute(connection -> {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT e.id, e.name, e.description, e.amount, e.picture, e.staff_id, " +
                "s.name as staff_name, s.position as staff_position, s.username as staff_username, s.role as staff_role " +
                "FROM expenses e " +
                "JOIN staff s ON e.staff_id = s.id " +
                "ORDER BY e.id DESC"
            );
            
            List<ExpenseWithStaff> expenses = new ArrayList<>();
            
            while (rs.next()) {
                Expense expense = new Expense(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBigDecimal("amount"),
                    rs.getString("picture"),
                    rs.getInt("staff_id")
                );
                
                ExpenseWithStaff expenseWithStaff = new ExpenseWithStaff(
                    expense,
                    rs.getString("staff_name"),
                    rs.getString("staff_position"),
                    rs.getString("staff_username"),
                    rs.getString("staff_role")
                );
                
                expenses.add(expenseWithStaff);
            }
            
            return expenses;
        });
    }
    
    /**
     * Class to hold an expense with its related staff information.
     */
    public static class ExpenseWithStaff {
        private final Expense expense;
        private final String staffName;
        private final String staffPosition;
        private final String staffUsername;
        private final String staffRole;
        
        public ExpenseWithStaff(Expense expense, String staffName, String staffPosition, String staffUsername, String staffRole) {
            this.expense = expense;
            this.staffName = staffName;
            this.staffPosition = staffPosition;
            this.staffUsername = staffUsername;
            this.staffRole = staffRole;
        }
        
        public Expense getExpense() {
            return expense;
        }
        
        public String getStaffName() {
            return staffName;
        }
        
        public String getStaffPosition() {
            return staffPosition;
        }
        
        public String getStaffUsername() {
            return staffUsername;
        }
        
        public String getStaffRole() {
            return staffRole;
        }
    }
}