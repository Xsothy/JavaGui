package Controller;

import Components.ExpenseFormPanel;
import Components.ExpensePanel;
import Model.Expense;
import Model.Staff;
import Repository.ExpenseRepository;
import Repository.StaffRepository;
import Support.FileUtils;
import Support.Router;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller for managing expense operations.
 * Provides business logic and validation for expense-related operations.
 */
public class ExpenseController {
    private static final Logger LOGGER = Logger.getLogger(ExpenseController.class.getName());
    
    private final ExpenseRepository expenseRepository;
    private final StaffRepository staffRepository;

    /**
     * Creates a new ExpenseController.
     */
    public ExpenseController() {
        this.expenseRepository = new ExpenseRepository();
        this.staffRepository = new StaffRepository();
        
        // Ensure the expense images directory exists
        File directory = new File("src/img/expenses/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public JPanel edit(Map<String, String> params, Router router) {
        // Extract the ID parameter
        int expenseId = Integer.parseInt(params.get("id"));

        try {
            // Get the expense to edit
            Optional<Expense> expenseOpt = this.getExpenseById(expenseId);
            if (expenseOpt.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Expense not found", "Error", JOptionPane.ERROR_MESSAGE);
                return new JPanel(); // Return empty panel or navigate back
            }

            return new ExpenseFormPanel(router, expenseOpt.get());
        } catch (SQLException ex) {
            Logger.getLogger(ExpensePanel.class.getName()).log(Level.SEVERE, "Error opening expense edit form", ex);
            JOptionPane.showMessageDialog(null, "Error opening expense edit form: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return new JPanel(); // Return empty panel or navigate back
        }
    }

    /**
     * Gets all expenses.
     * 
     * @return A list of all expenses
     * @throws SQLException If a database error occurs
     */
    public List<Expense> getAllExpenses() throws SQLException {
        return expenseRepository.getAllExpenses();
    }

    /**
     * Gets an expense by ID.
     * 
     * @param id The ID of the expense
     * @return An Optional containing the expense if found, or empty if not found
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     */
    public Optional<Expense> getExpenseById(int id) throws IllegalArgumentException, SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid expense ID");
        }
        return expenseRepository.getExpenseById(id);
    }

    /**
     * Gets expenses by staff ID.
     * 
     * @param staffId The ID of the staff member
     * @return A list of expenses created by the staff member
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     */
    public List<Expense> getExpensesByStaffId(int staffId) throws IllegalArgumentException, SQLException {
        if (staffId <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }

        // Check if staff exists
        Optional<Staff> staffOpt = staffRepository.getStaffById(staffId);
        if (staffOpt.isEmpty()) {
            throw new IllegalArgumentException("Staff not found");
        }

        // Filter all expenses by staff ID
        return expenseRepository.getAllExpenses().stream()
            .filter(expense -> expense.getStaffId() == staffId)
            .collect(Collectors.toList());
    }

    /**
     * Gets expenses by staff username.
     * 
     * @param staffName The username of the staff member
     * @return A list of expenses created by the staff member
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     */
    public List<Expense> getExpensesByStaffName(String staffName) throws IllegalArgumentException, SQLException {
        if (staffName == null || staffName.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff name cannot be empty");
        }

        // Check if staff exists
        Optional<Staff> staffOpt = staffRepository.getStaffByUserName(staffName);
        if (staffOpt.isEmpty()) {
            throw new IllegalArgumentException("Staff not found");
        }

        // Filter all expenses by staff ID
        int staffId = staffOpt.get().getId();
        return expenseRepository.getAllExpenses().stream()
            .filter(expense -> expense.getStaffId() == staffId)
            .collect(Collectors.toList());
    }

    /**
     * Deletes an expense.
     * 
     * @param expenseId The ID of the expense to delete
     * @return true if the deletion was successful, false otherwise
     * @throws IllegalArgumentException If the input is invalid or the expense doesn't exist
     * @throws SQLException If a database error occurs
     */
    public boolean deleteExpense(int expenseId) throws IllegalArgumentException, SQLException {
        // Validate input
        if (expenseId <= 0) {
            throw new IllegalArgumentException("Invalid expense ID");
        }

        // Check if expense exists
        Optional<Expense> expenseOpt = expenseRepository.getExpenseById(expenseId);
        if (expenseOpt.isEmpty()) {
            throw new IllegalArgumentException("Expense not found");
        }

        return expenseRepository.deleteExpense(expenseId);
    }

    /**
     * Gets an expense by ID with its related staff information.
     * 
     * @param id The ID of the expense
     * @return An Optional containing the expense with staff information if found, or empty if not found
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     */
    public Optional<ExpenseRepository.ExpenseWithStaff> getExpenseWithStaffById(int id) throws IllegalArgumentException, SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid expense ID");
        }
        return expenseRepository.getExpenseWithStaffById(id);
    }

    /**
     * Gets all expenses with their related staff information.
     * 
     * @return A list of expenses with staff information
     * @throws SQLException If a database error occurs
     */
    public List<ExpenseRepository.ExpenseWithStaff> getAllExpensesWithStaff() throws SQLException {
        return expenseRepository.getAllExpensesWithStaff();
    }

    /**
     * Searches for expenses with staff details matching the search term.
     * 
     * @param searchTerm The search term to match against expense name, description, or staff name
     * @return A list of expenses with staff details matching the search term
     * @throws SQLException If a database error occurs
     */
    public List<ExpenseRepository.ExpenseWithStaff> searchExpensesWithStaff(String searchTerm) throws SQLException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllExpensesWithStaff(); // Return all expenses if search term is empty
        }
        return expenseRepository.searchExpensesWithStaff(searchTerm.trim());
    }

    /**
     * Add a new expense.
     * 
     * @param name The name of the expense
     * @param description The description of the expense
     * @param amount The amount of the expense
     * @param staffId The ID of the staff member associated with the expense
     * @return true if the expense was added, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean addExpense(String name, String description, double amount, int staffId) throws SQLException {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense name cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense description cannot be empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than zero");
        }
        if (staffId <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }
        
        return expenseRepository.addExpense(name, description, amount, staffId);
    }

    /**
     * Update an existing expense.
     * 
     * @param expenseId The ID of the expense to update
     * @param name The new name of the expense
     * @param description The new description of the expense
     * @param amount The new amount of the expense
     * @param staffId The new ID of the staff member associated with the expense
     * @return true if the expense was updated, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updateExpense(int expenseId, String name, String description, double amount, int staffId) throws SQLException {
        // Validate input
        if (expenseId <= 0) {
            throw new IllegalArgumentException("Invalid expense ID");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense name cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense description cannot be empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than zero");
        }
        if (staffId <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }
        
        return expenseRepository.updateExpense(expenseId, name, description, amount, staffId);
    }

    /**
     * Add a new expense with an image path.
     * 
     * @param name The name of the expense
     * @param description The description of the expense
     * @param amount The amount of the expense
     * @param imagePath The path to the expense image
     * @param staffId The ID of the staff member associated with the expense
     * @return true if the expense was added, false otherwise
     * @throws SQLException If a database error occurs
     * @throws IOException If there's an error handling the image
     */
    public boolean addExpenseWithImage(String name, String description, double amount, String imagePath, int staffId) throws SQLException, IOException {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense name cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense description cannot be empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than zero");
        }
        if (staffId <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }
        
        // Handle the image if provided
        String savedImagePath = null;
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            savedImagePath = FileUtils.saveExpenseImage(imagePath);
        }
        
        // For now, we'll just ignore the saved image path
        // In a real implementation, you would save this path to the database
        return expenseRepository.addExpense(name, description, amount, staffId);
    }

    /**
     * Update an existing expense with an image path.
     * 
     * @param expenseId The ID of the expense to update
     * @param name The new name of the expense
     * @param description The new description of the expense
     * @param amount The new amount of the expense
     * @param imagePath The path to the expense image
     * @param staffId The new ID of the staff member associated with the expense
     * @return true if the expense was updated, false otherwise
     * @throws SQLException If a database error occurs
     * @throws IOException If there's an error handling the image
     */
    public boolean updateExpenseWithImage(int expenseId, String name, String description, double amount, String imagePath, int staffId) throws SQLException, IOException {
        // Validate input
        if (expenseId <= 0) {
            throw new IllegalArgumentException("Invalid expense ID");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense name cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense description cannot be empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than zero");
        }
        if (staffId <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }
        
        // Handle the image if provided
        String savedImagePath = null;
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            savedImagePath = FileUtils.saveExpenseImage(imagePath);
        }
        
        // For now, we'll just ignore the saved image path
        // In a real implementation, you would save this path to the database
        return expenseRepository.updateExpense(expenseId, name, description, amount, staffId);
    }
}