package Support;

import java.sql.*;

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
       * Get singleton instance of Container
       */
      public static DB getInstance() {
            if (instance == null) {
                  instance = new DB();
            }
            return instance;
      }

      /**
       * Get database connection with error handling
       * @return Response object with connection data or error message
       */
      public Response<Connection> getConnection() {
            try {
                  
                  // Check if we already have a valid connection
                  if (connection != null && !connection.isClosed()) {
                        return Response.success("Database connection retrieved successfully", connection);
                  }

                  // Check if connection parameters are set
                  if (url == null || username == null || password == null) {
                        return Response.error("Database connection parameters not initialized");
                  }
                  // Create new connection
                  Class.forName("org.sqlite.JDBC");
                  connection = DriverManager.getConnection(url, username, password);
                  return Response.success("Database connection established successfully", connection);
            } catch (SQLException e) {
                  return Response.error("Failed to connect to database: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
      }

      /**
       * Close the database connection safely
       * @return Response indicating success or failure
       */
      public Response<Void> closeConnection() {
            try {
                  if (connection != null && !connection.isClosed()) {
                        connection.close();
                        connection = null;
                        return Response.success("Database connection closed successfully", null);
                  }
                  return Response.success("No active connection to close", null);
            } catch (SQLException e) {
                  return Response.error("Error closing database connection: " + e.getMessage());
            }
      }

      /**
       * Execute a database operation with automatic error handling
       * @param operation The database operation to execute
       * @return Response with the operation result or error message
       */
      public <T> Response<T> execute(DB.DatabaseOperation<T> operation) {
            Response<Connection> connectionResponse = getConnection();
            if (!connectionResponse.isSuccess()) {
                  return Response.error(connectionResponse.getMessage());
            }

            try {
                  T result = operation.execute(connectionResponse.getData());
                  return Response.success("Operation executed successfully", result);
            } catch (SQLException e) {
                  return Response.error("Database operation failed: " + e.getMessage());
            }
      }

      /**
       * Functional interface for database operations
       */
      @FunctionalInterface
      public interface DatabaseOperation<T> {
            T execute(Connection connection) throws SQLException;
      }
}
