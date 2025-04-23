package View.Dashboard;

import Controller.StaffController;
import Model.Staff;
import Support.Router;
import Support.UIConstants;
import View.Layout.DashboardLayout;
import View.NavigatePanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Panel for adding and editing staff members.
 */
public class StaffFormPanel extends DashboardLayout {
    private final StaffController staffController;
    private final Staff staffToEdit;
    private final boolean isEditMode;
    
    // Form fields
    private JTextField nameField;
    private JTextField positionField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private NavigatePanel contentPanel;
    
    /**
     * Constructor for creating a new staff member.
     */
    public StaffFormPanel() {
        super();
        this.staffController = new StaffController();
        this.staffToEdit = null;
        this.isEditMode = false;
    }
    
    /**
     * Constructor for editing an existing staff member.
     * 
     * @param staffToEdit The staff member to edit
     */
    public StaffFormPanel(Staff staffToEdit) {
        super();
        this.staffController = new StaffController();
        this.staffToEdit = staffToEdit;
        this.isEditMode = true;
    }
    
    @Override
    public void render() {
        super.render();
        setupForm();
    }

    /**
     * Sets up the form with data after the panel has been rendered
     */
    private void setupForm() {
        if (contentPanel != null && isEditMode && staffToEdit != null) {
            populateFormFields();
        }
    }

    @Override
    public NavigatePanel getContentPanel() {
        // Create a basic panel with layout but without interactive data
        contentPanel = new NavigatePanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.setBackground(Color.WHITE);
        
        // Create header panel with title and back button
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        contentPanel.add(formPanel, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    /**
     * Populate the form fields with the staff member's data.
     */
    private void populateFormFields() {
        // Only populate if fields have been initialized
        if (nameField == null || positionField == null || usernameField == null || 
            passwordField == null || confirmPasswordField == null || roleComboBox == null) {
            return;
        }
        
        nameField.setText(staffToEdit.getName());
        positionField.setText(staffToEdit.getPosition());
        usernameField.setText(staffToEdit.getUserName());
        
        // Don't populate password fields when editing
        passwordField.setText("");
        confirmPasswordField.setText("");
        
        // Set the role
        roleComboBox.setSelectedItem(staffToEdit.getRole());
    }
    
    /**
     * Create the header panel with title and back button.
     * 
     * @return The header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING
            )
        ));
        
        // Left side of header: Back button
        JButton backButton = new JButton("Back to Staff List");
        backButton.setFont(UIConstants.BUTTON_FONT);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(UIConstants.PRIMARY_COLOR);
        backButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(UIConstants.ACCENT_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(UIConstants.PRIMARY_COLOR);
            }
        });
        
        backButton.addActionListener(e -> {
            Router.navigate("dashboard/staffs");
        });
        
        // Right side of header: Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        
        String titleText = isEditMode ? "Edit Staff Member" : "Add New Staff Member";
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        String subtitleText = isEditMode ? "Update staff information" : "Enter staff information";
        JLabel subtitleLabel = new JLabel(subtitleText);
        subtitleLabel.setFont(UIConstants.SUBTITLE_FONT);
        subtitleLabel.setForeground(UIConstants.LIGHT_TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Create the form panel.
     * 
     * @return The form panel
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SECTION_SPACING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING
        ));
        
        // Form container with fields
        JPanel formFieldsPanel = new JPanel(new GridBagLayout());
        formFieldsPanel.setBackground(Color.WHITE);
        formFieldsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        nameField = new JTextField(20);
        nameField.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(nameField, gbc);
        
        // Position field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(positionLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        positionField = new JTextField(20);
        positionField.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(positionField, gbc);
        
        // Username field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        usernameField = new JTextField(20);
        usernameField.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(usernameField, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel passwordLabel = new JLabel(isEditMode ? "New Password:" : "Password:");
        passwordLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        passwordField = new JPasswordField(20);
        passwordField.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(passwordField, gbc);
        
        // Confirm Password field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel confirmPasswordLabel = new JLabel(isEditMode ? "Confirm New Password:" : "Confirm Password:");
        confirmPasswordLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(confirmPasswordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(confirmPasswordField, gbc);
        
        // Note about passwords (for edit mode)
        if (isEditMode) {
            gbc.gridx = 1;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            JLabel noteLabel = new JLabel("Leave blank to keep current password");
            noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            noteLabel.setForeground(UIConstants.LIGHT_TEXT_COLOR);
            formFieldsPanel.add(noteLabel, gbc);
        }
        
        // Role field
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(roleLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        String[] roles = {"staff", "admin"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(roleComboBox, gbc);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(UIConstants.BUTTON_FONT);
        cancelButton.setForeground(UIConstants.TEXT_COLOR);
        cancelButton.setBackground(UIConstants.SECONDARY_COLOR);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        cancelButton.addActionListener(e -> {
            Router.navigate("dashboard/staffs");
        });
        
        // Save button
        JButton saveButton = new JButton("Submit");
        saveButton.setFont(UIConstants.BUTTON_FONT);
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(UIConstants.SUCCESS_COLOR);
        saveButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        saveButton.addActionListener(e -> {
            saveStaff();
        });
        
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(saveButton);
        
        // Add form fields panel and buttons panel to the form panel
        formPanel.add(formFieldsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(buttonsPanel);
        
        return formPanel;
    }
    
    /**
     * Save the staff member.
     */
    private void saveStaff() {
        // Validate form
        if (!validateForm()) {
            return;
        }
        
        String name = nameField.getText().trim();
        String position = positionField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();
        
        try {
            if (isEditMode) {
                // Update existing staff
                Staff updatedStaff = new Staff(
                    staffToEdit.getId(),
                    name,
                    position,
                    username,
                    staffToEdit.getPassword(), // Keep original password if not changing
                    role
                );
                
                boolean success;
                
                // Check if password is being updated
                if (!password.isEmpty()) {
                    if (!password.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(this,
                            "Passwords do not match. Please try again.",
                            "Password Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    success = staffController.updateStaffWithPassword(updatedStaff, password);
                } else {
                    success = staffController.updateStaff(updatedStaff);
                }
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Staff updated successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    Router.navigate("dashboard/staffs");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to update staff",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Create new staff
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this,
                        "Passwords do not match. Please try again.",
                        "Password Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Staff newStaff = staffController.createStaff(name, position, username, password, role);
                
                if (newStaff != null) {
                    JOptionPane.showMessageDialog(this,
                        "Staff created successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    Router.navigate("dashboard/staffs");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to create staff",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate the form fields.
     * 
     * @return true if the form is valid, false otherwise
     */
    private boolean validateForm() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a name",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a username",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            usernameField.requestFocus();
            return false;
        }
        
        // For new staff, password is required
        if (!isEditMode && password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a password",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return false;
        }
        
        // If password field has content, confirm must match
        if (!password.isEmpty() && !password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            confirmPasswordField.requestFocus();
            return false;
        }
        
        return true;
    }
} 