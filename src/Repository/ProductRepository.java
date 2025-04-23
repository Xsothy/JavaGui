package Repository;

import Model.Category;
import Model.Product;
import Model.ProductWithCategory;
import Support.DB;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for handling database operations related to products.
 */
public class ProductRepository {
    private final CategoryRepository categoryRepository;
    
    /**
     * Constructor for ProductRepository.
     */
    public ProductRepository() {
        this.categoryRepository = new CategoryRepository();
    }
    
    /**
     * Get all products from the database with their categories.
     * 
     * @return List of products with categories
     */
    public List<ProductWithCategory> getAllProductsWithCategories() {
        return DB.unsafeExecute(
            connection -> {
                List<ProductWithCategory> products = new ArrayList<>();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(
                    "SELECT p.PID, p.PName, p.Sqty, p.Price, p.Image, p.CatID, c.CatName " +
                    "FROM product p " +
                    "LEFT JOIN category c ON p.CatID = c.CatID " +
                    "ORDER BY p.PName"
                );
                
                while (rs.next()) {
                    int categoryId = rs.getInt("CatID");
                    Category category = categoryId > 0 ? 
                        new Category(categoryId, rs.getString("CatName")) : null;
                    
                    Product product = new Product(
                        rs.getInt("PID"),
                        rs.getString("PName"),
                        rs.getInt("Sqty"),
                        rs.getBigDecimal("Price"),
                        rs.getString("Image"),
                        categoryId
                    );
                    
                    products.add(new ProductWithCategory(product, category));
                }
                
                return products;
            }
        );
    }
    
    /**
     * Get all products from the database.
     * 
     * @return List of products
     */
    public List<Product> getAllProducts() {
        return DB.unsafeExecute(
            connection -> {
                List<Product> products = new ArrayList<>();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(
                    "SELECT PID, PName, Sqty, Price, Image, CatID FROM product ORDER BY PName"
                );
                
                while (rs.next()) {
                    products.add(
                        new Product(
                            rs.getInt("PID"),
                            rs.getString("PName"),
                            rs.getInt("Sqty"),
                            rs.getBigDecimal("Price"),
                            rs.getString("Image"),
                            rs.getInt("CatID")
                        )
                    );
                }
                
                return products;
            }
        );
    }
    
    /**
     * Get products by category ID.
     * 
     * @param categoryId The category ID
     * @return List of products in the category
     */
    public List<Product> getProductsByCategory(int categoryId) {
        return DB.unsafeExecute(
            connection -> {
                List<Product> products = new ArrayList<>();
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT PID, PName, Sqty, Price, Image, CatID " +
                    "FROM product WHERE CatID = ? ORDER BY PName"
                );
                stmt.setInt(1, categoryId);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    products.add(
                        new Product(
                            rs.getInt("PID"),
                            rs.getString("PName"),
                            rs.getInt("Sqty"),
                            rs.getBigDecimal("Price"),
                            rs.getString("Image"),
                            rs.getInt("CatID")
                        )
                    );
                }
                
                return products;
            }
        );
    }
    
    /**
     * Get a product by ID.
     * 
     * @param productId The product ID
     * @return Optional containing the product if found, empty otherwise
     */
    public Optional<Product> getProductById(int productId) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT PID, PName, Sqty, Price, Image, CatID FROM product WHERE PID = ?"
                );
                stmt.setInt(1, productId);
                ResultSet rs = stmt.executeQuery();
                
                if (!rs.next()) {
                    return Optional.empty();
                }
                
                return Optional.of(
                    new Product(
                        rs.getInt("PID"),
                        rs.getString("PName"),
                        rs.getInt("Sqty"),
                        rs.getBigDecimal("Price"),
                        rs.getString("Image"),
                        rs.getInt("CatID")
                    )
                );
            }
        );
    }
    
    /**
     * Get a product with its category by ID.
     * 
     * @param productId The product ID
     * @return Optional containing the product with category if found, empty otherwise
     */
    public Optional<ProductWithCategory> getProductWithCategoryById(int productId) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT p.PID, p.PName, p.Sqty, p.Price, p.Image, p.CatID, c.CatName " +
                    "FROM product p " +
                    "LEFT JOIN category c ON p.CatID = c.CatID " +
                    "WHERE p.PID = ?"
                );
                stmt.setInt(1, productId);
                ResultSet rs = stmt.executeQuery();
                
                if (!rs.next()) {
                    return Optional.empty();
                }
                
                int categoryId = rs.getInt("CatID");
                Category category = categoryId > 0 ? 
                    new Category(categoryId, rs.getString("CatName")) : null;
                
                Product product = new Product(
                    rs.getInt("PID"),
                    rs.getString("PName"),
                    rs.getInt("Sqty"),
                    rs.getBigDecimal("Price"),
                    rs.getString("Image"),
                    categoryId
                );
                
                return Optional.of(new ProductWithCategory(product, category));
            }
        );
    }
    
    /**
     * Create a new product.
     * 
     * @param name Product name
     * @param stockQuantity Stock quantity
     * @param price Price
     * @param imagePath Image path
     * @param categoryId Category ID
     * @return The created product
     */
    public Product createProduct(String name, int stockQuantity, BigDecimal price, String imagePath, int categoryId) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO product (PName, Sqty, Price, Image, CatID) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                stmt.setString(1, name);
                stmt.setInt(2, stockQuantity);
                stmt.setBigDecimal(3, price);
                stmt.setString(4, imagePath);
                stmt.setInt(5, categoryId);
                
                int affectedRows = stmt.executeUpdate();
                
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return new Product(
                        generatedKeys.getInt(1),
                        name,
                        stockQuantity,
                        price,
                        imagePath,
                        categoryId
                    );
                } else {
                    return null;
                }
            }
        );
    }
    
    /**
     * Update an existing product.
     * 
     * @param product The product to update
     * @return True if the update was successful, false otherwise
     */
    public boolean updateProduct(Product product) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE product SET PName = ?, Sqty = ?, Price = ?, Image = ?, CatID = ? WHERE PID = ?"
                );
                stmt.setString(1, product.getName());
                stmt.setInt(2, product.getStockQuantity());
                stmt.setBigDecimal(3, product.getPrice());
                stmt.setString(4, product.getImagePath());
                stmt.setInt(5, product.getCategoryId());
                stmt.setInt(6, product.getId());
                
                return stmt.executeUpdate() > 0;
            }
        );
    }
    
    /**
     * Delete a product.
     * 
     * @param productId The ID of the product to delete
     * @return True if the deletion was successful, false otherwise
     */
    public boolean deleteProduct(int productId) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM product WHERE PID = ?"
                );
                stmt.setInt(1, productId);
                
                return stmt.executeUpdate() > 0;
            }
        );
    }
    
    /**
     * Search for products by name.
     * 
     * @param searchTerm The search term
     * @return List of products matching the search term
     */
    public List<ProductWithCategory> searchProducts(String searchTerm) {
        return DB.unsafeExecute(
            connection -> {
                List<ProductWithCategory> products = new ArrayList<>();
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT p.PID, p.PName, p.Sqty, p.Price, p.Image, p.CatID, c.CatName " +
                    "FROM product p " +
                    "LEFT JOIN category c ON p.CatID = c.CatID " +
                    "WHERE p.PName LIKE ? " +
                    "ORDER BY p.PName"
                );
                stmt.setString(1, "%" + searchTerm + "%");
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    int categoryId = rs.getInt("CatID");
                    Category category = categoryId > 0 ? 
                        new Category(categoryId, rs.getString("CatName")) : null;
                    
                    Product product = new Product(
                        rs.getInt("PID"),
                        rs.getString("PName"),
                        rs.getInt("Sqty"),
                        rs.getBigDecimal("Price"),
                        rs.getString("Image"),
                        categoryId
                    );
                    
                    products.add(new ProductWithCategory(product, category));
                }
                
                return products;
            }
        );
    }
    
    /**
     * Update the stock quantity of a product.
     * 
     * @param productId The product ID
     * @param newQuantity The new stock quantity
     * @return True if the update was successful, false otherwise
     */
    public boolean updateProductStock(int productId, int newQuantity) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE product SET Sqty = ? WHERE PID = ?"
                );
                stmt.setInt(1, newQuantity);
                stmt.setInt(2, productId);
                
                return stmt.executeUpdate() > 0;
            }
        );
    }
} 