package Repository;

import Model.Expense;
import Model.ExpenseWithStaff;
import Model.Staff;
import Support.DB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
     * @return The expense with the specified ID, or null if not found
     */
    public Expense getExpenseById(int expenseId) {
        String query = "SELECT * FROM expenses WHERE id = ?";
        
        try (ResultSet rs = db.executeQuery(query, expenseId)) {
            if (rs.next()) {
                return new Expense(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("amount"),
                    rs.getInt("staff_id")
                );
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving expense by ID", ex);
        }
        
        return null;
    }
    
    /**
     * Retrieves all expenses from the database.
     *
     * @return A list of all expenses
     */
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT * FROM expenses";
        
        try (ResultSet rs = db.executeQuery(query)) {
            while (rs.next()) {
                expenses.add(new Expense(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("amount"),
                    rs.getInt("staff_id")
                ));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving all expenses", ex);
        }
        
        return expenses;
    }
    
    /**
     * Adds a new expense to the database.
     *
     * @param name The name of the expense
     * @param description The description of the expense
     * @param amount The amount of the expense
     * @param staffId The ID of the staff associated with the expense
     * @return The ID of the newly created expense, or -1 if an error occurred
     */
    public int addExpense(String name, String description, double amount, int staffId) {
        String query = "INSERT INTO expenses (name, description, amount, staff_id) VALUES (?, ?, ?, ?)";
        
        try {
            int rowsAffected = db.execute(query, name, description, amount, staffId);
            if (rowsAffected > 0) {
                // Get the ID of the newly inserted expense
                try (ResultSet rs = db.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding expense", ex);
        }
        
        return -1;
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
        String query = "UPDATE expenses SET name = ?, description = ?, amount = ?, staff_id = ? WHERE id = ?";
        
        try {
            int rowsAffected = db.execute(query, name, description, amount, staffId, expenseId);
            return rowsAffected > 0;
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
        String query = "DELETE FROM expenses WHERE id = ?";
        
        try {
            int rowsAffected = db.execute(query, expenseId);
            return rowsAffected > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting expense", ex);
            return false;
        }
    }
    
    /**
     * Retrieves an expense with staff information by expense ID.
     *
     * @param expenseId The ID of the expense to retrieve
     * @return The expense with staff information, or null if not found
     */
    public ExpenseWithStaff getExpenseWithStaffById(int expenseId) {
        String query = "SELECT e.*, s.name as staff_name, s.position as staff_position " +
                       "FROM expenses e " +
                       "JOIN staff s ON e.staff_id = s.id " +
                       "WHERE e.id = ?";
        
        try (ResultSet rs = db.executeQuery(query, expenseId)) {
            if (rs.next()) {
                Expense expense = new Expense(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("amount"),
                    rs.getInt("staff_id")
                );
                
                Staff staff = new Staff();
                staff.setId(rs.getInt("staff_id"));
                staff.setName(rs.getString("staff_name"));
                staff.setPosition(rs.getString("staff_position"));
                
                return new ExpenseWithStaff(expense, staff);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving expense with staff by ID", ex);
        }
        
        return null;
    }
    
    /**
     * Retrieves all expenses with staff information.
     *
     * @return A list of all expenses with staff information
     */
    public List<ExpenseWithStaff> getAllExpensesWithStaff() {
        List<ExpenseWithStaff> expensesWithStaff = new ArrayList<>();
        String query = "SELECT e.*, s.name as staff_name, s.position as staff_position " +
                       "FROM expenses e " +
                       "JOIN staff s ON e.staff_id = s.id";
        
        try (ResultSet rs = db.executeQuery(query)) {
            while (rs.next()) {
                Expense expense = new Expense(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("amount"),
                    rs.getInt("staff_id")
                );
                
                Staff staff = new Staff();
                staff.setId(rs.getInt("staff_id"));
                staff.setName(rs.getString("staff_name"));
                staff.setPosition(rs.getString("staff_position"));
                
                expensesWithStaff.add(new ExpenseWithStaff(expense, staff));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving all expenses with staff", ex);
        }
        
        return expensesWithStaff;
    }
    
    /**
     * Searches for expenses with staff information based on a search term.
     *
     * @param searchTerm The term to search for in expense name, description, or staff name
     * @return A list of expenses with staff information matching the search term
     */
    public List<ExpenseWithStaff> searchExpensesWithStaff(String searchTerm) {
        List<ExpenseWithStaff> results = new ArrayList<>();
        String query = "SELECT e.*, s.name as staff_name, s.position as staff_position " +
                       "FROM expenses e " +
                       "JOIN staff s ON e.staff_id = s.id " +
                       "WHERE e.name LIKE ? OR e.description LIKE ? OR s.name LIKE ?";
        
        String likeParam = "%" + searchTerm + "%";
        
        try (ResultSet rs = db.executeQuery(query, likeParam, likeParam, likeParam)) {
            while (rs.next()) {
                Expense expense = new Expense(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("amount"),
                    rs.getInt("staff_id")
                );
                
                Staff staff = new Staff();
                staff.setId(rs.getInt("staff_id"));
                staff.setName(rs.getString("staff_name"));
                staff.setPosition(rs.getString("staff_position"));
                
                results.add(new ExpenseWithStaff(expense, staff));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error searching expenses with staff", ex);
        }
        
        return results;
    }
} 