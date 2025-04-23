package View.Dashboard;

import Controller.StaffController;
import Model.ExpenseWithStaff;
import Model.Staff;
import Model.StaffWithExpenses;
import Support.Router;
import Support.UIConstants;
import View.Layout.DashboardLayout;
import View.NavigatePanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for displaying staff details with their expenses.
 */
public class StaffDetailsPanel extends DashboardLayout {
    private final StaffController staffController;
    private final Staff staff;
    private NavigatePanel contentPanel;
    private JPanel mainPanel;
    private JPanel expensePanel;

    /**
     * Creates a new StaffDetailsPanel.
     *
     * @param staff staff member to display
     */
    public StaffDetailsPanel(Staff staff) {
        super();
        this.staffController = new StaffController();
        this.staff = staff;
    }

    @Override
    public void render() {
        super.render();
        loadStaffData();
    }

    @Override
    public NavigatePanel getContentPanel() {
        // Create a basic structure panel without data
        contentPanel = new NavigatePanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        // Create a header with title and back button
        JPanel headerPanel = setupHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Create main content panel
        mainPanel = new JPanel(new BorderLayout(UIConstants.SECTION_SPACING, UIConstants.SECTION_SPACING));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SECTION_SPACING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING
        ));
        
        // Add a loading indicator initially
        JLabel loadingLabel = new JLabel("Loading staff data...", JLabel.CENTER);
        loadingLabel.setFont(UIConstants.SUBTITLE_FONT);
        loadingLabel.setForeground(UIConstants.LIGHT_TEXT_COLOR);
        mainPanel.add(loadingLabel, BorderLayout.CENTER);
        
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        
        return contentPanel;
    }

    /**
     * Load the staff data and update the panel.
     */
    private void loadStaffData() {
        if (contentPanel == null || mainPanel == null) {
            return; // Not initialized yet
        }
        
        try {
            StaffWithExpenses staffWithExpenses = 
                staffController.getStaffWithExpensesById(staff.getId())
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
            
            // Clear the main panel
            mainPanel.removeAll();
            
            // Create the staff info panel
            JPanel staffInfoPanel = setupStaffDetailsPanel(staffWithExpenses.getStaff());
            mainPanel.add(staffInfoPanel, BorderLayout.NORTH);
            
            // Create expense table panel with title
            expensePanel = new JPanel(new BorderLayout(10, 10));
            expensePanel.setBackground(Color.WHITE);
            expensePanel.setBorder(BorderFactory.createEmptyBorder(UIConstants.SECTION_SPACING, 0, 0, 0));
            
            // Create a title panel with heading and stats
            JPanel expenseTitlePanel = new JPanel(new BorderLayout());
            expenseTitlePanel.setBackground(Color.WHITE);
            expenseTitlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            
            JLabel expensesTitle = new JLabel("Expenses");
            expensesTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            expensesTitle.setForeground(UIConstants.TEXT_COLOR);
            
            // Stats panel showing counts
            JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
            statsPanel.setBackground(Color.WHITE);
            
            JLabel totalCountLabel = new JLabel("Total: " + staffWithExpenses.getExpenseCount() + " expenses");
            totalCountLabel.setFont(UIConstants.SUBTITLE_FONT);
            totalCountLabel.setForeground(UIConstants.LIGHT_TEXT_COLOR);
            
            JLabel totalAmountLabel = new JLabel("Amount: $" + staffWithExpenses.getTotalExpenseAmount());
            totalAmountLabel.setFont(UIConstants.BUTTON_FONT);
            totalAmountLabel.setForeground(UIConstants.TEXT_COLOR);
            
            statsPanel.add(totalCountLabel);
            statsPanel.add(totalAmountLabel);
            
            expenseTitlePanel.add(expensesTitle, BorderLayout.WEST);
            expenseTitlePanel.add(statsPanel, BorderLayout.EAST);
            
            expensePanel.add(expenseTitlePanel, BorderLayout.NORTH);
            
            // Create the expense table
            JTable expenseTable = createExpenseTable(staffWithExpenses);
            JScrollPane scrollPane = new JScrollPane(expenseTable);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(Color.WHITE);
            
            expensePanel.add(scrollPane, BorderLayout.CENTER);
            
            mainPanel.add(expensePanel, BorderLayout.CENTER);
            
            // Refresh the UI
            contentPanel.revalidate();
            contentPanel.repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
            // Show error message
            mainPanel.removeAll();
            JPanel errorPanel = new JPanel(new BorderLayout());
            errorPanel.setBackground(Color.WHITE);
            
            JLabel errorLabel = new JLabel("Error loading staff data: " + e.getMessage(), JLabel.CENTER);
            errorLabel.setFont(UIConstants.SUBTITLE_FONT);
            errorLabel.setForeground(UIConstants.DANGER_COLOR);
            errorPanel.add(errorLabel, BorderLayout.CENTER);
            
            JButton retryButton = new JButton("Retry");
            retryButton.setFont(UIConstants.BUTTON_FONT);
            retryButton.setForeground(Color.WHITE);
            retryButton.setBackground(UIConstants.PRIMARY_COLOR);
            retryButton.addActionListener(evt -> loadStaffData());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.add(retryButton);
            errorPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            mainPanel.add(errorPanel, BorderLayout.CENTER);
            
            // Refresh the UI
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    /**
     * Setup the header panel.
     *
     * @return The header panel
     */
    private JPanel setupHeaderPanel() {
        // Header panel with card-like appearance
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(UIConstants.CONTENT_PADDING, UIConstants.CONTENT_PADDING, UIConstants.CONTENT_PADDING, UIConstants.CONTENT_PADDING)
        ));

        // Back button
        JButton btnBack = new JButton("Back to Staff List");
        btnBack.setFont(UIConstants.BUTTON_FONT);
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(UIConstants.PRIMARY_COLOR);
        btnBack.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        btnBack.setFocusPainted(false);

        // Add hover effect
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(UIConstants.ACCENT_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(UIConstants.PRIMARY_COLOR);
            }
        });

        btnBack.addActionListener(e -> {
            Router.navigate("dashboard/staffs");
        });

        headerPanel.add(btnBack, BorderLayout.WEST);

        return headerPanel;
    }

    /**
     * Setup the staff details panel.
     *
     * @return The staff details panel
     */
    private JPanel setupStaffDetailsPanel(Staff staff) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout(20, 20));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING
        ));

        // Panel for staff information
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout(20, 0));
        infoPanel.setBackground(Color.WHITE);

        // Staff details in the form of a grid
        JPanel detailsGrid = new JPanel(new GridLayout(0, 2, 10, 15));
        detailsGrid.setBackground(Color.WHITE);

        // Title for staff details
        JLabel nameLabel = new JLabel(staff.getName());
        nameLabel.setFont(UIConstants.TITLE_FONT);
        nameLabel.setForeground(UIConstants.TEXT_COLOR);

        JLabel positionLabel = new JLabel(staff.getPosition());
        positionLabel.setFont(UIConstants.SUBTITLE_FONT);
        positionLabel.setForeground(UIConstants.ACCENT_COLOR);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        positionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(nameLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(positionLabel);

        // Add fields to the grid
        addDetailRow(detailsGrid, "Staff ID:", String.valueOf(staff.getId()));
        addDetailRow(detailsGrid, "Username:", staff.getUserName());
        addDetailRow(detailsGrid, "Role:", staff.getRole());

        JPanel staffInfoPanel = new JPanel();
        staffInfoPanel.setLayout(new BoxLayout(staffInfoPanel, BoxLayout.Y_AXIS));
        staffInfoPanel.setBackground(Color.WHITE);
        staffInfoPanel.add(titlePanel);
        staffInfoPanel.add(detailsGrid);

        infoPanel.add(staffInfoPanel, BorderLayout.CENTER);

        // Add edit button if appropriate (depending on user role/permissions)
        JButton editButton = new JButton("Edit Staff Member");
        editButton.setFont(UIConstants.BUTTON_FONT);
        editButton.setForeground(Color.WHITE);
        editButton.setBackground(UIConstants.PRIMARY_COLOR);
        editButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        editButton.setFocusPainted(false);
        editButton.addActionListener(e -> Router.navigate("dashboard/staffs/edit/" + staff.getId()));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(editButton);

        detailsPanel.add(infoPanel, BorderLayout.CENTER);
        detailsPanel.add(buttonPanel, BorderLayout.SOUTH);

        return detailsPanel;
    }

    /**
     * Add a row to the details grid.
     *
     * @param panel The panel to add the row to
     * @param labelText The label text
     * @param valueText The value text
     */
    private void addDetailRow(JPanel panel, String labelText, String valueText) {
        JLabel label = new JLabel(labelText);
        label.setFont(UIConstants.SUBTITLE_FONT);
        label.setForeground(UIConstants.LIGHT_TEXT_COLOR);
        
        JLabel value = new JLabel(valueText);
        value.setFont(UIConstants.TABLE_CONTENT_FONT);
        value.setForeground(UIConstants.TEXT_COLOR);
        
        panel.add(label);
        panel.add(value);
    }

    /**
     * Create the expense table.
     *
     * @param staffWithExpenses The staff with expenses data
     * @return The expense table
     */
    private JTable createExpenseTable(StaffWithExpenses staffWithExpenses) {
        // Create table model
        String[] columnNames = {"ID", "Name", "Description", "Amount", "Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        // Populate with data
        for (ExpenseWithStaff expense : staffWithExpenses.getExpenses()) {
            model.addRow(new Object[]{
                expense.getExpense().getId(),
                expense.getExpense().getName(),
                expense.getExpense().getDescription(),
                "$" + expense.getExpense().getAmount(),
                expense.getExpense().getDate()
            });
        }
        
        // Create and configure table
        JTable table = new JTable(model);
        table.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        table.setShowGrid(true);
        table.setGridColor(UIConstants.BORDER_COLOR);
        table.setSelectionBackground(UIConstants.TABLE_SELECTION_BG_COLOR);
        table.setSelectionForeground(UIConstants.TABLE_SELECTION_FG_COLOR);
        table.setFont(UIConstants.TABLE_CONTENT_FONT);
        
        // Style header
        table.getTableHeader().setFont(UIConstants.TABLE_HEADER_FONT);
        table.getTableHeader().setBackground(UIConstants.TABLE_HEADER_BG_COLOR);
        table.getTableHeader().setForeground(UIConstants.TABLE_HEADER_FG_COLOR);
        table.getTableHeader().setPreferredSize(
            new Dimension(table.getTableHeader().getPreferredSize().width, UIConstants.TABLE_HEADER_HEIGHT)
        );
        
        // Set column widths
        int totalWidth = table.getParent() != null ? table.getParent().getWidth() : 800;
        table.getColumnModel().getColumn(0).setPreferredWidth((int)(totalWidth * 0.1));  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth((int)(totalWidth * 0.25)); // Name
        table.getColumnModel().getColumn(2).setPreferredWidth((int)(totalWidth * 0.35)); // Description
        table.getColumnModel().getColumn(3).setPreferredWidth((int)(totalWidth * 0.15)); // Amount
        table.getColumnModel().getColumn(4).setPreferredWidth((int)(totalWidth * 0.15)); // Date
        
        // Center align ID and Amount columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        return table;
    }
} 