package View.Dashboard;

import Controller.CategoryController;
import Model.Category;
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
 * Panel for viewing and managing product categories.
 */
public class CategoryPanel extends DashboardLayout {
    private final CategoryController categoryController;
    private JTable categoryTable;
    private JTextField searchField;

    /**
     * Constructor for CategoryPanel.
     */
    public CategoryPanel() {
        super();
        this.categoryController = new CategoryController();
    }

    @Override
    public void render() {
        super.render();
        loadCategories();
    }

    @Override
    public NavigatePanel getContentPanel() {
        NavigatePanel contentPanel = new NavigatePanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        contentPanel.setBackground(Color.WHITE);

        // Create header panel with title and add button
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

        // Left side of header: Title and subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Category Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Add, edit or delete product categories");
        subtitleLabel.setFont(UIConstants.SUBTITLE_FONT);
        subtitleLabel.setForeground(UIConstants.LIGHT_TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);

        // Right side of header: Search and Add button
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setBackground(Color.WHITE);

        // Search field with icon
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel searchIcon = new JLabel("\uD83D\uDD0D"); // Unicode magnifying glass
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        searchField = new JTextField(15);
        searchField.setBorder(null);
        searchField.setFont(UIConstants.TABLE_CONTENT_FONT);

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Add button
        JButton addButton = new JButton("+ Add Category");
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

        // Only admin can add categories
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

        addButton.addActionListener(e -> Router.navigate("dashboard/categories/add"));

        actionsPanel.add(searchPanel);
        actionsPanel.add(addButton);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(actionsPanel, BorderLayout.EAST);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel with table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SECTION_SPACING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING
        ));

        // Create the category table
        categoryTable = new JTable();
        categoryTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        categoryTable.setShowGrid(true);
        categoryTable.setGridColor(UIConstants.BORDER_COLOR);
        categoryTable.setSelectionBackground(UIConstants.TABLE_SELECTION_BG_COLOR);
        categoryTable.setSelectionForeground(UIConstants.TABLE_SELECTION_FG_COLOR);
        categoryTable.setFont(UIConstants.TABLE_CONTENT_FONT);
        categoryTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        categoryTable.setFillsViewportHeight(true);

        // Style the header
        categoryTable.getTableHeader().setFont(UIConstants.TABLE_HEADER_FONT);
        categoryTable.getTableHeader().setBackground(UIConstants.TABLE_HEADER_BG_COLOR);
        categoryTable.getTableHeader().setForeground(UIConstants.TABLE_HEADER_FG_COLOR);
        categoryTable.getTableHeader().setPreferredSize(
            new Dimension(categoryTable.getTableHeader().getPreferredSize().width, UIConstants.TABLE_HEADER_HEIGHT)
        );

        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Add search listener
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = searchField.getText().trim();
                filterCategories(searchText);
            }
        });

        return contentPanel;
    }

    /**
     * Load categories from the database.
     */
    private void loadCategories() {
        List<Category> categories = categoryController.getAllCategories();
        updateTableModel(categories);
    }

    /**
     * Filter categories based on search text.
     *
     * @param searchText The search text
     */
    private void filterCategories(String searchText) {
        List<Category> categories = categoryController.getAllCategories();
        
        // If search text is empty, show all categories
        if (searchText.isEmpty()) {
            updateTableModel(categories);
            return;
        }
        
        // Filter categories by name
        List<Category> filteredCategories = categories.stream()
            .filter(category -> category.getName().toLowerCase().contains(searchText.toLowerCase()))
            .toList();
        
        updateTableModel(filteredCategories);
    }

    /**
     * Update the table model with the given categories.
     *
     * @param categories The list of categories to display
     */
    private void updateTableModel(List<Category> categories) {
        // Create table model
        String[] columnNames = {"ID", "Name", "Actions"};
        Object[][] data = new Object[categories.size()][columnNames.length];

        // Add the categories to the table
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            data[i][0] = category.getId();
            data[i][1] = category.getName();
            data[i][2] = ""; // Will be filled with action buttons
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only make the actions column editable
            }
        };

        categoryTable.setModel(model);

        // Set column widths
        int totalWidth = categoryTable.getParent() != null ? categoryTable.getParent().getWidth() : 800;
        categoryTable.getColumnModel().getColumn(0).setPreferredWidth((int)(totalWidth * 0.1));  // ID
        categoryTable.getColumnModel().getColumn(1).setPreferredWidth((int)(totalWidth * 0.5));  // Name
        categoryTable.getColumnModel().getColumn(2).setPreferredWidth((int)(totalWidth * 0.4));  // Actions

        // Set the action buttons in the last column
        categoryTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonsRenderer());
        categoryTable.getColumnModel().getColumn(2).setCellEditor(new ButtonsEditor());

        // Center the ID column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        categoryTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    }
    
    /**
     * Delete a category.
     *
     * @param categoryId The category ID to delete
     */
    private void deleteCategory(int categoryId) {
        try {
            boolean deleted = categoryController.deleteCategory(categoryId);
            if (deleted) {
                loadCategories();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error: Category could not be deleted. It may be in use by products.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, 
                "Invalid input: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Check if the current user is an admin.
     * 
     * @return true if the user is an admin, false otherwise
     */
    private boolean isAdmin() {
        return SessionManager.getCurrentUser() != null 
                && "admin".equals(SessionManager.getCurrentUser().getRole());
    }

    /**
     * A simplified renderer for buttons in a table cell.
     */
    private class ButtonsRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JButton viewButton;
        private final JButton editButton;
        private final JButton deleteButton;

        public ButtonsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);

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
            
            // Only admin can edit/delete
            boolean isAdmin = isAdmin();
            editButton.setEnabled(isAdmin);
            deleteButton.setEnabled(isAdmin);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }

            int height = table.getRowHeight(row);
            int buttonHeight = editButton.getPreferredSize().height;
            int padding = Math.max(0, (height - buttonHeight) / 2);
            setBorder(BorderFactory.createEmptyBorder(padding, 0, padding, 0));

            return this;
        }
    }

    /**
     * An editor for buttons in a table cell.
     */
    private class ButtonsEditor extends DefaultCellEditor {
        private final JPanel panel;
        private final JButton viewButton;
        private final JButton editButton;
        private final JButton deleteButton;
        private int categoryId;

        public ButtonsEditor() {
            super(new JCheckBox());
            
            panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);
            
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
                Router.navigate("dashboard/categories/" + categoryId);
            });
            
            editButton.addActionListener(e -> {
                fireEditingStopped();
                Router.navigate("dashboard/categories/edit/" + categoryId);
            });
            
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                
                int confirm = JOptionPane.showConfirmDialog(
                    panel,
                    "Are you sure you want to delete this category?\nThis will also remove the category from all products.",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteCategory(categoryId);
                }
            });
            
            panel.add(viewButton);
            panel.add(editButton);
            panel.add(deleteButton);
            
            // Only admin can edit/delete
            boolean isAdmin = isAdmin();
            editButton.setEnabled(isAdmin);
            deleteButton.setEnabled(isAdmin);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            // Safety check
            if (row < 0 || row >= table.getRowCount() || table.getValueAt(row, 0) == null) {
                return panel;
            }
            
            categoryId = (int) table.getValueAt(row, 0);
            
            panel.setBackground(table.getSelectionBackground());
            
            // Center vertically
            int height = table.getRowHeight(row);
            int buttonHeight = editButton.getPreferredSize().height;
            int padding = Math.max(0, (height - buttonHeight) / 2);
            panel.setBorder(BorderFactory.createEmptyBorder(padding, 0, padding, 0));
            
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
} 