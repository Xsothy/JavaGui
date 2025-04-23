package Controller;

import Model.Product;
import Model.ProductWithCategory;
import Repository.ProductRepository;
import Support.Router;
import View.Dashboard.ProductDetailsPanel;
import View.Dashboard.ProductFormPanel;
import View.NavigatePanel;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 * Controller class for managing products.
 */
public class ProductController {
    private final ProductRepository productRepository;
    private final CategoryController categoryController;
    
    /**
     * Constructor for ProductController.
     */
    public ProductController() {
        this.productRepository = new ProductRepository();
        this.categoryController = new CategoryController();
    }
    
    /**
     * Get all products with their categories.
     * 
     * @return List of products with categories
     */
    public List<ProductWithCategory> getAllProductsWithCategories() {
        return productRepository.getAllProductsWithCategories();
    }
    
    /**
     * Get all products.
     * 
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }
    
    /**
     * Get products by category ID.
     * 
     * @param categoryId The category ID
     * @return List of products in the category
     */
    public List<Product> getProductsByCategory(int categoryId) {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Invalid category ID");
        }
        
        return productRepository.getProductsByCategory(categoryId);
    }
    
    /**
     * Get a product by ID.
     * 
     * @param productId The product ID
     * @return Optional containing the product if found, empty otherwise
     */
    public Optional<Product> getProductById(int productId) {
        if (productId <= 0) {
            return Optional.empty();
        }
        
        return productRepository.getProductById(productId);
    }
    
    /**
     * Get a product with its category by ID.
     * 
     * @param productId The product ID
     * @return Optional containing the product with category if found, empty otherwise
     */
    public Optional<ProductWithCategory> getProductWithCategoryById(int productId) {
        if (productId <= 0) {
            return Optional.empty();
        }
        
        return productRepository.getProductWithCategoryById(productId);
    }
    
    /**
     * Create a new product.
     * 
     * @param name Product name
     * @param stockQuantity Stock quantity
     * @param price Price
     * @param imagePath Image path
     * @param categoryId Category ID
     * @return The created product or null if creation failed
     */
    public Product createProduct(String name, int stockQuantity, BigDecimal price, String imagePath, int categoryId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be a non-negative number");
        }
        
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Invalid category ID");
        }
        
        // Check if the category exists
        if (categoryController.getCategoryById(categoryId).isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        
        return productRepository.createProduct(name.trim(), stockQuantity, price, imagePath, categoryId);
    }
    
    /**
     * Update an existing product.
     * 
     * @param product The product to update
     * @return True if the update was successful, false otherwise
     */
    public boolean updateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        
        if (product.getId() <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be a non-negative number");
        }
        
        if (product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        
        if (product.getCategoryId() <= 0) {
            throw new IllegalArgumentException("Invalid category ID");
        }
        
        // Check if the category exists
        if (categoryController.getCategoryById(product.getCategoryId()).isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        
        // Check if the product exists
        Optional<Product> existingProduct = getProductById(product.getId());
        if (existingProduct.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }
        
        return productRepository.updateProduct(product);
    }
    
    /**
     * Delete a product.
     * 
     * @param productId The ID of the product to delete
     * @return True if the deletion was successful, false otherwise
     */
    public boolean deleteProduct(int productId) {
        if (productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        
        Optional<Product> product = getProductById(productId);
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }
        
        return productRepository.deleteProduct(productId);
    }
    
    /**
     * Search for products by name.
     * 
     * @param searchTerm The search term
     * @return List of products matching the search term
     */
    public List<ProductWithCategory> searchProducts(String searchTerm) {
        if (searchTerm == null) {
            return getAllProductsWithCategories();
        }
        
        return productRepository.searchProducts(searchTerm.trim());
    }
    
    /**
     * Update the stock quantity of a product.
     * 
     * @param productId The product ID
     * @param newQuantity The new stock quantity
     * @return True if the update was successful, false otherwise
     */
    public boolean updateProductStock(int productId, int newQuantity) {
        if (productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        
        Optional<Product> product = getProductById(productId);
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }
        
        return productRepository.updateProductStock(productId, newQuantity);
    }
    
    /**
     * Show product details panel.
     * 
     * @param params Route parameters
     * @return The product details panel
     */
    public NavigatePanel show(Map<String, String> params) {
        int productId = Integer.parseInt(params.get("id"));
        
        Optional<ProductWithCategory> productOpt = getProductWithCategoryById(productId);
        if (productOpt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
            return new NavigatePanel();
        }
        
        return new ProductDetailsPanel(productOpt.get());
    }
    
    /**
     * Show product edit form.
     * 
     * @param params Route parameters
     * @return The product form panel
     */
    public NavigatePanel edit(Map<String, String> params) {
        int productId = Integer.parseInt(params.get("id"));
        
        Optional<Product> productOpt = getProductById(productId);
        if (productOpt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
            return new NavigatePanel();
        }
        
        return new ProductFormPanel(productOpt.get());
    }
} 