package Model;

/**
 * Model class representing a sale detail with associated product information.
 */
public class SaleDetailWithProduct {
    private final SaleDetail saleDetail;
    private final Product product;
    
    /**
     * Constructor for SaleDetailWithProduct.
     *
     * @param saleDetail The sale detail object
     * @param product The product object associated with this sale detail
     */
    public SaleDetailWithProduct(SaleDetail saleDetail, Product product) {
        this.saleDetail = saleDetail;
        this.product = product;
    }
    
    /**
     * Get the sale detail object.
     *
     * @return The sale detail object
     */
    public SaleDetail getSaleDetail() {
        return saleDetail;
    }
    
    /**
     * Get the product object.
     *
     * @return The product object
     */
    public Product getProduct() {
        return product;
    }
    
    /**
     * Get the product name.
     *
     * @return The product name
     */
    public String getProductName() {
        return product != null ? product.getName() : "";
    }
    
    /**
     * Get the product price.
     *
     * @return The product price
     */
    public String getProductPrice() {
        return product != null ? product.getPrice().toString() : "";
    }
    
    /**
     * Get the line total (quantity * price).
     *
     * @return The line total
     */
    public java.math.BigDecimal getLineTotal() {
        if (product != null) {
            return product.getPrice().multiply(new java.math.BigDecimal(saleDetail.getQuantity()));
        }
        return java.math.BigDecimal.ZERO;
    }
} 