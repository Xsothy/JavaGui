package Controller;

import Model.Category;
import Repository.CategoryRepository;
import Support.Router;
import View.Dashboard.CategoryDetailsPanel;
import View.Dashboard.CategoryFormPanel;
import View.NavigatePanel;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 * Controller class for managing categories.
 */
public class CategoryController {
    private final CategoryRepository categoryRepository;
    
    /**
     * Constructor for CategoryController.
     */
    public CategoryController() {
        this.categoryRepository = new CategoryRepository();
    }
    
    /**
     * Get all categories.
     * 
     * @return List of all categories
     */
    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }
    
    /**
     * Create a new category.
     * 
     * @param name Category name
     * @return The created category or null if creation failed
     */
    public Category createCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        
        if (categoryRepository.categoryNameExists(name.trim())) {
            throw new IllegalArgumentException("A category with this name already exists");
        }
        
        return categoryRepository.createCategory(name.trim());
    }
    
    /**
     * Update an existing category.
     * 
     * @param category The category to update
     * @return True if the update was successful, false otherwise
     */
    public boolean updateCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        
        if (category.getId() <= 0) {
            throw new IllegalArgumentException("Invalid category ID");
        }
        
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        
        String name = category.getName().trim();
        
        // Check if another category already has this name
        Optional<Category> existingCategory = getCategoryById(category.getId());
        if (existingCategory.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        
        if (!existingCategory.get().getName().equals(name) && categoryRepository.categoryNameExists(name)) {
            throw new IllegalArgumentException("A category with this name already exists");
        }
        
        return categoryRepository.updateCategory(category);
    }
    
    /**
     * Delete a category.
     * 
     * @param categoryId The ID of the category to delete
     * @return True if the deletion was successful, false otherwise
     */
    public boolean deleteCategory(int categoryId) {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Invalid category ID");
        }
        
        Optional<Category> category = getCategoryById(categoryId);
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        
        return categoryRepository.deleteCategory(categoryId);
    }
    
    /**
     * Get a category by ID.
     * 
     * @param categoryId The category ID
     * @return Optional containing the category if found, empty otherwise
     */
    public Optional<Category> getCategoryById(int categoryId) {
        if (categoryId <= 0) {
            return Optional.empty();
        }
        
        return categoryRepository.getCategoryById(categoryId);
    }
    
    /**
     * Show category details panel.
     *
     * @param params Route parameters
     * @return The category details panel
     */
    public NavigatePanel show(Map<String, String> params) {
        int categoryId = Integer.parseInt(params.get("id"));

        Optional<Category> categoryOpt = getCategoryById(categoryId);
        if (categoryOpt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Category not found", "Error", JOptionPane.ERROR_MESSAGE);
            return new NavigatePanel();
        }

        return new CategoryDetailsPanel(categoryOpt.get());
    }
    
    /**
     * Show category edit form.
     * 
     * @param params Route parameters
     * @return The category form panel
     */
    public NavigatePanel edit(Map<String, String> params) {
        int categoryId = Integer.parseInt(params.get("id"));
        
        Optional<Category> categoryOpt = getCategoryById(categoryId);
        if (categoryOpt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Category not found", "Error", JOptionPane.ERROR_MESSAGE);
            return new NavigatePanel();
        }
        
        return new CategoryFormPanel(categoryOpt.get());
    }
} 