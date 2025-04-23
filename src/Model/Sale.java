package Model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Model class representing a sale.
 */
public class Sale {
    private int id;
    private Date date;
    private BigDecimal total;
    private int staffId;
    
    /**
     * Constructor for creating a Sale with ID.
     * 
     * @param id The sale ID
     * @param date The sale date
     * @param total The total amount
     * @param staffId The staff ID who made the sale
     */
    public Sale(int id, Date date, BigDecimal total, int staffId) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.staffId = staffId;
    }
    
    /**
     * Constructor for creating a new Sale without ID.
     * 
     * @param date The sale date
     * @param total The total amount
     * @param staffId The staff ID who made the sale
     */
    public Sale(Date date, BigDecimal total, int staffId) {
        this(-1, date, total, staffId);
    }
    
    /**
     * Get the sale ID.
     * 
     * @return The sale ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Set the sale ID.
     * 
     * @param id The sale ID
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Get the sale date.
     * 
     * @return The sale date
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Set the sale date.
     * 
     * @param date The sale date
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    /**
     * Get the total amount.
     * 
     * @return The total amount
     */
    public BigDecimal getTotal() {
        return total;
    }
    
    /**
     * Set the total amount.
     * 
     * @param total The total amount
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    /**
     * Get the staff ID.
     * 
     * @return The staff ID
     */
    public int getStaffId() {
        return staffId;
    }
    
    /**
     * Set the staff ID.
     * 
     * @param staffId The staff ID
     */
    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
    
    @Override
    public String toString() {
        return "Sale{" + 
                "id=" + id + 
                ", date=" + date + 
                ", total=" + total + 
                ", staffId=" + staffId + 
                '}';
    }
} 