package Components;

import Controller.StaffController;
import Model.ExpenseWithStaff;
import Model.Staff;
import Repository.StaffRepository;
import Support.Router;
import Support.UIConstants;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Panel for displaying staff details with their expenses.
 */
public class StaffDetailsPanel extends JPanel {
    private final StaffController staffController;
    private final Staff staff;
    private final Router router;

    /**
     * Creates a new StaffDetailsPanel.
     *
     * @param staff staff member to display
     * @param router The router for navigation
     */
    public StaffDetailsPanel(Staff staff, Router router) {
        this.staffController = new StaffController();
        this.staff = staff;
        this.router = router;

        initComponents();
        loadStaffData();
    }

    /**
     * Initialize the components.
     */
    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setBackground(Color.WHITE);

        // Add a loading label initially
        JLabel loadingLabel = new JLabel("Loading staff data...");
        loadingLabel.setHorizontalAlignment(JLabel.CENTER);
        loadingLabel.setFont(UIConstants.TITLE_FONT);
        loadingLabel.setForeground(UIConstants.TEXT_COLOR);
        add(loadingLabel, BorderLayout.CENTER);
    }

    /**
     * Load the staff data.
     */
    private void loadStaffData() {
        try {
            StaffRepository.StaffWithExpenses staffWithExpenses =
                    staffController.getStaffWithExpensesById(staff.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

            // Clear the panel
            removeAll();

            // Create a header with title and back button
            JPanel headerPanel = setupHeaderPanel();
            add(headerPanel, BorderLayout.NORTH);

            // Create main content panel
            JPanel contentPanel = new JPanel(new BorderLayout(UIConstants.SECTION_SPACING, UIConstants.SECTION_SPACING));
            contentPanel.setBackground(Color.WHITE);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.SECTION_SPACING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING
            ));

            // Create the staff info panel
            JPanel staffInfoPanel = setupStaffDetailsPanel(staffWithExpenses.getStaff());
            contentPanel.add(staffInfoPanel, BorderLayout.NORTH);

            // Create expense table panel with title
            JPanel expensePanel = new JPanel(new BorderLayout(10, 10));
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

            contentPanel.add(expensePanel, BorderLayout.CENTER);

            add(contentPanel, BorderLayout.CENTER);

            revalidate();
            repaint();
        } catch (SQLException | IllegalArgumentException ex) {
            Logger.getLogger(StaffDetailsPanel.class.getName()).log(Level.SEVERE, "Error loading staff data", ex);
            JOptionPane.showMessageDialog(this, "Error loading staff data: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
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
            router.navigate("/staffs");
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

        detailsPanel.add(infoPanel, BorderLayout.NORTH);

        return detailsPanel;
    }

    /**
     * Add a label pair to a panel.
     *
     * @param panel The panel to add to
     * @param labelText The label text
     * @param valueText The value text
     */
    private void addDetailRow(JPanel panel, String labelText, String valueText) {
        JLabel label = new JLabel(labelText);
        label.setFont(UIConstants.BUTTON_FONT);
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
     * @param staffWithExpenses The staff member with expenses
     * @return The expense table
     */
    private JTable createExpenseTable(StaffRepository.StaffWithExpenses staffWithExpenses) {
        // Create the table model
        String[] columnNames = {"ID", "Name", "Description", "Amount"};
        Object[][] data = new Object[staffWithExpenses.getExpenses().size()][columnNames.length];

        // Add the expenses to the table
        for (int i = 0; i < staffWithExpenses.getExpenses().size(); i++) {
            ExpenseWithStaff expense = staffWithExpenses.getExpenses().get(i);
            data[i][0] = expense.getExpense().getId();
            data[i][1] = expense.getExpense().getName();
            data[i][2] = expense.getExpense().getDescription();
            data[i][3] = expense.getExpense().getAmount();
        }

        // Create the table
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        table.setShowGrid(true);
        table.setGridColor(UIConstants.BORDER_COLOR);
        table.setSelectionBackground(new Color(235, 245, 255));
        table.setSelectionForeground(UIConstants.TEXT_COLOR);
        table.setFont(UIConstants.TABLE_CONTENT_FONT);

        // Use AUTO_RESIZE_ALL_COLUMNS to make the table fill the container width
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Style the header
        table.getTableHeader().setFont(UIConstants.TABLE_HEADER_FONT);
        table.getTableHeader().setBackground(UIConstants.PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getPreferredSize().width, UIConstants.TABLE_HEADER_HEIGHT));

        // Set relative column widths as percentages
//        int totalWidth = table.getParent().getWidth();
//        if (totalWidth > 0) {
//            table.getColumnModel().getColumn(0).setPreferredWidth((int)(totalWidth * 0.10));  // ID (10%)
//            table.getColumnModel().getColumn(1).setPreferredWidth((int)(totalWidth * 0.25));  // Name (25%)
//            table.getColumnModel().getColumn(2).setPreferredWidth((int)(totalWidth * 0.45));  // Description (45%)
//            table.getColumnModel().getColumn(3).setPreferredWidth((int)(totalWidth * 0.20));  // Amount (20%)
//        }

        // Center the ID and Amount columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        return table;
    }
} 