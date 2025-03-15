
import Support.DB;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class frmUserView extends javax.swing.JPanel {

    JPanel frmUserView = new JPanel();

    public frmUserView() {
        initComponents();
         btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));  
        
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
        });
        
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchUser(txtSearch.getText()); 
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchUser(txtSearch.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // This method is needed for compatibility but you can leave it empty.
            }
        });
        
        
        tblUser.getTableHeader().setPreferredSize(new java.awt.Dimension(tblUser.getTableHeader().getPreferredSize().width, 40)); 

        tblUser.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
                c.setBackground(new java.awt.Color(57, 117, 247));
                c.setForeground(new java.awt.Color(247, 249, 252));
                return c;
            }
        });
        tblUser.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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
        
        tblUser.getColumnModel().getColumn(5).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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

        tblUser.getColumnModel().getColumn(3).setPreferredWidth(100);

        tblUser.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblUser.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblUser.getColumnModel().getColumn(1).setPreferredWidth(670);
        tblUser.getColumnModel().getColumn(2).setPreferredWidth(300);
        tblUser.getColumnModel().getColumn(3).setPreferredWidth(300);
        tblUser.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblUser.getColumnModel().getColumn(5).setPreferredWidth(80);

        tblUser.revalidate();
        tblUser.repaint();
        
        fetchData();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAdd = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblUser = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("Bundle"); // NOI18N
        btnAdd.setText(bundle.getString("frmUserView.btnAdd.text")); // NOI18N
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText(bundle.getString("frmUserView.jLabel1.text")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText(bundle.getString("frmUserView.jLabel2.text")); // NOI18N

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearch.setText(bundle.getString("frmUserView.txtSearch.text")); // NOI18N
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        tblUser.setAutoCreateRowSorter(true);
        tblUser.setFont(new java.awt.Font("Kh Dangrek", 0, 14));
        tblUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "ID", "Name", "Email", "Role","","",
            }
        ));
        tblUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblUser.setDragEnabled(true);
        tblUser.setFillsViewportHeight(true);
        tblUser.setRowHeight(50);
        tblUser.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblUser.setShowGrid(false);
        jScrollPane2.setViewportView(tblUser);
        tblUser.getAccessibleContext().setAccessibleName(bundle.getString("frmUserView.tblUser.AccessibleContext.accessibleName")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1282, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAdd)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addGap(39, 39, 39))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSearch)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
        searchUser(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchActionPerformed

     private boolean isEditing = false;  

    public void fetchData() {
        DefaultTableModel model = (DefaultTableModel) tblUser.getModel();
        model.setRowCount(0);

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = DB.getInstance().getConnection().getData();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user ORDER BY ID ASC");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("ID"),
                    rs.getString("Username"),
                    rs.getString("Email"),
                    rs.getString("Role"),
                    new ImageIcon("D:\\Y3S2\\javaII\\TestMainfrm_1\\src\\img\\edit.png"),
                    new ImageIcon("D:\\Y3S2\\javaII\\TestMainfrm_1\\src\\img\\delete.png")
                });
            }

            tblUser.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    
                    if (isEditing) {
                        return;
                    }

                    int row = tblUser.rowAtPoint(e.getPoint());
                    int column = tblUser.columnAtPoint(e.getPoint());

                    if (column == 4) { 
                        isEditing = true; 

                        int id = (int) tblUser.getValueAt(row, 0);
                        String username = (String) tblUser.getValueAt(row, 1);
                        String email = (String) tblUser.getValueAt(row, 2);
                        String role = (String) tblUser.getValueAt(row, 3);

                        SignUp signUpForm = new SignUp();
                        signUpForm.setUserData(id, username, email, role);
                        btnAdd.setEnabled(false);
                        
                        signUpForm.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                isEditing = false;
                                btnAdd.setEnabled(true);
                            }
                        });

                        signUpForm.setVisible(true);
                    } else if (column == 5) {  
                        int id = (int) tblUser.getValueAt(row, 0);

                        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            deleteUser(id);
                        }
                    }
                }
            });

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error closing resources: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void deleteUser(int userId) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DB.getInstance().getConnection().getData();
            if (con == null) {
                throw new SQLException("Database connection failed.");
            }

            String sql = "DELETE FROM user WHERE ID = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, userId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                fetchData();
            } else {
                JOptionPane.showMessageDialog(null, "Error: User could not be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error closing resources: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {                                     
            btnAdd.setEnabled(false);

            SignUp newForm = new SignUp();
            newForm.setVisible(true);
            newForm.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    btnAdd.setEnabled(true);            
                }
            });
    }
    public void searchUser(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tblUser.getModel();
        model.setRowCount(0);

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DB.getInstance().getConnection().getData();
            if (con == null) {
                throw new SQLException("Database connection failed.");
            }

            String sql = "SELECT * FROM user WHERE LOWER(username) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?) ORDER BY ID ASC";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("ID"),
                    rs.getString("Username"),
                    rs.getString("Email"),
                    rs.getString("Role"),
                    new ImageIcon("D:\\Y3S2\\javaII\\TestMainfrm_1\\src\\img\\edit.png"),  
                    new ImageIcon("D:\\Y3S2\\javaII\\TestMainfrm_1\\src\\img\\delete.png")
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
    private javax.swing.JLabel btnAdd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTable tblUser;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
