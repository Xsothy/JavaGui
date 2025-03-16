import Controller.StaffController;
import Repository.StaffRepository;
import java.awt.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * A view for displaying staff details with their expenses.
 */
public class frmStaffDetailsView extends JPanel {
    private final StaffController staffController;
    private final int staffId;
    
    /**
     * Create a new StaffDetailsView.
     * 
     * @param staffId The ID of the staff member to display
     */
    public frmStaffDetailsView(int staffId) {
        this.staffController = new StaffController();
        this.staffId = staffId;
        
        initComponents();
        loadStaffData();
    }
    
    /**
     * Initialize the components.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add a loading label initially
        JLabel loadingLabel = new JLabel("Loading staff data...");
        loadingLabel.setHorizontalAlignment(JLabel.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 16));
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
            
            // Create the staff info panel
            JPanel staffInfoPanel = createStaffInfoPanel(staffWithExpenses);
            add(staffInfoPanel, BorderLayout.NORTH);
            
            // Create the expense table
            JTable expenseTable = createExpenseTable(staffWithExpenses);
            JScrollPane scrollPane = new JScrollPane(expenseTable);
            add(scrollPane, BorderLayout.CENTER);
            
            // Add a back button
            JButton backButton = new JButton("Back to Staff List");
            backButton.addActionListener(e -> {
                // Get the parent container
                Container parent = getParent();
                
                // If the parent is using a CardLayout, show the staff list card
                if (parent.getLayout() instanceof CardLayout layout) {
                    layout.show(parent, "staffList");
                }
                // If the parent is using the Router, navigate to the staff list
                else if (parent instanceof JPanel) {
                    Support.Router router = Support.Router.getInstance((JPanel) parent);
                    if (router.hasRoute("/staff")) {
                        router.navigate("/staff");
                    }
                }
            });
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            buttonPanel.add(backButton);
            add(buttonPanel, BorderLayout.SOUTH);
            
            revalidate();
            repaint();
        } catch (SQLException | IllegalArgumentException ex) {
            Logger.getLogger(frmStaffDetailsView.class.getName()).log(Level.SEVERE, "Error loading staff data", ex);
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
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Staff Information"));
        
        // Add staff info
        panel.add(new JLabel("ID:"));
        panel.add(new JLabel(String.valueOf(staffWithExpenses.getStaff().getId())));
        
        panel.add(new JLabel("Name:"));
        panel.add(new JLabel(staffWithExpenses.getStaff().getName()));
        
        panel.add(new JLabel("Position:"));
        panel.add(new JLabel(staffWithExpenses.getStaff().getPosition()));
        
        panel.add(new JLabel("Username:"));
        panel.add(new JLabel(staffWithExpenses.getStaff().getUserName()));
        
        panel.add(new JLabel("Role:"));
        panel.add(new JLabel(staffWithExpenses.getStaff().getRole()));
        
        panel.add(new JLabel("Total Expenses:"));
        panel.add(new JLabel(String.valueOf(staffWithExpenses.getExpenseCount())));
        
        panel.add(new JLabel("Total Amount:"));
        panel.add(new JLabel(staffWithExpenses.getTotalExpenseAmount().toString()));
        
        return panel;
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
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
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