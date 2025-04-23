package View.Dashboard;

import Controller.CategoryController;
import Model.Category;
import Support.Router;
import Support.UIConstants;
import View.Layout.DashboardLayout;
import View.NavigatePanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Panel for adding and editing categories.
 */
public class CategoryFormPanel extends DashboardLayout {
    private final CategoryController categoryController;
    private final Category categoryToEdit;
    private final boolean isEditMode;
    
    // Form fields
    private JTextField nameField;
    private NavigatePanel contentPanel;
    
    /**
     * Constructor for creating a new category.
     */
    public CategoryFormPanel() {
        super();
        this.categoryController = new CategoryController();
        this.categoryToEdit = null;
        this.isEditMode = false;
    }
    
    /**
     * Constructor for editing an existing category.
     * 
     * @param categoryToEdit The category to edit
     */
    public CategoryFormPanel(Category categoryToEdit) {
        super();
        this.categoryController = new CategoryController();
        this.categoryToEdit = categoryToEdit;
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
        if (contentPanel != null && isEditMode && categoryToEdit != null) {
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
     * Populate the form fields with the category's data.
     */
    private void populateFormFields() {
        // Only populate if fields have been initialized
        if (nameField == null) {
            return;
        }
        
        nameField.setText(categoryToEdit.getName());
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
        JButton backButton = new JButton("Back to Categories");
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
            Router.navigate("dashboard/categories");
        });
        
        // Right side of header: Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        
        String titleText = isEditMode ? "Edit Category" : "Add New Category";
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        String subtitleText = isEditMode ? "Update category information" : "Enter category information";
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
        JLabel nameLabel = new JLabel("Category Name:");
        nameLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        nameField = new JTextField(20);
        nameField.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(nameField, gbc);
        
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
            Router.navigate("dashboard/categories");
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
            saveCategory();
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
     * Save the category.
     */
    private void saveCategory() {
        // Validate form
        if (!validateForm()) {
            return;
        }
        
        String name = nameField.getText().trim();
        
        try {
            if (isEditMode) {
                // Update existing category
                Category updatedCategory = new Category(
                    categoryToEdit.getId(),
                    name
                );
                
                boolean success = categoryController.updateCategory(updatedCategory);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Category updated successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    Router.navigate("dashboard/categories");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to update category",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Create new category
                Category newCategory = categoryController.createCategory(name);
                
                if (newCategory != null) {
                    JOptionPane.showMessageDialog(this,
                        "Category created successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    Router.navigate("dashboard/categories");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to create category",
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
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a category name",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        
        return true;
    }
} 