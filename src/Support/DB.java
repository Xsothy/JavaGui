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


      public void closeConnection() throws SQLException {
            if (connection != null && !connection.isClosed()) {
                  connection.close();
                  connection = null;
            }
      }

      public static <T> T execute(DatabaseOperation<T> operation) throws SQLException {
            Connection conn = getInstance().getConnection();
            T result = operation.execute(conn);
            getInstance().closeConnection();
            return result;
      }

      public static <T> T unsafeExecute(DatabaseOperation<T> operation) {
          try {
              return execute(operation);
          } catch (SQLException e) {
              throw new RuntimeException(e);
          }
      }

      @FunctionalInterface
      public interface DatabaseOperation<T> {
            T execute(Connection connection) throws SQLException;
      }
}
