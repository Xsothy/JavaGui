package Repository;

import Model.Expense;
import Model.ExpenseWithStaff;
import Model.Staff;
import Support.DB;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository class for managing Expense data in the database.
 */
public class ExpenseRepository {
    private static final Logger LOGGER = Logger.getLogger(ExpenseRepository.class.getName());
    private final DB db;
    
    /**
     * Constructor that initializes the database connection.
     */
    public ExpenseRepository() {
        this.db = DB.getInstance();
    }
    
    /**
     * Retrieves an expense by its ID.
     *
     * @param expenseId The ID of the expense to retrieve
     * @return An Optional containing the expense if found, or empty if not found
     */
    public Optional<Expense> getExpenseById(int expenseId) {
        try {
            return db.execute(connection -> {
                var stmt = connection.prepareStatement(
                    "SELECT * FROM expenses WHERE id = ?"
                );
                stmt.setInt(1, expenseId);
                var rs = stmt.executeQuery();
                
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
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving expense by ID", ex);
            return Optional.empty();
        }
    }
    
    /**
     * Retrieves all expenses from the database.
     *
     * @return A list of all expenses
     */
    public List<Expense> getAllExpenses() {
        try {
            return db.execute(connection -> {
                var expenseList = new ArrayList<Expense>();
                var stmt = connection.createStatement();
                var rs = stmt.executeQuery(
                    "SELECT * FROM expenses"
                );
                
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
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving all expenses", ex);
            return new ArrayList<>();
        }
    }
    
    /**
     * Adds a new expense to the database.
     *
     * @param name The name of the expense
     * @param description The description of the expense
     * @param amount The amount of the expense
     * @param staffId The ID of the staff associated with the expense
     * @return True if the expense was added successfully, false otherwise
     */
    public boolean addExpense(String name, String description, double amount, int staffId) {
        try {
            return db.execute(connection -> {
                var stmt = connection.prepareStatement(
                    "INSERT INTO expenses (name, description, amount, staff_id) VALUES (?, ?, ?, ?)"
                );
                stmt.setString(1, name);
                stmt.setString(2, description);
                stmt.setDouble(3, amount);
                stmt.setInt(4, staffId);
                
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            });
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding expense", ex);
            return false;
        }
    }
    
    /**
     * Updates an existing expense in the database.
     *
     * @param expenseId The ID of the expense to update
     * @param name The updated name of the expense
     * @param description The updated description of the expense
     * @param amount The updated amount of the expense
     * @param staffId The updated staff ID associated with the expense
     * @return true if the update was successful, false otherwise
     */
    public boolean updateExpense(int expenseId, String name, String description, double amount, int staffId) {
        try {
            return db.execute(connection -> {
                var stmt = connection.prepareStatement(
                    "UPDATE expenses SET name = ?, description = ?, amount = ?, staff_id = ? WHERE id = ?"
                );
                stmt.setString(1, name);
                stmt.setString(2, description);
                stmt.setDouble(3, amount);
                stmt.setInt(4, staffId);
                stmt.setInt(5, expenseId);
                
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            });
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating expense", ex);
            return false;
        }
    }
    
    /**
     * Deletes an expense from the database.
     *
     * @param expenseId The ID of the expense to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteExpense(int expenseId) {
        try {
            return db.execute(connection -> {
                var stmt = connection.prepareStatement(
                    "DELETE FROM expenses WHERE id = ?"
                );
                stmt.setInt(1, expenseId);
                
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            });
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting expense", ex);
            return false;
        }
    }
    
    /**
     * Retrieves an expense with staff information by expense ID.
     *
     * @param expenseId The ID of the expense to retrieve
     * @return An Optional containing the expense with staff information if found, or empty if not found
     */
    public Optional<ExpenseWithStaff> getExpenseWithStaffById(int expenseId) {
        try {
            return db.execute(connection -> {
                var stmt = connection.prepareStatement(
                    "SELECT e.*, s.id as s_id, s.name as staff_name, s.position, s.username, s.password, s.role " +
                    "FROM expenses e " +
                    "JOIN staff s ON e.staff_id = s.id " +
                    "WHERE e.id = ?"
                );
                stmt.setInt(1, expenseId);
                var rs = stmt.executeQuery();
                
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
                
                Staff staff = new Staff(
                    rs.getInt("s_id"),
                    rs.getString("staff_name"),
                    rs.getString("position"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
                
                return Optional.of(new ExpenseWithStaff(expense, staff));
            });
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving expense with staff by ID", ex);
            return Optional.empty();
        }
    }
    
    /**
     * Retrieves all expenses with staff information.
     *
     * @return A list of all expenses with staff information
     */
    public List<ExpenseWithStaff> getAllExpensesWithStaff() {
        try {
            return db.execute(connection -> {
                var expensesWithStaff = new ArrayList<ExpenseWithStaff>();
                var stmt = connection.createStatement();
                var rs = stmt.executeQuery(
                    "SELECT e.*, s.id as s_id, s.name as staff_name, s.position, s.username, s.password, s.role " +
                    "FROM expenses e " +
                    "JOIN staff s ON e.staff_id = s.id"
                );
                
                while (rs.next()) {
                    Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("amount"),
                        rs.getString("picture"),
                        rs.getInt("staff_id")
                    );
                    
                    Staff staff = new Staff(
                        rs.getInt("s_id"),
                        rs.getString("staff_name"),
                        rs.getString("position"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                    
                    expensesWithStaff.add(new ExpenseWithStaff(expense, staff));
                }
                
                return expensesWithStaff;
            });
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving all expenses with staff", ex);
            return new ArrayList<>();
        }
    }
    
    /**
     * Searches for expenses with staff information based on a search term.
     *
     * @param searchTerm The term to search for in expense name, description, or staff name
     * @return A list of expenses with staff information matching the search term
     */
    public List<ExpenseWithStaff> searchExpensesWithStaff(String searchTerm) {
        try {
            return db.execute(connection -> {
                var results = new ArrayList<ExpenseWithStaff>();
                var stmt = connection.prepareStatement(
                    "SELECT e.*, s.id as s_id, s.name as staff_name, s.position, s.username, s.password, s.role " +
                    "FROM expenses e " +
                    "JOIN staff s ON e.staff_id = s.id " +
                    "WHERE e.name LIKE ? OR e.description LIKE ? OR s.name LIKE ?"
                );
                
                String likeParam = "%" + searchTerm + "%";
                stmt.setString(1, likeParam);
                stmt.setString(2, likeParam);
                stmt.setString(3, likeParam);
                var rs = stmt.executeQuery();
                
                while (rs.next()) {
                    Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("amount"),
                        rs.getString("picture"),
                        rs.getInt("staff_id")
                    );
                    
                    Staff staff = new Staff(
                        rs.getInt("s_id"),
                        rs.getString("staff_name"),
                        rs.getString("position"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                    
                    results.add(new ExpenseWithStaff(expense, staff));
                }
                
                return results;
            });
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching expenses with staff", ex);
            return new ArrayList<>();
        }
    }
} 