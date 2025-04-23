package Migrations;

import Support.Migration;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Migration to create the sale details table.
 */
public class M007_CreateSaleDetailsTable implements Migration {

    @Override
    public String getName() {
        return "007_create_sales_details_table";
    }

    @Override
    public void up(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS sale_details (" +
                "ID INTEGER," +
                "PID INTEGER," +
                "Qty INTEGER NOT NULL," +
                "PRIMARY KEY (ID, PID)," +
                "FOREIGN KEY (ID) REFERENCES sales(ID) ON DELETE CASCADE," +
                "FOREIGN KEY (PID) REFERENCES product(PID) ON DELETE CASCADE" +
                ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

            // Insert some sample sale details for the sample sales
            String[] sampleSaleDetails = {
                    // Sale 1 details (total 1299.98)
                    "INSERT OR IGNORE INTO sale_details (ID, PID, Qty) VALUES (1, 1, 1);", // Smartphone 599.99
                    "INSERT OR IGNORE INTO sale_details (ID, PID, Qty) VALUES (1, 2, 1);", // Laptop 999.99
                    
                    // Sale 2 details (total 89.97)
                    "INSERT OR IGNORE INTO sale_details (ID, PID, Qty) VALUES (2, 3, 3);", // T-Shirt 19.99 x 3
                    "INSERT OR IGNORE INTO sale_details (ID, PID, Qty) VALUES (2, 5, 1);", // Novel 12.99
                    
                    // Sale 3 details (total 149.97)
                    "INSERT OR IGNORE INTO sale_details (ID, PID, Qty) VALUES (3, 3, 2);", // T-Shirt 19.99 x 2
                    "INSERT OR IGNORE INTO sale_details (ID, PID, Qty) VALUES (3, 7, 1);", // Coffee Maker 79.99
                    "INSERT OR IGNORE INTO sale_details (ID, PID, Qty) VALUES (3, 5, 2);"  // Novel 12.99 x 2
            };

            for (String detailsSql : sampleSaleDetails) {
                stmt.execute(detailsSql);
            }

            System.out.println("Sale details table created successfully with sample data");
        }
    }
} 