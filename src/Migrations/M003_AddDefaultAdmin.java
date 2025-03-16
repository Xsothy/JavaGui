package Migrations;

import Model.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Migration to add a default admin staff member
 */
public class M003_AddDefaultAdmin implements Migration {
    
    @Override
    public String getName() {
        return "003_add_default_admin";
    }
    
    @Override
    public void up(Connection conn) throws SQLException {
        // Check if admin already exists
        String checkSql = "SELECT COUNT(*) FROM staff WHERE username = 'admin'";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Admin user already exists, skipping creation");
                return;
            }
        }
        
        // Add default admin
        String sql = "INSERT INTO staff (name, position, username, password, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "Administrator");
            pstmt.setString(2, "System Admin");
            pstmt.setString(3, "admin");
            pstmt.setString(4, Staff.hashPassword("admin123")); // In production, use a hashed password
            pstmt.setString(5, "admin");
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Default admin user created successfully");
            }
        }
    }
} 