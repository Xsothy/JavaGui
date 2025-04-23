package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a sale with its details and staff information.
 */
public class SaleWithDetails {
    private final Sale sale;
    private final Staff staff;
    private final List<SaleDetailWithProduct> saleDetails;
    
    /**
     * Constructor for SaleWithDetails.
     *
     * @param sale The sale object
     * @param staff The staff object
     * @param saleDetails The list of sale details with product information
     */
    public SaleWithDetails(Sale sale, Staff staff, List<SaleDetailWithProduct> saleDetails) {
        this.sale = sale;
        this.staff = staff;
        this.saleDetails = saleDetails != null ? saleDetails : new ArrayList<>();
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
     * Get the sale details.
     *
     * @return The list of sale details with product information
     */
    public List<SaleDetailWithProduct> getSaleDetails() {
        return saleDetails;
    }
    
    /**
     * Get the staff name.
     *
     * @return The staff name
     */
    public String getStaffName() {
        return staff != null ? staff.getName() : "";
    }
    
    /**
     * Get the total number of items in the sale.
     *
     * @return The total number of items
     */
    public int getTotalItems() {
        int total = 0;
        for (SaleDetailWithProduct detail : saleDetails) {
            total += detail.getSaleDetail().getQuantity();
        }
        return total;
    }
    
    /**
     * Get the number of unique products in the sale.
     *
     * @return The number of unique products
     */
    public int getUniqueProducts() {
        return saleDetails.size();
    }
} 