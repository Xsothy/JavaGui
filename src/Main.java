import Support.DB;
import Support.Response;
import java.io.File;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Define the database file path
        String dbFilePath = "C:\\Users\\Saloeun\\OneDrive\\Documents\\NetBeansProjects\\Testing_Java\\database.db";
        
        // Check if the database file exists
        File dbFile = new File(dbFilePath);
        if (!dbFile.exists()) {
            System.out.println("Database file does not exist. It will be created.");
        }
        
        // Initialize the DB connection with SQLite
        DB.initialize(
                "jdbc:sqlite:" + dbFilePath,  // SQLite connection string
                "",  // SQLite doesn't require username
                ""   // SQLite doesn't require password
        );
        
        // Test the connection and create the database file if it doesn't exist
        Response<Connection> conn = DB.getInstance().getConnection();
        if (conn.isSuccess()) {
            System.out.println("Database connection successful. Database file is ready.");
        } else {
            System.err.println("Error connecting to database: " + conn.getMessage());
        }

        new Login().setVisible(true);
    }
}
