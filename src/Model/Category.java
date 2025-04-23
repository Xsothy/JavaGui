package Model;

/**
 * Model class representing a product category.
 */
public class Category {
    private int id;
    private String name;
    
    /**
     * Constructor for creating a Category with ID.
     * 
     * @param id Category ID
     * @param name Category name
     */
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * Constructor for creating a new Category without ID.
     * 
     * @param name Category name
     */
    public Category(String name) {
        this(-1, name);
    }
    
    /**
     * Get the category ID.
     * 
     * @return The category ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Set the category ID.
     * 
     * @param id The category ID
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Get the category name.
     * 
     * @return The category name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the category name.
     * 
     * @param name The category name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
} 