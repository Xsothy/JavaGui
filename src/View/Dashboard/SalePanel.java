package View.Dashboard;

import Controller.SaleController;
import Model.SaleWithStaff;
import Support.Router;
import Support.SessionManager;
import Support.UIConstants;
import View.Layout.DashboardLayout;
import View.NavigatePanel;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for viewing and managing sales.
 */
public class SalePanel extends DashboardLayout {
    private final SaleController saleController;
    private JTable saleTable;
    private JTextField searchField;
    private JComboBox<String> durationComboBox;
    private final SimpleDateFormat dateFormat;

    /**
     * Constructor for SalePanel.
     */
    public SalePanel() {
        super();
        this.saleController = new SaleController();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void render() {
        super.render();
        loadSales();
    }

    @Override
    public NavigatePanel getContentPanel() {
        return createSaleContent();
    }

    /**
     * Create the sale management content panel.
     * 
     * @return The sale content panel
     */
    private NavigatePanel createSaleContent() {
        NavigatePanel saleContent = new NavigatePanel();
        saleContent.setLayout(new BorderLayout(0, 0));
        saleContent.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        saleContent.setBackground(Color.WHITE);

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

        JLabel titleLabel = new JLabel("Sales Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Create, view and manage sales records");
        subtitleLabel.setFont(UIConstants.SUBTITLE_FONT);
        subtitleLabel.setForeground(UIConstants.LIGHT_TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);

        // Right side of header: Search, duration filter, and Add button
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setBackground(Color.WHITE);

        // Duration filter
        JPanel durationPanel = new JPanel(new BorderLayout());
        durationPanel.setBackground(Color.WHITE);
        durationPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        String[] durations = {"All", "Today", "This Week", "Last Week", "This Month", "Last Month", "This Year"};
        durationComboBox = new JComboBox<>(durations);
        durationComboBox.setFont(UIConstants.TABLE_CONTENT_FONT);
        durationComboBox.setBackground(Color.WHITE);
        durationComboBox.setBorder(null);
        durationPanel.add(durationComboBox, BorderLayout.CENTER);

        // Search field with icon
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        searchField = new JTextField(15);
        searchField.setBorder(null);
        searchField.setFont(UIConstants.TABLE_CONTENT_FONT);

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Add button
        JButton addButton = new JButton("+ New Sale");
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

        // Add hover effect
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(UIConstants.ACCENT_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(UIConstants.PRIMARY_COLOR);
            }
        });

        addButton.addActionListener(e -> Router.navigate("dashboard/sales/create"));

        // Dashboard button
        JButton dashboardButton = new JButton("Dashboard");
        dashboardButton.setFont(UIConstants.BUTTON_FONT);
        dashboardButton.setForeground(Color.WHITE);
        dashboardButton.setBackground(UIConstants.INFO_COLOR);
        dashboardButton.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.BUTTON_PADDING_V,
                UIConstants.BUTTON_PADDING_H,
                UIConstants.BUTTON_PADDING_V,
                UIConstants.BUTTON_PADDING_H
        ));
        dashboardButton.setFocusPainted(false);
        dashboardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        dashboardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                dashboardButton.setBackground(new Color(70, 150, 195));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                dashboardButton.setBackground(UIConstants.INFO_COLOR);
            }
        });

        dashboardButton.addActionListener(e -> Router.navigate("dashboard/sales/dashboard"));

        actionsPanel.add(durationPanel);
        actionsPanel.add(searchPanel);
        actionsPanel.add(dashboardButton);
        actionsPanel.add(addButton);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(actionsPanel, BorderLayout.EAST);

        saleContent.add(headerPanel, BorderLayout.NORTH);

        // Content panel with table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.SECTION_SPACING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING
        ));

        // Create the sale table
        saleTable = new JTable();
        saleTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        saleTable.setShowGrid(true);
        saleTable.setGridColor(UIConstants.BORDER_COLOR);
        saleTable.setSelectionBackground(new Color(235, 245, 255));
        saleTable.setSelectionForeground(UIConstants.TEXT_COLOR);
        saleTable.setFont(UIConstants.TABLE_CONTENT_FONT);
        saleTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        saleTable.setFillsViewportHeight(true);

        // Style the header
        saleTable.getTableHeader().setFont(UIConstants.TABLE_HEADER_FONT);
        saleTable.getTableHeader().setBackground(UIConstants.TABLE_HEADER_BG_COLOR);
        saleTable.getTableHeader().setForeground(UIConstants.TABLE_HEADER_FG_COLOR);
        saleTable.getTableHeader().setPreferredSize(
                new Dimension(saleTable.getTableHeader().getPreferredSize().width, UIConstants.TABLE_HEADER_HEIGHT)
        );

        JScrollPane scrollPane = new JScrollPane(saleTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        saleContent.add(tablePanel, BorderLayout.CENTER);

        // Add search and filter listeners
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterSales();
            }
        });

        durationComboBox.addActionListener(e -> filterSales());

        return saleContent;
    }

    /**
     * Load and display all sales.
     */
    private void loadSales() {
        List<SaleWithStaff> saleList = saleController.getAllSalesWithStaff();
        updateTableModel(saleList);
    }

    /**
     * Filter sales based on search term and selected duration.
     */
    private void filterSales() {
        String searchTerm = searchField.getText();
        String duration = (String) durationComboBox.getSelectedItem();

        List<SaleWithStaff> filteredSales;
        
        if (searchTerm != null && !searchTerm.isEmpty()) {
            filteredSales = saleController.searchSales(searchTerm);
        } else {
            filteredSales = saleController.getAllSalesWithStaff();
        }

        // TODO: Implement date filtering based on selected duration

        updateTableModel(filteredSales);
    }

    /**
     * Update the table model with the provided sale list.
     * 
     * @param saleList The list of sales to display
     */
    private void updateTableModel(List<SaleWithStaff> saleList) {
        // Create table model
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the Actions column is editable
            }
        };

        // Add columns
        model.addColumn("ID");
        model.addColumn("Date");
        model.addColumn("Staff");
        model.addColumn("Items");
        model.addColumn("Total");
        model.addColumn("Actions");

        // Add rows
        for (SaleWithStaff sale : saleList) {
            model.addRow(new Object[]{
                sale.getSale().getId(),
                dateFormat.format(sale.getSale().getDate()),
                sale.getStaffName(),
                "View Details", // Placeholder, we would need to fetch details for this
                "$" + sale.getSale().getTotal(),
                "Actions" // Placeholder for the buttons
            });
        }

        // Set the model
        saleTable.setModel(model);

        // Set column widths
        saleTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        saleTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Date
        saleTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Staff
        saleTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Items
        saleTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Total
        saleTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Actions

        // Center-align all columns except the last one
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < saleTable.getColumnCount() - 1; i++) {
            saleTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set button renderer and editor for the Actions column
        saleTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonsRenderer());
        saleTable.getColumnModel().getColumn(5).setCellEditor(new ButtonsEditor());
    }

    /**
     * Delete a sale.
     * 
     * @param saleId The ID of the sale to delete
     */
    private void deleteSale(int saleId) {
        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this sale? This will restore product quantities.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            saleController.deleteSale(saleId);
            JOptionPane.showMessageDialog(null, "Sale deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadSales();
        }
    }

    /**
     * Renderer for the buttons in the Actions column.
     */
    private static class ButtonsRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JButton viewButton;
        private final JButton deleteButton;

        public ButtonsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);

            viewButton = new JButton("View");
            viewButton.setFont(UIConstants.BUTTON_FONT.deriveFont(11f));
            viewButton.setForeground(Color.WHITE);
            viewButton.setBackground(UIConstants.PRIMARY_COLOR);
            viewButton.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
            viewButton.setFocusPainted(false);

            deleteButton = new JButton("Delete");
            deleteButton.setFont(UIConstants.BUTTON_FONT.deriveFont(11f));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(UIConstants.DANGER_COLOR);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
            deleteButton.setFocusPainted(false);

            add(viewButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }

    /**
     * Editor for the buttons in the Actions column.
     */
    private class ButtonsEditor extends DefaultCellEditor {
        private final JPanel panel;
        private final JButton viewButton;
        private final JButton deleteButton;
        private int saleId;

        public ButtonsEditor() {
            super(new JTextField());
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);

            // View button
            viewButton = createSimpleButton("View", UIConstants.PRIMARY_COLOR);
            viewButton.addActionListener(e -> {
                fireEditingStopped();
                Router.navigate("dashboard/sales/" + saleId);
            });

            // Delete button
            deleteButton = createSimpleButton("Delete", UIConstants.DANGER_COLOR);
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                deleteSale(saleId);
            });

            panel.add(viewButton);
            panel.add(deleteButton);
        }

        private JButton createSimpleButton(String text, Color bgColor) {
            JButton button = new JButton(text);
            button.setFont(UIConstants.BUTTON_FONT.deriveFont(11f));
            button.setForeground(Color.WHITE);
            button.setBackground(bgColor);
            button.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return button;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            saleId = (int) table.getValueAt(row, 0);
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }
} 