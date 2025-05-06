package View.Dashboard;

import Controller.ProductController;
import Model.Product;
import Model.ProductWithCategory;
import Support.Router;
import Support.SessionManager;
import Support.UIConstants;
import View.Layout.DashboardLayout;
import View.NavigatePanel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for viewing and managing products.
 */
public class ProductPanel extends DashboardLayout {
    private final ProductController productController;
    private JTable productTable;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    // Stock alert threshold
    private static final int LOW_STOCK_THRESHOLD = 10;
    
    /**
     * Constructor for ProductPanel.
     */
    public ProductPanel() {
        super();
        this.productController = new ProductController();
    }
    
    @Override
    public void render() {
        super.render();
        loadProducts();
    }
    
    @Override
    public NavigatePanel getContentPanel() {
        NavigatePanel contentPanel = new NavigatePanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        contentPanel.setBackground(Color.WHITE);
        
        // Header panel with title and actions
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
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Product Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Add, edit or delete products");
        subtitleLabel.setFont(UIConstants.SUBTITLE_FONT);
        subtitleLabel.setForeground(UIConstants.LIGHT_TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);
        
        // Actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setBackground(Color.WHITE);
        
        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel searchIcon = new JLabel("\uD83D\uDD0D"); // Magnifying glass emoji
        searchIcon.setFont(new Font("Dialog", Font.PLAIN, 14));
        
        searchField = new JTextField(15);
        searchField.setBorder(null);
        searchField.setFont(UIConstants.TABLE_CONTENT_FONT);
        
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        // Category filter
        filterComboBox = new JComboBox<>(new String[]{"All Categories"});
        filterComboBox.setFont(UIConstants.TABLE_CONTENT_FONT);
        filterComboBox.setBackground(Color.WHITE);
        
        // Add button
        JButton addButton = new JButton("+ Add Product");
        addButton.setFont(UIConstants.BUTTON_FONT);
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(UIConstants.PRIMARY_COLOR);
        addButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Enable add button only for admin users
        addButton.setEnabled(isAdmin());
        
        // Add hover effect
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (addButton.isEnabled()) {
                    addButton.setBackground(UIConstants.ACCENT_COLOR);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (addButton.isEnabled()) {
                    addButton.setBackground(UIConstants.PRIMARY_COLOR);
                }
            }
        });
        
        addButton.addActionListener(e -> Router.navigate("dashboard/products/add"));
        
        // Add search functionality
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = searchField.getText().trim();
                filterProducts(searchText);
            }
        });
        
        // Add filter functionality
        filterComboBox.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            filterProducts(searchText);
        });
        
        actionsPanel.add(searchPanel);
        actionsPanel.add(filterComboBox);
        actionsPanel.add(addButton);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(actionsPanel, BorderLayout.EAST);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SECTION_SPACING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING
        ));
        
        // Create the product table
        productTable = new JTable();
        productTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        productTable.setShowGrid(true);
        productTable.setGridColor(UIConstants.BORDER_COLOR);
        productTable.setSelectionBackground(UIConstants.TABLE_SELECTION_BG_COLOR);
        productTable.setSelectionForeground(UIConstants.TABLE_SELECTION_FG_COLOR);
        productTable.setFont(UIConstants.TABLE_CONTENT_FONT);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        productTable.setFillsViewportHeight(true);
        
        // Style the header
        productTable.getTableHeader().setFont(UIConstants.TABLE_HEADER_FONT);
        productTable.getTableHeader().setBackground(UIConstants.TABLE_HEADER_BG_COLOR);
        productTable.getTableHeader().setForeground(UIConstants.TABLE_HEADER_FG_COLOR);
        productTable.getTableHeader().setPreferredSize(
            new Dimension(productTable.getTableHeader().getPreferredSize().width, UIConstants.TABLE_HEADER_HEIGHT)
        );
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    /**
     * Load all products.
     */
    private void loadProducts() {
        List<ProductWithCategory> products = productController.getAllProductsWithCategories();
        updateTableModel(products);
        populateCategoryFilter();
    }
    
    /**
     * Populate the category filter dropdown.
     */
    private void populateCategoryFilter() {
        filterComboBox.removeAllItems();
        filterComboBox.addItem("All Categories");
        
        // Add unique category names
        productController.getAllProductsWithCategories().stream()
            .map(ProductWithCategory::getCategoryName)
            .distinct()
            .sorted()
            .forEach(filterComboBox::addItem);
    }
    
    /**
     * Filter products by search text and category.
     * 
     * @param searchText The search text
     */
    private void filterProducts(String searchText) {
        String selectedCategory = (String) filterComboBox.getSelectedItem();
        List<ProductWithCategory> products;
        
        // Get products by search text
        if (searchText.isEmpty()) {
            products = productController.getAllProductsWithCategories();
        } else {
            products = productController.searchProducts(searchText);
        }
        
        // Filter by category if not "All Categories"
        if (selectedCategory != null && !selectedCategory.equals("All Categories")) {
            products = products.stream()
                .filter(p -> p.getCategoryName().equals(selectedCategory))
                .toList();
        }
        
        updateTableModel(products);
    }
    
    /**
     * Update the table model with the provided products.
     * 
     * @param products The list of products to display
     */
    private void updateTableModel(List<ProductWithCategory> products) {
        // Create table model
        String[] columnNames = {"ID", "Name", "Price", "Stock", "Category", "Actions"};
        Object[][] data = new Object[products.size()][columnNames.length];
        
        // Add the products to the table
        for (int i = 0; i < products.size(); i++) {
            ProductWithCategory productWithCategory = products.get(i);
            Product product = productWithCategory.getProduct();
            data[i][0] = product.getId();
            data[i][1] = product.getName();
            data[i][2] = product.getPrice();
            data[i][3] = product.getStockQuantity();
            data[i][4] = productWithCategory.getCategoryName();
            data[i][5] = ""; // Will be filled with action buttons
        }
        
        // Create a DefaultTableModel that is not editable
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the actions column is editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class;
                } else if (columnIndex == 2) {
                    return java.math.BigDecimal.class;
                } else if (columnIndex == 3) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        
        productTable.setModel(model);
        
        // Set up custom renderer for stock column that highlights low stock
        productTable.getColumnModel().getColumn(3).setCellRenderer(new StockLevelRenderer());
        
        // Set up renderers for other columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        productTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        // Set column widths
        productTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        productTable.getColumnModel().getColumn(5).setPreferredWidth(200);
        
        // Set up the buttons renderer and editor
        productTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonsRenderer());
        productTable.getColumnModel().getColumn(5).setCellEditor(new ButtonsEditor());
    }
    
    /**
     * Custom renderer for the stock column that highlights low stock levels
     */
    private static class StockLevelRenderer extends DefaultTableCellRenderer {
        public StockLevelRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof Integer) {
                int stockLevel = (Integer) value;
                
                if (stockLevel <= 0) {
                    setText("No Stock");
                    c.setForeground(Color.RED);
                    return c;
                } else if (stockLevel < LOW_STOCK_THRESHOLD) {
                    setText("Low Stock (" + stockLevel + ")");
                    c.setForeground(new Color(255, 165, 0));
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                }
                setToolTipText(null);
            }
            
            return c;
        }
    }
    
    /**
     * Delete a product.
     * 
     * @param productId The ID of the product to delete
     */
    private void deleteProduct(int productId) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this product?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = productController.deleteProduct(productId);
                if (success) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Product deleted successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    loadProducts();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error: Product could not be deleted. It may be in use by other records.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid input: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    /**
     * Check if the current user is an admin.
     * 
     * @return True if the user is an admin, false otherwise
     */
    private boolean isAdmin() {
        return SessionManager.getCurrentUser() != null 
                && "admin".equals(SessionManager.getCurrentUser().getRole());
    }
    
    /**
     * Cell renderer for the action buttons.
     */
    private class ButtonsRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JButton viewButton;
        private final JButton editButton;
        private final JButton deleteButton;
        
        public ButtonsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setBackground(Color.WHITE);
            
            // View button
            viewButton = new JButton("View");
            viewButton.setForeground(Color.WHITE);
            viewButton.setBackground(UIConstants.INFO_COLOR);
            viewButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            viewButton.setFocusPainted(false);
            viewButton.setFont(UIConstants.SMALL_BUTTON_FONT);
            
            // Edit button
            editButton = new JButton("Edit");
            editButton.setForeground(Color.WHITE);
            editButton.setBackground(UIConstants.PRIMARY_COLOR);
            editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            editButton.setFocusPainted(false);
            editButton.setFont(UIConstants.SMALL_BUTTON_FONT);
            
            // Delete button
            deleteButton = new JButton("Delete");
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(UIConstants.DANGER_COLOR);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            deleteButton.setFocusPainted(false);
            deleteButton.setFont(UIConstants.SMALL_BUTTON_FONT);
            
            add(viewButton);
            add(editButton);
            add(deleteButton);
            
            // Disable edit and delete buttons for non-admin users
            boolean isAdminUser = isAdmin();
            editButton.setEnabled(isAdminUser);
            deleteButton.setEnabled(isAdminUser);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
    
    /**
     * Cell editor for the action buttons.
     */
    private class ButtonsEditor extends DefaultCellEditor {
        private final JPanel panel;
        private final JButton viewButton;
        private final JButton editButton;
        private final JButton deleteButton;
        private int productId;
        
        public ButtonsEditor() {
            super(new JCheckBox());
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBackground(Color.WHITE);
            
            // View button
            viewButton = new JButton("View");
            viewButton.setForeground(Color.WHITE);
            viewButton.setBackground(UIConstants.INFO_COLOR);
            viewButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            viewButton.setFocusPainted(false);
            viewButton.setFont(UIConstants.SMALL_BUTTON_FONT);
            
            // Edit button
            editButton = new JButton("Edit");
            editButton.setForeground(Color.WHITE);
            editButton.setBackground(UIConstants.PRIMARY_COLOR);
            editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            editButton.setFocusPainted(false);
            editButton.setFont(UIConstants.SMALL_BUTTON_FONT);
            
            // Delete button
            deleteButton = new JButton("Delete");
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(UIConstants.DANGER_COLOR);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            deleteButton.setFocusPainted(false);
            deleteButton.setFont(UIConstants.SMALL_BUTTON_FONT);
            
            // Add action listeners
            viewButton.addActionListener(e -> {
                fireEditingStopped();
                Router.navigate("dashboard/products/" + productId);
            });
            
            editButton.addActionListener(e -> {
                fireEditingStopped();
                Router.navigate("dashboard/products/edit/" + productId);
            });
            
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                deleteProduct(productId);
            });
            
            panel.add(viewButton);
            panel.add(editButton);
            panel.add(deleteButton);
            
            // Disable edit and delete buttons for non-admin users
            boolean isAdminUser = isAdmin();
            editButton.setEnabled(isAdminUser);
            deleteButton.setEnabled(isAdminUser);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            productId = (int) table.getValueAt(row, 0);
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
} 