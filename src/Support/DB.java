package Support;

import java.sql.*;

/**
 * Database connection manager.
 * Implements the singleton pattern to provide a single database connection throughout the application.
 */
public class DB
{
      private static DB instance;
      private Connection connection;
      private String url;
      private String username;
      private String password;


      // Private constructor to enforce singleton pattern
      private DB() {

      }

      /**
       * Initialize database connection parameters
       */
      public static void initialize(String url, String username, String password) {
            DB container = getInstance();
            container.url = url;
            container.username = username;
            container.password = password;
      }

      /**
       * Get singleton instance of DB
       */
      public static DB getInstance() {
            if (instance == null) {
                  instance = new DB();
            }
            return instance;
      }

      /**
       * Get database connection with error handling
       * 
       * @return The database connection
       * @throws SQLException If a database error occurs
       * @throws IllegalStateException If connection parameters are not initialized
       */
      public Connection getConnection() throws SQLException, IllegalStateException {
            // Check if we already have a valid connection
            if (connection != null && !connection.isClosed()) {
                  return connection;
            }

            // Check if connection parameters are set
            if (url == null || username == null || password == null) {
                  throw new IllegalStateException("Database connection parameters not initialized");
            }

            // Create new connection
            connection = DriverManager.getConnection(url, username, password);
            return connection;
      }

      /**
       * Close the database connection safely
       * 
       * @throws SQLException If a database error occurs
       */
      public void closeConnection() throws SQLException {
            if (connection != null && !connection.isClosed()) {
                  connection.close();
                  connection = null;
            }
      }

      /**
       * Execute a database operation with automatic error handling
       * 
       * @param operation The database operation to execute
       * @return The result of the operation
       * @throws SQLException If a database error occurs
       * @throws IllegalStateException If connection parameters are not initialized
       */
      public <T> T execute(DatabaseOperation<T> operation) throws SQLException, IllegalStateException {
            Connection conn = getConnection();
            return operation.execute(conn);
      }

      /**
       * Functional interface for database operations
       */
      @FunctionalInterface
      public interface DatabaseOperation<T> {
            T execute(Connection connection) throws SQLException;
      }
}
