package Components;

import Controller.StaffController;
import Model.Staff;
import Support.Router;
import Support.UIConstants;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for viewing and managing staff members.
 */
public class StaffPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(StaffPanel.class.getName());
    private final StaffController staffController;
    private final Router router;
    private JButton btnAdd;
    private JTable tblStaff;
    private JTextField txtSearch;
    private JScrollPane scrollPane;

    /**
     * Creates a new StaffPanel.
     *
     * @param router The router for navigation
     */
    public StaffPanel(Router router) {
        staffController = new StaffController();
        this.router = router;

        initComponents();
        setupListeners();
        // We'll load data after components are initialized
    }

    /**
     * Setup component listeners.
     */
    private void setupListeners() {
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnAdd.addActionListener(e -> router.navigate("/staffs/add"));

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

        // Add mouse listener to handle double-click for viewing details
        tblStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblStaff.rowAtPoint(e.getPoint());

                if (row >= 0 && e.getClickCount() == 2) {
                    // Double click to view details
                    int staffId = (int) tblStaff.getValueAt(row, 0);
                    router.navigate("/staff/" + staffId);
                }
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();

        // This ensures that data is loaded after the component is fully realized
        SwingUtilities.invokeLater(this::initializeTable);
    }

    /**
     * Initialize the table in a safe way, ensuring all steps occur in the right order
     */
    private void initializeTable() {
        try {
            // Load data first
            List<Staff> staffList = staffController.getAllStaff();

            // Set a standard model with our column definitions - no Actions column for now
            DefaultTableModel model = new DefaultTableModel(
                new Object[][] {},
                new String[] {"ID", "Name", "Position", "Username"}
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // No editable cells
                }
                
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    if (columnIndex == 0) return Integer.class;
                    return String.class;
                }
            };
            
            // Add data rows
            for (Staff staff : staffList) {
                model.addRow(new Object[] {
                    staff.getId(),
                    staff.getName(),
                    staff.getPosition(),
                    staff.getUserName()
                });
            }
            
            // Set the model to the table
            tblStaff.setModel(model);
            
            // Very basic appearance setup
            tblStaff.setRowHeight(35);
            tblStaff.setShowGrid(true);
            tblStaff.setGridColor(Color.LIGHT_GRAY);
            
            // Simple center renderer
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            
            for (int i = 0; i < tblStaff.getColumnCount(); i++) {
                tblStaff.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            
            // Add double-click event listener for viewing staff details
            tblStaff.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = tblStaff.getSelectedRow();
                        if (row >= 0) {
                            int staffId = (int) tblStaff.getValueAt(row, 0);
                            try {
                                router.navigate("/staff/" + staffId);
                            } catch (Exception ex) {
                                LOGGER.severe("Navigation error: " + ex.getMessage());
                            }
                        }
                    }
                }
            });

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading staff data", ex);
            JOptionPane.showMessageDialog(this,
                "Error loading staff data: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load staff data and refresh the table.
     */
    private void loadStaffData() {
        SwingUtilities.invokeLater(this::initializeTable);
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
            LOGGER.log(Level.SEVERE, "Database error", ex);
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Render the staff table with the given list of staff members.
     *
     * @param staffs The list of staff members to display
     */
    private void renderStaffTable(List<Staff> staffs) {
        try {
            // Create a new model with our column headers
            DefaultTableModel model = new DefaultTableModel(
                new Object[][] {},
                new String[] {"ID", "Name", "Position", "Username"}) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // No editable cells
                    }
                    
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        if (columnIndex == 0) return Integer.class;
                        return String.class;
                    }
                };
            
            // Add data rows
            for (Staff staff : staffs) {
                model.addRow(new Object[]{
                    staff.getId(),
                    staff.getName(),
                    staff.getPosition(),
                    staff.getUserName()
                });
            }
            
            // Set the model to the table
            tblStaff.setModel(model);
            
            // Basic styling directly here
            tblStaff.setRowHeight(35);
            tblStaff.setShowGrid(true);
            tblStaff.setGridColor(Color.LIGHT_GRAY);
            
            // Simple center renderer
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            
            for (int i = 0; i < tblStaff.getColumnCount(); i++) {
                tblStaff.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            
            // Make sure table repaints
            tblStaff.revalidate();
            tblStaff.repaint();
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error rendering staff table", ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(0, 0));

        // Header panel with card-like appearance
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(UIConstants.CONTENT_PADDING, UIConstants.CONTENT_PADDING, UIConstants.CONTENT_PADDING, UIConstants.CONTENT_PADDING)
        ));

        // Left side of header: Title and description
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Staff Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Manage your staff members");
        subtitleLabel.setFont(UIConstants.SUBTITLE_FONT);
        subtitleLabel.setForeground(UIConstants.LIGHT_TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);

        // Right side of header: Search and Add button
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setBackground(Color.WHITE);

        // Search field with icon
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel searchIcon = new JLabel(getImageIcon("src/img/search.png"));

        txtSearch = new JTextField(15);
        txtSearch.setBorder(null);
        txtSearch.setFont(UIConstants.TABLE_CONTENT_FONT);

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        // Add button
        btnAdd = new JButton("+ Add Staff");
        btnAdd.setFont(UIConstants.BUTTON_FONT);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(UIConstants.PRIMARY_COLOR);
        btnAdd.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        btnAdd.setFocusPainted(false);

        // Add hover effect
        btnAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnAdd.setBackground(UIConstants.ACCENT_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnAdd.setBackground(UIConstants.PRIMARY_COLOR);
            }
        });

        actionsPanel.add(searchPanel);
        actionsPanel.add(btnAdd);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(actionsPanel, BorderLayout.EAST);

        // Content panel with table
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SECTION_SPACING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING
        ));

        // Initialize the table with a simple model
        tblStaff = new JTable();
        tblStaff.setModel(new DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Name", "Position", "Username"}
        ));
        tblStaff.setFillsViewportHeight(true);
        tblStaff.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Make the JScrollPane look modern
        scrollPane = new JScrollPane(tblStaff);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Add both panels to the main panel
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
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
                Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
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
     * A simplified renderer for buttons in a table cell.
     */
    private class ButtonsRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JPanel buttonPanel;

        public ButtonsRenderer() {
            setLayout(new BorderLayout());
            setOpaque(true);
            
            buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            buttonPanel.setOpaque(true);
            
            // Add placeholder buttons that don't need to be functional
            JButton viewBtn = new JButton("View");
            JButton editBtn = new JButton("Edit");
            JButton deleteBtn = new JButton("Delete");
            
            // Simple styling
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setBackground(UIConstants.INFO_COLOR);
            viewBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            editBtn.setForeground(Color.WHITE);
            editBtn.setBackground(UIConstants.PRIMARY_COLOR);
            editBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setBackground(UIConstants.DANGER_COLOR);
            deleteBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            buttonPanel.add(viewBtn);
            buttonPanel.add(editBtn);
            buttonPanel.add(deleteBtn);
            
            add(buttonPanel, BorderLayout.CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                buttonPanel.setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
                buttonPanel.setBackground(table.getBackground());
            }
            
            return this;
        }
    }

    /**
     * An editor for buttons in a table cell.
     */
    private class ButtonsEditor extends DefaultCellEditor {
        private final JPanel panel;
        private final JButton previewButton;
        private final JButton editButton;
        private final JButton deleteButton;
        private int staffId;

        public ButtonsEditor() {
            super(new JCheckBox());
            
            panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);
            
            // Create simple buttons with minimal configuration to prevent buffer issues
            previewButton = createSimpleButton("View", UIConstants.INFO_COLOR);
            editButton = createSimpleButton("Edit", UIConstants.PRIMARY_COLOR);
            deleteButton = createSimpleButton("Delete", UIConstants.DANGER_COLOR);
            
            // Add action listeners with minimal code
            previewButton.addActionListener(e -> handlePreviewAction());
            editButton.addActionListener(e -> handleEditAction());
            deleteButton.addActionListener(e -> handleDeleteAction());
            
            panel.add(previewButton);
            panel.add(editButton);
            panel.add(deleteButton);
        }

        // Create helper methods to minimize code in the constructor
        private JButton createSimpleButton(String text, Color bgColor) {
            JButton button = new JButton(text);
            button.setForeground(Color.WHITE);
            button.setBackground(bgColor);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setFocusPainted(false);
            button.setFont(UIConstants.SMALL_BUTTON_FONT);
            return button;
        }

        // Handle preview action
        private void handlePreviewAction() {
            fireEditingStopped();
            if (staffId <= 0) {
                JOptionPane.showMessageDialog(panel, 
                    "Invalid staff ID. Cannot view staff details.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                router.navigate("/staff/" + staffId);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Navigation error", ex);
                JOptionPane.showMessageDialog(panel, 
                    "Error navigating to staff details: " + ex.getMessage(), 
                    "Navigation Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        // Handle edit action
        private void handleEditAction() {
            fireEditingStopped();
            if (staffId <= 0) {
                JOptionPane.showMessageDialog(panel, 
                    "Invalid staff ID. Cannot edit staff.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                router.navigate("/staff/edit/" + staffId);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Navigation error", ex);
                JOptionPane.showMessageDialog(panel, 
                    "Error navigating to staff edit: " + ex.getMessage(), 
                    "Navigation Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        // Handle delete action
        private void handleDeleteAction() {
            fireEditingStopped();
            
            int confirm = JOptionPane.showConfirmDialog(
                panel,
                "Are you sure you want to delete this staff member?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                deleteStaff(staffId);
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            // Safety check to avoid IndexOutOfBoundsException
            if (row < 0 || row >= table.getRowCount() || table.getValueAt(row, 0) == null) {
                return panel; // Return panel without setting staffId
            }

            try {
                staffId = (int) table.getValueAt(row, 0); // Get the ID from the first column
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error getting staff ID from table", ex);
                staffId = -1; // Set invalid ID
            }

            panel.setBackground(table.getSelectionBackground());
            
            // Center the panel vertically
            int height = table.getRowHeight(row);
            int buttonHeight = editButton.getPreferredSize().height;
            int padding = Math.max(0, (height - buttonHeight) / 2);
            panel.setBorder(BorderFactory.createEmptyBorder(padding, 0, padding, 0));
            
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
} 