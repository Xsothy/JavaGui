package Components;

import Controller.ExpenseController;
import Model.Expense;
import Repository.ExpenseRepository;
import Support.Router;

import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for viewing and managing expenses.
 */
public class ExpensePanel extends JPanel {
    private final ExpenseController expenseController;
    private final Router router;
    private JFrame expenseForm = null;
    
    // Components
    private JLabel btnAdd;
    private JTextField txtSearch;
    private JTable tblExpense;
    
    /**
     * Creates a new ExpensePanel.
     * 
     * @param containerPanel The container panel for routing
     */
    public ExpensePanel(JPanel containerPanel) {
        this.expenseController = new ExpenseController();
        this.router = Router.getInstance(containerPanel);
        
        initComponents();
        setupListeners();
        setupTableAppearance();
        loadExpenseData();
    }
    
    /**
     * Initialize the components.
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 255, 255));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(new Color(255, 255, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Title and add button panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(255, 255, 255));
        
        JLabel titleLabel = new JLabel("Expense Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        
        btnAdd = new JLabel(getImageIcon("src/img/add.png"));
        btnAdd.setText("Add New Expense");
        btnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        titlePanel.add(btnAdd);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(255, 255, 255));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchPanel.add(searchLabel);
        
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchPanel.add(txtSearch);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        // Table
        tblExpense = new JTable();
        tblExpense.setModel(new DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Name", "Description", "Amount", "Staff", "Picture", "", ""}
        ));
        tblExpense.setRowHeight(50);
        tblExpense.setShowGrid(false);
        tblExpense.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tblExpense.setFillsViewportHeight(true);
        
        JScrollPane scrollPane = new JScrollPane(tblExpense);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Setup component listeners.
     */
    private void setupListeners() {
        btnAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
        });
        
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchExpense(txtSearch.getText()); 
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchExpense(txtSearch.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components don't fire these events
            }
        });
        
        // Add mouse listener to handle edit and delete actions
        tblExpense.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblExpense.rowAtPoint(e.getPoint());
                int column = tblExpense.columnAtPoint(e.getPoint());
                
                if (row >= 0) {
                    if (column == 6) { // Edit icon clicked
                        editExpense(row);
                    } else if (column == 7) { // Delete icon clicked
                        int expenseId = (int) tblExpense.getValueAt(row, 0);
                        int confirm = JOptionPane.showConfirmDialog(null, 
                            "Are you sure you want to delete this expense?", 
                            "Confirm Delete", 
                            JOptionPane.YES_NO_OPTION);
                        
                        if (confirm == JOptionPane.YES_OPTION) {
                            deleteExpense(expenseId);
                        }
                    }
                }
            }
        });
    }
    
    /**
     * Setup table appearance.
     */
    private void setupTableAppearance() {
        // Add custom cell renderers for the icon and picture columns
        int[] iconColumns = {5, 6, 7}; // Picture, Edit, Delete columns
        for (int i : iconColumns) {
            tblExpense.getColumnModel().getColumn(i).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    if (value instanceof ImageIcon) {
                        JLabel label = new JLabel((ImageIcon) value);
                        label.setHorizontalAlignment(JLabel.CENTER); 
                        return label;
                    }
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            });
        }
        
        // Setup table header appearance
        tblExpense.getTableHeader().setPreferredSize(new Dimension(tblExpense.getTableHeader().getPreferredSize().width, 40));
        
        tblExpense.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Arial", Font.BOLD, 16));
                c.setBackground(new Color(57, 117, 247));
                c.setForeground(new Color(247, 249, 252));
                return c;
            }
        });

        // Set column widths
        tblExpense.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblExpense.getColumnModel().getColumn(0).setPreferredWidth(80);  
        tblExpense.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblExpense.getColumnModel().getColumn(2).setPreferredWidth(300);  
        tblExpense.getColumnModel().getColumn(3).setPreferredWidth(150);
        tblExpense.getColumnModel().getColumn(4).setPreferredWidth(200);  
        tblExpense.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblExpense.getColumnModel().getColumn(6).setPreferredWidth(80);
        tblExpense.getColumnModel().getColumn(7).setPreferredWidth(80);
    }
    
    /**
     * Load initial expense data.
     */
    private void loadExpenseData() {
        try {
            List<ExpenseRepository.ExpenseWithStaff> expenses = expenseController.getAllExpensesWithStaff();
            renderExpenseTable(expenses);
        } catch (SQLException ex) {
            Logger.getLogger(ExpensePanel.class.getName()).log(Level.SEVERE, "Error loading expense data", ex);
            JOptionPane.showMessageDialog(this, "Error loading expense data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Render the expense table with the given list of expenses.
     * 
     * @param expenses The list of expenses to display
     */
    private void renderExpenseTable(List<ExpenseRepository.ExpenseWithStaff> expenses) {
        DefaultTableModel model = (DefaultTableModel) tblExpense.getModel();
        model.setRowCount(0);
        
        expenses.forEach(expenseWithStaff -> {
            Expense expense = expenseWithStaff.getExpense();
            model.addRow(new Object[]{
                expense.getId(),
                expense.getName(),
                expense.getDescription(),
                expense.getAmount(),
                expenseWithStaff.getStaffName(), // Display staff name instead of ID
                getImageIcon(expense.getPicture()),
                getImageIcon("src/img/edit.png"),
                getImageIcon("src/img/delete.png")
            });
        });
        
        tblExpense.revalidate();
        tblExpense.repaint();
    }
    
    /**
     * Get an image icon from the specified path.
     * 
     * @param imagePath The path to the image
     * @return The image icon
     */
    public static ImageIcon getImageIcon(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Path path = Paths.get(imagePath);
                ImageIcon icon = new ImageIcon(path.toAbsolutePath().toString());
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            } catch (Exception e) {
                Logger.getLogger(ExpensePanel.class.getName()).log(Level.WARNING, "Error loading image: " + imagePath, e);
            }
        }
        return null;
    }
    
    /**
     * Search for expenses.
     * 
     * @param keyword The search keyword
     */
    private void searchExpense(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                loadExpenseData(); // If search is empty, show all expenses
                return;
            }
            
            List<Expense> expenses = expenseController.getAllExpenses();
            String searchTerm = keyword.toLowerCase().trim();
            
            // Filter expenses based on keyword (case-insensitive)
            DefaultTableModel model = (DefaultTableModel) tblExpense.getModel();
            model.setRowCount(0);
            
            expenses.stream()
                .filter(expense -> 
                    expense.getName().toLowerCase().contains(searchTerm) || 
                    expense.getDescription().toLowerCase().contains(searchTerm))
                .forEach(expense -> {
                    try {
                        // Get the staff name for this expense
                        Optional<ExpenseRepository.ExpenseWithStaff> expenseWithStaff = 
                                expenseController.getExpenseWithStaffById(expense.getId());
                        
                        model.addRow(new Object[]{
                            expense.getId(),
                            expense.getName(),
                            expense.getDescription(),
                            expense.getAmount(),
                            expenseWithStaff.isPresent() ? expenseWithStaff.get().getStaffName() : expense.getStaffId(),
                            getImageIcon(expense.getPicture()),
                            getImageIcon("src/img/edit.png"),
                            getImageIcon("src/img/delete.png")
                        });
                    } catch (SQLException ex) {
                        Logger.getLogger(ExpensePanel.class.getName()).log(Level.SEVERE, "Error getting staff name", ex);
                    }
                });
            
            tblExpense.revalidate();
            tblExpense.repaint();
        } catch (SQLException ex) {
            Logger.getLogger(ExpensePanel.class.getName()).log(Level.SEVERE, "Database error", ex);
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Edit an expense.
     * 
     * @param row The row in the table
     */
    private void editExpense(int row) {
        try {
            if (expenseForm != null && expenseForm.isVisible()) {
                return;
            }
            
            int id = (int) tblExpense.getValueAt(row, 0);
            
            // Get the expense from the controller
            Optional<Expense> expenseOpt = expenseController.getExpenseById(id);
            if (expenseOpt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Expense not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Expense expense = expenseOpt.get();
            
            // Create and show the expense form
            expenseForm = new JFrame("Edit Expense");
            ExpenseFormPanel editPanel = new ExpenseFormPanel(id, expense, this);
            expenseForm.getContentPane().add(editPanel);
            expenseForm.pack();
            expenseForm.setLocationRelativeTo(this);
            expenseForm.setVisible(true);
            
            // Add window listener to refresh data when closed
            expenseForm.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadExpenseData();
                    expenseForm = null;
                }
            });
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error editing expense: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(ExpensePanel.class.getName()).log(Level.SEVERE, "Database error", ex);
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Delete an expense.
     * 
     * @param expenseId The ID of the expense to delete
     */
    private void deleteExpense(int expenseId) {
        try {
            boolean deleted = expenseController.deleteExpense(expenseId);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Expense deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadExpenseData();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting expense: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(ExpensePanel.class.getName()).log(Level.SEVERE, "Database error", ex);
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Handle the add button click event.
     */
    private void btnAddMouseClicked(MouseEvent evt) {
        expenseForm = new JFrame("Add New Expense");
        ExpenseFormPanel addPanel = new ExpenseFormPanel(this);
        expenseForm.getContentPane().add(addPanel);
        expenseForm.pack();
        expenseForm.setLocationRelativeTo(this);
        expenseForm.setVisible(true);
        
        // Add window listener to refresh data when closed
        expenseForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loadExpenseData();
                expenseForm = null;
            }
        });
    }
} 