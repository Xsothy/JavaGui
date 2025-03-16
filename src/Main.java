import Support.DB;
import Support.MigrationManager;
import java.io.File;
import java.sql.Connection;

/**
 * Main entry point for the application.
 * Initializes the database connection and runs migrations before starting the UI.
 */
public class Main {
    // SQLite database file path
    private static final String DB_FILE_PATH = "database.db";
    
    public static void main(String[] args) {
        System.out.println("Starting application...");
        
        // Initialize the database
        if (initializeDatabase()) {
            // Start the UI
            new Login().setVisible(true);
        } else {
            System.err.println("Failed to initialize database. Application cannot start.");
        }
    }
    
    /**
     * Initializes the database connection and runs migrations.
     * 
     * @return true if initialization was successful, false otherwise
     */
    private static boolean initializeDatabase() {
        // Check if the database file exists
        File dbFile = new File(DB_FILE_PATH);
        if (!dbFile.exists()) {
            System.out.println("Database file does not exist. It will be created.");
        }
        
        // Load the SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
            return false; // Exit if driver not found
        }
        
        // Initialize the DB connection with SQLite
        DB.initialize(
                "jdbc:sqlite:" + DB_FILE_PATH,  // SQLite connection string
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
            
            return true;
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
