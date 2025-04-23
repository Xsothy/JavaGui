package View.Dashboard;

import Controller.CategoryController;
import Controller.ProductController;
import Model.Category;
import Model.Product;
import Support.FileUtils;
import Support.Router;
import Support.UIConstants;
import View.Layout.DashboardLayout;
import View.NavigatePanel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Panel for adding and editing products.
 */
public class ProductFormPanel extends DashboardLayout {
    private final ProductController productController;
    private final CategoryController categoryController;
    private final Product productToEdit;
    private final boolean isEditMode;
    
    // Form fields
    private JTextField nameField;
    private JTextField priceField;
    private JTextField stockField;
    private JComboBox<Category> categoryComboBox;
    private JLabel imagePreviewLabel;
    private JButton uploadImageButton;
    private String imagePath;
    private NavigatePanel contentPanel;
    
    /**
     * Constructor for creating a new product.
     */
    public ProductFormPanel() {
        super();
        this.productController = new ProductController();
        this.categoryController = new CategoryController();
        this.productToEdit = null;
        this.isEditMode = false;
        this.imagePath = "";
    }
    
    /**
     * Constructor for editing an existing product.
     * 
     * @param productToEdit The product to edit
     */
    public ProductFormPanel(Product productToEdit) {
        super();
        this.productController = new ProductController();
        this.categoryController = new CategoryController();
        this.productToEdit = productToEdit;
        this.isEditMode = true;
        this.imagePath = productToEdit.getImagePath();
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
        if (contentPanel != null && isEditMode && productToEdit != null) {
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
     * Populate the form fields with the product's data.
     */
    private void populateFormFields() {
        // Only populate if fields have been initialized
        if (nameField == null || priceField == null || stockField == null || categoryComboBox == null) {
            return;
        }
        
        nameField.setText(productToEdit.getName());
        priceField.setText(productToEdit.getPrice().toString());
        stockField.setText(String.valueOf(productToEdit.getStockQuantity()));
        
        // Set the category
        for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
            Category category = categoryComboBox.getItemAt(i);
            if (category.getId() == productToEdit.getCategoryId()) {
                categoryComboBox.setSelectedIndex(i);
                break;
            }
        }
        
        // Load image preview
        updateImagePreview(productToEdit.getImagePath());
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
        JButton backButton = new JButton("Back to Products");
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
            Router.navigate("dashboard/products");
        });
        
        // Right side of header: Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        
        String titleText = isEditMode ? "Edit Product" : "Add New Product";
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        String subtitleText = isEditMode ? "Update product information" : "Enter product information";
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
        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        nameField = new JTextField(20);
        nameField.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(nameField, gbc);
        
        // Price field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(priceLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        priceField = new JTextField(20);
        priceField.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(priceField, gbc);
        
        // Stock field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel stockLabel = new JLabel("Stock Quantity:");
        stockLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(stockLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        stockField = new JTextField(20);
        stockField.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(stockField, gbc);
        
        // Category field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(categoryLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        
        // Populate category dropdown
        List<Category> categories = categoryController.getAllCategories();
        categoryComboBox = new JComboBox<>(categories.toArray(new Category[0]));
        categoryComboBox.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(categoryComboBox, gbc);
        
        // Image field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel imageLabel = new JLabel("Product Image:");
        imageLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        formFieldsPanel.add(imageLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        uploadImageButton = new JButton("Upload Image");
        uploadImageButton.setFont(UIConstants.BUTTON_FONT);
        uploadImageButton.setBackground(UIConstants.SECONDARY_COLOR);
        uploadImageButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        uploadImageButton.setFocusPainted(false);
        uploadImageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formFieldsPanel.add(uploadImageButton, gbc);
        
        uploadImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                
                // Save the file and get the path
                String savedImagePath = FileUtils.saveProductImage(selectedFile);
                if (savedImagePath != null) {
                    imagePath = savedImagePath;
                    updateImagePreview(savedImagePath);
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error saving image. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        
        // Image preview
        imagePreviewLabel = new JLabel("No image selected");
        imagePreviewLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        imagePreviewLabel.setPreferredSize(new Dimension(200, 120));
        imagePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR));
        formFieldsPanel.add(imagePreviewLabel, gbc);
        
        // If editing, show the current image
        if (isEditMode && productToEdit != null && productToEdit.getImagePath() != null && !productToEdit.getImagePath().isEmpty()) {
            updateImagePreview(productToEdit.getImagePath());
        }
        
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
            Router.navigate("dashboard/products");
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
            saveProduct();
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
     * Update the image preview with the provided image path.
     * 
     * @param path The image path
     */
    private void updateImagePreview(String path) {
        if (path != null && !path.isEmpty()) {
            try {
                ImageIcon imageIcon = FileUtils.loadProductImageIcon(path, 200, 120);
                if (imageIcon != null) {
                    imagePreviewLabel.setIcon(imageIcon);
                    imagePreviewLabel.setText("");
                } else {
                    imagePreviewLabel.setIcon(null);
                    imagePreviewLabel.setText("Image not found");
                }
            } catch (Exception e) {
                imagePreviewLabel.setIcon(null);
                imagePreviewLabel.setText("Error loading image");
            }
        } else {
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("No image selected");
        }
    }
    
    /**
     * Save the product.
     */
    private void saveProduct() {
        // Validate form
        if (!validateForm()) {
            return;
        }
        
        try {
            String name = nameField.getText().trim();
            BigDecimal price = new BigDecimal(priceField.getText().trim());
            int stockQuantity = Integer.parseInt(stockField.getText().trim());
            Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
            int categoryId = selectedCategory.getId();
            
            if (isEditMode) {
                // Update existing product
                Product updatedProduct = new Product(
                    productToEdit.getId(),
                    name,
                    stockQuantity,
                    price,
                    imagePath,
                    categoryId
                );
                
                boolean success = productController.updateProduct(updatedProduct);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Product updated successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    Router.navigate("dashboard/products");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to update product",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Create new product
                Product newProduct = productController.createProduct(
                    name,
                    stockQuantity,
                    price,
                    imagePath,
                    categoryId
                );
                
                if (newProduct != null) {
                    JOptionPane.showMessageDialog(this,
                        "Product created successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    Router.navigate("dashboard/products");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to create product",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Invalid number format. Please check price and stock fields.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
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
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a product name",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        
        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a price",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            priceField.requestFocus();
            return false;
        }
        
        try {
            BigDecimal price = new BigDecimal(priceText);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this,
                    "Price cannot be negative",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                priceField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid price format. Please enter a valid number.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            priceField.requestFocus();
            return false;
        }
        
        if (stockText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter stock quantity",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            stockField.requestFocus();
            return false;
        }
        
        try {
            int stock = Integer.parseInt(stockText);
            if (stock < 0) {
                JOptionPane.showMessageDialog(this,
                    "Stock quantity cannot be negative",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                stockField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid stock format. Please enter a valid integer.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            stockField.requestFocus();
            return false;
        }
        
        if (categoryComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a category",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            categoryComboBox.requestFocus();
            return false;
        }
        
        return true;
    }
} 