package Model;

/**
 * Model class representing a sale detail (line item).
 */
public class SaleDetail {
    private int saleId;
    private int productId;
    private int quantity;
    
    /**
     * Constructor for creating a SaleDetail.
     * 
     * @param saleId The sale ID
     * @param productId The product ID
     * @param quantity The quantity sold
     */
    public SaleDetail(int saleId, int productId, int quantity) {
        this.saleId = saleId;
        this.productId = productId;
        this.quantity = quantity;
    }
    
    /**
     * Get the sale ID.
     * 
     * @return The sale ID
     */
    public int getSaleId() {
        return saleId;
    }
    
    /**
     * Set the sale ID.
     * 
     * @param saleId The sale ID
     */
    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
    
    /**
     * Get the product ID.
     * 
     * @return The product ID
     */
    public int getProductId() {
        return productId;
    }
    
    /**
     * Set the product ID.
     * 
     * @param productId The product ID
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    /**
     * Get the quantity.
     * 
     * @return The quantity
     */
    public int getQuantity() {
        return quantity;
    }
    
    /**
     * Set the quantity.
     * 
     * @param quantity The quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public String toString() {
        return "SaleDetail{" + 
                "saleId=" + saleId + 
                ", productId=" + productId + 
                ", quantity=" + quantity + 
                '}';
    }
} 