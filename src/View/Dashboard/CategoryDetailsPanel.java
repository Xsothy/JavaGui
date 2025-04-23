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
 * Panel for displaying category details.
 */
public class CategoryDetailsPanel extends DashboardLayout {
    private final CategoryController categoryController;
    private final Category category;
    
    /**
     * Constructor for CategoryDetailsPanel.
     * 
     * @param category The category to display
     */
    public CategoryDetailsPanel(Category category) {
        super();
        this.categoryController = new CategoryController();
        this.category = category;
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
        
        JLabel titleLabel = new JLabel("Category Details");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("View category information");
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
     * Create the details panel with category information.
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
        
        // Details container
        JPanel detailsContainer = new JPanel(new GridBagLayout());
        detailsContainer.setBackground(Color.WHITE);
        detailsContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // ID field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel idLabel = new JLabel("Category ID:");
        idLabel.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        detailsContainer.add(idLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel idValue = new JLabel(String.valueOf(category.getId()));
        idValue.setFont(UIConstants.TABLE_CONTENT_FONT);
        detailsContainer.add(idValue, gbc);
        
        // Name field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Category Name:");
        nameLabel.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        detailsContainer.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JLabel nameValue = new JLabel(category.getName());
        nameValue.setFont(UIConstants.TABLE_CONTENT_FONT);
        detailsContainer.add(nameValue, gbc);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Edit button
        JButton editButton = new JButton("Edit Category");
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
            Router.navigate("dashboard/categories/edit/" + category.getId());
        });
        
        // Delete button
        JButton deleteButton = new JButton("Delete Category");
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
                "Are you sure you want to delete this category?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = categoryController.deleteCategory(category.getId());
                    if (success) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Category deleted successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        Router.navigate("dashboard/categories");
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "Failed to delete category. It may be in use by products.",
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
        
        // Add details container and buttons panel to the details panel
        detailsPanel.add(detailsContainer);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(buttonsPanel);
        
        return detailsPanel;
    }
} 