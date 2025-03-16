package Components;

import Controller.StaffController;
import Model.Staff;
import Repository.StaffRepository;
import Repository.ExpenseRepository;
import Support.Router;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for displaying staff details with their expenses.
 */
public class StaffDetailsPanel extends JPanel {
    private final StaffController staffController;
    private final int staffId;
    private final Router router;
    
    /**
     * Creates a new StaffDetailsPanel.
     * 
     * @param staffId The ID of the staff member to display
     * @param router The router for navigation
     */
    public StaffDetailsPanel(int staffId, Router router) {
        this.staffController = new StaffController();
        this.staffId = staffId;
        this.router = router;
        
        initComponents();
        loadStaffData();
    }
    
    /**
     * Initialize the components.
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        setBackground(new Color(255, 255, 255));
        
        // Add a loading label initially
        JLabel loadingLabel = new JLabel("Loading staff data...");
        loadingLabel.setHorizontalAlignment(JLabel.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(loadingLabel, BorderLayout.CENTER);
    }
    
    /**
     * Load the staff data.
     */
    private void loadStaffData() {
        try {
            StaffRepository.StaffWithExpenses staffWithExpenses = 
                    staffController.getStaffWithExpensesById(staffId)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
            
            // Clear the panel
            removeAll();
            
            // Create a header with title and back button
            JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
            headerPanel.setBackground(new Color(255, 255, 255));
            
            JLabel titleLabel = new JLabel("Staff Details: " + staffWithExpenses.getStaff().getName());
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            headerPanel.add(titleLabel, BorderLayout.WEST);
            
            JButton backButton = new JButton("Back to Staff List");
            backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            backButton.addActionListener(e -> router.navigate("/staff"));
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(new Color(255, 255, 255));
            buttonPanel.add(backButton);
            headerPanel.add(buttonPanel, BorderLayout.EAST);
            
            add(headerPanel, BorderLayout.NORTH);
            
            // Create main content panel
            JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
            contentPanel.setBackground(new Color(255, 255, 255));
            
            // Create the staff info panel
            JPanel staffInfoPanel = createStaffInfoPanel(staffWithExpenses);
            contentPanel.add(staffInfoPanel, BorderLayout.NORTH);
            
            // Create expense table panel with title
            JPanel expensePanel = new JPanel(new BorderLayout(10, 10));
            expensePanel.setBackground(new Color(255, 255, 255));
            
            JLabel expensesTitle = new JLabel("Expenses");
            expensesTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            expensePanel.add(expensesTitle, BorderLayout.NORTH);
            
            // Create the expense table
            JTable expenseTable = createExpenseTable(staffWithExpenses);
            JScrollPane scrollPane = new JScrollPane(expenseTable);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            expensePanel.add(scrollPane, BorderLayout.CENTER);
            
            contentPanel.add(expensePanel, BorderLayout.CENTER);
            
            add(contentPanel, BorderLayout.CENTER);
            
            revalidate();
            repaint();
        } catch (SQLException | IllegalArgumentException ex) {
            Logger.getLogger(StaffDetailsPanel.class.getName()).log(Level.SEVERE, "Error loading staff data", ex);
            JOptionPane.showMessageDialog(this, "Error loading staff data: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Create the staff info panel.
     * 
     * @param staffWithExpenses The staff member with expenses
     * @return The staff info panel
     */
    private JPanel createStaffInfoPanel(StaffRepository.StaffWithExpenses staffWithExpenses) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 20, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Staff Information"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(new Color(255, 255, 255));
        
        // Add staff info with styled components
        Staff staff = staffWithExpenses.getStaff();
        
        addLabelPair(panel, "ID:", String.valueOf(staff.getId()));
        addLabelPair(panel, "Name:", staff.getName());
        addLabelPair(panel, "Position:", staff.getPosition());
        addLabelPair(panel, "Username:", staff.getUserName());
        addLabelPair(panel, "Role:", staff.getRole());
        addLabelPair(panel, "Total Expenses:", String.valueOf(staffWithExpenses.getExpenseCount()));
        addLabelPair(panel, "Total Amount:", staffWithExpenses.getTotalExpenseAmount().toString());
        
        return panel;
    }
    
    /**
     * Add a label pair to a panel.
     * 
     * @param panel The panel to add to
     * @param labelText The label text
     * @param valueText The value text
     */
    private void addLabelPair(JPanel panel, String labelText, String valueText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel value = new JLabel(valueText);
        value.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        panel.add(label);
        panel.add(value);
    }
    
    /**
     * Create the expense table.
     * 
     * @param staffWithExpenses The staff member with expenses
     * @return The expense table
     */
    private JTable createExpenseTable(StaffRepository.StaffWithExpenses staffWithExpenses) {
        // Create the table model
        String[] columnNames = {"ID", "Name", "Description", "Amount", "Actions"};
        Object[][] data = new Object[staffWithExpenses.getExpenses().size()][columnNames.length];
        
        // Add the expenses to the table
        for (int i = 0; i < staffWithExpenses.getExpenses().size(); i++) {
            Repository.ExpenseRepository.ExpenseWithStaff expense = staffWithExpenses.getExpenses().get(i);
            data[i][0] = expense.getExpense().getId();
            data[i][1] = expense.getExpense().getName();
            data[i][2] = expense.getExpense().getDescription();
            data[i][3] = expense.getExpense().getAmount();
            data[i][4] = "View";
        }
        
        // Create the table
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(232, 242, 254));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(57, 117, 247));
        table.getTableHeader().setForeground(new Color(255, 255, 255));
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getPreferredSize().width, 40));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Add a button renderer for the actions column
        table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        return table;
    }
    
    /**
     * A renderer for buttons in a table cell.
     */
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            setBackground(new Color(57, 117, 247));
            setForeground(Color.WHITE);
            return this;
        }
    }
    
    /**
     * An editor for buttons in a table cell.
     */
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(57, 117, 247));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Handle the button click
                if ("View".equals(label)) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        int expenseId = (int) table.getValueAt(row, 0);
                        JOptionPane.showMessageDialog(button, "Viewing expense with ID: " + expenseId);
                        
                        // In a more complete implementation, we'd navigate to an expense details view
                        // router.navigate("/expenses/" + expenseId);
                    }
                }
            }
            isPushed = false;
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
} 