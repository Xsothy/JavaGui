package Controller;

import Model.Expense;
import Model.Staff;
import Repository.ExpenseRepository;
import Repository.StaffRepository;
import Support.FileUtils;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

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

        return expenseRepository.getExpensesByStaffId(staffId);
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

        return expenseRepository.getExpensesByStaffId(staffOpt.get().getId());
    }

    /**
     * Creates a new expense.
     * 
     * @param name The name of the expense
     * @param description The description of the expense
     * @param amount The amount of the expense
     * @param imagePath The path to the expense image
     * @param staffId The ID of the staff member who created the expense
     * @return The created expense
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     * @throws IOException If an error occurs while handling the image file
     */
    public Expense createExpense(String name, String description, BigDecimal amount, String imagePath, int staffId) 
            throws IllegalArgumentException, SQLException, IOException {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense name cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense description cannot be empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than zero");
        }
        if (staffId <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }

        // Check if staff exists
        Optional<Staff> staffOpt = staffRepository.getStaffById(staffId);
        if (staffOpt.isEmpty()) {
            throw new IllegalArgumentException("Staff not found");
        }
        
        // Handle image file if provided
        String savedImagePath = null;
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            savedImagePath = FileUtils.saveExpenseImage(imagePath);
            if (savedImagePath == null) {
                throw new IOException("Failed to save image file");
            }
        }
        
        // Create expense with saved image path
        return expenseRepository.createExpense(name, description, amount, savedImagePath, staffId);
    }
    
    /**
     * Updates an expense.
     * 
     * @param expense The expense to update
     * @param newImagePath The path to the new expense image, or null to keep the existing image
     * @return true if the update was successful, false otherwise
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     * @throws IOException If an error occurs while handling the image file
     */
    public boolean updateExpense(Expense expense, String newImagePath) 
            throws IllegalArgumentException, SQLException, IOException {
        // Validate input
        if (expense == null) {
            throw new IllegalArgumentException("Expense cannot be null");
        }
        if (expense.getId() <= 0) {
            throw new IllegalArgumentException("Invalid expense ID");
        }
        if (expense.getName() == null || expense.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Expense name cannot be empty");
        }
        if (expense.getDescription() == null || expense.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Expense description cannot be empty");
        }
        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than zero");
        }
        if (expense.getStaffId() <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }

        // Check if expense exists
        Optional<Expense> existingExpenseOpt = expenseRepository.getExpenseById(expense.getId());
        if (existingExpenseOpt.isEmpty()) {
            throw new IllegalArgumentException("Expense not found");
        }
        
        // Check if staff exists
        Optional<Staff> staffOpt = staffRepository.getStaffById(expense.getStaffId());
        if (staffOpt.isEmpty()) {
            throw new IllegalArgumentException("Staff not found");
        }
        
        // Handle image file if a new one is provided
        if (newImagePath != null && !newImagePath.trim().isEmpty()) {
            String savedImagePath = FileUtils.saveExpenseImage(newImagePath);
            if (savedImagePath == null) {
                throw new IOException("Failed to save image file");
            }
            expense.setPicture(savedImagePath);
        }
        
        return expenseRepository.updateExpense(expense);
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

        boolean deleted = expenseRepository.deleteExpense(expenseId);
        if (!deleted) {
            throw new SQLException("Failed to delete expense with ID: " + expenseId);
        }
        return true;
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
}