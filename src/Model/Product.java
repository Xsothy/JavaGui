package Model;

import java.math.BigDecimal;

/**
 * Model class representing a product.
 */
public class Product {
    private int id;
    private String name;
    private int stockQuantity;
    private BigDecimal price;
    private String imagePath;
    private int categoryId;
    
    /**
     * Constructor for creating a Product with ID.
     * 
     * @param id Product ID
     * @param name Product name
     * @param stockQuantity Stock quantity
     * @param price Price
     * @param imagePath Image path
     * @param categoryId Category ID
     */
    public Product(int id, String name, int stockQuantity, BigDecimal price, String imagePath, int categoryId) {
        this.id = id;
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.imagePath = imagePath;
        this.categoryId = categoryId;
    }
    
    /**
     * Constructor for creating a new Product without ID.
     * 
     * @param name Product name
     * @param stockQuantity Stock quantity
     * @param price Price
     * @param imagePath Image path
     * @param categoryId Category ID
     */
    public Product(String name, int stockQuantity, BigDecimal price, String imagePath, int categoryId) {
        this(-1, name, stockQuantity, price, imagePath, categoryId);
    }
    
    /**
     * Get the product ID.
     * 
     * @return The product ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Set the product ID.
     * 
     * @param id The product ID
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Get the product name.
     * 
     * @return The product name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the product name.
     * 
     * @param name The product name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get the stock quantity.
     * 
     * @return The stock quantity
     */
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    /**
     * Set the stock quantity.
     * 
     * @param stockQuantity The stock quantity
     */
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    /**
     * Get the price.
     * 
     * @return The price
     */
    public BigDecimal getPrice() {
        return price;
    }
    
    /**
     * Set the price.
     * 
     * @param price The price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    /**
     * Get the image path.
     * 
     * @return The image path
     */
    public String getImagePath() {
        return imagePath;
    }
    
    /**
     * Set the image path.
     * 
     * @param imagePath The image path
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    /**
     * Get the category ID.
     * 
     * @return The category ID
     */
    public int getCategoryId() {
        return categoryId;
    }
    
    /**
     * Set the category ID.
     * 
     * @param categoryId The category ID
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    @Override
    public String toString() {
        return "Product{" + 
                "id=" + id + 
                ", name='" + name + '\'' + 
                ", stockQuantity=" + stockQuantity + 
                ", price=" + price + 
                ", categoryId=" + categoryId + 
                '}';
    }
} 