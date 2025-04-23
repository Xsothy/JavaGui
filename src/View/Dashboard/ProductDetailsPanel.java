package View.Dashboard;

import Controller.ProductController;
import Model.ProductWithCategory;
import Support.FileUtils;
import Support.Router;
import Support.UIConstants;
import View.Layout.DashboardLayout;
import View.NavigatePanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Panel for displaying product details.
 */
public class ProductDetailsPanel extends DashboardLayout {
    private final ProductController productController;
    private final ProductWithCategory productWithCategory;
    
    /**
     * Constructor for ProductDetailsPanel.
     * 
     * @param productWithCategory The product with category to display
     */
    public ProductDetailsPanel(ProductWithCategory productWithCategory) {
        super();
        this.productController = new ProductController();
        this.productWithCategory = productWithCategory;
    }
    
    @Override
    public void render() {
        super.render();
    }
    
    @Override
    public NavigatePanel getContentPanel() {
        NavigatePanel contentPanel = new NavigatePanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.setBackground(Color.WHITE);
        
        // Create header panel with title and back button
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create details panel
        JPanel detailsPanel = createDetailsPanel();
        contentPanel.add(detailsPanel, BorderLayout.CENTER);
        
        return contentPanel;
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
        
        JLabel titleLabel = new JLabel("Product Details");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("View product information");
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
     * Create the details panel with product information.
     * 
     * @return The details panel
     */
    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SECTION_SPACING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING
        ));
        
        // Image panel at the top
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(300, 200));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR));
        
        String imagePath = productWithCategory.getProduct().getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                ImageIcon imageIcon = FileUtils.loadProductImageIcon(imagePath, 300, 200);
                if (imageIcon != null) {
                    imageLabel.setIcon(imageIcon);
                } else {
                    imageLabel.setText("Image not found");
                }
            } catch (Exception e) {
                imageLabel.setText("Error loading image");
            }
        } else {
            imageLabel.setText("No image available");
        }
        
        imagePanel.add(imageLabel);
        
        // Details container
        JPanel detailsContainer = new JPanel(new GridBagLayout());
        detailsContainer.setBackground(Color.WHITE);
        detailsContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        detailsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // ID field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel idLabel = new JLabel("Product ID:");
        idLabel.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        detailsContainer.add(idLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel idValue = new JLabel(String.valueOf(productWithCategory.getProduct().getId()));
        idValue.setFont(UIConstants.TABLE_CONTENT_FONT);
        detailsContainer.add(idValue, gbc);
        
        // Name field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        detailsContainer.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JLabel nameValue = new JLabel(productWithCategory.getProduct().getName());
        nameValue.setFont(UIConstants.TABLE_CONTENT_FONT);
        detailsContainer.add(nameValue, gbc);
        
        // Price field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        detailsContainer.add(priceLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel priceValue = new JLabel(productWithCategory.getProduct().getPrice().toString());
        priceValue.setFont(UIConstants.TABLE_CONTENT_FONT);
        detailsContainer.add(priceValue, gbc);
        
        // Stock field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel stockLabel = new JLabel("Stock Quantity:");
        stockLabel.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        detailsContainer.add(stockLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel stockValue = new JLabel(String.valueOf(productWithCategory.getProduct().getStockQuantity()));
        stockValue.setFont(UIConstants.TABLE_CONTENT_FONT);
        detailsContainer.add(stockValue, gbc);
        
        // Category field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        detailsContainer.add(categoryLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel categoryValue = new JLabel(productWithCategory.getCategoryName());
        categoryValue.setFont(UIConstants.TABLE_CONTENT_FONT);
        detailsContainer.add(categoryValue, gbc);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Edit button
        JButton editButton = new JButton("Edit Product");
        editButton.setFont(UIConstants.BUTTON_FONT);
        editButton.setForeground(Color.WHITE);
        editButton.setBackground(UIConstants.PRIMARY_COLOR);
        editButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        editButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                editButton.setBackground(UIConstants.ACCENT_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                editButton.setBackground(UIConstants.PRIMARY_COLOR);
            }
        });
        
        editButton.addActionListener(e -> {
            Router.navigate("dashboard/products/edit/" + productWithCategory.getProduct().getId());
        });
        
        // Delete button
        JButton deleteButton = new JButton("Delete Product");
        deleteButton.setFont(UIConstants.BUTTON_FONT);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(UIConstants.DANGER_COLOR);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this product?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = productController.deleteProduct(productWithCategory.getProduct().getId());
                    if (success) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Product deleted successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        Router.navigate("dashboard/products");
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "Failed to delete product. It may be in use by other records.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        buttonsPanel.add(editButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(deleteButton);
        
        // Add all panels to the details panel
        detailsPanel.add(imagePanel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(detailsContainer);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(buttonsPanel);
        
        return detailsPanel;
    }
} 