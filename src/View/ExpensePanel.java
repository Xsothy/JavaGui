package View;

import Controller.ExpenseController;
import Model.ExpenseWithStaff;
import Model.Staff;
import Support.Router;
import Support.SessionManager;
import Support.UIConstants;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for displaying and managing expenses.
 */
public class ExpensePanel extends NavigatePanel {
    private final ExpenseController expenseController;
    private final Router router;
    private JTable expenseTable;
    private JTextField searchField;
    private JComboBox<String> durationComboBox; // Added ComboBox
    /**
     * Creates a new ExpensePanel.
     * 
     * @param router The router for navigation
     */
    public ExpensePanel(Router router) {
        this.expenseController = new ExpenseController();
        this.router = router;

        initComponents();
        loadExpenses();
    }

    @Override
    public void render()
    {
        loadExpenses();
    }

    /**
     * Initialize the components.
     */
    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setBackground(Color.WHITE);
        
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
        
        // Right side of header: Search and Add button
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setBackground(Color.WHITE);

        String[] durationOptions = {"All", "This Week", "Last Week", "This Month", "Last Month", "This Year"}; // Added options
        durationComboBox = new JComboBox<>(durationOptions);
        durationComboBox.setFont(UIConstants.TABLE_CONTENT_FONT);
        durationComboBox.setBackground(Color.WHITE);
        durationComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
                BorderFactory.createEmptyBorder(0,0,0,0)
        ));
        actionsPanel.add(durationComboBox);

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
        
        addButton.addActionListener(e -> router.navigate("/expenses/add"));
        
        actionsPanel.add(searchPanel);
        actionsPanel.add(addButton);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(actionsPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Content panel with table
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(
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
        expenseTable.getTableHeader().setBackground(UIConstants.PRIMARY_COLOR);
        expenseTable.getTableHeader().setForeground(Color.WHITE);
        expenseTable.getTableHeader().setPreferredSize(
            new Dimension(expenseTable.getTableHeader().getPreferredSize().width, UIConstants.TABLE_HEADER_HEIGHT)
        );
        
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Add search listener
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterExpenses();
            }
        });


        // add listener to ducation
        durationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterExpenses();
            }
        });

    }
    
    /**
     * Load the expenses from the database.
     */
    private void loadExpenses() {
        List<ExpenseWithStaff> expenses = expenseController.getAllExpensesWithStaff();
        updateTableModel(expenses);
    }


    private void filterExpenses()
    {
        String searchText = searchField.getText().trim();
        String selectedDuration = (String) durationComboBox.getSelectedItem();
        List<ExpenseWithStaff> expenses = expenseController.filterExpense(searchText, selectedDuration);
        updateTableModel(expenses);
    }
    
    /**
     * Update the table model with the provided expenses.
     * 
     * @param expenses The list of expenses to display
     */
    private void updateTableModel(List<ExpenseWithStaff> expenses) {
        // Create the table model
        String[] columnNames = {"ID", "Name", "Date", "Amount", "Staff", "Actions"};
        Object[][] data = new Object[expenses.size()][columnNames.length];
        
        // Populate the data
        for (int i = 0; i < expenses.size(); i++) {
            ExpenseWithStaff expense = expenses.get(i);
            data[i][0] = expense.getExpense().getId();
            data[i][1] = expense.getExpense().getName();
            data[i][2] = DateFormat.getDateInstance().format(expense.getExpense().getDate());
            data[i][3] = String.format("$%.2f", expense.getExpense().getAmount());
            data[i][4] = expense.getStaffName();
            data[i][5] = ""; // Will be replaced with buttons
        }
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only make the actions column editable
            }
        };
        
        expenseTable.setModel(model);
        
        // Set relative column widths as percentages
        int totalWidth = expenseTable.getParent().getWidth();
        if (totalWidth > 0) {
            expenseTable.getColumnModel().getColumn(0).setPreferredWidth((int)(totalWidth * 0.05));  // ID (5%)
            expenseTable.getColumnModel().getColumn(1).setPreferredWidth((int)(totalWidth * 0.20));  // Name (20%)
            expenseTable.getColumnModel().getColumn(2).setPreferredWidth((int)(totalWidth * 0.30));  // Date (20%)
            expenseTable.getColumnModel().getColumn(3).setPreferredWidth((int)(totalWidth * 0.15));  // Amount (20%)
            expenseTable.getColumnModel().getColumn(4).setPreferredWidth((int)(totalWidth * 0.15));  // Staff (20%)
            expenseTable.getColumnModel().getColumn(5).setPreferredWidth((int)(totalWidth * 0.15));  // Actions (15%)
        }
        
        // Set the action buttons in the last column
        expenseTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonsRenderer());
        expenseTable.getColumnModel().getColumn(5).setCellEditor(new ButtonsEditor(new JCheckBox()));
        
        // Center the ID and Amount columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        expenseTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        expenseTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
    }
    
    /**
     * A renderer for buttons in a table cell.
     */
    private static class ButtonsRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JButton editButton;
        private final JButton deleteButton;

        public ButtonsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);
            
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
            
            add(editButton);
            add(deleteButton);

            Staff user = SessionManager.getCurrentUser();

            if(!user.getRole().equals("admin"))
            {
                deleteButton.setEnabled(false);
                editButton.setEnabled(false);
            }

        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            
            // Center the panel vertically
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
        private final JButton editButton;
        private final JButton deleteButton;
        private int expenseId;
        
        public ButtonsEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);
            
            // Edit button
            editButton = new JButton("Edit");
            editButton.setForeground(Color.WHITE);
            editButton.setBackground(UIConstants.PRIMARY_COLOR);
            editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            editButton.setFocusPainted(false);
            editButton.setFont(UIConstants.SMALL_BUTTON_FONT);
            
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
                fireEditingStopped();

                router.navigate("/expenses/edit/" + expenseId);
            });
            
            // Delete button
            deleteButton = new JButton("Delete");
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(UIConstants.DANGER_COLOR);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            deleteButton.setFocusPainted(false);
            deleteButton.setFont(UIConstants.SMALL_BUTTON_FONT);
            
            // Add hover effect
            deleteButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    deleteButton.setBackground(new Color(220, 38, 38)); // Darker red
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    deleteButton.setBackground(UIConstants.DANGER_COLOR);
                }
            });
            
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                
                // Ask for confirmation
                int confirm = JOptionPane.showConfirmDialog(
                    panel,
                    "Are you sure you want to delete this expense?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    expenseController.deleteExpense(expenseId);
                    loadExpenses();
                }
            });
            
            panel.add(editButton);
            panel.add(deleteButton);

            Staff user = SessionManager.getCurrentUser();

            if(!user.getRole().equals("admin"))
            {
                deleteButton.setEnabled(false);
                editButton.setEnabled(false);
            }
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            expenseId = (int) table.getValueAt(row, 0); // Get the ID from the first column
            panel.setBackground(table.getSelectionBackground());
            
            // Center the panel vertically
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