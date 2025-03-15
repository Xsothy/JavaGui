
import Support.DB;

import java.awt.Image;
import java.io.File;
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

public class frmUExpenseView extends javax.swing.JPanel {
    public frmUExpenseView() {
        initComponents();
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
                // This method is needed for compatibility but you can leave it empty.
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

        tblExpense.getColumnModel().getColumn(3).setPreferredWidth(100);

        tblExpense.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblExpense.getColumnModel().getColumn(0).setPreferredWidth(90);  
        tblExpense.getColumnModel().getColumn(1).setPreferredWidth(478);
        tblExpense.getColumnModel().getColumn(2).setPreferredWidth(350);  
        tblExpense.getColumnModel().getColumn(3).setPreferredWidth(250);
        tblExpense.getColumnModel().getColumn(4).setPreferredWidth(180);  
        tblExpense.getColumnModel().getColumn(5).setPreferredWidth(180); 
        tblExpense.revalidate();
        tblExpense.repaint();
        
        
        fetchData();
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
        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        txtSearch = new javax.swing.JTextField();

        setBackground(new java.awt.Color(253, 253, 253));
        setMinimumSize(new java.awt.Dimension(1280, 1920));

        tblExpense.setAutoCreateRowSorter(true);
        tblExpense.setFont(new java.awt.Font("Kh Dangrek", 0, 14));
        tblExpense.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "ID", "Date", "Description", "Amount","SName","Picture",
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
        jLabel1.setText(bundle.getString("frmUExpenseView.jLabel1.text")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText(bundle.getString("frmUExpenseView.jLabel2.text")); // NOI18N

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearch.setText(bundle.getString("frmUExpenseView.txtSearch.text")); // NOI18N
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
                        .addGap(0, 63, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1081, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(663, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
        searchExpense(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchActionPerformed
    
    public void fetchData() {
        DefaultTableModel model = (DefaultTableModel) tblExpense.getModel();
        model.setRowCount(0); 
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        String imageFolder = "D:/Y3S2/javaII/Testing_Java/src/Expenses/";

        try {
            con = DB.getInstance().getConnection().getData();
            stmt = con.createStatement();
            String query = "SELECT e.ID, e.date, e.desc, e.amount, e.picture, e.SId, s.name AS staff_name " +
                           "FROM expense e " +
                           "JOIN staff s ON e.SId = s.ID " +
                           "ORDER BY e.ID ASC";
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String imageName = rs.getString("picture");
                ImageIcon imageIcon = null;

                if (imageName != null && !imageName.isEmpty()) {
                    String fullImagePath = imageFolder + imageName;
                    File imageFile = new File(fullImagePath);

                    if (imageFile.exists()) { 
                        imageIcon = new ImageIcon(fullImagePath);
                        Image img = imageIcon.getImage();
                        Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                        imageIcon = new ImageIcon(scaledImg);
                    } else {
                        System.out.println("Image file not found: " + fullImagePath);
                    }
                }

                model.addRow(new Object[]{
                    rs.getInt("ID"),
                    rs.getString("date"),
                    rs.getString("desc"),
                    rs.getDouble("amount"),
                    rs.getString("staff_name"),
                    imageIcon,
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error closing resources: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void searchExpense(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tblExpense.getModel();
        model.setRowCount(0);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String imageFolder = "D:/Y3S2/javaII/Testing_Java/src/Expenses/";

        try {
            con = DB.getInstance().getConnection().getData();
            if (con == null) {
                throw new SQLException("Database connection failed.");
            }

            String sql;
            if (keyword.isEmpty()) {
                sql = "SELECT e.ID, e.date, e.desc, e.amount, e.picture, s.name AS staff_name " +
                      "FROM expense e " +
                      "JOIN staff s ON e.SId = s.ID " +
                      "ORDER BY e.ID ASC";
                pstmt = con.prepareStatement(sql);
            } else {
                sql = "SELECT e.ID, e.date, e.desc, e.amount, e.picture, s.name AS staff_name " +
                      "FROM expense e " +
                      "JOIN staff s ON e.SId = s.ID " +
                      "WHERE e.desc LIKE ? OR s.name LIKE ? " +
                      "ORDER BY e.ID ASC";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, "%" + keyword + "%"); 
                pstmt.setString(2, "%" + keyword + "%");
            }

            rs = pstmt.executeQuery();

            while (rs.next()) { // ✅ rs is now properly initialized
                String imageName = rs.getString("picture");
                ImageIcon imageIcon = null;
                if (imageName != null && !imageName.isEmpty()) {
                    String fullImagePath = imageFolder + imageName;
                    File imageFile = new File(fullImagePath);
                    if (imageFile.exists()) { 
                        imageIcon = new ImageIcon(fullImagePath);
                        Image img = imageIcon.getImage();
                        Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                        imageIcon = new ImageIcon(scaledImg);
                    } else {
                        System.out.println("Image file not found: " + fullImagePath);
                    }
                }

                model.addRow(new Object[]{
                    rs.getInt("ID"),
                    rs.getString("date"),
                    rs.getString("desc"),
                    rs.getDouble("amount"),
                    rs.getString("staff_name"),
                    imageIcon,
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error closing resources: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
   }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTable tblExpense;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
