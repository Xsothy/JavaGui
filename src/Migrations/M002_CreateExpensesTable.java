package Migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Migration to create the expenses table
 */
public class M002_CreateExpensesTable implements Migration {
    
    @Override
    public String getName() {
        return "002_create_expenses_table";
    }
    
    @Override
    public void up(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS expenses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "amount REAL NOT NULL," +
                    "picture TEXT," +
                    "staff_id INTEGER NOT NULL," +
                    "FOREIGN KEY (staff_id) REFERENCES staff(id)" +
                    ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Expenses table created successfully");
        }
    }
} 