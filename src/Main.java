import Controller.CategoryController;
import Controller.ExpenseController;
import Controller.ProductController;
import Controller.SaleController;
import Controller.StaffController;
import Support.DB;
import Support.MigrationManager;
import Support.Router;
import View.Dashboard.CategoryFormPanel;
import View.Dashboard.CategoryPanel;
import View.Dashboard.ExpenseFormPanel;
import View.Dashboard.ExpensePanel;
import View.Dashboard.ProductFormPanel;
import View.Dashboard.ProductPanel;
import View.Dashboard.SaleDashboardPanel;
import View.Dashboard.SaleFormPanel;
import View.Dashboard.SalePanel;
import View.Dashboard.StaffFormPanel;
import View.Dashboard.StaffPanel;
import View.DashboardPanel;
import View.HomePanel;
import View.LoginPanel;
import View.NavigatePanel;

import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;

public class Main {
    // SQLite database file path
    private static final String DB_FILE_PATH = "database.db";

    public static void main(String[] args) {
        System.out.println("Starting application...");
        JFrame application = new JFrame();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        NavigatePanel contentPane = new NavigatePanel();
        application.setContentPane(contentPane);


        if (!initializeDatabase()) {
            JOptionPane.showMessageDialog(application,
                    "Failed to initialize database. Application cannot start.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        Router.initialize(application);
        Router.register("/", new HomePanel());
        Router.register("login", new LoginPanel());
        Router.register("dashboard", new DashboardPanel());
        Router.register("dashboard/staffs", new StaffPanel());
        
        // Register dynamic routes for staff operations
        Router.register("dashboard/staffs/add", params -> new StaffFormPanel());
        Router.register("dashboard/staffs/edit/{id}", params -> 
            new StaffController().edit(params));
        Router.register("dashboard/staffs/{id}", params -> 
            new StaffController().show(params));
            
        // Register routes for expense operations
        Router.register("dashboard/expenses", new ExpensePanel());
        Router.register("dashboard/expenses/add", params -> new ExpenseFormPanel());
        Router.register("dashboard/expenses/edit/{id}", params ->
            new ExpenseController().edit(params)
        );
        
        // Register Expense detail route
        Router.register("dashboard/expenses/{id}", params -> new ExpenseController().show(params));

        // Register Sale routes
        Router.register("dashboard/sales/dashboard", params -> new SaleDashboardPanel());
        Router.register("dashboard/sales", new SalePanel());
        Router.register("dashboard/sales/create", params -> new SaleFormPanel());
        Router.register("dashboard/sales/{id}", params -> 
            new SaleController().show(params));

        // Register Category routes
        Router.register("dashboard/categories", params -> new CategoryPanel());
        Router.register("dashboard/categories/add", params -> new CategoryFormPanel());
        Router.register("dashboard/categories/{id}", params -> new CategoryController().show(params));
        Router.register("dashboard/categories/edit/{id}", params -> new CategoryController().edit(params));

        // Register Product routes
        Router.register("dashboard/products", params -> new ProductPanel());
        Router.register("dashboard/products/add", params -> new ProductFormPanel());
        Router.register("dashboard/products/{id}", params -> new ProductController().show(params));
        Router.register("dashboard/products/edit/{id}", params -> new ProductController().edit(params));

        Router.navigate("/");
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

        try {
            Connection conn = DB.getInstance().getConnection();
            System.out.println("Database connection successful. Database file is ready.");

            MigrationManager migrationManager = new MigrationManager(conn);
            migrationManager.migrate();

            return true;
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            return false;
        } catch (IllegalStateException e) {
            System.err.println("Database configuration error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return false;
        }
    }
}
