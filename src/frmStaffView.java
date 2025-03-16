import Controller.StaffController;
import Model.Staff;
import Repository.StaffRepository;
import Support.DB;

import java.awt.event.*;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Container;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class frmStaffView extends javax.swing.JPanel {

    JPanel frmUserView = new JPanel();
    private final StaffController staffController;

    public frmStaffView() {
        staffController = new StaffController();
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
                // Plain text components don't fire these events
            }
        });
        
        // Add mouse listener to handle edit and delete actions
        tblStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblStaff.rowAtPoint(e.getPoint());
                int column = tblStaff.columnAtPoint(e.getPoint());
                
                if (row >= 0) {
                    if (column == 4) { // Edit icon clicked
                        editStaff(row);
                    } else if (column == 5) { // Delete icon clicked
                        int staffId = (int) tblStaff.getValueAt(row, 0);
                        int confirm = JOptionPane.showConfirmDialog(null, 
                            "Are you sure you want to delete this staff member?", 
                            "Confirm Delete", 
                            JOptionPane.YES_NO_OPTION);
                        
                        if (confirm == JOptionPane.YES_OPTION) {
                            deleteStaff(staffId);
                        }
                    } else if (column == 6) { // View details icon clicked
                        int staffId = (int) tblStaff.getValueAt(row, 0);
                        viewStaffDetails(staffId);
                    }
                }
            }
        });
        
        // Add custom cell renderers for the edit and delete icon columns
        tblStaff.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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
        
        tblStaff.getColumnModel().getColumn(5).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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
        
        tblStaff.getTableHeader().setPreferredSize(new java.awt.Dimension(tblStaff.getTableHeader().getPreferredSize().width, 40));

        tblStaff.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
                c.setBackground(new java.awt.Color(57, 117, 247));
                c.setForeground(new java.awt.Color(247, 249, 252));
                return c;
            }
        });

        tblStaff.getColumnModel().getColumn(3).setPreferredWidth(100);

        tblStaff.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblStaff.getColumnModel().getColumn(0).setPreferredWidth(100);  
        tblStaff.getColumnModel().getColumn(1).setPreferredWidth(670);
        tblStaff.getColumnModel().getColumn(2).setPreferredWidth(200);  
        tblStaff.getColumnModel().getColumn(3).setPreferredWidth(200);
        tblStaff.getColumnModel().getColumn(4).setPreferredWidth(80);  
        tblStaff.getColumnModel().getColumn(5).setPreferredWidth(80);
        tblStaff.getColumnModel().getColumn(6).setPreferredWidth(80);

        tblStaff.revalidate();
        tblStaff.repaint();

        // Load initial data
        try {
            List<Staff> staffList = staffController.getAllStaff();
            renderStaffTable(staffList);
        } catch (SQLException ex) {
            Logger.getLogger(frmStaffView.class.getName()).log(Level.SEVERE, "Error loading staff data", ex);
            JOptionPane.showMessageDialog(this, "Error loading staff data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
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
        tblStaff = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));

        btnAdd.setIcon(new javax.swing.ImageIcon(Paths.get("src/img/add.png").toAbsolutePath().toString())); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("Bundle"); // NOI18N
        btnAdd.setText(bundle.getString("frmStaffView.btnAdd.text")); // NOI18N
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText(bundle.getString("frmStaffView.jLabel1.text")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText(bundle.getString("frmStaffView.jLabel2.text")); // NOI18N

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearch.setText(bundle.getString("frmStaffView.txtSearch.text")); // NOI18N
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        tblStaff.setAutoCreateRowSorter(true);
        tblStaff.setFont(new java.awt.Font("Kh Dangrek", 0, 14));
        tblStaff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "ID", "Name", "Position","Username","","","",
            }
        ));
        tblStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblStaff.setDoubleBuffered(true);
        tblStaff.setDragEnabled(true);
        tblStaff.setFillsViewportHeight(true);
        tblStaff.setFocusCycleRoot(true);
        tblStaff.setFocusTraversalPolicyProvider(true);
        tblStaff.setInheritsPopupMenu(true);
        tblStaff.setRowHeight(50);
        tblStaff.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblStaff.setShowGrid(false);
        jScrollPane2.setViewportView                // This method is needed for compatibility but you can leave it empty.
                (tblStaff);
        tblStaff.getAccessibleContext().setAccessibleName(bundle.getString("frmStaffView.tblStaff.AccessibleContext.accessibleName")); // NOI18N

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

    public boolean isEditing = false;

    private void renderStaffTable(List<Staff> staffs) {
        DefaultTableModel model = (DefaultTableModel) tblStaff.getModel();
        model.setRowCount(0);
        staffs.forEach(staff -> {
            model.addRow(new Object[]{
                    staff.getId(),
                    staff.getName(),
                    staff.getPosition(),
                    staff.getUserName(),
                    frmExpenseView.getImageIcon("src/img/edit.png"),
                    frmExpenseView.getImageIcon("src/img/delete.png"),
                    frmExpenseView.getImageIcon("src/img/view.png")
            });
        });
        tblStaff.revalidate();
        tblStaff.repaint();
    }

    public void deleteStaff(int staffId) {
        try {
            boolean deleted = staffController.deleteStaff(staffId);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Staff deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Refresh the table
                List<Staff> staffList = staffController.getAllStaff();
                renderStaffTable(staffList);
            } else {
                JOptionPane.showMessageDialog(this, "Error: Staff could not be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(frmStaffView.class.getName()).log(Level.SEVERE, "Database error", ex);
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Opens the staff edit form with the selected staff's data.
     * 
     * @param row The row index in the table
     */
    public void editStaff(int row) {
        try {
            int staffId = (int) tblStaff.getValueAt(row, 0);
            String name = (String) tblStaff.getValueAt(row, 1);
            String position = (String) tblStaff.getValueAt(row, 2);
            String userName = (String) tblStaff.getValueAt(row, 3);
            
            // Get the full staff object to get all data
            Staff staff = staffController.getStaffById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
            
            // Create and show the edit form
            frmStaffAdd editForm = new frmStaffAdd();
            // The frmStaffAdd expects: id, name, tel, position, role
            // Since Staff doesn't have a tel property, we'll pass an empty string
            editForm.setStaffData(staffId, name, "", position, staff.getRole());
            editForm.setVisible(true);
            
            // Add a listener to refresh the table when the form is closed
            editForm.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    try {
                        List<Staff> staffList = staffController.getAllStaff();
                        renderStaffTable(staffList);
                    } catch (SQLException ex) {
                        Logger.getLogger(frmStaffView.class.getName()).log(Level.SEVERE, "Error refreshing staff data", ex);
                        JOptionPane.showMessageDialog(null, "Error refreshing staff data: " + ex.getMessage(), 
                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error editing staff: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(frmStaffView.class.getName()).log(Level.SEVERE, "Database error", ex);
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {                                     
        frmStaffAdd addForm = new frmStaffAdd();
        addForm.setVisible(true);
        addForm.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    try {
                        List<Staff> staffList = staffController.getAllStaff();
                        renderStaffTable(staffList);
                    } catch (SQLException ex) {
                        Logger.getLogger(frmStaffView.class.getName()).log(Level.SEVERE, "Error refreshing staff data", ex);
                        JOptionPane.showMessageDialog(null, "Error refreshing staff data: " + ex.getMessage(), 
                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
    }
    public void searchUser(String keyword) {
        try {
            List<Staff> staffList = staffController.getAllStaff();
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchTerm = keyword.toLowerCase().trim();
                
                // Filter the staff list based on the search term
                staffList = staffList.stream()
                    .filter(staff -> 
                        staff.getName().toLowerCase().contains(searchTerm) ||
                        staff.getPosition().toLowerCase().contains(searchTerm) ||
                        staff.getUserName().toLowerCase().contains(searchTerm) ||
                        staff.getRole().toLowerCase().contains(searchTerm))
                    .toList();
            }
            
            renderStaffTable(staffList);
        } catch (SQLException ex) {
            Logger.getLogger(frmStaffView.class.getName()).log(Level.SEVERE, "Database error", ex);
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens the staff details view for the specified staff ID.
     * 
     * @param staffId The ID of the staff member to view
     */
    private void viewStaffDetails(int staffId) {
        try {
            // Check if the staff exists
            staffController.getStaffById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
            
            // Create and show the staff details view
            frmStaffDetailsView detailsView = new frmStaffDetailsView(staffId);
            
            // Get the parent container
            Container parent = getParent();
            
            // If the parent is using the Router, register and navigate to the details view
            if (parent instanceof JPanel) {
                Support.Router router = Support.Router.getInstance((JPanel) parent);
                String detailsPath = "/staff/" + staffId;
                
                if (!router.hasRoute(detailsPath)) {
                    router.register(detailsPath, detailsView);
                }
                
                router.navigate(detailsPath);
            }
            // Otherwise, just add the details view to the parent
            else {
                parent.removeAll();
                parent.add(detailsView);
                parent.revalidate();
                parent.repaint();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error viewing staff details: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(frmStaffView.class.getName()).log(Level.SEVERE, "Database error", ex);
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnAdd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTable tblStaff;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
