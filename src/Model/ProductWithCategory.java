package Model;

/**
 * Model class representing a product with its associated category information.
 */
public class ProductWithCategory {
    private final Product product;
    private final Category category;
    
    /**
     * Constructor for ProductWithCategory.
     *
     * @param product The product object
     * @param category The category object associated with this product
     */
    public ProductWithCategory(Product product, Category category) {
        this.product = product;
        this.category = category;
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
     * Get the category object.
     *
     * @return The category object
     */
    public Category getCategory() {
        return category;
    }
    
    /**
     * Get the name of the category associated with this product.
     *
     * @return The category name
     */
    public String getCategoryName() {
        return category != null ? category.getName() : "";
    }
    
    /**
     * Get the ID of the product.
     *
     * @return The product ID
     */
    public int getProductId() {
        return product.getId();
    }
    
    /**
     * Get the name of the product.
     *
     * @return The product name
     */
    public String getProductName() {
        return product.getName();
    }
} 