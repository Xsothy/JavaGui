package View.Dashboard;

import Controller.StaffController;
import Model.Staff;
import Support.Router;
import Support.SessionManager;
import Support.UIConstants;
import View.DashboardPanel;
import View.Layout.DashboardLayout;
import View.NavigatePanel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for viewing and managing staff members.
 */
public class StaffPanel extends DashboardLayout {
    private final StaffController staffController;
    private JTable staffTable;
    private JTextField searchField;


    public StaffPanel() {
        super();
        this.staffController = new StaffController();
    }


    @Override
    public void render()
    {
        super.render();
        loadStaff();
    }

    @Override
    public NavigatePanel getContentPanel() {
        NavigatePanel contentPanel = new NavigatePanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        contentPanel.setBackground(Color.WHITE);

        // Create header panel with title and add button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.BORDER_COLOR),
                BorderFactory.createEmptyBorder(
                        UIConstants.CONTENT_PADDING,
                        UIConstants.CONTENT_PADDING,
                        UIConstants.CONTENT_PADDING,
                        UIConstants.CONTENT_PADDING
                )
        ));

        // Left side of header: Title and subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Staff Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Add, edit or delete staff records");
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

        JLabel searchIcon = new JLabel("");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        searchField = new JTextField(15);
        searchField.setBorder(null);
        searchField.setFont(UIConstants.TABLE_CONTENT_FONT);

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Add button
        JButton addButton = new JButton("+ Add Staff");
        addButton.setFont(UIConstants.BUTTON_FONT);
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(UIConstants.PRIMARY_COLOR);
        addButton.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.BUTTON_PADDING_V,
                UIConstants.BUTTON_PADDING_H,
                UIConstants.BUTTON_PADDING_V,
                UIConstants.BUTTON_PADDING_H
        ));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // TODO:: Implement Policy
        Staff user = SessionManager.getCurrentUser();
        if (user != null) {
            addButton.setEnabled(user.getRole().equals("admin"));
        }

        // Add hover effect
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(UIConstants.ACCENT_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(UIConstants.PRIMARY_COLOR);
            }
        });

        addButton.addActionListener(e -> Router.navigate("dashboard/staffs/add"));

        actionsPanel.add(searchPanel);
        actionsPanel.add(addButton);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(actionsPanel, BorderLayout.EAST);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel with table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.SECTION_SPACING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING
        ));

        // Create the staff table
        staffTable = new JTable();
        staffTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        staffTable.setShowGrid(true);
        staffTable.setGridColor(UIConstants.BORDER_COLOR);
        staffTable.setSelectionBackground(new Color(235, 245, 255));
        staffTable.setSelectionForeground(UIConstants.TEXT_COLOR);
        staffTable.setFont(UIConstants.TABLE_CONTENT_FONT);
        staffTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        staffTable.setFillsViewportHeight(true);

        // Style the header
        staffTable.getTableHeader().setFont(UIConstants.TABLE_HEADER_FONT);
        staffTable.getTableHeader().setBackground(UIConstants.PRIMARY_COLOR);
        staffTable.getTableHeader().setForeground(Color.WHITE);
        staffTable.getTableHeader().setPreferredSize(
                new Dimension(staffTable.getTableHeader().getPreferredSize().width, UIConstants.TABLE_HEADER_HEIGHT)
        );

        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Add search listener
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = searchField.getText().trim();
                if (searchText.isEmpty()) {
                    loadStaff();
                } else {
                    searchStaff(searchText);
                }
            }
        });
        return contentPanel;
    }

    /**
     * Create the staff management content panel.
     * 
     * @return The staff content panel
     */
    private JPanel createStaffContent() {
        JPanel staffContent = new JPanel(new BorderLayout(0, 0));
        staffContent.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        staffContent.setBackground(Color.WHITE);

        // Create header panel with title and add button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.BORDER_COLOR),
                BorderFactory.createEmptyBorder(
                        UIConstants.CONTENT_PADDING,
                        UIConstants.CONTENT_PADDING,
                        UIConstants.CONTENT_PADDING,
                        UIConstants.CONTENT_PADDING
                )
        ));

        // Left side of header: Title and subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Staff Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Add, edit or delete staff records");
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

        JLabel searchIcon = new JLabel("");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        searchField = new JTextField(15);
        searchField.setBorder(null);
        searchField.setFont(UIConstants.TABLE_CONTENT_FONT);

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Add button
        JButton addButton = new JButton("+ Add Staff");
        addButton.setFont(UIConstants.BUTTON_FONT);
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(UIConstants.PRIMARY_COLOR);
        addButton.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.BUTTON_PADDING_V,
                UIConstants.BUTTON_PADDING_H,
                UIConstants.BUTTON_PADDING_V,
                UIConstants.BUTTON_PADDING_H
        ));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // TODO:: Implement Policy
        Staff user = SessionManager.getCurrentUser();
        if (user != null) {
            addButton.setEnabled(user.getRole().equals("admin"));
        }

        // Add hover effect
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(UIConstants.ACCENT_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(UIConstants.PRIMARY_COLOR);
            }
        });

        addButton.addActionListener(e -> Router.navigate("dashboard/staffs/add"));

        actionsPanel.add(searchPanel);
        actionsPanel.add(addButton);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(actionsPanel, BorderLayout.EAST);

        staffContent.add(headerPanel, BorderLayout.NORTH);

        // Content panel with table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.SECTION_SPACING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING
        ));

        // Create the staff table
        staffTable = new JTable();
        staffTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        staffTable.setShowGrid(true);
        staffTable.setGridColor(UIConstants.BORDER_COLOR);
        staffTable.setSelectionBackground(new Color(235, 245, 255));
        staffTable.setSelectionForeground(UIConstants.TEXT_COLOR);
        staffTable.setFont(UIConstants.TABLE_CONTENT_FONT);
        staffTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        staffTable.setFillsViewportHeight(true);

        // Style the header
        staffTable.getTableHeader().setFont(UIConstants.TABLE_HEADER_FONT);
        staffTable.getTableHeader().setBackground(UIConstants.PRIMARY_COLOR);
        staffTable.getTableHeader().setForeground(Color.WHITE);
        staffTable.getTableHeader().setPreferredSize(
                new Dimension(staffTable.getTableHeader().getPreferredSize().width, UIConstants.TABLE_HEADER_HEIGHT)
        );

        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        staffContent.add(tablePanel, BorderLayout.CENTER);

        // Add search listener
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = searchField.getText().trim();
                if (searchText.isEmpty()) {
                    loadStaff();
                } else {
                    searchStaff(searchText);
                }
            }
        });
        
        return staffContent;
    }

    /**
     * Load the staff from the database.
     */
    private void loadStaff() {
        List<Staff> staff = staffController.getAllStaff();
        updateTableModel(staff);
    }

    /**
     * Search for staff matching the search text.
     *
     * @param searchText The text to search for
     */
    private void searchStaff(String searchText) {
        List<Staff> staffList = staffController.getAllStaff();

        if (searchText != null && !searchText.trim().isEmpty()) {
            String searchTerm = searchText.toLowerCase().trim();

            // Filter the staff list based on the search term
            staffList = staffList.stream()
                    .filter(staff ->
                            staff.getName().toLowerCase().contains(searchTerm) ||
                                    staff.getPosition().toLowerCase().contains(searchTerm) ||
                                    staff.getUserName().toLowerCase().contains(searchTerm) ||
                                    staff.getRole().toLowerCase().contains(searchTerm))
                    .toList();
        }

        updateTableModel(staffList);
    }

    /**
     * Update the table model with the provided staff.
     *
     * @param staffList The list of staff to display
     */
    private void updateTableModel(List<Staff> staffList) {
        // Create the table model
        String[] columnNames = {"ID", "Name", "Position", "Username", "Actions"};
        Object[][] data = new Object[staffList.size()][columnNames.length];

        // Populate the data
        for (int i = 0; i < staffList.size(); i++) {
            Staff staff = staffList.get(i);
            data[i][0] = staff.getId();
            data[i][1] = staff.getName();
            data[i][2] = staff.getPosition();
            data[i][3] = staff.getUserName();
            data[i][4] = ""; // Will be replaced with buttons
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only make the actions column editable
            }
        };

        staffTable.setModel(model);

        // Set relative column widths as percentages
        int totalWidth = staffTable.getParent().getWidth();
        if (totalWidth > 0) {
            staffTable.getColumnModel().getColumn(0).setPreferredWidth((int)(totalWidth * 0.05));  // ID (5%)
            staffTable.getColumnModel().getColumn(1).setPreferredWidth((int)(totalWidth * 0.25));  // Name (30%)
            staffTable.getColumnModel().getColumn(2).setPreferredWidth((int)(totalWidth * 0.20));  // Position (25%)
            staffTable.getColumnModel().getColumn(3).setPreferredWidth((int)(totalWidth * 0.20));  // Username (25%)
            staffTable.getColumnModel().getColumn(4).setPreferredWidth((int)(totalWidth * 0.30));  // Actions (15%)
        }

        // Set the action buttons in the last column
        staffTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonsRenderer());
        staffTable.getColumnModel().getColumn(4).setCellEditor(new ButtonsEditor());

        // Center the ID column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        staffTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
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
                loadStaff();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Staff could not be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * A simplified renderer for buttons in a table cell.
     */
    private static class ButtonsRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JButton viewButton;
        private final JButton editButton;
        private final JButton deleteButton;

        public ButtonsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);

            // View button
            viewButton = new JButton("View");
            viewButton.setForeground(Color.WHITE);
            viewButton.setBackground(UIConstants.INFO_COLOR);
            viewButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            viewButton.setFocusPainted(false);
            viewButton.setFont(UIConstants.SMALL_BUTTON_FONT);

            // Edit button
            editButton = new JButton("Edit");
            editButton.setForeground(Color.WHITE);
            editButton.setBackground(UIConstants.PRIMARY_COLOR);
            editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            editButton.setFocusPainted(false);
            editButton.setFont(UIConstants.SMALL_BUTTON_FONT);

            // Delete button
            deleteButton = new JButton("Delete");
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(UIConstants.DANGER_COLOR);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            deleteButton.setFocusPainted(false);
            deleteButton.setFont(UIConstants.SMALL_BUTTON_FONT);

            add(viewButton);
            add(editButton);
            add(deleteButton);

            // TODO: IMPLEMENT POLICY
            Staff user = SessionManager.getCurrentUser();

            deleteButton.setEnabled(!user.getRole().equals("admin"));

        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }

            int height = table.getRowHeight(row);
            int buttonHeight = editButton.getPreferredSize().height;
            int padding = Math.max(0, (height - buttonHeight) / 2);
            setBorder(BorderFactory.createEmptyBorder(padding, 0, padding, 0));

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

            Staff user = SessionManager.getCurrentUser();

            deleteButton.setEnabled(user.getRole().equals("admin"));
            previewButton.setEnabled((user.getRole().equals("admin") || user.getId() == staffId));
            editButton.setEnabled((user.getRole().equals("admin") || user.getId() == staffId));
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
                Router.navigate("dashboard/staffs/" + staffId);
            } catch (Exception ex) {
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
                Router.navigate("dashboard/staffs/edit/" + staffId);
            } catch (Exception ex) {
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

            staffId = (int) table.getValueAt(row, 0);

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