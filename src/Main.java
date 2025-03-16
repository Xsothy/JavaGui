import Support.DB;
import Support.MigrationManager;
import java.io.File;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Define the database file path
        String dbFilePath = "database.db";
        
        // Check if the database file exists
        File dbFile = new File(dbFilePath);
        if (!dbFile.exists()) {
            System.out.println("Database file does not exist. It will be created.");
        }
        
        // Load the SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
            return; // Exit if driver not found
        }
        
        // Initialize the DB connection with SQLite
        DB.initialize(
                "jdbc:sqlite:" + dbFilePath,  // SQLite connection string
                "",  // SQLite doesn't require username
                ""   // SQLite doesn't require password
        );
        
        // Test the connection and create the database file if it doesn't exist
        try {
            Connection conn = DB.getInstance().getConnection().getData();
            System.out.println("Database connection successful. Database file is ready.");
            
            // Run migrations
            MigrationManager migrationManager = new MigrationManager(conn);
            migrationManager.migrate();
            
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
        
        new Login().setVisible(true);
    }
}
