package View.Dashboard;

import Controller.ProductController;
import Controller.SaleController;
import Model.Product;
import Model.ProductWithCategory;
import Support.Router;
import Support.UIConstants;
import View.Layout.DashboardLayout;
import View.NavigatePanel;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for creating a new sale.
 */
public class SaleFormPanel extends DashboardLayout {
    private final SaleController saleController;
    private final ProductController productController;
    private JTable cartTable;
    private JComboBox<ProductWithCategory> productComboBox;
    private JSpinner quantitySpinner;
    private JLabel totalLabel;
    private final List<Map<String, Object>> cartItems;
    private BigDecimal totalAmount;
    
    /**
     * Constructor for SaleFormPanel.
     */
    public SaleFormPanel() {
        super();
        this.saleController = new SaleController();
        this.productController = new ProductController();
        this.cartItems = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
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
        
        // Create main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, UIConstants.SECTION_SPACING));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.SECTION_SPACING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING
        ));
        
        // Create product selection panel
        JPanel productPanel = createProductSelectionPanel();
        mainPanel.add(productPanel, BorderLayout.NORTH);
        
        // Create cart panel
        JPanel cartPanel = createCartPanel();
        mainPanel.add(cartPanel, BorderLayout.CENTER);
        
        // Create checkout panel
        JPanel checkoutPanel = createCheckoutPanel();
        mainPanel.add(checkoutPanel, BorderLayout.SOUTH);
        
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        
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
        
        // Left side: Back button
        JButton backButton = new JButton("Back to Sales");
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
        
        backButton.addActionListener(e -> Router.navigate("dashboard/sales"));
        
        // Right side: Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Create New Sale");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Add products to cart and complete sale");
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
     * Create the product selection panel.
     * 
     * @return The product selection panel
     */
    private JPanel createProductSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Section title
        JLabel titleLabel = new JLabel("Add Products to Cart");
        titleLabel.setFont(UIConstants.SUBTITLE_FONT.deriveFont(Font.BOLD));
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Product dropdown
        JLabel productLabel = new JLabel("Select Product:");
        productLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        
        // Load products
        List<ProductWithCategory> products = productController.getAllProductsWithCategories();
        
        // Create a custom renderer for the product dropdown
        productComboBox = new JComboBox<>(products.toArray(new ProductWithCategory[0]));
        productComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof ProductWithCategory) {
                    ProductWithCategory product = (ProductWithCategory) value;
                    String categoryName = product.getCategory() != null ? product.getCategory().getName() : "Uncategorized";
                    value = product.getProduct().getName() + " - $" + product.getProduct().getPrice() + " (" + categoryName + ")";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        
        // Quantity spinner
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.setFont(UIConstants.TABLE_CONTENT_FONT);
        
        // Add to cart button
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setFont(UIConstants.BUTTON_FONT);
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setBackground(UIConstants.SUCCESS_COLOR);
        addToCartButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        addToCartButton.setFocusPainted(false);
        addToCartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addToCartButton.addActionListener(e -> addToCart());
        
        // Add components to the form
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        formPanel.add(productLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        formPanel.add(productComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        formPanel.add(quantityLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.6;
        formPanel.add(quantitySpinner, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.2;
        formPanel.add(addToCartButton, gbc);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create the cart panel.
     * 
     * @return The cart panel
     */
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Section title
        JLabel titleLabel = new JLabel("Shopping Cart");
        titleLabel.setFont(UIConstants.SUBTITLE_FONT.deriveFont(Font.BOLD));
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        
        // Create table model
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the Actions column is editable
            }
        };
        
        // Add columns
        model.addColumn("Product ID");
        model.addColumn("Product Name");
        model.addColumn("Price");
        model.addColumn("Quantity");
        model.addColumn("Total");
        model.addColumn("Actions");
        
        // Create and configure table
        cartTable = new JTable(model);
        cartTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        cartTable.setShowGrid(true);
        cartTable.setGridColor(UIConstants.BORDER_COLOR);
        cartTable.setFont(UIConstants.TABLE_CONTENT_FONT);
        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        cartTable.setFillsViewportHeight(true);
        
        // Style the header
        cartTable.getTableHeader().setFont(UIConstants.TABLE_HEADER_FONT);
        cartTable.getTableHeader().setBackground(UIConstants.PRIMARY_COLOR);
        cartTable.getTableHeader().setForeground(Color.WHITE);
        cartTable.getTableHeader().setPreferredSize(
            new Dimension(cartTable.getTableHeader().getPreferredSize().width, UIConstants.TABLE_HEADER_HEIGHT)
        );
        
        // Set column widths
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Product ID
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Product Name
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(80); // Price
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(80); // Quantity
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(80); // Total
        cartTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Actions
        
        // Center-align all columns except the last one
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 5; i++) {
            cartTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Set button renderer and editor for the Actions column
        cartTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        cartTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JTextField()));
        
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create the checkout panel.
     * 
     * @return The checkout panel
     */
    private JPanel createCheckoutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Left side: Total amount
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(Color.WHITE);
        
        JLabel totalTextLabel = new JLabel("Total Amount: ");
        totalTextLabel.setFont(UIConstants.SUBTITLE_FONT.deriveFont(Font.BOLD));
        totalTextLabel.setForeground(UIConstants.TEXT_COLOR);
        
        totalLabel = new JLabel("$0.00");
        totalLabel.setFont(UIConstants.SUBTITLE_FONT.deriveFont(Font.BOLD));
        totalLabel.setForeground(UIConstants.SUCCESS_COLOR);
        
        totalPanel.add(totalTextLabel);
        totalPanel.add(totalLabel);
        
        // Right side: Complete Sale button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton completeButton = new JButton("Complete Sale");
        completeButton.setFont(UIConstants.BUTTON_FONT);
        completeButton.setForeground(Color.WHITE);
        completeButton.setBackground(UIConstants.SUCCESS_COLOR);
        completeButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        completeButton.setFocusPainted(false);
        completeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        completeButton.addActionListener(e -> completeSale());
        
        buttonPanel.add(completeButton);
        
        panel.add(totalPanel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Add the selected product to the cart.
     */
    private void addToCart() {
        if (productComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Please select a product", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ProductWithCategory selectedProduct = (ProductWithCategory) productComboBox.getSelectedItem();
        Product product = selectedProduct.getProduct();
        int quantity = (int) quantitySpinner.getValue();
        
        // Check if there's enough stock
        if (product.getStockQuantity() < quantity) {
            JOptionPane.showMessageDialog(null, "Not enough stock available. Only " + product.getStockQuantity() + " items left.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if the product is already in the cart
        for (Map<String, Object> item : cartItems) {
            int productId = (int) item.get("productId");
            if (productId == product.getId()) {
                // Update quantity instead of adding a new row
                int currentQuantity = (int) item.get("quantity");
                int newQuantity = currentQuantity + quantity;
                
                // Check if the new quantity exceeds available stock
                if (product.getStockQuantity() < newQuantity) {
                    JOptionPane.showMessageDialog(null, "Not enough stock available. Only " + product.getStockQuantity() + " items left.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                item.put("quantity", newQuantity);
                updateCartTable();
                return;
            }
        }
        
        // If not already in cart, add as new item
        BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(quantity));
        
        Map<String, Object> newItem = new HashMap<>();
        newItem.put("productId", product.getId());
        newItem.put("productName", product.getName());
        newItem.put("price", product.getPrice());
        newItem.put("quantity", quantity);
        newItem.put("total", itemTotal);
        
        cartItems.add(newItem);
        
        // Update the cart table
        updateCartTable();
    }
    
    /**
     * Update the cart table with the current cart items.
     */
    private void updateCartTable() {
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        model.setRowCount(0); // Clear the table
        
        totalAmount = BigDecimal.ZERO;
        
        for (Map<String, Object> item : cartItems) {
            int productId = (int) item.get("productId");
            String productName = (String) item.get("productName");
            BigDecimal price = (BigDecimal) item.get("price");
            int quantity = (int) item.get("quantity");
            BigDecimal total = price.multiply(new BigDecimal(quantity));
            
            // Update the item's total
            item.put("total", total);
            
            // Add row to table
            model.addRow(new Object[]{
                productId,
                productName,
                "$" + price,
                quantity,
                "$" + total,
                "Remove"
            });
            
            // Add to total amount
            totalAmount = totalAmount.add(total);
        }
        
        // Update the total label
        totalLabel.setText("$" + totalAmount);
    }
    
    /**
     * Complete the sale.
     */
    private void completeSale() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Cart is empty. Please add products to complete the sale.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int saleId = saleController.createSale(cartItems);
            
            if (saleId > 0) {
                JOptionPane.showMessageDialog(null, "Sale completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                Router.navigate("dashboard/sales/" + saleId);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to create the sale. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Remove an item from the cart.
     * 
     * @param row The row index in the cart table
     */
    private void removeFromCart(int row) {
        if (row >= 0 && row < cartItems.size()) {
            cartItems.remove(row);
            updateCartTable();
        }
    }
    
    /**
     * Renderer for the Remove button in the cart table.
     */
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("Remove");
            setFont(UIConstants.BUTTON_FONT.deriveFont(11f));
            setForeground(Color.WHITE);
            setBackground(UIConstants.DANGER_COLOR);
            setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
            setFocusPainted(false);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    /**
     * Editor for the Remove button in the cart table.
     */
    private class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        
        public ButtonEditor(JTextField textField) {
            super(textField);
            
            button = new JButton("Remove");
            button.setFont(UIConstants.BUTTON_FONT.deriveFont(11f));
            button.setForeground(Color.WHITE);
            button.setBackground(UIConstants.DANGER_COLOR);
            button.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            button.addActionListener(e -> {
                fireEditingStopped();
                removeFromCart(cartTable.getSelectedRow());
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "Remove";
        }
    }
} 