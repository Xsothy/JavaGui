package Support;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for database migrations.
 * All migrations should implement this interface to be discovered by the MigrationManager.
 */
public interface Migration {
    /**
     * Get the name of the migration.
     * This should be a unique identifier, preferably with a numeric prefix for ordering (e.g., "001_create_staff_table").
     * 
     * @return The migration name
     */
    String getName();
    
    /**
     * Apply the migration to the database.
     * This method should contain the SQL statements to modify the database schema.
     * 
     * @param conn The database connection
     * @throws SQLException If an SQL error occurs
     */
    void up(Connection conn) throws SQLException;
} 