import Controller.ExpenseController;
import Model.Expense;
import Model.Staff;
import Support.DB;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

public class frmExpenseView extends javax.swing.JPanel {
    private final ExpenseController expenseController;
    private frmExpenseAdd expenseForm = null;

    public frmExpenseView() {
        expenseController = new ExpenseController();
        initComponents();
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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

            }
        });
        
        tblExpense.getTableHeader().setPreferredSize(new java.awt.Dimension(tblExpense.getTableHeader().getPreferredSize().width, 40));

        tblExpense.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
                c.setBackground(new java.awt.Color(57, 117, 247));
                c.setForeground(new java.awt.Color(247, 249, 252));
                return c;
            }
        });
        tblExpense.getColumnModel().getColumn(5).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    JLabel label = new JLabel((ImageIcon) value);
                    label.setHorizontalAlignment(JLabel.CENTER); 
                    return label;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        tblExpense.getColumnModel().getColumn(6).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    JLabel label = new JLabel((ImageIcon) value);
                    label.setHorizontalAlignment(JLabel.CENTER); 
                    return label;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        tblExpense.getColumnModel().getColumn(7).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    JLabel label = new JLabel((ImageIcon) value);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    return label;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        tblExpense.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblExpense.rowAtPoint(e.getPoint());
                int column = tblExpense.columnAtPoint(e.getPoint());
                if (column == 6) { // Edit icon clicked
                    editExpense(row);
                } else if (column == 7) { // Delete icon clicked
                    deleteExpense(row);
                }
            }
        });

        tblExpense.getColumnModel().getColumn(3).setPreferredWidth(100);

        tblExpense.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblExpense.getColumnModel().getColumn(0).setPreferredWidth(90);  
        tblExpense.getColumnModel().getColumn(1).setPreferredWidth(478);
        tblExpense.getColumnModel().getColumn(2).setPreferredWidth(350);  
        tblExpense.getColumnModel().getColumn(3).setPreferredWidth(250);
        tblExpense.getColumnModel().getColumn(4).setPreferredWidth(100);  
        tblExpense.getColumnModel().getColumn(5).setPreferredWidth(100); 
        tblExpense.getColumnModel().getColumn(6).setPreferredWidth(80);  
        tblExpense.getColumnModel().getColumn(7).setPreferredWidth(80);
        tblExpense.getColumnModel().getColumn(8).setPreferredWidth(1);
        tblExpense.revalidate();
        tblExpense.repaint();

        renderExpenseTable();
    }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tblExpense = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jProgressBar1 = new javax.swing.JProgressBar();

        setBackground(new java.awt.Color(253, 253, 253));
        setMinimumSize(new java.awt.Dimension(1280, 1920));

        tblExpense.setAutoCreateRowSorter(true);
        tblExpense.setFont(new java.awt.Font("Kh Dangrek", 0, 14));
        tblExpense.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "ID", "Date", "Description", "Amount","SName","Picture","","",""
            }
        ));
        tblExpense.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblExpense.setDoubleBuffered(true);
        tblExpense.setDragEnabled(true);
        tblExpense.setFillsViewportHeight(true);
        tblExpense.setFocusCycleRoot(true);
        tblExpense.setFocusTraversalPolicyProvider(true);
        tblExpense.setInheritsPopupMenu(true);
        tblExpense.setRowHeight(50);
        tblExpense.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblExpense.setShowGrid(false);
        tblExpense.setSurrendersFocusOnKeystroke(true);
        jScrollPane2.setViewportView(tblExpense);
        tblExpense.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("frmExpenseView.jLabel1.text")); // NOI18N

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        btnAdd.setText(bundle.getString("frmExpenseView.btnAdd.text")); // NOI18N
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText(bundle.getString("frmExpenseView.jLabel2.text")); // NOI18N

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearch.setText(bundle.getString("frmExpenseView.txtSearch.text")); // NOI18N
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1535, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(1108, 1108, 1108))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(67, 67, 67)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1081, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(703, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
        searchExpense(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchActionPerformed

    public void renderExpenseTable() {
        DefaultTableModel model = (DefaultTableModel) tblExpense.getModel();
        model.setRowCount(0);

        List<Expense> expenses;
        try {
            expenses = expenseController.getAllExpenses();
            
            expenses.forEach(expense -> {
                model.addRow(new Object[]{
                        expense.getId(),
                        expense.getName(),
                        expense.getDescription(),
                        expense.getAmount(),
                        expense.getStaffId(),
                        getImageIcon(expense.getPicture()),
                        getImageIcon("src/img/edit.png"),
                        getImageIcon("src/img/delete.png")
                });
            });
            
            tblExpense.revalidate();
            tblExpense.repaint();
        } catch (SQLException e) {
            showError("Failed to get expenses: " + e.getMessage());
        }
    }

    public static ImageIcon getImageIcon(String imageName) {
         if (imageName != null && !imageName.isEmpty()) {
             Path path = Paths.get(imageName);
             File imageFile = new File(path.toAbsolutePath().toString());
             if (imageFile.exists()) {
                 ImageIcon icon = new ImageIcon(path.toAbsolutePath().toString());
                 Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                 return new ImageIcon(img);
             }
         }
         return null;
     }

      private void editExpense(int row) {
        try {
            if (expenseForm != null && expenseForm.isVisible()) {
                return;
            }
            int id = (int) tblExpense.getValueAt(row, 0);
            
            // Get the expense from the controller
            Optional<Expense> expenseOpt = expenseController.getExpenseById(id);
            if (expenseOpt.isEmpty()) {
                showError("Expense not found");
                return;
            }
            
            Expense expense = expenseOpt.get();
            String name = expense.getName();
            String description = expense.getDescription();
            BigDecimal amount = expense.getAmount();
            int staffId = expense.getStaffId();
            String picture = expense.getPicture();
            
            expenseForm = new frmExpenseAdd(this, id, name, description, amount.doubleValue(), String.valueOf(staffId), picture);
            btnAdd.setEnabled(false);

            expenseForm.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    btnAdd.setEnabled(true);
                    renderExpenseTable();
                    expenseForm = null;
                }
            });
            
            expenseForm.setVisible(true);
        } catch (IllegalArgumentException e) {
            showError("Error editing expense: " + e.getMessage());
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
  
    private void deleteExpense(int row) {
        try {
            int id = (int) tblExpense.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this expense?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = expenseController.deleteExpense(id);
                if (deleted) {
                    JOptionPane.showMessageDialog(null, "Expense deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    renderExpenseTable();
                }
            }
        } catch (IllegalArgumentException e) {
            showError("Error deleting expense: " + e.getMessage());
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }
    public void searchExpense(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            renderExpenseTable(); // If search is empty, show all expenses
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) tblExpense.getModel();
        model.setRowCount(0);

        try {
            List<Expense> expenses = expenseController.getAllExpenses();
            
            // Filter expenses based on keyword (case-insensitive)
            expenses.stream()
                .filter(expense -> 
                    expense.getName().toLowerCase().contains(keyword.toLowerCase()) || 
                    expense.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .forEach(expense -> {
                    model.addRow(new Object[]{
                        expense.getId(),
                        expense.getName(),
                        expense.getDescription(),
                        expense.getAmount(),
                        expense.getStaffId(),
                        getImageIcon(expense.getPicture()),
                        getImageIcon("src/img/edit.png"),
                        getImageIcon("src/img/delete.png")
                    });
                });
            
            tblExpense.revalidate();
            tblExpense.repaint();
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }


    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {
        btnAdd.setEnabled(false);
        frmExpenseAdd newForm = new frmExpenseAdd(); 
        newForm.setVisible(true); 

        newForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                btnAdd.setEnabled(true);
                renderExpenseTable(); // Refresh the table when the form is closed
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnAdd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTable tblExpense;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
