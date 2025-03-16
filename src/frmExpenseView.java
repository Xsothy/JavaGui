import Controller.ExpenseController;
import Model.Expense;
import Model.Staff;
import Support.DB;
import Support.Response;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class frmExpenseView extends javax.swing.JPanel {
    private final ExpenseController expenseController;
    private frmExpenseAdd expenseForm = null;
    private static final String IMAGE_FOLDER = "D:/Y3S2/javaII/Testing_Java/src/Expenses/";

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

        Response<List<Expense>> res = expenseController.getAllExpenses();
        if (!res.isSuccess()) {
            JOptionPane.showMessageDialog(this, "Failed to get expense: " + res.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        res.getData().forEach(expense -> {
            model.addRow(new Object[]{
                    expense.getId(),
                    expense.getName(),
                    expense.getAmount(),
                    expense.getStaffId(),
                    getImageIcon("src/img/edit.png"),
                    getImageIcon("src/img/delete.png")
            });
        });
        tblExpense.revalidate();
        tblExpense.repaint();
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
         if (expenseForm != null && expenseForm.isVisible()) {
             return;
         }
         int id = (int) tblExpense.getValueAt(row, 0);
         String date = (String) tblExpense.getValueAt(row, 1);
         String description = (String) tblExpense.getValueAt(row, 2);
         double amount = (double) tblExpense.getValueAt(row, 3);
         String staffName = (String) tblExpense.getValueAt(row, 4);
         String imageName = (String) tblExpense.getValueAt(row, 8);
         String fullImagePath = (imageName != null) ? IMAGE_FOLDER + imageName : null;

         expenseForm = new frmExpenseAdd(this, id, date, description, amount, staffName, fullImagePath);
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
     }
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
  
    private void deleteExpense(int row) {
            int id = (int) tblExpense.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this expense?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                expenseController.deleteExpense(id);
            }
            renderExpenseTable();
        }
    public void searchExpense(String keyword) {
//        DefaultTableModel model = (DefaultTableModel) tblExpense.getModel();
//        model.setRowCount(0);
//
//        String imageFolder = "D:/Y3S2/javaII/Testing_Java/src/Expenses/";
//        Connection con = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            con = DB.getInstance().getConnection().getData();
//            if (con == null) {
//                throw new SQLException("Database connection failed.");
//            }
//
//            String  sql = "SELECT e.ID, e.date, e.desc, e.amount, e.picture, s.name AS staff_name " +
//                      "FROM expenses e " +
//                      "JOIN staff s ON e.SId = s.ID " +
//                      "WHERE e.desc LIKE ? OR s.name LIKE ? " +
//                      "ORDER BY e.ID ASC";
//                pstmt = con.prepareStatement(sql);
//                pstmt.setString(1, "%" + keyword + "%");
//                pstmt.setString(2, "%" + keyword + "%");
//
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                String imageName = rs.getString("picture");
//                ImageIcon imageIcon = null;
//
//                if (imageName != null && !imageName.isEmpty()) {
//                    String fullImagePath = imageFolder + imageName;
//                    File imageFile = new File(fullImagePath);
//                    if (imageFile.exists()) {
//                        imageIcon = new ImageIcon(fullImagePath);
//                        Image img = imageIcon.getImage();
//                        Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
//                        imageIcon = new ImageIcon(scaledImg);
//                    } else {
//                        System.out.println("Image file not found: " + fullImagePath);
//                    }
//                }
//
//                model.addRow(new Object[]{
//                    rs.getInt("ID"),
//                    rs.getString("date"),
//                    rs.getString("desc"),
//                    rs.getDouble("amount"),
//                    rs.getString("staff_name"),
//                    imageIcon,
//                    new ImageIcon("D:\\Y3S2\\javaII\\Testing_Java\\src\\img\\edit.png"),
//                    new ImageIcon("D:\\Y3S2\\javaII\\Testing_Java\\src\\img\\delete.png"),
//                    imageName
//                });
//            }
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (pstmt != null) pstmt.close();
//                if (con != null) con.close();
//            } catch (SQLException e) {
//                JOptionPane.showMessageDialog(null, "Error closing resources: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
    }


    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {
        btnAdd.setEnabled(false);
        frmExpenseAdd newForm = new frmExpenseAdd(); 
        newForm.setVisible(true); 

        newForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                btnAdd.setEnabled(true);            
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
