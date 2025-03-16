package Support;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Manages database migrations.
 * This class is responsible for discovering, tracking, and applying migrations.
 */
public class MigrationManager {
    private final Connection connection;
    private final String migrationsPackage;
    
    /**
     * Creates a new MigrationManager with the specified database connection.
     * 
     * @param connection The database connection
     */
    public MigrationManager(Connection connection) {
        this.connection = connection;
        this.migrationsPackage = "Migrations";
    }
    
    /**
     * Runs all pending migrations.
     * Migrations that have already been applied will be skipped.
     */
    public void migrate() {
        try {
            // Create migrations table if it doesn't exist
            createMigrationTable();
            
            // Get list of applied migrations
            Set<String> appliedMigrations = getAppliedMigrations();
            
            // Discover and run pending migrations
            List<Migration> migrations = discoverMigrations();
            
            // Sort migrations by name to ensure they run in order
            migrations.sort(Comparator.comparing(Migration::getName));
            
            int appliedCount = 0;
            for (Migration migration : migrations) {
                if (!appliedMigrations.contains(migration.getName())) {
                    System.out.println("Applying migration: " + migration.getName());
                    migration.up(connection);
                    recordMigration(migration.getName());
                    System.out.println("Migration applied: " + migration.getName());
                    appliedCount++;
                } else {
                    System.out.println("Skipping already applied migration: " + migration.getName());
                }
            }
            
            if (appliedCount > 0) {
                System.out.println("Applied " + appliedCount + " migrations successfully");
            } else {
                System.out.println("No new migrations to apply");
            }
        } catch (Exception e) {
            System.err.println("Error during migration: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates the migrations table if it doesn't exist.
     * This table tracks which migrations have been applied.
     * 
     * @throws SQLException If an SQL error occurs
     */
    private void createMigrationTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS migrations (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "migration_name TEXT NOT NULL," +
                     "applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    /**
     * Gets the list of migrations that have already been applied.
     * 
     * @return A set of migration names that have been applied
     * @throws SQLException If an SQL error occurs
     */
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
    
    /**
     * Records that a migration has been applied.
     * 
     * @param migrationName The name of the migration
     * @throws SQLException If an SQL error occurs
     */
    private void recordMigration(String migrationName) throws SQLException {
        String sql = "INSERT INTO migrations (migration_name) VALUES (?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, migrationName);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Discovers all migrations in the migrations package.
     * 
     * @return A list of discovered migrations
     */
    private List<Migration> discoverMigrations() {
        List<Migration> migrations = new ArrayList<>();
        
        try {
            // Get all classes in the migrations package
            List<Class<?>> migrationClasses = findClassesInPackage(migrationsPackage);
            
            // Filter for classes that implement Migration
            for (Class<?> clazz : migrationClasses) {
                if (Migration.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                    try {
                        // Create an instance of the migration
                        Migration migration = (Migration) clazz.getDeclaredConstructor().newInstance();
                        migrations.add(migration);
                        System.out.println("Discovered migration: " + migration.getName() + " (" + clazz.getName() + ")");
                    } catch (Exception e) {
                        System.err.println("Error instantiating migration class " + clazz.getName() + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error discovering migrations: " + e.getMessage());
            e.printStackTrace();
        }
        
        return migrations;
    }
    
    /**
     * Finds all classes in a package.
     * 
     * @param packageName The name of the package
     * @return A list of classes in the package
     */
    private List<Class<?>> findClassesInPackage(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        
        try {
            // Convert package name to path
            String packagePath = packageName.replace('.', '/');
            
            // Get the class loader
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            
            // Get all resources for the package
            Enumeration<URL> resources = classLoader.getResources(packagePath);
            
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                
                // Handle directory-based resources
                if (resource.getProtocol().equals("file")) {
                    File directory = new File(resource.getFile());
                    if (directory.exists()) {
                        // Get all .class files in the directory
                        File[] files = directory.listFiles((dir, name) -> name.endsWith(".class"));
                        if (files != null) {
                            for (File file : files) {
                                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                                try {
                                    Class<?> clazz = Class.forName(className);
                                    classes.add(clazz);
                                } catch (ClassNotFoundException e) {
                                    System.err.println("Error loading class " + className + ": " + e.getMessage());
                                }
                            }
                        }
                    }
                }
                // Handle JAR-based resources
                else if (resource.getProtocol().equals("jar")) {
                    String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                    try (JarFile jar = new JarFile(jarPath)) {
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String entryName = entry.getName();
                            if (entryName.startsWith(packagePath) && entryName.endsWith(".class")) {
                                String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
                                try {
                                    Class<?> clazz = Class.forName(className);
                                    classes.add(clazz);
                                } catch (ClassNotFoundException e) {
                                    System.err.println("Error loading class " + className + ": " + e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding classes in package " + packageName + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return classes;
    }
}
