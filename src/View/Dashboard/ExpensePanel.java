package View.Dashboard;

import Controller.ExpenseController;
import Model.ExpenseWithStaff;
import Support.Router;
import Support.SessionManager;
import Support.UIConstants;
import View.DashboardPanel;
import View.Layout.DashboardLayout;
import View.NavigatePanel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for viewing and managing expenses.
 */
public class ExpensePanel extends DashboardLayout {
    private final ExpenseController expenseController;
    private JTable expenseTable;
    private JTextField searchField;
    private JComboBox<String> durationComboBox;

    public ExpensePanel() {
        super();
        this.expenseController = new ExpenseController();
    }

    @Override
    public void render() {
        super.render();
        loadExpenses();
    }

    public NavigatePanel getContentPanel() {
        return createExpenseContent();
    }
    /**
     * Create the expense management content panel.
     * 
     * @return The expense content panel
     */
    private NavigatePanel createExpenseContent() {
        NavigatePanel expenseContent = new NavigatePanel();
        expenseContent.setLayout(new BorderLayout(0, 0));
        expenseContent.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        expenseContent.setBackground(Color.WHITE);

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

        JLabel titleLabel = new JLabel("Expense Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Add, edit or delete expense records");
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

        String[] durations = {"All", "This Week", "Last Week", "This Month", "Last Month", "This Year"};
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

        JLabel searchIcon = new JLabel("");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        searchField = new JTextField(15);
        searchField.setBorder(null);
        searchField.setFont(UIConstants.TABLE_CONTENT_FONT);

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Add button
        JButton addButton = new JButton("+ Add Expense");
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

        addButton.addActionListener(e -> Router.navigate("dashboard/expenses/add"));

        actionsPanel.add(durationPanel);
        actionsPanel.add(searchPanel);
        actionsPanel.add(addButton);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(actionsPanel, BorderLayout.EAST);

        expenseContent.add(headerPanel, BorderLayout.NORTH);

        // Content panel with table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.SECTION_SPACING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING
        ));

        // Create the expense table
        expenseTable = new JTable();
        expenseTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        expenseTable.setShowGrid(true);
        expenseTable.setGridColor(UIConstants.BORDER_COLOR);
        expenseTable.setSelectionBackground(new Color(235, 245, 255));
        expenseTable.setSelectionForeground(UIConstants.TEXT_COLOR);
        expenseTable.setFont(UIConstants.TABLE_CONTENT_FONT);
        expenseTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        expenseTable.setFillsViewportHeight(true);

        // Style the header
        expenseTable.getTableHeader().setFont(UIConstants.TABLE_HEADER_FONT);
        expenseTable.getTableHeader().setBackground(UIConstants.TABLE_HEADER_BG_COLOR);
        expenseTable.getTableHeader().setForeground(UIConstants.TABLE_HEADER_FG_COLOR);
        expenseTable.getTableHeader().setPreferredSize(
                new Dimension(expenseTable.getTableHeader().getPreferredSize().width, UIConstants.TABLE_HEADER_HEIGHT)
        );

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        expenseContent.add(tablePanel, BorderLayout.CENTER);

        // Add search listener
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterExpenses();
            }
        });
        
        // Add duration filter listener
        durationComboBox.addActionListener(e -> filterExpenses());
        
        return expenseContent;
    }

    /**
     * Load all expenses from the database.
     */
    private void loadExpenses() {
        List<ExpenseWithStaff> expenses = expenseController.getAllExpensesWithStaff();
        updateTableModel(expenses);
    }
    
    /**
     * Filter expenses based on search text and duration.
     */
    private void filterExpenses() {
        String searchText = searchField.getText().trim();
        String selectedDuration = (String) durationComboBox.getSelectedItem();
        
        List<ExpenseWithStaff> expenses = expenseController.filterExpense(searchText, selectedDuration);
        updateTableModel(expenses);
    }

    /**
     * Update the table model with the provided expenses.
     *
     * @param expenseList The list of expenses to display
     */
    private void updateTableModel(List<ExpenseWithStaff> expenseList) {
        // Create the table model
        String[] columnNames = {"ID", "Name", "Description", "Amount", "Staff", "Date", "Actions"};
        Object[][] data = new Object[expenseList.size()][columnNames.length];

        // Populate the data
        for (int i = 0; i < expenseList.size(); i++) {
            ExpenseWithStaff expense = expenseList.get(i);
            data[i][0] = expense.getExpense().getId();
            data[i][1] = expense.getExpense().getName();
            data[i][2] = expense.getExpense().getDescription();
            data[i][3] = "$" + expense.getExpense().getAmount();
            data[i][4] = expense.getStaffName();
            data[i][5] = expense.getExpense().getDate();
            data[i][6] = ""; // Will be replaced with buttons
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only make the actions column editable
            }
        };

        expenseTable.setModel(model);

        // Set relative column widths
        int totalWidth = expenseTable.getParent().getWidth();
        if (totalWidth > 0) {
            expenseTable.getColumnModel().getColumn(0).setPreferredWidth((int)(totalWidth * 0.05));  // ID (5%)
            expenseTable.getColumnModel().getColumn(1).setPreferredWidth((int)(totalWidth * 0.15));  // Name (15%)
            expenseTable.getColumnModel().getColumn(2).setPreferredWidth((int)(totalWidth * 0.25));  // Description (25%)
            expenseTable.getColumnModel().getColumn(3).setPreferredWidth((int)(totalWidth * 0.10));  // Amount (10%)
            expenseTable.getColumnModel().getColumn(4).setPreferredWidth((int)(totalWidth * 0.15));  // Staff (15%)
            expenseTable.getColumnModel().getColumn(5).setPreferredWidth((int)(totalWidth * 0.10));  // Date (10%)
            expenseTable.getColumnModel().getColumn(6).setPreferredWidth((int)(totalWidth * 0.20));  // Actions (20%)
        }

        // Set the action buttons in the last column
        expenseTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonsRenderer());
        expenseTable.getColumnModel().getColumn(6).setCellEditor(new ButtonsEditor());

        // Center the ID and Amount columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        expenseTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        expenseTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
    }

    /**
     * Delete an expense.
     *
     * @param expenseId The ID of the expense to delete
     */
    private void deleteExpense(int expenseId) {
        try {
            expenseController.deleteExpense(expenseId);
            JOptionPane.showMessageDialog(this, 
                "Expense deleted successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            loadExpenses();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * A renderer for the buttons in the actions column.
     */
    private static class ButtonsRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
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
     * An editor for the buttons in the actions column.
     */
    private class ButtonsEditor extends DefaultCellEditor {
        private final JPanel panel;
        private final JButton viewButton;
        private final JButton editButton;
        private final JButton deleteButton;
        private int expenseId;

        public ButtonsEditor() {
            super(new JCheckBox());

            panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);

            // Create simple buttons
            viewButton = createSimpleButton("View", UIConstants.INFO_COLOR);
            editButton = createSimpleButton("Edit", UIConstants.PRIMARY_COLOR);
            deleteButton = createSimpleButton("Delete", UIConstants.DANGER_COLOR);

            // Add action listeners
            viewButton.addActionListener(e -> {
                fireEditingStopped();
                if (expenseId > 0) {
                    Router.navigate("dashboard/expenses/" + expenseId);
                }
            });

            editButton.addActionListener(e -> {
                fireEditingStopped();
                if (expenseId > 0) {
                    Router.navigate("dashboard/expenses/edit/" + expenseId);
                }
            });

            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                
                if (expenseId > 0) {
                    int confirm = JOptionPane.showConfirmDialog(
                        panel,
                        "Are you sure you want to delete this expense?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteExpense(expenseId);
                    }
                }
            });

            panel.add(viewButton);
            panel.add(editButton);
            panel.add(deleteButton);
        }

        private JButton createSimpleButton(String text, Color bgColor) {
            JButton button = new JButton(text);
            button.setForeground(Color.WHITE);
            button.setBackground(bgColor);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setFocusPainted(false);
            button.setFont(UIConstants.SMALL_BUTTON_FONT);
            return button;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (row >= 0 && row < table.getRowCount() && table.getValueAt(row, 0) != null) {
                expenseId = (int) table.getValueAt(row, 0);
            } else {
                expenseId = -1;
            }

            panel.setBackground(table.getSelectionBackground());
            
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
} 