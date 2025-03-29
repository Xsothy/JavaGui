package Model;

import Model.ExpenseWithStaff;
import Model.Staff;

import java.util.List;

public class StaffWithExpenses {
    private final Staff staff;
    private final List<ExpenseWithStaff> expenses;

    public StaffWithExpenses(Staff staff, List<ExpenseWithStaff> expenses) {
        this.staff = staff;
        this.expenses = expenses;
    }

    public Staff getStaff() {
        return staff;
    }

    public List<ExpenseWithStaff> getExpenses() {
        return expenses;
    }

    public int getExpenseCount() {
        return expenses.size();
    }

    public java.math.BigDecimal getTotalExpenseAmount() {
        return expenses.stream()
                .map(e -> e.getExpense().getAmount())
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }
}