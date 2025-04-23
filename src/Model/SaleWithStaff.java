package Model;

/**
 * Model class representing a sale with associated staff information.
 */
public class SaleWithStaff {
    private final Sale sale;
    private final Staff staff;
    
    /**
     * Constructor for SaleWithStaff.
     *
     * @param sale The sale object
     * @param staff The staff object associated with this sale
     */
    public SaleWithStaff(Sale sale, Staff staff) {
        this.sale = sale;
        this.staff = staff;
    }
    
    /**
     * Get the sale object.
     *
     * @return The sale object
     */
    public Sale getSale() {
        return sale;
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
     * Get the name of the staff member associated with this sale.
     *
     * @return The staff name
     */
    public String getStaffName() {
        return staff != null ? staff.getName() : "";
    }
    
    /**
     * Get the position of the staff member associated with this sale.
     *
     * @return The staff position
     */
    public String getStaffPosition() {
        return staff != null ? staff.getPosition() : "";
    }
} 