package Migrations;

import Support.Migration;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Migration to create the product table.
 */
public class M005_CreateProductTable implements Migration {

    @Override
    public String getName() {
        return "005_create_product_table";
    }

    @Override
    public void up(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS product (" +
                "PID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PName TEXT NOT NULL," +
                "Sqty INTEGER DEFAULT 0," +
                "Price REAL NOT NULL," +
                "Image TEXT," +
                "CatID INTEGER," +
                "FOREIGN KEY (CatID) REFERENCES category(CatID) ON DELETE SET NULL" +
                ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

            // Insert some sample products
            String[] sampleProducts = {
                    "INSERT OR IGNORE INTO product (PName, Sqty, Price, Image, CatID) VALUES ('Smartphone', 25, 599.99, 'smartphone.jpg', 1);",
                    "INSERT OR IGNORE INTO product (PName, Sqty, Price, Image, CatID) VALUES ('Laptop', 15, 999.99, 'laptop.jpg', 1);",
                    "INSERT OR IGNORE INTO product (PName, Sqty, Price, Image, CatID) VALUES ('T-Shirt', 50, 19.99, 'tshirt.jpg', 2);",
                    "INSERT OR IGNORE INTO product (PName, Sqty, Price, Image, CatID) VALUES ('Jeans', 30, 49.99, 'jeans.jpg', 2);",
                    "INSERT OR IGNORE INTO product (PName, Sqty, Price, Image, CatID) VALUES ('Novel', 40, 12.99, 'novel.jpg', 3);",
                    "INSERT OR IGNORE INTO product (PName, Sqty, Price, Image, CatID) VALUES ('Cookbook', 20, 24.99, 'cookbook.jpg', 3);",
                    "INSERT OR IGNORE INTO product (PName, Sqty, Price, Image, CatID) VALUES ('Coffee Maker', 10, 79.99, 'coffeemaker.jpg', 4);",
                    "INSERT OR IGNORE INTO product (PName, Sqty, Price, Image, CatID) VALUES ('Blender', 15, 39.99, 'blender.jpg', 4);",
                    "INSERT OR IGNORE INTO product (PName, Sqty, Price, Image, CatID) VALUES ('Basketball', 20, 29.99, 'basketball.jpg', 5);",
                    "INSERT OR IGNORE INTO product (PName, Sqty, Price, Image, CatID) VALUES ('Tennis Racket', 15, 59.99, 'tennisracket.jpg', 5);"
            };

            for (String productSql : sampleProducts) {
                stmt.execute(productSql);
            }

            System.out.println("Product table created successfully with sample products");
        }
    }
}