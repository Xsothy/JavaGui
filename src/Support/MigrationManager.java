package Support;

import Migrations.Migration;
import Migrations.M001_CreateStaffTable;
import Migrations.M002_CreateExpensesTable;
import Migrations.M003_AddDefaultAdmin;

import java.sql.*;
import java.util.*;

public class MigrationManager {
    private final Connection connection;

    public MigrationManager(Connection connection) {
        this.connection = connection;
    }

    public void migrate() {
        try {
            // Create migrations table if it doesn't exist
            createMigrationTable();

            // Get list of applied migrations
            Set<String> appliedMigrations = getAppliedMigrations();

            // Run pending migrations
            List<Migration> migrations = getMigrations();
            for (Migration migration : migrations) {
                if (!appliedMigrations.contains(migration.getName())) {
                    System.out.println("Applying migration: " + migration.getName());
                    migration.up(connection);
                    recordMigration(migration.getName());
                    System.out.println("Migration applied: " + migration.getName());
                }
            }

            System.out.println("All migrations completed successfully");
        } catch (SQLException e) {
            System.err.println("Error during migration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createMigrationTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS migrations (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "migration_name TEXT NOT NULL," +
                     "applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    private Set<String> getAppliedMigrations() throws SQLException {
        Set<String> migrations = new HashSet<>();
        String sql = "SELECT migration_name FROM migrations";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                migrations.add(rs.getString("migration_name"));
            }
        }

        return migrations;
    }

    private void recordMigration(String migrationName) throws SQLException {
        String sql = "INSERT INTO migrations (migration_name) VALUES (?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, migrationName);
            pstmt.executeUpdate();
        }
    }

    private List<Migration> getMigrations() {
        // Load migrations from the Migrations package
        List<Migration> migrations = new ArrayList<>();

        // Add migrations in order
        migrations.add(new M001_CreateStaffTable());
        migrations.add(new M002_CreateExpensesTable());
        migrations.add(new M003_AddDefaultAdmin());
        
        return migrations;
    }
}
