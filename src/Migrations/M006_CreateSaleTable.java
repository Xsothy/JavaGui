package Migrations;

import Support.Migration;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Migration to create the sale table.
 */
public class M006_CreateSaleTable implements Migration {

    @Override
    public String getName() {
        return "006_create_sale_table";
    }

    @Override
    public void up(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS sales (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Date TEXT NOT NULL," +  // SQLite stores dates as TEXT, REAL, or INTEGER
                "Total REAL NOT NULL," +
                "SID INTEGER," +
                "FOREIGN KEY (SID) REFERENCES staff(ID) ON DELETE SET NULL" +
                ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

            // Current date for sample data
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate = dateFormat.format(new Date());
            
            // Insert some sample sales
            String[] sampleSales = {
                    "INSERT OR IGNORE INTO sales (Date, Total, SID) VALUES ('" + currentDate + "', 1299.98, 1);",
                    "INSERT OR IGNORE INTO sales (Date, Total, SID) VALUES ('" + currentDate + "', 89.97, 2);",
                    "INSERT OR IGNORE INTO sales (Date, Total, SID) VALUES ('" + currentDate + "', 149.97, 1);"
            };

            for (String saleSql : sampleSales) {
                stmt.execute(saleSql);
            }

            System.out.println("Sale table created successfully with sample data");
        }
    }
} 