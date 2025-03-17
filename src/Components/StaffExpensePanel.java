package Components;

import Controller.StaffController;
import Model.Staff;
import Repository.StaffRepository;
import Repository.ExpenseRepository;
import Model.Expense;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A panel for displaying a staff member with their expenses.
 */
public class StaffExpensePanel extends NavigatePanel {
    private final StaffController staffController;
    private final StaffRepository.StaffWithExpenses staffWithExpenses;
    private final JTable expenseTable;
    
    /**
     * Create a new StaffExpensePanel.
     * 
     * @param staffWithExpenses The staff member with expenses to display
     */
    public StaffExpensePanel(StaffRepository.StaffWithExpenses staffWithExpenses) {
        this.staffController = new StaffController();
        this.staffWithExpenses = staffWithExpenses;
        
        // Set up the panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create the staff info panel
        JPanel staffInfoPanel = createStaffInfoPanel();
        add(staffInfoPanel, BorderLayout.NORTH);
        
        // Create the expense table
        expenseTable = createExpenseTable();
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Create the staff info panel.
     * 
     * @return The staff info panel
     */
    private JPanel createStaffInfoPanel() {
        Staff staff = staffWithExpenses.getStaff();
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Staff Information"));
        
        // Add staff info
        panel.add(new JLabel("ID:"));
        panel.add(new JLabel(String.valueOf(staff.getId())));
        
        panel.add(new JLabel("Name:"));
        panel.add(new JLabel(staff.getName()));
        
        panel.add(new JLabel("Position:"));
        panel.add(new JLabel(staff.getPosition()));
        
        panel.add(new JLabel("Username:"));
        panel.add(new JLabel(staff.getUserName()));
        
        panel.add(new JLabel("Role:"));
        panel.add(new JLabel(staff.getRole()));
        
        panel.add(new JLabel("Total Expenses:"));
        panel.add(new JLabel(String.valueOf(staffWithExpenses.getExpenseCount())));
        
        panel.add(new JLabel("Total Amount:"));
        panel.add(new JLabel(staffWithExpenses.getTotalExpenseAmount().toString()));
        
        return panel;
    }
    
    /**
     * Create the expense table.
     * 
     * @return The expense table
     */
    private JTable createExpenseTable() {
        // Create the table model
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Name", "Description", "Amount"}, 0);
        
        // Add the expenses to the table
        staffWithExpenses.getExpenses().forEach(expense -> {
            model.addRow(new Object[]{
                expense.getExpense().getId(),
                expense.getExpense().getName(),
                expense.getExpense().getDescription(),
                expense.getExpense().getAmount()
            });
        });
        
        // Create the table
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        return table;
    }
    
    /**
     * Refresh the panel with updated data.
     * 
     * @param staffId The ID of the staff member to refresh
     */
    public void refresh(int staffId) {
        try {
            StaffRepository.StaffWithExpenses updatedStaffWithExpenses = 
                    staffController.getStaffWithExpensesById(staffId)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
            
            // Update the staff info panel
            removeAll();
            add(createStaffInfoPanel(), BorderLayout.NORTH);
            
            // Update the expense table
            DefaultTableModel model = (DefaultTableModel) expenseTable.getModel();
            model.setRowCount(0);
            
            updatedStaffWithExpenses.getExpenses().forEach(expense -> {
                model.addRow(new Object[]{
                    expense.getExpense().getId(),
                    expense.getExpense().getName(),
                    expense.getExpense().getDescription(),
                    expense.getExpense().getAmount()
                });
            });
            
            JScrollPane scrollPane = new JScrollPane(expenseTable);
            add(scrollPane, BorderLayout.CENTER);
            
            revalidate();
            repaint();
        } catch (SQLException | IllegalArgumentException ex) {
            Logger.getLogger(StaffExpensePanel.class.getName()).log(Level.SEVERE, "Error refreshing staff data", ex);
            JOptionPane.showMessageDialog(this, "Error refreshing staff data: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
} 