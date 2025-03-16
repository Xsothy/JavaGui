package Migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Migration to create the staff table
 */
public class M001_CreateStaffTable implements Migration {
    
    @Override
    public String getName() {
        return "001_create_staff_table";
    }
    
    @Override
    public void up(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS staff (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "position TEXT," +
                    "username TEXT UNIQUE," +
                    "password TEXT NOT NULL," +
                    "role TEXT" +
                    ")";

        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Staff table created successfully");
        }
    }
}