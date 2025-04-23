package Repository;

import Model.Category;
import Support.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for handling database operations related to categories.
 */
public class CategoryRepository {

    /**
     * Get all categories from the database.
     * 
     * @return List of categories
     */
    public List<Category> getAllCategories() {
        return DB.unsafeExecute(
            connection -> {
                List<Category> categories = new ArrayList<>();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT CatID, CatName FROM category ORDER BY CatName");
                
                while (rs.next()) {
                    categories.add(
                        new Category(
                            rs.getInt("CatID"),
                            rs.getString("CatName")
                        )
                    );
                }
                
                return categories;
            }
        );
    }
    
    /**
     * Get a category by ID.
     * 
     * @param categoryId The category ID
     * @return Optional containing the category if found, empty otherwise
     */
    public Optional<Category> getCategoryById(int categoryId) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT CatID, CatName FROM category WHERE CatID = ?"
                );
                stmt.setInt(1, categoryId);
                ResultSet rs = stmt.executeQuery();
                
                if (!rs.next()) {
                    return Optional.empty();
                }
                
                return Optional.of(
                    new Category(
                        rs.getInt("CatID"),
                        rs.getString("CatName")
                    )
                );
            }
        );
    }
    
    /**
     * Create a new category.
     * 
     * @param name Category name
     * @return The created category
     */
    public Category createCategory(String name) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO category (CatName) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                stmt.setString(1, name);
                
                int affectedRows = stmt.executeUpdate();
                
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return new Category(
                        generatedKeys.getInt(1),
                        name
                    );
                } else {
                    return null;
                }
            }
        );
    }
    
    /**
     * Update an existing category.
     * 
     * @param category The category to update
     * @return True if the update was successful, false otherwise
     */
    public boolean updateCategory(Category category) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE category SET CatName = ? WHERE CatID = ?"
                );
                stmt.setString(1, category.getName());
                stmt.setInt(2, category.getId());
                
                return stmt.executeUpdate() > 0;
            }
        );
    }
    
    /**
     * Delete a category.
     * 
     * @param categoryId The ID of the category to delete
     * @return True if the deletion was successful, false otherwise
     */
    public boolean deleteCategory(int categoryId) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM category WHERE CatID = ?"
                );
                stmt.setInt(1, categoryId);
                
                return stmt.executeUpdate() > 0;
            }
        );
    }
    
    /**
     * Check if a category with the given name exists.
     * 
     * @param name The category name to check
     * @return True if a category with the name exists, false otherwise
     */
    public boolean categoryNameExists(String name) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM category WHERE CatName = ?"
                );
                stmt.setString(1, name);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                
                return rs.getInt(1) > 0;
            }
        );
    }
} 