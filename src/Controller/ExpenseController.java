package Controller;

import Model.Expense;
import Model.ExpenseWithStaff;
import Model.Staff;
import Repository.ExpenseRepository;
import Repository.StaffRepository;
import Support.FileUtils;
import View.Dashboard.ExpenseFormPanel;
import View.Dashboard.ExpensePanel;
import View.NavigatePanel;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.*;

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

    public NavigatePanel edit(Map<String, String> params) {
        // Extract the ID parameter
        int expenseId = Integer.parseInt(params.get("id"));

        try {
            // Get the expense to edit
            Optional<Expense> expenseOpt = this.getExpenseById(expenseId);
            if (expenseOpt.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Expense not found", "Error", JOptionPane.ERROR_MESSAGE);
                return new NavigatePanel(); // Return empty panel or navigate back
            }

            return new ExpenseFormPanel(expenseOpt.get());
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ExpensePanel.class.getName()).log(Level.SEVERE, "Error opening expense edit form", ex);
            JOptionPane.showMessageDialog(null, "Error opening expense edit form: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return new NavigatePanel(); // Return empty panel or navigate back
        }
    }

    /**
     * Gets all expenses.
     * 
     * @return A list of all expenses
     */
    public List<Expense> getAllExpenses() {
        return expenseRepository.getAllExpenses();
    }

    /**
     * Gets an expense by ID.
     * 
     * @param id The ID of the expense
     * @return An Optional containing the expense if found, or empty if not found
     * @throws IllegalArgumentException If the input is invalid
     */
    public Optional<Expense> getExpenseById(int id) throws IllegalArgumentException {
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
     */
    public List<Expense> getExpensesByStaffId(int staffId) throws IllegalArgumentException {
        if (staffId <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }

        try {
            // Check if staff exists
            Optional<Staff> staffOpt = staffRepository.getStaffById(staffId);
            if (staffOpt.isEmpty()) {
                throw new IllegalArgumentException("Staff not found");
            }

            // Filter all expenses by staff ID
            return expenseRepository.getAllExpenses().stream()
                .filter(expense -> expense.getStaffId() == staffId)
                .collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error getting expenses by staff ID", ex);
            return new ArrayList<>();
        }
    }

    /**
     * Gets expenses by staff username.
     * 
     * @param staffName The username of the staff member
     * @return A list of expenses created by the staff member
     * @throws IllegalArgumentException If the input is invalid
     */
    public List<Expense> getExpensesByStaffName(String staffName) throws IllegalArgumentException {
        if (staffName == null || staffName.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff name cannot be empty");
        }

        try {
            // Check if staff exists
            Optional<Staff> staffOpt = staffRepository.getStaffByUserName(staffName);
            if (staffOpt.isEmpty()) {
                throw new IllegalArgumentException("Staff not found");
            }

            // Get all expenses by staff ID
            return getExpensesByStaffId(staffOpt.get().getId());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error getting expenses by staff name", ex);
            return new ArrayList<>();
        }
    }

    /**
     * Deletes an expense.
     *
     * @param expenseId The ID of the expense to delete
     * @throws IllegalArgumentException If the input is invalid
     */
    public void deleteExpense(int expenseId) throws IllegalArgumentException {
        if (expenseId <= 0) {
            throw new IllegalArgumentException("Invalid expense ID");
        }

        expenseRepository.deleteExpense(expenseId);
    }

    /**
     * Gets all expenses with staff information.
     * 
     * @return A list of all expenses with staff information
     */
    public List<ExpenseWithStaff> getAllExpensesWithStaff() {
        return expenseRepository.getAllExpensesWithStaff();
    }

    public List<ExpenseWithStaff> filterExpense(String searchTerm, String duration) {
        List<ExpenseWithStaff> expenses;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            expenses = expenseRepository.searchExpensesWithStaff(searchTerm);
        } else {
            expenses = expenseRepository.getAllExpensesWithStaff();
        }

        if (duration != null && !duration.equals("All")) {
            expenses = filterByDuration(expenses, duration);
        }

        return expenses;
    }

    private List<ExpenseWithStaff> filterByDuration(List<ExpenseWithStaff> expenses, String duration) {
        List<ExpenseWithStaff> filteredExpenses = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        java.util.Date today = calendar.getTime();

        switch (duration) {
            case "This Week":
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                java.util.Date startOfWeek = calendar.getTime();
                calendar.add(Calendar.DAY_OF_WEEK, 7);
                java.util.Date endOfWeek = calendar.getTime();
                filteredExpenses = filterByDateRange(expenses, startOfWeek, endOfWeek);
                break;
            case "Last Week":
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                java.util.Date startOfLastWeek = calendar.getTime();
                calendar.add(Calendar.DAY_OF_WEEK, 7);
                java.util.Date endOfLastWeek = calendar.getTime();
                filteredExpenses = filterByDateRange(expenses, startOfLastWeek, endOfLastWeek);
                break;
            case "This Month":
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                java.util.Date startOfMonth = calendar.getTime();
                calendar.add(Calendar.MONTH, 1);
                java.util.Date endOfMonth = calendar.getTime();
                filteredExpenses = filterByDateRange(expenses, startOfMonth, endOfMonth);
                break;
            case "Last Month":
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                java.util.Date startOfLastMonth = calendar.getTime();
                calendar.add(Calendar.MONTH, 1);
                java.util.Date endOfLastMonth = calendar.getTime();
                filteredExpenses = filterByDateRange(expenses, startOfLastMonth, endOfLastMonth);
                break;
            case "This Year":
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                java.util.Date startOfYear = calendar.getTime();
                calendar.add(Calendar.YEAR, 1);
                java.util.Date endOfYear = calendar.getTime();
                filteredExpenses = filterByDateRange(expenses, startOfYear, endOfYear);
                break;
            default:
                filteredExpenses = expenses;
                break;
        }

        return filteredExpenses;
    }

    private List<ExpenseWithStaff> filterByDateRange(List<ExpenseWithStaff> expenses, java.util.Date startDate, java.util.Date endDate) {
        List<ExpenseWithStaff> filteredExpenses = new ArrayList<>();
        for (ExpenseWithStaff expenseWithStaff : expenses) {
            java.util.Date expenseDate = expenseWithStaff.getExpense().getDate();
            if (expenseDate.compareTo(startDate) >= 0 && expenseDate.compareTo(endDate) < 0) {
                filteredExpenses.add(expenseWithStaff);
            }
        }
        return filteredExpenses;
    }


    /**
     * Gets an expense with staff information by expense ID.
     * 
     * @param expenseId The ID of the expense
     * @return An Optional containing the expense with staff information if found, or empty if not found
     * @throws IllegalArgumentException If the input is invalid
     */
    public Optional<ExpenseWithStaff> getExpenseWithStaffById(int expenseId) throws IllegalArgumentException {
        if (expenseId <= 0) {
            throw new IllegalArgumentException("Invalid expense ID");
        }
        
        return expenseRepository.getExpenseWithStaffById(expenseId);
    }

    /**
     * Adds a new expense.
     *
     * @param name The name of the expense
     * @param description The description of the expense
     * @param amount The amount of the expense
     * @param staffId The ID of the staff member associated with the expense
     * @return True if the expense was added successfully, false otherwise
     * @throws IllegalArgumentException If any input is invalid
     */
    public boolean addExpense(String name, String description, double amount, int staffId) throws IllegalArgumentException {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense name is required");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (staffId <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }
        
        return expenseRepository.addExpense(name, description, amount, staffId);
    }

    /**
     * Updates an existing expense.
     *
     * @param expenseId The ID of the expense to update
     * @param name The new name of the expense
     * @param description The new description of the expense
     * @param amount The new amount of the expense
     * @param staffId The new ID of the staff member associated with the expense
     * @return True if the expense was updated successfully, false otherwise
     * @throws IllegalArgumentException If any input is invalid
     */
    public boolean updateExpense(int expenseId, String name, String description, double amount, int staffId) throws IllegalArgumentException {
        // Validate inputs
        if (expenseId <= 0) {
            throw new IllegalArgumentException("Invalid expense ID");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense name is required");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
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
     * @throws IOException If there's an error handling the image
     */
    public boolean addExpenseWithImage(String name, String description, double amount, String imagePath, int staffId) throws IOException {
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

        try {
            // Save image to the expenses directory if provided
            String savedImagePath = null;
            if (imagePath != null && !imagePath.trim().isEmpty()) {
                savedImagePath = FileUtils.saveExpenseImage(imagePath);
            }

            // Add expense with image
            return expenseRepository.addExpense(name, description, amount, staffId);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error adding expense with image", ex);
            return false;
        }
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
     * @throws IOException If there's an error handling the image
     */
    public boolean updateExpenseWithImage(int expenseId, String name, String description, double amount, String imagePath, int staffId) throws IOException {
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
        
        try {
            // Handle the image if provided
            String savedImagePath = null;
            if (imagePath != null && !imagePath.trim().isEmpty()) {
                savedImagePath = FileUtils.saveExpenseImage(imagePath);
            }
            
            // For now, we'll just ignore the saved image path
            // In a real implementation, you would save this path to the database
            return expenseRepository.updateExpense(expenseId, name, description, amount, staffId);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating expense with image", ex);
            return false;
        }
    }
}