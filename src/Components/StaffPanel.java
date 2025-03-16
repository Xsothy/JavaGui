package Components;

import Controller.StaffController;
import Model.Staff;
import Support.Router;

import java.awt.*;
import java.awt.event.*;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Panel for viewing and managing staff members.
 */
public class StaffPanel extends javax.swing.JPanel {
    private final StaffController staffController;
    private final Router router;

    /**
     * Creates a new StaffPanel.
     * 
     * @param containerPanel The container panel for routing
     */
    public StaffPanel(JPanel containerPanel) {
        staffController = new StaffController();
        router = Router.getInstance(containerPanel);
        
        initComponents();
        setupListeners();
        setupTableAppearance();
        loadStaffData();
    }

    /**
     * Setup component listeners.
     */
    private void setupListeners() {
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
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
        
        // Add mouse listener to handle edit, delete, and view actions
        tblStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblStaff.rowAtPoint(e.getPoint());
                int column = tblStaff.columnAtPoint(e.getPoint());
                
                if (row >= 0) {
                    int staffId = (int) tblStaff.getValueAt(row, 0);
                    
                    if (column == 4) { // Edit icon clicked
                        editStaff(row);
                    } else if (column == 5) { // Delete icon clicked
                        int confirm = JOptionPane.showConfirmDialog(null, 
                            "Are you sure you want to delete this staff member?", 
                            "Confirm Delete", 
                            JOptionPane.YES_NO_OPTION);
                        
                        if (confirm == JOptionPane.YES_OPTION) {
                            deleteStaff(staffId);
                        }
                    } else if (column == 6) { // View details icon clicked
                        viewStaffDetails(staffId);
                    }
                }
            }
        });
    }

    /**
     * Setup table appearance.
     */
    private void setupTableAppearance() {
        // Add custom cell renderers for the icon columns
        for (int i = 4; i <= 6; i++) {
            tblStaff.getColumnModel().getColumn(i).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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
        tblStaff.getTableHeader().setPreferredSize(new Dimension(tblStaff.getTableHeader().getPreferredSize().width, 40));
        
        tblStaff.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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
        tblStaff.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblStaff.getColumnModel().getColumn(0).setPreferredWidth(100);  
        tblStaff.getColumnModel().getColumn(1).setPreferredWidth(670);
        tblStaff.getColumnModel().getColumn(2).setPreferredWidth(200);  
        tblStaff.getColumnModel().getColumn(3).setPreferredWidth(200);
        tblStaff.getColumnModel().getColumn(4).setPreferredWidth(80);  
        tblStaff.getColumnModel().getColumn(5).setPreferredWidth(80);
        tblStaff.getColumnModel().getColumn(6).setPreferredWidth(80);
    }

    /**
     * Load initial staff data.
     */
    private void loadStaffData() {
        try {
            List<Staff> staffList = staffController.getAllStaff();
            renderStaffTable(staffList);
        } catch (SQLException ex) {
            Logger.getLogger(StaffPanel.class.getName()).log(Level.SEVERE, "Error loading staff data", ex);
            JOptionPane.showMessageDialog(this, "Error loading staff data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Search for staff members.
     * 
     * @param keyword The search keyword
     */
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
            Logger.getLogger(StaffPanel.class.getName()).log(Level.SEVERE, "Database error", ex);
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Render the staff table with the given list of staff members.
     * 
     * @param staffs The list of staff members to display
     */
    private void renderStaffTable(List<Staff> staffs) {
        DefaultTableModel model = (DefaultTableModel) tblStaff.getModel();
        model.setRowCount(0);
        staffs.forEach(staff -> {
            model.addRow(new Object[]{
                    staff.getId(),
                    staff.getName(),
                    staff.getPosition(),
                    staff.getUserName(),
                    getImageIcon("src/img/edit.png"),
                    getImageIcon("src/img/delete.png"),
                    getImageIcon("src/img/view.png")
            });
        });
        tblStaff.revalidate();
        tblStaff.repaint();
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
                ImageIcon icon = new ImageIcon(Paths.get(imagePath).toAbsolutePath().toString());
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            } catch (Exception e) {
                Logger.getLogger(StaffPanel.class.getName()).log(Level.WARNING, "Error loading image: " + imagePath, e);
            }
        }
        return null;
    }

    /**
     * Delete a staff member.
     * 
     * @param staffId The ID of the staff member to delete
     */
    private void deleteStaff(int staffId) {
        try {
            boolean deleted = staffController.deleteStaff(staffId);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Staff deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Refresh the table
                loadStaffData();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Staff could not be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(StaffPanel.class.getName()).log(Level.SEVERE, "Database error", ex);
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Open the staff edit form.
     * 
     * @param row The row in the table
     */
    private void editStaff(int row) {
        try {
            int staffId = (int) tblStaff.getValueAt(row, 0);
            String name = (String) tblStaff.getValueAt(row, 1);
            String position = (String) tblStaff.getValueAt(row, 2);
            String userName = (String) tblStaff.getValueAt(row, 3);
            
            // Get the full staff object to get all data
            Staff staff = staffController.getStaffById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
            
            // Create and show the edit form
            JFrame editFrame = new JFrame("Edit Staff");
            StaffFormPanel editPanel = new StaffFormPanel(staffId, name, position, userName, staff.getRole(), this);
            editFrame.getContentPane().add(editPanel);
            editFrame.pack();
            editFrame.setLocationRelativeTo(this);
            editFrame.setVisible(true);
            
            // Add window listener to refresh data when closed
            editFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadStaffData();
                }
            });
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error editing staff: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(StaffPanel.class.getName()).log(Level.SEVERE, "Database error", ex);
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Open the staff add form.
     */
    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {
        JFrame addFrame = new JFrame("Add Staff");
        StaffFormPanel addPanel = new StaffFormPanel(this);
        addFrame.getContentPane().add(addPanel);
        addFrame.pack();
        addFrame.setLocationRelativeTo(this);
        addFrame.setVisible(true);
        
        // Add window listener to refresh data when closed
        addFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loadStaffData();
            }
        });
    }

    /**
     * View staff details.
     * 
     * @param staffId The ID of the staff member
     */
    private void viewStaffDetails(int staffId) {
        try {
            // Create and register the staff details panel
            StaffDetailsPanel detailsPanel = new StaffDetailsPanel(staffId, router);
            
            // Navigate to it
            String detailsPath = "/staff/" + staffId;
            if (!router.hasRoute(detailsPath)) {
                router.register(detailsPath, detailsPanel);
            }
            router.navigate(detailsPath);
        } catch (Exception ex) {
            Logger.getLogger(StaffPanel.class.getName()).log(Level.SEVERE, "Error viewing staff details", ex);
            JOptionPane.showMessageDialog(this, "Error viewing staff details: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {
        setBackground(new Color(255, 255, 255));
        setLayout(new BorderLayout(10, 10));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(new Color(255, 255, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Title and add button panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(255, 255, 255));
        
        JLabel titleLabel = new JLabel("Staff Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        
        btnAdd = new JLabel(getImageIcon("src/img/add.png"));
        btnAdd.setText("Add New Staff");
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
        tblStaff = new JTable();
        tblStaff.setModel(new DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Name", "Position", "Username", "", "", ""}
        ));
        tblStaff.setRowHeight(50);
        tblStaff.setShowGrid(false);
        tblStaff.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tblStaff.setFillsViewportHeight(true);
        
        JScrollPane scrollPane = new JScrollPane(tblStaff);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Variables declaration
    private JLabel btnAdd;
    private JTable tblStaff;
    private JTextField txtSearch;
} 