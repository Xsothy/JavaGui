package Model;

/**
 * Model class representing an expense with associated staff information.
 */
public class ExpenseWithStaff {
    private final Expense expense;
    private final Staff staff;
    
    /**
     * Constructor for ExpenseWithStaff.
     *
     * @param expense The expense object
     * @param staff The staff object associated with this expense
     */
    public ExpenseWithStaff(Expense expense, Staff staff) {
        this.expense = expense;
        this.staff = staff;
    }
    
    /**
     * Get the expense object.
     *
     * @return The expense object
     */
    public Expense getExpense() {
        return expense;
    }
    
    /**
     * Get the staff object.
     *
     * @return The staff object
     */
    public Staff getStaff() {
        return staff;
    }
    
    /**
     * Get the name of the staff member associated with this expense.
     *
     * @return The staff name
     */
    public String getStaffName() {
        return staff != null ? staff.getName() : "";
    }
    
    /**
     * Get the position of the staff member associated with this expense.
     *
     * @return The staff position
     */
    public String getStaffPosition() {
        return staff != null ? staff.getPosition() : "";
    }
}
