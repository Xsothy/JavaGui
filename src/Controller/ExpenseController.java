package Controller;

import Model.Expense;
import Model.Staff;
import Repository.ExpenseRepository;
import Repository.StaffRepository;
import Support.Response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ExpenseController {
    private final ExpenseRepository expenseRepository;
    private final StaffRepository staffRepository;

    public ExpenseController() {
        this.expenseRepository = new ExpenseRepository();
        this.staffRepository = new StaffRepository();
    }

    public Response<List<Expense>> getAllExpenses() {
        return expenseRepository.getAllExpenses();
    }

    public Response<Optional<Expense>> getExpenseById(int id) {
        if (id <= 0) {
            return Response.error("Invalid expense ID");
        }
        return expenseRepository.getExpenseById(id);
    }

    public Response<List<Expense>> getExpensesByStaffId(int staffId) {
        if (staffId <= 0) {
            return Response.error("Invalid staff ID");
        }

        // Check if staff exists
        Response<Optional<Staff>> staffResponse = staffRepository.getStaffById(staffId);
        if (!staffResponse.isSuccess()) {
            return Response.error("Error accessing staff data: " + staffResponse.getMessage());
        }
        if (staffResponse.getData().isEmpty()) {
            return Response.error("Staff not found");
        }

        return expenseRepository.getExpensesByStaffId(staffId);
    }

    public Response<List<Expense>> getExpensesByStaffName(String staffName) {
        if (staffName == null || staffName.trim().isEmpty()) {
            return Response.error("Staff name cannot be empty");
        }

        // Check if staff exists
        Response<Optional<Staff>> staffResponse = staffRepository.getStaffByUserName(staffName);
        if (!staffResponse.isSuccess()) {
            return Response.error("Error accessing staff data: " + staffResponse.getMessage());
        }
        if (staffResponse.getData().isEmpty()) {
            return Response.error("Staff not found");
        }

        return expenseRepository.getExpensesByStaffId(staffResponse.getData().get().getId());
    }

    public Response<Expense> createExpense(String name, String description, BigDecimal amount, String picture, int staffId) {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            return Response.error("Expense name cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            return Response.error("Expense description cannot be empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Response.error("Expense amount cannot be empty");
        }
        if (picture == null || picture.trim().isEmpty()) {
            return Response.error("Expense picture cannot be empty");
        }
        if (staffId <= 0) {
            return Response.error("Invalid staff ID");
        }

        // Check if staff exists
        Response<Optional<Staff>> staffResponse = staffRepository.getStaffById(staffId);
        if (!staffResponse.isSuccess()) {
            return Response.error("Error accessing staff data: " + staffResponse.getMessage());
        }
        if (staffResponse.getData().isEmpty()) {
            return Response.error("Staff not found");
        }

        return expenseRepository.createExpense(name, description, amount, picture, staffId);
    }

    public Response<Boolean> updateExpense(Expense expense) {
        // Validate input
        if (expense == null) {
            return Response.error("Expense cannot be null");
        }
        if (expense.getId() <= 0) {
            return Response.error("Invalid expense ID");
        }
        if (expense.getName() == null || expense.getName().trim().isEmpty()) {
            return Response.error("Expense name cannot be empty");
        }
        if (expense.getDescription() == null || expense.getDescription().trim().isEmpty()) {
            return Response.error("Expense description cannot be empty");
        }
        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Response.error("Expense amount cannot be empty");
        }
        if (expense.getPicture() == null || expense.getPicture().trim().isEmpty()) {
            return Response.error("Expense picture cannot be empty");
        }
        if (expense.getStaffId() <= 0) {
            return Response.error("Invalid staff ID");
        }

        // Check if staff exists
        Response<Optional<Staff>> staffResponse = staffRepository.getStaffById(expense.getStaffId());
        if (!staffResponse.isSuccess()) {
            return Response.error("Error accessing staff data: " + staffResponse.getMessage());
        }
        if (staffResponse.getData().isEmpty()) {
            return Response.error("Staff not found");
        }

        return expenseRepository.updateExpense(expense);
    }

    public Response<Boolean> deleteExpense(int expenseId) {
        // Validate input
        if (expenseId <= 0) {
            return Response.error("Invalid expense ID");
        }

        // Check if staff exists
        Response<Optional<Expense>> expenseResponse = expenseRepository.getExpenseById(expenseId);
        if (!expenseResponse.isSuccess()) {
            return Response.error("Error accessing expense data: " + expenseResponse.getMessage());
        }
        if (expenseResponse.getData().isEmpty()) {
            return Response.error("Expense not found");
        }

        return expenseRepository.deleteExpense(expenseId);
    }
}