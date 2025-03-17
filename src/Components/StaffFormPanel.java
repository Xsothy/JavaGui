package Components;

import Controller.StaffController;
import Model.Staff;
import Support.Router;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A panel for creating and editing staff members.
 */
public class StaffFormPanel extends NavigatePanel {
    private final StaffController staffController;
    private Staff staff;
    private int staffId = 0;
    private boolean isEditMode;
    private final Router router;
    
    // Form components
    private JTextField txtName;
    private JTextField txtPosition;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JButton btnSave;
    private JButton btnCancel;

    public StaffFormPanel(Router router) {
        this.staffController = new StaffController();
        this.router = router;
        this.isEditMode = false;
        this.staff = null;
        
        initComponents();
        setupListeners();
    }

    public StaffFormPanel(Staff staff, Router router) {
        this.router = router;
        this.staffController = new StaffController();
        this.staff = staff;
        this.staffId = staff.getId();
        this.isEditMode = true;
        
        initComponents();
        setupListeners();
        
        // Fill the form with the staff data
        txtName.setText(staff.getName());
        txtPosition.setText(staff.getPosition());
        txtUsername.setText(staff.getUserName());
        cmbRole.setSelectedItem(staff.getRole());
        
        // In edit mode, password is optional, so make the field less prominent
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        txtPassword.setBackground(new Color(245, 245, 245));
    }
    
    /**
     * Initialize the components.
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(255, 255, 255));
        setPreferredSize(new Dimension(500, 400));
        
        // Form title
        JLabel titleLabel = new JLabel(isEditMode ? "Edit Staff" : "Add New Staff");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add form fields
        addFormField(formPanel, "Name:", txtName = new JTextField(20), 0, gbc);
        addFormField(formPanel, "Position:", txtPosition = new JTextField(20), 1, gbc);
        addFormField(formPanel, "Username:", txtUsername = new JTextField(20), 2, gbc);
        addFormField(formPanel, isEditMode ? "New Password:" : "Password:", txtPassword = new JPasswordField(20), 3, gbc);
        
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(roleLabel, gbc);
        
        cmbRole = new JComboBox<>(new String[]{"admin", "staff"});
        cmbRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(cmbRole, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 255, 255));
        
        btnSave = new JButton(isEditMode ? "Update" : "Save");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(57, 117, 247));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        
        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Add a form field to the panel.
     * 
     * @param panel The panel to add to
     * @param labelText The label text
     * @param field The field component
     * @param row The row index
     * @param gbc The GridBagConstraints
     */
    private void addFormField(JPanel panel, String labelText, JComponent field, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);
        
        if (field instanceof JTextField) {
            ((JTextField) field).setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        
        gbc.gridx = 1;
        gbc.gridy = row;
        panel.add(field, gbc);
    }
    
    /**
     * Setup component listeners.
     */
    private void setupListeners() {
        // Save button listener
        btnSave.addActionListener(e -> {
            try {
                if (validateForm()) {
                    if (isEditMode) {
                        updateStaff();
                    } else {
                        createStaff();
                    }
                    
                    router.navigate("/staffs");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StaffFormPanel.class.getName()).log(Level.SEVERE, "Database error", ex);
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Cancel button listener
        btnCancel.addActionListener(e -> {
            router.navigate("/staffs");
        });
    }
    
    /**
     * Validate the form inputs.
     * 
     * @return true if the form is valid, false otherwise
     */
    private boolean validateForm() {
        // Check name
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtName.requestFocus();
            return false;
        }
        
        // Check username
        if (txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtUsername.requestFocus();
            return false;
        }
        
        // Check password (only required in add mode)
        if (!isEditMode && new String(txtPassword.getPassword()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtPassword.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Create a new staff member.
     * 
     * @throws SQLException If a database error occurs
     */
    private void createStaff() throws SQLException {
        String name = txtName.getText().trim();
        String position = txtPosition.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String role = (String) cmbRole.getSelectedItem();
        
        try {
            Staff staff = staffController.createStaff(name, position, username, password, role);
            JOptionPane.showMessageDialog(this, "Staff created successfully",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            throw ex;
        }
    }
    
    /**
     * Update an existing staff member.
     * 
     * @throws SQLException If a database error occurs
     */
    private void updateStaff() throws SQLException {
        String name = txtName.getText().trim();
        String position = txtPosition.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String role = (String) cmbRole.getSelectedItem();

        try {
            // Get the staff member from the database
            Staff staff = staffController.getStaffById(staffId)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
            
            // Update the staff member
            staff.setName(name);
            staff.setPosition(position);
            staff.setUserName(username);
            staff.setRole(role);
            
            boolean success;
            if (password.isEmpty()) {
                // Update without changing the password
                success = staffController.updateStaff(staff);
            } else {
                // Update with a new password
                success = staffController.updateStaffWithPassword(staff, password);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Staff updated successfully",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update staff",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            throw ex;
        }
    }
} 