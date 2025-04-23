package Migrations;

import Support.Migration;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Migration to create the category table.
 */
public class M004_CreateCategoryTable implements Migration {

    @Override
    public String getName() {
        return "004_create_category_table";
    }

    @Override
    public void up(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS category (" +
                    "CatID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "CatName TEXT NOT NULL UNIQUE" +
                    ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            
            // Insert some default categories
            String[] defaultCategories = {
                "Electronics", "Clothing", "Books", "Home & Kitchen", "Sports", "Toys", "Beauty", "Food", "Other"
            };
            
            for (String category : defaultCategories) {
                String insertSql = "INSERT OR IGNORE INTO category (CatName) VALUES ('" + category + "');";
                stmt.execute(insertSql);
            }
            
            System.out.println("Category table created successfully with default categories");
        }
    }
} 