package Support;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for database migrations
 */
public interface Migration {
    /**
     * Get the name of the migration
     * @return The migration name
     */
    String getName();
    
    /**
     * Apply the migration to the database
     * @param conn The database connection
     * @throws SQLException If an SQL error occurs
     */
    void up(Connection conn) throws SQLException;
} 